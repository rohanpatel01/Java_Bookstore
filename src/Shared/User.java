package Shared;

import java.io.Serializable;

public class User implements Serializable {

    public String username;
    public String password;
    public boolean isAdmin;

    public User() {}

    public User(String username, String password, boolean isAdmin) {
        this.username = username;
        this.password = password;
        this.isAdmin = isAdmin;
    }

    @Override
    public String toString() {
       return "{Username: " + username + " : " + "Password: " + password + "}";
    }

}
