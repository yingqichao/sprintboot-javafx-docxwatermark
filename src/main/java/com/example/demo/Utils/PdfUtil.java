package com.example.demo.Utils;

import java.io.*;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PdfUtil {
    public static boolean isPureDigital(String str) {
        if (str == null || "".equals(str)){
            return false;
        }

        Pattern p;
        Matcher m;
        p = Pattern.compile("[0-9]*");
        m = p.matcher(str);
        if (m.matches()){
            return true;
        }else{
            return false;
        }
    }

    public static int findInteger(String str, int index){

        String strNum = "";
        boolean flag = false;
        int i = -1;

        for(i = index; i < str.length(); i++){
            if(str.charAt(i) >= '0' && str.charAt(i) <= '9')
                flag = true;
            if(flag) {
                if(str.charAt(i) >= '0' && str.charAt(i) <= '9')
                    strNum = strNum + str.charAt(i);
                else
                    break;
            }
        }

        if(i == str.length()) // 没找到
            return -1;

        return i; // 找到了，末尾位置是i
    }

    public static int getInteger(String str, int index){

        String strNum = "";
        boolean flag = false;
        int i = -1;

        if(index < 0)
            return i;

        for(i = index; i < str.length(); i++){
            if(str.charAt(i) >= '0' && str.charAt(i) <= '9')
                flag = true;
            if(flag) {
                if(str.charAt(i) >= '0' && str.charAt(i) <= '9')
                    strNum = strNum + str.charAt(i);
                else
                    break;
            }
        }

        if(i == str.length()) // 没找到
            return -1;

        return Integer.valueOf(strNum); // 找到了，末尾位置是i
    }

    public static String insert2String(String src, String ins, int index){
        StringBuilder sb = new StringBuilder(src);//构造一个StringBuilder对象
        sb.insert(index, ins);//在指定的位置1，插入指定的字符串
        return sb.toString();
    }

    public static String replaceStringNumAt(String srcStr, String dstStr, int first, int last){
        int i;
        for(i = first; i < srcStr.length(); i++) {
            if (srcStr.charAt(i) >= '0' && srcStr.charAt(i) <= '9')
                break;
        }

        StringBuilder sb = new StringBuilder(srcStr);
        sb.replace(i, last, dstStr);
        return sb.toString();
    }

    public static String replaceStringAt(String srcStr, String dstStr, int first, int last){

        String a = srcStr.substring(0, first);
        String b = srcStr.substring(last);
        String c = a + dstStr + b;

        return c;

//        StringBuilder sb = new StringBuilder(srcStr);
//        String s = srcStr.substring(first,last);
//        sb.replace(first, last, dstStr);
//        return sb.toString();
    }


    public static String readToString(String fileName) throws IOException {
        String encoding = "utf-8";
        File file = new File(fileName);
        Long filelength = file.length();
        byte[] filecontent = new byte[filelength.intValue()];
        FileInputStream in = new FileInputStream(file);
        in.read(filecontent);
        in.close();

//        return new String(filecontent, encoding);
        return new String(filecontent);
    }

    public static byte[] readToByte(String fileName) throws IOException {
        File f = new File(fileName);
        int length = (int) f.length();
        byte[] data = new byte[length];
        new FileInputStream(f).read(data);

        return data;
    }

    /***
     * 去除String数组中的空值
     */
    public static String[] deleteArrayNull(String string[]) {
        String strArr[] = string;

        // step1: 定义一个list列表，并循环赋值
        ArrayList<String> strList = new ArrayList<String>();
        for (int i = 0; i < strArr.length; i++) {
            strList.add(strArr[i]);
        }

        // step2: 删除list列表中所有的空值
        while (strList.remove(null));
        while (strList.remove(""));

        // step3: 把list列表转换给一个新定义的中间数组，并赋值给它
        String strArrLast[] = strList.toArray(new String[strList.size()]);

        return strArrLast;
    }

    public static void copyFileUsingFileStreams(File source, File dest)
            throws IOException {
        InputStream input = null;
        OutputStream output = null;
        try {
            input = new FileInputStream(source);
            output = new FileOutputStream(dest);
            byte[] buf = new byte[1024];
            int bytesRead;
            while ((bytesRead = input.read(buf)) != -1) {
                output.write(buf, 0, bytesRead);
            }
        } finally {
            input.close();
            output.close();
        }
    }
}
