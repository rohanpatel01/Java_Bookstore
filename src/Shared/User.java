package Shared;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class User implements Serializable {

    public String username;
    public String password;
    public boolean isAdmin;
    public Map<String, LibraryItem> cartItems;
    public boolean isSignup;

    public User() {}

    public User(String username, String password, boolean isAdmin) {
        this.username = username;
        this.password = password;
        this.isAdmin = isAdmin;
        cartItems = new HashMap<>();
        isSignup= false;

    }


    public void add(LibraryItem libraryItem) {
        cartItems.put(libraryItem.title, libraryItem);
    }

    public void printCart(){
        System.out.println("======");
        System.out.println("Cart Items: ");
        for (String s : cartItems.keySet()) {
            System.out.println(cartItems.get(s));
        }
        System.out.println("======");
    }

    @Override
    public String toString() {
       return "{Username: " + username + " : " + "Password: " + password + "}";
    }

}
