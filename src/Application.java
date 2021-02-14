import models.Room;
import models.User;

public class Application {
    public static void main(String[] args) {
        User u = new User("Matheus");
        User u2 = new User("João");
        Room r = new Room("228.5.6.7","Games");
        r.addUser(u);
        r.addUser(u2);
        System.out.println("Users on room: " + r.showUsers());
        r.removeUser(u2);
        System.out.println("Removing João: " + r.showUsers());
        System.out.println("Matheus: " + u + " - Room: " + u.getRoom());
        System.out.println("João: " + u2 + " - Room: " + u2.getRoom());
        r.removeUser(u);
        System.out.println("Removing Matheus: " + r.showUsers());
    }
}
