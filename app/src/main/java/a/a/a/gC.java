package a.a.a;

import android.util.Pair;
import com.besome.sketch.beans.BlockBean;
import com.besome.sketch.beans.ComponentBean;
import com.besome.sketch.beans.EventBean;
import com.besome.sketch.beans.ViewBean;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.util.ArrayList;

public class gC {
    public enum a {
        VIEW, FAB, VARIABLE, LIST, COMPONENT, EVENT, FUNC, LOGIC
    }

    private String baseName;
    private String suffix;
    private a type;

    public gC(String header) {
        if (header.endsWith("_var")) {
            baseName = header.substring(0, header.length() - 4);
            suffix = "var";
            type = a.VARIABLE;
        } else if (header.endsWith("_list")) {
            baseName = header.substring(0, header.length() - 5);
            suffix = "list";
            type = a.LIST;
        } else if (header.endsWith("_components")) {
            baseName = header.substring(0, header.length() - 11);
            suffix = "components";
            type = a.COMPONENT;
        } else if (header.endsWith("_events")) {
            baseName = header.substring(0, header.length() - 7);
            suffix = "events";
            type = a.EVENT;
        } else if (header.endsWith("_func")) {
            baseName = header.substring(0, header.length() - 5);
            suffix = "func";
            type = a.FUNC;
        } else if (header.endsWith("_fab")) {
            baseName = header.substring(0, header.length() - 4);
            suffix = "fab";
            type = a.FAB;
        } else {
            int lastUnderscore = header.lastIndexOf("_");
            if (lastUnderscore > 0) {
                baseName = header.substring(0, lastUnderscore);
                suffix = header.substring(lastUnderscore + 1);
                type = a.LOGIC;
            } else {
                baseName = header;
                suffix = "";
                type = a.VIEW;
            }
        }
    }

    public a a() {
        return type;
    }

    public String b() {
        return baseName;
    }

    public String c() {
        return suffix;
    }

    public Object a(String content) {
        Gson gson = new Gson();
        if (content == null || content.trim().isEmpty()) {
            return type == a.FAB ? null : new ArrayList<>();
        }
        
        switch (type) {
            case VARIABLE:
            case LIST: {
                ArrayList<Pair<Integer, String>> pairs = new ArrayList<>();
                for (String line : content.split("\n")) {
                    int colon = line.indexOf(":");
                    if (colon > 0) {
                        try {
                            pairs.add(new Pair<>(Integer.parseInt(line.substring(0, colon)), line.substring(colon + 1)));
                        } catch (NumberFormatException ignored) {}
                    }
                }
                return pairs;
            }
            case COMPONENT: {
                ArrayList<ComponentBean> list = new ArrayList<>();
                for (String line : content.split("\n")) {
                    if (!line.trim().isEmpty()) {
                        list.add(gson.fromJson(line, ComponentBean.class));
                    }
                }
                return list;
            }
            case EVENT: {
                ArrayList<EventBean> list = new ArrayList<>();
                for (String line : content.split("\n")) {
                    if (!line.trim().isEmpty()) {
                        list.add(gson.fromJson(line, EventBean.class));
                    }
                }
                return list;
            }
            case FUNC: {
                ArrayList<Pair<String, String>> pairs = new ArrayList<>();
                for (String line : content.split("\n")) {
                    int colon = line.indexOf(":");
                    if (colon > 0) {
                        pairs.add(new Pair<>(line.substring(0, colon), line.substring(colon + 1)));
                    }
                }
                return pairs;
            }
            case LOGIC: {
                ArrayList<BlockBean> list = new ArrayList<>();
                for (String line : content.split("\n")) {
                    if (!line.trim().isEmpty()) {
                        list.add(gson.fromJson(line, BlockBean.class));
                    }
                }
                return list;
            }
            case VIEW: {
                ArrayList<ViewBean> list = new ArrayList<>();
                for (String line : content.split("\n")) {
                    if (!line.trim().isEmpty()) {
                        list.add(gson.fromJson(line, ViewBean.class));
                    }
                }
                return list;
            }
            case FAB:
                return gson.fromJson(content.trim(), ViewBean.class);
            default:
                return null;
        }
    }
}
