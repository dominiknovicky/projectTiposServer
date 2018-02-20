package akademiasovy.tipos.server.db;

import akademiasovy.tipos.server.User;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class MySQL {

    private Connection conn;
    private String driver = "com.mysql.jdbc.Driver";
    private String url="jdbc:mysql://localhost:3307/tipos";
    private String username="root";
    private String password="";

    public User getUser(String username, String password){
        try {
            Class.forName(driver).newInstance();
            conn = DriverManager.getConnection(url,username,password);
            String query = "select * from users where username like ? and password like ?";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, username);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                User user = new User(rs.getString("firstname"),rs.getString("lastname"), rs.getString("login"), rs.getString("password"));
                query = "INSERT INTO tokens (idu, token) values(? ?)";
                ps = conn.prepareStatement(query);
                ps.setInt(1, rs.getInt("id"));
                ps.setString(2, user.getToken());
            }
            return null;

        }catch(Exception ex){
            System.out.println("Error: "+ ex.getMessage());
        }
        return null;
    }
}
