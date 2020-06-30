package com.example.demo.Controller;

import com.example.demo.entity.LoginFXML;
import com.example.demo.entity.PdfFXML;
import de.felixroske.jfxsupport.AbstractJavaFxApplicationSupport;
import de.felixroske.jfxsupport.FXMLController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

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
//        Image image = new Image("file:D:\\\\sprintboot-javafx-docxwatermark\\\\src\\\\main\\\\resources\\\\static\\\\logo\\\\solve.jpg");
//
//        logo.setImage(image);
    }

    @FXML
    public void gotoPDF(ActionEvent actionEvent) {
        AbstractJavaFxApplicationSupport.showView(PdfFXML.class);
    }

    @FXML
    public void gotoWord(ActionEvent actionEvent) {
        //launch(MainController.class, LoginFXML.class, null);
        AbstractJavaFxApplicationSupport.showView(LoginFXML.class);
    }

    @FXML
    public void showInfo(ActionEvent actionEvent) throws Exception{
        Parent target = FXMLLoader.load(getClass().getResource("/static/fxml/info.fxml"));//载入窗口B的定义文件；<span style="white-space:pre">	</span>
        Scene scene = new Scene(target); //创建场景；
        Stage stg = new Stage();//创建舞台；
        stg.setScene(scene); //将场景载入舞台；
        stg.show(); //显示窗口；
    }

}
