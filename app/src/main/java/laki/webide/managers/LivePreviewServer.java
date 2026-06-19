package laki.webide.managers;

import android.content.Context;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import laki.webide.core.LakiFiles;

public class LivePreviewServer {
    private static ServerSocket serverSocket;
    private static Thread serverThread;
    private static boolean isRunning = false;
    private static String currentScId;
    private static String currentProjectRoot;
    public static long lastModifiedTime = System.currentTimeMillis();
    private static final int PORT = 8282;

    public static synchronized void startServer(Context context, String scId) {
        if (isRunning && scId.equals(currentScId)) return;
        
        stopServer();
        
        currentScId = scId;
        String projectName = a.a.a.yB.c(a.a.a.lC.b(scId), "my_ws_name");
        currentProjectRoot = LakiFiles.getProjectRoot(projectName, scId, false);
        isRunning = true;
        
        serverThread = new Thread(() -> {
            try {
                serverSocket = new ServerSocket(PORT);
                while (isRunning) {
                    Socket socket = serverSocket.accept();
                    if (!isRunning) {
                        try { socket.close(); } catch (IOException e) {}
                        break;
                    }
                    new Thread(() -> handleConnection(socket)).start();
                }
            } catch (IOException e) {
                // Server stopped
            }
        });
        serverThread.start();
    }

    public static synchronized void stopServer() {
        isRunning = false;
        if (serverSocket != null) {
            try {
                serverSocket.close();
            } catch (IOException e) {}
            serverSocket = null;
        }
        if (serverThread != null) {
            serverThread.interrupt();
            serverThread = null;
        }
    }

    public static String getUrl(String pageName) {
        String htmlName = pageName;
        if (htmlName.endsWith(".xml")) {
            htmlName = htmlName.replace(".xml", ".html");
        } else if (!htmlName.endsWith(".html")) {
            htmlName = htmlName + ".html";
        }
        return "http://localhost:" + PORT + "/html/" + htmlName;
    }

    private static void handleConnection(Socket socket) {
        try (InputStream input = socket.getInputStream();
             OutputStream output = socket.getOutputStream()) {
            
            BufferedReader reader = new BufferedReader(new InputStreamReader(input, StandardCharsets.UTF_8));
            String requestLine = reader.readLine();
            if (requestLine == null || requestLine.isEmpty()) return;
            
            String[] parts = requestLine.split(" ");
            if (parts.length < 2) return;
            
            String method = parts[0];
            String fullPath = parts[1];
            
            int qIdx = fullPath.indexOf('?');
            String path = qIdx != -1 ? fullPath.substring(0, qIdx) : fullPath;
            
            if (path.equals("/__live_check")) {
                String json = "{\"lastModified\":" + lastModifiedTime + "}";
                byte[] responseBytes = json.getBytes(StandardCharsets.UTF_8);
                sendHeaders(output, 200, "application/json", responseBytes.length);
                output.write(responseBytes);
                output.flush();
                return;
            }
            
            if (path.contains("..")) {
                sendError(output, 403, "Forbidden");
                return;
            }
            
            if (path.equals("/")) {
                path = "/html/main.html";
            }
            
            File file = new File(currentProjectRoot, path);
            if (!file.exists() || file.isDirectory()) {
                sendError(output, 404, "Not Found");
                return;
            }
            
            byte[] fileBytes = readFileToBytes(file);
            String mimeType = getMimeType(path);
            
            if (mimeType.equals("text/html")) {
                String htmlStr = new String(fileBytes, StandardCharsets.UTF_8);
                String script = "\n<script>\n" +
                    "  (function() {\n" +
                    "    var lastUpdate = null;\n" +
                    "    function checkUpdate() {\n" +
                    "      fetch('/__live_check?t=' + Date.now())\n" +
                    "        .then(function(r){return r.json();})\n" +
                    "        .then(function(data) {\n" +
                    "          if (lastUpdate && data.lastModified > lastUpdate) {\n" +
                    "            window.location.reload();\n" +
                    "          } else {\n" +
                    "            lastUpdate = data.lastModified;\n" +
                    "          }\n" +
                    "        })\n" +
                    "        .catch(function(err) {})\n" +
                    "        .finally(function() {\n" +
                    "          setTimeout(checkUpdate, 1000);\n" +
                    "        });\n" +
                    "    }\n" +
                    "    checkUpdate();\n" +
                    "  })();\n" +
                    "</script>\n";
                int bodyEndIdx = htmlStr.toLowerCase().lastIndexOf("</body>");
                if (bodyEndIdx != -1) {
                    htmlStr = htmlStr.substring(0, bodyEndIdx) + script + htmlStr.substring(bodyEndIdx);
                } else {
                    htmlStr += script;
                }
                fileBytes = htmlStr.getBytes(StandardCharsets.UTF_8);
            }
            
            sendHeaders(output, 200, mimeType, fileBytes.length);
            output.write(fileBytes);
            output.flush();
            
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (IOException e) {}
        }
    }

    private static void sendHeaders(OutputStream output, int status, String mimeType, int length) throws IOException {
        PrintWriter writer = new PrintWriter(new OutputStreamWriter(output, StandardCharsets.UTF_8), true);
        writer.print("HTTP/1.1 " + status + " OK\r\n");
        writer.print("Content-Type: " + mimeType + "\r\n");
        writer.print("Content-Length: " + length + "\r\n");
        writer.print("Connection: close\r\n");
        writer.print("\r\n");
        writer.flush();
    }

    private static void sendError(OutputStream output, int status, String message) throws IOException {
        byte[] bytes = message.getBytes(StandardCharsets.UTF_8);
        PrintWriter writer = new PrintWriter(new OutputStreamWriter(output, StandardCharsets.UTF_8), true);
        writer.print("HTTP/1.1 " + status + " " + message + "\r\n");
        writer.print("Content-Type: text/plain\r\n");
        writer.print("Content-Length: " + bytes.length + "\r\n");
        writer.print("Connection: close\r\n");
        writer.print("\r\n");
        writer.flush();
        output.write(bytes);
        output.flush();
    }

    private static byte[] readFileToBytes(File file) throws IOException {
        try (FileInputStream fis = new FileInputStream(file)) {
            byte[] data = new byte[(int) file.length()];
            int bytesRead = 0;
            while (bytesRead < data.length) {
                int read = fis.read(data, bytesRead, data.length - bytesRead);
                if (read == -1) break;
                bytesRead += read;
            }
            return data;
        }
    }

    private static String getMimeType(String path) {
        String ext = "";
        int i = path.lastIndexOf('.');
        if (i > 0) ext = path.substring(i + 1).toLowerCase();
        
        return switch (ext) {
            case "html", "htm" -> "text/html";
            case "css" -> "text/css";
            case "js" -> "application/javascript";
            case "png" -> "image/png";
            case "jpg", "jpeg" -> "image/jpeg";
            case "gif" -> "image/gif";
            case "svg" -> "image/svg+xml";
            case "webp" -> "image/webp";
            case "json" -> "application/json";
            default -> "application/octet-stream";
        };
    }
}
