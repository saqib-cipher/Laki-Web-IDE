package laki.webide.core;

import android.content.Context;
import android.os.Environment;
import java.io.File;
import a.a.a.DB;
import laki.webide.SketchApplication;

/**
 * Global Registry for all Web IDE Folder and File Structures.
 * Centralizing this ensures consistency across Creation, Save, Backup, and Restore.
 */
public class LakiFiles {

    // 1. Root and Core Sub-Roots
    public static final String ROOT = ".lakiwebsites";
    public static final String SUB_SIMPLE = "simple";
    public static final String SUB_COMPLEX = "complex";

    // 2. System and Metadata Paths
    public static final String DIR_DATA = "data";
    public static final String DIR_MYSYNC = "mysc";
    public static final String DIR_LIST = "list";
    public static final String DIR_HTML = "html";
    public static final String DIR_CSS = "css";
    public static final String DIR_ASSETS = "assets";
    public static final String DIR_SETTINGS = "settings";

    // 2.1 Assets Subfolders
    public static final String SUB_IMAGES = "images";
    public static final String SUB_FONTS = "fonts";
    public static final String SUB_ICONS = "icons";
    public static final String SUB_SOUNDS = "sounds";

    // 2.2 Critical Setting Files
    public static final String FILE_EXTEND_CSS_BLOCKS = "extendcssblocks.json";
    public static final String FILE_HTML_TAGS = "htmltags.json";
    public static final String FILE_HTML_HEAD = "htmlhead.json";

    // 3. Advance Project Structure (Future Frameworks)
    public static final String DIR_REACT = "react";
    public static final String DIR_ANGULAR = "angular";

    // 3.1 React Subfolders
    public static final String SUB_REACT_SRC = "src";
    public static final String SUB_REACT_PUBLIC = "public";
    public static final String SUB_REACT_COMPONENTS = "src/components";

    // 3.2 Angular Subfolders
    public static final String SUB_ANGULAR_SRC = "src";
    public static final String SUB_ANGULAR_APP = "src/app";
    public static final String SUB_ANGULAR_ASSETS = "src/assets";

