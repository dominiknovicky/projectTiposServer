package akademiasovy.tipos.server.resources;


import akademiasovy.tipos.server.Credentials;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;


@Path("/auth")
public class Login {

    @POST
    @Path("/login")
    @Produces(MediaType.APPLICATION_JSON)
    public String checkCredentials(Credentials credential){
        System.out.println(credential.getUsername());
        return "{\"token\" : \"abc\"}";
    }

}
