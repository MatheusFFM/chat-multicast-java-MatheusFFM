package models;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Room {
    private String address;
    private String name;
    private List<User> users;

    public Room(String address, String name) {
        this.address = address;
        this.name = name;
        this.users = new ArrayList<User>();
    }

    public String showUsers(){
        return users.size() == 0 ?
                "No users" :
                users.stream().map(User::toString).collect(Collectors.joining(", "));
    }

    public boolean addUser(User u){
        u.setRoom(this.address);
        return this.users.add(u);
    }

    public boolean removeUser(User u){
        u.setRoom(null);
        return this.users.remove(u);
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString(){
        return "Room " + this.name;
    }
}
