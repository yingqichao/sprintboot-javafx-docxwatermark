package com.google.test;

import com.google.zxing.WriterException;
import com.google.zxing.client.result.common.BitArray;
import com.google.zxing.qrcode.encoder.ByteMatrix;
import com.google.zxing.qrcode.encoder.MaskUtil;
import com.google.zxing.qrcode.encoder.QRCode;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.HashSet;
import javax.imageio.ImageIO;

public class MyQArt {//艺术二维码工具类
    private InitInfo initInfo;

    public MyQArt() {
        this.initInfo = InitInfo.getInstance();
    }

    	  // From Appendix E. Table 1, JIS0510X:2004 (p 71). The table was double-checked by komatsu.
private static final int[][] POSITION_ADJUSTMENT_PATTERN_COORDINATE_TABLE = {
    	      {-1, -1, -1, -1,  -1,  -1,  -1},  // Version 1
    	      { 6, 18, -1, -1,  -1,  -1,  -1},  // Version 2
    	      { 6, 22, -1, -1,  -1,  -1,  -1},  // Version 3
    	      { 6, 26, -1, -1,  -1,  -1,  -1},  // Version 4
    	      { 6, 30, -1, -1,  -1,  -1,  -1},  // Version 5
    	      { 6, 34, -1, -1,  -1,  -1,  -1},  // Version 6
    	      { 6, 22, 38, -1,  -1,  -1,  -1},  // Version 7
    	      { 6, 24, 42, -1,  -1,  -1,  -1},  // Version 8
    	      { 6, 26, 46, -1,  -1,  -1,  -1},  // Version 9
    	      { 6, 28, 50, -1,  -1,  -1,  -1},  // Version 10
    	      { 6, 30, 54, -1,  -1,  -1,  -1},  // Version 11
    	      { 6, 32, 58, -1,  -1,  -1,  -1},  // Version 12
    	      { 6, 34, 62, -1,  -1,  -1,  -1},  // Version 13
    	      { 6, 26, 46, 66,  -1,  -1,  -1},  // Version 14
    	      { 6, 26, 48, 70,  -1,  -1,  -1},  // Version 15
    	      { 6, 26, 50, 74,  -1,  -1,  -1},  // Version 16
    	      { 6, 30, 54, 78,  -1,  -1,  -1},  // Version 17
    	      { 6, 30, 56, 82,  -1,  -1,  -1},  // Version 18
    	      { 6, 30, 58, 86,  -1,  -1,  -1},  // Version 19
    	      { 6, 34, 62, 90,  -1,  -1,  -1},  // Version 20
    	      { 6, 28, 50, 72,  94,  -1,  -1},  // Version 21
    	      { 6, 26, 50, 74,  98,  -1,  -1},  // Version 22
    	      { 6, 30, 54, 78, 102,  -1,  -1},  // Version 23
    	      { 6, 28, 54, 80, 106,  -1,  -1},  // Version 24
    	      { 6, 32, 58, 84, 110,  -1,  -1},  // Version 25
    	      { 6, 30, 58, 86, 114,  -1,  -1},  // Version 26
    	      { 6, 34, 62, 90, 118,  -1,  -1},  // Version 27
    	      { 6, 26, 50, 74,  98, 122,  -1},  // Version 28
    	      { 6, 30, 54, 78, 102, 126,  -1},  // Version 29
    	      { 6, 26, 52, 78, 104, 130,  -1},  // Version 30
    	      { 6, 30, 56, 82, 108, 134,  -1},  // Version 31
    	      { 6, 34, 60, 86, 112, 138,  -1},  // Version 32
    	      { 6, 30, 58, 86, 114, 142,  -1},  // Version 33
    	      { 6, 34, 62, 90, 118, 146,  -1},  // Version 34
    	      { 6, 30, 54, 78, 102, 126, 150},  // Version 35
    	      { 6, 24, 50, 76, 102, 128, 154},  // Version 36
    	      { 6, 28, 54, 80, 106, 132, 158},  // Version 37
    	      { 6, 32, 58, 84, 110, 136, 162},  // Version 38
    	      { 6, 26, 54, 82, 110, 138, 166},  // Version 39
    	      { 6, 30, 58, 86, 114, 142, 170},  // Version 40
    	  };

    double weight[][];//权重矩阵
    //获取预掩码后的填充比特并还原块

