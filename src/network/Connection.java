package network;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;

public class Connection extends Thread {
        private DataInputStream in;
        private DataOutputStream out;
        private Socket clientSocket;

        public Connection(Socket cs) {
            try {
                clientSocket = cs;
                in = new DataInputStream(clientSocket.getInputStream());
                out = new DataOutputStream(clientSocket.getOutputStream());
                this.start();
            } catch (IOException e) {
                System.out.println("Connection: " + e.getMessage());
            }
        }

        public void run() {
            try {
                    String data = in.readUTF();
                    System.out.println("Received: " + data);
                    out.writeUTF(data);
            } catch (EOFException e) {
                System.out.println("EOF:" + e.getMessage());
            } catch (IOException e) {
                System.out.println("readline:" + e.getMessage());
            } finally {
                try {
                    clientSocket.close();
                    System.out.println("Servidor: fechando conexão com cliente.");
                } catch (IOException e) {
                    /* close falhou */
                }
            }
        }
    }

