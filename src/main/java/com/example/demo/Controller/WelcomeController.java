package com.example.demo.Controller;

import com.example.demo.entity.LoginFXML;
import com.example.demo.entity.PdfFXML;
import com.example.demo.test.DialogBuilder;
import com.jfoenix.controls.JFXButton;
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
    private JFXButton toPDF;
    @FXML
    private JFXButton toWord;
    @FXML
    private Button login;
    @FXML
    private ImageView logo;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
//        Image image = new Image("file:D:\\\\sprintboot-javafx-docxwatermark\\\\src\\\\main\\\\resources\\\\static\\\\logo\\\\solve.jpg");
//        logo.setImage(image);

        System.out.println("- WelcomeController initialized -");
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
    public void usingjfoneix(ActionEvent actionEvent) {
        //launch(MainController.class, LoginFXML.class, null);
        //AbstractJavaFxApplicationSupport.showView(usingjfoneixFXML.class);
        new DialogBuilder(login).setNegativeBtn("取消", new DialogBuilder.OnClickListener() {
            @Override
            public void onClick() {
                //点击取消按钮之后执行的动作
                System.out.println("用户点击了取消");
            }
        }).setPositiveBtn("确定", new DialogBuilder.OnClickListener() {
            @Override
            public void onClick() {
                //点击确定按钮之后执行的动作
                System.out.println("用户点击了确定");
            }
        }).setTitle("提示").setMessage("hello world").create();
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
