package sk.akademiasovy.tipos.server.db;

import sk.akademiasovy.tipos.server.DrawNumber;
import sk.akademiasovy.tipos.server.Registration;
import sk.akademiasovy.tipos.server.Ticket;
import sk.akademiasovy.tipos.server.Users;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MySQL {
    private Connection conn;
    private String driver = "com.mysql.jdbc.Driver";
    private String url="jdbc:mysql://localhost:3306/tipos";
    private String username="root";
    private String password="";

    public Users getUser(String username, String password){
        try {
            Class.forName(driver).newInstance();
            conn = DriverManager.getConnection(url, this.username, this.password);

            String query = "select * from users where login like ? and password like ?";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1,username);
            ps.setString(2,password);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                 Users users =new Users(rs.getString("firstname"),rs.getString("lastname"),rs.getString("login"),rs.getString("email"));
                 query = "update tokens SET token=? WHERE idu=?";
                ps = conn.prepareStatement(query);
                ps.setString(1, users.getToken());
                ps.setInt(2,rs.getInt("id"));

                ps.executeUpdate();
                System.out.println(ps);
                return users;
            }
            return null;
        }catch(Exception e){
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
            System.out.println(ps);
            ps.executeUpdate();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public boolean checkIfEmailOrLoginExist(String login, String email) {
        try {
            Class.forName(driver).newInstance();
            conn = DriverManager.getConnection(url, this.username, this.password);

            String query = "select count(*) as num from users where login like ? OR email like ?";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1,login);
            ps.setString(2,email);
            ResultSet rs=ps.executeQuery();
            System.out.println(ps);

            rs.next();
            if(rs.getInt("num")==0)
                return false;
            else
                return true;

        }catch(Exception e){
            e.printStackTrace();
        }

        return true;
    }

    public void insertNewUserIntoDb(Registration registration) {
        try {
            Class.forName(driver).newInstance();
            conn = DriverManager.getConnection(url, this.username, this.password);
            String query = "insert into users(firstname, lastname, email, login, password) "+
                    " values (?,?,?,?,?)";

            PreparedStatement ps= conn.prepareStatement(query);
            ps.setString(1,registration.firstname);
            ps.setString(2,registration.lastname);
            ps.setString(3,registration.email);
            ps.setString(4,registration.login);
            ps.setString(5,registration.password);
            ps.executeUpdate();


        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public boolean checkLogin(String login) {
        try {
            Class.forName(driver).newInstance();
            conn = DriverManager.getConnection(url, this.username, this.password);

            String query = "select * from users where login like ?";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1,login);
            ResultSet rs=ps.executeQuery();

            if(rs.next())
                return true;
            else
                return false;

        }catch(Exception e){
            e.printStackTrace();
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
            ResultSet rs=ps.executeQuery();

            if(rs.next())
                return true;
            else
                return false;

        }catch(Exception e){
            e.printStackTrace();
        }
        return false;
    }

    public void insertBets(Ticket ticket) {
        try {
            Class.forName(driver).newInstance();
            conn = DriverManager.getConnection(url, this.username, this.password);

            String query = "insert into bets (idu) select id from users where login like ?";
            PreparedStatement ps = conn.prepareStatement(query);

            ps.setString(1,ticket.login);
            System.out.println(ps);
            ps.executeUpdate();
            query="select max(id) as max from bets where idu = (select id from users where login like ?)";
            ps = conn.prepareStatement(query);
            ps.setString(1,ticket .login);
            System.out.println(ps);
            ResultSet rs = ps.executeQuery();
            rs.next();
            int id_bet=rs.getInt("max");
            query = "insert into bet_details(idb,bet1, bet2, bet3, bet4,bet5) values (?,?,?,?,?,?)";
            ps= conn.prepareStatement(query);
            ps.setInt(1,id_bet);
            ps.setInt(2,ticket.bet1);
            ps.setInt(3,ticket.bet2);
            ps.setInt(4,ticket.bet3);
            ps.setInt(5,ticket.bet4);
            ps.setInt(6,ticket.bet5);
            ps.executeUpdate();


        }catch(Exception e){
            e.printStackTrace();
        }

    }

    public DrawNumber getDrawNumbers(int id) {
       try {
           Class.forName(driver).newInstance();
           conn = DriverManager.getConnection(url, this.username, this.password);

           String query = "select * from draw_history where id = ?";
           PreparedStatement ps = conn.prepareStatement(query);
           ps.setInt(1, id);
           ResultSet rs = ps.executeQuery();
           if(rs.next()){
              DrawNumber dn = new DrawNumber(rs.getInt("ball1"),rs.getInt("ball2"),rs.getInt("ball3"),rs.getInt("ball4"),
                      rs.getInt("ball5"), null);
               return dn;
           }
           else return null;
       }catch (Exception e){
           e.printStackTrace();
       }
        return null;
    }

    public List<Ticket> getTicketByDrawId(int id) {
        try {
            Class.forName(driver).newInstance();
            conn = DriverManager.getConnection(url, this.username, this.password);

            String query = "select * from bets inner join bet_details on bets.id=bet_details.idb where draw_id like ?";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            List<Ticket> list=new ArrayList<>();
            while(rs.next()){
                int bet1=rs.getInt("bet1");
                int bet2=rs.getInt("bet2");
                int bet3=rs.getInt("bet3");
                int bet4=rs.getInt("bet4");
                int bet5=rs.getInt("bet5");
                Date date = rs.getDate("date");
                int idb = rs.getInt("bets.id");
                Ticket ticket=new Ticket(bet1,bet2,bet3,bet4,bet5,date, idb);
                list.add(ticket);
            }
            return list;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public List getActualTickets(String username) {
        try {
            Class.forName(driver).newInstance();
            conn = DriverManager.getConnection(url, this.username, this.password);

            String query = "select * from bets "
                    +" inner join users on users.id=bets.idu "
                    +" inner join bet_details on bets.id=bet_details.idb "
                    +" where login like ? and draw_id is null";
            PreparedStatement ps = conn.prepareStatement(query);

            ps.setString(1,username);
            System.out.println(ps);
            ResultSet rs = ps.executeQuery();

            List<Ticket> list=new ArrayList<>();
            while(rs.next()){
                int bet1=rs.getInt("bet1");
                int bet2=rs.getInt("bet2");
                int bet3=rs.getInt("bet3");
                int bet4=rs.getInt("bet4");
                int bet5=rs.getInt("bet5");
                Date date = rs.getDate("date");
                int id = rs.getInt("bets.id");
                Ticket ticket=new Ticket(bet1, bet2,bet3,bet4,bet5,date, id);
                list.add(ticket);
            }
            return list;
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public List getAllTickets(String username) {
        try {
            Class.forName(driver).newInstance();
            conn = DriverManager.getConnection(url, this.username, this.password);

            String query = "select * from bets "
                    +" inner join users on users.id=bets.idu "
                    +" inner join bet_details on bets.id=bet_details.idb "
                    +" where login like ? and draw_id is not null";
            PreparedStatement ps = conn.prepareStatement(query);

            ps.setString(1,username);
            System.out.println(ps);
            ResultSet rs = ps.executeQuery();

            List<Ticket> list=new ArrayList<>();
            while(rs.next()){
                int bet1=rs.getInt("bet1");
                int bet2=rs.getInt("bet2");
                int bet3=rs.getInt("bet3");
                int bet4=rs.getInt("bet4");
                int bet5=rs.getInt("bet5");
                Date date = rs.getDate("date");
                int id = rs.getInt("bets.id");
                Ticket ticket=new Ticket(bet1, bet2,bet3,bet4,bet5,date, id);
                list.add(ticket);
            }
            return list;
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }


}
