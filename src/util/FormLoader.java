package util;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class FormLoader {
    public void loadForm(String formName){
        try {
            Parent root = FXMLLoader.load(this.getClass().getResource("../view/forms/"+formName+".fxml"));
            Stage stage = new Stage();
            stage.setTitle(formName);
            stage.setScene(new Scene(root));
            stage.centerOnScreen();
            stage.show();
        }catch (Exception e){
            System.out.println(e);
        }
    }
}
