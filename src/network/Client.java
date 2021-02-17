package network;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.*;
import java.util.Scanner;

public class Client {

    private static MulticastSocket mSocket = null;
    private static InetAddress groupIp;
    private static int port = 6876;

    private static void showOptions(){
        System.out.println("type " + Commands.JOIN + " and the address to join a room \n" +
                "type " + Commands.CREATE_ROOM + " and the room name to create a room \n" +
                "type " + Commands.LIST_ROOMS + " to see all rooms\n" +
                "type " + Commands.USERS + " and the room address to see all the members\n" +
                "type " + Commands.EXIT + " to exit your room or the program\n" +
                "type " + Commands.HELP + " for see this again\n");
    }

    private static void joinRoom(String username, String data) throws IOException {
        //data is address
        try {
            groupIp = InetAddress.getByName(data);
            mSocket = new MulticastSocket(port);
            mSocket.joinGroup(groupIp);
            String joinMessage = "< " + username + " JOINED THE ROOM>";
            byte[] message = joinMessage.getBytes();
            DatagramPacket messageOut = new DatagramPacket(message, message.length, groupIp, port);
            mSocket.send(messageOut);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Socket s;
        int serverPort = Server.PORT;
        String host = "localhost";
        Scanner input = new Scanner(System.in);
        boolean connected = false;

        try {
            s = new Socket(host, serverPort);
            DataInputStream in = new DataInputStream(s.getInputStream());
            DataOutputStream out = new DataOutputStream(s.getOutputStream());
            System.out.println("Type your username: ");
            String username = input.nextLine();

            System.out.println("\nWelcome! " + username + "!\n");

            while (true) {
                showOptions();

                while (!connected){
                String message = input.nextLine();

                if(message.startsWith(Commands.JOIN)){
                    message = message + Commands.USER + username;
                }

                out.writeUTF(message);
                String data = in.readUTF();

                switch (message.split(" ")[0]) {
                    case Commands.JOIN:
                        if(data.equals(Commands.ERROR)){
                            System.out.println("\nERROR! This room does not exist\n" +
                                    "Try other address or create your own room");
                        } else {
                            joinRoom(username, data);
                            connected = true;
                        }
                        break;
                    case Commands.USERS:
                        if(data.equals(Commands.ERROR)){
                            System.out.println("\nERROR! This room does not exist\n" +
                                    "Try other address");
                        } else {
                            System.out.println(data);
                        }
                        break;
                    case Commands.CREATE_ROOM:
                        System.out.println("\nSUCCESS! Your room address is: " + data);
                        break;
                    case Commands.LIST_ROOMS:
                        System.out.println("All rooms created: \n" + data);
                        break;
                    case Commands.HELP:
                        showOptions();
                        break;
                    case Commands.EXIT:
                        System.exit(200);
                        System.out.println("Exiting ");
                        break;
                    default:
                        System.out.println("\nPlease, type a valid command. " +
                                "\n Type /help if you need help");
                }}

                Thread thread = new Thread(() -> {
                    try {
                        while (true) {
                            byte[] buffer = new byte[1000];
                            DatagramPacket messageIn = new DatagramPacket(buffer, buffer.length, groupIp, port);
                            mSocket.receive(messageIn);
                            String msg = new String(messageIn.getData()).trim();
                            System.out.println(new String(messageIn.getData()).trim());
                        }
                    } catch (IOException e) {
                        System.out.println("IO: " + e.getMessage());
                    }
                });
                thread.start();

                String noAuthorMessage;
                String messageWithAuthor;

                while (connected) {
                    noAuthorMessage = input.nextLine();
                    if (noAuthorMessage.startsWith(Commands.EXIT)) {
                        mSocket.leaveGroup(groupIp);
                        out.writeUTF(Commands.EXIT);
                        connected = false;
                        System.out.println("YOU LEFT THE ROOM");
                    } else {
                        messageWithAuthor = "<" + username + "> :  " + noAuthorMessage;
                        byte[] msg = messageWithAuthor.getBytes();
                        DatagramPacket messageOut = new DatagramPacket(msg, msg.length, groupIp, port);
                        mSocket.send(messageOut);
                    }
                }

            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