    // --- Migrated from wq.java ---
    public static final String A = "lakiwebsites" + File.separator + "localization" + File.separator + "strings_provided.xml";
    public static final String B = "lakiwebsites" + File.separator + "signed_apk";
    public static final String C = "lakiwebsites" + File.separator + "keystore";
    public static final String D = "lakiwebsites" + File.separator + "keystore" + File.separator + "release_key.jks";
    public static final String E = "lakiwebsites" + File.separator + "service_account";
    public static final String F = ".lakiwebsites" + File.separator + "upload";
    public static final String[] G = {"subs_year_01", "subs_50_year_01", "subs_30_year_01", "subs_20_year_01", "subs_month_06", "subs_month_03", "subs_month_01", "subs_50_month_01", "subs_30_month_01", "subs_20_month_01"};
    public static final long[] H = {32140800000L, 32140800000L, 32140800000L, 32140800000L, 16070400000L, 8035200000L, 2678400000L, 2678400000L, 2678400000L, 2678400000L};
    public static final String[] I = {"subs_month_01", "subs_year_01"};
    public static final String[] L = {"subs_20_month_01", "subs_20_year_01"};
    public static final String[] M = {"F83085529A75E7A8CEDD64013B1A374B", "90C443DFAB7F23424DE7E079787466CD", "F83085529A75E7A8CEDD64013B1A374B", "C99E5B3F179203AE2749F8F9B5A7493A", "100EFD7391FF1BEE4A1E2F960A1B8AF2"};
    public static final String[] N = {"1486507718310013_1788685811425534", "1486507718310013_1804931006467681", "1486507718310013_1805009746459807", "1486507718310013_1805001526460629", "1486507718310013_1805273579766757", "1486507718310013_1805397669754348", "1486507718310013_1805436593083789", "1486507718310013_1805666736394108", "1486507718310013_1805724186388363", "1486507718310013_1809233042704144"};
    public static final String[] O = {"255022168522663_266931247331755", "255022168522663_268282677196612", "255022168522663_268283823863164", "255022168522663_266575314034015", "255022168522663_279474749410738"};
    public static final String a = ".lakiwebsites" + File.separator + "libs";
    public static final String simple = ROOT + File.separator + SUB_SIMPLE;
    public static final String complex = ROOT + File.separator + SUB_COMPLEX;
    public static final String b = ROOT + File.separator + DIR_MYSYNC;
    public static final String c = b + File.separator + DIR_LIST;
    public static final String d = ROOT + File.separator + DIR_DATA;
    public static final String e = ".lakiwebsites" + File.separator + "bak";
    public static final String f = ".lakiwebsites" + File.separator + "temp" + File.separator + "images";
    public static final String g = ".lakiwebsites" + File.separator + "temp" + File.separator + "sounds";
    public static final String h = ".lakiwebsites" + File.separator + "temp" + File.separator + "fonts";
    public static final String i = ".lakiwebsites" + File.separator + "temp" + File.separator + "proj";
    public static final String j = ".lakiwebsites" + File.separator + "temp" + File.separator + "data";
    public static final String l = ".lakiwebsites" + File.separator + "resources";
    public static final String m = ".lakiwebsites" + File.separator + "resources" + File.separator + "icons";
    public static final String n = ".lakiwebsites" + File.separator + "resources" + File.separator + "images";
    public static final String o = ".lakiwebsites" + File.separator + "resources" + File.separator + "sounds";
    public static final String p = ".lakiwebsites" + File.separator + "resources" + File.separator + "fonts";
    public static final String r = ".lakiwebsites" + File.separator + "download" + File.separator + "apk";
    public static final String s = ".lakiwebsites" + File.separator + "download" + File.separator + "data";
    public static final String t = ".lakiwebsites" + File.separator + "tutorial" + File.separator + "images";
    public static final String u = ".lakiwebsites" + File.separator + "tutorial" + File.separator + "sounds";
    public static final String v = ".lakiwebsites" + File.separator + "tutorial" + File.separator + "fonts";
    public static final String w = ".lakiwebsites" + File.separator + "tutorial" + File.separator + "proj";
    public static final String x = ".lakiwebsites" + File.separator + "collection";
    public static final String y = "lakiwebsites" + File.separator + "localization";
    public static final String z = "lakiwebsites" + File.separator + "localization" + File.separator + "strings.xml";

    public static final String EXTRA_SYSTEM_DATA = ".lakiwebsites" + File.separator + "data" + File.separator + "system";
    public static final String CUSTOM_COMPONENT_FILE = EXTRA_SYSTEM_DATA + File.separator + "component.json";
    public static final String EXTRA_DATA_EXPORT = EXTRA_SYSTEM_DATA + File.separator + "export";

    /**
     * @return The physical root path of the IDE on external storage.
     */
    public static String getExternalRoot() {
        return new File(Environment.getExternalStorageDirectory(), ROOT).getAbsolutePath();
    }

    public static String getAbsolutePathOf(String path) {
        return new File(Environment.getExternalStorageDirectory(), path).getAbsolutePath();
    }

    /**
     * Resolves the root folder for a specific project.
     * Convention: /.lakiwebsites/[simple|advance]/[ProjectName]_[sc_id]/
     */
    public static String getProjectRoot(String projectName, String sc_id, boolean isComplex) {
        String subRoot = isComplex ? SUB_COMPLEX : SUB_SIMPLE;
        String folderName = (projectName == null || projectName.isEmpty()) ? sc_id : projectName + "_" + sc_id;
        return getExternalRoot() + File.separator + subRoot + File.separator + folderName;
    }

    public static String getSystemDataPath(String sc_id) {
        return getExternalRoot() + File.separator + DIR_DATA + File.separator + sc_id;
    }

    public static String getSystemMetadataPath(String sc_id) {
        return getExternalRoot() + File.separator + DIR_MYSYNC + File.separator + DIR_LIST + File.separator + sc_id;
    }

    public static String getProjectListPath() {
        return getExternalRoot() + File.separator + DIR_MYSYNC + File.separator + DIR_LIST;
    }

