package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import util.CustomerTM;
import util.DatabaseConnection;
import util.ItemTM;
import util.OrderTM;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class GetData {
    DatabaseConnection databaseConnection = new DatabaseConnection();
    Connection connection = databaseConnection.dbConnection();

    public ObservableList<CustomerTM> customer(){
        ObservableList<CustomerTM> observableList = FXCollections.observableArrayList();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM customer");

            while (resultSet.next()){
                observableList.add(new CustomerTM(resultSet.getString("cId"), resultSet.getString("cName"), resultSet.getString("cAddress")));
            }
        }catch (Exception e){
            System.out.println(e);
        }

        return observableList;
    }

    public ObservableList<ItemTM> item(){
        ObservableList<ItemTM> observableList = FXCollections.observableArrayList();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM item");

            while (resultSet.next()){
                observableList.add(new ItemTM(resultSet.getString("iId"), resultSet.getString("iName"), Integer.parseInt(resultSet.getString("iQuantity")), Integer.parseInt(resultSet.getString("iUnitPrice"))));
            }
        }catch (Exception e){
            System.out.println(e);
        }

        return observableList;
    }

    public ObservableList<OrderTM> orders(){
        ObservableList<OrderTM> observableList = FXCollections.observableArrayList();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM orders");

            while (resultSet.next()){
                observableList.add(new OrderTM(resultSet.getString("cId"), resultSet.getString("iId"), Integer.parseInt(resultSet.getString("iQuantity")), Integer.parseInt(resultSet.getString("oValue"))));
            }
        }catch (Exception e){
            System.out.println(e);
        }

        return observableList;
    }

    public ItemTM singleItem(String id){
        ItemTM item = new ItemTM();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM item WHERE iId='"+id+"'");

            while (resultSet.next()){
                item.setId(resultSet.getString("iId"));
                item.setName(resultSet.getString("iName"));
                item.setQuantity(resultSet.getInt("iQuantity"));
                item.setUnitPrice(resultSet.getInt("iUnitPrice"));
            }
        }catch (Exception e){
            System.out.println(e);
        }

        return item;
    }
}
