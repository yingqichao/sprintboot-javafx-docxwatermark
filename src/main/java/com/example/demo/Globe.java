package com.example.demo;

import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class Globe {

    public static String chooseFile(String[] description,String[] extension) {
        try {
            Stage mainStage = null;
            FileChooser fileChooser = new FileChooser();//构建一个文件选择器实例

            fileChooser.setTitle("选择文件");
            for(int i=0;i<description.length;i++)
                fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(description[i],extension[i]));

            File selectedFile = fileChooser.showOpenDialog(mainStage);
            return selectedFile.getPath();
        }catch(Exception e){
            System.out.println("未选择文件");
            return null;
        }
    }

    public static String chooseFolder() {
        try {
            Stage fileStage = null;
            DirectoryChooser folderChooser = new DirectoryChooser();
            folderChooser.setTitle("Choose Folder");
            File selectedFile = folderChooser.showDialog(fileStage);
            return selectedFile.getPath();
        }catch(Exception e){
            System.out.println("未选择文件夹");
            return null;
        }
    }

}
