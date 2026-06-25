package laki.webide.managers;

import android.content.Context;
import android.util.Log;
import java.io.*;
import java.util.Map;
import fi.iki.elonen.NanoHTTPD;
import laki.webide.core.LakiFiles;

public class LivePreviewServer {
    private static InternalServer server;
    private static String currentScId;
    private static String currentProjectRoot;
    public static long lastModifiedTime = System.currentTimeMillis();
    private static final int PORT = 8282;

    public static synchronized void startServer(Context context, String scId) {
        if (server != null && server.isAlive() && scId.equals(currentScId)) return;
        stopServer();
        currentScId = scId;
        String projectName = a.a.a.yB.c(a.a.a.lC.b(scId), "my_ws_name");
        currentProjectRoot = LakiFiles.getProjectRoot(projectName, scId, false);
        
        try {
            server = new InternalServer(PORT);
            server.start(NanoHTTPD.SOCKET_READ_TIMEOUT, false);
            Log.d("LivePreviewServer", "Server started on port " + PORT);
        } catch (IOException e) {
            Log.e("LivePreviewServer", "Couldn't start server", e);
        }
    }

    public static synchronized void stopServer() {
        if (server != null) {
            server.stop();
            server = null;
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

    private static class InternalServer extends NanoHTTPD {
        public InternalServer(int port) {
            super(port);
        }

        @Override
        public Response serve(IHTTPSession session) {
            String uri = session.getUri();
            
            // 1. Live Reload Check
            if (uri.equals("/__live_check")) {
                String json = "{\"lastModified\":" + lastModifiedTime + "}";
                return newFixedLengthResponse(Response.Status.OK, "application/json", json);
            }

            // 2. Handle Root Redirect
            if (uri.equals("/")) {
                uri = "/html/main.html";
            }

            // 3. Resolve File Path
            String relativePath = uri.startsWith("/") ? uri.substring(1) : uri;
            File file = new File(currentProjectRoot, relativePath);

            if (!file.exists() || file.isDirectory()) {
                return newFixedLengthResponse(Response.Status.NOT_FOUND, NanoHTTPD.MIME_PLAINTEXT, "File Not Found: " + uri);
            }

            try {
                String mimeType = getMimeType(uri);
                InputStream inputStream = new FileInputStream(file);
                
                // 4. Inject Live Reload Script into HTML
                if (mimeType.equals("text/html")) {
                    return serveHtmlWithInjection(inputStream);
                }

                // 5. Standard Response with CORS
                Response response = newChunkedResponse(Response.Status.OK, mimeType, inputStream);
                response.addHeader("Access-Control-Allow-Origin", "*");
                response.addHeader("Cache-Control", "no-cache, no-store, must-revalidate");
                return response;

            } catch (FileNotFoundException e) {
                return newFixedLengthResponse(Response.Status.NOT_FOUND, NanoHTTPD.MIME_PLAINTEXT, "Error reading file");
            }
        }

        private Response serveHtmlWithInjection(InputStream inputStream) {
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line).append("\n");
                }
                String html = sb.toString();

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

                int bodyEndIdx = html.toLowerCase().lastIndexOf("</body>");
                if (bodyEndIdx != -1) {
                    html = html.substring(0, bodyEndIdx) + script + html.substring(bodyEndIdx);
                } else {
                    html += script;
                }

                Response response = newFixedLengthResponse(Response.Status.OK, "text/html", html);
                response.addHeader("Access-Control-Allow-Origin", "*");
                response.addHeader("Cache-Control", "no-cache, no-store, must-revalidate");
                return response;
            } catch (IOException e) {
                return newFixedLengthResponse(Response.Status.INTERNAL_ERROR, NanoHTTPD.MIME_PLAINTEXT, "Injection Failed");
            }
        }

        private String getMimeType(String uri) {
            String ext = "";
            int i = uri.lastIndexOf('.');
            if (i > 0) ext = uri.substring(i + 1).toLowerCase();
            
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
                case "woff" -> "font/woff";
                case "woff2" -> "font/woff2";
                case "ttf" -> "font/ttf";
                case "otf" -> "font/otf";
                default -> "application/octet-stream";
            };
        }
    }
}
