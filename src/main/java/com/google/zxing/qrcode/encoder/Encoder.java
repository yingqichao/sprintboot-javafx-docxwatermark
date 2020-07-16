/*
 * Copyright 2008 ZXing authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.zxing.qrcode.encoder;

import com.google.zxing.WriterException;
import com.google.zxing.client.result.common.BitArray;
import com.google.zxing.client.result.common.CharacterSetECI;
import com.google.zxing.client.result.common.ECI;
import com.google.zxing.client.result.common.reedsolomon.GenericGF;
import com.google.zxing.client.result.common.reedsolomon.ReedSolomonEncoder;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.google.zxing.qrcode.decoder.Mode;
import com.google.zxing.qrcode.decoder.Version;

import java.io.UnsupportedEncodingException;
import java.util.Vector;

/**
 * @author satorux@google.com (Satoru Takabayashi) - creator
 * @author dswitkin@google.com (Daniel Switkin) - ported from C++
 */
public final class Encoder {

    // The original table is defined in the table 5 of JISX0510:2004 (p.19).
    private static final int[] ALPHANUMERIC_TABLE = {
            -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,  // 0x00-0x0f
            -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,  // 0x10-0x1f
            36, -1, -1, -1, 37, 38, -1, -1, -1, -1, 39, 40, -1, 41, 42, 43,  // 0x20-0x2f
            0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 44, -1, -1, -1, -1, -1,  // 0x30-0x3f
            -1, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24,  // 0x40-0x4f
            25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, -1, -1, -1, -1, -1,  // 0x50-0x5f
    };
    static final String DEFAULT_BYTE_MODE_ENCODING = "ISO-8859-1";
    private Encoder() {
    }

    // The mask penalty calculation is complicated.  See Table 21 of JISX0510:2004 (p.45) for details.
    // Basically it applies four rules and summate all penalties.
    private static int calculateMaskPenalty(ByteMatrix matrix) {
        int penalty = 0;
        penalty += MaskUtil.applyMaskPenaltyRule1(matrix);
        penalty += MaskUtil.applyMaskPenaltyRule2(matrix);
        penalty += MaskUtil.applyMaskPenaltyRule3(matrix);
        penalty += MaskUtil.applyMaskPenaltyRule4(matrix);
        return penalty;
    }

