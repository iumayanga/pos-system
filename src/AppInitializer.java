import javafx.application.Application;
import javafx.stage.Stage;
import util.FormLoader;

public class AppInitializer extends Application {
    @Override
    public void start(Stage primaryStage){
        FormLoader formLoader = new FormLoader();
        formLoader.loadForm("LogIn");
    }
}
