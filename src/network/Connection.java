package network;

import models.IpAddress;
import models.Room;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class Connection extends Thread {
    private DataInputStream in;
    private DataOutputStream out;
    private Socket clientSocket;
    private static List<Room> rooms = new ArrayList<Room>();
    private static Set<IpAddress> ips = new TreeSet<IpAddress>();
    public static final int START_MULTICAST = 224;

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
                                IpAddress newIp = new IpAddress(224,0,0, 1);
                                if(ips.size() == 0){
                                    System.out.println("TA VAZIO");
                                    ips.add(newIp);
                                } else{
                                    System.out.println("TA CHEIO, VAMO VE O PROXIMO");
                                    List<IpAddress> auxIp = new ArrayList<IpAddress>(ips);
                                    newIp = auxIp.get(auxIp.size() - 1).getNext();
                                    ips.add(newIp);
                                }
                                rooms.add(new Room(newIp.toString(), content));
                                System.out.println(newIp);
                                System.out.println(rooms);
                                out.writeUTF(newIp.toString());
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
                            case Commands.LIST_ROOMS:
                                if(rooms.size() == 0){
                                    out.writeUTF("No rooms");
                                } else {
                                    StringBuilder roomsBuilder = new StringBuilder();
                                    for (Room r : rooms) {
                                        roomsBuilder.append("<").append(r).append(" - ").append(r.getAddress()).append("> \n");
                                    }
                                    out.writeUTF(roomsBuilder.toString());
                                }
                                break;
                        }
                    } else {
                        out.writeUTF(data);
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

