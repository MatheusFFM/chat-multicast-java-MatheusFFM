package models;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Room implements Comparable<Room> {
    private IpAddress address;
    private String name;
    private List<User> users;

    public Room(IpAddress address, String name) {
        this.address = address;
        this.name = name;
        this.users = new ArrayList<User>();
    }

    public String showUsers() {
        return users.size() == 0 ?
                "No users" :
                users.stream().map(User::toString).collect(Collectors.joining(", "));
    }

    public void addUser(User u) {
        this.users.add(u);
    }

    public boolean removeUser(User u) {
        if (this.users.remove(u)) {
            u.setRoom(null);
            return true;
        }
        return false;
    }

    public IpAddress getAddress() {
        return address;
    }

    public void setAddress(IpAddress address) {
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<User> getUsers() {
        return users;
    }

    @Override
    public String toString() {
        return "Room " + this.name;
    }

    @Override
    public int compareTo(Room o) {
        return this.address.compareTo(o.address);
    }
}
