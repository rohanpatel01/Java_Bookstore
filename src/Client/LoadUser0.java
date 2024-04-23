package Client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class LoadUser0 extends Application {

    @Override
    public void start(Stage applicationStage) throws IOException {
        Parent loader = FXMLLoader.load(getClass().getResource("LoginPage.fxml"));
//        Parent loader = FXMLLoader.load(getClass().getResource("MemberLibrary.fxml"));
//        Parent loader = FXMLLoader.load(getClass().getResource("AdminLibrary.fxml"));

        //Scene scene = new Scene(loader.load());
        applicationStage.setTitle("MyJavaFX");
        applicationStage.setScene(new Scene(loader));
        applicationStage.show();

    }
}
