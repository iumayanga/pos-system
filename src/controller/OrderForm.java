package controller;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
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
    public Button btnNew;
    public Button btnSave;
    public Button btnDelete;
    public TextField txtCustomerId;
    public TextField txtItemId;
    public TextField txtItemQuantity;
    public TableView<OrderTM> tblOrder;

    int successful = 0;

    GetData getData = new GetData();

    ObservableList<OrderTM> observableList;

    public void btnBack_OnAction() {
        Stage stage = (Stage) btnBack.getScene().getWindow();
        stage.close();
        FormLoader formLoader = new FormLoader();
        formLoader.loadForm("Dashboard");
    }

    public void btnNew_OnAction() {
        txtCustomerId.setDisable(false);
        txtItemId.setDisable(false);
        txtItemQuantity.setDisable(false);

        btnSave.setText("Save");

        txtCustomerId.clear();
        txtItemId.clear();
        txtItemQuantity.clear();
    }

    public void btnSave_OnAction() {
        OrderTM order = new OrderTM();
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
            btnNew_OnAction();
        }

        if (successful > 0){
            observableList.clear();
            observableList = getData.orders();
            tblOrder.setItems(observableList);

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
            successful = deleteEntry.orders(txtCustomerId.getText(), txtItemId.getText());

            ItemTM item = new ItemTM();
            item = getData.singleItem(txtItemId.getText());
            int currentQuantity = item.getQuantity();
            item.setQuantity(currentQuantity + Integer.parseInt(txtItemQuantity.getText()));

            UpdateEntry updateItemQuantity = new UpdateEntry();
            updateItemQuantity.item(item);
        }

        if (successful > 0){
            observableList.clear();
            observableList = getData.orders();
            tblOrder.setItems(observableList);

            txtCustomerId.clear();
            txtItemId.clear();
            txtItemQuantity.clear();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        tblOrder.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("customerId"));
        tblOrder.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("itemId"));
        tblOrder.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("itemQuantity"));
        tblOrder.getColumns().get(3).setCellValueFactory(new PropertyValueFactory<>("value"));

        observableList = getData.orders();
        tblOrder.setItems(observableList);

        disableTextFields();

        tblOrder.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<OrderTM>() {
            @Override
            public void changed(ObservableValue<? extends OrderTM> observable, OrderTM oldValue, OrderTM newValue) {
                OrderTM selectedItem = tblOrder.getSelectionModel().getSelectedItem();

                disableTextFields();
                btnSave.setDisable(true);

                txtCustomerId.setText(selectedItem.getCustomerId());
                txtItemId.setText(selectedItem.getItemId());
                txtItemQuantity.setText(Integer.toString(selectedItem.getItemQuantity()));
            }
        });
    }

    public void disableTextFields(){
        txtCustomerId.setDisable(true);
        txtItemId.setDisable(true);
        txtItemQuantity.setDisable(true);
    }
}
