package controller;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.CreateEntry;
import model.DeleteEntry;
import model.GetData;
import model.UpdateEntry;
import util.FormLoader;
import util.ItemTM;
import util.OrderTM;

import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

public class OrderForm implements Initializable {
    public Button btnBack;
    public Button btnSave;
    public Button btnDelete;
    public TextField txtCustomerId;
    public TextField txtItemId;
    public TextField txtItemQuantity;
    public TableView<OrderTM> tblOrder;
    public Button btnFinish;
    public TextField txtId;
    public TextArea txtAreaBill;
    public Button btnPrint;

    int successful = 0;

    int billAmount = 0;

    GetData getData = new GetData();

    ArrayList<OrderTM> arrayList = new ArrayList<>();

    public void btnBack_OnAction() {
        Stage stage = (Stage) btnBack.getScene().getWindow();
        stage.close();
        FormLoader formLoader = new FormLoader();
        formLoader.loadForm("Dashboard");
    }

    public void btnSave_OnAction() {
        OrderTM order = new OrderTM();
        order.setId(txtId.getText());
        order.setCustomerId(txtCustomerId.getText());
        order.setItemId(txtItemId.getText());
        order.setItemQuantity(Integer.parseInt(txtItemQuantity.getText()));

        ItemTM item = new ItemTM();
        item = getData.singleItem(txtItemId.getText());
        int unitPrice = item.getUnitPrice();
        int currentQuantity = item.getQuantity();

        order.setValue(Integer.parseInt(txtItemQuantity.getText())*unitPrice);

        if (currentQuantity > Integer.parseInt(txtItemQuantity.getText())){
            CreateEntry createEntry = new CreateEntry();
            successful = createEntry.orders(order);

            item.setQuantity(currentQuantity - Integer.parseInt(txtItemQuantity.getText()));
            UpdateEntry itemQuantityUpdate = new UpdateEntry();
            itemQuantityUpdate.item(item);
        }else {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Maximum amount of "+item.getName()+" is "+Integer.toString(currentQuantity), ButtonType.OK);
            alert.showAndWait();
            txtItemQuantity.clear();
        }

        if (successful > 0){
            arrayList.add(order);

            String itemId = order.getItemId();
            int itemQuantity = order.getItemQuantity();
            int value = order.getValue();

            ObservableList<OrderTM> orderTable = tblOrder.getItems();
            orderTable.add(new OrderTM(itemId, itemQuantity, value));

            txtId.setDisable(true);
            txtCustomerId.setDisable(true);
            txtItemId.clear();
            txtItemQuantity.clear();
        }
    }

    public void btnDelete_OnAction() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this order?", ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> buttonType = alert.showAndWait();

        if(buttonType.get() == ButtonType.YES){
            DeleteEntry deleteEntry = new DeleteEntry();
            successful = deleteEntry.orders(txtId.getText(), txtCustomerId.getText(), txtItemId.getText());

            ItemTM item = new ItemTM();
            item = getData.singleItem(txtItemId.getText());
            int currentQuantity = item.getQuantity();
            item.setQuantity(currentQuantity + Integer.parseInt(txtItemQuantity.getText()));

            UpdateEntry updateItemQuantity = new UpdateEntry();
            updateItemQuantity.item(item);
        }

        if (successful > 0){
            for (int i = 0; i < arrayList.size(); i++) {
                OrderTM orderTM =  arrayList.get(i);

                if (orderTM.getId()==txtId.getText() && orderTM.getCustomerId()==txtCustomerId.getText() && orderTM.getItemId()==txtItemId.getText()){
                    arrayList.remove(i);
                }
            }

            OrderTM selectedItem = tblOrder.getSelectionModel().getSelectedItem();
            tblOrder.getItems().remove(selectedItem);
            tblOrder.getSelectionModel().clearSelection();

            txtId.setDisable(true);
            txtCustomerId.setDisable(true);
            txtItemId.setDisable(false);
            txtItemQuantity.setDisable(false);
            btnSave.setDisable(false);
            txtItemId.clear();
            txtItemQuantity.clear();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        tblOrder.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("itemId"));
        tblOrder.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("itemQuantity"));
        tblOrder.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("value"));

        tblOrder.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<OrderTM>() {
            @Override
            public void changed(ObservableValue<? extends OrderTM> observable, OrderTM oldValue, OrderTM newValue) {
                OrderTM selectedItem = tblOrder.getSelectionModel().getSelectedItem();

                txtId.setDisable(true);
                txtCustomerId.setDisable(true);
                txtItemId.setDisable(true);
                txtItemQuantity.setDisable(true);
                btnSave.setDisable(true);

                txtId.setText(selectedItem.getId());
                txtCustomerId.setText(selectedItem.getCustomerId());
                txtItemId.setText(selectedItem.getItemId());
                txtItemQuantity.setText(Integer.toString(selectedItem.getItemQuantity()));
            }
        });
    }

    public void btnFinish_OnAction() {
        billHead();
        billBody();

        for ( int i = 0; i<tblOrder.getItems().size(); i++) {
            tblOrder.getItems().clear();
        }

        txtId.clear();
        txtCustomerId.clear();
        txtItemId.clear();
        txtItemQuantity.clear();
        txtId.setDisable(true);
        txtCustomerId.setDisable(true);
        txtItemId.setDisable(true);
        txtItemQuantity.setDisable(true);
    }

    public void btnPrint_OnAction() {
        System.out.println(txtAreaBill.getText());

        txtAreaBill.clear();

        txtId.setDisable(false);
        txtCustomerId.setDisable(false);
        txtItemId.setDisable(false);
        txtItemQuantity.setDisable(false);
    }

    public void billHead(){
        OrderTM orderTM = new OrderTM();
        orderTM = arrayList.get(0);
        txtAreaBill.appendText("FOOD CITY\n" +
                "===================================\n" +
                "Order id: '"+orderTM.getId()+"'\n" +
                "Customer id: '"+orderTM.getCustomerId()+"'\n" +
                "===================================\n\n\n");
    }

    public void billBody(){
        OrderTM orderTM = new OrderTM();
        ItemTM itemTM = new ItemTM();
        txtAreaBill.appendText("Item | Quantity | Unit Price | Value \n");

        for (int i = 0; i < arrayList.size(); i++) {
            orderTM =  arrayList.get(i);
            itemTM = getData.singleItem(orderTM.getItemId());

            txtAreaBill.appendText("'"+itemTM.getName()+"' | '"+orderTM.getItemQuantity()+"' | '"+itemTM.getUnitPrice()+"' | '"+orderTM.getValue()+"'\n");

            billAmount += orderTM.getValue();
        }

        txtAreaBill.appendText("Total amount: "+billAmount);
    }
}
