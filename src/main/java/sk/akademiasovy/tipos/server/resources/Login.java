package sk.akademiasovy.tipos.server.resources;



import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import sk.akademiasovy.tipos.server.Credentials;
import sk.akademiasovy.tipos.server.Registration;
import sk.akademiasovy.tipos.server.Users;
import sk.akademiasovy.tipos.server.db.MySQL;

@Path("/auth")
public class Login {

    @POST
    @Path("/login")
    @Produces(MediaType.APPLICATION_JSON)
    public Response checkCredentials(Credentials credential){
        System.out.println(credential.getUsername());
        MySQL mySQL = new MySQL();
        Users users = mySQL.getUser(credential.username, credential.password);
        if(users ==null){
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
        else{
            String result;
            result="{\"firstname\":\""+ users.getFirstname()+"\" , ";
            result+="\"lastname\":\""+ users.getLastname()+"\" , ";
            result+="\"email\":\""+ users.getEmail()+"\" , ";
            result+="\"login\":\""+ users.getLogin()+"\" , ";
            result+="\"token\":\""+ users.getToken()+"\"}";
            return Response.ok(result,MediaType.APPLICATION_JSON_TYPE).build();
        }

    }

    @GET
    @Path("/logout/{token}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response logout(@PathParam("token")  String token){
        MySQL mySQL = new MySQL();
        mySQL.logout( token);
        return Response.ok().build();
    }

    @POST
    @Path("/reg")
    @Produces(MediaType.APPLICATION_JSON)
    public Response createNewUser(Registration registration) {
         MySQL mySQL=new MySQL();
         boolean exist=mySQL.checkIfEmailOrLoginExist(registration.login.trim(), registration.email.trim());
        if(exist){
            return Response.status(406).build();

        }
        else{
            //  to do registration
            System.out.println("do registration");
            mySQL.insertNewUserIntoDb(registration);
        }
          return Response.status(201).build();
    }



}
