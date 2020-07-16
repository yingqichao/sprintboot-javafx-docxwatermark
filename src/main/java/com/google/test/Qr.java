package com.google.test;

import com.google.zxing.WriterException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Field;
import java.util.Scanner;

public class Qr extends JFrame implements ActionListener {
    private JTextArea Contents, Ex;
    private JTextField QRheight, QRwidth, OutputPath, OutputFileName, ImagePath, ErrorCorrectionLevel, realWidth, realheight;
    private JLabel lQRheight, lQRwidth, lOutputPath, lOutputFileName, lImagePath, lErrorCorrectionLevel, lrealWidth, lrealheight, lContents, lEx;
    private JButton bOpen, bOut, bStart;
    private JLabel tips1,tips2,tips3;

    public static void main(String[] args) {

        new Qr().UI();

       /* try {
            new MyQRCodeWriter().encode();
        } catch (WriterException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }*/
    }

    public void UI() {
        this.setLayout(null);
        Font font = new Font(Font.SERIF, Font.BOLD, 18);

   /*     lQRheight = new JLabel("二维码宽度:");
        lQRheight.setBounds(20, 80, 100, 50);
        lQRheight.setFont(font);
        QRheight = new JTextField();
        QRheight.setBounds(140, 90, 150, 35);
        QRheight.setText("430");

        lQRwidth = new JLabel("二维码长度:");
        lQRwidth.setBounds(20, 140, 100, 50);//+60
        lQRwidth.setFont(font);
        QRwidth = new JTextField();
        QRwidth.setBounds(140, 150, 150, 35);
        QRwidth.setText("430");*/

        lErrorCorrectionLevel = new JLabel("纠错等级:");
        lErrorCorrectionLevel.setBounds(20, 50, 100, 50);
        lErrorCorrectionLevel.setFont(font);
        ErrorCorrectionLevel = new JTextField();
        ErrorCorrectionLevel.setBounds(140, 60, 150, 35);
        ErrorCorrectionLevel.setText("L");
        tips1 = new JLabel("*建议默认");
        tips1.setForeground(Color.red);
        tips1.setBounds(300,60,150,35);
        tips1.setFont(new Font(Font.SERIF, Font.BOLD, 16));

        lrealWidth = new JLabel("最小使用宽度:");
        lrealWidth.setBounds(20, 110, 150, 50);
        lrealWidth.setFont(font);
        realWidth = new JTextField();
        realWidth.setBounds(140, 120, 150, 35);
        realWidth.setText("5");
        tips2 = new JLabel("*实际应用场景（单位cm）");
        tips2.setForeground(Color.red);
        tips2.setBounds(300,120,200,35);
        tips2.setFont(new Font(Font.SERIF, Font.BOLD, 16));

        lrealheight = new JLabel("最小使用长度:");
        lrealheight.setBounds(20, 170, 150, 50);
        lrealheight.setFont(font);
        realheight = new JTextField();
        realheight.setBounds(140, 180, 150, 35);
        realheight.setText("5");

        lOutputPath = new JLabel("输出路径:");
        lOutputPath.setBounds(20, 230, 100, 50);
        lOutputPath.setFont(font);
        OutputPath = new JTextField();
        OutputPath.setBounds(140, 240, 300, 35);
        OutputPath.setText(".\\Output");

        lImagePath = new JLabel("图片路径:");
        lImagePath.setBounds(20, 290, 100, 50);
        lImagePath.setFont(font);
        ImagePath = new JTextField();
        ImagePath.setBounds(140, 300, 300, 35);
        ImagePath.setText(".\\Image\\1.png");
       
       
        
        
        lContents = new JLabel("嵌入内容:");
        lContents.setBounds(20, 350, 100, 50);
        lContents.setFont(font);
     
        JScrollPane scrollPane_1 = new JScrollPane();
        scrollPane_1.setBounds(140, 370, 300, 100);
        
        Contents = new JTextArea();
        Contents.setBounds(140, 370, 300, 100);
    /*    tips3 = new JLabel("*不宜过长");
        tips3.setBounds(450,420,150,35);
        tips3.setFont(new Font(Font.SERIF, Font.BOLD, 16));
        tips3.setForeground(Color.red);*/
        Contents.setText("<请输入嵌入内容>");
        Contents.setLineWrap(true); //激活自动换行功能      
        Contents.setWrapStyleWord(true);            // 激活断行不断字功能
        scrollPane_1.setViewportView(Contents);
        
        JScrollPane scrollPane_2 = new JScrollPane();
        scrollPane_2.setBounds(140, 500, 300, 100);
        lEx = new JLabel("隐藏内容:");
        lEx.setBounds(20, 480, 100, 50);
        lEx.setFont(font);
        Ex = new JTextArea();
        Ex.setBounds(140, 500, 300, 100);
        Ex.setText("<请输入隐藏内容>");
        Ex.setLineWrap(true); //激活自动换行功能      
        Ex.setWrapStyleWord(true);            // 激活断行不断字功能
        scrollPane_2.setViewportView(Ex);
        
        bOpen = new JButton("选择二维码");
        bOpen.setBounds(450, 300, 120, 30);
        bOpen.setFont(new Font(Font.SERIF, Font.BOLD, 16));
        bOpen.addActionListener(this);

        bOut = new JButton("选择路径");
        bOut.setBounds(450, 240, 120, 30);
        bOut.setFont(new Font(Font.SERIF, Font.BOLD, 16));
        bOut.addActionListener(this);

        bStart = new JButton("开始生成");
        bStart.setBounds(230, 640, 120, 50);
        bStart.setFont(new Font(Font.SERIF, Font.BOLD, 18));
        bStart.addActionListener(this);

//        this.add(bOpen);
      //  this.add(QRheight);
     //   this.add(lQRheight);
      //  this.add(QRwidth);
      //  this.add(lQRwidth);
        this.add(scrollPane_1);
        //this.add(scrollPane_2);
        this.add(lOutputPath);
        this.add(OutputPath);
        this.add(lImagePath);
        this.add(ImagePath);
        this.add(lErrorCorrectionLevel);
        this.add(ErrorCorrectionLevel);
        this.add(lrealWidth);
        this.add(realWidth);
        this.add(lrealheight);
        this.add(realheight);
        this.add(lContents);
     
       // this.add(lEx);
        this.add(bOpen);
        this.add(bOut);
        this.add(bStart);
        this.add(tips1);
        this.add(tips2);
       // this.add(tips3);

        this.setTitle("美化二维码生成器");

        this.setSize(600, 770);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
    }

