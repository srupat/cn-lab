import java.io.*;
import java.net.*;

public class UDPClient {
    public static void main(String[] args) {
        DatagramSocket socket = null;
        try {
            socket = new DatagramSocket();

            InetAddress IPAddress = InetAddress.getByName("localhost");
            byte[] sendData = new byte[1024];

            String fileName = "D://Srujan//Code//GitHub//cn-lab//encodings.py";
            System.out.println("Sending file: " + fileName);

            sendData = fileName.getBytes();
            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, 9876);
            socket.send(sendPacket);

            FileInputStream fis = new FileInputStream(fileName);
            BufferedInputStream bis = new BufferedInputStream(fis);

            int bytesRead;
            while ((bytesRead = bis.read(sendData)) != -1) {
                sendPacket = new DatagramPacket(sendData, bytesRead, IPAddress, 9876);
                socket.send(sendPacket);
            }

            String endMessage = "END";
            sendData = endMessage.getBytes();
            DatagramPacket endPacket = new DatagramPacket(sendData, sendData.length, IPAddress, 9876);
            socket.send(endPacket);

            System.out.println("File sent successfully.");

            bis.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
        }
    }
}
