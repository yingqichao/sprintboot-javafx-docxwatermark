package com.example.demo.Excel;

import com.example.demo.Excel.Utils.Util;
import org.apache.poi.POIXMLProperties;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import static com.example.demo.Excel.Setting.Settings.LONG;
import static com.example.demo.Excel.Setting.Settings.SHORT;

/**
 * @author Qichao Ying
 * @date 2019/8/7 15:50
 * @Description DEFAULT
 */
public class MainExtract_excel {
    public static List<String> Extract(String filePath,int startRow,int[] args) throws Exception{

        ExcelDecoder extract = new ExcelDecoder(new File(filePath),0.05,SHORT);
        System.out.println("\n================= Extract from file " + "\"" + filePath + "\" =================");
        try {
            List<String> res = extract.run(filePath,args);

//            List<String> list = extract.getEnglishResult();
//            List<String> chinese_list = extract.getChineseResult();


            return res;

//            System.out.println("-----------提取得到的信息是------------");
//            //打印提取结果
//            System.out.println("The ExcelWatermarkHelper is SUCCESSFULLY retrieved "+list.size()+" time(s)！");
//            for(String str:list){
//                System.out.println(str);
//            }
//
//            System.out.println("----如果您发现上面的解析内容是乱码，那么也可以参考以下gbk中文解码的水印内容----");
//            for(String str:chinese_list){
//                System.out.println(str);
//            }
        }catch(Exception e){
            e.printStackTrace();
            return new LinkedList<>();
        }

    }

    public static void main(String[] s) throws Exception{
        String filename = "3";//ta_cb_person_heatmap_collect_deleted
        String append = ".xlsx";
        int[] args = new int[]{};


        //CSV默认第一行是无效信息，第二行是表头，信息从第三行开始(使用嵌入csv的数据包会这样，对于一般的csv也可能没有第一行的无效信息)
        // xls默认第一行表头，第二行开始是信息
//        int startRow = (append.equals(".csv"))?2:1;


//        String[] Keys = {"id", "name", "time", "phone", "date"};
        String wmStr = Util.readWatermark("src//watermark.txt");
        String filePath = "src//embedded_results//"+filename+"_embedded"+append;

        List<String> result = Extract(filePath,0,null);

        System.out.println("------------------------");
        System.out.println("英文参考： "+result.get(0));
        System.out.println("中文参考： "+result.get(1));

    }
}