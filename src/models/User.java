package models;

public class User {
    private String name;
    private String room;

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

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    @Override
    public String toString(){
        return this.name;
    }
}
