package laki.webide.managers;

import android.content.Context;
import com.besome.sketch.beans.ProjectFileBean;
import com.besome.sketch.beans.ViewBean;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.lang.reflect.Type;

import laki.webide.core.LakiFiles;
import laki.webide.utility.FileUtil;
import laki.webide.utility.SketchwareUtil;

public class WebProjectStateManager {

    public static void saveProjectState(Context context, String sc_id, ProjectFileBean projectFile, ArrayList<ViewBean> viewBeans) {
        if (projectFile == null || viewBeans == null) return;

        String projectName = a.a.a.yB.c(a.a.a.lC.b(sc_id), "my_ws_name");
        String projectRoot = LakiFiles.getProjectRoot(projectName, sc_id, false);
        String tagsPath = LakiFiles.getPageHtmlTagsPath(projectRoot, projectFile.getXmlName());

        ArrayList<ViewBean> cleanViews = SketchwareUtil.sanitizeViewBeans(viewBeans);
        FileUtil.writeFile(tagsPath, new Gson().toJson(cleanViews));
        
        // Notify live preview server
        laki.webide.managers.LivePreviewServer.lastModifiedTime = System.currentTimeMillis();
    }

    public static ArrayList<ViewBean> loadProjectState(Context context, String sc_id, ProjectFileBean projectFile) {
        if (projectFile == null) return new ArrayList<>();

        String projectName = a.a.a.yB.c(a.a.a.lC.b(sc_id), "my_ws_name");
        String projectRoot = LakiFiles.getProjectRoot(projectName, sc_id, false);
        String tagsPath = LakiFiles.getPageHtmlTagsPath(projectRoot, projectFile.getXmlName());

        if (FileUtil.isExistFile(tagsPath)) {
            String json = FileUtil.readFile(tagsPath);
            if (json != null && !json.trim().isEmpty()) {
                try {
                    Type type = new TypeToken<ArrayList<ViewBean>>(){}.getType();
                    ArrayList<ViewBean> loadedViews = new Gson().fromJson(json, type);
                    ArrayList<ViewBean> cleanViews = SketchwareUtil.sanitizeViewBeans(loadedViews);
                    return cleanViews;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        return new ArrayList<>();
    }

    public static ArrayList<ViewBean> blockTreeToViewBeans(laki.webide.core.BlockPane pane) {
        ArrayList<ViewBean> list = new ArrayList<>();
        if (pane == null || pane.getRoot() == null) return list;

        int nextId = pane.getRoot().nextBlock;
        int index = 0;
        while (nextId != -1) {
            laki.webide.core.Block b = (laki.webide.core.Block) pane.findViewWithTag(nextId);
            if (b == null) break;
            traverseAndBuild(b, "root", ViewBean.VIEW_TYPE_LAYOUT_LINEAR, index++, list, pane);
            nextId = b.nextBlock;
        }
        return list;
    }

    private static void traverseAndBuild(laki.webide.core.Block block, String parentId, int parentType, int index, ArrayList<ViewBean> list, laki.webide.core.BlockPane pane) {
        if (block == null || block.mOpCode == null) return;

        if (block.mOpCode.startsWith("html_")) {
            ViewBean vb = new ViewBean();
            vb.id = block.attributes.getOrDefault("id", "widget_" + block.getTag().toString());
            vb.parent = parentId;
            vb.parentType = parentType;
            vb.index = index;
            vb.type = getHtmlTypeFromOpCode(block.mOpCode, block);
            vb.classNames = block.attributes.getOrDefault("class", "");
            vb.convert = ViewBean.getViewTypeName(vb.type);

            vb.parentAttributes.putAll(block.attributes);
            String tagName = block.mOpCode.substring(5);
            if (tagName.equals("h")) {
                Object tagArg = block.getArgValue(0);
                tagName = (tagArg != null) ? tagArg.toString() : "h1";
            }
            vb.parentAttributes.put("html_tag", tagName);

            list.add(vb);

            if (block.canHaveSubstack1() && block.subStack1 != -1) {
                int childId = block.subStack1;
                int childIndex = 0;
                while (childId != -1) {
                    laki.webide.core.Block childBlock = (laki.webide.core.Block) pane.findViewWithTag(childId);
                    if (childBlock == null) break;
                    traverseAndBuild(childBlock, vb.id, vb.type, childIndex++, list, pane);
                    childId = childBlock.nextBlock;
                }
            }
        }
    }

    public static void loadProjectStateIntoPane(laki.webide.core.BlockPane pane, ArrayList<ViewBean> viewBeans, android.view.View.OnTouchListener touchListener) {
        if (pane == null) return;

        pane.removeAllViews();
        pane.setupRoot("When Page Load", "onPageLoad", touchListener);

        if (viewBeans == null || viewBeans.isEmpty()) {
            pane.getRoot().fixLayout();
            pane.calculateWidthHeight();
            return;
        }

        java.util.HashMap<String, laki.webide.core.Block> createdBlocks = new java.util.HashMap<>();
        java.util.HashMap<String, ArrayList<ViewBean>> childrenMap = new java.util.HashMap<>();

        for (ViewBean vb : viewBeans) {
            if (vb.parent == null) continue;
            childrenMap.computeIfAbsent(vb.parent, k -> new ArrayList<>()).add(vb);
        }

        for (ArrayList<ViewBean> list : childrenMap.values()) {
            java.util.Collections.sort(list, (o1, o2) -> Integer.compare(o1.index, o2.index));
        }

        int initialBlockId = 1000;
        pane.blockId = initialBlockId;

        laki.webide.core.Block rootBlock = pane.getRoot();
        ArrayList<ViewBean> rootChildren = childrenMap.get("root");
        if (rootChildren != null) {
            laki.webide.core.Block lastBlock = null;
            for (int i = 0; i < rootChildren.size(); i++) {
                ViewBean vb = rootChildren.get(i);
                laki.webide.core.Block b = createBlockFromViewBean(pane, vb, touchListener);
                if (b == null) continue;

                createdBlocks.put(vb.id, b);

                if (i == 0) {
                    rootBlock.insertBlock(b);
                } else {
                    lastBlock.insertBlock(b);
                }
                lastBlock = b;
            }

            for (java.util.Map.Entry<String, laki.webide.core.Block> entry : createdBlocks.entrySet()) {
                attachChildren(entry.getValue(), entry.getKey(), childrenMap, createdBlocks, pane, touchListener);
            }
        }

        pane.getRoot().fixLayout();
        pane.calculateWidthHeight();
    }

    private static void attachChildren(laki.webide.core.Block parentBlock, String parentId, java.util.HashMap<String, ArrayList<ViewBean>> childrenMap, java.util.HashMap<String, laki.webide.core.Block> createdBlocks, laki.webide.core.BlockPane pane, android.view.View.OnTouchListener touchListener) {
        ArrayList<ViewBean> children = childrenMap.get(parentId);
        if (children == null || children.isEmpty()) return;

        laki.webide.core.Block lastBlock = null;
        for (int i = 0; i < children.size(); i++) {
            ViewBean vb = children.get(i);
            laki.webide.core.Block b = createBlockFromViewBean(pane, vb, touchListener);
            if (b == null) continue;

            createdBlocks.put(vb.id, b);

            if (i == 0) {
                parentBlock.insertBlockSub1(b);
            } else {
                lastBlock.insertBlock(b);
            }
            lastBlock = b;
        }

        for (ViewBean vb : children) {
            laki.webide.core.Block childBlock = createdBlocks.get(vb.id);
            if (childBlock != null) {
                attachChildren(childBlock, vb.id, childrenMap, createdBlocks, pane, touchListener);
            }
        }
    }

    private static laki.webide.core.Block createBlockFromViewBean(laki.webide.core.BlockPane pane, ViewBean vb, android.view.View.OnTouchListener touchListener) {
        String opCode = getOpCodeFromViewType(vb.type);
        laki.webide.core.CreateBlock cb = laki.webide.core.CreateBlock.getDefinition(opCode);
        if (cb == null) {
            for (laki.webide.core.CreateBlock b : laki.webide.core.html.HtmlBlocks.getAllHtmlBlocks()) {
                if (b.opCode.equals(opCode)) {
                    cb = b;
                    break;
                }
            }
        }

        if (cb == null) return null;

        int newId = pane.blockId++;
        laki.webide.core.Block b = new laki.webide.core.Block(pane.getContext(), newId, cb.spec, cb.type, cb.opCode, new Object[]{cb.getColor()});
        b.category = cb.category;
        b.mColor = cb.getColor();
        b.setSpec(cb.spec, null);
        b.pane = pane;

        b.attributes = new java.util.HashMap<>(vb.parentAttributes);
        if (vb.id != null) b.attributes.put("id", vb.id);
        if (vb.classNames != null) b.attributes.put("class", vb.classNames);

        if (cb.opCode.equals("html_h")) {
            String tagName = vb.parentAttributes.getOrDefault("html_tag", "h1");
            b.setArgValue(0, tagName);
            b.setArgValue(1, vb.id);
        } else {
            b.setArgValue(0, vb.id);
        }

        pane.addView(b);
        b.setOnTouchListener(touchListener);
        return b;
    }

    private static String getOpCodeFromViewType(int type) {
        return switch (type) {
            case ViewBean.VIEW_TYPE_HTML_DIV -> "html_div";
            case ViewBean.VIEW_TYPE_HTML_HEADER -> "html_header";
            case ViewBean.VIEW_TYPE_HTML_FOOTER -> "html_footer";
            case ViewBean.VIEW_TYPE_HTML_SECTION -> "html_section";
            case ViewBean.VIEW_TYPE_HTML_NAV -> "html_nav";
            case ViewBean.VIEW_TYPE_HTML_MAIN -> "html_main";
            case ViewBean.VIEW_TYPE_HTML_P -> "html_p";
            case ViewBean.VIEW_TYPE_HTML_SPAN -> "html_span";
            case ViewBean.VIEW_TYPE_HTML_H1, ViewBean.VIEW_TYPE_HTML_H2,
                 ViewBean.VIEW_TYPE_HTML_H3, ViewBean.VIEW_TYPE_HTML_H4,
                 ViewBean.VIEW_TYPE_HTML_H5, ViewBean.VIEW_TYPE_HTML_H6 -> "html_h";
            case ViewBean.VIEW_TYPE_HTML_A -> "html_a";
            case ViewBean.VIEW_TYPE_HTML_IMG -> "html_img";
            case ViewBean.VIEW_TYPE_HTML_UL -> "html_ul";
            case ViewBean.VIEW_TYPE_HTML_OL -> "html_ol";
            case ViewBean.VIEW_TYPE_HTML_LI -> "html_li";
            case ViewBean.VIEW_TYPE_HTML_BR -> "html_br";
            case ViewBean.VIEW_TYPE_HTML_HR -> "html_hr";
            case ViewBean.VIEW_TYPE_HTML_FORM -> "html_form";
            case ViewBean.VIEW_TYPE_HTML_INPUT -> "html_input";
            case ViewBean.VIEW_TYPE_HTML_BUTTON -> "html_button";
            case ViewBean.VIEW_TYPE_HTML_LABEL -> "html_label";
            default -> "html_div";
        };
    }

    private static int getHtmlTypeFromOpCode(String opCode, laki.webide.core.Block block) {
        return switch (opCode) {
            case "html_div" -> ViewBean.VIEW_TYPE_HTML_DIV;
            case "html_header" -> ViewBean.VIEW_TYPE_HTML_HEADER;
            case "html_footer" -> ViewBean.VIEW_TYPE_HTML_FOOTER;
            case "html_section" -> ViewBean.VIEW_TYPE_HTML_SECTION;
            case "html_nav" -> ViewBean.VIEW_TYPE_HTML_NAV;
            case "html_main" -> ViewBean.VIEW_TYPE_HTML_MAIN;
            case "html_p" -> ViewBean.VIEW_TYPE_HTML_P;
            case "html_span" -> ViewBean.VIEW_TYPE_HTML_SPAN;
            case "html_h" -> {
                Object val = block.getArgValue(0);
                if (val != null) {
                    yield switch (val.toString().toLowerCase()) {
                        case "h1" -> ViewBean.VIEW_TYPE_HTML_H1;
                        case "h2" -> ViewBean.VIEW_TYPE_HTML_H2;
                        case "h3" -> ViewBean.VIEW_TYPE_HTML_H3;
                        case "h4" -> ViewBean.VIEW_TYPE_HTML_H4;
                        case "h5" -> ViewBean.VIEW_TYPE_HTML_H5;
                        case "h6" -> ViewBean.VIEW_TYPE_HTML_H6;
                        default -> ViewBean.VIEW_TYPE_HTML_H1;
                    };
                }
                yield ViewBean.VIEW_TYPE_HTML_H1;
            }
            case "html_a" -> ViewBean.VIEW_TYPE_HTML_A;
            case "html_img" -> ViewBean.VIEW_TYPE_HTML_IMG;
            case "html_ul" -> ViewBean.VIEW_TYPE_HTML_UL;
            case "html_ol" -> ViewBean.VIEW_TYPE_HTML_OL;
            case "html_li" -> ViewBean.VIEW_TYPE_HTML_LI;
            case "html_br" -> ViewBean.VIEW_TYPE_HTML_BR;
            case "html_hr" -> ViewBean.VIEW_TYPE_HTML_HR;
            case "html_form" -> ViewBean.VIEW_TYPE_HTML_FORM;
            case "html_input" -> ViewBean.VIEW_TYPE_HTML_INPUT;
            case "html_button" -> ViewBean.VIEW_TYPE_HTML_BUTTON;
            case "html_label" -> ViewBean.VIEW_TYPE_HTML_LABEL;
            default -> ViewBean.VIEW_TYPE_HTML_DIV;
        };
    }
}
