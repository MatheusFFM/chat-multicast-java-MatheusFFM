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
    private static final int START_MULTICAST = 224;
    private static final String NO_ROOMS_MESSAGE = "No rooms. Type /room and the room name to create a room";
    private static final String GENERIC_ROOM_NAME = "Generic";

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

    private String joinRoom(String content) {
        String ipFromContent = content.split(Commands.USER)[0];
        String userFromContent = content.split(Commands.USER)[1];

        User user = new User(userFromContent);

        IpAddress ipToEnter = IpAddress.ipFromString(ipFromContent);
        Room roomToEnter = new Room(ipToEnter, GENERIC_ROOM_NAME);
        for (Room r : rooms) {
            if (r.compareTo(roomToEnter) == 0) {
                r.addUser(user);
                return ipToEnter.toString();
            }
        }
        return Commands.ERROR;
    }

    private String createRoom(String content) {
        IpAddress newIp = new IpAddress(START_MULTICAST, 0, 0, 1);
        if (ips.size() != 0) {
            List<IpAddress> auxIp = new ArrayList<IpAddress>(ips);
            newIp = auxIp.get(auxIp.size() - 1).getNext();
        }
        ips.add(newIp);
        rooms.add(new Room(newIp, content));
        return newIp.toString();
    }

    private void exit(String content) {
        String userToRemove = content.split(Commands.USER)[1];
        Room roomRef = null;
        User userRef = null;
        boolean find = false;

        for (Room r : rooms) {
            for (User u : r.getUsers()) {
                if (u.getName().equals(userToRemove)) {
                    roomRef = r;
                    userRef = u;
                    find = true;
                }
            }
            if (find) {
                break;
            }
        }
        assert roomRef != null;
        roomRef.removeUser(userRef);
    }

    private String list_rooms() {
        StringBuilder roomsBuilder = new StringBuilder();
        for (Room r : rooms) {
            roomsBuilder.append("\n<").append(r).append(" - ").append(r.getAddress()).append(">");
        }
        return roomsBuilder.toString();
    }


    private String user_list(String content) {
        IpAddress ipToShow = IpAddress.ipFromString(content);
        Room roomToShow = new Room(ipToShow, GENERIC_ROOM_NAME);
        for (Room r : rooms) {
            if (r.compareTo(roomToShow) == 0) {
                roomToShow = r;
                break;
            }
        }
        return roomToShow.showUsers();
    }

    public void run() {
        try {
            while (true) {
                String data = in.readUTF();
                String command, content;

                if (data.startsWith("/")) {
                    String[] splitedDate = data.split(" ");
                    command = splitedDate[0];
                    StringBuilder sb = new StringBuilder();
                    for (int i = 1; i < splitedDate.length; i++) {
                        sb.append(splitedDate[i]);
                    }
                    content = sb.toString();
                    switch (command) {
                        case Commands.JOIN:
                            out.writeUTF(joinRoom(content));
                            break;
                        case Commands.CREATE_ROOM:
                            out.writeUTF(createRoom(content));
                            break;
                        case Commands.EXIT:
                            exit(content);
                            out.writeUTF("");
                            break;
                        case Commands.LIST_ROOMS:
                            if (rooms.size() == 0) {
                                out.writeUTF(NO_ROOMS_MESSAGE);
                            } else {
                                out.writeUTF(list_rooms());
                            }
                            break;
                        case Commands.USERS_LIST:
                            out.writeUTF(user_list(content));
                            break;
                        default:
                            out.writeUTF(command);
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