    public static String a() {
        return getAbsolutePathOf(x);
    }

    public static String a(int type) {
        return switch (type) {
            case 1 -> "SL-01";
            case 2 -> "SL-02";
            case 3 -> "SL-03";
            case 4 -> "SL-04";
            default -> "";
        };
    }

    public static String a(String sc_id) {
        return getAbsolutePathOf(e + File.separator + sc_id);
    }

    public static void a(Context context, String preferenceName) {
        DB dataP17 = new DB(context, "P17_" + preferenceName);
        DB dataP18 = new DB(context, "P18_" + preferenceName);
        DB dataP13 = new DB(context, "P13_" + preferenceName);
        DB dataP14 = new DB(context, "P14_" + preferenceName);
        dataP17.a();
        dataP18.a();
        dataP13.a();
        dataP14.a();
        DB dataD03 = new DB(context, "D03_" + preferenceName);
        DB dataD04 = new DB(context, "D04_" + preferenceName);
        DB dataD01 = new DB(context, "D01_" + preferenceName);
        DB dataD02 = new DB(context, "D02_" + preferenceName);
        dataD03.a();
        dataD04.a();
        dataD01.a();
        dataD02.a();
    }

    public static String b() {
        return getAbsolutePathOf(s);
    }

    public static String b(int type) {
        return switch (type) {
            case 0 -> "linear";
            case 1 -> "relativelayout";
            case 2 -> "hscroll";
            case 3 -> "button";
            case 4 -> "textview";
            case 5 -> "edittext";
            case 6 -> "imageview";
            case 7 -> "webview";
            case 8 -> "progressbar";
            case 9 -> "listview";
            case 10 -> "spinner";
            case 11 -> "checkbox";
            case 12 -> "vscroll";
            case 13 -> "switch";
            case 14 -> "seekbar";
            case 15 -> "calendarview";
            case 16 -> "fab";
            case 17 -> "adview";
            case 18 -> "mapview";
            case 19 -> "radiobutton";
            case 20 -> "ratingbar";
            case 21 -> "videoview";
            case 22 -> "searchview";
            case 23 -> "autocomplete";
            case 24 -> "multiautocomplete";
            case 25 -> "gridview";
            case 26 -> "analogclock";
            case 27 -> "datepicker";
            case 28 -> "timepicker";
            case 29 -> "digitalclock";
            case 30 -> "tablayout";
            case 31 -> "viewpager";
            case 32 -> "bottomnavigation";
            case 33 -> "badgeview";
            case 34 -> "patternlockview";
            case 35 -> "wavesidebar";
            case 36 -> "cardview";
            case 37 -> "collapsingtoolbar";
            case 38 -> "textinputlayout";
            case 39 -> "swiperefreshlayout";
            case 40 -> "radiogroup";
            case 41 -> "materialbutton";
            case 42 -> "signinbutton";
            case 43 -> "circleimageview";
            case 44 -> "lottie";
            case 45 -> "youtube";
            case 46 -> "otpview";
            case 47 -> "codeview";
            case 48 -> "recyclerview";
            default -> "widget";
        };
    }

    public static String b(String sc_id) {
        return getSystemDataPath(sc_id);
    }

    public static String c() {
        return getAbsolutePathOf(ROOT + File.separator + "download");
    }

    public static String c(String sc_id) {
        return getSystemMetadataPath(sc_id);
    }

    public static String d() {
        return getAbsolutePathOf(p);
    }

    public static String d(String sc_id) {
        return getAbsolutePathOf(b + File.separator + sc_id);
    }

    public static String f(String projectName) {
        File simpleDir = new File(getAbsolutePathOf(simple));
        if (simpleDir.exists() && simpleDir.isDirectory()) {
            File[] files = simpleDir.listFiles();
            if (files != null) {
                for (File f : files) {
                    if (f.getName().startsWith(projectName + "_")) {
                        return f.getAbsolutePath();
                    }
                }
            }
        }
        return getAbsolutePathOf(simple + File.separator + projectName);
    }

