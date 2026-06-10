package mod.hey.studios.project.backup;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.besome.sketch.beans.BlockBean;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import a.a.a.lC;
import a.a.a.yB;
import laki.webide.core.LakiFiles;
import laki.webide.utility.FilePathUtil;
import mod.hey.studios.editor.manage.block.ExtraBlockInfo;
import mod.hey.studios.editor.manage.block.v2.BlockLoader;
import mod.hey.studios.project.custom_blocks.CustomBlocksManager;
import mod.hey.studios.util.Helper;
import mod.hilal.saif.activities.tools.ConfigActivity;
import laki.webide.utility.FileUtil;
import laki.webide.utility.SketchwareUtil;

public class BackupFactory {
    public static final String EXTENSION = "swb";
    public static final String DEF_PATH = ".lakiwebsites/backups/";

    final String sc_id;
    File outPath;
    boolean backupLocalLibs;
    boolean backupCustomBlocks;
    String error = "";
    boolean restoreSuccess = true;

    public BackupFactory(String sc_id) {
        this.sc_id = sc_id;
    }

    public static String getBackupDir() {
        return new File(Environment.getExternalStorageDirectory(), ConfigActivity.getBackupPath())
                .getAbsolutePath();
    }

    public static boolean zipContainsFile(String zipPath, String search) {
        try (ZipFile zipFile = new ZipFile(zipPath)) {
            Enumeration<? extends ZipEntry> entries = zipFile.entries();
            while (entries.hasMoreElements()) {
                ZipEntry entry = entries.nextElement();
                if (entry.getName().contains(search)) {
                    return true;
                }
            }
        } catch (IOException e) {
            return false;
        }
        return false;
    }

    private static File getAllLocalLibsDir() {
        return new File(Environment.getExternalStorageDirectory(), ".lakiwebsites/libs/local_libs");
    }