    public static int String2Color(String str) {
        Field field = null;
        Color c = null;
        try {
            field = Class.forName("java.awt.Color").getField(str);
            c = (Color) field.get(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return (int) (((int) c.getRed() << 16) | (int) (((int) c.getGreen() << 8) | c.getBlue()));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton jbt = (JButton) e.getSource();
        //1.点击bOpen要做的事为
        if (jbt == bOpen) {
            JFileChooser jfc = new JFileChooser(new File("."));
            //打开文件选择器对话框
            int status = jfc.showOpenDialog(this);
            //没有选打开按钮结果提示
            if (status != JFileChooser.APPROVE_OPTION) {
                ImagePath.setText("没有选中文件");
            } else {
                try {
                    //被选中的文件保存为文件对象
                    File file = jfc.getSelectedFile();
                    Scanner scanner = new Scanner(file);
                    ImagePath.setText(file.toString());

                } catch (FileNotFoundException e1) {
                    System.out.println("系统没有找到此文件");
                    e1.printStackTrace();
                }


            }
        } else if (jbt == bOut) {
            JFileChooser jfc = new JFileChooser(new File("."));
            jfc.setFileSelectionMode(jfc.DIRECTORIES_ONLY);
            int status = jfc.showOpenDialog(this);
            File file = jfc.getSelectedFile();
            OutputPath.setText(file.toString());

        } else if (jbt == bStart) {
            InitInfo init = InitInfo.getInstance();
         //   init.setWidth(Integer.valueOf(QRwidth.getText()));
          //  init.setHeight(Integer.valueOf(QRheight.getText()));
            init.setOutputFile(new File(OutputPath.getText()));
            init.setGraphUri(new File(ImagePath.getText()));
            init.setErrorCorrectionLevel(ErrorCorrectionLevel.getText());
            init.setrealWidth(Integer.valueOf(realWidth.getText()));
            init.setrealHeight(Integer.valueOf(realheight.getText()));
            init.setContents(Contents.getText());
            init.setEx(Ex.getText());
            try {
                new MyQRCodeWriter().encode();
            } catch (WriterException we) {
                // TODO Auto-generated catch block
                we.printStackTrace();
            }
            JOptionPane.showMessageDialog(null, "制作完成！", "提示", JOptionPane.PLAIN_MESSAGE);

        }
    }
}
