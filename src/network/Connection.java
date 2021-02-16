package network;

import models.Room;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Connection extends Thread {
    private DataInputStream in;
    private DataOutputStream out;
    private Socket clientSocket;
    private static List<Room> rooms = new ArrayList<Room>();

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
                    String data = in.readUTF();
                    String command, content;
                    if(data.startsWith("/")) {
                        String[] splitedDate = data.split(" ");
                        command = splitedDate[0];
                        StringBuilder sb = new StringBuilder();
                        for (int i = 1; i < splitedDate.length; i++) {
                            sb.append(splitedDate[i]);
                        }
                        content = sb.toString();
                        switch (command){
                            case Commands.CREATE_ROOM:
                                System.out.println("CREATE ROOM " + content);
                                break;
                            case Commands.EXIT:
                                System.out.println("EXIT ROOM");
                                break;
                            case Commands.JOIN:
                                System.out.println("JOIN THE ROOM " + content);
                                out.writeUTF(content);
                                break;
                            case Commands.USERS:
                                System.out.println("SHOW USERS OF " + content);
                                break;
                        }
                    }
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

