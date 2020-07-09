package com.example.demo;
import com.example.demo.PdfHelper.PdfWm;

import java.io.File;

public class PdfMain {
    public static void main(String[] args) throws Exception {
        testOnce1();
//        testOnce2();
//        testTimes();
    }

    public static void testOnce1() throws Exception {
        String PdfFile = "C:\\Users\\24962\\IdeaProjects\\PDFWM\\data\\猪流行性腹泻病毒基因及其疫苗的研究_王凤.pdf";
        String objFile = "C:\\Users\\24962\\IdeaProjects\\PDFWM\\wmData\\WM-猪流行性腹泻病毒基因及其疫苗的研究_王凤.pdf";

        PdfWm pdfps = new PdfWm();
        pdfps.embedWm(PdfFile, objFile, "Hello World!");
        String s = pdfps.extractWm(objFile);

        if(s.length() > 0)
            System.out.println("Extracted watermark : " + s);
        else
            System.out.println("There is no watermark!");
    }

    public static void testOnce2() throws Exception {
        String PdfFile = "C:\\Users\\24962\\IdeaProjects\\PDFWM\\data\\猪流行性腹泻病毒基因及其疫苗的研究_王凤.pdf";
        String objFile = "C:\\Users\\24962\\IdeaProjects\\PDFWM\\wmData\\WM-猪流行性腹泻病毒基因及其疫苗的研究_王凤.pdf";

        PdfWm pdfps = new PdfWm(PdfFile, objFile, "Hello World!");
        String s = pdfps.extractWm(objFile);

        if(s.length() > 0)
            System.out.println("Extracted watermark : " + s);
        else
            System.out.println("There is no watermark!");
    }

    public static void testTimes() throws Exception {
        String path = "C:\\Users\\24962\\IdeaProjects\\PDFWM\\data";
        String dstPath = "C:\\Users\\24962\\IdeaProjects\\PDFWM\\wmData";
        File file = new File(path);		//获取其file对象
        File[] fs = file.listFiles();	//遍历path下的文件和目录，放在File数组中
        for(File PdfFile:fs) {                    //遍历File[]数组
            if (!PdfFile.isDirectory()) {        //若非目录(即文件)，则打印
                System.out.println(PdfFile);

                String objFile = dstPath + "\\WM-" + PdfFile.getName();


                PdfWm pdfps = new PdfWm();
                pdfps.embedWm(PdfFile.toString(), objFile, "Hello World!");
                String s = pdfps.extractWm(objFile);

                if(s.length() > 0)
                    System.out.println("Extracted watermark : " + s);
                else
                    System.out.println("There is no watermark!");
            }
        }

    }
}
