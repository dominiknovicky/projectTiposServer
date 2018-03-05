package akademiasovy.tipos.server.resources;

import akademiasovy.tipos.server.Ticket;
import akademiasovy.tipos.server.db.MySQL;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/bets")
public class Bets {

    @POST
    @Path("/new")
    @Produces(MediaType.APPLICATION_JSON)
    public Response newTicket(Ticket ticket){

        MySQL mySQL = new MySQL();
        boolean log  = mySQL.checkLogin(ticket.login);
        boolean tok  = mySQL.checkToken(ticket.token);


        if(log && tok)
            System.out.println("Incorrect");
        else {
            mySQL.insertBets(ticket);
        }
        return Response.status(200).build();

    }

}
