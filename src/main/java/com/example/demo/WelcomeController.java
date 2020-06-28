package com.example.demo;

import de.felixroske.jfxsupport.AbstractJavaFxApplicationSupport;
import de.felixroske.jfxsupport.FXMLController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.util.ResourceBundle;

@FXMLController
public class WelcomeController extends AbstractJavaFxApplicationSupport implements Initializable {

    @FXML
    private Button toPDF;
    @FXML
    private Button toWord;
    @FXML
    private ImageView logo;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Image image = new Image("file:D:\\\\sprintboot-javafx-docxwatermark\\\\src\\\\main\\\\resources\\\\static\\\\logo\\\\solve.jpg");

        logo.setImage(image);
    }

    @FXML
    public void gotoPDF(ActionEvent actionEvent) {

    }

    @FXML
    public void gotoWord(ActionEvent actionEvent) {
        //launch(MainController.class, LoginFXML.class, null);
        AbstractJavaFxApplicationSupport.showView(LoginFXML.class);
    }

}
