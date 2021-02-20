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
        System.out.println("\n========= MENU =========\n" + "type " + Commands.JOIN + " and the address to join a room \n" +
                "type " + Commands.CREATE_ROOM + " and the room name to create a room \n" +
                "type " + Commands.LIST_ROOMS + " to see all rooms\n" +
                "type " + Commands.USERS_LIST + " and the room address to see all the members\n" +
                "type " + Commands.EXIT + " to exit your room or the program\n" +
                "type " + Commands.HELP + " for see this again\n");
        System.out.print(">  ");

    }

    private static void joinRoom(String username, String data) throws IOException {
        //data is address
        try {
            groupIp = InetAddress.getByName(data);
            mSocket = new MulticastSocket(port);
            mSocket.joinGroup(groupIp);
            String joinMessage = "<| " + username + " JOINED THE ROOM |>";
            byte[] message = joinMessage.getBytes();
            DatagramPacket messageOut = new DatagramPacket(message, message.length, groupIp, port);
            mSocket.send(messageOut);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        int serverPort = Server.PORT;
        String host = "localhost";
        Scanner input = new Scanner(System.in);
        boolean socketOnServer = false;
        String data = null;

        boolean connected = false;

            System.out.print("> Type your username: ");
            String username = input.nextLine();
            System.out.println("\n<| Welcome, " + username + "! |>\n");

        try {
            Socket s = new Socket(host, serverPort);;
            DataInputStream in = new DataInputStream(s.getInputStream());
            DataOutputStream out = new DataOutputStream(s.getOutputStream());

            while (true) {

                showOptions();
                if(data != null){
                    data = in.readUTF();
                }

                while (!connected){

                String message = input.nextLine();

                if(message.startsWith(Commands.JOIN)){
                    message = message + Commands.USER + username;
                }

                out.writeUTF(message);
                data = in.readUTF();

                switch (message.split(" ")[0]) {
                    case Commands.JOIN:
                        if(data.equals(Commands.ERROR)){
                            System.out.println("\nERROR! This room does not exist\n" +
                                    "Try other address or create your own room\n");
                        } else {
                            joinRoom(username, data);
                            connected = true;
                        }
                        break;
                    case Commands.USERS_LIST:
                        if(data.equals(Commands.ERROR)){
                            System.out.println("\n## ERROR! This room does not exist\n" +
                                    "Try other address\n");
                        } else {
                            System.out.println("\n## Members: \n" + data + "\n###########\n");
                        }
                        break;
                    case Commands.CREATE_ROOM:
                        System.out.println("\n## SUCCESS! Your room address is: " + data + "\n");
                        break;
                    case Commands.LIST_ROOMS:
                        System.out.println("## All rooms created: \n" + data + "\n");
                        break;
                    case Commands.HELP:
                        showOptions();
                        break;
                    case Commands.EXIT:
                        System.exit(200);
                        break;
                    default:
                        System.out.println("\n## Please, type a valid command. " +
                                "\n Type /help if you need help\n");
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
                        String leftMessage = "<| " + username + " LEFT THE ROOM |>";
                        byte[] message = leftMessage.getBytes();
                        DatagramPacket messageOut = new DatagramPacket(message, message.length, groupIp, port);
                        mSocket.send(messageOut);
                        mSocket.leaveGroup(groupIp);
                        out.writeUTF(Commands.EXIT + " " + Commands.USER + username);
                        in.readUTF();
                        connected = false;
                    } else {
                        messageWithAuthor = "<" + username + ">:  " + noAuthorMessage;
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
