import java.io.*;
import java.net.*;
import java.util.Scanner;

public class TCPClient {
    public static void main(String[] args) {
        try (Socket socket = new Socket("localhost", 12345);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             Scanner scanner = new Scanner(System.in)) {

            String userInput;
            while (true) {
                System.out.println("Enter command (hello, file, exit): ");
                userInput = scanner.nextLine();

                if (userInput.equalsIgnoreCase("exit")) {
                    break;
                }
                out.println(userInput);

                if (userInput.equalsIgnoreCase("file")) {
                    sendFile(socket, scanner);
                } else {
                    String response = in.readLine();
                    System.out.println("Server: " + response);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void sendFile(Socket socket, Scanner scanner) throws IOException {
        System.out.print("Enter file name to send: ");
        String fileName = scanner.nextLine();

        // Send the file name first
        try (DataOutputStream dos = new DataOutputStream(socket.getOutputStream())) {
            dos.writeUTF(fileName); // Send file name to server
            File file = new File(fileName);

            try (FileInputStream fis = new FileInputStream(file);
                 BufferedInputStream bis = new BufferedInputStream(fis)) {
                byte[] buffer = new byte[4096];
                int bytesRead;
                while ((bytesRead = bis.read(buffer)) != -1) {
                    dos.write(buffer, 0, bytesRead);
                }
                System.out.println("File sent.");
            }
        }
    }
}

