package com.example.demo;

import com.example.demo.Utils.ZipUtil;
import org.apache.commons.io.FileUtils;
import org.dom4j.*;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

//项目背景：需要通过Word文档中的部件（段落、标题等）对应的rsid字段，查找出目标Word中各个位置的最后修改者是谁，从而可以追求改错Word的人的责任
//          每个打开Word的用户会得到Word给他分配的一个新的rsid，该用户对文档中的部件进行修改，被修改的部件的rsid就会更新为该用户的rsid
//          文档部件rsid举例：
//            <w:p w:rsidR="007809A1" w:rsidRDefault="00709A1" w:rsidP="0019125">
//              <w:r>
//                  <w:t>Paragraph of text here.</w:t>
//              </w:r>
//            </w:p>
//          Word文档的所有已经使用过该文档的rsid保存在该文档的settings.xml中，举例如下：
//            <w:rsidRoot w:val="00E46950"/>
//              <w:rsid w:val="001A41D7"/>
//              <w:rsid w:val="004B7540"/>
//              <w:rsid w:val="006601F9"/>
//              <w:rsid w:val="00A1905"/>
//              <w:rsid w:val="00E46950"/>
//现在希望实现的内容：希望在用户打开Word之前，能够用本系统（Java代码）为其生成一个专属的rsid（A），并检查目标Word已有的rsid，保存在List中
//          随后，用户访问并修改Word后，该Word文档会多出一个rsid（B），我们的这个系统用A来代替B，这就包括：
//      1：把settings.xml里面多出来的rsid B删除掉，并添加上A
//      2：Word部件中rsid为B的把rsid改成A，并记录修改了哪些地方


public class Dom4jHelper {

    public static void Embed(String docxFileName,String desDir,String water,String outPathFile) throws Exception {
        List<String> newKeys = new LinkedList<>();
        int ind = 0;
        String waterHex = URLEncoder.encode(water, "GBK");StringBuilder temp = new StringBuilder();
        if(waterHex.equals(water)){

            //纯英文
            for(char c:water.toCharArray()){
                temp.append(Integer.toHexString(c));
            }
            //如果是奇数个符号，则尾部添加空格作为填充
            if(water.length()%2==1){
                temp.append(Integer.toHexString(' '));
            }

        }else{
            //观察是否为混合模式,注意，水印中不能有%
            int i=0;
            while(i<waterHex.length()){
                if(waterHex.charAt(i)=='%'){
                    temp.append(waterHex.substring(i,i+3));
                    i+=3;
                }
                else{
                    temp.append(String.format("%02x",(int)waterHex.charAt(i)));
                    i++;
                    if(i>=waterHex.length())
                        temp.append(String.format("%02x",(int)' '));
                    else{
                        temp.append(String.format("%02x",(int)waterHex.charAt(i)));
                        i++;
                    }

                }
            }
            //去除%
             //temp = new StringBuilder(waterHex);
            i=0;
            while(i<temp.length()){
                if(temp.charAt(i)=='%')
                    temp.deleteCharAt(i);
                i++;
            }


        }
        waterHex = temp.toString();
        for(int i=0;i<waterHex.length();i+=4){
            String newkey = String.format("%02x", ind)
                    +waterHex.substring(i,i+4);
            while(newkey.length()<8)
                newkey = '0'+newkey;

            newKeys.add(newkey);
            ind++;
        }

        System.out.println("-------- Unzip Files ----------");
        ZipUtil.unZipFiles(docxFileName,desDir);
        System.out.println("-------- Watermarking ----------");
        Dom4jHelper.dom4j(docxFileName,desDir,newKeys,true);
        System.out.println("-------- Zipping Files ----------");
        ZipUtil.toZip(desDir,outPathFile,false);
    }


