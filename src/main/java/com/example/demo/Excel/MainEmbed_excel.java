package com.example.demo.Excel;

import com.example.demo.Excel.Setting.Settings;
import com.example.demo.Excel.Utils.Util;

import java.io.File;
import java.io.FileOutputStream;

/**
 * @author Qichao Ying
 * @date 2019/8/7 15:37
 * @Description DEFAULT
 */
public class MainEmbed_excel {
    public static boolean Embed(String filePath,int[] args,String wmStr,String outPath){
        //input:foldname 载体文件夹 filename 载体名称 append 载体后缀名 waterPath 水印文件txt位置 args 不允许做嵌入的列indices
        //return: 是否嵌入成功
        if(wmStr==null || wmStr.length()==0)
            wmStr = Util.readWatermark("src//watermark.txt");
        String binarySeq = Util.StreamFromString(wmStr);
        System.out.println("Bit Num: "+binarySeq.length());

        System.out.println("embedded ExcelWatermarkHelper : " + wmStr);

        //String append = filePath.substring(filePath.indexOf(".")+1);
        //String filePath = "src//resources//"+foldname+"//"+filename+append;
        System.out.println("\n================= Embed from file " + "\"" + filePath + "\" =================");
        ExcelEncoder embed = new ExcelEncoder(binarySeq,filePath,0.05, Settings.SHORT);
        try {
//            FileOutputStream out = new FileOutputStream("src//embedded_results//" + filename + "_embedded" + append);
            embed.run(filePath,outPath,args);
            return true;
        }
        catch(Exception e){
            e.printStackTrace();
            return false;
        }

    }

    public static void main(String s[]){
//        String MODE = "EXTRACT";

        int[] args = new int[]{};
        String foldname = "EXCEL";
        String filename = "3";
        String append = ".xlsx";
        String waterPath = "src//watermark.txt";
        String outPath = "src//embedded_results//" + filename + "_embedded" + append;
        //Embed(foldname,filename,append,waterPath,args,null,outPath);

    }


}

