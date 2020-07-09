package com.example.demo.PdfHelper;

import com.example.demo.Utils.PdfUtil;
import com.example.demo.Utils.RSACode;

import java.io.*;
import java.util.*;

public class PdfWm {

    String srcFilePath;
    String dstFilePath;

     Boolean No_Xref = false;

//    Map<Integer, ArrayList<Integer>> ParsedXref = new TreeMap<>();//解析后的交叉引用表Xref
    Map<Integer, String> StringXref = new TreeMap<>();//解析后的交叉引用表Xref
    Map<Integer, String> AlteredStringXref = new TreeMap<>();//更改后的交叉引用表Xref

    ArrayList<Integer> PrevList = new ArrayList<>();
    ArrayList<Integer> AlteredPrevList = new ArrayList<>();
    int threshold;
    int Offset;

    String strContext;

    public PdfWm() throws Exception {
    }

    public PdfWm(String srcFile, String dstFile, String wm) throws Exception {
        embedWm( srcFile, dstFile, wm);
    }

    public void embedWm(String srcFile, String dstFile, String wm) throws Exception {
        // 将Pdf中所有内容保存到context中
        srcFilePath = srcFile;
        dstFilePath = dstFile;
        strContext =  PdfUtil.readToString(srcFile);

        System.out.println("\033[34m" + "\n====================================================");
        System.out.println("Try to Embed info to \n<" + dstFile + "> from \n<" + srcFile + ">...");
        System.out.println("====================================================\n" + "\033[m");

        //PdfUtil.copyFileUsingFileStreams(new File(srcFile), new File(dstFile));
        createFile();

        embed2DstFile(dstFile, wm);

    }

    public String extractWm(String dstFile) throws Exception {
        String deWm = "";

        System.out.println("\033[34m" + "\n====================================================");
        System.out.println("Try to extract info from \n<" + dstFile + ">...");
        System.out.println("====================================================\n" + "\033[m");

        dstFilePath = dstFile;
        strContext =  PdfUtil.readToString(dstFile);

        // 获得PDF中最大的obj序列号
        PdfParsing pdfparsing = new PdfParsing(dstFile);
        int maxEleNo = pdfparsing.getMaxEleNo();

        int mx = 0;
        SortedMap<Integer, String> parsedBody = new TreeMap<>();
        for(String s : pdfparsing.Body){
            if(s.contains("stream") && s.contains("endstream")) {
                String[] strs = s.split(" ");
                int no = Integer.valueOf(strs[0]);
                if(parsedBody.containsKey(no)) {
                    parsedBody.put(no + 1, s);
                    if(mx >= maxEleNo)
                        mx = maxEleNo;
                }
                else
                    parsedBody.put(no, s);
            }
        }

        for(Integer key : parsedBody.keySet()){
            if(key >= maxEleNo) {
                String s = parsedBody.get(key);
//                int wFirst = s.indexOf("stream") + "stream".length();
//                int wLast  = s.indexOf("endstream");
//                String wm = s.substring(wFirst,wLast);
                String wm = decodeWm(s);

                int ind = s.indexOf("Length ");
                int Size = PdfUtil.getInteger(s, ind);

                if(Size != -1 && wm.length() == Size)
                    deWm = wm;
            }
        }


        pdfparsing.PdfClose();

        System.out.println("\033[34m" + "Extraction Finished!" + "\033[m\n");

        return deWm;
    }

    public void embed2DstFile(String dstFile, String wm) throws Exception {
        // 找到所有startxref信息，将所有有效的xref提取出来
        String enWm = encodeWm(wm);

        getXref();

        // 获得PDF中最大的obj序列号
        PdfParsing pdfparsing = new PdfParsing(srcFilePath);
        int maxEleNo = pdfparsing.getMaxEleNo();
        pdfparsing.PdfClose();

        for(Integer key : StringXref.keySet()){
            embed2contentOnce(maxEleNo + enWm + 1, key);
            break;
        }

    }

    public int calcEmbedPos(){
        int maxIndXref = -1;
        for(Integer key : StringXref.keySet())
            if(maxIndXref < key)
                maxIndXref = key;

        for(Integer key : StringXref.keySet()) {
            String curXref = StringXref.get(key);
            int ind;
            // 更改 Prev
            if ((ind = curXref.indexOf("Prev ")) > 0) {
                int Prev = PdfUtil.getInteger(curXref, ind);
                if(maxIndXref < Prev)
                    maxIndXref = Prev;
            }
        }

        return maxIndXref;

    }