    /**
     * Encode "bytes" with the error correction level "ecLevel". The encoding mode will be chosen
     * internally by chooseMode(). On success, store the result in "qrCode".
     * <p>
     * We recommend you to use QRCode.EC_LEVEL_L (the lowest level) for
     * "getECLevel" since our primary use is to show QR code on desktop screens. We don't need very
     * strong error correction for this purpose.
     * <p>
     * Note that there is no way to encode bytes in MODE_KANJI. We might want to add EncodeWithMode()
     * with which clients can specify the encoding mode. For now, we don't need the functionality.
     */
    public static void encode(String content, ErrorCorrectionLevel ecLevel, String encoding,
                              QRCode qrCode) throws WriterException {

        // Step 1: Choose the mode (encoding).
        Mode mode = chooseMode(content, encoding);
        // Step 2: Append "bytes" into "dataBits" in appropriate encoding.编码信息的字符类型
        BitArray dataBits = new BitArray();
        appendBytes(content, mode, dataBits, encoding);
        // Step 3: Initialize QR code that can contain "dataBits".初始化可以包含“dataBits”的二维码。确定使用版本号
        int numInputBytes = dataBits.getSizeInBytes();
        initQRCode(numInputBytes,ecLevel, mode, qrCode);
      //  System.out.println("初始化完成");
        // Step 4: Build another bit vector that contains header and data.
        //构建另一个包含头和数据的位向量
        BitArray headerAndDataBits = new BitArray();//未交织，无纠错码

        // Step 4.5: Append ECI message if applicable
        if (mode == Mode.BYTE && !DEFAULT_BYTE_MODE_ENCODING.equals(encoding)) {
            CharacterSetECI eci = CharacterSetECI.getCharacterSetECIByName(encoding);
            if (eci != null) {
                appendECI(eci, headerAndDataBits);
            }
        }
        appendModeInfo(mode, headerAndDataBits);
        int numLetters = mode.equals(Mode.BYTE) ? dataBits.getSizeInBytes() : content.length();
        
        appendLengthInfo(numLetters, qrCode.getVersion(), mode, headerAndDataBits);
        headerAndDataBits.appendBitArray(dataBits);

        // Step 5: Terminate the bits properly.填充比特
   //     BitArray other = new BitArray();
       // Mode mode2 = chooseMode(qrCode.getEx(), encoding);//确定编码
        //appendBytes(qrCode.getEx(), mode2, other, encoding);//编码
    //    BitArray headerAndDataBits2 = new BitArray();
     //   appendModeInfo(mode2, headerAndDataBits2);
     //   numLetters = mode2.equals(Mode.BYTE) ? other.getSizeInBytes() : qrCode.getEx().length();
      //  appendLengthInfo(numLetters, qrCode.getVersion(), mode2, headerAndDataBits2);//长度及版本信息
    //    headerAndDataBits2.appendBitArray(other);
       // append8BitBytes(qrCode.getEx(), other,"UTF-8");
     //   System.out.println("初始化完成1");
        int validDataBytes = terminateBits(qrCode.getNumDataBytes(), headerAndDataBits);//获取有效数据比特数
        qrCode.setValidDataBytes(validDataBytes);


       /* System.out.println("有效比特数为：" + validDataBytes);
        System.out.println("总数据比特数为：" + qrCode.getNumDataBytes());
        double pcent=((double)validDataBytes/qrCode.getNumDataBytes())*100;
        System.out.println("百分比:"+(double)pcent);
        System.out.println("总比特数为：" + qrCode.getNumTotalBytes());//总比特=总数据比特+纠错码容量*2*RS码块数
        System.out.println("RS码块数为：" + qrCode.getNumRSBlocks());*/
        
        // Step 6: Interleave data bits with error correction code.将数据位与纠错码交织
        BitArray finalBits = new BitArray();//交织，含纠错码
        interleaveWithECBytes(headerAndDataBits, qrCode.getNumTotalBytes(), qrCode.getNumDataBytes(),
                qrCode.getNumRSBlocks(), finalBits);

        qrCode.setRsCode(finalBits);
        // Step 7: Choose the mask pattern and set to "qrCode".系统默认为根据内容择优选择掩码
        ByteMatrix matrix = new ByteMatrix(qrCode.getMatrixWidth(), qrCode.getMatrixWidth());
       
        // Step 8.  Build the matrix and set it to "qrCode".

        BitArray preBits = new BitArray();//预掩码比特存储
    
    
        MatrixUtil.buildMatrix(finalBits, qrCode.getMaskPattern(), matrix,qrCode);
        qrCode.setMatrix(matrix);

        // Step 9.  Make sure we have a valid QR Code.确保我们有一个有效的二维码。
        if (!qrCode.isValid()) {
            throw new WriterException("Invalid QR code: " + qrCode.toString());
        }
    }

    /**
     * @return the code point of the table used in alphanumeric mode or
     * -1 if there is no corresponding code in the table.
     */
    static int getAlphanumericCode(int code) {
        if (code < ALPHANUMERIC_TABLE.length) {
            return ALPHANUMERIC_TABLE[code];
        }
        return -1;
    }

    public static Mode chooseMode(String content) {
        return chooseMode(content, null);
    }

    /**
     * Choose the best mode by examining the content. Note that 'encoding' is used as a hint;
     * if it is Shift_JIS, and the input is only double-byte Kanji, then we return {@link Mode#KANJI}.
     */
    public static Mode chooseMode(String content, String encoding) {//自动检测并选择编码模式
        if ("Shift_JIS".equals(encoding)) {
            // Choose Kanji mode if all input are double-byte characters
            return isOnlyDoubleByteKanji(content) ? Mode.KANJI : Mode.BYTE;
        }
        boolean hasNumeric = false;
        boolean hasAlphanumeric = false;
        for (int i = 0; i < content.length(); ++i) {//检测内容中是否含数字或字母
            char c = content.charAt(i);
            if (c >= '0' && c <= '9') {
                hasNumeric = true;
            } else if (getAlphanumericCode(c) != -1) {
                hasAlphanumeric = true;
            } else {
                return Mode.BYTE;
            }
        }
        if (hasAlphanumeric) {
            return Mode.ALPHANUMERIC;
        } else if (hasNumeric) {
            return Mode.NUMERIC;
        }
        return Mode.BYTE;
    }

    private static boolean isOnlyDoubleByteKanji(String content) {
        byte[] bytes;
        try {
            bytes = content.getBytes("Shift_JIS");
        } catch (UnsupportedEncodingException uee) {
            return false;
        }
        int length = bytes.length;
        if (length % 2 != 0) {
            return false;
        }
        for (int i = 0; i < length; i += 2) {
            int byte1 = bytes[i] & 0xFF;
            if ((byte1 < 0x81 || byte1 > 0x9F) && (byte1 < 0xE0 || byte1 > 0xEB)) {
                return false;
            }
        }
        return true;
    }

