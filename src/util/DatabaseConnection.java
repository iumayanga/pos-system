package util;

import java.sql.Connection;
import java.sql.DriverManager;

public class DatabaseConnection {
    public Connection dbConnection(){
        Connection connection = null;

        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/foodCity", "root", "");
        }catch (Exception e){
            System.out.println(e);
        }

        return connection;
    }
}