    public void embed2contentOnce(String enWm, Integer indXref) throws IOException {

        if(No_Xref){
            System.out.println("Not Found Cross reference table(Xref), try to second plan...");
            embed2NoXrefFile(enWm);
            return;
        }

        Offset = enWm.length(); // 偏移量
        // 计算嵌入位置
        threshold = calcEmbedPos(); // 嵌入位置，作为阈值


        // 计算修改以后的Xref
        int preDelta = 0;
        for(Integer key : StringXref.keySet()) {
            String curXref = StringXref.get(key);
            int orgLen = curXref.length();
            int ind;
            // 更改 Prev
            if ((ind = curXref.indexOf("Prev ")) > 0) {
                int lastIndPrev = PdfUtil.findInteger(curXref, ind);
                int Prev = PdfUtil.getInteger(curXref, ind);
                PrevList.add(Prev);
                if (Prev + preDelta >= threshold) {
                    curXref = PdfUtil.replaceStringNumAt(curXref, String.valueOf(Prev + Offset + preDelta), ind, lastIndPrev);
                    AlteredPrevList.add(Prev + Offset + preDelta);
                }else
                    AlteredPrevList.add(Prev + preDelta);
            }
//                // 更改 Size
//                if ((ind = curXref.indexOf("Size ")) > 0){
//                    int lastIndSize = PdfUtil.findInteger(curXref, ind);
//                    int Size = PdfUtil.getInteger(curXref, ind);
//                    curXref = PdfUtil.replaceStringNumAt(curXref, String.valueOf(Size+1), ind, lastIndSize);
//                }
            // 更改 startxref
            if ((ind = curXref.indexOf("startxref")) > 0) {
                int lastIndStartxref = PdfUtil.findInteger(curXref, ind);
                int startxref = PdfUtil.getInteger(curXref, ind);
                if (startxref + preDelta >= threshold) {
                    curXref = PdfUtil.replaceStringNumAt(curXref, String.valueOf(startxref + Offset + preDelta), ind, lastIndStartxref);
                }else
                    curXref = PdfUtil.replaceStringNumAt(curXref, String.valueOf(startxref + preDelta), ind, lastIndStartxref);

            }

            if (key+preDelta < threshold)
                AlteredStringXref.put(key + preDelta, curXref);
            else
                AlteredStringXref.put(key + Offset + preDelta, curXref);

            preDelta += (orgLen - curXref.length()); // 数值进位带来的误差

        }

        embed2Disk(enWm);
    }


    public void embed2NoXrefFile(String enWm) throws IOException {
        int len = "startxref".length();

        PdfUtil.copyFileUsingFileStreams(new File(srcFilePath), new File(dstFilePath));

        RandomAccessFile output = new RandomAccessFile(new File(dstFilePath), "rw");

        long fileLength = output.length();
        long start = output.getFilePointer();
        long readIndex = start + fileLength - len;
        output.seek(readIndex);

        long len2 = len; // 计算startxref以后有多少数据
        Boolean findStartXref = false;
        while(readIndex > 0) {
            byte[] buf = new byte[len];
            int l = -1;
            if ((l = output.read(buf, 0, len)) != -1) {
                String s = new String(buf);
                if (s.equals("startxref")) {
                    findStartXref = true;
                    break;
                }
            }

            readIndex--;
            len2++;
            output.seek(readIndex);
        }

        long posStartXref = -1;
        if(findStartXref) {
            posStartXref = readIndex;
            output.seek(posStartXref);

            byte[] buf2 = new byte[(int) len2];
            if ((output.read(buf2, 0, (int) len2)) == -1)
                System.out.println("尾部没有读出来: " + String.valueOf(buf2));
            output.seek(posStartXref);
            output.writeBytes(enWm);
            output.write(buf2,0, (int) len2);

            System.out.println("\033[34m" + "Embedding Succeed!\n"  + "\033[m\n");
        }else
            System.out.println("\033[31m" + "Not Found startxref, embedding failed!" + "\033[m\n");

        output.close();

    }

