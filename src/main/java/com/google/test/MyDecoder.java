package com.google.test;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.client.result.common.HybridBinarizer;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Scanner;


public class MyDecoder extends JFrame implements ActionListener {
    private BinaryBitmap binaryBitmap;
    private File input;
    private int height;
    private int width;
    private int[][] Matrix;
    private JTextArea ta;
    private JFileChooser jfc = new JFileChooser(new File("."));
    private JButton bOpen, bSave;
    private JScrollPane ps;

    public static void main(String[] args) {

        new MyDecoder().MyDecoderUI();
//        new MyDecoder().getMessage(new File("F:\\二维码","1.png"));
    }

    public void actionPerformed(ActionEvent e) {

        JButton jbt = (JButton) e.getSource();
        //1.点击bOpen要做的事为
        if (jbt == bOpen) {
            //打开文件选择器对话框
            int status = jfc.showOpenDialog(this);
            //没有选打开按钮结果提示
            if (status != JFileChooser.APPROVE_OPTION) {
                ta.setText("没有选中文件");
            } else {

                try {
                    //被选中的文件保存为文件对象
                    File file = jfc.getSelectedFile();
                    Scanner scanner = new Scanner(file);
                    input = file;
                    BufferedImage image = null;

                    image = ImageIO.read(input);
                    width = image.getWidth();
                    height = image.getHeight();

                    binaryBitmap = new BinaryBitmap(new HybridBinarizer(new BufferedImageLuminanceSource(image)));

                    MultiFormatReader formatReader = new MultiFormatReader();
                    Result result = null;
//            result = formatReader.decode(binaryBitmap, hints);
                    result = formatReader.decode(binaryBitmap);


                    StringBuffer info = new StringBuffer();
//                    info.append(result.getRawBytes().length);
                    info.append("解析结果：" + result.toString()+"\n");
                    info.append("二维码格式类型：" + result.getBarcodeFormat()+"\n");
                    info.append("二维码文本内容：" + result.getText()+"\n");
                    info.append("隐藏内容：" + result.getAdditionalInformation()+"\n");
                    ta.setText(info.toString());

                } catch (FileNotFoundException e1) {
                    System.out.println("系统没有找到此文件");
                    e1.printStackTrace();
                } catch (IOException ex) {
                    ex.printStackTrace();
                } catch (NotFoundException ex) {
                    ex.printStackTrace();
                }


            }

        }
        //表示单击存盘按钮
        /*else {
            int re = jfc.showOpenDialog(this);
            if (re == JFileChooser.APPROVE_OPTION) {

                File f = jfc.getSelectedFile();
                try {
                    FileOutputStream fsp = new FileOutputStream(f);
                    BufferedOutputStream out = new BufferedOutputStream(fsp);
                    //将文本内容转换为字节存到字节数组
                    byte[] b = (ta.getText()).getBytes();
                    //将数组b中的全部内容写到out流对应的文件中
                    out.write(b, 0, b.length);
                    out.close();


                } catch (FileNotFoundException e1) {
                    System.out.println("系统没有找到此文件");

                } catch (IOException e1) {

                    System.out.println("IOException");
                }
            }


        }*/

    }

    public void MyDecoderUI() {
        ta = new JTextArea(10, 20);
        ta.setEditable(false);
        ps = new JScrollPane(ta);
        bOpen = new JButton("选择二维码");
//        bSave = new JButton("保存文件");
        bOpen.addActionListener(this);
//        bSave.addActionListener(this);
        this.add(ps);
        this.add(bOpen);
//        this.add(bSave);
        this.setTitle("定制二维码扫描器");
        this.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 10));
        this.setSize(300, 300);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
    }


    public void getMessage(File file) {//获取二维码信息
        //定义二维码的参数
       /* Hashtable<EncodeHintType,String> hints = new Hashtable<EncodeHintType, String>();
        hints.put(EncodeHintType.CHARACTER_SET, "utf-8");//编码方式*/
        input = file;
        BufferedImage image = null;
        try {
            image = ImageIO.read(input);
            width = image.getWidth();
            height = image.getHeight();
        } catch (IOException e) {
            e.printStackTrace();
        }
        binaryBitmap = new BinaryBitmap(new HybridBinarizer(new BufferedImageLuminanceSource(image)));

        MultiFormatReader formatReader = new MultiFormatReader();
        Result result = null;
        try {
//            result = formatReader.decode(binaryBitmap, hints);
            result = formatReader.decode(binaryBitmap);
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        /*System.out.println(result.getRawBytes().length);*/
        System.out.println("解析结果：" + result.toString());
        System.out.println("二维码格式类型：" + result.getBarcodeFormat());
        System.out.println("二维码文本内容：" + result.getText());
        System.out.println("隐藏内容：" + result.getAdditionalInformation());
    }

    public int[][] getMatrix() {//获取扫描矩阵
        try {
            Matrix = binaryBitmap.getBlackMatrix().toArray();
            for (int y = 0; y < Matrix.length; y++) {
                for (int x = 0; x < Matrix.length; x++) {
                    Matrix[y][x] = Matrix[y][x] == 1 ? 0 : 1;
                }
            }
            return Matrix;
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public int[][] getRMatrix() {//获取扫描矩阵
        try {
            int[][] RMatrix = binaryBitmap.getBlackMatrix().toArray();
            return RMatrix;
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public int[][] getMicMatrix() {//获取41*41矩阵,仅适用于369
        if (Matrix != null) {
            if (Matrix.length != 369) {
                try {
                    throw new Exception("仅适用于尺寸为369x369的图片");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            int[][] micMatrix = new int[41][41];
            for (int y = 4, yy = 0; y < Matrix.length; y += 9, yy++) {
                for (int x = 4, xx = 0; x < Matrix.length; x += 9, xx++) {
                    micMatrix[yy][xx] = Matrix[y][x];
                }
            }
            return micMatrix;
        }
        return null;
    }

    public void log(int[][] m) {//txt日志
        int height = m.length;
        int width = m[0].length;
        File outputFile = new File("G:\\研究生\\二维码\\测试图", "log.txt");
        for (int i = 1; outputFile.exists(); i++)//判断文件名是否重复
        {
            outputFile = new File("G:\\研究生\\二维码\\测试图", "log" + "(" + i + ")" + ".txt");
        }
        try {
            FileWriter fw = new FileWriter(outputFile, true);
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    fw.write("" + m[y][x]);
                }
                fw.write("\r\n");
            }
            fw.close();
            System.out.println("输出完成:" + outputFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