    public static String f(String projectName, String sc_id) {
        return f(projectName, sc_id, false);
    }

    public static String f(String projectName, String sc_id, boolean isComplex) {
        return getProjectRoot(projectName, sc_id, isComplex);
    }

    public static String simple() {
        return getAbsolutePathOf(simple);
    }

    public static String e() {
        return getAbsolutePathOf(m);
    }

    public static String e(String sc_id) {
        return "resource" + File.separator + sc_id + File.separator + "res.zip";
    }

    public static String getExtractedIconPackStoreLocation() {
        return new File(SketchApplication.getContext().getFilesDir(), "iconpack").getAbsolutePath();
    }

    public static String g() {
        return getAbsolutePathOf(n);
    }

    public static String h() {
        return "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAxqe7Fu3i3VfnKRSRRljTsMuk7Br0dXaFdGnMNCzMGLQ72PSTEAUo4sXs5+Utmdf9R2s2tZyArdnehk9+Q72F0XzEZGeVfgzfLky7ffuk04yxUye/FlXBun/s0F7g2496+PyfXCP9jIBdncvQ9kaT8Xn6F/j0s2TqS/6xlCD38eYgCVFyp1mld1vYhGCZBlQvXFVJAoKCzqN2QZVO5KarkyTQSGeudvV/UQsJgyHh5zTZKnla1VIVj1Wl3nBb//s2dsmFnAx3500Y/h//XHveLUS7BkP34AGGWPLuoyJruLNvrZ3uUNDnCgnW4+z8Ilaj2SwCTeqQvvw/suZdExs88QIDAQAB";
    }

    @Deprecated
    public static String i() {
        return getAbsolutePathOf(C);
    }

    public static String j() {
        return getAbsolutePathOf(D);
    }

    public static String k() {
        return getAbsolutePathOf(y);
    }

    public static String l() {
        return getAbsolutePathOf(z);
    }

    public static String m() {
        return getAbsolutePathOf(A);
    }

    public static String n() {
        return getAbsolutePathOf(c);
    }

    public static String o() {
        return getAbsolutePathOf(B);
    }

    public static String p() {
        return getAbsolutePathOf(E);
    }

    public static String q() {
        return getAbsolutePathOf(".lakiwebsites" + File.separator);
    }

    public static String r() {
        return getAbsolutePathOf(a);
    }

    public static String s() {
        return getAbsolutePathOf("lakiwebsites");
    }

    public static String t() {
        return getAbsolutePathOf(o);
    }

    public static String u() {
        return getAbsolutePathOf(h);
    }

    public static String v() {
        return getAbsolutePathOf(f);
    }

    public static String w() {
        return getAbsolutePathOf(g);
    }

    public static String x() {
        return getAbsolutePathOf(F);
    }

    public static String getCustomComponent() {
        return getAbsolutePathOf(CUSTOM_COMPONENT_FILE);
    }

    public static String getExtraDataExport() {
        return getAbsolutePathOf(EXTRA_DATA_EXPORT);
    }

    public static String y() {
        return "16011998";
    }

    public static String[] z() {
        return new String[]{"Blowfish", "Blowfish/CBC/PKCS5Padding"};
    }

    // --- Path Resolvers for Simple Projects ---

    public static String getHtmlPath(String projectRoot) {
        return projectRoot + File.separator + DIR_HTML;
    }

    public static String getCssPath(String projectRoot) {
        return projectRoot + File.separator + DIR_CSS;
    }

    public static String getAssetsPath(String projectRoot) {
        return projectRoot + File.separator + DIR_ASSETS;
    }

    public static String getSettingsPath(String projectRoot) {
        return projectRoot + File.separator + DIR_SETTINGS;
    }

    public static String getImagesPath(String projectRoot) {
        return getAssetsPath(projectRoot) + File.separator + SUB_IMAGES;
    }

    /**
     * Resolves path for page-specific block data.
     * Result: /[ProjectRoot]/settings/[pageName]_extendcssblocks.json
     */
    public static String getPageExtendBlocksPath(String projectRoot, String pageName) {
        return getSettingsPath(projectRoot) + File.separator + pageName + "_" + FILE_EXTEND_CSS_BLOCKS;
    }

