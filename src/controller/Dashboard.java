package controller;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import util.FormLoader;


public class Dashboard {
    public Button btnCustomer;
    public Button btnItem;
    public Button btnOrder;
    public Button btnLogOut;

    FormLoader formLoader = new FormLoader();

    public void btnCustomer_OnAction() {
        Stage stage = (Stage) btnCustomer.getScene().getWindow();
        stage.close();
        formLoader.loadForm("CustomerForm");
    }

    public void btnItem_OnAction() {
        Stage stage = (Stage) btnItem.getScene().getWindow();
        stage.close();
        formLoader.loadForm("ItemForm");
    }

    public void btnOrder_OnAction() {
        Stage stage = (Stage) btnOrder.getScene().getWindow();
        stage.close();
        formLoader.loadForm("OrderForm");
    }

    public void btnLogOut_OnAction() {
        Stage stage = (Stage) btnLogOut.getScene().getWindow();
        stage.close();
        formLoader.loadForm("LogIn");
    }
}
