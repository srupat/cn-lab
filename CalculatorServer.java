import java.io.*;
import java.net.*;
import java.util.StringTokenizer;

class CalculatorServer {
    public static void main(String[] args) {
        int port = 5000;
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server started and listening on port " + port);
            Socket clientSocket = serverSocket.accept();
            System.out.println("Client connected");

            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

            String clientMessage;
            while ((clientMessage = in.readLine()) != null) {
                System.out.println("Received from client: " + clientMessage);

                // Process the client's request
                String serverResponse = processRequest(clientMessage);
                out.println(serverResponse);  // Send the result back to the client
            }

            clientSocket.close();
            System.out.println("Client disconnected");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Process client request and return the result
    private static String processRequest(String request) {
        StringTokenizer tokenizer = new StringTokenizer(request);
        String command = tokenizer.nextToken();

        switch (command.toLowerCase()) {
            case "add":
                double sum = Double.parseDouble(tokenizer.nextToken()) + Double.parseDouble(tokenizer.nextToken());
                return "Result: " + sum;
            case "sub":
                double difference = Double.parseDouble(tokenizer.nextToken()) - Double.parseDouble(tokenizer.nextToken());
                return "Result: " + difference;
            case "mul":
                double product = Double.parseDouble(tokenizer.nextToken()) * Double.parseDouble(tokenizer.nextToken());
                return "Result: " + product;
            case "div":
                double divisor = Double.parseDouble(tokenizer.nextToken());
                if (divisor == 0) return "Error: Division by zero!";
                double quotient = Double.parseDouble(tokenizer.nextToken()) / divisor;
                return "Result: " + quotient;
            case "sin":
                double sinValue = Math.sin(Math.toRadians(Double.parseDouble(tokenizer.nextToken())));
                return "Result: " + sinValue;
            case "cos":
                double cosValue = Math.cos(Math.toRadians(Double.parseDouble(tokenizer.nextToken())));
                return "Result: " + cosValue;
            case "tan":
                double tanValue = Math.tan(Math.toRadians(Double.parseDouble(tokenizer.nextToken())));
                return "Result: " + tanValue;
            default:
                return "Error: Unsupported operation!";
        }
    }
}
