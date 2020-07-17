package com.example.demo.Controller;

import com.example.demo.entity.AppModel;
import de.felixroske.jfxsupport.AbstractJavaFxApplicationSupport;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.util.ResourceBundle;

public class ErrController extends AbstractJavaFxApplicationSupport implements Initializable {

    // 必须static 类型
    public  static AppModel model = new AppModel();

    @FXML
    private Label msg;
    @FXML
    private ImageView logo;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        model.textProperty().addListener((obs, oldText, newText) -> msg.setText(newText));
        if(logo!=null){
            Image logoimage = new Image(getClass().getResource("/static/logo/solve.jpg").toExternalForm());
            logo.setImage(logoimage);
        }
    }


}
