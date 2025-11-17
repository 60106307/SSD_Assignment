import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Label;

public class App extends Application {

    @Override
    public void start(Stage primaryStage) {
        //this.primaryStage = primaryStage;
        UserLogin login = new UserLogin(primaryStage);
        login.initializeComponents();
    }

    public static void main(String[] args) throws Exception{
        launch();
    }

    public static void launch(String[] args) {
    }
}
