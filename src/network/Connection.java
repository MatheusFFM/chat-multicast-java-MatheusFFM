package network;

import models.IpAddress;
import models.Room;
import models.User;

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
    public static final String NO_ROOMS_MESSAGE = "No rooms. Type /room and the room name to create a room";
    public static final String GENERIC_ROOM_NAME = "Generic";

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
                                IpAddress newIp = new IpAddress(START_MULTICAST,0,0, 1);
                                if(ips.size() != 0){
                                    List<IpAddress> auxIp = new ArrayList<IpAddress>(ips);
                                    newIp = auxIp.get(auxIp.size() - 1).getNext();
                                }
                                ips.add(newIp);
                                rooms.add(new Room(newIp, content));
                                out.writeUTF(newIp.toString());
                                break;
                            case Commands.JOIN:
                                String ipFromContent = content.split(Commands.USER)[0];
                                String userFromContent = content.split(Commands.USER)[1];

                                User user = new User(userFromContent);

                                IpAddress ipToEnter = IpAddress.ipFromString(ipFromContent);
                                Room roomToEnter = new Room(ipToEnter, GENERIC_ROOM_NAME);
                                for(Room r: rooms){
                                    if(r.compareTo(roomToEnter) == 0){
                                        r.addUser(user);
                                        out.writeUTF(ipToEnter.toString());
                                    }
                                }
                                out.writeUTF(Commands.ERROR);
                                break;
                            case Commands.USERS_LIST:
                                boolean find = false;
                                IpAddress ipToShow = IpAddress.ipFromString(content);
                                Room roomToShow = new Room(ipToShow, GENERIC_ROOM_NAME);
                                for(Room r: rooms){
                                    if(r.compareTo(roomToShow) == 0){
                                        find = true;
                                        roomToShow = r;
                                        break;
                                    }
                                }
                                out.writeUTF(roomToShow.showUsers());
                                break;
                            case Commands.LIST_ROOMS:
                                if(rooms.size() == 0){
                                    out.writeUTF(NO_ROOMS_MESSAGE);
                                } else {
                                    StringBuilder roomsBuilder = new StringBuilder();
                                    for (Room r : rooms) {
                                        roomsBuilder.append("\n<").append(r).append(" - ").append(r.getAddress()).append(">");
                                    }
                                    out.writeUTF(roomsBuilder.toString());
                                }
                                break;
                            default:
                                out.writeUTF(data);
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
                    /* close failed */
                }
            }
        }
    }

