package com.example.demo.Controller;

import com.example.demo.Dom4jHelper;
import com.example.demo.Excel.MainEmbed_excel;
import com.example.demo.Excel.MainExtract_excel;
import com.example.demo.Excel.Utils.Util;
import com.example.demo.Globe;
import com.example.demo.PdfHelper.PdfParsing;
import com.example.demo.PdfHelper.PdfWm;
import com.example.demo.Setting;
import com.example.demo.VideoWatermark;
import com.example.demo.WordHelper.WordParsing;
import com.example.demo.entity.AppModel;
import com.example.demo.entity.WelcomeFXML;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import de.felixroske.jfxsupport.AbstractJavaFxApplicationSupport;
import de.felixroske.jfxsupport.FXMLController;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;

import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.scene.Node;

@FXMLController
public class DocxController extends AbstractJavaFxApplicationSupport implements Initializable {

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
    private Button btnDetail;
//    @FXML
//    private Button btnEmbed;

    @FXML
    private Button btnEmbed;

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
    @FXML
    private Button btnFromFile;
    @FXML
    private Label extension;

    @FXML
    private ImageView wordImage;
    @FXML
    private ImageView pdfImage;
    @FXML
    private ImageView excelImage;

    private String filepath = new String();
    private String watermark = new String();
    private String outDir = new String();
    private String savename = new String();
    //docx存放解压后文件夹的暂存路径
    private static String desDir = "D://UnzipDocx";

    private String cssDefault = "-fx-border-color: black;\n" + "-fx-border-insets: 1;\n"
            + "-fx-border-width: 1;\n" + "-fx-border-style: dashed;\n";

    private FXMLLoader fxmlLoader = null;
    private Parent target = null;
    private ErrController controller = null;
    private String errorMsg = "";

    @FXML
    private AnchorPane anchor1;
    @FXML
    private AnchorPane anchor2;
    @FXML
    private AnchorPane anchor3;

    @FXML
    private RadioButton wordSelect;
    @FXML
    private RadioButton pdfSelect;
    @FXML
    private RadioButton excelSelect;

    private String mode = model.getText();

//    final ToggleGroup group = new ToggleGroup();
    // 必须static 类型
    public  static AppModel model = new AppModel();
    public String ffmpegPath = "D:/";//getClass().getResource("/static/").toExternalForm();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Image excel = new Image(getClass().getResource("/static/logo/excelImage.png").toExternalForm());
        Image word = new Image(getClass().getResource("/static/logo/wordImage.png").toExternalForm());
        Image pdf = new Image(getClass().getResource("/static/logo/pdfImage.png").toExternalForm());
        Image video = new Image(getClass().getResource("/static/logo/videoImage.png").toExternalForm());
        System.out.println("Model.Text: "+model.getText()+" Mode: "+mode);
        model.textProperty().addListener((obs, oldText, newText) -> {
            mode = newText;
            if(mode!=null)
                if(mode.equals("Word"))  {
                    wordImage.setImage(word);if(extension!=null) extension.setText(".docx");
                }
                else if(mode.equals("Excel"))  {
                    wordImage.setImage(excel);if(extension!=null) extension.setText(".xlsx/.csv");
                }
                else if(mode.equals("Pdf"))  {
                    wordImage.setImage(pdf);if(extension!=null) extension.setText(".pdf");
                }
                else if(mode.equals("Video"))  {
                    wordImage.setImage(video);if(extension!=null) extension.setText(".mp4");
                }
        });
        if(mode!=null)
            if(mode.equals("Word"))  {
                wordImage.setImage(word);if(extension!=null) extension.setText(".docx");
            }
            else if(mode.equals("Excel"))  {
                wordImage.setImage(excel);if(extension!=null) extension.setText(".xlsx/.csv");
            }
            else if(mode.equals("Pdf"))  {
                wordImage.setImage(pdf);if(extension!=null) extension.setText(".pdf");
            }
            else if(mode.equals("Video"))  {
                wordImage.setImage(video);if(extension!=null) extension.setText(".mp4");
            }