    //选择mask图案
    private static int chooseMaskPattern(BitArray bits, ErrorCorrectionLevel ecLevel, int version,
                                         ByteMatrix matrix) throws WriterException {

        int minPenalty = Integer.MAX_VALUE;  // Lower penalty is better.
        int bestMaskPattern = -1;
        // We try all mask patterns to choose the best one.
        for (int maskPattern = 0; maskPattern < QRCode.NUM_MASK_PATTERNS; maskPattern++) {
            MatrixUtil.buildMatrix(bits, ecLevel, version, maskPattern, matrix);
            int penalty = calculateMaskPenalty(matrix);
            if (penalty < minPenalty) {
                minPenalty = penalty;
                bestMaskPattern = maskPattern;
            }
        }
        return bestMaskPattern;
    }

    /**
     * Initialize "qrCode" according to "numInputBytes", "ecLevel", and "mode". On success,
     * modify "qrCode".
     */
    private static void initQRCode(ErrorCorrectionLevel ecLevel, Mode mode,
                                   QRCode qrCode) throws WriterException {//设置版本后，系统默认为择优选择
        qrCode.setECLevel(ecLevel);
        qrCode.setMode(mode);

        int versionNum = 10;//使用固定的版本号，其中版本6为定位图案最少而数据量最大的版本
        Version version = Version.getVersionForNumber(versionNum);
        int numBytes = version.getTotalCodewords();
        Version.ECBlocks ecBlocks = version.getECBlocksForLevel(ecLevel);
        int numEcBytes = ecBlocks.getTotalECCodewords();
        int numRSBlocks = ecBlocks.getNumBlocks();
        int numDataBytes = numBytes - numEcBytes;
        qrCode.setVersion(versionNum);
        qrCode.setNumTotalBytes(numBytes);
        qrCode.setNumDataBytes(numDataBytes);
        qrCode.setNumRSBlocks(numRSBlocks);
        qrCode.setNumECBytes(numEcBytes);
        qrCode.setMatrixWidth(version.getDimensionForVersion());

    }

    private static void initQRCode(int numInputBytes, ErrorCorrectionLevel ecLevel, Mode mode,
                                   QRCode qrCode) throws WriterException {//设置版本后，系统默认为择优选择
        qrCode.setECLevel(ecLevel);
        qrCode.setMode(mode);
        // In the following comments, we use numbers of Version 7-H.
        for (int versionNum = 1; versionNum <= 40; versionNum++) {
            Version version = Version.getVersionForNumber(versionNum);
            // numBytes = 196
            int numBytes = version.getTotalCodewords();
            // getNumECBytes = 130
            Version.ECBlocks ecBlocks = version.getECBlocksForLevel(ecLevel);
            int numEcBytes = ecBlocks.getTotalECCodewords();
            // getNumRSBlocks = 5
            int numRSBlocks = ecBlocks.getNumBlocks();
            // getNumDataBytes = 196 - 130 = 66
            int numDataBytes = numBytes - numEcBytes;
            // We want to choose the smallest version which can contain data of "numInputBytes" + some
            // extra bits for the header (mode info and length info). The header can be three bytes
            // (precisely 4 + 16 bits) at most. Hence we do +3 here.
            if (numDataBytes >= numInputBytes + 3) {
                // Yay, we found the proper rs block info!
                qrCode.setVersion(versionNum);
                qrCode.setNumTotalBytes(numBytes);
                qrCode.setNumDataBytes(numDataBytes);
                qrCode.setNumRSBlocks(numRSBlocks);
                // getNumECBytes = 196 - 66 = 130
                qrCode.setNumECBytes(numEcBytes);
                // matrix width = 21 + 6 * 4 = 45
                qrCode.setMatrixWidth(version.getDimensionForVersion());
                return;
            }
            
        }
        
        throw new WriterException("Cannot find proper rs block info (input data too big?)");
    }

