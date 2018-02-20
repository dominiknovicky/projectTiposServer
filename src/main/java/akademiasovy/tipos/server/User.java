package akademiasovy.tipos.server;

import java.util.Random;

public class User {
    private String firstname;
    private String lastname;
    private String username;
    private String email;
    private String token;

    public User(String firstname, String lastname, String username, String email) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.username = username;
        this.email = email;
    }

    private void generateToken(){
        char[] text = new char[50];
        Random random = new Random();

        for (int i = 0; i < 40; i++) {
            text[i] = (char)(random.nextInt(82)+33);
        }
        token = text.toString();
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getToken() {
        return token;
    }
}
