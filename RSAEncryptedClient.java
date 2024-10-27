package EnDe;

import java.io.*;
import java.net.*;
import java.security.*;
import java.util.Scanner;

import javax.crypto.Cipher;

public class RSAEncryptedClient {
    private static final String SERVER_ADDRESS = "localhost"; // Change to server IP if needed
    private static final int PORT = 12345;

    public static void main(String[] args) {
    	Scanner sc=new Scanner(System.in);
        try (Socket socket = new Socket(SERVER_ADDRESS, PORT)) {
            System.out.println("Connected to the server");

            // Receive public key from the server
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
            PublicKey publicKey = (PublicKey) in.readObject();

            // Message to encrypt and send
            System.out.println("Enter the Data to send to Server: ");
            String message = sc.nextLine();
            System.out.println("Sending message: " + message);

            // Encrypt the message with the public key
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            byte[] encryptedMessage = cipher.doFinal(message.getBytes());

            // Send encrypted message to the server
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            out.writeObject(encryptedMessage);

            // Close connections
            out.close();
            in.close();
            socket.close();
            System.out.println("Message sent to the server");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
