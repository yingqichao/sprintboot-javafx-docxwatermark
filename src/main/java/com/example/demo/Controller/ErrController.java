package com.example.demo.Controller;

import com.example.demo.Globe;
import com.example.demo.entity.AppModel;
import de.felixroske.jfxsupport.AbstractJavaFxApplicationSupport;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;


import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class ErrController extends AbstractJavaFxApplicationSupport implements Initializable {

    // 必须static 类型
    public  static AppModel model = new AppModel();

    @FXML
    private Label msg;
    @FXML
    private ImageView logo;
    @FXML
    TextField username;
    @FXML
    TextField password;

    private Map<String,String> validUsers = new HashMap<>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        model.textProperty().addListener((obs, oldText, newText) -> {if(msg!=null) msg.setText(newText);});
        if(logo!=null){
            Image logoimage = new Image(getClass().getResource("/static/logo/solve.jpg").toExternalForm());
            logo.setImage(logoimage);
        }

        validUsers.put("yingqichao","123456");
    }


    @FXML
    public void confirm(ActionEvent actionEvent) {
        String pword = password.getText();
        String uname = username.getText();

        if(!validUsers.containsKey(uname) || !validUsers.get(uname).equals(pword)){
            Globe.f_alert_informationDialog("用户名或密码错误！","Please retry!");
        }else{
            //valid user
            sendText(actionEvent);

        }
    }

    @FXML
    private void sendText(ActionEvent actionEvent){
        // 获取结果界面控制器
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/static/fxml/complexCmd.fxml"));
        try
        {
            VBox login = (VBox) loader.load();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        LoginController control = (LoginController) loader.getController();
        // 设置结果界面内容
        //String fileAddress = outDir+"\\"+savename+".docx";
        control.model.setPassword(password.getText());
        control.model.setUsername(username.getText());//true

//        WordParsing.convertDocxToPDF(new File(fileAddress),outDir+"\\"+savename+".pdf");
//        System.out.println("Saving PDF: "+outDir+"\\"+savename+".pdf");
//        // 获得摘要
//        String out = PdfParsing.pdf2png(outDir,savename,0,1,"jpg");
//        System.out.println("Screenshot for "+fileAddress+" saves As: "+out);
        //关闭窗口
        ((Node) (actionEvent.getSource())).getScene().getWindow().hide();
    }


}
