import java.io.*;
import java.net.*;
import java.util.*;

public class DynamicHTTPClient {

    public static void main(String[] args) throws IOException {
        // Example input for dynamic testing
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter server host: ");
        String host = scanner.nextLine();

        System.out.print("Enter server port: ");
        int port = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Enter HTTP method (e.g., GET): ");
        String method = scanner.nextLine();

        System.out.print("Enter path (e.g., /index.html): ");
        String path = scanner.nextLine();

        System.out.print("Enter HTTP version (e.g., HTTP/1.1): ");
        String httpVersion = scanner.nextLine();

        System.out.print("Do you want to add custom headers? (yes/no): ");
        boolean addHeaders = scanner.nextLine().equalsIgnoreCase("yes");

        Map<String, String> headers = new HashMap<>();
        if (addHeaders) {
            while (true) {
                System.out.print("Enter header key (or 'done' to finish): ");
                String key = scanner.nextLine();
                if (key.equalsIgnoreCase("done")) break;

                System.out.print("Enter header value: ");
                String value = scanner.nextLine();
                headers.put(key, value);
            }
        }

        sendRequest(host, port, method, path, httpVersion, headers);

        scanner.close();
    }

    private static void sendRequest(String host, int port, String method, String path, String httpVersion, Map<String, String> headers) throws IOException {
        Socket socket = new Socket(host, port);

        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        String request = method + " " + path + " " + httpVersion;
        System.out.println("Sending request: " + request);
        out.println(request);
        out.println("Host: " + host);

        for (Map.Entry<String, String> header : headers.entrySet()) {
            out.println(header.getKey() + ": " + header.getValue());
        }

        out.println("Connection: close");
        out.println("");

        String responseLine;
        while ((responseLine = in.readLine()) != null) {
            System.out.println(responseLine);
        }
        socket.close();
    }
}