    public static List<String> Extract(String docxFileName,String desDir,List<String> newKeys) throws Exception {
        //GBK与中文互转的例子
//        System.out
//                .println(URLDecoder.decode("%D6%D0%CE%C4%B9%FA%BC%CA", "GBK"));// GBK编码转中文
//        System.out.println(URLEncoder.encode("中文国际", "GBK")); // 中文编码转GBK
        List<String> res = new LinkedList<>();
        System.out.println("-------- Unzip Files ----------");
        ZipUtil.unZipFiles(docxFileName,desDir);
        System.out.println("-------- Extracting ----------");
        List<String> raw = Dom4jHelper.dom4j(docxFileName,desDir,newKeys,false);
        //Filter
        String[] chinese = new String[raw.size()];char[] english = new char[2*raw.size()];
        for(String i:raw){
            int loc = Integer.parseInt(i.substring(0,4),16);
            if(loc<=16) {
                //长度不能超过16
                //英文
                english[2 * loc] = (char) Integer.parseInt(i.substring(4, 6),16);
                english[2 * loc + 1] = (char) Integer.parseInt(i.substring(6, 8),16);
                //中文
                chinese[loc] = URLDecoder.decode(("%" + i.substring(4, 6) + "%" + i.substring(6, 8)), "GBK");
            }
        }
        StringBuilder chineseStr = new StringBuilder();StringBuilder englishStr = new StringBuilder();
        for(int i=0;i<chinese.length;i++) {
            if (english[i]!=0) {
                englishStr.append(english[2*i]);englishStr.append(english[2*i+1]);
            }
            if(chinese[i]!=null){
                chineseStr.append(chinese[i]);
            }
        }
        res.add( englishStr.toString());
        res.add( chineseStr.toString());
        return res;
    }

    public static List<String> dom4j(String docxFileName,String desDir,List<String> newKeys,boolean isEmbed) throws Exception {
//      String docxFileName = "D://Hello Word.docx";String desDir = "D://UnzipDocx";
        List<String> res = new LinkedList<>();

        String xmlPath = desDir+"\\word\\settings.xml";
        String readFileToString = FileUtils.readFileToString(new File(xmlPath), "UTF-8");
        Document doc = DocumentHelper.parseText(readFileToString);

        Element rootElement = doc.getRootElement();

        doc.getRootElement().addNamespace("w", "http://schemas.openxmlformats.org/wordprocessingml/2006/main");

        //final String A = "00001234";
        //通常只有一个
        List<Element> rsidsList = doc.getRootElement().elements("rsids");
        Element resIdElement = rsidsList.get(0);
        if(isEmbed) {

            //删除原有的节点
            //((Element)(resIdElement.elements("rsid").get(0))).attributeValue("val")
            List<Element> rsidList = resIdElement.elements("rsid");
            for(Element rsid:rsidList){
                if(Integer.parseInt(rsid.attributeValue("val").substring(0,4),16)<16)
                    resIdElement.remove(rsid);
            }


            //嵌入
            for (String A : newKeys) {
                //要添加的节点
                Element aNode = DocumentHelper.createElement("w:rsid").addAttribute("w:val", A);

                if (rsidsList != null && rsidsList.size() > 0) {

                    //查找是否有
                    Node node = resIdElement.selectSingleNode("rsid[@w:val='" + A + "']");
                    if (node != null) {
                        System.out.println(A + "已经存在");
                    } else {
                        //作为子节点添加在rsids的末尾
                        resIdElement.add(aNode);
                        //resIdElement.addElement("rsid").addAttribute("w:val", A);
                        //以下代码添加到特定位置
//                List rsidList = resIdElement.elements("rsid");
//                if (rsidList != null) {
//                    rsidList.add(0, aNode);
//                }
                    }
                }
            }

            //保存到原文件
            OutputFormat format = OutputFormat.createPrettyPrint();
            XMLWriter xmlWriter = new XMLWriter(new FileOutputStream(xmlPath), format);
            xmlWriter.write(doc);
            xmlWriter.close();
        }else{
            //提取
            if (rsidsList != null && rsidsList.size() > 0) {
                //Element resIdElement = (Element) rsidsList.get(0);
                //查找是否有
               List elements = resIdElement.elements();
               Iterator iter = elements.iterator();
               while (iter.hasNext()) {
                   Element node = (Element)iter.next();
                   String str = node.attributeValue("val");
                   System.out.println("- Found w:rsid: "+str);

                   res.add(str);

               }
            }
        }


        doc.getRootElement().remove(new Namespace("w", "http://schemas.openxmlformats.org/wordprocessingml/2006/main"));
        return res;
    }

}
