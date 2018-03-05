package akademiasovy.tipos.server;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Credentials {
    @JsonProperty("login")
    public String username;

    @JsonProperty("password")
    public String password;


    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}