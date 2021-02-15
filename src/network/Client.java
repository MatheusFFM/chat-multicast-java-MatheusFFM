package network;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.*;
import java.util.Scanner;

public class Client {

    static MulticastSocket mSocket = null;
    static InetAddress groupIp;
    static int port = 6876;

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

        try {
            s = new Socket(host, serverPort);
            DataInputStream in = new DataInputStream(s.getInputStream());
            DataOutputStream out = new DataOutputStream(s.getOutputStream());
            System.out.println("Type your username: ");
            String username = input.nextLine();

            System.out.println("Welcome! \n" + username + "! " +
                    "type " + Commands.JOIN + " and the address to join a room \n" +
                    "type " + Commands.CREATE_ROOM + " and the room name to create a room \n" +
                    "type " + Commands.LIST_ROOMS + " to see all rooms\n" +
                    "type " + Commands.USERS + " and the room address to see all the members\n" +
                    "type " + Commands.EXIT + " to exit your room");

            System.out.println("Vou enviar");
            out.writeUTF("/hi young boy");
            System.out.println("Enviei");

            String data = in.readUTF();

            //createRoom(username, data);

        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
