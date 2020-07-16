package com.google.test;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Scanner;

public class GraphDeal {

    File d;//需处理图像+路径
    String dir;
    int gra[][];//灰度矩阵
    int HSL[][];
    int gray[][];
    int height=0;
    int width=0;
    int bn[][];//二值化矩阵
    int col[][];//RGB色彩矩阵，需用Integer.toHexString转换成16进制
    BufferedImage nbi = null;//图像
    BufferedImage bi = null;//图像
    String method;
    String grayMod;
    private static final int BLACK = 0xFF000000;
    private static final int WHITE = 0xFFFFFFFF;

    /**函数目录
     * GraphDeal(String dir, String GraphName)--初始化
     * void graphRead()--图像读取并灰度化
     * BufferedImage getBinary()--图像二值化
     * void matrixRead(int gra[][])--矩阵文本读取
     * void matrixOutput() | void matrixOutput(int[][] matrix)--矩阵文本输出
     * void matrixPrint() | void matrixPrint(int[][] matrix)--矩阵命令行输出
     * void digi()--生成二值化矩阵
     * void graPrint()--图像输出
     * BufferedImage matrixToGra()--矩阵成像
     * BufferedImage zoom(int width, int height,String fileName) |
     * BufferedImage zoom(int width, int height)--图像缩放
     * void GtoD(int width,int height)--自动化流程（图像二值化至缩放数字化）
     * public BufferedImage getBinary2()--直接读入黑白图片并二值化
     */

    public GraphDeal(File dir)
    {
        d = dir;
        this.dir = dir.getParent();
//        System.out.println("---------------图像处理---------------");
    }
    public GraphDeal(File dir,int[][] matrix)
    {
        d = dir;
        this.dir = dir.getParent();
        bn=matrix;
        width = matrix[0].length;
        height = matrix.length;
//        System.out.println("---------------图像处理---------------");
    }
    //图像读取并灰度化
    public void graphRead() {
        BufferedImage bi= null;//通过imageio将图像载入
        try {
            bi = ImageIO.read(d);
        } catch (IOException e) {
            e.printStackTrace();
        }
        height=bi.getHeight();//获取图像的高
        width=bi.getWidth();//获取图像的宽
        nbi=new BufferedImage(width,height,BufferedImage.TYPE_BYTE_BINARY);
        gra=new int[height][width];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < height; x++) {
                gra[y][x] = getGray(bi.getRGB(x, y));
                nbi.setRGB(x, y, new Color(gra[y][x],gra[y][x],gra[y][x]).getRGB());
            }
        }
