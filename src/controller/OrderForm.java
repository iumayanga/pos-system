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

    int successful = 0;

    GetData getData = new GetData();

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
            String id = order.getId();
            String customerId = order.getCustomerId();
            String itemId = order.getItemId();
            int itemQuantity = order.getItemQuantity();
            int value = order.getValue();

            ObservableList<OrderTM> orderTable = tblOrder.getItems();
            orderTable.add(new OrderTM(id, customerId, itemId, itemQuantity, value));

            txtId.setDisable(true);
            txtCustomerId.clear();
            txtItemId.clear();
            txtItemQuantity.clear();
        }
    }

    public void btnDelete_OnAction() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this order?", ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> buttonType = alert.showAndWait();

        if(buttonType.get() == ButtonType.YES){
            DeleteEntry deleteEntry = new DeleteEntry();
            successful = deleteEntry.orders(txtId.getText());

            ItemTM item = new ItemTM();
            item = getData.singleItem(txtItemId.getText());
            int currentQuantity = item.getQuantity();
            item.setQuantity(currentQuantity + Integer.parseInt(txtItemQuantity.getText()));

            UpdateEntry updateItemQuantity = new UpdateEntry();
            updateItemQuantity.item(item);
        }

        if (successful > 0){
            OrderTM selectedItem = tblOrder.getSelectionModel().getSelectedItem();
            tblOrder.getItems().remove(selectedItem);
            tblOrder.getSelectionModel().clearSelection();

            txtId.setDisable(true);
            txtCustomerId.setDisable(false);
            txtItemId.setDisable(false);
            txtItemQuantity.setDisable(false);
            btnSave.setDisable(false);
            txtCustomerId.clear();
            txtItemId.clear();
            txtItemQuantity.clear();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        tblOrder.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("id"));
        tblOrder.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("customerId"));
        tblOrder.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("itemId"));
        tblOrder.getColumns().get(3).setCellValueFactory(new PropertyValueFactory<>("itemQuantity"));
        tblOrder.getColumns().get(4).setCellValueFactory(new PropertyValueFactory<>("value"));

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

    }
}
