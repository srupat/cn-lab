import java.io.*;
import java.net.*;

public class UDPServer {
    public static void main(String[] args) {
        DatagramSocket socket = null;
        try {
            socket = new DatagramSocket(9876);
            byte[] receiveData = new byte[1024];

            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
            socket.receive(receivePacket);
            String fileName = new String(receivePacket.getData(), 0, receivePacket.getLength());
            System.out.println("Receiving file: " + fileName);

            FileOutputStream fos = new FileOutputStream(fileName);
            BufferedOutputStream bos = new BufferedOutputStream(fos);

            boolean receiving = true;
            while (receiving) {
                receivePacket = new DatagramPacket(receiveData, receiveData.length);
                socket.receive(receivePacket);

                String receivedString = new String(receivePacket.getData(), 0, receivePacket.getLength());

                if (receivedString.equals("END")) {
                    System.out.println("File transfer completed.");
                    receiving = false;
                } else {
                    bos.write(receivePacket.getData(), 0, receivePacket.getLength());
                }
            }

            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
        }
    }
}

