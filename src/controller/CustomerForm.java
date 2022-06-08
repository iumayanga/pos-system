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
import model.UpdateEntry;
import util.CustomerTM;
import util.FormLoader;
import model.GetData;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class CustomerForm implements Initializable {
    public Button btnNew;
    public TextField txtId;
    public TextField txtName;
    public TextField txtAddress;
    public Button btnSave;
    public Button btnDelete;
    public TableView<CustomerTM> tblCustomer;
    public Button btnBack;

    int successful = 0;

    GetData getData = new GetData();
    ObservableList<CustomerTM> observableList;

    public void btnNew_OnAction() {
        txtId.setDisable(false);
        txtName.setDisable(false);
        txtAddress.setDisable(false);

        btnSave.setText("Save");

        txtId.clear();
        txtName.clear();
        txtAddress.clear();
    }

    public void btnSave_OnAction() {
        CustomerTM customer = new CustomerTM();
        customer.setId(txtId.getText());
        customer.setName(txtName.getText());
        customer.setAddress(txtAddress.getText());

        if (btnSave.getText().equals("Save")){
            CreateEntry createEntry = new CreateEntry();
            successful = createEntry.customer(customer);
        }else {
            UpdateEntry updateEntry = new UpdateEntry();
            successful = updateEntry.customer(customer);
        }

        if (successful > 0){
            observableList.clear();
            observableList = getData.customer();
            tblCustomer.setItems(observableList);

            txtId.clear();
            txtName.clear();
            txtAddress.clear();
        }
    }

    public void btnDelete_OnAction() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this customer?", ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> buttonType = alert.showAndWait();

        if(buttonType.get() == ButtonType.YES){
            DeleteEntry deleteEntry = new DeleteEntry();
            successful = deleteEntry.customer(txtId.getText());
        }

        if (successful > 0){
            observableList.clear();
            observableList = getData.customer();
            tblCustomer.setItems(observableList);

            txtId.clear();
            txtName.clear();
            txtAddress.clear();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        tblCustomer.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("id"));
        tblCustomer.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("name"));
        tblCustomer.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("address"));

        observableList = getData.customer();
        tblCustomer.setItems(observableList);

        txtId.setDisable(true);
        txtName.setDisable(true);
        txtAddress.setDisable(true);

        tblCustomer.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<CustomerTM>() {
            @Override
            public void changed(ObservableValue<? extends CustomerTM> observable, CustomerTM oldValue, CustomerTM newValue) {
                CustomerTM selectedItem = tblCustomer.getSelectionModel().getSelectedItem();

                if(selectedItem == null){
                    btnSave.setText("Save");
                    return;
                }

                btnSave.setText("Update");

                txtId.setDisable(false);
                txtName.setDisable(false);
                txtAddress.setDisable(false);

                txtId.setText(selectedItem.getId());
                txtName.setText(selectedItem.getName());
                txtAddress.setText(selectedItem.getAddress());
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
