package EnDe;

import java.io.*;
import java.net.*;
import java.security.*;
import javax.crypto.Cipher;
import java.util.Base64;

public class RSAEncryptedServer {
    private static final int PORT = 12345;

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server is listening on port " + PORT);

            // Generate RSA key pair
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
            keyGen.initialize(2048);
            KeyPair keyPair = keyGen.generateKeyPair();
            PublicKey publicKey = keyPair.getPublic(); // Public key for clients
            PrivateKey privateKey = keyPair.getPrivate(); // Private key for server

            while (true) {
                Socket socket = serverSocket.accept(); // Accept client connection
                System.out.println("Client connected");

                // Send public key to the client
                ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
                out.writeObject(publicKey);

                // Read encrypted message from client
                ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
                byte[] encryptedMessage = (byte[]) in.readObject();

                // Decrypt the message
                Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
                cipher.init(Cipher.DECRYPT_MODE, privateKey);
                byte[] decryptedMessage = cipher.doFinal(encryptedMessage);
                
                // Print decrypted message
                System.out.println("Received decrypted message: " + new String(decryptedMessage));

                // Close connections
                in.close();
                out.close();
                socket.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
