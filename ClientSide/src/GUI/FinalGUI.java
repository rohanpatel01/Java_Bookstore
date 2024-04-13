package GUI;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class FinalGUI extends Application {

    @Override
    public void start(Stage applicationStage) throws IOException {
        Parent loader = FXMLLoader.load(getClass().getResource("GUI.fxml"));
        //Scene scene = new Scene(loader.load());
        applicationStage.setTitle("MyJavaFX");
        applicationStage.setScene(new Scene(loader));
        applicationStage.show();

    }
}