        //Image image = new Image("file:D:\\\\sprintboot-javafx-docxwatermark\\\\src\\\\main\\\\resources\\\\static\\\\logo\\\\solve.jpg");
        if(anchor1!=null) anchor1.setStyle(cssDefault);
        if(anchor2!=null) anchor2.setStyle(cssDefault);
        if(anchor3!=null) anchor3.setStyle(cssDefault);
        //        excelImage.setImage(excel);
//        pdfImage.setImage(pdf);


//        wordSelect.setToggleGroup(group);
//        wordSelect.setUserData(Setting.WORD_MODE);
//        pdfSelect.setToggleGroup(group);
//        pdfSelect.setUserData(Setting.PDF_MODE);
//        excelSelect.setToggleGroup(group);
//        excelSelect.setUserData(Setting.EXCEL_MODE);

        System.out.println("- DocxController initialized -");

        // 选中某个单选框时输出选中的值
//        group.selectedToggleProperty().addListener(
//                new ChangeListener<Toggle>() {
//                    public void changed(ObservableValue<? extends Toggle> ov,Toggle old_toggle, Toggle new_toggle) {
//                        if (group.getSelectedToggle() != null) {
//                            System.out.println(group.getSelectedToggle().getUserData());
//                        }
//                    }
//                });


    }

    @FXML
    public void fromFile(ActionEvent actionEvent) throws Exception {
        try {
            String select = Globe.chooseFile( new String[]{"Text File"}, new String[]{"*.txt"});
            System.out.println("文件选择： " + select);
            //errorMsg = "文件选择： "+select;
            String wm = Util.readWatermark(select);
            System.out.println("读取到的水印信息： " + wm);
            waterText.setText(wm);
            watermark = wm;
        }catch (Exception e){
            System.out.println("未选择文件" );
        }
    }

    @FXML
    public void startEmbed(ActionEvent actionEvent) throws Exception {
        watermark = waterText.getText();
        savename = outFileName.getText();
        if(filepath.length()==0){
            Globe.f_alert_informationDialog("必须先选择需要执行操作的文件！","单击确定以返回");
            return;
        }
        if(watermark.length()==0) {
            Globe.f_alert_informationDialog("请输入水印内容！", "单击确定以返回");
            return;
        }
        if(outDir.length()==0 || savename.length()==0){
            Globe.f_alert_informationDialog("请输入保存文件的路径/名称！","单击确定以返回");
            return;
        }
        System.out.println("水印内容： "+watermark);
        System.out.println("文件保存名： "+savename);
        if(mode.equals("Word")){
            docxEmbed(actionEvent);
        }else if(mode.equals("Pdf")){
            pdfEmbed(actionEvent);
        }else if(mode.equals("Excel")){
            excelEmbed(actionEvent);
        }else if(mode.equals("Video")){
            videoEmbed(actionEvent);
        }else{
            Globe.f_alert_informationDialog("模式出错，请重试！","单击确定以返回");
        }
    }

    public void excelEmbed(ActionEvent actionEvent) throws Exception {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);

        String append = filepath.substring(filepath.indexOf("."));
        extension.setText(append);
        String outPathFile = outDir+"\\"+savename+append;
        //嵌入主逻辑
        try {
            MainEmbed_excel.Embed(filepath,null,watermark,outPathFile);
            //f_alert_informationDialog("嵌入已完成！","水印： " + watermark+" ，文件保存路径： " + outPathFile);
            sendText(actionEvent,outPathFile);

        }catch (Exception e){
            //e.printStackTrace();
            Globe.f_alert_informationDialog("嵌入失败！","水印： " + watermark+" ，文件保存路径： " + outPathFile);
            e.printStackTrace(pw);
            pw.flush();
            sw.flush();
            errorMsg = sw.toString();
            //打开新的窗口
            showDetail(null);

        } finally {
            try {
                pw.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            try {
                sw.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

    }

    public void videoEmbed(ActionEvent actionEvent) throws Exception {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);

        String append = filepath.substring(filepath.indexOf("."));
        extension.setText(append);
        String outPathFile = outDir+"/"+savename+append;

        //嵌入主逻辑
        try {
            VideoWatermark.embed(filepath, watermark, ffmpegPath, outPathFile);
            //f_alert_informationDialog("嵌入已完成！","水印： " + watermark+" ，文件保存路径： " + outPathFile);
            sendText(actionEvent,outPathFile);

        }catch (Exception e){
            //e.printStackTrace();
            Globe.f_alert_informationDialog("嵌入失败！","水印： " + watermark+" ，文件保存路径： " + outPathFile);
            e.printStackTrace(pw);
            pw.flush();
            sw.flush();
            errorMsg = sw.toString();
            //打开新的窗口
            showDetail(null);

        } finally {
            try {
                pw.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            try {
                sw.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

    }

    public void pdfEmbed(ActionEvent actionEvent) {


        String outPathFile = outDir+"\\"+savename+".pdf";
        //嵌入主逻辑
        try {

//            String PdfFile = "C:\\Users\\24962\\IdeaProjects\\PDFWM\\data\\猪流行性腹泻病毒基因及其疫苗的研究_王凤.pdf";
//            String dstFile = "C:\\Users\\24962\\IdeaProjects\\PDFWM\\wmData\\WM-猪流行性腹泻病毒基因及其疫苗的研究_王凤.pdf";

            PdfWm pdfps = new PdfWm();
            pdfps.embedWm(filepath, outPathFile, watermark);
            sendText(actionEvent,outPathFile);

        }catch (Exception e){
            Globe.f_alert_informationDialog("嵌入失败！","水印： " + watermark+" ，文件保存路径： " + outPathFile);
            e.printStackTrace();
        }

    }

    public void docxEmbed(ActionEvent actionEvent) throws Exception {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);

        if(watermark.length()>16){
            Globe.f_alert_informationDialog("水印长度超出限定长度（16个汉字）","单击确定以返回");
            return;
        }

        String outPathFile = outDir+"\\"+savename+".docx";
        //嵌入主逻辑
        try {
            Dom4jHelper.Embed(filepath, desDir, watermark, outPathFile);
            //f_alert_informationDialog("嵌入已完成！","水印： " + watermark+" ，文件保存路径： " + outPathFile);
            sendText(actionEvent,outPathFile);

        }catch (Exception e){
            //e.printStackTrace();
            Globe.f_alert_informationDialog("嵌入失败！","水印： " + watermark+" ，文件保存路径： " + outPathFile);
            e.printStackTrace(pw);
            pw.flush();
            sw.flush();
            errorMsg = sw.toString();
            //打开新的窗口
            showDetail(null);

        } finally {
            try {
                pw.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            try {
                sw.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

    }

    @FXML
    private void showDetail(ActionEvent actionEvent) throws Exception{
        fxmlLoader = new FXMLLoader(getClass().getResource("/static/fxml/log.fxml"));
        target = fxmlLoader.load();//载入窗口B的定义文件；<span style="white-space:pre">	</span>
        controller = (ErrController) fxmlLoader.getController();
        controller.model.setText(errorMsg);
//        if(target.getScene() != null)
//            target.getScene().setRoot(null);
        Scene scene = new Scene(target); //创建场景；
        Stage stg = new Stage();//创建舞台；
        stg.setScene(scene); //将场景载入舞台；
        stg.show(); //显示窗口；

    }

    @FXML
    private void sendText(ActionEvent actionEvent,String fileAddress) throws Exception {
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
        control.model.setText(fileAddress);
        control.model.setIsWord(mode);//true

//        WordParsing.convertDocxToPDF(new File(fileAddress),outDir+"\\"+savename+".pdf");
//        System.out.println("Saving PDF: "+outDir+"\\"+savename+".pdf");
//        // 获得摘要
//        String out = PdfParsing.pdf2png(outDir,savename,0,1,"jpg");
//        System.out.println("Screenshot for "+fileAddress+" saves As: "+out);
        //关闭窗口
        ((Node) (actionEvent.getSource())).getScene().getWindow().hide();
    }

//    public void confirm(ActionEvent actionEvent)
//    {
//        //改变模板设置控制器的模板名列表属性，触发观察者
//        String inputContent=textEnter.getText();
//        ResultController.setText(inputContent);
//        //关闭窗口
//        ((Node) (actionEvent.getSource())).getScene().getWindow().hide();
//    }

    @FXML
    public void startExtract(ActionEvent actionEvent) {
        if(filepath.length()==0){
            Globe.f_alert_informationDialog("必须先选择需要执行操作的文件！","单击确定以返回");
            return;
        }
        List<String> extracted = new LinkedList<>();



        //提取主逻辑
        try {
            if(mode.equals("Word")){
                extracted = docxExtract(actionEvent);
            }else if(mode.equals("Pdf")){
                extracted = pdfExtract(actionEvent);
            }else if(mode.equals("Excel")){
                extracted = excelExtract(actionEvent);
            }else if(mode.equals("Video")){
                extracted = videoExtract(actionEvent);
            }else{
                Globe.f_alert_informationDialog("模式出错，请重试！","单击确定以返回");
            }

            //List<String> extracted = Dom4jHelper.Extract(filepath, desDir, null);
            String english = extracted.get(0);String chinese = extracted.get(1);
            englishResult.setText(english);
            chineseResult.setText(chinese);
            System.out.println("英文提取结果： " + english);
            System.out.println("中文提取结果： " + chinese);
            //关闭窗口
            ((Node) (actionEvent.getSource())).getScene().getWindow().hide();
            Globe.f_alert_informationDialog("提取已完成！","英文提取结果： " + english+" ，中文提取结果： " + chinese);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public List<String> videoExtract(ActionEvent actionEvent) throws Exception{
        String s = VideoWatermark.extract(filepath,ffmpegPath);
        List<String> res = new LinkedList<>();res.add(s);res.add(s);
        return res;
    }

    public List<String> docxExtract(ActionEvent actionEvent) throws Exception{
        return Dom4jHelper.Extract(filepath, desDir, null);
    }

    public List<String> pdfExtract(ActionEvent actionEvent) throws Exception{
        PdfWm pdfps = new PdfWm();
        String s = pdfps.extractWm(filepath);
        List<String> res = new LinkedList<>();res.add(s);res.add(s);
        return res;
    }

    public List<String> excelExtract(ActionEvent actionEvent) throws Exception{
        return MainExtract_excel.Extract(filepath,0,null);

    }

    @FXML
    public void selectFile(ActionEvent actionEvent) {
        String[] description;String[] extension;
        if(mode.equals("Word")) {
            description = new String[]{"Microsoft Word File"};
            extension = new String[]{"*.docx"};
        }else if(mode.equals("Pdf")){
            description = new String[]{"Pdf File"};
            extension = new String[]{"*.pdf"};
        }else if(mode.equals("Video")){
            description = new String[]{"Video File"};
            extension = new String[]{"*.mp4","*.avi"};
        }else{
            description = new String[]{"Microsoft Excel File", "CSV File"};
            extension =  new String[]{"*.xlsx","*.csv"};
        }

        String select = Globe.chooseFile(description,extension);
        if(select!=null) {
            filepath = select;
            System.out.println("文件选择： "+filepath);
            errorMsg = "文件选择： "+filepath;
        }
        showFile.setText(filepath);
    }

    @FXML
    public void selectOutDir(ActionEvent actionEvent) {
        String select = Globe.chooseFolder();
        if(select!=null) {
            outDir = select;
            System.out.println("输出文件夹选择： "+outDir);
            errorMsg = "输出文件夹选择： "+outDir;
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


}