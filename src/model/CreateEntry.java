package model;

import util.CustomerTM;
import util.DatabaseConnection;
import util.ItemTM;
import util.OrderTM;

import java.sql.Connection;
import java.sql.Statement;

public class CreateEntry {
    int successful = 0;
    DatabaseConnection databaseConnection = new DatabaseConnection();
    Connection connection = databaseConnection.dbConnection();

    public int customer(CustomerTM customerTM){
        try {
            Statement statement = connection.createStatement();
            successful = statement.executeUpdate("INSERT INTO customer VALUES('"+customerTM.getId()+"', '"+customerTM.getName()+"', '"+customerTM.getAddress()+"')");
        }catch (Exception e){
            System.out.println(e);
        }
        return successful;
    }

    public int item(ItemTM itemTM){
        try {
            Statement statement = connection.createStatement();
            successful = statement.executeUpdate("INSERT INTO item VALUES('"+itemTM.getId()+"', '"+itemTM.getName()+"', '"+itemTM.getQuantity()+"', '"+itemTM.getUnitPrice()+"')");
        }catch (Exception e){
            System.out.println(e);
        }
        return successful;
    }

    public int orders(OrderTM orderTM){
        try {
            Statement statement = connection.createStatement();
            successful = statement.executeUpdate("INSERT INTO orders VALUES('"+orderTM.getCustomerId()+"', '"+orderTM.getItemId()+"', '"+orderTM.getItemQuantity()+"', '"+orderTM.getValue()+"')");
        }catch (Exception e){
            System.out.println(e);
        }
        return successful;
    }
}
