package akademiasovy.tipos.server.db;

import akademiasovy.tipos.server.Registration;
import akademiasovy.tipos.server.Ticket;
import akademiasovy.tipos.server.User;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class MySQL {
    private Connection conn=null;
    private String driver = "com.mysql.jdbc.Driver";
    private String url="jdbc:mysql://localhost:3307/tipos";
    private String username="root";
    private String password="";

    public User getUser(String username, String password){
        try {
            Class.forName(driver).newInstance();
            conn = DriverManager.getConnection(url, this.username, this.password);

            String query = "select * from users where login like ? and password like ?";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1,username);
            ps.setString(2,password);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                User user=new User(rs.getString("firstname"),rs.getString("lastname"),rs.getString("login"),rs.getString("email"));
                query = "update tokens set token=? where idu=?";
                ps = conn.prepareStatement(query);
                ps.setString(1, user.getToken());
                ps.setInt(2,rs.getInt("id"));
                ps.executeUpdate();

                return user;
            }
            return null;
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public void logout( String token) {
        try {
            Class.forName(driver).newInstance();
            conn = DriverManager.getConnection(url, this.username, this.password);

            String query = "update tokens set token=\"\" where token like ?";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1,token);
            ps.executeUpdate();
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    public boolean checkIfEmailOrLoginExist(String login, String email) {
        try {
            Class.forName(driver).newInstance();

            conn = DriverManager.getConnection(url, "root", "");
            String query = "select count(*) as num from users where login like ? or email like ?";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1,login);
            ps.setString(2,email);
            ResultSet rs = ps.executeQuery();

            rs.next();
            if(rs.getInt("num") == 0)
                return false;
            else
                return true;

        }
        catch(Exception e){
            e.getMessage();
        }
        return true;
    }

    public void insertNewUser(Registration registration){
        try {
            Class.forName(driver).newInstance();
            conn = DriverManager.getConnection(url, this.username, this.password);

            String query = "insert into users(firstname, lastname, email, login, password) values(?,?,?,?,?)";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1,registration.firstname);
            ps.setString(2,registration.lastname);
            ps.setString(3,registration.email);
            ps.setString(4,registration.login);
            ps.setString(5,registration.password);
            ps.executeUpdate();
        }
        catch(Exception e){
            e.getMessage();
        }
    }

    public boolean checkLogin(String login) {
        try {
            Class.forName(driver).newInstance();
            conn = DriverManager.getConnection(url, this.username, this.password);
            String query = "select * from users where login like ?";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1,login);
            ResultSet rs = ps.executeQuery();
            rs.next();
            if(rs.next())
                return true;
            else
                return false;

        }
        catch(Exception e){
            e.getMessage();
        }
        return false;
    }

    public boolean checkToken(String token) {
        try {
            Class.forName(driver).newInstance();
            conn = DriverManager.getConnection(url, this.username, this.password);
            String query = "select * from tokens where token like ?";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1,token);
            ResultSet rs = ps.executeQuery();
            rs.next();
            if(rs.next())
                return true;
            else
                return false;

        }
        catch(Exception e){
            e.getMessage();
        }
        return false;
    }


    public void insertBets(Ticket ticket) {
        try {
            Class.forName(driver).newInstance();
            conn = DriverManager.getConnection(url, this.username, this.password);

            String query = "insert into bets (idu) select id from users where login like ?";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, ticket.login);
            ps.executeUpdate();

            query = "select max(id) as max from bets where idu = (select id from users where login like ?)";
            ps = conn.prepareStatement(query);
            ps.setString(1, ticket.login);
            ResultSet rs =  ps.executeQuery();
            rs.next();
            int id_bet = rs.getInt("max");
            query = "insert into bet_details(idb, bet1, bet2, bet3, bet4, bet5) values (?,?,?,?,?,?)";
            ps= conn.prepareStatement(query);
            ps.setInt(1, id_bet);
            ps.setInt(2, ticket.bet1);
            ps.setInt(3, ticket.bet2);
            ps.setInt(4, ticket.bet3);
            ps.setInt(5, ticket.bet4);
            ps.setInt(6, ticket.bet5);
            ps.executeUpdate();

        }
        catch(Exception e){
            e.getMessage();
        }
    }
}