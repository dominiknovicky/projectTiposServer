package akademiasovy.tipos.server.resources;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import akademiasovy.tipos.server.Credentials;
import akademiasovy.tipos.server.User;
import akademiasovy.tipos.server.db.MySQL;

@Path("/auth")
public class Login {

    @POST
    @Path("/login")
    @Produces(MediaType.APPLICATION_JSON)
    public String checkCredentials(Credentials credential){
        System.out.println(credential.getUsername());
        System.out.println(credential.getPassword());
        MySQL mySQL = new MySQL();
        User user=mySQL.getUser(credential.username, credential.password);
        if(user==null){
            return "{}";
        }
        else {
            String result = null;
            result += "{\"firstname\":\""+user.getFirstname()+"\" , ";
            result += "\"lastname\":\""+user.getLastname()+"\" , ";
            result += "\"email\":\""+user.getEmail()+"\" , ";
            result += "\"login\":\""+user.getLogin()+"\" , ";
            result += "\"token\":\""+user.getToken()+"\"}";
            return result;
        }

    }

    @GET
    @Path("/logout/{token}")
    @Produces(MediaType.APPLICATION_JSON)
    public String logout(@PathParam("token") String token){
        MySQL mySQL = new MySQL();
        mySQL.logout(token);
        return "{}";
    }
}