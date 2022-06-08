package model;

import util.CustomerTM;
import util.DatabaseConnection;
import util.ItemTM;
import util.OrderTM;

import java.sql.Connection;
import java.sql.Statement;

public class UpdateEntry {
    int successful = 0;
    DatabaseConnection databaseConnection = new DatabaseConnection();
    Connection connection = databaseConnection.dbConnection();

    public int customer(CustomerTM customerTM){
        try {
            Statement statement = connection.createStatement();
            successful = statement.executeUpdate("UPDATE customer SET cName='"+customerTM.getName()+"',cAddress='"+customerTM.getAddress()+"' WHERE cId='"+customerTM.getId()+"'");
        }catch (Exception e){
            System.out.println(e);
        }

        return successful;
    }

    public int item(ItemTM itemTM){
        try {
            Statement statement = connection.createStatement();
            successful = statement.executeUpdate("UPDATE item SET iName='"+itemTM.getName()+"',iQuantity='"+itemTM.getQuantity()+"',iUnitPrice='"+itemTM.getUnitPrice()+"' WHERE iId='"+itemTM.getId()+"'");
        }catch (Exception e){
            System.out.println(e);
        }

        return successful;
    }

    public int orders(OrderTM orderTM){
        try {
            Statement statement = connection.createStatement();
            successful = statement.executeUpdate("UPDATE orders SET iQuantity='"+orderTM.getItemQuantity()+"', oValue='"+orderTM.getValue()+"' WHERE cId='"+orderTM.getCustomerId()+"' AND iId='"+orderTM.getItemId()+"'");
        }catch (Exception e){
            System.out.println(e);
        }

        return successful;
    }
}
