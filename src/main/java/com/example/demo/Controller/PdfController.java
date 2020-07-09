package com.example.demo.Controller;

import com.example.demo.PdfHelper.PdfParsing;
import com.example.demo.PdfHelper.PdfWm;
import com.example.demo.entity.WelcomeFXML;
import de.felixroske.jfxsupport.AbstractJavaFxApplicationSupport;
import de.felixroske.jfxsupport.FXMLController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;

import javax.imageio.ImageIO;

@FXMLController
public class PdfController implements Initializable {

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
    @FXML
    private Button btnEmbed;
    @FXML
    private ImageView logo;
    @FXML
    private Label englishResult;
    @FXML
    private Label chineseResult;
    @FXML
    private Label showFile;
    @FXML
    private Label showOutDir;
    @FXML
    private Button btnExtract;
    @FXML
    private Button btnBack;

    private String filepath = new String();
    private String watermark = new String();
    private String outDir = new String();
    private String savename = new String();
    //docx存放解压后文件夹的暂存路径
    private static String desDir = "D://UnzipDocx";




    @Override
    public void initialize(URL location, ResourceBundle resources) {
//        Image image = new Image("file:D:\\\\sprintboot-javafx-docxwatermark\\\\src\\\\main\\\\resources\\\\static\\\\logo\\\\solve.jpg");
//
//        logo.setImage(image);
        System.out.println("- PdfController initialized -");
    }

    @FXML
    public void startEmbed(ActionEvent actionEvent) {
        watermark = waterText.getText();
        savename = outFileName.getText();
        System.out.println("水印内容： "+watermark);
        System.out.println("文件保存名： "+savename);
        if(filepath.length()==0){
            f_alert_informationDialog("必须先选择需要执行操作的文件！","单击确定以返回");
            return;
        }
        if(watermark.length()==0) {
            f_alert_informationDialog("请输入水印内容！", "单击确定以返回");
            return;
        }
//        }else if(watermark.length()>16){
//            f_alert_informationDialog("水印长度超出限定长度（16个汉字）","单击确定以返回");
//            return;
//        }
        if(outDir.length()==0 || savename.length()==0){
            f_alert_informationDialog("请输入保存文件的路径/名称！","单击确定以返回");
            return;
        }

        String outPathFile = outDir+"\\"+savename+".pdf";
        //嵌入主逻辑
        try {

//            String PdfFile = "C:\\Users\\24962\\IdeaProjects\\PDFWM\\data\\猪流行性腹泻病毒基因及其疫苗的研究_王凤.pdf";
//            String dstFile = "C:\\Users\\24962\\IdeaProjects\\PDFWM\\wmData\\WM-猪流行性腹泻病毒基因及其疫苗的研究_王凤.pdf";

            PdfWm pdfps = new PdfWm();
            pdfps.embedWm(filepath, outPathFile, watermark);
            sendText(actionEvent);

        }catch (Exception e){
            f_alert_informationDialog("嵌入失败！","水印： " + watermark+" ，文件保存路径： " + outPathFile);
            e.printStackTrace();
        }

    }

    @FXML
    private void sendText(ActionEvent actionEvent) throws Exception {
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
        String fileAddress = outDir+"\\"+savename+".pdf";
        control.model.setText(fileAddress);
        // 获得摘要
        String out = PdfParsing.pdf2png(outDir,savename,0,1,"jpg");
        System.out.println("Screenshot for "+fileAddress+" saves As: "+out);
        control.model.setIsWord(true);
        //关闭窗口
        ((Node) (actionEvent.getSource())).getScene().getWindow().hide();
    }


    @FXML
    public void startExtract(ActionEvent actionEvent) {
        if(filepath.length()==0){
            f_alert_informationDialog("必须先选择需要执行操作的文件！","单击确定以返回");
            return;
        }
        //提取主逻辑
        try {

            PdfWm pdfps = new PdfWm();
            String s = pdfps.extractWm(filepath);
            chineseResult.setText(s);

            if(s.length() > 0)
                System.out.println("Extracted watermark : " + s);
            else
                System.out.println("There is no watermark!");
            //关闭窗口
            ((Node) (actionEvent.getSource())).getScene().getWindow().hide();

            f_alert_informationDialog("提取已完成！","提取结果： " + s);

        }catch (Exception e){
            e.printStackTrace();
        }
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
        if(englishResult!=null) englishResult.setText("* 空白 *");
        if(chineseResult!=null) chineseResult.setText("* 空白 *");
        if(waterText!=null) waterText.clear();
        if(outFileName!=null) outFileName.clear();
        if(filepath!=null) filepath = new String();
        if(watermark!=null) watermark = new String();
        if(outDir!=null) outDir = new String();
        if(savename!=null) savename = new String();
        if(showOutDir!=null) showOutDir.setText("* 未选择 *");
        if(showFile!=null) showFile.setText("* 未选择文件 *");
    }

    public String chooseFile() {
        try {
            Stage mainStage = null;
            FileChooser fileChooser = new FileChooser();//构建一个文件选择器实例

            fileChooser.setTitle("选择文件");
            fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Microsoft Pdf Files", "*.pdf"));
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


    /**
     * 转换全部的pdf
     * @param fileAddress 文件地址
     * @param filename PDF文件名
     * @param type 图片类型
     */
    public static void pdf2png(String fileAddress,String filename,String type) {
        // 将pdf装图片 并且自定义图片得格式大小
        File file = new File(fileAddress+"\\"+filename+".pdf");
        try {
            PDDocument doc = PDDocument.load(file);
            PDFRenderer renderer = new PDFRenderer(doc);
            int pageCount = doc.getNumberOfPages();
            for (int i = 0; i < pageCount; i++) {
                BufferedImage image = renderer.renderImageWithDPI(i, 144); // Windows native DPI
                // BufferedImage srcImage = resize(image, 240, 240);//产生缩略图
                ImageIO.write(image, type, new File(fileAddress+"\\"+filename+"_"+(i+1)+"."+type));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}