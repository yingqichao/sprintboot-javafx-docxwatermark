package com.example.demo.Excel;

import com.example.demo.Excel.Setting.Settings;
import com.example.demo.Excel.Utils.Util;
import com.example.demo.Excel.Utils.cyclic.CyclicCoder;
import com.google.gson.*;

import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.attribute.UserDefinedFileAttributeView;
import java.util.*;

/**
 * @author Qichao Ying
 * @date 2019/8/8 14:47
 * @Description DEFAULT
 */
public class JSONEncoder extends AbstractEncoder {

    TreeMap<String,String> JSON = new TreeMap<>();
    TreeMap<String,String> watermarkedJSON = new TreeMap<>();


    public JSONEncoder(int seed, double c, double delta, String f_bytes) {
        super(seed, c, delta, f_bytes);
    }

    public JSONEncoder(String f_bytes) {
        super(f_bytes);
    }

    public String encoder(String key, String value){
//        Generates an infinite sequence of blocks to transmit to the receiver
        String res = value;
        if(!Util.isNumeric(value)){
            System.out.println("[Warning] Embedding into String is now prohibited...");
        }else{
            Set<Integer> duplicateSet = new HashSet<>();
            int innerPackage = value.replaceAll("[^A-Za-z0-9]","").length()/ Settings.DEFAULT_MINLEN;
            if(innerPackage>1)
                System.out.println("-- This package is splitted to embed "+innerPackage+" packages --");
            for(int ite=0;ite<innerPackage;ite++){
                this.seed = Util.BKDRHash(key+((innerPackage>1)?ite:""),131);
                this.solitionGenerator.setSeed(this.seed);
                List<Integer> list = this.solitionGenerator.get_src_blocks(null);
                int block_data = 0;
                for(int i=2;i<list.size();i++)
                    block_data ^= this.blocks[list.get(i)];

                res = modify(res,key,block_data,list,duplicateSet);

            }
        }
        return res;

    }

    public String modify(String value,String keyname,Integer waterSeq,List<Integer> blocks,Set<Integer> duplicateSet){
        //modify the value to embed data
        Character first = null;boolean negative = false;int dotIndex = -1;
        StringBuilder newvalue = new StringBuilder(value);
        int startFrom = 0;String buffer = "";
        //preprocess
        if(Util.isInteger(value)){
            long value_int = Long.parseLong(value);
            //去除前缀0
            while(value.charAt(startFrom)=='-' || value.charAt(startFrom)=='.' || value.charAt(startFrom)=='0'){
//                newvalue.deleteCharAt(0);
                startFrom++;
            }

            //前两位暂存
            buffer = value.substring(0,startFrom+2);
            newvalue = new StringBuilder(value.substring(startFrom+2));

//            first = value.charAt(startFrom);newvalue.deleteCharAt(0);
        }else if(Util.isNumeric(value)){
            double value_double = Double.parseDouble(value);
//            negative = value_double<0;
//            if(negative)    {
////                newvalue.deleteCharAt(0);
//                startFrom++;
//            }

            //去除前缀0
            while(value.charAt(startFrom)=='-' || value.charAt(startFrom)=='.' || value.charAt(startFrom)=='0'){
//                newvalue.deleteCharAt(0);
                startFrom++;
            }

            //前两位暂存
            buffer = value.substring(0,startFrom+2);
            newvalue = new StringBuilder(value.substring(startFrom+2));

//            first = value.charAt(startFrom);newvalue.deleteCharAt(0);
//            dotIndex = newvalue.indexOf(".");
//            if(dotIndex>=0) {
//                newvalue.deleteCharAt(dotIndex);
//            }
        }

//        Set<Integer> duplicateSet = new HashSet<>(0);
        int embedded = 0;

//        String crc_text = Util.toBinary(waterSeq,Setting.Settings.DEFAULT_DATALEN);
//        crc_text += Util.crc_remainder(crc_text,null,null);

        String crc_text = Util.dec2bin(CyclicCoder.encode(waterSeq,Settings.NORMAL,Settings.SHORT),Settings.DEFAULT_EMBEDLEN);

        int debug = 0;

        while(embedded<crc_text.length()){
            int num = this.solitionGenerator.get_next() % newvalue.length();
            if(embedded==0)
                debug = num;
            if(!duplicateSet.contains(num)){
                duplicateSet.add(num);
                char ori = newvalue.charAt(num);
                if ((ori >= 'a' && ori <= 'z') || (ori >= 'A' && ori <= 'Z')) {
                    //对于小写大写字母：统一向上取结果
                    newvalue.setCharAt(num, (char) (ori + ori % 2 - crc_text.charAt(embedded) + '0'));
                    embedded ++;
                }
                else if(ori >= '0' && ori <= '9') {
                    //对于数字：统一向下取结果
                    newvalue.setCharAt(num, (char) (ori - ori % 2 + crc_text.charAt(embedded) - '0'));
                    embedded ++;
                }
                //其他的字符全都直接跳过
            }
        }

        //recovery
//        if(first!=null) newvalue.insert(0,first);
//        if(dotIndex!=-1) newvalue.insert(dotIndex,'.');
//        if(negative)    newvalue.insert(0,'-');
        newvalue.insert(0,buffer);

        System.out.println("Debug Embed: data->" + waterSeq + " seed->" + debug +" " + keyname + " " + newvalue);
        return newvalue.toString();

    }


