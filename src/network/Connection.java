package network;

import models.User;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Connection extends Thread {
    private DataInputStream in;
    private DataOutputStream out;
    private Socket clientSocket;
    private static List<User> users = new ArrayList<User>();

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
                while(true){
                    System.out.println("Entrei");
                    String data = in.readUTF();
                    String command;
                    String content;
                    if(data.startsWith("/")){
                        String[] splitedDate = data.split(" ");
                        command = splitedDate[0];
                        StringBuilder sb = new StringBuilder();
                        for(int i = 1; i < splitedDate.length; i++){
                            sb.append(splitedDate[i] + " ");
                        }
                        content = sb.toString();
                        System.out.println("Comando = " + command + "\nContent = " + content);
                    }
                    System.out.println("Received: " + data);
                    out.writeUTF(data);
                }
            } catch (EOFException e) {
                System.out.println("EOF:" + e.getMessage());
            } catch (IOException e) {
                System.out.println("readline:" + e.getMessage());
            } finally {
                try {
                    clientSocket.close();
                    System.out.println("Server: Closing connection");
                } catch (IOException e) {
                    /* close falhou */
                }
            }
        }
    }