    public void embed2Disk(String enWm) throws IOException {
        // 使用文件指针操作目标文件
        // 先计算嵌入位置，将所有的Xref更新后再嵌入一次信息即可
        InputStream  input  = new FileInputStream(new File(srcFilePath));
        OutputStream output = new FileOutputStream(new File(dstFilePath));

        ArrayList<Integer> AlteredIndStringXref = new ArrayList<>();
        for(Integer key : AlteredStringXref.keySet())      AlteredIndStringXref.add(key);
        ArrayList<Integer> IndStringXref = new ArrayList<>();
        for(Integer key : StringXref.keySet())      IndStringXref.add(key);

        SortedSet<Integer> indXref = new TreeSet<>();
        SortedSet<Integer> AlteredIndXref = new TreeSet<>();

        for(Integer key : StringXref.keySet())      indXref.add(key);
        for(Integer key : PrevList)                 indXref.add(key);
        for(Integer key : AlteredStringXref.keySet())       AlteredIndXref.add(key);
        for(Integer key : AlteredPrevList)                  AlteredIndXref.add(key);

        Object[] iXref = indXref.toArray();
        Object[] AltIXref = AlteredIndXref.toArray();

        int val = 1;
        // 写入
        int k = 0;
        if((int)iXref[0] == 0)
            k = 1;

        int writeCnt = 0;
        Boolean isEmbed = false;

        int orgOff = 0;
        int c = 0;
        int readLen = 0;
        for(int i = k; i < iXref.length; i++){

            int bytesRead = 0;
            int bufLen = (int) iXref[i] - readLen;
            byte[] buf = new byte[bufLen];
            if ((bytesRead = input.read(buf)) != -1) {
                output.write(buf, orgOff, bytesRead-orgOff);
                writeCnt += bytesRead-orgOff;

                orgOff = StringXref.get(IndStringXref.get(c)).length();
                readLen = readLen + bytesRead;
            }

            if(!isEmbed && readLen >= threshold){
                byte[] wmBuf = enWm.getBytes();
                output.write(wmBuf, 0, enWm.length());
                isEmbed = true;
            }


            byte[] buf2 = AlteredStringXref.get(AlteredIndStringXref.get(c)).getBytes();
            int bytesRead2 = AlteredStringXref.get(AlteredIndStringXref.get(c)).length();
            output.write(buf2, 0, bytesRead2);
            writeCnt += bytesRead-bytesRead2;

            c++;
        }

        int bytesRead = 0;
        int bufLen = 100000;
        byte[] buf = new byte[bufLen];
        if ((bytesRead = input.read(buf)) != -1) {
            output.write(buf, orgOff, bytesRead-orgOff);
        }

        input.close();
        output.close();

        System.out.println("\033[34m" + "Embedding Succeed!\n" + "\033[m\n");
    }

    /*
        从当前位置开始提取出xref
        没有xref则返回空串
     */
    public void getXref() throws IOException {

        System.out.println("Parsing the Cross reference table (Xref) ...");
        // 取出所欲的交叉引用表
        for(int i = strContext.indexOf("startxref"); i > 0 && i < strContext.length(); i++){
            int indEof =  strContext.indexOf("%%EOF", i);
            int indStart = i+1+"startxref".length();
            String s = strContext.substring(indStart-1, indEof-1).replace("\r", "");
            s = s.replace("\n", "");
            s = s.replace(" ", "");
            int indXref = Integer.valueOf(s);
            int indGBKXref = strContext.substring(0,i).lastIndexOf("xref");

            if(-1 == indGBKXref) {
                indGBKXref = i;
                No_Xref = true;
            }
            String curXref = strContext.substring(indGBKXref, indEof);
            StringXref.put(indXref, curXref); //添加真实的偏移地址

            i = strContext.indexOf("startxref", indEof);
        }

    }

    public String decodeWm(String enWm) throws Exception {

        RSACode RsaCode = new RSACode();
        RsaCode.genKeyPair();

        enWm = enWm.substring(enWm.indexOf("stream")+"stream".length(), enWm.indexOf("endstream"));
        //decode字符串
//        System.out.println("随机生成的公钥为:" + RsaCode.keyMap.get(0));
//        System.out.println("随机生成的私钥为:" + RsaCode.keyMap.get(1));
        String deWm = RsaCode.decrypt(enWm,RsaCode.keyMap.get(1));

        return deWm;
    }

    public String encodeWm(String wm) throws Exception {

        RSACode RsaCode = new RSACode();
        RsaCode.genKeyPair();
        //加密字符串
//        System.out.println("随机生成的公钥为:" + RsaCode.keyMap.get(0));
//        System.out.println("随机生成的私钥为:" + RsaCode.keyMap.get(1));
        String enWm = RsaCode.encrypt(wm,RsaCode.keyMap.get(0));
        enWm = " 0 obj\r<</Length " + wm.length() + "/Filter/FlateDecode>>stream" + enWm + "endstream\rendobj\r\n";

        return enWm;
    }


    public void createFile() throws IOException {
        File checkFile = new File(dstFilePath);
        // 二、检查目标文件是否存在，不存在则创建
        System.out.println("Create target file : " + dstFilePath + "...");
        if (!checkFile.exists()) {
            checkFile.createNewFile();// 创建目标文件
        }
    }


}
