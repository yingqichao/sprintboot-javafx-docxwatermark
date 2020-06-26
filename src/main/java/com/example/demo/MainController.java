package com.example.demo;


import com.example.demo.LoginFXML;
import de.felixroske.jfxsupport.AbstractJavaFxApplicationSupport;
import javafx.stage.Stage;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * maven构建JavaFX+SpringBoot项目启动类
 */
@ComponentScan({"com.dome.view","com.dome.controller","com.dome.service"})
@SpringBootApplication
public class MainController extends AbstractJavaFxApplicationSupport {

    public static void main(String[] args) {
        launch(MainController.class, LoginFXML.class, args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        super.start(stage);
        stage.setTitle("用户登录");
        //窗口最大化显示
//        Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
//        stage.setX(primaryScreenBounds.getMinX());
//        stage.setY(primaryScreenBounds.getMinY());
//        stage.setWidth(primaryScreenBounds.getWidth());
//        stage.setHeight(primaryScreenBounds.getHeight());
//        stage.setMaximized(true);//设置窗口最大化
//        stage.setFullScreen(true);//全屏显示，Esc退出
//        stage.setAlwaysOnTop(true);//始终显示在其他窗口之上
    }
}