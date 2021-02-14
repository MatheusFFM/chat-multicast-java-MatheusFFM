package network;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    public static void main(String[] args) {
        int port = 6875;
        ServerSocket listenSocket = null;

        try {
            listenSocket = new ServerSocket(port);
            System.out.println("Servidor ouvindo a porta - TCP: " + port);

            while (true){
                Socket clientSocket = listenSocket.accept();
                new Connection(clientSocket);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
