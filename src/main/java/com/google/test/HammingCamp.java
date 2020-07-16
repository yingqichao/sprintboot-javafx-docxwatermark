package com.google.test;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class HammingCamp {
    private int height;
    private int width;
    private File d;
    private int[][] gra;

    public static void main(String[] args) {
//        File source = new File("F:\\二维码\\柯达标准测试图片\\二值", "2.png");
//        File qrcode = new File("F:\\二维码\\柯达标准测试图片\\测试2", "r"+"14.png");
//        System.out.println(count(source,qrcode));

        File source = new File("F:\\二维码\\柯达标准测试图片\\二值");
        File qrcode = new File("F:\\二维码\\柯达标准测试图片\\测试2\\PP70");

        loop(source,qrcode,"","",24);

//-------------------------------------------------------------------------------
       /* File dir = new File("F:\\二维码\\柯达标准测试图片\\测试2\\p70");//原图
        File aim = new File("F:\\二维码\\柯达标准测试图片\\测试2\\pp70");//保存
        String[] filelist = dir.list();
        for (int i=1;i<=filelist.length;i++) {
            try {
                File outputfile=new File(aim,filelist[i-1]);
                for (int t=1;outputfile.exists();t++) {
                    outputfile=new File(aim,i+t+".png");
                }
                imageout(whitesidecut(ImageIO.read(new File(dir,filelist[i-1])), 123),outputfile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }*/

    }

    public static void imageout(BufferedImage image, File outputFile) {
        try {
            ImageIO.write(image, "png", outputFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(outputFile+"----图像输出成功");
    }
    public static void loop(File sou, File aim, String head1, String head2, int num) {
        ArrayList head = new ArrayList();
        ArrayList data = new ArrayList();
        for (int i = 1; i <= num; i++) {
            File source = new File(sou, head1+i+".png");
            File qrcode = new File(aim, head2+i+".png");
            int hm=count(source,qrcode);
            head.add(i);
            data.add(getPercent(hm,369*369));
            System.out.println(i+":汉明距离为:"+hm+",比例为："+(double)hm/(369*369));
        }
        new CSVUtil().createCSVFile(head, data,"F:\\二维码","hamming_ttest");
    }
    public static int count(File source,File qrcode) {
        int size=369;
        BufferedImage qr= null;//通过imageio将图像载入
        BufferedImage sou=null;
        try {
            qr = ImageIO.read(qrcode);
            sou = ImageIO.read(source);
        } catch (IOException e) {
            e.printStackTrace();
        }
        int height=qr.getHeight();//获取图像的高
        int width=qr.getWidth();//获取图像的宽
            if (height < 369) {
                    BufferedImage img = null;
                //获取缩放后的Image对象
                Image _img = qr.getScaledInstance(369, 369, Image.SCALE_DEFAULT);
                //新建一个和Image对象相同大小的画布
                qr= new BufferedImage(369, 369, BufferedImage.TYPE_INT_RGB);
                //获取画笔
                Graphics2D graphics = qr.createGraphics();
                //将Image对象画在画布上,最后一个参数,ImageObserver:接收有关 Image 信息通知的异步更新接口,没用到直接传空
                graphics.drawImage(_img, 0, 0, null);
                //释放资源
                graphics.dispose();
            }
        int hm=0;
        for (int y = 0; y < 369; y++) {
            for (int x = 0; x < 369; x++) {
                if (sou.getRGB(x, y) != qr.getRGB(x, y)) {
                    hm++;
                }
            }
        }
        return hm;

    }

    public static BufferedImage whitesidecut(BufferedImage image,int size) {//白边切割
        int height=image.getHeight();
        int width = image.getWidth();
        BufferedImage result=new BufferedImage(size, size, BufferedImage.TYPE_INT_RGB);
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (image.getRGB(x, y) != Color.white.getRGB()) {
                    //获取画笔
                    Graphics2D graphics = result.createGraphics();
                    //将Image对象画在画布上,最后一个参数,ImageObserver:接收有关 Image 信息通知的异步更新接口,没用到直接传空
                    graphics.drawImage(image,-x,-y,null);
                    //释放资源
                    graphics.dispose();
                    return result;
                }
            }
        }
        return null;
    }

    public static String getPercent(double x, double total) {
        DecimalFormat df = new DecimalFormat("0.00%");// ##.00% 百分比格式，后面不足2位的用0补齐
        double result = x / total;
        return df.format(result);
    }

}
