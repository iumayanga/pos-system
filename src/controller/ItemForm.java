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

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class ItemForm implements Initializable {
    public Button btnNew;
    public TextField txtId;
    public TextField txtName;
    public TextField txtQuantity;
    public TextField txtUnitPrice;
    public Button btnSave;
    public Button btnDelete;
    public TableView<ItemTM> tblItem;
    public Button btnBack;

    int successful = 0;

    GetData getData = new GetData();

    ObservableList<ItemTM> observableList;

    public void btnNew_OnAction() {
        txtId.setDisable(false);
        txtName.setDisable(false);
        txtQuantity.setDisable(false);
        txtUnitPrice.setDisable(false);

        btnSave.setText("Save");

        txtId.clear();
        txtName.clear();
        txtQuantity.clear();
        txtUnitPrice.clear();
    }

    public void btnSave_OnAction() {
        try {
            ItemTM item = new ItemTM();
            item.setId(txtId.getText());
            item.setName(txtName.getText());
            item.setQuantity(Integer.parseInt(txtQuantity.getText()));
            item.setUnitPrice(Integer.parseInt(txtUnitPrice.getText()));

            if(btnSave.getText().equals("Save")){
                CreateEntry createEntry = new CreateEntry();
                successful = createEntry.item(item);
            }else {
                UpdateEntry updateEntry = new UpdateEntry();
                successful = updateEntry.item(item);
            }

            if (successful > 0){
                observableList.clear();
                observableList = getData.item();
                tblItem.setItems(observableList);

                txtId.clear();
                txtName.clear();
                txtQuantity.clear();
                txtUnitPrice.clear();
                txtId.setDisable(true);
                txtName.setDisable(true);
                txtQuantity.setDisable(true);
                txtUnitPrice.setDisable(true);
            }
        }catch (Exception e){
            Alert alert = new Alert(Alert.AlertType.WARNING, "The data supplied is of the wrong type!", ButtonType.CLOSE);
            alert.showAndWait();
        }
    }

    public void btnDelete_OnAction() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this item?", ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> buttonType = alert.showAndWait();

        if (buttonType.get() == ButtonType.YES){
            DeleteEntry deleteEntry = new DeleteEntry();
            successful = deleteEntry.item(txtId.getText());
        }

        if (successful > 0){
            observableList.clear();
            observableList = getData.item();
            tblItem.setItems(observableList);

            txtId.clear();
            txtName.clear();
            txtQuantity.clear();
            txtUnitPrice.clear();
            txtId.setDisable(true);
            txtName.setDisable(true);
            txtQuantity.setDisable(true);
            txtUnitPrice.setDisable(true);
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        tblItem.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("id"));
        tblItem.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("name"));
        tblItem.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("quantity"));
        tblItem.getColumns().get(3).setCellValueFactory(new PropertyValueFactory<>("unitPrice"));

        observableList = getData.item();
        tblItem.setItems(observableList);

        txtId.setDisable(true);
        txtName.setDisable(true);
        txtQuantity.setDisable(true);
        txtUnitPrice.setDisable(true);

        tblItem.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<ItemTM>() {
            @Override
            public void changed(ObservableValue<? extends ItemTM> observable, ItemTM oldValue, ItemTM newValue) {
                ItemTM selectedItem = tblItem.getSelectionModel().getSelectedItem();

                if (selectedItem == null){
                    btnSave.setText("Save");
                    return;
                }

                btnSave.setText("Update");

                txtId.setDisable(true);
                txtName.setDisable(false);
                txtQuantity.setDisable(false);
                txtUnitPrice.setDisable(false);

                txtId.setText(selectedItem.getId());
                txtName.setText(selectedItem.getName());
                txtQuantity.setText(Integer.toString(selectedItem.getQuantity()));
                txtUnitPrice.setText(Integer.toString(selectedItem.getUnitPrice()));
            }
        });
    }

    public void btnBack_OnAction() {
        Stage stage = (Stage) btnBack.getScene().getWindow();
        stage.close();
        FormLoader formLoader = new FormLoader();
        formLoader.loadForm("Dashboard");
    }
}