    /**
     * Terminate bits as described in 8.4.8 and 8.4.9 of JISX0510:2004 (p.24).
     */
    static int terminateBits(int numDataBytes, BitArray bits) throws WriterException {//填充块
        //numDataBytes-数据比特，bits.getSize()-有效比特
        int capacity = numDataBytes << 3;
       // System.out.println(capacity);
        if (bits.getSize() > capacity) {
            throw new WriterException("data bits cannot fit in the QR Code" + bits.getSize() + " > " +
                    capacity);
        }
        for (int i = 0; i < 4 && bits.getSize() < capacity; ++i) {//结尾必补四0
            bits.appendBit(false);
        }
        // Append termination bits. See 8.4.8 of JISX0510:2004 (p.24) for details.
        // If the last byte isn't 8-bit aligned, we'll add padding bits.

        int numBitsInLastByte = bits.getSize() & 0x07;//00000111 8的倍数
        if (numBitsInLastByte > 0) {
            for (int i = numBitsInLastByte; i < 8; i++) {
                bits.appendBit(false);
            }
        }
        // If we have more space, we'll fill the space with padding patterns defined in 8.4.9 (p.24).
        int numPaddingBytes = numDataBytes - bits.getSizeInBytes();//填充比特数
        int validDataBytes = bits.getSizeInBytes();

      /*  int temp=other.getSize() & 0x07;
        if (temp > 0) {
            for (int i = temp; i < 8; i++) {
                other.appendBit(false);
            }
        }
        bits.appendBitArray(other);*/
        /*bits.appendBits(0x82,8);//10000010//00011110//10000010-00100000-10000110-100
        bits.appendBits(0x00,8);//00000000//11000000*/
      //  numPaddingBytes-= other.getSize()/8;
        for (int i = 0; i < numPaddingBytes; ++i) {//填充内容设置
  //    bits.appendBits((i & 0x01) == 0 ? 0xAA : 0x55, 8);
//      bits.appendBits(0, 8);
//      bits.appendBits(0xff,8);
        bits.appendBits((i & 0x01) == 0 ? 0xEC : 0x11, 8);//11101100,00010001
        }
        if (bits.getSize() != capacity) {
            throw new WriterException("Bits size does not equal capacity");
        }
        return validDataBytes;
    }

    /**
     * Get number of data bytes and number of error correction bytes for block id "blockID". Store
     * the result in "numDataBytesInBlock", and "numECBytesInBlock". See table 12 in 8.5.1 of
     * JISX0510:2004 (p.30)
     */
    static void getNumDataBytesAndNumECBytesForBlockID(int numTotalBytes, int numDataBytes,
                                                       int numRSBlocks, int blockID, int[] numDataBytesInBlock,
                                                       int[] numECBytesInBlock) throws WriterException {
        if (blockID >= numRSBlocks) {
            throw new WriterException("Block ID too large");
        }
        // numRsBlocksInGroup2 = 196 % 5 = 1（以版本7-H为例）
        int numRsBlocksInGroup2 = numTotalBytes % numRSBlocks;
        // numRsBlocksInGroup1 = 5 - 1 = 4
        int numRsBlocksInGroup1 = numRSBlocks - numRsBlocksInGroup2;
        // numTotalBytesInGroup1 = 196 / 5 = 39
        int numTotalBytesInGroup1 = numTotalBytes / numRSBlocks;
        // numTotalBytesInGroup2 = 39 + 1 = 40
        int numTotalBytesInGroup2 = numTotalBytesInGroup1 + 1;
        // numDataBytesInGroup1 = 66 / 5 = 13
        int numDataBytesInGroup1 = numDataBytes / numRSBlocks;
        // numDataBytesInGroup2 = 13 + 1 = 14
        int numDataBytesInGroup2 = numDataBytesInGroup1 + 1;
        // numEcBytesInGroup1 = 39 - 13 = 26
        int numEcBytesInGroup1 = numTotalBytesInGroup1 - numDataBytesInGroup1;
        // numEcBytesInGroup2 = 40 - 14 = 26
        int numEcBytesInGroup2 = numTotalBytesInGroup2 - numDataBytesInGroup2;
        // Sanity checks.
        // 26 = 26
        if (numEcBytesInGroup1 != numEcBytesInGroup2) {
            throw new WriterException("EC bytes mismatch");
        }
        // 5 = 4 + 1.
        if (numRSBlocks != numRsBlocksInGroup1 + numRsBlocksInGroup2) {
            throw new WriterException("RS blocks mismatch");
        }
        // 196 = (13 + 26) * 4 + (14 + 26) * 1
        if (numTotalBytes !=
                ((numDataBytesInGroup1 + numEcBytesInGroup1) *
                        numRsBlocksInGroup1) +
                        ((numDataBytesInGroup2 + numEcBytesInGroup2) *
                                numRsBlocksInGroup2)) {
            throw new WriterException("Total bytes mismatch");
        }

        if (blockID < numRsBlocksInGroup1) {
            numDataBytesInBlock[0] = numDataBytesInGroup1;
            numECBytesInBlock[0] = numEcBytesInGroup1;
        } else {
            numDataBytesInBlock[0] = numDataBytesInGroup2;
            numECBytesInBlock[0] = numEcBytesInGroup2;
        }
    }

