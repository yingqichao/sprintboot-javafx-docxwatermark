package com.example.demo.PdfHelper;

import com.example.demo.Utils.PdfUtil;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;

public class PdfParsing {

    private ArrayList<String> Header = new ArrayList<>();
    public ArrayList<String> Body = new ArrayList<>();
    private ArrayList<String> XrefList = new ArrayList<>();
    private ArrayList<String> Trailer = new ArrayList<>();
    private ArrayList<String> NotIndentify = new ArrayList<>();
    private ArrayList<Integer> EleOrder = new ArrayList<>();

    FileReader fr;
    BufferedReader bf;

    public PdfParsing(String strFile) throws IOException {
        fr = new FileReader(strFile);
        bf = new BufferedReader(fr);

        String str;

        // 按行读取字符串
        // -1 其他
        // 0 Header
        // 1 Body
        // 2 Xref
        // 3 Trailer
        int flag = 0;

        while((str = bf.readLine()) != null){
            String[] strs = str.split(" ");
            if(strs.length >= 3 && PdfUtil.isPureDigital(strs[0]) && PdfUtil.isPureDigital(strs[1]) && strs[2].equals("obj"))
                flag = 1;

            if(str.equals("xref"))
                flag = 2;

            if(str.equals("trailer"))
                flag = 3;

            EleOrder.add(flag);
            switch(flag){
                case 0: // Header
                    Header.add(str);
                    break;
                case 1: // Body
                    String body = str;
                    while((str = bf.readLine()) != null){
                        body = body + "\r\n" + str;
                        if(str.equals("endobj")){
                            Body.add(body);
                            flag = -1;
                            break;
                        }
                    }
                    break;
                case 2: // Xref
                    String xref = str;
                    while((str = bf.readLine()) != null){
                        if(str.contains("trailer")) {
                            XrefList.add(xref);
                            flag = 3;
                            break;
                        }
                        xref = xref + "\r\n" + str;
                    }
                    break;
                case 3: // Trailer
                    String trailer = str;

                    if(!str.equals("trailer"))
                        trailer = "trailer" + trailer;

                    while((str = bf.readLine()) != null) {
                        trailer = trailer + "\r\n" + str;
                        if(str.equals("%%EOF")){
                            Trailer.add(trailer);
                            flag = -1;
                            break;
                        }
                    }
                    break;

                default:
                    NotIndentify.add(str);
                    // System.out.println("未知表达 : <" + str + ">");
            }

        }
        // System.out.println("解析完成！");
    }

    /**
     *自由确定起始页和终止页
     * @param fileAddress 文件地址
     * @param filename pdf文件名
     * @param indexOfStart 开始页  开始转换的页码，从0开始
     * @param indexOfEnd 结束页  停止转换的页码，-1为全部
     * @param type 图片类型
     */
    public static String pdf2png(String fileAddress,String filename,int indexOfStart,int indexOfEnd,String type) {
        // 将pdf装图片 并且自定义图片得格式大小
        File file = new File(fileAddress+"\\"+filename+".pdf");
        try {
            PDDocument doc = PDDocument.load(file);
            PDFRenderer renderer = new PDFRenderer(doc);
            int pageCount = doc.getNumberOfPages();
//            for (int i = indexOfStart; i < indexOfEnd; i++) {
            BufferedImage image = renderer.renderImageWithDPI(indexOfStart, 144); // Windows native DPI
            // BufferedImage srcImage = resize(image, 240, 240);//产生缩略图

            ImageIO.write(image, type, new File(fileAddress+"\\"+filename+"."+type));
//            }
            return fileAddress+"\\"+filename+"."+type;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public int getMaxEleNo() {
        int maxEleNo = -1;

        for(String s : Body){
            String strEleNo = s.substring(0, s.indexOf("obj"));
            String[] strs = strEleNo.split(" ");
            int eleNo = Integer.valueOf(strs[0]);
            if(maxEleNo < eleNo)
                maxEleNo = eleNo;
        }

        return maxEleNo;
    }

    public void PdfClose() throws IOException {
        bf.close();
        fr.close();
    }

}