    private static boolean isEmpty(int value) {
        return value == -1;
    }

    public void preMaskGraph(int[][] matirx, QRCode qrCode) {//图片预掩码
    //	 System.out.println("qrCode.getVersion()"+qrCode.getVersion());
        int width = (qrCode.getVersion()-1)*4+21;
        int height = (qrCode.getVersion()-1)*4+21;
        GraphDeal graphDeal = new GraphDeal(qrCode.getGraphUri());
        qrCode.setRsGraph(graphDeal.GtoD(width,height));
        int[][] temp = qrCode.getRsGraph();
        int maskPattern = qrCode.getMaskPattern();
        for (int y = 0; y < height; y++)
            for (int x = 0; x < width; x++) {
                matirx[y][x] = (temp[y][x] == 1 ? 0 : 1);
                if (maskPattern != -1) {//掩码异或处理
                    if (MaskUtil.getDataMaskBit(maskPattern, x, y)) {
                        matirx[y][x] = (matirx[y][x] == 1 ? 0 : 1);
                    }
                }
            }
//        System.out.println("预掩码完成");
    }

    
    public void preRender(QRCode qrCode, int multiple,int level) {//预渲染，提高图片质量
        if (multiple == 1) {
            return;
        }
        ByteMatrix input = qrCode.getMatrix();
        int inWidth = input.getWidth();
        int inHeight = input.getHeight();
        int outWidth = inWidth * multiple;
        int outHeight = inHeight * multiple;
        ByteMatrix output = new ByteMatrix(outWidth, outHeight);
        int[][] gra = qrCode.getHdGraph();


        //step1.全体放大mutiple倍
        for (int y = 0; y < inHeight; y++) {
            for (int x = 0; x < inWidth; x++) {
                for (int yy = multiple * (y + 1) - (multiple / 2 + 1) - multiple / 2, m = 0; m < multiple; m++, yy++) {
                    for (int xx = multiple * (x + 1) - (multiple / 2 + 1) - multiple / 2, mm = 0; mm < multiple; mm++, xx++) {
                        output.set(xx, yy, input.get(x, y));
                    }
                }

            }
        }
        
        int tempsize1 = 0,tempsize2=0;
        switch(level) {
         case 1:
        	tempsize1=((multiple +1)/2+1)/2;
            tempsize2=((multiple +1)/2+1)/2;
             break;
         case 2:
        	 tempsize1=(multiple +1)/2/2;
        	 tempsize2=(multiple +1)/2/2;
        	 break;
         case 3:
        	 tempsize1=multiple/2/3;
             tempsize2=multiple/2/3;       
         }
        
        int index = qrCode.getVersion() - 1;
        int[] coordinates = POSITION_ADJUSTMENT_PATTERN_COORDINATE_TABLE[index];
        int numCoordinates = POSITION_ADJUSTMENT_PATTERN_COORDINATE_TABLE[index].length;
       
            
        int[][] cent = initInfo.getCent();
        for (int y = 0; y < inHeight; y++) {
            for (int x = 0; x < inWidth; x++) {
            	int flag=0;
                if (x < 7 && y < 7)//左上角定位符
                    continue;
                if (x < 7 && y >= inHeight - 7)//左下角定位符
                    continue;
                if (x >= inWidth - 7 && y < 7)//右上角定位符
                    continue;
                for (int ii = 0; ii < numCoordinates; ++ii) {
                    for (int jj = 0; jj < numCoordinates; ++jj) {
                      int y2 = coordinates[ii];
                      int x2 = coordinates[jj];
                      if (x2 == -1 || y2 == -1) {
                        continue;
                      }
                      if(x2-2<7&&y2-2<7||x2+2>=inHeight-7&&y2-2<7||y2-2<7&&x2+2>=inWidth-7||y2-2<7&&x2-2<7||y2+2>=inHeight-7&&x2-2<7||x2-2<7&&y2+2>=inWidth-7)
                    	  continue;
                      if (x > x2-3 && x <= x2+2 && y <= y2+2 && y > y2-3)//右下角小定位符
                      {
                    	  flag=1;
                    	  break;
                      }
                    }
                    if(flag==1)
                    	break;
                }
                if(flag==1)
                	continue;
                int cx = -1;
                int cy = 0;
                for (int yy = multiple * (y + 1) - (multiple / 2 + 1) - multiple / 2, m = 0; m < multiple; m++, yy++) {
                    for (int xx = multiple * (x + 1) - (multiple / 2 + 1) - multiple / 2, mm = 0; mm < multiple; mm++, xx++) {
                        if ((xx >= multiple * x + multiple / 2+1 - tempsize1) && (xx <= multiple * x + multiple / 2+1 + tempsize2)
                                && (yy >= multiple * y + multiple / 2+1 - tempsize1) && (yy <= multiple * y + multiple / 2+1 + tempsize2)) {

                            if (cx < cent.length - 1)
                                cx++;
                            else if (cy < cent.length - 1) {
                                cy++;
                                cx = 0;
                            }
                            if (cent[cy][cx] == 1 && multiple > 3)
                                continue;
                            else if (multiple <= 3)
                                continue;
                        }
                        output.set(xx, yy, gra[yy][xx] == 1 ? 0 : 1);

                    }
                }
            }
        }
        qrCode.setMatrix(output);
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

            case 2://均值算法
                Vgray=(r+g+b)/3;

            case 3://整数算法
                Vgray = (r * 30 + g * 59 + b * 11) / 100;

            case 4://位移算法
                Vgray=(r*28+g*151+b*77)>>8;
     
            case 5://浮点算法
                Vgray =(int) (r * 0.3 + g * 0.59 + b * 0.11);

            case 6:
                Vgray = r*306 + g*601 + b*117 >> 10;
        }
//        System.out.println(r+" "+g+" "+b);
        return (int)(Vgray);
    }
    
    
    public int HSL2RGB(int hsl){
            float H = (hsl>> 16)& 0xFF;  
            float S = (hsl>> 8)& 0xFF; 
            float L = (hsl)& 0xFF;   
      
            float R, G, B, var_1, var_2;  
            if (S == 0) {  
                R = L;  
                G = L;  
                B = L;  
            } else {  
                if (L < 128) {  
                    var_2 = (L * (256 + S)) / 256;  
                } else {  
                    var_2 = (L + S) - (S * L) / 256;  
                }  
      
                if (var_2 > 255) {  
                    var_2 = Math.round(var_2);  
                }  
      
                if (var_2 > 254) {  
                    var_2 = 255;  
                }  
      
                var_1 = 2 * L - var_2;  
                R = RGBFromHue(var_1, var_2, H + 120);  
                G = RGBFromHue(var_1, var_2, H);  
                B = RGBFromHue(var_1, var_2, H - 120);  
            }  
            R = R < 0 ? 0 : R;  
            R = R > 255 ? 255 : R;  
            G = G < 0 ? 0 : G;  
            G = G > 255 ? 255 : G;  
            B = B < 0 ? 0 : B;  
            B = B > 255 ? 255 : B;  
            int rgb=(int)(R)<< 16;
            rgb=rgb|(int)(G)<< 8;
            rgb=rgb|(int)(B);
            return rgb;
        }  
      
        public static float RGBFromHue(float a, float b, float h) {  
            if (h < 0) {  
                h += 360;  
            }  
            if (h >= 360) {  
                h -= 360;  
            }  
            if (h < 60) {  
                return a + ((b - a) * h) / 60;  
            }  
            if (h < 180) {  
                return b;  
            }  
      
            if (h < 240) {  
                return a + ((b - a) * (240 - h)) / 60;  
            }  
            return a;  
        }  
   