    private static HashMap<String, Object> getProject(File file) {
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            byte[] key = "sketchwaresecure".getBytes();
            cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(key, "AES"), new IvParameterSpec(key));
            byte[] encrypted;
            try (RandomAccessFile raf = new RandomAccessFile(file, "r")) {
                encrypted = new byte[(int) raf.length()];
                raf.readFully(encrypted);
            }
            byte[] decrypted = cipher.doFinal(encrypted);
            return new Gson().fromJson(new String(decrypted).trim(), Helper.TYPE_MAP);
        } catch (Exception e) {
            return null;
        }
    }

    private static boolean writeEncrypted(File file, String string) {
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            byte[] key = "sketchwaresecure".getBytes();
            cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(key, "AES"), new IvParameterSpec(key));
            byte[] encrypted = cipher.doFinal((string.trim()).getBytes());
            try (RandomAccessFile raf = new RandomAccessFile(file, "rw")) {
                raf.setLength(0);
                raf.write(encrypted);
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean unzip(File zipFile, File destinationDir) {
        int DEFAULT_BUFFER = 2048;
        try (ZipFile zip = new ZipFile(zipFile)) {
            destinationDir.mkdirs();
            Enumeration<? extends ZipEntry> zipFileEntries = zip.entries();
            while (zipFileEntries.hasMoreElements()) {
                ZipEntry entry = zipFileEntries.nextElement();
                File destFile = new File(destinationDir, entry.getName());
                File destinationParent = destFile.getParentFile();
                if (destinationParent != null && !destinationParent.exists()) {
                    destinationParent.mkdirs();
                }
                if (!entry.isDirectory()) {
                    try (BufferedInputStream is = new BufferedInputStream(zip.getInputStream(entry))) {
                        try (FileOutputStream fos = new FileOutputStream(destFile);
                             BufferedOutputStream dest = new BufferedOutputStream(fos, DEFAULT_BUFFER)) {
                            int currentByte;
                            byte[] data = new byte[DEFAULT_BUFFER];
                            while ((currentByte = is.read(data, 0, DEFAULT_BUFFER)) != -1) {
                                dest.write(data, 0, currentByte);
                            }
                            dest.flush();
                        }
                    }
                }
            }
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public static void zipFolder(File srcFolder, File destZipFile) throws Exception {
        try (FileOutputStream fileWriter = new FileOutputStream(destZipFile);
             ZipOutputStream zip = new ZipOutputStream(fileWriter)) {
            addFolderToZip(srcFolder, srcFolder, zip);
            zip.flush();
        }
    }

    private static void addFileToZip(File rootPath, File srcFile, ZipOutputStream zip) throws Exception {
        if (srcFile.isDirectory()) {
            addFolderToZip(rootPath, srcFile, zip);
        } else {
            byte[] buf = new byte[1024];
            int len;
            try (FileInputStream in = new FileInputStream(srcFile)) {
                String name = srcFile.getPath().substring(rootPath.getPath().length() + 1);
                zip.putNextEntry(new ZipEntry(name));
                while ((len = in.read(buf)) > 0) {
                    zip.write(buf, 0, len);
                }
            }
        }
    }

    private static void addFolderToZip(File rootPath, File srcFolder, ZipOutputStream zip) throws Exception {
        File[] srcFolderFiles = srcFolder.listFiles();
        if (srcFolderFiles != null) {
            for (File fileName : srcFolderFiles) {
                addFileToZip(rootPath, fileName, zip);
            }
        }
    }

    public static void copySafe(File source, File destination) {
        if (!source.exists()) {
            destination.mkdirs();
            FileUtil.writeFile(new File(destination, ".nomedia").getAbsolutePath(), "");
        } else {
            copy(source, destination);
        }
    }

    public static void copy(File source, File destination) {
        if (source.isDirectory()) {
            if (!destination.exists()) destination.mkdirs();
            String[] files = source.list();
            if (files != null) {
                for (String file : files) {
                    copy(new File(source, file), new File(destination, file));
                }
            }
        } else {
            if (source.getName().equals(".nomedia")) return;
            try (InputStream in = new FileInputStream(source);
                 OutputStream out = new FileOutputStream(destination)) {
                byte[] buffer = new byte[1024];
                int length;
                while ((length = in.read(buffer)) > 0) {
                    out.write(buffer, 0, length);
                }
            } catch (IOException e) {
                Log.e("BackupFactory", "Copy failed", e);
            }
        }
    }

    /************************ BACKUP ************************/

    public void backup(Context context, String project_name) {
        String versionName = yB.c(lC.b(sc_id), "sc_ver_name");
        String versionCode = yB.c(lC.b(sc_id), "sc_ver_code");
        String pkgName = yB.c(lC.b(sc_id), "my_sc_pkg_name");
        String projectNameOnly = project_name.replace("_d", "").replace(File.separator, "");
        
        String finalFileName = projectNameOnly + " v" + versionName + " (" + pkgName + ", " + versionCode + ") " + getFormattedDateFrom("yyyy-MM-dd'T'HHmmss");
        createBackupsFolder();

        File outFolder = new File(getBackupDir(), project_name + "_temp");
        File outZip = new File(getBackupDir() + File.separator + projectNameOnly, finalFileName + "." + EXTENSION);

        if (outZip.exists()) {
            backup(context, project_name + "_d");
            return;
        }

        if (outFolder.exists()) FileUtil.deleteFile(outFolder.getAbsolutePath());
        FileUtil.makeDir(outFolder.getAbsolutePath());
        FileUtil.makeDir(new File(getBackupDir() + File.separator + projectNameOnly).getAbsolutePath());

        // 1. Metadata (From Sketchware Database)
        copy(new File(LakiFiles.c(sc_id), "project"), new File(outFolder, "project"));

        // 2. Settings (Web Project Settings)
        copySafe(new File(FilePathUtil.getProjectRoot(sc_id), "settings"), new File(outFolder, "settings"));

        // 3. History & View Data (Crucial for Undo/Redo)
        copySafe(new File(LakiFiles.b(sc_id)), new File(outFolder, "data_history"));

        // 4. Web Folders
        String root = FilePathUtil.getProjectRoot(sc_id);
        copySafe(new File(root, LakiFiles.DIR_HTML), new File(outFolder, LakiFiles.DIR_HTML));
        copySafe(new File(root, LakiFiles.DIR_CSS), new File(outFolder, LakiFiles.DIR_CSS));
        copySafe(new File(root, LakiFiles.DIR_ASSETS), new File(outFolder, LakiFiles.DIR_ASSETS));

        try {
            zipFolder(outFolder, outZip);
            outPath = outZip;
        } catch (Exception e) {
            error = Log.getStackTraceString(e);
            outPath = null;
        } finally {
            FileUtil.deleteFile(outFolder.getAbsolutePath());
        }
    }

    /************************ RESTORE ************************/

    public void restore(File swbPath) {
        restore(swbPath, swbPath.getName().replace("." + EXTENSION, ""));
    }

    private void restore(File swbPath, String name) {
        createBackupsFolder();
        File tempFolder = new File(getBackupDir(), name + "_restore_temp");
        if (tempFolder.exists()) {
            restore(swbPath, name + "_d");
            return;
        }

        if (!unzip(swbPath, tempFolder)) {
            error = "couldn't unzip the backup";
            restoreSuccess = false;
            return;
        }

        File projectFile = new File(tempFolder, "project");
        HashMap<String, Object> map = getProject(projectFile);
        if (map == null) {
            error = "couldn't read project metadata";
            restoreSuccess = false;
            return;
        }

        // 1. Register project in Sketchware Database
        map.put("sc_id", sc_id);
        lC.a(sc_id, map); 

        // 2. Restore Metadata to system list
        String systemMetadataPath = LakiFiles.c(sc_id);
        FileUtil.makeDir(systemMetadataPath);
        writeEncrypted(new File(systemMetadataPath, "project"), new Gson().toJson(map));

        // 3. Restore History & View Logic data
        String systemDataPath = LakiFiles.b(sc_id);
        copy(new File(tempFolder, "data_history"), new File(systemDataPath));

        // 4. Resolve and create professional structure
        String projectRoot = FilePathUtil.getProjectRoot(sc_id);
        LakiFiles.createSimpleProjectStructure(projectRoot);

        // 4. Copy restored components to the new structure
        copy(new File(tempFolder, LakiFiles.DIR_SETTINGS), new File(projectRoot, LakiFiles.DIR_SETTINGS));
        copy(new File(tempFolder, LakiFiles.DIR_HTML), new File(projectRoot, LakiFiles.DIR_HTML));
        copy(new File(tempFolder, LakiFiles.DIR_CSS), new File(projectRoot, LakiFiles.DIR_CSS));
        copy(new File(tempFolder, LakiFiles.DIR_ASSETS), new File(projectRoot, LakiFiles.DIR_ASSETS));
        
        // Finalize metadata inside project root
        writeEncrypted(new File(projectRoot, "project"), new Gson().toJson(map));

        FileUtil.deleteFile(tempFolder.getAbsolutePath());
        restoreSuccess = true;
    }

    private String getFormattedDateFrom(String format) {
        return new SimpleDateFormat(format, Locale.ENGLISH).format(Calendar.getInstance().getTime());
    }

    public File getOutFile() { return outPath; }
    public void setBackupLocalLibs(boolean b) { backupLocalLibs = b; }
    public void setBackupCustomBlocks(boolean b) { backupCustomBlocks = b; }
    public String getError() { return error; }
    public boolean isRestoreSuccess() { return restoreSuccess; }

    private void createBackupsFolder() {
        FileUtil.makeDir(getBackupDir());
    }

    private File getDataDir() { return new File(FilePathUtil.getProjectRoot(sc_id), LakiFiles.DIR_SETTINGS); }
    private File getProjectPath() { return new File(FilePathUtil.getProjectRoot(sc_id), "project"); }
    private File getLocalLibsPath() { return new File(FilePathUtil.getProjectRoot(sc_id), LakiFiles.DIR_SETTINGS + File.separator + "local_library"); }
}
