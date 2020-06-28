package com.example.demo;

import java.util.List;

public class App 
{
    public static void main( String[] args )
    {
        String docxFileName = "D://Hello Word.docx";
        String desDir = "D://UnzipDocx";
        String outPathFile = "D://watermarked.docx";
        String water = "测试test";
        try {
            //Embed
//            List<String> newKeys = new LinkedList<>();
//            newKeys.add("00123456");
            Dom4jHelper.Embed( docxFileName, desDir,water,outPathFile);

            //Extract
            List<String> extracted = Dom4jHelper.Extract( outPathFile, desDir,null);
            System.out.println("英文提取结果： "+extracted.get(0));
            System.out.println("中文提取结果： "+extracted.get(1));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



}
