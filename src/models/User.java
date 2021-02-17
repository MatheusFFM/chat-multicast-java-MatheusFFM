package models;

public class User {
    private String name;
    private IpAddress room;

    public User(String name){
        this.name = name;
        this.room = null;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public IpAddress getRoom() {
        return room;
    }

    public void setRoom(IpAddress room) {
        this.room = room;
    }

    @Override
    public String toString(){
        return this.name;
    }
}
