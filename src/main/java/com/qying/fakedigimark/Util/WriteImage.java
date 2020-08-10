package com.qying.fakedigimark.Util;

import java.io.IOException;

public class WriteImage {
    public static void writeToFile(ByteMatrix matrix) throws IOException {
//        int inputWidth = matrix.getWidth();
//        int inputHeight = matrix.getHeight();
//        int multiple=2;
//        int qrWidth = (inputWidth + (QUIET_ZONE_SIZE << 1))* multiple;
//        int qrHeight = (inputHeight + (QUIET_ZONE_SIZE << 1))* multiple;
//        int leftPadding = (qrWidth - inputWidth* multiple) / 2;
//        int topPadding = (qrHeight - inputHeight* multiple) / 2;
//        BitMatrix output = new BitMatrix(qrWidth, qrHeight);
//
//        for (int inputY = 0, outputY = topPadding; inputY < inputHeight; inputY++, outputY += multiple){
//            // Write the contents of this row of the barcode
//            for (int inputX = 0, outputX = leftPadding; inputX < inputWidth; inputX++, outputX += multiple) {
//                if (matrix.get(inputX, inputY) == 1) {
//                    output.setRegion(outputX, outputY, multiple, multiple);
//                }
//            }
//        }
//        String format = "png";
//        File file = initInfo.getOutputFile();
//        BufferedImage image = new BufferedImage(qrWidth, qrHeight, BufferedImage.TYPE_INT_RGB);
//        //填充背景色与码块颜色
//        for (int x = 0; x < qrWidth; x++) {
//            for (int y = 0; y < qrHeight; y++) {
//                image.setRGB(x, y, (output.get(x, y) ? new Color(0,0,0).getRGB() : new Color(255,255,255).getRGB()));
//            }
//        }
//        File outputFile = new File(file, "target." + format);
//
//
//
//        if (!ImageIO.write(image, format, outputFile)) {
//            System.out.println("生成图片失败");
//            throw new IOException("Could not write an image of format " + format + " to " + outputFile);
//        } else {
//            System.out.println("图片生成成功！" + outputFile);
//        }
    }
}
