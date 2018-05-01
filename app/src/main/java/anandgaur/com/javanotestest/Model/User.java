package anandgaur.com.javanotestest.Model;

/**
 * Created by Anand on 12-01-2018.
 */

public class User {

    // Todo 1 buat field String userName, password, email
    private String userName, password, email;

    // todo 2 null constructor
    public User() {
    }

    // todo 3 all constructor
    public User(String userName, String password, String email) {
        this.userName = userName;
        this.password = password;
        this.email = email;
    }

    // todo 4 create getter and setter
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