//        System.out.println("灰度化完成：（"+grayMod+"）");
    }
    //灰度化
    
    public static int getHSL(int rgb) {  
        int r =(rgb>> 16)& 0xFF;
        int g =(rgb>> 8)& 0xFF;
        int b =(rgb)& 0xFF;
        float H, S, L, var_Min, var_Max, del_Max, del_R, del_G, del_B;  
        H = 0;  
        var_Min = Math.min(r, Math.min(g, b));  
        var_Max = Math.max(r, Math.max(b, g));  
        del_Max = var_Max - var_Min;  
        L = (var_Max + var_Min) / 2;  
        if (del_Max == 0) {  
            H = 0;  
            S = 0;  
  
        } else {  
            if (L < 128) {  
                S = 256 * del_Max / (var_Max + var_Min);  
            } else {  
                S = 256 * del_Max / (512 - var_Max - var_Min);  
            }  
            del_R = ((360 * (var_Max - r) / 6) + (360 * del_Max / 2))  
                    / del_Max;  
            del_G = ((360 * (var_Max - g) / 6) + (360 * del_Max / 2))  
                    / del_Max;  
            del_B = ((360 * (var_Max - b) / 6) + (360 * del_Max / 2))  
                    / del_Max;  
            if (r == var_Max) {  
                H = del_B - del_G;  
            } else if (g == var_Max) {  
                H = 120 + del_R - del_B;  
            } else if (b == var_Max) {  
                H = 240 + del_G - del_R;  
            }  
            if (H < 0) {  
                H += 360;  
            }  
            if (H >= 360) {  
                H -= 360;  
            }  
            if (L >= 256) {  
                L = 255;  
            }  
            if (S >= 256) {  
                S = 255;  
            }  
        }  
        
        int  HSL=(int)(H)<< 16;
        HSL=HSL|(int)(S)<< 8;
       // System.out.println(r+" "+g+" "+b+" "+rgb);
        //System.out.println(H+" "+S+" "+L+" "+HSL);
        return (int)(HSL);  
    }  
    
    public int getGray(int rgb){
        int r =(rgb>> 16)& 0xFF;
        int g =(rgb>> 8)& 0xFF;
        int b =(rgb)& 0xFF;
        int mode=1;
        int Vgray=0;
        //灰度算法（5种）
        switch (mode) {
            case 1://matlab中加权算法
                Vgray =(int)( 0.29900 * r + 0.58700 * g + 0.11400 * b);
                grayMod="matlab加权算法";break;
            case 2://均值算法
                Vgray=(r+g+b)/3;
                grayMod = "均值算法";break;
            case 3://整数算法
                Vgray = (r * 30 + g * 59 + b * 11) / 100;
                grayMod = "整数算法";break;
            case 4://位移算法
                Vgray=(r*28+g*151+b*77)>>8;
                grayMod = "位移算法";break;
            case 5://浮点算法
                Vgray =(int) (r * 0.3 + g * 0.59 + b * 0.11);
                grayMod = "浮点算法";break;
            case 6:
                Vgray = r*306 + g*601 + b*117 >> 10;
                grayMod = "扫码器算法";break;
        }
//        System.out.println(r+" "+g+" "+b);
        return (int)(Vgray);
    }
    //二值化
    public BufferedImage getBinary() {

        bn = new int[height][width];
        int mode=4;
        int SW=130;
        //选择阈值算法
        switch (mode) {
            case 1://OSTU大律法
                SW=GetOSTUThreshold(transArr(gra));method="OSTU大律法";break;
            case 2://ISODATA(Round算法有差异)
                SW=GetIsoDataThreshold(transArr(gra));method="ISODATA";break;
            case 3://一维最大熵
                SW = Get1DMaxEntropyThreshold(transArr(gra));method="一维最大熵";break;
            case 4://灰度平均值
                SW = GetMeanThreshold(transArr(gra));method = "灰度平均值";break;
        }
//        SW=160;
        nbi=new BufferedImage(width,height,BufferedImage.TYPE_BYTE_BINARY);
        bn = new int[height][width];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if(getAverageColor(gra, y, x, height, width)>SW){
                    nbi.setRGB(x, y, WHITE);
                    bn[y][x]=1;
                }else{
                    nbi.setRGB(x, y, BLACK);
                    bn[y][x]=0;
                }
            }
        }
        System.out.println("二值化完成:("+method+")");

        return nbi;
    }

    public int[][] getColor() {
        BufferedImage bi= null;//通过imageio将图像载入
        try {
            bi = ImageIO.read(d);
        } catch (IOException e) {
            e.printStackTrace();
        }
        height=bi.getHeight();//获取图像的高
        width=bi.getWidth();//获取图像的宽
        col=new int[height][width];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < height; x++) {
                col[y][x] = bi.getRGB(x, y);
            }
        }
        return col;
    }

    public BufferedImage getBinary2() {//直接读图入二值化图
        try {
            nbi = ImageIO.read(d);
        } catch (IOException e) {
            e.printStackTrace();
        }
        height=nbi.getHeight();//获取图像的高
        width=nbi.getWidth();//获取图像的宽
        return nbi;
    }

    public BufferedImage getBinary3() {
        try {
            nbi = ImageIO.read(d);
        } catch (IOException e) {
            e.printStackTrace();
        }
        height=nbi.getHeight();//获取图像的高
        width=nbi.getWidth();//获取图像的宽
        bn = new int[height][width];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int rgb = nbi.getRGB(x, y);
                int r =(rgb>> 16)& 0xFF;
                int g =(rgb>> 8)& 0xFF;
                int b =(rgb)& 0xFF;
                double a = 0.29900 * r + 0.58700 * g + 0.11400 * b;
                if(a>=127.5){
                    nbi.setRGB(x, y, WHITE);
                }else{
                    nbi.setRGB(x, y, BLACK);
                }
            }
        }
        return nbi;
    }

    public static int  getAverageColor(int[][] gra, int x, int y, int w, int h)
    {
        int rs = gra[x][y]
                + (x == 0 ? 255 : gra[x - 1][y])
                + (x == 0 || y == 0 ? 255 : gra[x - 1][y - 1])
                + (x == 0 || y == h - 1 ? 255 : gra[x - 1][y + 1])
                + (y == 0 ? 255 : gra[x][y - 1])
                + (y == h - 1 ? 255 : gra[x][y + 1])
                + (x == w - 1 ? 255 : gra[x + 1][ y])
                + (x == w - 1 || y == 0 ? 255 : gra[x + 1][y - 1])
                + (x == w - 1 || y == h - 1 ? 255 : gra[x + 1][y + 1]);
        return rs / 9;
    }

    //将二维图像矩阵转化为一维直方图
    public int[] transArr(int gra[][]) {
        int[] r = new int[height * width];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < height; x++) {
                r[(y*height)+x]=gra[y][x];
            }
        }
        return r;
    }
    //灰度矩阵读取
    public int[][] matrixRead(int gra[][]) {
        this.gra=gra;
        Scanner sc = null;
        try {
            sc = new Scanner(d);
            height=gra[0].length;
            width=gra.length;
            for (int y=0;y<height;y++)
                for (int x=0;x<width;x++)
                {
                    int n = sc.nextInt();
                    if(n==255||n==1)
                    gra[y][x]=1;
                    else if (n == 0) {
                        gra[y][x] = 0;
                    }
                }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return gra;
    }
    //将二值矩阵输出到文件
    public void matrixOutput() {
        String fileName = "rs1";
        File outputFile=new File(dir,fileName+".txt");
        for (int i=1;outputFile.exists();i++)//判断文件名是否重复
        {
            outputFile=new File(dir,fileName+"("+i+")"+".txt");
        }
        StringBuilder sb;
        try {
            FileWriter fw = new FileWriter(outputFile);
            for (int y = 0; y < height; y++) {
                sb = new StringBuilder();
                for (int x = 0; x < width; x++) {
                    sb.append(bn[y][x] + " ");
                }
                fw.append(sb.append("\r\n"));
                fw.flush();
            }
//            System.out.println("矩阵文本输出完成："+outputFile);
        }
       catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void matrixOutput(int[][] matrix) {
        String outFileName = "preMask.txt";
        StringBuilder sb;
        try {
            FileWriter fw = new FileWriter(new File(dir , outFileName));
            sb = new StringBuilder();
//            System.out.println(matrix.length+":"+matrix[0].length);
            for (int y = 0; y < matrix.length; y++) {
                for (int x = 0; x < matrix[0].length; x++) {
                    sb.append(matrix[y][x] + " ");
                }
                sb.append("\r\n");

            }
            fw.write(""+sb);
            fw.flush();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    //矩阵命令行输出
    public void matrixPrint()
    {
        for (int y=0;y<height;y++)
        {
            for (int x=0;x<width;x++)
            {
              System.out.print(bn[y][x]+" ");
            }
           System.out.println();
        }
    }
    public void matrixPrint(int[][] matrix)
    {
        for (int y=0;y<matrix.length;y++)
        {
            for (int x=0;x<matrix[0].length;x++)
            {
                System.out.print(gra[y][x]+" ");
            }
            System.out.println();
        }
    }
    //图像输出
    public void graPrint() {
        String fileName = "graph";
        if (dir == null) {
            dir = "";
        }
        File outputFile=new File(dir,fileName+".png");
        for (int i=1;outputFile.exists();i++)//判断文件名是否重复
        {
            outputFile=new File(dir,fileName+"("+i+")"+".png");
        }
        try {
            ImageIO.write(nbi, "png", outputFile);
            System.out.println(outputFile+"----图像输出成功");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //矩阵成像
    public BufferedImage matrixToGra() {//0白1黑
        nbi=new BufferedImage(width,height,BufferedImage.TYPE_BYTE_BINARY);
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                    nbi.setRGB(x, y, bn[y][x]==0?WHITE:BLACK);
                }
            }
        System.out.println("矩阵成像完成");
        return nbi;
    }
    //图片缩放
        public void zoom(double scale,String fileName){
            //获取缩放后的长和宽
            int _width = (int) (scale * width);
            int _height = (int) (scale * height);
            BufferedImage img = null;
            try {
                img = ImageIO.read(new File(dir,fileName));
                //获取缩放后的Image对象
                Image _img = img.getScaledInstance(_width, _height, Image.SCALE_DEFAULT);
                //新建一个和Image对象相同大小的画布
                nbi= new BufferedImage(_width, _height, BufferedImage.TYPE_INT_RGB);
                //获取画笔
                Graphics2D graphics = nbi.createGraphics();
                //将Image对象画在画布上,最后一个参数,ImageObserver:接收有关 Image 信息通知的异步更新接口,没用到直接传空
                graphics.drawImage(_img, 0, 0, null);
                //释放资源
                graphics.dispose();
//                System.out.println("图片缩放完成：（缩放比例+"+scale+"+）");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        public BufferedImage zoom(int width, int height,String fileName) {
            //与按比例缩放的不同只在于,不需要获取新的长和宽,其余相同.
            BufferedImage img = null;
            this.width = width;
            this.height = height;
            try {
                img = ImageIO.read(new File(dir,fileName));
                nbi = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
                Graphics graphics = nbi.createGraphics();
                graphics.drawImage(img, 0, 0,width, height, null);
                graphics.dispose();
//                System.out.println("图片缩放完成：（图片大小为"+width + "x" + height+")");
            } catch (IOException e) {
                e.printStackTrace();
            }
            return nbi;
        }
        
    public BufferedImage zoom(int width, int height) {
        //无输出
        if (this.width == width && this.height == height) {
            return nbi;
        }
            this.width = width;
            this.height = height;
            BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            Graphics graphics = image.createGraphics();
            graphics.drawImage(nbi, 0, 0,width, height, null);
            graphics.dispose();
            nbi =image;
//            System.out.println("图片缩放完成：（图片大小为"+width + "x" + height+")");
            return nbi;
    }

    public BufferedImage zoom2(int width, int height) {
        //无输出
    	try {
            bi = ImageIO.read(d);
        } catch (IOException e) {
            e.printStackTrace();
        }
    	
            this.width = width;
            this.height = height;
            BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
            Graphics graphics = image.createGraphics();
            graphics.drawImage(bi, 0, 0,width, height, null);
            graphics.dispose();
            bi =image;
            System.out.println("图片缩放完成：（图片大小为"+width + "x" + height+")");
            return bi;
    }
    
    public int[][] digi() {//二进制化
        bn = new int[height][width];
        for (int y=0;y<height;y++)
        {
            for (int x=0;x<width;x++)
            {
                bn[y][x] = (nbi.getRGB(x, y)==BLACK?0:1);
            }
        }
        return bn;
    }
    //自动化流程：彩色图像二值化缩放
    public int[][] GtoD(int width,int height) {
        graphRead();
        getBinary();
      //  getBinary2();
//        getBinary3();
        zoom(width, height);
//        graPrint();
        return digi();
    }

  /*  public int[][] GetHS(int width,int height) {
         zoom2(width, height);
         HSL = new int[height][width];
         for (int y = 0; y < height; y++) {
             for (int x = 0; x < height; x++) {
                 HSL[y][x] = getHSL(bi.getRGB(x, y));                
             }
         } 
         
        return HSL;
    }*/
    
    public int[][] Getgra(int width,int height) {

        zoom2(width, height);
        
        gray = new int[height][width];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < height; x++) {
            	gray[y][x] = getGray(bi.getRGB(x, y));                
            }
        } 
        
       return gray;
   }
    
    
    public BufferedImage Getgraph(int width,int height) {
           bi=zoom2(width, height);           
          return bi;
      }
    //阈值算法：
    //OSTU大律法
    public static int GetOSTUThreshold(int[] HistGram)
    {
        int X, Y, Amount = 0;
        int PixelBack = 0, PixelFore = 0, PixelIntegralBack = 0, PixelIntegralFore = 0, PixelIntegral = 0;
        double OmegaBack, OmegaFore, MicroBack, MicroFore, SigmaB, Sigma;              // 类间方差;
        int MinValue, MaxValue;
        int Threshold = 0;

        for (MinValue = 0; MinValue < 256 && HistGram[MinValue] == 0; MinValue++) ;
        for (MaxValue = 255; MaxValue > MinValue && HistGram[MinValue] == 0; MaxValue--) ;
        if (MaxValue == MinValue) return MaxValue;          // 图像中只有一个颜色
        if (MinValue + 1 == MaxValue) return MinValue;      // 图像中只有二个颜色

        for (Y = MinValue; Y <= MaxValue; Y++) Amount += HistGram[Y];        //  像素总数

        PixelIntegral = 0;
        for (Y = MinValue; Y <= MaxValue; Y++) PixelIntegral += HistGram[Y] * Y;
        SigmaB = -1;
        for (Y = MinValue; Y < MaxValue; Y++)
        {
            PixelBack = PixelBack + HistGram[Y];
            PixelFore = Amount - PixelBack;
            OmegaBack = (double)PixelBack / Amount;
            OmegaFore = (double)PixelFore / Amount;
            PixelIntegralBack += HistGram[Y] * Y;
            PixelIntegralFore = PixelIntegral - PixelIntegralBack;
            MicroBack = (double)PixelIntegralBack / PixelBack;
            MicroFore = (double)PixelIntegralFore / PixelFore;
            Sigma = OmegaBack * OmegaFore * (MicroBack - MicroFore) * (MicroBack - MicroFore);
            if (Sigma > SigmaB)
            {
                SigmaB = Sigma;
                Threshold = Y;
            }
        }
        return Threshold;
    }
    //ISODATA(也叫做intermeans法）
    public static int GetIsoDataThreshold(int[] HistGram)
    {
        int i, l, toth, totl, h, g = 0;
        for (i = 1; i < HistGram.length; i++)
        {
            if (HistGram[i] > 0)
            {
                g = i + 1;
                break;
            }
        }
        while (true)
        {
            l = 0;
            totl = 0;
            for (i = 0; i < g; i++)
            {
                totl = totl + HistGram[i];
                l = l + (HistGram[i] * i);
            }
            h = 0;
            toth = 0;
            for (i = g + 1; i < HistGram.length; i++)
            {
                toth += HistGram[i];
                h += (HistGram[i] * i);
            }
            if (totl > 0 && toth > 0)
            {
                l /= totl;
                h /= toth;
                if (g == (int)Math.round((l + h) / 2.0))
                    break;
            }
            g++;
            if (g > HistGram.length - 2)
            {
                return 0;
            }
        }
        return g;
    }
    //一维最大熵
    public static int Get1DMaxEntropyThreshold(int[] HistGram)
    {
        int  X, Y,Amount=0;
        double[] HistGramD = new double[256];
        double SumIntegral, EntropyBack, EntropyFore, MaxEntropy;
        int MinValue = 255, MaxValue = 0;
        int Threshold = 0;

        for (MinValue = 0; MinValue < 256 && HistGram[MinValue] == 0; MinValue++) ;
        for (MaxValue = 255; MaxValue > MinValue && HistGram[MinValue] == 0; MaxValue--) ;
        if (MaxValue == MinValue) return MaxValue;          // 图像中只有一个颜色
        if (MinValue + 1 == MaxValue) return MinValue;      // 图像中只有二个颜色

        for (Y = MinValue; Y <= MaxValue; Y++) Amount += HistGram[Y];        //  像素总数

        for (Y = MinValue; Y <= MaxValue; Y++)   HistGramD[Y] = (double)HistGram[Y] / Amount+1e-17;

        MaxEntropy = 0; ;
        for (Y = MinValue + 1; Y < MaxValue; Y++)
        {
            SumIntegral = 0;
            for (X = MinValue; X <= Y; X++) SumIntegral += HistGramD[X];
            EntropyBack = 0;
            for (X = MinValue; X <= Y; X++) EntropyBack += (-HistGramD[X] / SumIntegral * Math.log(HistGramD[X] / SumIntegral));
            EntropyFore = 0;
            for (X = Y + 1; X <= MaxValue; X++) EntropyFore += (-HistGramD[X] / (1 - SumIntegral) * Math.log(HistGramD[X] / (1 - SumIntegral)));
            if (MaxEntropy < EntropyBack + EntropyFore)
            {
                Threshold = Y;
                MaxEntropy = EntropyBack + EntropyFore;
            }
        }
        return Threshold;
    }
    //灰度平均值
    public static int GetMeanThreshold(int[] HistGram)
    {
        int Sum = 0, Amount = 0;
        for (int Y = 0; Y < 256; Y++)
        {
            Amount += HistGram[Y];
            Sum += Y * HistGram[Y];
        }
        return Sum / Amount;
    }
}

