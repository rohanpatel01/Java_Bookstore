package Client;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.util.Duration;


public class ExitPageController {

    @FXML
    Label goodbyeLabel;

    public void initialize() {

        Duration delay = Duration.seconds(3);

        Timeline timeline = new Timeline(new KeyFrame(delay, event -> {
                javafx.application.Platform.exit();
        }));

        timeline.play();
    }
}
