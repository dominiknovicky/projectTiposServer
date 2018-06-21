package sk.akademiasovy.tipos.server;

import io.dropwizard.Configuration;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.*;
import javax.validation.constraints.*;

public class TiposConfiguration extends Configuration {
    // TODO: implement service configuration

      private String user;

      private String password;

    public String getUser() {
        return user;
    }

    public String getPassword() {
        return password;
    }
}