    /**
     * Interleave "bits" with corresponding error correction bytes. On success, store the result in
     * "result". The interleave rule is complicated. See 8.6 of JISX0510:2004 (p.37) for details.
     */
    static void interleaveWithECBytes(BitArray bits, int numTotalBytes,
                                      int numDataBytes, int numRSBlocks, BitArray result) throws WriterException {
        //numRSBlocks-RS码块数目,result-交织结束后的编码，bits-数据比特
        // "bits" must have "getNumDataBytes" bytes of data.
    //	System.out.println("bits.getSizeInBytes()"+bits.getSizeInBytes());
        if (bits.getSizeInBytes() != numDataBytes) {
            throw new WriterException("Number of bits and data bytes does not match");
        }

        // Step 1.  Divide data bytes into blocks and generate error correction bytes for them. We'll
        // store the divided data bytes blocks and error correction bytes blocks into "blocks".
        int dataBytesOffset = 0;
        int maxNumDataBytes = 0;
        int maxNumEcBytes = 0;

        // Since, we know the number of reedsolmon blocks, we can initialize the vector with the number.
        Vector blocks = new Vector(numRSBlocks);

        for (int i = 0; i < numRSBlocks; ++i) {
            int[] numDataBytesInBlock = new int[1];//采用数组保证经函数处理后值保留
            int[] numEcBytesInBlock = new int[1];
            getNumDataBytesAndNumECBytesForBlockID(
                    numTotalBytes, numDataBytes, numRSBlocks, i,
                    numDataBytesInBlock, numEcBytesInBlock);//计算数据比特与纠错比特分块

            int size = numDataBytesInBlock[0];
            byte[] dataBytes = new byte[size];
            bits.toBytes(8 * dataBytesOffset, dataBytes, 0, size);
            byte[] ecBytes = generateECBytes(dataBytes, numEcBytesInBlock[0]);//纠错码编码
            blocks.addElement(new BlockPair(dataBytes, ecBytes));

            maxNumDataBytes = Math.max(maxNumDataBytes, size);
            maxNumEcBytes = Math.max(maxNumEcBytes, ecBytes.length);
            dataBytesOffset += numDataBytesInBlock[0];
        }
        if (numDataBytes != dataBytesOffset) {
            throw new WriterException("Data bytes does not match offset");
        }
        // First, place data blocks.开始交织
        for (int i = 0; i < maxNumDataBytes; ++i) {
            for (int j = 0; j < blocks.size(); ++j) {
                byte[] dataBytes = ((BlockPair) blocks.elementAt(j)).getDataBytes();
                if (i < dataBytes.length) {
                    result.appendBits(dataBytes[i], 8);
                }
            }
        }
        // Then, place error correction blocks.
        for (int i = 0; i < maxNumEcBytes; ++i) {
            for (int j = 0; j < blocks.size(); ++j) {
                byte[] ecBytes = ((BlockPair) blocks.elementAt(j)).getErrorCorrectionBytes();
                if (i < ecBytes.length) {
                    result.appendBits(ecBytes[i], 8);
                }
            }
        }
        if (numTotalBytes != result.getSizeInBytes()) {  // Should be same.
            throw new WriterException("Interleaving error: " + numTotalBytes + " and " +
                    result.getSizeInBytes() + " differ.");
        }
    }


    static byte[] generateECBytes(byte[] dataBytes, int numEcBytesInBlock) {//生成纠错比特
        int numDataBytes = dataBytes.length;
        int[] toEncode = new int[numDataBytes + numEcBytesInBlock];
        for (int i = 0; i < numDataBytes; i++) {
            toEncode[i] = dataBytes[i] & 0xFF;
        }
        new ReedSolomonEncoder(GenericGF.QR_CODE_FIELD_256).encode(toEncode, numEcBytesInBlock);

        byte[] ecBytes = new byte[numEcBytesInBlock];
        for (int i = 0; i < numEcBytesInBlock; i++) {
            ecBytes[i] = (byte) toEncode[numDataBytes + i];
        }
        return ecBytes;
    }

