package network;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    public static final int PORT = 6875;

    public static void main(String[] args) {
        ServerSocket listenSocket = null;

        try {
            listenSocket = new ServerSocket(PORT);
            System.out.println("Server on port - TCP: " + PORT);

            while (true){
                Socket clientSocket = listenSocket.accept();
                new Connection(clientSocket);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
