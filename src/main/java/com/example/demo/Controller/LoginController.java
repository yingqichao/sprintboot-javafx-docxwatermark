package com.example.demo.Controller;

import com.example.demo.entity.AppModel;
import com.example.demo.entity.WelcomeFXML;
import com.jfoenix.controls.JFXButton;
import de.felixroske.jfxsupport.AbstractJavaFxApplicationSupport;
import de.felixroske.jfxsupport.FXMLController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

@FXMLController
public class LoginController extends AbstractJavaFxApplicationSupport implements Initializable {

    @FXML
    private TextField waterText;
    @FXML
    private TextField outFileName;
    @FXML
    private Button btnReset;
    @FXML
    private Button btnSelectFile;
    @FXML
    private Button btnSelectOutDir;
//    @FXML
//    private Button btnEmbed;

    @FXML
    private Label englishResult;
    @FXML
    private Label chineseResult;
    @FXML
    private Label showFile;
    @FXML
    private Label showOutDir;
    @FXML
    private JFXButton pdfEmbed;
    @FXML
    private JFXButton btnRelatedFile;
    @FXML
    private Button pdfExtract;
    @FXML
    private JFXButton docxEmbed;
    @FXML
    private Button docxExtract;
    @FXML
    private Button btnBack;
    @FXML
    private ImageView welcomeLogo;
    @FXML
    private Label filename;

    private String filepath = new String();
    private String watermark = new String();
    private String outDir = new String();
    private String savename = new String();
    //docx存放解压后文件夹的暂存路径
    private static String desDir = "D://UnzipDocx";

    // 必须static 类型
    public  static AppModel model = new AppModel();


    @Override
    public void initialize(URL location, ResourceBundle resources) {
//        Class<?> clazz = this.getClass();
//        InputStream input = clazz.getResourceAsStream("/com/xntutor/javafx/icon/java-32.png");
        Image logoImage = new Image("file:D:\\\\sprintboot-javafx-docxwatermark\\\\src\\\\main\\\\resources\\\\static\\\\logo\\\\logo.jpg");
        Image wordImage = new Image("file:D:\\\\sprintboot-javafx-docxwatermark\\\\src\\\\main\\\\resources\\\\static\\\\logo\\\\wordImage.png");
        Image pdfImage = new Image("file:D:\\\\sprintboot-javafx-docxwatermark\\\\src\\\\main\\\\resources\\\\static\\\\logo\\\\pdfImage.png");

        //function1.setImage(image);function2.setImage(image);
        model.textProperty().addListener((obs, oldText, newText) -> filename.setText(newText));
        model.isWordProperty().addListener((obs, oldbool, isWord) -> {
            String name = filename.getText();
            if (isWord)
                welcomeLogo.setImage(new Image("file:"+name.substring(0,name.length()-4)+".jpg"));
            else
                welcomeLogo.setImage(pdfImage);
        });
        System.out.println("- LoginController initialized - Got Text:"+filename.getText());
//        try {
//            Parent target = FXMLLoader.load(getClass().getResource("/static/fxml/welcome.fxml"));//载入窗口B的定义文件；<span style="white-space:pre">	</span>
//            Scene scene = new Scene(target); //创建场景；
//            Stage stg = new Stage();//创建舞台；
//            stg.setScene(scene); //将场景载入舞台；
//            stg.show(); //显示窗口；
//        }catch (Exception e){
//            e.printStackTrace();
//        }

        welcomeLogo.setImage(logoImage);
    }

    @FXML
    public void openRelatedFile(ActionEvent actionEvent) throws Exception{
        try {
            getHostServices().showDocument(filename.getText());
        }catch (Exception e){
            System.out.println("打开文件操作无效，文件名："+filename.getText());
        }

    }

    @FXML
    public void docxEmbed(ActionEvent actionEvent) throws Exception{
        System.out.println("-- Go to Docx Watermark --");
        Parent target = FXMLLoader.load(getClass().getResource("/static/fxml/docx.fxml"));//载入窗口B的定义文件；<span style="white-space:pre">	</span>
        Scene scene = new Scene(target); //创建场景；
        Stage stg = new Stage();//创建舞台；
        stg.setScene(scene); //将场景载入舞台；
        stg.show(); //显示窗口；
        stg.setTitle("DocxMark文档水印溯源系统");

    }

    @FXML
    public void docxExtract(ActionEvent actionEvent) throws Exception{
        System.out.println("-- Go to Docx Watermark --");
        Parent target = FXMLLoader.load(getClass().getResource("/static/fxml/docxExtract.fxml"));//载入窗口B的定义文件；<span style="white-space:pre">	</span>
        Scene scene = new Scene(target); //创建场景；
        Stage stg = new Stage();//创建舞台；
        stg.setScene(scene); //将场景载入舞台；
        stg.show(); //显示窗口；
        stg.setTitle("DocxMark文档水印溯源系统");

    }

