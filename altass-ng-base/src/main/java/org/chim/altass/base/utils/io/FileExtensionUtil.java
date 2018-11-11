package org.chim.altass.base.utils.io;

import java.io.FileInputStream;
import java.io.IOException;

/**
 * Class Name: FileExtensionUtil
 * Create Date: 2017/10/31 10:33
 * Creator: Chim·Zigui
 * Version: v1.0
 * Updater:
 * Date Time:
 * Description:
 */
public class FileExtensionUtil {

    /**
     * 将字节专程16进制数
     *
     * @param srcBytes 需要转化的字节
     * @return 转化后的十六进制编码
     */
    public static String bytesToHexString(byte[] srcBytes) {
        StringBuilder stringBuilder = new StringBuilder();
        if (srcBytes == null || srcBytes.length <= 0) {
            return null;
        }

        for (byte srcByte : srcBytes) {
            int v = srcByte & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }

    /**
     * 根据文件流的头几个字节检测文件的扩展名
     *
     * @param header 文件流的偷几个字节对应的十六进制
     * @return 获得文件的扩展名
     */
    public static String checkType(String header) {
        switch (header) {
            case "FFD8FF":
                return "jpg";
            case "89504E":
                return "png";
            case "474946":
                return "gif";

            default:
                return "0000";
        }
    }

    public static void main(String[] args) throws IOException {
        FileInputStream fileInputStream = new FileInputStream("D:\\15309720");
        byte[] b = new byte[3];
        fileInputStream.read(b, 0, b.length);
        String header = bytesToHexString(b);
        header = header.toUpperCase();
        System.out.println("头文件是：" + header);
        String ooo = checkType(header);
        System.out.println("后缀名是：" + ooo);
    }
}
