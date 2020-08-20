package com.example.demo.Controller;

import com.example.demo.Globe;
import com.example.demo.entity.LoginFXML;
import com.example.demo.entity.PdfFXML;
//import com.example.demo.test.DialogBuilder;
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

//    private boolean initialized = false;

    @FXML
    private ImageView logo;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

            Image image = new Image(getClass().getResource("/static/logo/logo.jpg").toExternalForm());
            logo.setImage(image);

            System.out.println("- WelcomeController initialized -");
//            try {
//                //Thread.sleep(2000);
//                Parent target = FXMLLoader.load(getClass().getResource("/static/fxml/complexCmd.fxml"));//载入窗口B的定义文件；<span style="white-space:pre">	</span>
//                Scene scene = new Scene(target); //创建场景；
//                Stage stg = new Stage();//创建舞台；
//                stg.setScene(scene); //将场景载入舞台；
//                stg.show(); //显示窗口；
//                this.getStage().close();
//                System.out.println("- WelcomeController Closed -");
//            } catch (Exception e) {
//                e.printStackTrace();
//            }

    }

    @FXML
    public void VideoEmbed(ActionEvent actionEvent) throws Exception {

        System.out.println("-- Go to Video Watermark --");
        //Parent target = FXMLLoader.load(getClass().getResource("/static/fxml/excel.fxml"));//载入窗口B的定义文件；<span style="white-space:pre">	</span>
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/static/fxml/docx.fxml"));
        Parent target = fxmlLoader.load();//载入窗口B的定义文件；<span style="white-space:pre">	</span>
        DocxController controller = (DocxController) fxmlLoader.getController();
        controller.model.setText("Video");
        Scene scene = new Scene(target); //创建场景；
        Stage stg = new Stage();//创建舞台；
        stg.setScene(scene); //将场景载入舞台；
        stg.show(); //显示窗口；
        stg.setTitle("VideoMark文档水印溯源系统");
    }

    @FXML
    public void VideoExtract(ActionEvent actionEvent) throws Exception {

        System.out.println("-- Go to Video Watermark --");
        //Parent target = FXMLLoader.load(getClass().getResource("/src/main/backup/excelExtract.fxml"));//载入窗口B的定义文件；<span style="white-space:pre">	</span>
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/static/fxml/docxExtract.fxml"));
        Parent target = fxmlLoader.load();//载入窗口B的定义文件；<span style="white-space:pre">	</span>
        DocxController controller = (DocxController) fxmlLoader.getController();
        controller.model.setText("Video");
        Scene scene = new Scene(target); //创建场景；
        Stage stg = new Stage();//创建舞台；
        stg.setScene(scene); //将场景载入舞台；
        stg.show(); //显示窗口；
        stg.setTitle("VideoMark文档水印溯源系统");
    }

}
