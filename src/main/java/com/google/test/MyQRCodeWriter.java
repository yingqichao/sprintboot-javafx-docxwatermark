/*
 * Copyright 2008 ZXing authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.test;

import com.google.zxing.WriterException;
import com.google.zxing.client.result.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.google.zxing.qrcode.encoder.ByteMatrix;
import com.google.zxing.qrcode.encoder.Encoder;
import com.google.zxing.qrcode.encoder.Encoder2;
import com.google.zxing.qrcode.encoder.QRCode;

import javax.imageio.ImageIO;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public final class MyQRCodeWriter {

    static final String DEFAULT_BYTE_MODE_ENCODING = "ISO-8859-1";
    private static final int QUIET_ZONE_SIZE = 2;
    private static final int BLACK = 0xFF000000;//用于设置图案的颜色
    private static final int WHITE = 0xFFFFFFFF; //用于背景色
    private static final int Red = 0xFFFC143C;
    private static final int Yellow = 0xFFFFFF00;
    private static final int Blue = 0xFF0000FF;
    private static final int Green = 0xFF00FF7F;
    private static final int c6 = 0xFFe61e2b;//可乐红
    private static final int c7 = 0xFFc7342c;//校徽色
    private static final int c8 = 0xFFc56b2d;//猴子色
    private static final int c10 = 0xFF81b5FF;//天空蓝
    private static final int c12 = 0xFF5b92e5;//联合蓝
    private static InitInfo initInfo;

    public MyQRCodeWriter() {
        this.initInfo = InitInfo.getInstance();
    }

    public static void writeToFile(BufferedImage matrix) throws IOException {
        String format = initInfo.getFormat();
        File file = initInfo.getOutputFile();
       
        int width = matrix.getWidth();
        int height = matrix.getHeight();
        int plusSize=width/15;
        int qrWidth = width + plusSize;
        int qrHeight = height + plusSize;
        BufferedImage output = new BufferedImage( qrWidth, qrHeight,BufferedImage.TYPE_3BYTE_BGR);
        for (int i=0;i<qrWidth;i++)
        	for (int j=0;j<qrHeight;j++)
        		output.setRGB(i, j, new Color(255,255,255).getRGB());
  
        for (int inputY = 0, outputY = plusSize/2; inputY < height; inputY++, outputY ++) {
            // Write the contents of this row of the barcode
            for (int inputX = 0, outputX = plusSize/2; inputX < width; inputX++, outputX ++) {
                			output.setRGB(outputX, outputY, matrix.getRGB(inputX, inputY));	
            }
        }
        File outputFile = new File(file, initInfo.getFileName() + "." + format);
        for (int i = 1; outputFile.exists(); i++)//判断文件名是否重复
        {
            outputFile = new File(file, initInfo.getFileName() + "(" + i + ")" + "." + format);
        }

        if (!ImageIO.write(output, format, outputFile)) {
            System.out.println("生成图片失败");
            throw new IOException("Could not write an image of format " + format + " to " + outputFile);
        } else {
            System.out.println("图片生成成功！" + outputFile);
        }
 }

       public static void writeToFile2(ByteMatrix matrix) throws IOException {
    	   int inputWidth = matrix.getWidth();
           int inputHeight = matrix.getHeight();
           int multiple=9;
           int qrWidth = (inputWidth + (QUIET_ZONE_SIZE << 1))* multiple;
           int qrHeight = (inputHeight + (QUIET_ZONE_SIZE << 1))* multiple;
           int leftPadding = (qrWidth - inputWidth* multiple) / 2;
           int topPadding = (qrHeight - inputHeight* multiple) / 2;
           BitMatrix output = new BitMatrix(qrWidth, qrHeight);

           for (int inputY = 0, outputY = topPadding; inputY < inputHeight; inputY++, outputY += multiple){
               // Write the contents of this row of the barcode
               for (int inputX = 0, outputX = leftPadding; inputX < inputWidth; inputX++, outputX += multiple) {
                   if (matrix.get(inputX, inputY) == 1) {
                       output.setRegion(outputX, outputY, multiple, multiple);
                   }
               }
           }
            String format = initInfo.getFormat();
            File file = initInfo.getOutputFile();
            BufferedImage image = new BufferedImage(qrWidth, qrHeight, BufferedImage.TYPE_INT_RGB);
            //填充背景色与码块颜色
            for (int x = 0; x < qrWidth; x++) {
                for (int y = 0; y < qrHeight; y++) {
                    image.setRGB(x, y, (output.get(x, y) ? new Color(0,0,0).getRGB() : new Color(255,255,255).getRGB()));
                }
            }
            File outputFile = new File(file, initInfo.getFileName() + "." + format);
            for (int i = 1; outputFile.exists(); i++)//判断文件名是否重复
            {
                outputFile = new File(file, initInfo.getFileName() + "(" + i + ")" + "." + format);
            }

            if (!ImageIO.write(image, format, outputFile)) {
                System.out.println("生成图片失败");
                throw new IOException("Could not write an image of format " + format + " to " + outputFile);
            } else {
                System.out.println("图片生成成功！" + outputFile);
            }
        }
        

    public void encode() throws WriterException {
        String contents = initInfo.getContents();
        String encoding = initInfo.getCharacterSet();
        if (contents == null || contents.length() == 0) {
            throw new IllegalArgumentException("Found empty contents");
        }
        //系统默认纠错等级，若为未设置纠错等级则为默认
        ErrorCorrectionLevel errorCorrectionLevel = ErrorCorrectionLevel.L;
        if (initInfo.getErrorCorrectionLevel() != null) {
            errorCorrectionLevel = ErrorCorrectionLevel.forLevel(initInfo.getErrorCorrectionLevel());
        }
        MyQArt myQArt = new MyQArt();
        QRCode code = new QRCode();
        if (encoding == null) {
            encoding = DEFAULT_BYTE_MODE_ENCODING;
        }
        code.setOutPutFile(initInfo.getOutputFile());

        code.setGraphUri(initInfo.getGraphUri());
        int multiple = initInfo.getMutiple();//渲染清晰度
        int best = Integer.MAX_VALUE;
        int bestPattern = 0;
        GraphDeal graphDeal = new GraphDeal(code.getGraphUri());

        //code.setEx(initInfo.getEx());
        int temprealW=initInfo.getrealWidth();
        int temprealH=initInfo.getrealHeight();

        Encoder2.encode(contents, errorCorrectionLevel, encoding, code);
        try {
            writeToFile2(code.getMatrix());
        } catch (IOException e) {
            e.printStackTrace();
        }  
        for (int t = 0; t <= 7; t++) {//八组掩码
            code.setMaskPattern(t);
            Encoder.encode(contents, errorCorrectionLevel, encoding, code);
       //	 System.out.println("code.getVersion()"+code.getVersion());
            int sizeR=(code.getVersion()-1)*4+21;
            code.setHdGraph(graphDeal.GtoD(sizeR * multiple,sizeR * multiple));//倍数高清图
            code.setgraph(graphDeal.Getgraph(sizeR * multiple, sizeR * multiple));
            int level=1;
            if(temprealW<2.5||temprealH<2.5||code.getVersion()>20)
            	level=1;
            else if(temprealW<5||temprealH<5||code.getVersion()<=20&&code.getVersion()>10)
            	level=2;
            else
            	level=3;
            myQArt.preRender(code, multiple,level);//预渲染
            int differ = myQArt.matrixComp(code.getMatrix(), code.getHdGraph());
            System.out.print("版本" + t + "差异值为：" + differ + "(" + (differ) / (float) (sizeR* sizeR * multiple * multiple) * 100 + "% " + ")");
            if (best > differ) {
                bestPattern = t;
                best = differ;
            }
        }
        code.setMaskPattern(bestPattern);//固定mask版本（0-7）

        
        Encoder.encode(contents, errorCorrectionLevel, encoding, code);
        
        System.out.println();
        System.out.println("最优版本为:" + bestPattern + " 最小差异为" + best);
        System.out.println("版本号为:" +code.getVersion());
        int level=1;
        if(temprealW<2.5||temprealH<2.5||code.getVersion()>20)
        	level=1;
        else if(temprealW<5||temprealH<5||code.getVersion()<=20&&code.getVersion()>10)
        	level=2;
        else
        	level=3;
        myQArt.colorRender(code, multiple,level);//预渲染m倍
        System.out.println("预渲染结束");
        try {
            writeToFile(code.getMatrix2());
        } catch (IOException e) {
            e.printStackTrace();
        }
//        }

    }
}
