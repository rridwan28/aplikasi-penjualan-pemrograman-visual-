package util;
 
import model.User;
 
public class Session {
    public static User currentUser = null;
 
    public static boolean isAdmin() {
        return currentUser != null && "admin".equals(currentUser.getRole());
    }
 
    public static boolean isLoggedIn() {
        return currentUser != null;
    }
 
    public static void logout() {
        currentUser = null;
    }
}
 