    /**
     * Resolves path for page-specific HTML tag settings.
     * Result: /[ProjectRoot]/settings/[pageName]_htmltags.json
     */
    public static String getPageHtmlTagsPath(String projectRoot, String pageName) {
        return getSettingsPath(projectRoot) + File.separator + pageName + "_" + FILE_HTML_TAGS;
    }

    /**
     * Resolves path for page-specific HTML head configurations.
     * Result: /[ProjectRoot]/settings/[pageName]_htmlhead.json
     */
    public static String getPageHtmlHeadPath(String projectRoot, String pageName) {
        return getSettingsPath(projectRoot) + File.separator + pageName + "_" + FILE_HTML_HEAD;
    }

    // --- Future Path Resolvers for Advance Projects ---

    public static String getReactPath(String projectRoot) {
        return projectRoot + File.separator + DIR_REACT;
    }

    public static String getAngularPath(String projectRoot) {
        return projectRoot + File.separator + DIR_ANGULAR;
    }

    public static String getReactSrcPath(String reactRoot) {
        return reactRoot + File.separator + SUB_REACT_SRC;
    }

    public static String getAngularAppPath(String angularRoot) {
        return angularRoot + File.separator + SUB_ANGULAR_APP;
    }

    // --- Physical Creation Logic (Centralized Builder) ---

    /**
     * Physically creates the folder structure for a simple web project.
     * This ensures consistency between Create, Backup, and Restore.
     */
    public static void createSimpleProjectStructure(String projectRoot) {
        mkdir(projectRoot);
        mkdir(getHtmlPath(projectRoot));
        mkdir(getCssPath(projectRoot));
        mkdir(getSettingsPath(projectRoot));

        String assets = getAssetsPath(projectRoot);
        mkdir(assets);
        mkdir(assets + File.separator + SUB_IMAGES);
        mkdir(assets + File.separator + SUB_FONTS);
        mkdir(assets + File.separator + SUB_ICONS);
        mkdir(assets + File.separator + SUB_SOUNDS);
    }

    /**
     * Initializes a new web page with default settings files.
     * Creates: [page]_extendcssblocks.json, [page]_htmltags.json, [page]_htmlhead.json
     */
    public static void initializePageSettings(String projectRoot, String pageName) {
        saveFile(getPageExtendBlocksPath(projectRoot, pageName), "[]");
        saveFile(getPageHtmlTagsPath(projectRoot, pageName), "[]");
        saveFile(getPageHtmlHeadPath(projectRoot, pageName), "{}");
    }

    /**
     * Creates the initial HTML and CSS files for a new project/page.
     */
    public static void createInitialWebFiles(String projectRoot, String pageName) {
        String htmlFile = getHtmlPath(projectRoot) + File.separator + pageName + ".html";
        String cssFile = getCssPath(projectRoot) + File.separator + pageName + ".css";

        // Initial HTML content boilerplate
        String htmlContent = "<!DOCTYPE html>\n<html lang=\"en\">\n<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                "    <title>" + pageName + "</title>\n" +
                "    <link rel=\"stylesheet\" href=\"../css/" + pageName + ".css\">\n" +
                "</head>\n<body>\n\n</body>\n</html>";

        // Initial CSS content placeholder
        String cssContent = "/* CSS for " + pageName + " */\nbody {\n    margin: 0;\n    padding: 0;\n}";

        saveFile(htmlFile, htmlContent);
        saveFile(cssFile, cssContent);
    }

    /**
     * Helper to create a directory if it doesn't exist.
     */
    private static void mkdir(String path) {
        File f = new File(path);
        if (!f.exists()) {
            f.mkdirs();
        }
    }

    /**
     * Helper to write a simple string to a file.
     */
    private static void saveFile(String path, String content) {
        try (java.io.FileWriter writer = new java.io.FileWriter(path)) {
            writer.write(content);
        } catch (java.io.IOException e) {
            android.util.Log.e("LakiFiles", "Error writing file: " + path, e);
        }
    }
}
