package GUI;


import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;

import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.util.*;


public class Controller {

    @FXML
    Button uniqueBook;

    @FXML
    GridPane itemSelectionGridPane;


    public void initialize() {
        // put any code to run before grid here
        // maybe about retrieving items from database or something idk
    }

    public void bookSelected(){
        System.out.println("book selected");
    }

}
