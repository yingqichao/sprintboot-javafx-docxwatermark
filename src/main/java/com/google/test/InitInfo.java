package com.google.test;

import java.io.File;

public class InitInfo {
  //  private int width = 430; // 二维码图片宽度 430
 //   private int height = 430; // 二维码图片高度430
    private int realHeight =5;
    private int realWidth =5;
    private File outputFile = new File("F:\\二维码");
    private String fileName = "1";
    private File graphUri = new File("F:\\Paper\\submitVersion\\png", "25.png");
    private String format = "png";
    private int mutiple = 9;//1,3,9
    private String contends = "http://gra.hnu.edu.cn/";
    private String ex = "你好";
    private String errorCorrectionLevel = "L";
    private int margin = 1;
    private String characterSet = "utf-8";
    
  /*  private int color1 = 0xFF000000;
    private int color2 = 0xFFFFFFFF;*/
    /*
     * 010 110 111 101 110 100 101 011 101
     * 111 111 101 111 111 111 101 101 111
     * 111 011 111 111 111 111 111 111 011
     * */
    private int[][] cent = {
            {1, 1,1,1,1,1,1,1,1},
            {1, 1,1,1,1,1,1,1,1},
            {1, 1,1,1,1,1,1,1,1},
            {1, 1,1,1,1,1,1,1,1},
            {1, 1,1,1,1,1,1,1,1},
            {1, 1,1,1,1,1,1,1,1},
            {1, 1,1,1,1,1,1,1,1},
            {1, 1,1,1,1,1,1,1,1},
            {1, 1,1,1,1,1,1,1,1}
    };
    private int[] salient={
            0,0,   //x,y
            0,0    //width,height
    };

    private static InitInfo instance;

    public static InitInfo getInstance() {
        if (instance == null) {
            instance = new InitInfo();
        }
        return instance;
    }

    public String getEx() {
        return ex;
    }

    public void setEx(String ex) {
        this.ex = ex;
    }

  /*  public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }*/

    public File getOutputFile() {
        return outputFile;
    }

    public void setOutputFile(File outputFile) {
        this.outputFile = outputFile;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public File getGraphUri() {
        return graphUri;
    }

    public void setGraphUri(File graphUri) {
        this.graphUri = graphUri;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public int getMutiple() {
        return mutiple;
    }

    public void setMutiple(int mutiple) {
        this.mutiple = mutiple;
    }

    public String getContents() {
        return contends;
    }

    public void setContents(String contends) {
        this.contends = contends;
    }

    public String getErrorCorrectionLevel() {
        return errorCorrectionLevel;
    }

    public void setErrorCorrectionLevel(String errorCorrectionLevel) {
        this.errorCorrectionLevel = errorCorrectionLevel;
    }

    public int getMargin() {
        return margin;
    }

    public void setMargin(int margin) {
        this.margin = margin;
    }

    public int getrealWidth() {
        return realWidth;
    }

    public void setrealWidth(int realWidth) {
        this.realWidth = realWidth;
    }

    public int getrealHeight() {
        return realHeight;
    }

    public void setrealHeight(int realHeight) {
        this.realHeight = realHeight;
    }

    public String getCharacterSet() {
        return characterSet;
    }

    public void setCharacterSet(String characterSet) {
        this.characterSet = characterSet;
    }

    public int[][] getCent() {
        return cent;
    }

    public void setCent(int[][] cent) {
        this.cent = cent;
    }

    public int[] getSalient() {
        return salient;
    }

    public void setSalient(int[] salient) {
        this.salient = salient;
    }
}
