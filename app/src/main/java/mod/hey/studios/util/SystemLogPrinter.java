package mod.hey.studios.util;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import mod.jbk.util.LogUtil;
import laki.webide.utility.FileUtil;

public class SystemLogPrinter {

    private static final String PATH = FileUtil.getExternalStorageDir().concat("/.lakiwebsites/debug.txt");
    private static PrintStream ps;

    public static void start() {
        start(PATH);
    }

    public static void start(String path) {
        // Reset the log file
        FileUtil.writeFile(path, "");

        try {
            // Use FileOutputStream instead of FileWriter
            ps = new PrintStream(new FileOutputStream(path, true), true);
            System.setOut(ps);
            System.setErr(ps);
        } catch (IOException e) {
            LogUtil.e("SystemLogPrinter", "IOException while creating PrintStream to " + path, e);
        }
    }

    public static void stop() {
        if (ps != null) {
            ps.close();
            ps = null;
        }
    }
}
