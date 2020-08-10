package com.example.demo;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.PointerByReference;


/**
 * 小图片的水印添加、提取---- 上海提供
 * @author lk
 *
 */
public class VideoWatermark {


    static String get = "D:\\WaterMarkGetA" ;//提取水印、添加水印(2019-12-02)
    static String embed = "D:\\WaterMarkEmbA" ;//提取水印、添加水印(2019-12-02)


    /**
     * 添加、
     * embedStrength 2
     * @author lk
     *
     */
    public interface Get extends Library {

        Get g = Native.loadLibrary(get, Get.class);

        //嵌入
        //path：载体图像路径 ,secret：水印序列内容，例：“Cloudwalk”
        //savePath：含水印图像保存路径,alpha：嵌入强度，可以设置为5，10或20,key：密钥，可以随意设置，默认为0
        //double videoWatermark_getText_fragment(String ffmpegFilePath, String videoPath, int secretNum, String savePath, int embedStep,int height,int width);

        void videoWatermark_getText_0513(PointerByReference val, String ffmpegFilePath, String videoPath, int secretNum, String savePath, int embedStep, int height, int width);
        void clear(Pointer p);
    }

    public interface Embed extends Library
    {

        Embed e = Native.loadLibrary(embed,Embed.class);

        //嵌入
        //path：载体图像路径 ,secret：水印序列内容，例：“Cloudwalk”
        //savePath：含水印图像保存路径,alpha：嵌入强度，可以设置为5，10或20,key：密钥，可以随意设置，默认为0
//        int animal_imitator
//        (String ffmpegFilePath, String waterTextPath, String videoPath, String videoEmbedSavefileName, int animal_kind);
//        int videoWatermark_embText_with_attack
//                (String ffmpegFilePath, String waterTextPath, String videoPath, String videoEmbedSavefileName, int secretNum, int embStep, double intensity);
        int videoWatermark_embText_0513
                (String ffmpegFilePath, String waterTextPath, String videoPath, String videoEmbedSavefileName, int secretNum, int embStep, double intensity);

    }



    public static void embed(String coverPath,String water,String ffmpegFilePath,String outputPath) {
        //---------SETTINGS----------
//        MyExcelReader myExcelReader = new MyExcelReader("src/result.xlsx");
//        Object[][] cells = myExcelReader.getSheetsRowAndCol();
//        myExcelReader.writeCell(0,0,"Test");
//        myExcelReader.save("");
//        String str = "wang001";
//
//        String waterTextPath = "D:/water.txt";
//        String ffmpegFilePath = "D:/";
//        String coverPath = "D:/videos/" + str + ".mp4";
//        String videoPath = "D:/cover.mp4";
        int secretNum = 1;
//        String savePath = "D:/extracted.txt";
        int embedStep = 4;



        //----------EMBED------------
//
        System.out.println("Embed:");
        Embed.e.videoWatermark_embText_0513(
                ffmpegFilePath, water, coverPath, outputPath, secretNum, embedStep, 30);
    }
//////
    public static String extract(String extractFileName,String ffmpegFilePath) {

        final PointerByReference ptrRef = new PointerByReference();

        //--------EXTRACT--------------
            System.out.println("Extract:");

        int secretNum = 1;int embedStep = 4;

//            String extractFileName = "D:/"+str+"_cover.mp4";

           Get.g.videoWatermark_getText_0513(
                   ptrRef,ffmpegFilePath, extractFileName, secretNum,"",embedStep,-1,-1);
//
            final Pointer p = ptrRef.getValue();

            final String val = p.getString(0);
            System.out.println("-> Extracted: " + val);

    // clean up memory allocated in C
            Get.g.clear(p);

            return val;

    }



}
