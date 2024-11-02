import java.io.*;
import java.net.*;
import java.util.*;

public class DynamicHTTPServer {

    public static void main(String[] args) throws IOException {

        int port = 8080;
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server started on port " + port + "...");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                new ClientHandler(clientSocket).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

class ClientHandler extends Thread {
    private Socket clientSocket;

    public ClientHandler(Socket socket) {
        this.clientSocket = socket;
    }

    public void run() {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {

            String requestLine = in.readLine();
            if (requestLine == null) return;

            System.out.println("Request: " + requestLine);

            Map<String, String> headers = new HashMap<>();
            String headerLine;
            while (!(headerLine = in.readLine()).equals("")) {
                String[] headerParts = headerLine.split(": ", 2);
                if (headerParts.length == 2) {
                    headers.put(headerParts[0], headerParts[1]);
                }
            }

            String[] requestParts = requestLine.split(" ");
            if (requestParts.length != 3) {
                sendErrorResponse(out, "400 Bad Request");
                return;
            }

            String method = requestParts[0];
            String path = requestParts[1];
            String httpVersion = requestParts[2];

            handleRequest(method, path, httpVersion, out);

            clientSocket.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleRequest(String method, String path, String httpVersion, PrintWriter out) {
        String responseBody;
        String statusLine;

        if (!httpVersion.matches("HTTP/\\d\\.\\d")) {
            sendErrorResponse(out, "505 HTTP Version Not Supported");
            return;
        }

        switch (httpVersion) {
            case "HTTP/1.0":
            case "HTTP/1.1":
            case "HTTP/2.0":
                if (method.equals("GET")) {
                    responseBody = "<html><body><h1>Response from " + path + " using " + httpVersion + "</h1></body></html>";
                    statusLine = httpVersion + " 200 OK";
                } else {
                    sendErrorResponse(out, "405 Method Not Allowed");
                    return;
                }
                break;
            default:
                sendErrorResponse(out, "400 Bad Request");
                return;
        }

        out.println(statusLine);
        out.println("Content-Type: text/html");
        out.println("Content-Length: " + responseBody.length());
        out.println("");
        out.println(responseBody);
    }

    private void sendErrorResponse(PrintWriter out, String status) {
        String responseBody = "<html><body><h1>" + status + "</h1></body></html>";
        out.println(status);
        out.println("Content-Type: text/html");
        out.println("Content-Length: " + responseBody.length());
        out.println("");
        out.println(responseBody);
    }
}
