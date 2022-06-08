package model;

import util.DatabaseConnection;

import java.sql.Connection;
import java.sql.Statement;

public class DeleteEntry {
    int successful = 0;
    DatabaseConnection databaseConnection = new DatabaseConnection();
    Connection connection = databaseConnection.dbConnection();

    public int customer(String id){
        try {
            Statement statement = connection.createStatement();
            successful = statement.executeUpdate("DELETE FROM customer WHERE cId='"+id+"'");
        }catch (Exception e){
            System.out.println(e);
        }

        return successful;
    }

    public int item(String id){
        try {
            Statement statement = connection.createStatement();
            successful = statement.executeUpdate("DELETE FROM item WHERE iId='"+id+"'");
        }catch (Exception e){
            System.out.println(e);
        }

        return successful;
    }

    public int orders(String customerId, String itemId){
        try {
            Statement statement = connection.createStatement();
            successful = statement.executeUpdate("DELETE FROM orders WHERE cId='"+customerId+"' AND iId='"+itemId+"'");
        }catch (Exception e){
            System.out.println(e);
        }

        return successful;
    }
}
