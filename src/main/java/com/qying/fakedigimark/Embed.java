package com.qying.fakedigimark;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

public class Embed {
//    static{
//        //加载opencv动态库
//        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
//    }
    public static void main(String[] args) {

        encode("res.png","123",512,512);
    }

    public static void encode(String filepath,String content,int matrixWidth,int matrixHeight){

        int QUIET_ZONE_SIZE = 8;
        Mat blank =new Mat(matrixWidth+2*QUIET_ZONE_SIZE,matrixHeight+2*QUIET_ZONE_SIZE, CvType.CV_32F);
        // Get Bitstream of watermark
        int numInputBytes = content.length();
        byte[] dataBytes = new byte[numInputBytes];
        for(int i=0;i<content.length();i++)
            dataBytes[i] = (byte)content.charAt(i);

        int[] locX = new int[]{QUIET_ZONE_SIZE,QUIET_ZONE_SIZE,matrixWidth-QUIET_ZONE_SIZE-8,matrixWidth-QUIET_ZONE_SIZE-8};
        int[] locY = new int[]{QUIET_ZONE_SIZE,matrixWidth-QUIET_ZONE_SIZE-8,QUIET_ZONE_SIZE,matrixWidth-QUIET_ZONE_SIZE-8};

        // 定位点体现为8*8小块DCT[4][5]系数明显大于DCT[5][4]
        for(int i=0;i<3;i++) {
            Mat block = ImgWatermarkUtil.getImageValue(blank, locX[i], locY[i], 8);
            //对分块进行DCT变换
            Core.dct(block, block);
            int x1 = 4, y1 = 5;
            int x2 = 5, y2 = 4;
            double dense = 5.0;
            double[] a = block.get(x1,y1);
            double[] c = block.get(x2,y2);

            block.put(x1,y1, dense);
            block.put(x2,y2, 0);

            //对上面分块进行IDCT变换
            Core.idct(block, block);
            for(int m=0;m<8;m++) {
                for(int t=0;t<8;t++) {
                    double[] e = block.get(m, t);
                    blank.put(locX[i] + m,locY[i] + t, e);
                }
            }

        }

        Imgcodecs.imwrite(filepath, blank);

//        //总大小256*256，每个格子16*16
//        int batchWidth = 32;int batchNum = matrixWidth/batchWidth;
//        Random r=new Random(10);//r.nextInt()
//        Set<Integer> used = new HashSet<>();
////        BitArray finalBits = new BitArray();
////        interleaveWithECBytes(dataBits, qrCode.getNumTotalBytes(), qrCode.getNumDataBytes(),
////                qrCode.getNumRSBlocks(), finalBits);
//        byte[] ecBytes = RSUtil.generateECBytes(dataBytes, numInputBytes/2);
//
//        qrCode.setRsCode(finalBits);
//
//        // Step 7: Choose the mask pattern and set to "qrCode".系统默认为根据内容择优选择掩码
//        StringBuilder str = new StringBuilder();
//        ByteMatrix matrix = new ByteMatrix(matrixWidth, matrixWidth);
//        int pdpWidth = Settings.pdpWidth;
//        System.out.println("matrixWidth: "+matrixWidth);
//        System.out.println("pdpWidth: "+pdpWidth);
//        MatrixUtil.embedPositionDetectionPatternsAndSeparators(matrix); // Separactor已经取消
//        outer:for(int i=0;i<dataBytes.length+ecBytes.length;i++){
//            byte b = (i<dataBytes.length)?dataBytes[i]:ecBytes[i-dataBytes.length];
//            for(int j=0;j<8;j++){
//                int index = Math.abs(r.nextInt())%(batchNum*batchNum);
//                int k = (b>>j)%2;//从低位开始
//                str.append((char)('0'+k));
////                if(k==1) {
//                int row = index / batchNum;
//                int col = index % batchNum;
//                if ((row==0 || row==batchNum-1) || (col==0 || col==batchNum-1)) {
//                    System.out.println("检测区域不可用，Net randInt");
//                    j--;
//                }else if(used.contains(row*batchNum+col)){
//                    System.out.println("这个区域已经用过，Net randInt");
//                    j--;
//                }else {
//                    //在选定的batch中嵌入2~4个点，规定提取出至少5个才能认为正确,是1
//                    if(k==1) {
//                        int randNum = 2 + (int) (Math.random() * 2);
//                        for (int z = 0; z < randNum; z++) {
//                            int rowEmb = row * batchWidth + batchWidth / 4 + (int) (Math.random() * batchWidth / 2);
//                            int colEmb = col * batchWidth + batchWidth / 4 + (int) (Math.random() * batchWidth / 2);
//                            matrix.set(rowEmb, colEmb, 1);
//                        }
//                        System.out.println("Embedded in: " + row + " " + col+" Detected Num:"+randNum);
//                    }else{
//                        System.out.println("Embedded in: " + row + " " + col+" Detected Num:"+0);
//                    }
//                    used.add(row * batchNum + col);
//                    if(used.size()==(batchNum-1)*(batchNum-1))
//                        throw new Exception("嵌入失败，容量超出");
//
//
//                }
//
////                }
//            }
//
//
//        }
//
//        // MatrixUtil.buildMatrix(finalBits, qrCode.getMaskPattern(), matrix,qrCode);
//
//
//        qrCode.setMatrix(matrix);
//        StringBuilder debug = new StringBuilder();
//        for(int i=0;i<matrixWidth;i++){
//            for(int j=0;j<matrixWidth;j++){
//                debug.append(matrix.get(i,j)==1?".":" ");
//
//            }
//            System.out.println(debug);
//            debug = new StringBuilder();
//
//        }
//
//        System.out.println("Bitflow: "+str.toString());

        // Step 9.  Make sure we have a valid QR Code.确保我们有一个有效的二维码。

    }
}
