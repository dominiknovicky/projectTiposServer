package akademiasovy.tipos.server.db;

import akademiasovy.tipos.server.User;

import java.sql.*;

public class MySQL {
    private Connection conn;
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
                System.out.println(ps);
                return user;
            }
            return null;
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public void logout(String token) {

        try {
            Class.forName(driver).newInstance();
            conn = DriverManager.getConnection(url, this.username, this.password);

            String query = "update tokens set token = \"\" where token like ?";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1,token);
            ps.executeQuery();
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
}