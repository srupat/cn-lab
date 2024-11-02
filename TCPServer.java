import java.io.*;
import java.net.*;

public class TCPServer {
    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(12345)) {
            System.out.println("Server is listening on port 12345");

            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("Client connected");
                new ClientHandler(socket).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

class ClientHandler extends Thread {
    private Socket socket;

    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    public void run() {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

            String clientMessage;
            while ((clientMessage = in.readLine()) != null) {
                System.out.println("Client: " + clientMessage);

                if (clientMessage.equalsIgnoreCase("hello")) {
                    out.println("Hello from server!");
                } else if (clientMessage.equalsIgnoreCase("file")) {
                    receiveFile(socket);
                } else {
                    out.println("Unknown command");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void receiveFile(Socket socket) throws IOException {
        try (InputStream inputStream = socket.getInputStream();
             DataInputStream dataInputStream = new DataInputStream(inputStream)) {
            String fileName = dataInputStream.readUTF(); // Read file name
            File file = new File(fileName);
            try (FileOutputStream fos = new FileOutputStream(file);
                 BufferedOutputStream bos = new BufferedOutputStream(fos)) {
                byte[] buffer = new byte[4096];
                int bytesRead;
                while ((bytesRead = dataInputStream.read(buffer)) != -1) {
                    bos.write(buffer, 0, bytesRead);
                }
                System.out.println("File " + fileName + " received.");
            }
        }
    }
}
