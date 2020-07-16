package com.example.demo.Controller;

import com.example.demo.Excel.Utils.Util;
import com.example.demo.Globe;
import com.google.test.InitInfo;
import com.google.test.MyQRCodeWriter;
import com.google.zxing.WriterException;
import de.felixroske.jfxsupport.AbstractJavaFxApplicationSupport;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class QrCodeController extends AbstractJavaFxApplicationSupport implements Initializable {

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
    private ImageView oriImage;
    @FXML
    private ImageView outImage;


    private String filepath = new String();
    private String watermark = new String();
    private String outDir = new String();
    private String savename = new String();


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


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if(anchor1!=null) anchor1.setStyle(cssDefault);
        if(anchor2!=null) anchor2.setStyle(cssDefault);
        if(anchor3!=null) anchor3.setStyle(cssDefault);
        oriImage.setStyle(cssDefault);outImage.setStyle(cssDefault);
    }

    @FXML
    private void start(ActionEvent actionEvent) throws Exception {
        String outPathFile = outDir+"\\"+savename+".png";
        String ErrorCorrectionLevel = "L";
        String Ex = "";
        String realWidth = "5";String realheight = "5";
        InitInfo init = InitInfo.getInstance();
        //   init.setWidth(Integer.valueOf(QRwidth.getText()));
        //  init.setHeight(Integer.valueOf(QRheight.getText()));
        init.setOutputFile(new File(outPathFile));
        init.setGraphUri(new File(filepath));
        init.setErrorCorrectionLevel(ErrorCorrectionLevel);
        init.setrealWidth(Integer.valueOf(realWidth));
        init.setrealHeight(Integer.valueOf(realheight));
        init.setContents(watermark);
        init.setEx(Ex);
        try {
            new MyQRCodeWriter().encode();
            System.out.println("制作完成！");
            outImage.setImage(new Image("file:"+outPathFile));
        } catch (WriterException we) {
            // TODO Auto-generated catch block
            we.printStackTrace();
        }

    }

    @FXML
    private void showDetail(ActionEvent actionEvent) throws Exception{
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/static/fxml/log.fxml"));
        Parent target = fxmlLoader.load();//载入窗口B的定义文件；<span style="white-space:pre">	</span>
        ErrController controller = (ErrController) fxmlLoader.getController();
        controller.model.setText(errorMsg);
//        if(target.getScene() != null)
//            target.getScene().setRoot(null);
        Scene scene = new Scene(target); //创建场景；
        Stage stg = new Stage();//创建舞台；
        stg.setScene(scene); //将场景载入舞台；
        stg.show(); //显示窗口；

    }

    @FXML
    public void selectFile(ActionEvent actionEvent) {
        String[] description;String[] extension;

        description = new String[]{"JPG files (*.jpg)", "PNG files (*.png)","JPEG Files (*.jpeg)"};
        extension =  new String[]{"*.JPG","*.PNG",".JPEG"};


        String select = Globe.chooseFile(description,extension);
        if(select!=null) {
            filepath = select;
            System.out.println("文件选择： "+filepath);
            errorMsg = "文件选择： "+filepath;
        }
        showFile.setText(filepath);
        oriImage.setImage(new Image("file:"+filepath));
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
    public void reset(ActionEvent actionEvent) {

    }
}
