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

    public static void createRoom(String username, String data) throws IOException {
        //data é o endereço Ip
        try {
            groupIp = InetAddress.getByName(data);
            mSocket = new MulticastSocket(port);
            mSocket.joinGroup(groupIp);
            String joinMessage = username + " JOINED THE ROOM";
            byte[] message = joinMessage.getBytes();
            DatagramPacket messageOut = new DatagramPacket(message, message.length, groupIp, port);
            mSocket.send(messageOut);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Socket s = null;
        int serverPort = Server.PORT;
        String host = "localhost";
        Scanner input = new Scanner(System.in);
        boolean connected = true;

        try {
            s = new Socket(host, serverPort);
            DataInputStream in = new DataInputStream(s.getInputStream());
            DataOutputStream out = new DataOutputStream(s.getOutputStream());
            System.out.println("Type your username: ");
            String username = input.nextLine();

            System.out.println("Welcome! " + username + "! " +
                    "type " + Commands.JOIN + " and the address to join a room \n" +
                    "type " + Commands.CREATE_ROOM + " and the room name to create a room \n" +
                    "type " + Commands.LIST_ROOMS + " to see all rooms\n" +
                    "type " + Commands.USERS + " and the room address to see all the members\n" +
                    "type " + Commands.EXIT + " to exit your room");

            //String message = input.nextLine();
            //out.writeUTF(message);
            System.out.println("Vou escrever");
            out.writeUTF("/join 228.5.6.7");
            System.out.println("Escrevi");
            String data = in.readUTF();
            System.out.println("DATA = " + data);

            createRoom(username, data);

            Thread thread = new Thread(() -> {
                try {
                    while (true) {
                        System.out.println("Thread");
                        byte[] buffer = new byte[1000];
                        DatagramPacket messageIn = new DatagramPacket(buffer, buffer.length, groupIp, port);
                        mSocket.receive(messageIn);
                        String msg = new String(messageIn.getData()).trim();
                        System.out.println( new String(messageIn.getData()).trim());
                    }
                } catch (IOException e) {
                    System.out.println("IO: " + e.getMessage());
                }
            });
            thread.start();

            String noAuthorMessage;
            String messageWithAuthor;

            while(connected) {
                noAuthorMessage = input.nextLine();
                if (noAuthorMessage.startsWith(Commands.EXIT)) {
                    mSocket.leaveGroup(groupIp);
                    out.writeUTF(Commands.EXIT);
                    connected = false;
                    System.out.println("## CHAT ENCERRADO COM SUCESSO ##");
                    System.exit(200);
                } else {
                    messageWithAuthor = "<" + username + "> :  " + noAuthorMessage;
                    byte[] message = messageWithAuthor.getBytes();
                    DatagramPacket messageOut = new DatagramPacket(message, message.length, groupIp, port);
                    mSocket.send(messageOut);
                }
            }
                //createRoom(username, data);

        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