    public void run(JsonObject object,String outpath, Set<String> banList){
        System.out.println("-----------------------------Embedding---------------------------------------");
        try {
            // 解析string
            JSON = eliminateLevels(object,banList);
            if(valid<this.minRequire){
                throw new Exception("[Error] Not enough valid packages for watermarking! Please shorter the watermark sequence...");
            }
            //Embedment
            for(String key:this.JSON.keySet()){
                String newValue = encoder(key,this.JSON.get(key));
                watermarkedJSON.put(key,newValue);
            }

            JsonElement newJsonElement = JsonUpdating(object);
//            FileOutputStream out=new FileOutputStream("src//embedded.json");
//            Utils.Util.writeJsonStream(out,object);

//            Utils.Util.writeFromJSON(watermarkedJSON);
            // Writing Json

            ByteBuffer buf = ByteBuffer.allocateDirect(10) ;
            buf.put(((Integer)this.solitionGenerator.K).toString().getBytes()) ;
            buf.flip();
            UserDefinedFileAttributeView userDefined = Files.getFileAttributeView(Paths.get(outpath), UserDefinedFileAttributeView.class);
            userDefined.write("num_packages",buf);


            FileOutputStream out=new FileOutputStream(outpath);
            Util.writeJsonStream(out,newJsonElement);

            System.out.println("-----------------Embedding was conducted successfully...--------------------");

            return;
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("-----------------[Warning] Embedding was not conducted...--------------------");
        }



        return;

    }

    //    public void eliminateLevels(){
////        int sum = 0,valid = 0;
//        for(String key:this.JSON.keySet()){
////            String value = this.JSON.get(key);
////            if(value.replaceAll("[^0-9a-zA-Z]","" ).length()>=7){
//                String newValue = encoder(key,value);
//                watermarkedJSON.put(key,newValue);
////                valid += 1;
////            }else{
////                watermarkedJSON.put(key,value);
////            }
////            sum += 1;
//        }
//
//    }

    public TreeMap<String,String> eliminateLevels(JsonObject object,Set<String> banList){
        // clear
        JSON = new TreeMap<>();
        sum = 0;valid = 0;

        recursiveEliminateHelper(object,"",0,banList);

        for(String key:JSON.keySet())
            System.out.println(key+"   "+JSON.get(key));

        System.out.println("-------------------------------------------------");

        System.out.println("Sum of KEYS: " + sum + ". Sum of Valid: " + valid);
        return JSON;
    }

    public void recursiveEliminateHelper(JsonElement object, String prefix,int arrayCount,Set<String> banList){
        // if the node is in the ban list, return and prohibit further recursion
        if(banList.contains(prefix))
            return;

        if(arrayCount>0){
            //which indicates the parent object is an array
            prefix += arrayCount;
            arrayCount = 0;
        }
        if(object instanceof JsonObject){
            // continue the recursion
            for(Map.Entry<String, JsonElement> entry:((JsonObject) object).entrySet()){
                recursiveEliminateHelper(entry.getValue(),prefix+entry.getKey(),arrayCount,banList);
            }
        }else if(object instanceof JsonArray){
            int count = 1;
            for (Iterator<JsonElement> iter = ((JsonArray) object).iterator(); iter.hasNext();){
                recursiveEliminateHelper(iter.next(),prefix,count,banList);
                count++;
            }
        }else if(!(object instanceof JsonNull)){
            // instance of JsonPrimitive
            String value = ((JsonPrimitive)object).getAsString();
            int lengthQualify = Util.lengthQualify(value,3);
            if (Util.isNumeric(value) && lengthQualify > Settings.DEFAULT_MINLEN){
                //valid
                JSON.put(prefix.replaceAll("[^A-Za-z0-9]",""),value);
//                if(Util.isNumeric(value))
                    //新规定要yy求只能在float或者int中嵌入数据
                valid += lengthQualify/Settings.DEFAULT_MINLEN;
            }
            sum++;
        }
    }

    public JsonElement JsonUpdating(JsonObject object){
        JsonElement jsonElement = Util.replaceKey(object,watermarkedJSON,"","",0,Settings.newTagName,Settings.packageNumName,this.K);

        System.out.println("Successfully updated!......");

        return jsonElement;

    }
}