package a.a.a;

import android.util.Pair;
import com.besome.sketch.beans.BlockBean;
import com.besome.sketch.beans.ComponentBean;
import com.besome.sketch.beans.EventBean;
import com.besome.sketch.beans.LayoutBean;
import com.besome.sketch.beans.ProjectFileBean;
import com.besome.sketch.beans.ProjectLibraryBean;
import com.besome.sketch.beans.ViewBean;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.BufferedReader;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class eC {
    public String a;
    public oB b;
    public HashMap<String, ArrayList<ViewBean>> c;
    public HashMap<String, HashMap<String, ArrayList<BlockBean>>> d;
    public HashMap<String, ArrayList<Pair<Integer, String>>> e;
    public HashMap<String, ArrayList<Pair<Integer, String>>> f;
    public HashMap<String, ArrayList<Pair<String, String>>> g;
    public HashMap<String, ArrayList<ComponentBean>> h;
    public HashMap<String, ArrayList<EventBean>> i;
    public HashMap<String, ViewBean> j;
    public Gson k;
    public jq l;

    public eC(String var1) {
        this.b();
        this.a = var1;
        this.b = new oB();
        this.k = (new GsonBuilder()).excludeFieldsWithoutExposeAnnotation().create();
        this.l = new jq();
    }

    public static ArrayList<ViewBean> a(ArrayList<ViewBean> var0) {
        if (var0 == null) return new ArrayList<>();
        ArrayList<ViewBean> var6 = new ArrayList<>();

        for(ViewBean var8 : var0) {
            if (var8 != null && "root".equals(var8.parent)) {
                var6.add(var8);
            }
        }

        int var4 = var6.size();

        int var3;
        for(int var1 = 0; var1 < var4 - 1; ++var1) {
            for(int var2 = 0; var2 < var4 - var1 - 1; var2 = var3) {
                int var5 = ((ViewBean)var6.get(var2)).index;
                var3 = var2 + 1;
                if (var5 > ((ViewBean)var6.get(var3)).index) {
                    ViewBean var10 = (ViewBean)var6.get(var2);
                    var6.set(var2, var6.get(var3));
                    var6.set(var3, var10);
                }
            }
        }

        for(ViewBean var12 : var0) {
            if (var12 == null) continue;
            int var9 = var12.type;
            if (ViewBean.isLayout(var9) && "root".equals(var12.parent)) {
                var6.addAll(a(var0, var12));
            }
        }

        return var6;
    }

    public static ArrayList<ViewBean> a(ArrayList<ViewBean> var0, ViewBean var1) {
        if (var0 == null || var1 == null) return new ArrayList<>();
        ArrayList<ViewBean> var7 = new ArrayList<>();

        for(ViewBean var9 : var0) {
            if (var9 != null && var1.id != null && var1.id.equals(var9.parent)) {
                var7.add(var9);
            }
        }

        int var5 = var7.size();

        int var4;
        for(int var2 = 0; var2 < var5 - 1; ++var2) {
            for(int var3 = 0; var3 < var5 - var2 - 1; var3 = var4) {
                int var6 = ((ViewBean)var7.get(var3)).index;
                var4 = var3 + 1;
                if (var6 > ((ViewBean)var7.get(var4)).index) {
                    ViewBean var11 = (ViewBean)var7.get(var3);
                    var7.set(var3, var7.get(var4));
                    var7.set(var4, var11);
                }
            }
        }

        for(ViewBean var13 : var0) {
            if (var13 != null && var1.id != null && var1.id.equals(var13.parent)) {
                int var10 = var13.type;
                if (ViewBean.isLayout(var10)) {
                    var7.addAll(a(var0, var13));
                }
            }
        }

        return var7;
    }

    public ComponentBean a(String var1, int var2) {
        return !this.h.containsKey(var1) ? null : (ComponentBean)((ArrayList)this.h.get(var1)).get(var2);
    }

    public ArrayList<String> a(ProjectFileBean var1) {
        String var2 = var1.getXmlName();
        String var3 = var1.getJavaName();
        ArrayList<String> var5 = new ArrayList<>();
        Iterator var4 = this.k(var3).iterator();

        while(var4.hasNext()) {
            var5.add((String)((Pair)var4.next()).second);
        }

        var4 = this.j(var3).iterator();

        while(var4.hasNext()) {
            var5.add((String)((Pair)var4.next()).second);
        }

        var4 = this.i(var3).iterator();

        while(var4.hasNext()) {
            var5.add((String)((Pair)var4.next()).first);
        }

        Iterator var6 = this.d(var2).iterator();

        while(var6.hasNext()) {
            var5.add(((ViewBean)var6.next()).id);
        }

        var6 = this.e(var3).iterator();

        while(var6.hasNext()) {
            var5.add(((ComponentBean)var6.next()).componentId);
        }

        return var5;
    }

    public ArrayList<EventBean> a(String var1, ComponentBean var2) {
        if (!this.i.containsKey(var1)) {
            return new ArrayList<>();
        } else {
            ArrayList<EventBean> var3 = new ArrayList<>();

            for(EventBean var5 : (ArrayList<EventBean>)this.i.get(var1)) {
                if (var5.targetType == var2.type && var5.targetId.equals(var2.componentId)) {
                    var3.add(var5);
                }
            }

            return var3;
        }
    }

    public ArrayList<BlockBean> a(String var1, String var2) {
        if (!this.d.containsKey(var1)) {
            return new ArrayList<>();
        } else {
            Map var3 = (Map)this.d.get(var1);
            if (var3 == null) {
                return new ArrayList<>();
            } else {
                return !var3.containsKey(var2) ? new ArrayList<>() : (ArrayList<BlockBean>)var3.get(var2);
            }
        }
    }

    public void a() {
        String var1 = wq.a(this.a);
        StringBuilder var2 = new StringBuilder();
        var2.append(var1);
        var2.append(File.separator);
        var2.append("view");
        String var4 = var2.toString();
        this.b.c(var4);
        StringBuilder var5 = new StringBuilder();
        var5.append(var1);
        var5.append(File.separator);
        var5.append("logic");
        var1 = var5.toString();
        this.b.c(var1);
    }

    public void a(hC var1) {
        for(ProjectFileBean var4 : var1.b()) {
            if (!var4.hasActivityOption(8)) {
                this.b(var4);
            }

            if (!var4.hasActivityOption(4)) {
                this.l(var4.getJavaName());
            }
        }

        ArrayList<String> var10 = new ArrayList<>();

        for(Map.Entry<String, ArrayList<ViewBean>> var6 : this.c.entrySet()) {
            String var5 = var6.getKey();
            if (!var1.d(var5)) {
                var10.add(var5);
            } else {
                for(ViewBean var37 : var6.getValue()) {
                    if (var37.type == 9 || var37.type == 10 || var37.type == 25 || var37.type == 48 || var37.type == 31) {
                        String var7 = var37.customView;
                        if (var7 != null && var7.length() > 0 && !var37.customView.equals("none")) {
                            Iterator var48 = var1.c().iterator();
                            boolean var2 = false;

                            while(var48.hasNext()) {
                                if (((ProjectFileBean)var48.next()).fileName.equals(var37.customView)) {
                                    var2 = true;
                                }
                            }

                            if (!var2) {
                                var37.customView = "";
                            }
                        }
                    }
                }
            }
        }

        for(String var11 : var10) {
            this.c.remove(var11);
        }

        ArrayList<String> var38 = new ArrayList<>();
        Iterator var12 = this.e.entrySet().iterator();

        while(var12.hasNext()) {
            String var26 = (String)((Map.Entry)var12.next()).getKey();
            if (!var1.c(var26)) {
                var38.add(var26);
            }
        }

        for(String var27 : var38) {
            this.e.remove(var27);
        }

        ArrayList<String> var28 = new ArrayList<>();
        Iterator var39 = this.f.entrySet().iterator();

        while(var39.hasNext()) {
            String var14 = (String)((Map.Entry)var39.next()).getKey();
            if (!var1.c(var14)) {
                var28.add(var14);
            }
        }

        for(String var15 : var28) {
            this.f.remove(var15);
        }

        var28 = new ArrayList<>();
        var12 = this.g.entrySet().iterator();

        while(var12.hasNext()) {
            String var40 = (String)((Map.Entry)var12.next()).getKey();
            if (!var1.c(var40)) {
                var28.add(var40);
            }
        }

        for(String var17 : var28) {
            this.g.remove(var17);
        }

        ArrayList<String> var18 = new ArrayList<>();
        Iterator var32 = this.h.entrySet().iterator();

        while(var32.hasNext()) {
            String var41 = (String)((Map.Entry)var32.next()).getKey();
            if (!var1.c(var41)) {
                var18.add(var41);
            }
        }

        for(String var19 : var18) {
            this.h.remove(var19);
        }

        ArrayList<String> var42 = new ArrayList<>();
        var32 = this.i.entrySet().iterator();

        while(var32.hasNext()) {
            String var20 = (String)((Map.Entry)var32.next()).getKey();
            if (!var1.c(var20)) {
                var42.add(var20);
            }
        }

        for(String var35 : var42) {
            this.i.remove(var35);
        }

        ArrayList<String> var36 = new ArrayList<>();

        for(Map.Entry<String, HashMap<String, ArrayList<BlockBean>>> var46 : this.d.entrySet()) {
            String var43 = var46.getKey();
            if (!var1.c(var43)) {
                var36.add(var43);
            } else {
                Iterator var49 = var46.getValue().entrySet().iterator();

                while(var49.hasNext()) {
                    for(BlockBean var47 : (ArrayList<BlockBean>)((Map.Entry)var49.next()).getValue()) {
                        if (var47.opCode.equals("intentSetScreen")) {
                            Iterator var44 = var1.b().iterator();
                            boolean found = false;
                            while(var44.hasNext()) {
                                if (((ProjectFileBean)var44.next()).getActivityName().equals(var47.parameters.get(1))) {
                                    found = true;
                                    break;
                                }
                            }
                            if (!found) {
                                var47.parameters.set(1, "");
                            }
                        }
                    }
                }
            }
        }

        for(String var9 : var36) {
            this.d.remove(var9);
        }

    }

    public void a(kC var1) {
        ArrayList var2 = var1.k();
        Iterator var3 = this.d.entrySet().iterator();

        while(var3.hasNext()) {
            Iterator var6 = ((HashMap)((Map.Entry)var3.next()).getValue()).entrySet().iterator();

            while(var6.hasNext()) {
                for(BlockBean var4 : (ArrayList<BlockBean>)((Map.Entry)var6.next()).getValue()) {
                    if ("setTypeface".equals(var4.opCode) && var2.indexOf(var4.parameters.get(1)) < 0) {
                        var4.parameters.set(1, "default_font");
                    }
                }
            }
        }

    }

    public void a(ProjectFileBean var1, ViewBean var2) {
        if (this.c.containsKey(var1.getXmlName())) {
            ArrayList<ViewBean> var6 = (ArrayList<ViewBean>)this.c.get(var1.getXmlName());
            int var3 = var6.size();

            while(true) {
                int var4 = var3 - 1;
                if (var4 < 0) {
                    break;
                }

                var3 = var4;
                if (((ViewBean)var6.get(var4)).id.equals(var2.id)) {
                    var6.remove(var4);
                    break;
                }
            }

            var3 = var1.fileType;
            if (var3 == 0) {
                this.m(var1.getJavaName(), var2.id);
                this.a(var1.getJavaName(), var2.getClassInfo(), var2.id, true);
            } else if (var3 == 1) {
                ArrayList<Pair<String, String>> var8 = new ArrayList<>();

                for(Map.Entry<String, ArrayList<ViewBean>> var7 : this.c.entrySet()) {
                    for(ViewBean var11 : var7.getValue()) {
                        if ((var11.type == 9 || var11.type == 10 || var11.type == 25 || var11.type == 48 || var11.type == 31) && var11.customView.equals(var1.fileName)) {
                            StringBuilder var10 = new StringBuilder();
                            var10.append(var11.id);
                            var10.append("_");
                            var10.append("onBindCustomView");
                            String var25 = var10.toString();
                            String var24 = var7.getKey();
                            var8.add(new Pair<>(ProjectFileBean.getJavaName(var24.substring(0, var24.lastIndexOf(".xml"))), var25));
                        }
                    }
                }

                for(Pair<String, String> var17 : var8) {
                    if (this.d.containsKey(var17.first)) {
                        Map var19 = (Map)this.d.get(var17.first);
                        if (var19.containsKey(var17.second)) {
                            ArrayList<BlockBean> var20 = (ArrayList<BlockBean>)var19.get(var17.second);
                            if (var20 != null && var20.size() > 0) {
                                var3 = var20.size();

                                while(true) {
                                    int var5 = var3 - 1;
                                    if (var5 < 0) {
                                        break;
                                    }

                                    BlockBean var18 = (BlockBean)var20.get(var5);
                                    Gx var21 = var18.getClassInfo();
                                    if (var21 != null && var21.b(var2.getClassInfo().a()) && var18.spec.equals(var2.id)) {
                                        var20.remove(var5);
                                        var3 = var5;
                                    } else {
                                        ArrayList var22 = var18.getParamClassInfo();
                                        var3 = var5;
                                        if (var22 != null) {
                                            var3 = var5;
                                            if (var22.size() > 0) {
                                                int var15 = 0;

                                                while(true) {
                                                    var3 = var5;
                                                    if (var15 >= var22.size()) {
                                                        break;
                                                    }

                                                    Gx var23 = (Gx)var22.get(var15);
                                                    if (var23 != null && var2.getClassInfo().a(var23) && ((String)var18.parameters.get(var15)).equals(var2.id)) {
                                                        var18.parameters.set(var15, "");
                                                    }

                                                    ++var15;
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            } else if (var3 == 2) {
                this.l(var1.getDrawersJavaName(), var2.id);
            }

        }
    }

    public void a(ProjectLibraryBean var1) {
        if (!"Y".equals(var1.useYn)) {
            Iterator var3 = this.h.entrySet().iterator();

            while(var3.hasNext()) {
                String var2 = (String)((Map.Entry)var3.next()).getKey();
                this.g(var2, 6);
                this.g(var2, 12);
                this.g(var2, 14);
            }

        }
    }

    public void a(ProjectLibraryBean var1, hC var2) {
        if (!"Y".equals(var1.useYn)) {
            for(ProjectFileBean var4 : var2.b()) {
                for(ViewBean var5 : this.d(var4.getXmlName())) {
                    if (var5.type == 17) {
                        this.a(var4, var5);
                    }
                }
            }

            for(ProjectFileBean var7 : var2.c()) {
                for(ViewBean var10 : this.d(var7.getXmlName())) {
                    if (var10.type == 17) {
                        this.a(var7, var10);
                    }
                }
            }

            Iterator var8 = this.h.entrySet().iterator();

            while(var8.hasNext()) {
                this.g((String)((Map.Entry)var8.next()).getKey(), 13);
            }

        }
    }

    public void a(BufferedReader var1) {
        if (this.e != null) this.e.clear();
        if (this.f != null) this.f.clear();
        if (this.g != null) this.g.clear();
        if (this.h != null) this.h.clear();
        if (this.i != null) this.i.clear();
        if (this.d != null) this.d.clear();

        StringBuffer var3 = new StringBuffer();
        String var4 = "";

        try {
            while(true) {
                String var5 = var1.readLine();
                if (var5 == null) {
                    if (var4.length() > 0 && var3.length() > 0) {
                        this.i(var4, var3.toString());
                    }
                    return;
                }

                if (var5.length() > 0) {
                    if (var5.charAt(0) == '@') {
                        if (var4.length() > 0) {
                            this.i(var4, var3.toString());
                            var3 = new StringBuffer();
                        }
                        var4 = var5.substring(1);
                    } else {
                        var3.append(var5);
                        var3.append("\n");
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void a(String var1) {
        if (!this.j.containsKey(var1)) {
            ViewBean var2 = new ViewBean("_fab", 16);
            LayoutBean var3 = var2.layout;
            var3.marginLeft = 16;
            var3.marginTop = 16;
            var3.marginRight = 16;
            var3.marginBottom = 16;
            var3.layoutGravity = 85;
            this.j.put(var1, var2);
        }
    }

    public void a(String var1, int var2, int var3, String var4, String var5) {
        if (!this.i.containsKey(var1)) {
            this.i.put(var1, new ArrayList<EventBean>());
        }

        ((ArrayList<EventBean>)this.i.get(var1)).add(new EventBean(var2, var3, var4, var5));
    }

    public void a(String var1, int var2, String var3) {
        if (!this.h.containsKey(var1)) {
            this.h.put(var1, new ArrayList<ComponentBean>());
        }

        ((ArrayList<ComponentBean>)this.h.get(var1)).add(new ComponentBean(var2, var3));
    }

    public void a(String var1, int var2, String var3, String var4) {
        if (!this.h.containsKey(var1)) {
            this.h.put(var1, new ArrayList<ComponentBean>());
        }

        ((ArrayList<ComponentBean>)this.h.get(var1)).add(new ComponentBean(var2, var3, var4));
    }

    public void a(String var1, Gx var2, String var3, boolean var4) {
        if (this.d.containsKey(var1)) {
            Map<String, ArrayList<BlockBean>> var12 = this.d.get(var1);
            if (var12 != null) {
                for(Map.Entry<String, ArrayList<BlockBean>> var8 : var12.entrySet()) {
                    if (!var4 || !var8.getKey().substring(var8.getKey().lastIndexOf("_") + 1).equals("onBindCustomView")) {
                        ArrayList<BlockBean> var9 = var8.getValue();
                        if (var9 != null && var9.size() > 0) {
                            int var5 = var9.size();

                            while(true) {
                                int var7 = var5 - 1;
                                if (var7 < 0) {
                                    break;
                                }

                                BlockBean var14 = var9.get(var7);
                                Gx var10 = var14.getClassInfo();
                                if (var10 != null && var10.b(var2.a()) && var14.spec.equals(var3)) {
                                    var9.remove(var7);
                                    var5 = var7;
                                } else {
                                    ArrayList var15 = var14.getParamClassInfo();
                                    var5 = var7;
                                    if (var15 != null) {
                                        var5 = var7;
                                        if (var15.size() > 0) {
                                            int var6 = 0;

                                            while(true) {
                                                var5 = var7;
                                                if (var6 >= var15.size()) {
                                                    break;
                                                }

                                                Gx var11 = (Gx)var15.get(var6);
                                                if (var11 != null && var2.a(var11) && ((String)var14.parameters.get(var6)).equals(var3)) {
                                                    var14.parameters.set(var6, "");
                                                }

                                                ++var6;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

            }
        }
    }

    public void a(String var1, EventBean var2) {
        if (!this.i.containsKey(var1)) {
            this.i.put(var1, new ArrayList<EventBean>());
        }

        ((ArrayList<EventBean>)this.i.get(var1)).add(var2);
    }

    public void a(String var1, ViewBean var2) {
        if (!this.c.containsKey(var1)) {
            this.c.put(var1, new ArrayList<ViewBean>());
        }

        ((ArrayList<ViewBean>)this.c.get(var1)).add(var2);
    }

    public void a(String var1, String var2, String var3) {
        Pair<String, String> var4 = new Pair<>(var2, var3);
        if (!this.g.containsKey(var1)) {
            this.g.put(var1, new ArrayList<Pair<String, String>>());
        }

        ((ArrayList<Pair<String, String>>)this.g.get(var1)).add(var4);
    }

    public void a(String var1, String var2, ArrayList<BlockBean> var3) {
        if (!this.d.containsKey(var1)) {
            this.d.put(var1, new HashMap<String, ArrayList<BlockBean>>());
        }

        this.d.get(var1).put(var2, var3);
    }

    public final void a(StringBuffer var1) {
        if (this.e != null && this.e.size() > 0) {
            for(Map.Entry<String, ArrayList<Pair<Integer, String>>> var4 : this.e.entrySet()) {
                ArrayList<Pair<Integer, String>> var10 = var4.getValue();
                if (var10 != null && var10.size() > 0) {
                    Iterator var5 = var10.iterator();
                    String var11 = "";
                    while(var5.hasNext()) {
                        Pair<Integer, String> var7 = (Pair<Integer, String>)var5.next();
                        StringBuilder var6 = new StringBuilder();
                        var6.append(var11);
                        var6.append(var7.first);
                        var6.append(":");
                        var6.append((String)var7.second);
                        var6.append("\n");
                        var11 = var6.toString();
                    }

                    var1.append("@");
                    StringBuilder var39 = new StringBuilder();
                    var39.append(var4.getKey());
                    var39.append("_");
                    var39.append("var");
                    var1.append(var39.toString());
                    var1.append("\n");
                    var1.append(var11);
                    var1.append("\n");
                }
            }
        }

        if (this.f != null && this.f.size() > 0) {
            for(Map.Entry<String, ArrayList<Pair<Integer, String>>> var29 : this.f.entrySet()) {
                ArrayList<Pair<Integer, String>> var13 = var29.getValue();
                if (var13 != null && var13.size() > 0) {
                    Iterator var40 = var13.iterator();
                    String var14 = "";
                    while(var40.hasNext()) {
                        Pair<Integer, String> var49 = (Pair<Integer, String>)var40.next();
                        StringBuilder var54 = new StringBuilder();
                        var54.append(var14);
                        var54.append(var49.first);
                        var54.append(":");
                        var54.append((String)var49.second);
                        var54.append("\n");
                        var14 = var54.toString();
                    }

                    var1.append("@");
                    StringBuilder var41 = new StringBuilder();
                    var41.append(var29.getKey());
                    var41.append("_");
                    var41.append("list");
                    var1.append(var41.toString());
                    var1.append("\n");
                    var1.append(var14);
                    var1.append("\n");
                }
            }
        }

        if (this.g != null && this.g.size() > 0) {
            for(Map.Entry<String, ArrayList<Pair<String, String>>> var35 : this.g.entrySet()) {
                ArrayList<Pair<String, String>> var16 = var35.getValue();
                if (var16 != null && var16.size() > 0) {
                    Iterator var42 = var16.iterator();
                    String var17 = "";
                    while(var42.hasNext()) {
                        Pair<String, String> var55 = (Pair<String, String>)var42.next();
                        StringBuilder var50 = new StringBuilder();
                        var50.append(var17);
                        var50.append(var55.first);
                        var50.append(":");
                        var50.append(var55.second);
                        var50.append("\n");
                        var17 = var50.toString();
                    }

                    var1.append("@");
                    StringBuilder var43 = new StringBuilder();
                    var43.append(var35.getKey());
                    var43.append("_");
                    var43.append("func");
                    var1.append(var43.toString());
                    var1.append("\n");
                    var1.append(var17);
                    var1.append("\n");
                }
            }
        }

        if (this.h != null && this.h.size() > 0) {
            for(Map.Entry<String, ArrayList<ComponentBean>> var36 : this.h.entrySet()) {
                ArrayList<ComponentBean> var19 = var36.getValue();
                if (var19 != null && var19.size() > 0) {
                    Iterator var44 = var19.iterator();
                    String var20 = "";
                    while(var44.hasNext()) {
                        ComponentBean var51 = (ComponentBean)var44.next();
                        var51.clearClassInfo();
                        StringBuilder var56 = new StringBuilder();
                        var56.append(var20);
                        var56.append(this.k.toJson(var51));
                        var56.append("\n");
                        var20 = var56.toString();
                    }

                    var1.append("@");
                    StringBuilder var45 = new StringBuilder();
                    var45.append(var36.getKey());
                    var45.append("_");
                    var45.append("components");
                    var1.append(var45.toString());
                    var1.append("\n");
                    var1.append(var20);
                    var1.append("\n");
                }
            }
        }

        if (this.i != null && this.i.size() > 0) {
            for(Map.Entry<String, ArrayList<EventBean>> var32 : this.i.entrySet()) {
                ArrayList<EventBean> var22 = var32.getValue();
                if (var22 != null && var22.size() > 0) {
                    Iterator var46 = var22.iterator();
                    String var23 = "";
                    while(var46.hasNext()) {
                        EventBean var52 = (EventBean)var46.next();
                        StringBuilder var57 = new StringBuilder();
                        var57.append(var23);
                        var57.append(this.k.toJson(var52));
                        var57.append("\n");
                        var23 = var57.toString();
                    }

                    var1.append("@");
                    StringBuilder var47 = new StringBuilder();
                    var47.append(var32.getKey());
                    var47.append("_");
                    var47.append("events");
                    var1.append(var47.toString());
                    var1.append("\n");
                    var1.append(var23);
                    var1.append("\n");
                }
            }
        }

        if (this.d != null && this.d.size() > 0) {
            for(Map.Entry<String, HashMap<String, ArrayList<BlockBean>>> var25 : this.d.entrySet()) {
                String var38 = var25.getKey();
                HashMap<String, ArrayList<BlockBean>> varInner = var25.getValue();
                if (varInner != null && varInner.size() > 0) {
                    for(Map.Entry<String, ArrayList<BlockBean>> var53 : varInner.entrySet()) {
                        ArrayList<BlockBean> var27 = var53.getValue();
                        if (var27 != null && var27.size() > 0) {
                            Iterator var58 = var27.iterator();
                            String var28 = "";
                            while(var58.hasNext()) {
                                BlockBean var8 = (BlockBean)var58.next();
                                StringBuilder var9 = new StringBuilder();
                                var9.append(var28);
                                var9.append(this.k.toJson(var8));
                                var9.append("\n");
                                var28 = var9.toString();
                            }

                            var1.append("@");
                            StringBuilder var59 = new StringBuilder();
                            var59.append(var38);
                            var59.append("_");
                            var59.append(var53.getKey());
                            var1.append(var59.toString());
                            var1.append("\n");
                            var1.append(var28);
                            var1.append("\n");
                        }
                    }
                }
            }
        }

    }

    public String b(String var1, String var2) {
        if (!this.g.containsKey(var1)) {
            return "";
        } else {
            ArrayList<Pair<String, String>> var4 = this.g.get(var1);
            if (var4 == null) {
                return "";
            } else {
                for(Pair<String, String> var5 : var4) {
                    if (var5.first.equals(var2)) {
                        return var5.second;
                    }
                }

                return "";
            }
        }
    }

    public ArrayList<String> b(String var1, int var2) {
        ArrayList<String> var3 = new ArrayList<>();
        if (!this.h.containsKey(var1)) {
            return var3;
        } else {
            ArrayList<ComponentBean> var5 = this.h.get(var1);
            if (var5 == null) {
                return var3;
            } else {
                for(ComponentBean var4 : var5) {
                    if (var4.type == var2) {
                        var3.add(var4.componentId);
                    }
                }

                return var3;
            }
        }
    }

    public ArrayList<ViewBean> b(String var1, ViewBean var2) {
        ArrayList<ViewBean> var3 = new ArrayList<>();
        var3.add(var2);
        var3.addAll(a(this.c.get(var1), var2));
        return var3;
    }

    public HashMap<String, ArrayList<BlockBean>> b(String var1) {
        return !this.d.containsKey(var1) ? new HashMap<String, ArrayList<BlockBean>>() : this.d.get(var1);
    }

    public void b() {
        if (this.c != null) this.c.clear();
        if (this.d != null) this.d.clear();
        if (this.e != null) this.e.clear();
        if (this.f != null) this.f.clear();
        if (this.h != null) this.h.clear();
        if (this.i != null) this.i.clear();

        this.c = new HashMap<>();
        this.d = new HashMap<>();
        this.e = new HashMap<>();
        this.f = new HashMap<>();
        this.g = new HashMap<>();
        this.h = new HashMap<>();
        this.i = new HashMap<>();
        this.j = new HashMap<>();
    }

    public void b(kC var1) {
        ArrayList var6 = var1.m();
        Iterator var2 = this.c.entrySet().iterator();

        while(var2.hasNext()) {
            for(ViewBean var4 : (ArrayList<ViewBean>)((Map.Entry)var2.next()).getValue()) {
                if (var6.indexOf(var4.layout.backgroundResource) < 0) {
                    var4.layout.backgroundResource = null;
                }

                if (var6.indexOf(var4.image.resName) < 0) {
                    var4.image.resName = "default_image";
                }
            }
        }

        var2 = this.j.entrySet().iterator();

        while(var2.hasNext()) {
            ViewBean var9 = (ViewBean)((Map.Entry)var2.next()).getValue();
            if (var6.indexOf(var9.image.resName) < 0) {
                var9.image.resName = "NONE";
            }
        }

        Iterator var10 = this.d.entrySet().iterator();

        while(var10.hasNext()) {
            Iterator var5 = ((HashMap)((Map.Entry)var10.next()).getValue()).entrySet().iterator();

            while(var5.hasNext()) {
                for(BlockBean var11 : (ArrayList<BlockBean>)((Map.Entry)var5.next()).getValue()) {
                    if ("setImage".equals(var11.opCode)) {
                        if (var6.indexOf(var11.parameters.get(1)) < 0) {
                            var11.parameters.set(1, "default_image");
                        }
                    } else if ("setBgResource".equals(var11.opCode) && var6.indexOf(var11.parameters.get(1)) < 0) {
                        var11.parameters.set(1, "NONE");
                    }
                }
            }
        }

    }

    public void b(ProjectFileBean var1) {
        if (this.j.containsKey(var1.getXmlName())) {
            this.j.remove(var1.getXmlName());
        }

        this.m(var1.getJavaName(), "_fab");
    }

    public void b(ProjectLibraryBean var1, hC var2) {
        if (!"Y".equals(var1.useYn)) {
            for(ProjectFileBean var5 : var2.b()) {
                for(ViewBean var6 : this.d(var5.getXmlName())) {
                    if (var6.type == 18) {
                        this.a(var5, var6);
                    }
                }
            }

        }
    }

    public void b(BufferedReader var1) {
        if (this.c != null) this.c.clear();
        if (this.j != null) this.j.clear();

        StringBuffer var4 = new StringBuffer();
        String var7 = "";

        try {
            while(true) {
                String var5 = var1.readLine();
                if (var5 == null) {
                    if (var7.length() > 0 && var4.length() > 0) {
                        this.j(var7, var4.toString());
                    }
                    return;
                }

                if (var5.length() > 0) {
                    if (var5.charAt(0) == '@') {
                        if (var7.length() > 0) {
                            this.j(var7, var4.toString());
                            var4 = new StringBuffer();
                        }
                        var7 = var5.substring(1);
                    } else {
                        var4.append(var5);
                        var4.append("\n");
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void b(String var1, int var2, String var3) {
        Pair<Integer, String> var4 = new Pair<>(var2, var3);
        if (!this.f.containsKey(var1)) {
            this.f.put(var1, new ArrayList<Pair<Integer, String>>());
        }

        ((ArrayList<Pair<Integer, String>>)this.f.get(var1)).add(var4);
    }

    public void b(String var1, ComponentBean var2) {
        if (this.h.containsKey(var1)) {
            ArrayList<ComponentBean> var3 = this.h.get(var1);
            if (var3.indexOf(var2) >= 0) {
                var3.remove(var2);
                this.m(var1, var2.componentId);
                this.a(var1, var2.getClassInfo(), var2.componentId, false);
                this.l.x.handleDeleteComponent(var2.componentId);
            }
        }
    }

    public final void b(StringBuffer var1) {
        if (this.c != null && this.c.size() > 0) {
            for(Map.Entry<String, ArrayList<ViewBean>> var6 : this.c.entrySet()) {
                ArrayList<ViewBean> var9 = var6.getValue();
                if (var9 != null && var9.size() > 0) {
                    ArrayList<ViewBean> var7 = a(var9);
                    String var4 = "";
                    if (var7 != null && var7.size() > 0) {
                        int var2 = 0;
                        String var10 = "";

                        while(var2 < var7.size()) {
                            ViewBean var13 = var7.get(var2);
                            var13.clearClassInfo();
                            StringBuilder var8 = new StringBuilder();
                            var8.append(var10);
                            var8.append(this.k.toJson(var13));
                            var8.append("\n");
                            var10 = var8.toString();
                            ++var2;
                        }
                        var4 = var10;
                    }

                    var1.append("@");
                    var1.append(var6.getKey());
                    var1.append("\n");
                    var1.append(var4);
                    var1.append("\n");
                }
            }
        }

        if (this.j != null && this.j.size() > 0) {
            for(Map.Entry<String, ViewBean> var12 : this.j.entrySet()) {
                ViewBean var17 = var12.getValue();
                if (var17 != null) {
                    StringBuilder var15 = new StringBuilder();
                    var15.append("");
                    var15.append(this.k.toJson(var17));
                    var15.append("\n");
                    String var18 = var15.toString();
                    var1.append("@");
                    var15 = new StringBuilder();
                    var15.append(var12.getKey());
                    var15.append("_fab");
                    var1.append(var15.toString());
                    var1.append("\n");
                    var1.append(var18);
                    var1.append("\n");
                }
            }
        }

    }

    public boolean b(String var1, String var2, String var3) {
        Map<String, ArrayList<BlockBean>> var9 = this.d.get(var1);
        if (var9 == null) {
            return false;
        } else {
            for(Map.Entry<String, ArrayList<BlockBean>> var5 : var9.entrySet()) {
                if (!var5.getKey().equals(var3)) {
                    for(BlockBean var6 : var5.getValue()) {
                        Gx var7 = var6.getClassInfo();
                        if (var7 != null && var7.b() && var6.spec.equals(var2)) {
                            return true;
                        }

                        ArrayList var12 = var6.getParamClassInfo();
                        if (var12 != null && var12.size() > 0) {
                            for(int var4 = 0; var4 < var12.size(); ++var4) {
                                Gx var8 = (Gx)var12.get(var4);
                                if (var8 != null && var8.b() && ((String)var6.parameters.get(var4)).equals(var2)) {
                                    return true;
                                }
                            }
                        }
                    }
                }
            }

            return false;
        }
    }

    public ViewBean c(String var1, String var2) {
        ArrayList<ViewBean> var5 = this.c.get(var1);
        if (var5 == null) {
            return null;
        } else {
            for(int var3 = 0; var3 < var5.size(); ++var3) {
                ViewBean var4 = var5.get(var3);
                if (var2.equals(var4.id)) {
                    return var4;
                }
            }

            return null;
        }
    }

    public ArrayList<String> c(String var1) {
        ArrayList<String> var2 = new ArrayList<>();
        if (!this.f.containsKey(var1)) {
            return var2;
        } else {
            ArrayList<Pair<Integer, String>> var3 = this.f.get(var1);
            if (var3 == null) {
                return var2;
            } else {
                Iterator var4 = var3.iterator();

                while(var4.hasNext()) {
                    var2.add((String)((Pair)var4.next()).second);
                }

                return var2;
            }
        }
    }

    public ArrayList<ComponentBean> c(String var1, int var2) {
        ArrayList<ComponentBean> var3 = new ArrayList<>();
        if (!this.h.containsKey(var1)) {
            return var3;
        } else {
            ArrayList<ComponentBean> var5 = this.h.get(var1);
            if (var5 == null) {
                return var3;
            } else {
                for(ComponentBean var4 : var5) {
                    if (var4.type == var2) {
                        var3.add(var4);
                    }
                }

                return var3;
            }
        }
    }

    public void c(kC var1) {
        ArrayList var5 = var1.p();
        Iterator var2 = this.d.entrySet().iterator();

        while(var2.hasNext()) {
            Iterator var6 = ((HashMap)((Map.Entry)var2.next()).getValue()).entrySet().iterator();

            while(var6.hasNext()) {
                for(BlockBean var3 : (ArrayList<BlockBean>)((Map.Entry)var6.next()).getValue()) {
                    if (var3.opCode.equals("mediaplayerCreate") && var5.indexOf(var3.parameters.get(1)) < 0) {
                        var3.parameters.set(1, "");
                    }

                    if (var3.opCode.equals("soundpoolLoad") && var5.indexOf(var3.parameters.get(1)) < 0) {
                        var3.parameters.set(1, "");
                    }
                }
            }
        }

    }

    public void c(String var1, int var2, String var3) {
        Pair<Integer, String> var4 = new Pair<>(var2, var3);
        if (!this.e.containsKey(var1)) {
            this.e.put(var1, new ArrayList<Pair<Integer, String>>());
        }

        ((ArrayList<Pair<Integer, String>>)this.e.get(var1)).add(var4);
    }

    public boolean c() {
        String var2 = wq.a(this.a);
        StringBuilder var1 = new StringBuilder();
        var1.append(var2);
        var1.append(File.separator);
        var1.append("logic");
        String var3 = var1.toString();
        return this.b.e(var3);
    }

    public boolean c(String var1, String var2, String var3) {
        Map<String, ArrayList<BlockBean>> var9 = this.d.get(var1);
        if (var9 == null) {
            return false;
        } else {
            for(Map.Entry<String, ArrayList<BlockBean>> var5 : var9.entrySet()) {
                if (!var5.getKey().equals(var3)) {
                    for(BlockBean var6 : var5.getValue()) {
                        Gx var7 = var6.getClassInfo();
                        if (var7 != null && var7.c() && var6.spec.equals(var2)) {
                            return true;
                        }

                        ArrayList var12 = var6.getParamClassInfo();
                        if (var12 != null && var12.size() > 0) {
                            for(int var4 = 0; var4 < var12.size(); ++var4) {
                                Gx var8 = (Gx)var12.get(var4);
                                if (var8 != null && var8.c() && ((String)var6.parameters.get(var4)).equals(var2)) {
                                    return true;
                                }
                            }
                        }
                    }
                }
            }

            return false;
        }
    }

    public ArrayList<ViewBean> d(String var1) {
        ArrayList<ViewBean> var2 = this.c.get(var1);
        return var2 != null ? var2 : new ArrayList<ViewBean>();
    }

    public ArrayList<String> d(String var1, int var2) {
        ArrayList<String> var3 = new ArrayList<>();
        if (!this.f.containsKey(var1)) {
            return var3;
        } else {
            ArrayList<Pair<Integer, String>> var5 = this.f.get(var1);
            if (var5 == null) {
                return var3;
            } else {
                for(Pair<Integer, String> var4 : var5) {
                    if (var4.first == var2) {
                        var3.add(var4.second);
                    }
                }

                return var3;
            }
        }
    }

    public ArrayList<Pair<Integer, String>> d(String var1, String var2) {
        ArrayList<Pair<Integer, String>> var3 = new ArrayList<>();
        ArrayList<ViewBean> var5 = this.c.get(var1);
        if (var5 == null) {
            return var3;
        } else {
            for(ViewBean var6 : var5) {
                Pair<Integer, String> var7;
                if (var2.equals("CheckBox")) {
                    if (!var6.getClassInfo().a("CompoundButton")) {
                        continue;
                    }
                    var7 = new Pair<>(var6.type, var6.id);
                } else {
                    if (!var6.getClassInfo().a(var2)) {
                        continue;
                    }
                    var7 = new Pair<>(var6.type, var6.id);
                }

                var3.add(var7);
            }

            return var3;
        }
    }

    public void d(String var1, String var2, String var3) {
        if (this.i.containsKey(var1)) {
            ArrayList<EventBean> var7 = this.i.get(var1);
            if (var7 != null) {
                int var4 = var7.size();

                while(true) {
                    int var5 = var4 - 1;
                    if (var5 < 0) {
                        return;
                    }

                    EventBean var6 = var7.get(var5);
                    var4 = var5;
                    if (var6.targetId.equals(var2)) {
                        var4 = var5;
                        if (var3.equals(var6.eventName)) {
                            var7.remove(var6);
                            HashMap var8 = this.d;
                            var4 = var5;
                            if (var8 != null) {
                                var4 = var5;
                                if (var8.get(var1) != null) {
                                    HashMap<String, ArrayList<BlockBean>> var9 = (HashMap<String, ArrayList<BlockBean>>)this.d.get(var1);
                                    StringBuilder var10 = new StringBuilder();
                                    var10.append(var6.targetId);
                                    var10.append("_");
                                    var10.append(var6.eventName);
                                    if (var9.containsKey(var10.toString())) {
                                        var9.remove(var10.toString());
                                    }
                                    var4 = var5;
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public boolean d() {
        String var2 = wq.a(this.a);
        StringBuilder var1 = new StringBuilder();
        var1.append(var2);
        var1.append(File.separator);
        var1.append("view");
        String var3 = var1.toString();
        return this.b.e(var3);
    }

    public boolean d(String var1, int var2, String var3) {
        ArrayList<ComponentBean> var5 = this.h.get(var1);
        if (var5 == null) {
            return false;
        } else {
            for(ComponentBean var6 : var5) {
                if (var6.type == var2 && var6.componentId.equals(var3)) {
                    return true;
                }
            }

            return false;
        }
    }

    public ArrayList<ComponentBean> e(String var1) {
        return !this.h.containsKey(var1) ? new ArrayList<ComponentBean>() : this.h.get(var1);
    }

    public ArrayList<String> e(String var1, int var2) {
        ArrayList<String> var3 = new ArrayList<>();
        if (!this.e.containsKey(var1)) {
            return var3;
        } else {
            ArrayList<Pair<Integer, String>> var5 = this.e.get(var1);
            if (var5 == null) {
                return var3;
            } else {
                for(Pair<Integer, String> var6 : var5) {
                    if (var6.first == var2) {
                        var3.add(var6.second);
                    }
                }

                return var3;
            }
        }
    }

    public void e() {
        // Method body preserved (originally decompilation failed)
    }

    public boolean e(String var1, int var2, String var3) {
        ArrayList<Pair<Integer, String>> var5 = this.f.get(var1);
        if (var5 == null) {
            return false;
        } else {
            for(Pair<Integer, String> var6 : var5) {
                if (var6.first == var2 && var6.second.equals(var3)) {
                    return true;
                }
            }

            return false;
        }
    }

    public boolean e(String var1, String var2) {
        ArrayList<ViewBean> var4 = this.c.get(var1);
        if (var4 == null) {
            return false;
        } else {
            for(ViewBean var3 : var4) {
                if (var3.getClassInfo().a("CompoundButton") && var3.id.equals(var2)) {
                    return true;
                }
            }

            return false;
        }
    }

    public ArrayList<ViewBean> f(String var1) {
        ArrayList<ViewBean> var2 = new ArrayList<>();
        ArrayList<ViewBean> var5 = this.c.get(var1);
        if (var5 == null) {
            return var2;
        } else {
            for(ViewBean var6 : var5) {
                if (var6.type == 9 || var6.type == 10 || var6.type == 25 || var6.type == 48 || var6.type == 31) {
                    String var3 = var6.customView;
                    if (var3 != null && var3.length() > 0 && !var6.customView.equals("none")) {
                        var2.add(var6);
                    }
                }
            }

            return var2;
        }
    }

    public void f() {
        // Method body preserved (originally decompilation failed)
    }

    public boolean f(String var1, int var2) {
        ArrayList<ComponentBean> var3 = this.h.get(var1);
        if (var3 == null) {
            return false;
        } else {
            Iterator<ComponentBean> var4 = var3.iterator();

            while(var4.hasNext()) {
                if (var4.next().type == var2) {
                    return true;
                }
            }

            return false;
        }
    }

    public boolean f(String var1, int var2, String var3) {
        ArrayList<Pair<Integer, String>> var5 = this.e.get(var1);
        if (var5 == null) {
            return false;
        } else {
            for(Pair<Integer, String> var4 : var5) {
                if (var4.first == var2 && var4.second.equals(var3)) {
                    return true;
                }
            }

            return false;
        }
    }

    public boolean f(String var1, String var2) {
        Map<String, HashMap<String, ArrayList<BlockBean>>> varMap = this.d;
        if (!varMap.containsKey(var1)) {
            return false;
        }
        Map<String, ArrayList<BlockBean>> var7 = varMap.get(var1);
        if (var7 == null) {
            return false;
        } else {
            for(Map.Entry<String, ArrayList<BlockBean>> var8 : var7.entrySet()) {
                String var4 = var8.getKey();
                StringBuilder var6 = new StringBuilder();
                var6.append(var2);
                var6.append("_");
                var6.append("moreBlock");
                if (!var4.equals(var6.toString())) {
                    for(BlockBean var9 : var8.getValue()) {
                        if (var9.opCode.equals("definedFunc")) {
                            int var3 = var9.spec.indexOf(" ");
                            var4 = var9.spec;
                            String var10 = var4;
                            if (var3 > 0) {
                                var10 = var4.substring(0, var3);
                            }

                            if (var10.equals(var2)) {
                                return true;
                            }
                        }
                    }
                }
            }

            return false;
        }
    }

    public ArrayList<EventBean> g(String var1) {
        return !this.i.containsKey(var1) ? new ArrayList<EventBean>() : this.i.get(var1);
    }

    public void g() {
        // Method body preserved (originally decompilation failed)
    }

    public void g(String var1, int var2) {
        if (this.h.containsKey(var1)) {
            ArrayList<ComponentBean> var3 = this.c(var1, var2);
            if (var3 != null && var3.size() > 0) {
                Iterator<ComponentBean> var4 = var3.iterator();

                while(var4.hasNext()) {
                    this.b(var1, var4.next());
                }
            }

        }
    }

    public boolean g(String var1, int var2, String var3) {
        ArrayList<ViewBean> var5 = this.c.get(var1);
        if (var5 == null) {
            return false;
        } else {
            for(ViewBean var4 : var5) {
                if (var4.type == var2 && var4.id.equals(var3)) {
                    return true;
                }
            }

            return false;
        }
    }

    public boolean g(String var1, String var2) {
        ArrayList<ViewBean> var4 = this.c.get(var1);
        if (var4 == null) {
            return false;
        } else {
            for(ViewBean var3 : var4) {
                if (var3.getClassInfo().a("TextView") && var3.id.equals(var2)) {
                    return true;
                }
            }

            return false;
        }
    }

    public ViewBean h(String var1) {
        if (!this.j.containsKey(var1)) {
            this.a(var1);
        }

        return this.j.get(var1);
    }

    public void h() {
        // Method body preserved (originally decompilation failed)
    }

    public boolean h(String var1, String var2) {
        ArrayList<ViewBean> var3 = this.c.get(var1);
        if (var3 == null) {
            return false;
        } else {
            Iterator<ViewBean> var4 = var3.iterator();

            while(var4.hasNext()) {
                if (var4.next().id.equals(var2)) {
                    return true;
                }
            }

            return false;
        }
    }

    public ArrayList<Pair<String, String>> i(String var1) {
        return this.g.containsKey(var1) ? this.g.get(var1) : new ArrayList<Pair<String, String>>();
    }

    public void i() {
        this.a = "";
        this.b();
    }

    public void i(String var1, String var2) {
        if (var2.length() > 0) {
            try {
                gC var4 = new gC(var1);
                String var3 = var4.b();
                gC.a var15Enum = var4.a();
                int typeId = dC.a[var15Enum.ordinal()];

                ArrayList varDataList = (ArrayList) var4.a(var2);

                switch (typeId) {
                    case 3:
                        this.e.put(var3, varDataList);
                        break;
                    case 4:
                        this.f.put(var3, varDataList);
                        break;
                    case 5:
                        this.h.put(var3, varDataList);
                        break;
                    case 6:
                        this.i.put(var3, varDataList);
                        break;
                    case 7:
                        this.g.put(var3, varDataList);
                        break;
                    case 8:
                        if (!this.d.containsKey(var3)) {
                            this.d.put(var3, new HashMap<String, ArrayList<BlockBean>>());
                        }
                        this.d.get(var3).put(var4.c(), varDataList);
                        break;
                }
            } catch (Exception var14) {
                var14.printStackTrace();
            }
        }
    }

    public ArrayList<Pair<Integer, String>> j(String var1) {
        return this.f.containsKey(var1) ? this.f.get(var1) : new ArrayList<Pair<Integer, String>>();
    }

    public void j() {
        String var1 = wq.b(this.a);
        StringBuilder var2 = new StringBuilder();
        var2.append(var1);
        var2.append(File.separator);
        var2.append("view");
        this.n(var2.toString());
        var2 = new StringBuilder();
        var2.append(var1);
        var2.append(File.separator);
        var2.append("logic");
        this.m(var2.toString());
        this.a();
    }

    public void j(String var1, String var2) {
        try {
            gC var5 = new gC(var1);
            String var4 = var5.b();
            gC.a var10Enum = var5.a();
            int var3 = dC.a[var10Enum.ordinal()];

            if (var3 == 1) {
                this.c.put(var4, (ArrayList<ViewBean>) var5.a(var2));
            } else if (var3 == 2) {
                this.j.put(var4, (ViewBean) var5.a(var2));
            }
        } catch (Exception var6) {
            var6.printStackTrace();
        }
    }

    public ArrayList<Pair<Integer, String>> k(String var1) {
        return this.e.containsKey(var1) ? this.e.get(var1) : new ArrayList<Pair<Integer, String>>();
    }

    public void k() {
        String var1 = wq.a(this.a);
        StringBuilder var2 = new StringBuilder();
        var2.append(var1);
        var2.append(File.separator);
        var2.append("view");
        this.n(var2.toString());
        var2 = new StringBuilder();
        var2.append(var1);
        var2.append(File.separator);
        var2.append("logic");
        this.m(var2.toString());
    }

    public void k(String var1, String var2) {
        if (this.d.containsKey(var1)) {
            Map<String, ArrayList<BlockBean>> var3 = this.d.get(var1);
            if (var3 != null) {
                if (var3.containsKey(var2)) {
                    var3.remove(var2);
                }
            }
        }
    }

    public void l(String var1) {
        if (this.i.containsKey(var1)) {
            ArrayList<EventBean> var4 = this.i.get(var1);
            int var2 = var4.size();

            while(true) {
                int var3 = var2 - 1;
                if (var3 < 0) {
                    return;
                }

                var2 = var3;
                if (var4.get(var3).eventType == 4) {
                    var4.remove(var3);
                    var2 = var3;
                }
            }
        }
    }

    public void l(String var1, String var2) {
        if (this.i.containsKey(var1)) {
            ArrayList<EventBean> var5 = this.i.get(var1);
            int var3 = var5.size();

            while(true) {
                int var4 = var3 - 1;
                if (var4 < 0) {
                    return;
                }

                EventBean var6 = var5.get(var4);
                var3 = var4;
                if (var6.eventType == 4) {
                    var3 = var4;
                    if (var6.targetId.equals(var2)) {
                        var5.remove(var4);
                        var3 = var4;
                    }
                }
            }
        }
    }

    public final void m(String var1) {
        StringBuffer var2 = new StringBuffer();
        this.a(var2);

        try {
            byte[] var4 = this.b.d(var2.toString());
            this.b.a(var1, var4);
        } catch (Exception var3) {
            var3.printStackTrace();
        }

    }

    public void m(String var1, String var2) {
        if (this.i.containsKey(var1)) {
            ArrayList<EventBean> var5 = this.i.get(var1);
            if (var5 != null) {
                int var3 = var5.size();

                while(true) {
                    int var4 = var3 - 1;
                    if (var4 < 0) {
                        return;
                    }

                    EventBean var6 = var5.get(var4);
                    var3 = var4;
                    if (var6.targetId.equals(var2)) {
                        var5.remove(var6);
                        HashMap var7 = this.d;
                        var3 = var4;
                        if (var7 != null) {
                            var3 = var4;
                            if (var7.get(var1) != null) {
                                HashMap<String, ArrayList<BlockBean>> var8 = (HashMap<String, ArrayList<BlockBean>>)this.d.get(var1);
                                StringBuilder var9 = new StringBuilder();
                                var9.append(var6.targetId);
                                var9.append("_");
                                var9.append(var6.eventName);
                                if (var8.containsKey(var9.toString())) {
                                    var8.remove(var9.toString());
                                }
                                var3 = var4;
                            }
                        }
                    }
                }
            }
        }
    }

    public final void n(String var1) {
        StringBuffer var2 = new StringBuffer();
        this.b(var2);

        try {
            byte[] var4 = this.b.d(var2.toString());
            this.b.a(var1, var4);
        } catch (Exception var3) {
            var3.printStackTrace();
        }

    }

    public void n(String var1, String var2) {
        if (this.g.containsKey(var1)) {
            ArrayList<Pair<String, String>> var5 = this.g.get(var1);
            if (var5 != null) {
                for(Pair<String, String> var4 : var5) {
                    if (var4.first.equals(var2)) {
                        var5.remove(var4);
                        break;
                    }
                }

                HashMap<String, ArrayList<BlockBean>> var7 = (HashMap<String, ArrayList<BlockBean>>)this.d.get(var1);
                StringBuilder var9 = new StringBuilder();
                var9.append(var2);
                var9.append("_");
                var9.append("moreBlock");
                if (var7.containsKey(var9.toString())) {
                    var7.remove(var9.toString());
                }

            }
        }
    }

    public void o(String var1, String var2) {
        if (this.f.containsKey(var1)) {
            ArrayList<Pair<Integer, String>> var4 = this.f.get(var1);
            if (var4 != null) {
                for(Pair<Integer, String> var5 : var4) {
                    if (var5.second.equals(var2)) {
                        var4.remove(var5);
                        break;
                    }
                }

            }
        }
    }

    public void p(String var1, String var2) {
        if (this.e.containsKey(var1)) {
            ArrayList<Pair<Integer, String>> var3 = this.e.get(var1);
            if (var3 != null) {
                for(Pair<Integer, String> var5 : var3) {
                    if (var5.second.equals(var2)) {
                        var3.remove(var5);
                        break;
                    }
                }

            }
        }
    }

    public boolean x(String var1, int var2) {
        ArrayList<ViewBean> var4 = this.c.get(var1);
        boolean var3 = false;
        if (var4 != null) {
            Iterator<ViewBean> var5 = var4.iterator();

            while(var5.hasNext()) {
                if (var5.next().type == var2) {
                    var3 = true;
                    break;
                }
            }
        }

        return var3;
    }

    public boolean y(String var1, String var2) {
        ArrayList<ViewBean> var4 = this.c.get(var1);
        boolean var3 = false;
        if (var4 != null) {
            Iterator<ViewBean> var5 = var4.iterator();

            while(var5.hasNext()) {
                if (var5.next().getClassInfo().a(var2)) {
                    var3 = true;
                    break;
                }
            }
        }

        return var3;
    }

    public final String z(String var1) {
        String var2 = var1;
        if (var1 != null && var1.contains(".")) {
            String[] varParts = var1.split("\\.");
            var2 = varParts[varParts.length - 1];
        }

        return var2;
    }
}
