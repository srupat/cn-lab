import java.io.*;
import java.net.*;

class CalculatorClient {
    public static void main(String[] args) {
        String serverAddress = "192.168.1.4"; // Server address
        int port = 5000; // Port to connect
        try (Socket socket = new Socket(serverAddress, port)) {
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));

            System.out.println("Connected to server");

            String userInput;
            while (true) {
                displayMenu();

                // Get input from user
                System.out.print("Enter command: ");
                userInput = stdIn.readLine();

                // If user types 'exit', break the loop
                if (userInput.equalsIgnoreCase("exit")) {
                    break;
                }

                // Send user input to the server
                out.println(userInput);

                // Receive and display response from the server
                String response = in.readLine();
                System.out.println("Server response: " + response);
            }

            socket.close();
            System.out.println("Disconnected from server");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Method to display available arithmetic and trigonometric functions
    private static void displayMenu() {
        System.out.println("\nAvailable commands:");
        System.out.println("   - add <num1> <num2>  ");
        System.out.println("   - sub <num1> <num2>  ");
        System.out.println("   - mul <num1> <num2>  ");
        System.out.println("   - div <num1> <num2>  ");
        System.out.println("   - sin <angle>       ");
        System.out.println("   - cos <angle>       ");
        System.out.println("   - tan <angle>      ");
        System.out.println("   - exit  ");
        System.out.println();
    }
}
