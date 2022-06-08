package controller;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import util.FormLoader;

public class LogIn {
    public TextField txtUsername;
    public TextField txtPassword;
    public Button btnLogIn;
    public Label lblLogInStatus;

    public void btnLogIn_OnAction() {
        FormLoader formLoader = new FormLoader();

        if (txtUsername.getText().equals("user") && txtPassword.getText().equals("123")){
            Stage stage = (Stage) btnLogIn.getScene().getWindow();
            stage.close();
            formLoader.loadForm("Dashboard");
        }else if (txtUsername.getText().isEmpty() || txtPassword.getText().isEmpty()){
            lblLogInStatus.setText("Please enter your data!");
        }else {
            lblLogInStatus.setText("Wrong username or password!");
        }
    }
}