public double G(int i,int j,double a1) {
	double ans=0;
	ans=1/(2*Math.PI*a1*a1)*Math.pow(Math.E,-(i*i+j*j)/(2*a1*a1));
//	 System.out.println(ans);
    return ans;
}

   public void GetW(int tempsize) {
	   double a1;
	   if(tempsize==3) 
		   a1 = 0.572;
	   else
	   a1=((double)tempsize-1)/6;
	 //  System.out.println(tempsize);
	   int center=(int)(tempsize/2+1);
	 //  System.out.println(center);
	   weight=new double[tempsize][tempsize];
	   for(int i=1;i<=tempsize;i++)
		   for(int j=1;j<=tempsize;j++)
		   {  
			   weight[i-1][j-1]=G(i-center,j-center,a1);
	        //  System.out.println(weight[i-1][j-1]+" "+i+" "+j);
	   }
   }
    
    public void colorRender(QRCode qrCode, int multiple,int level) {//预渲染，提高图片质量
    	 if (multiple == 1) {
             return;
         }
         ByteMatrix input = qrCode.getMatrix();
         int inWidth = input.getWidth();
         int inHeight = input.getHeight();
         int outWidth = inWidth * multiple;
         int outHeight = inHeight * multiple;
         BufferedImage output= new BufferedImage(outWidth, outHeight,BufferedImage.TYPE_3BYTE_BGR);
         BufferedImage graph= qrCode.getgraph();
         if (multiple == 1) {
             return;
         }

         //step1.全体放大mutiple倍
         for (int y = 0; y < inHeight; y++) {
             for (int x = 0; x < inWidth; x++) {
                 for (int yy = multiple * (y + 1) - (multiple / 2 + 1) - multiple / 2, m = 0; m < multiple; m++, yy++) {
                     for (int xx = multiple * (x + 1) - (multiple / 2 + 1) - multiple / 2, mm = 0; mm < multiple; mm++, xx++) {
                         output.setRGB(xx, yy, new Color((1-input.get(x, y))*255,(1-input.get(x, y))*255,(1-input.get(x, y))*255).getRGB());
                      //   System.out.println(input.get(x, y)*255);
                     }
                 }

             }
         }
        int tempsize1 = 0,tempsize2=0;
        switch(level) {
         case 1:
        	tempsize1=((multiple +1)/2+1)/2;
            tempsize2=((multiple +1)/2+1)/2;
             break;
         case 2:
        	 tempsize1=(multiple +1)/2/2;
        	 tempsize2=(multiple +1)/2/2;
        	 break;
         case 3:
        	 tempsize1=multiple/2/3;
             tempsize2=multiple/2/3;       
         }
        GetW(tempsize1+tempsize2+1);
        
        int index = qrCode.getVersion() - 1;
        int[] coordinates = POSITION_ADJUSTMENT_PATTERN_COORDINATE_TABLE[index];
        int numCoordinates = POSITION_ADJUSTMENT_PATTERN_COORDINATE_TABLE[index].length;
       
            
        for (int y = 0; y < inHeight; y++) {
            for (int x = 0; x < inWidth; x++) {
            	int flag=0;
                if (x < 7 && y < 7)//左上角定位符
                    continue;
                if (x < 7 && y >= inHeight - 7)//左下角定位符
                    continue;
                if (x >= inWidth - 7 && y < 7)//右上角定位符
                    continue;
                for (int ii = 0; ii < numCoordinates; ++ii) {
                    for (int jj = 0; jj < numCoordinates; ++jj) {
                      int y2 = coordinates[ii];
                      int x2 = coordinates[jj];
                      if (x2 == -1 || y2 == -1) {
                        continue;
                      }
                      if(x2-2<7&&y2-2<7||x2+2>=inHeight-7&&y2-2<7||y2-2<7&&x2+2>=inWidth-7||y2-2<7&&x2-2<7||y2+2>=inHeight-7&&x2-2<7||x2-2<7&&y2+2>=inWidth-7)
                    	  continue;
                      if (x > x2-3 && x <= x2+2 && y <= y2+2 && y > y2-3)//右下角小定位符
                      {
                    	  flag=1;
                    	  break;
                      }
                    }
                    if(flag==1)
                    	break;
                }
                if(flag==1)
                	continue;
                
                 int cx = 0;
                 int cy = 0;
                 
                 for (int yy = multiple * (y + 1) - (multiple / 2 + 1) - multiple / 2, m = 0; m < multiple; m++, yy++) {
                     for (int xx = multiple * (x + 1) - (multiple / 2 + 1) - multiple / 2, mm = 0; mm < multiple; mm++, xx++) {
                         if ((xx >= multiple * x + multiple / 2 +1- tempsize1) && (xx <= multiple * x + multiple / 2+1 + tempsize2)
                                 && (yy >= multiple * y + multiple / 2+1 - tempsize1) && (yy <= multiple * y + multiple / 2 +1+ tempsize2)) {
                        	 int R=(graph.getRGB(xx,yy)>> 16)& 0xFF; 
                             int G=(graph.getRGB(xx,yy)>> 8)& 0xFF; 
                             int B=(graph.getRGB(xx,yy))& 0xFF; 
                             int R2=(output.getRGB(xx,yy)>> 16)& 0xFF; 
                             int G2=(output.getRGB(xx,yy)>> 8)& 0xFF; 
                             int B2=(output.getRGB(xx,yy))& 0xFF; 
                             double temp=weight[cx][cy]+0.6;
                             int Rnew=(int)(R2*temp+R*(1-temp));
                             int Gnew=(int)(G2*temp+G*(1-temp));
                             int Bnew=(int)(B2*temp+B*(1-temp));
                             if(Rnew>255)
                            	 Rnew=255;
                             if(Gnew>255)
                            	 Gnew=255;
                             if(Bnew>255)
                            	 Bnew=255;
                             if(Rnew<0)
                            	 Rnew=0;
                             if(Gnew<0)
                            	 Gnew=0;
                             if(Bnew<0)
                            	 Bnew=0;
                          //   System.out.println(temp+" "+cx+" "+cy+" "+mm+" "+m);
                             cy=cy+1;
                             if(cy==tempsize1+tempsize2+1) {
                            	 cy=0;
                            	 cx=cx+1;
                             }
                             if(cx==tempsize1+tempsize2+1)
                            	 cx=0;
                             output.setRGB(xx, yy, new Color(Rnew,Gnew,Bnew).getRGB());
                        	 
                         }
                         else {
                        	 output.setRGB(xx, yy, graph.getRGB(xx,yy));
                         }
       
                         
                     }
                 }
             }
         }
         qrCode.setMatrix2(output);    	
    }
    
    public ByteMatrix createComp(ByteMatrix matrix, QRCode qrCode, int multiple)
            throws WriterException {//创建对比图
        int Width = matrix.getWidth();
        int Height = matrix.getHeight();
        ByteMatrix compMatrix = new ByteMatrix(Width, Height);
        for (int y = 0; y < Height; y++) {
            for (int x = 0; x < Width; x++) {
                compMatrix.set(x, y, matrix.get(x, y));
            }
        }
        int gra[][] = qrCode.getHdGraph();
        HashSet<XyDir> padding = qrCode.getXyData();
        for (XyDir xyDir : padding) {
            int x = xyDir.getX();
            int y = xyDir.getY();
            for (int yy = multiple * (y + 1) - (multiple / 2 + 1) - multiple / 2, m = 0; m < multiple; m++, yy++) {
                for (int xx = multiple * (x + 1) - (multiple / 2 + 1) - multiple / 2, mm = 0; mm < multiple; mm++, xx++) {
                    compMatrix.set(xx, yy, gra[yy][xx] == 1 ? 0 : 1);
                }
            }
        }
        return compMatrix;
    }

    public int matrixComp(ByteMatrix matrix, ByteMatrix compMatrix) throws WriterException {
        //计算两矩阵差异
        int width = compMatrix.getWidth();
        int height = compMatrix.getHeight();
        int differ = 0;
        if ((matrix.getWidth() != width) && (matrix.getHeight() != height)) {
            throw new WriterException("两矩阵大小不一致");
        }
//        int c=height/2;//划定显著区域
//        int b=123;
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
//                if ((x>c-b&&x<c+b)&&(y>c-b&&y<c+b))
//                if(y>=207&&x<=99&&x>=72)
                if (1 == (matrix.get(x, y) ^ compMatrix.get(x, y))) {
                    differ++;
                }
            }
        }
        return differ;
    }

    public int matrixComp(ByteMatrix matrix, int[][] compMatrix) throws WriterException {
        //计算两矩阵差异
        int width = compMatrix[0].length;
        int height = compMatrix.length;
        int differ = 0;
        if ((matrix.getWidth() != width) && (matrix.getHeight() != height)) {
            throw new WriterException("两矩阵大小不一致");
        }
        int[] salient = initInfo.getSalient();
        if (salient[0] + salient[1] + salient[2] + salient[3] == 0) {
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    if (0 == (matrix.get(x, y) ^ compMatrix[y][x])) {
                        differ++;
                    }
                }
            }
        } else {
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    if (x >= salient[0] && x <= salient[0] + salient[2] && y >= salient[1] && y <= salient[1] + salient[3])
                        if (0 == (matrix.get(x, y) ^ compMatrix[y][x])) {
                            differ++;
                        }
                }
            }
        }
        return differ;
    }

}