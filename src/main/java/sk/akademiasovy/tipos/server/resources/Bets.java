package sk.akademiasovy.tipos.server.resources;


import com.fasterxml.jackson.annotation.JsonIgnore;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import sk.akademiasovy.tipos.server.Credentials;
import sk.akademiasovy.tipos.server.DrawNumber;
import sk.akademiasovy.tipos.server.Ticket;
import sk.akademiasovy.tipos.server.db.MySQL;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/bets")
public class Bets {

    @POST
    @Path("/new")
    @Produces(MediaType.APPLICATION_JSON)
    public Response newTicket(Ticket ticket){
      MySQL mySQL=new MySQL();
        boolean ret1 = mySQL.checkLogin(ticket.login);
        boolean ret2 = mySQL.checkToken(ticket.token);
        if(ret1 && ret2) {
            System.out.println("Token and login are correct!");
            mySQL.insertBets(ticket);
            return Response.status(201).build();
        }
        else
        {
            System.out.println("Invalid username or token");
            return Response.status(401).build();
        }
     // return Response.status(201).build();
    }

    @POST
    @Path("/actual")
    @Produces(MediaType.APPLICATION_JSON)
    public Response actualTickets(Credentials credentials) {
        MySQL mySQL = new MySQL();
        boolean ret1 = mySQL.checkLogin(credentials.username);
        boolean ret2 = mySQL.checkToken(credentials.token);
        if(ret1 && ret2) {
            List<Ticket> tickets;
            tickets= mySQL.getActualTickets(credentials.username);

            if (tickets == null){
                return Response.noContent().build();
            }
            else {

            JSONArray obj=new JSONArray();
            for(Ticket ticket:tickets) {

                JSONObject tic=new JSONObject();
                tic.put("bet1", ticket.bet1);
                tic.put("bet2", ticket.bet2);
                tic.put("bet3", ticket.bet3);
                tic.put("bet4", ticket.bet4);
                tic.put("bet5", ticket.bet5);
                tic.put("username", credentials.username);
                tic.put("id",ticket.getId());

                obj.add(tic);
            }
            JSONObject result = new JSONObject();
            result.put("tickets",obj);
            System.out.println(result.toJSONString());
            return Response.ok(result.toString()).build();
            }
        }
        return Response.status(401).build();

    }


    @POST
    @Path("/all")
    @Produces(MediaType.APPLICATION_JSON)
    public Response allTickets(Credentials credentials) {
        MySQL mySQL = new MySQL();
        boolean ret1 = mySQL.checkLogin(credentials.username);
        boolean ret2 = mySQL.checkToken(credentials.token);
        if(ret1 && ret2) {
            List<Ticket> tickets;
            tickets= mySQL.getAllTickets(credentials.username);

            if (tickets == null){
                return Response.noContent().build();
            }
            else {

                JSONArray obj=new JSONArray();
                for(Ticket ticket:tickets) {

                    JSONObject tic=new JSONObject();
                    tic.put("bet1", ticket.bet1);
                    tic.put("bet2", ticket.bet2);
                    tic.put("bet3", ticket.bet3);
                    tic.put("bet4", ticket.bet4);
                    tic.put("bet5", ticket.bet5);
                    tic.put("username", credentials.username);
                    tic.put("id",ticket.getId());

                    obj.add(tic);
                }
                JSONObject result = new JSONObject();
                result.put("tickets",obj);
                System.out.println(result.toJSONString());
                return Response.ok(result.toString()).build();
            }
        }
        return Response.status(401).build();

    }



    @Path("/tickets/draw/{id}")

        @GET
        @Produces(MediaType.APPLICATION_JSON)
        public Response getTickets(@PathParam("id") int id){
            MySQL mySQL = new MySQL();
            List<Ticket> tickets;
            tickets = mySQL.getTicketByDrawId(id);
                System.out.println(tickets);
        if (tickets == null){
            return Response.noContent().build();
        }
        else {

            JSONArray obj=new JSONArray();
            for(Ticket ticket:tickets) {

                JSONObject tic=new JSONObject();
                tic.put("bet1", ticket.bet1);
                tic.put("bet2", ticket.bet2);
                tic.put("bet3", ticket.bet3);
                tic.put("bet4", ticket.bet4);
                tic.put("bet5", ticket.bet5);
                obj.add(tic);
            }
            JSONObject result = new JSONObject();
            result.put("tickets",obj);
            System.out.println(result.toJSONString());
            return Response.ok(result.toString()).build();
        }
    }
}