    /**
     * Append mode info. On success, store the result in "bits".
     */
    static void appendModeInfo(Mode mode, BitArray bits) {
        bits.appendBits(mode.getBits(), 4);
    }


    /**
     * Append length info. On success, store the result in "bits".
     */
    static void appendLengthInfo(int numLetters, int version, Mode mode, BitArray bits)
            throws WriterException {
        int numBits = mode.getCharacterCountBits(Version.getVersionForNumber(version));
        if (numLetters > ((1 << numBits) - 1)) {
            throw new WriterException(numLetters + "is bigger than" + ((1 << numBits) - 1));
        }
        bits.appendBits(numLetters, numBits);
    }

    /**
     * Append "bytes" in "mode" mode (encoding) into "bits". On success, store the result in "bits".
     */
    static void appendBytes(String content, Mode mode, BitArray bits, String encoding)
            throws WriterException {
        if (mode.equals(Mode.NUMERIC)) {
            appendNumericBytes(content, bits);
        } else if (mode.equals(Mode.ALPHANUMERIC)) {
            appendAlphanumericBytes(content, bits);
        } else if (mode.equals(Mode.BYTE)) {
            append8BitBytes(content, bits, encoding);
        } else if (mode.equals(Mode.KANJI)) {
            appendKanjiBytes(content, bits);
        } else {
            throw new WriterException("Invalid mode: " + mode);
        }
    }

    static void appendNumericBytes(String content, BitArray bits) {
        int length = content.length();
        int i = 0;
        while (i < length) {
            int num1 = content.charAt(i) - '0';
            if (i + 2 < length) {
                // Encode three numeric letters in ten bits.
                int num2 = content.charAt(i + 1) - '0';
                int num3 = content.charAt(i + 2) - '0';
                bits.appendBits(num1 * 100 + num2 * 10 + num3, 10);
                i += 3;
            } else if (i + 1 < length) {
                // Encode two numeric letters in seven bits.
                int num2 = content.charAt(i + 1) - '0';
                bits.appendBits(num1 * 10 + num2, 7);
                i += 2;
            } else {
                // Encode one numeric letter in four bits.
                bits.appendBits(num1, 4);
                i++;
            }
        }
    }

    static void appendAlphanumericBytes(String content, BitArray bits) throws WriterException {
        int length = content.length();
        int i = 0;
        while (i < length) {
            int code1 = getAlphanumericCode(content.charAt(i));
            if (code1 == -1) {
                throw new WriterException();
            }
            if (i + 1 < length) {
                int code2 = getAlphanumericCode(content.charAt(i + 1));
                if (code2 == -1) {
                    throw new WriterException();
                }
                // Encode two alphanumeric letters in 11 bits.
                bits.appendBits(code1 * 45 + code2, 11);
                i += 2;
            } else {
                // Encode one alphanumeric letter in six bits.
                bits.appendBits(code1, 6);
                i++;
            }
        }
    }

    static void append8BitBytes(String content, BitArray bits, String encoding)
            throws WriterException {
        byte[] bytes;
        try {
            bytes = content.getBytes(encoding);
        } catch (UnsupportedEncodingException uee) {
            throw new WriterException(uee.toString());
        }
        for (int i = 0; i < bytes.length; ++i) {
            bits.appendBits(bytes[i], 8);
        }
    }

    static void appendKanjiBytes(String content, BitArray bits) throws WriterException {
        byte[] bytes;
        try {
            bytes = content.getBytes("Shift_JIS");
        } catch (UnsupportedEncodingException uee) {
            throw new WriterException(uee.toString());
        }
        int length = bytes.length;
        for (int i = 0; i < length; i += 2) {
            int byte1 = bytes[i] & 0xFF;
            int byte2 = bytes[i + 1] & 0xFF;
            int code = (byte1 << 8) | byte2;
            int subtracted = -1;
            if (code >= 0x8140 && code <= 0x9ffc) {
                subtracted = code - 0x8140;
            } else if (code >= 0xe040 && code <= 0xebbf) {
                subtracted = code - 0xc140;
            }
            if (subtracted == -1) {
                throw new WriterException("Invalid byte sequence");
            }
            int encoded = ((subtracted >> 8) * 0xc0) + (subtracted & 0xff);
            bits.appendBits(encoded, 13);
        }
    }

    private static void appendECI(ECI eci, BitArray bits) {
        bits.appendBits(Mode.ECI.getBits(), 4);
        // This is correct for values up to 127, which is all we need now.
        bits.appendBits(eci.getValue(), 8);
    }

}