    @FXML
    public void showInfo(ActionEvent actionEvent) throws Exception{
        Parent target = FXMLLoader.load(getClass().getResource("/static/fxml/info.fxml"));//载入窗口B的定义文件；<span style="white-space:pre">	</span>
        Scene scene = new Scene(target); //创建场景；
        Stage stg = new Stage();//创建舞台；
        stg.setScene(scene); //将场景载入舞台；
        stg.show(); //显示窗口；
    }

    @FXML
    public void pdfEmbed(ActionEvent actionEvent) throws Exception {
        System.out.println("-- Go to Pdf Watermark --");
        Parent target = FXMLLoader.load(getClass().getResource("/static/fxml/pdf.fxml"));//载入窗口B的定义文件；<span style="white-space:pre">	</span>
        Scene scene = new Scene(target); //创建场景；
        Stage stg = new Stage();//创建舞台；
        stg.setScene(scene); //将场景载入舞台；
        stg.show(); //显示窗口；
        stg.setTitle("PdfMark文档水印溯源系统");
        //this.getStage().close();
    }

    @FXML
    public void pdfExtract(ActionEvent actionEvent) throws Exception {
        System.out.println("-- Go to Pdf Watermark --");
        Parent target = FXMLLoader.load(getClass().getResource("/static/fxml/pdfExtract.fxml"));//载入窗口B的定义文件；<span style="white-space:pre">	</span>
        Scene scene = new Scene(target); //创建场景；
        Stage stg = new Stage();//创建舞台；
        stg.setScene(scene); //将场景载入舞台；
        stg.show(); //显示窗口；
        stg.setTitle("PdfMark文档水印溯源系统");
        //this.getStage().close();
    }



    @FXML
    public void selectFile(ActionEvent actionEvent) {
        String select = chooseFile();
        if(select!=null) {
            filepath = select;
            System.out.println("文件选择： "+filepath);
        }
        showFile.setText(filepath);
    }

    @FXML
    public void selectOutDir(ActionEvent actionEvent) {
        String select = chooseFolder();
        if(select!=null) {
            outDir = select;
            System.out.println("输出文件夹选择： "+outDir);
        }
        showOutDir.setText(outDir);
    }

    @FXML
    public void backward(ActionEvent actionEvent) {
        //launch(MainController.class, LoginFXML.class, null);
        AbstractJavaFxApplicationSupport.showView(WelcomeFXML.class);
    }

    @FXML
    public void reset(ActionEvent actionEvent) {
        englishResult.setText("* 空白 *");
        chineseResult.setText("* 空白 *");
        waterText.clear();
        outFileName.clear();
        filepath = new String();
        watermark = new String();
        outDir = new String();
        savename = new String();
        showOutDir.setText("* 未选择 *");
        showFile.setText("* 未选择文件 *");
    }

    public String chooseFile() {
        try {
            Stage mainStage = null;
            FileChooser fileChooser = new FileChooser();//构建一个文件选择器实例

            fileChooser.setTitle("选择文件");
            fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Microsoft Word File", "*.docx"));
            File selectedFile = fileChooser.showOpenDialog(mainStage);
            return selectedFile.getPath();
        }catch(Exception e){
            System.out.println("未选择文件");
            return null;
        }
    }

    public String chooseFolder() {
        try {
        Stage fileStage = null;
        DirectoryChooser folderChooser = new DirectoryChooser();
        folderChooser.setTitle("Choose Folder");
        File selectedFile = folderChooser.showDialog(fileStage);
        return selectedFile.getPath();
        }catch(Exception e){
            System.out.println("未选择文件夹");
            return null;
        }
    }


    /**
     * 弹出一个通用的确定对话框
     * @param p_header 对话框的信息标题
     * @param p_message 对话框的信息
     * @return 用户点击了是或否
     */
    public boolean f_alert_confirmDialog(String p_header,String p_message){
//        按钮部分可以使用预设的也可以像这样自己 new 一个
        Alert _alert = new Alert(Alert.AlertType.CONFIRMATION,p_message,new ButtonType("取消", ButtonBar.ButtonData.NO),
                new ButtonType("确定", ButtonBar.ButtonData.YES));
//        设置窗口的标题
        _alert.setTitle("确认");
        _alert.setHeaderText(p_header);
//        设置对话框的 icon 图标，参数是主窗口的 stage
        //_alert.initOwner(d_stage);
//        showAndWait() 将在对话框消失以前不会执行之后的代码
        Optional<ButtonType> _buttonType = _alert.showAndWait();
//        根据点击结果返回
        if(_buttonType.get().getButtonData().equals(ButtonBar.ButtonData.YES)){
            return true;
        }
        else {
            return false;
        }
    }

    //    弹出一个信息对话框
    public void f_alert_informationDialog(String p_header, String p_message){
        Alert _alert = new Alert(Alert.AlertType.INFORMATION);
        _alert.setTitle("Error");
        _alert.setHeaderText(p_header);
        _alert.setContentText(p_message);
        //_alert.initOwner(d_stage);
        _alert.show();
    }

}