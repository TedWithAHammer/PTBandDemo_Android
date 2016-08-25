package com.bluetooth.leo.bluetoothcommunication.util;

/**
 * Created by leo on 2016/8/23.
 */

public class TransferUtil {
    public static String byte2HexStr(byte[] origin) {
        StringBuffer sb = new StringBuffer(origin.length);
        String sTemp;
        for (int i = 0; i < origin.length; i++) {
            sTemp = Integer.toHexString(0xFF & origin[i]);
            if (sTemp.length() < 2)
                sb.append(0);
            sb.append(sTemp.toUpperCase());
        }
        return sb.toString();
    }


    /**
     * every 4 chars add "-"
     *
     * @param origin
     * @return
     */
    public static String byte2SpecificFormatHexStr(byte[] origin) {
        StringBuffer sb = new StringBuffer();
        String sTemp;
        for (int i = 0; i < origin.length; i++) {
            sTemp = Integer.toHexString(0xFF & origin[i]);
            if (sTemp.length() < 2)
                sb.append(0);
            sb.append(sTemp.toUpperCase());
            if (i % 2 == 1 && i != origin.length - 1) {
                sb.append("-");
            } else if (i == origin.length - 1) {
                sb.append("^e");
            }
        }
        return sb.toString();
    }

    public static byte[] hex2Byte(String hexStr) {
        int len = (hexStr.length() / 2);
        byte[] result = new byte[len];
        char[] achar = hexStr.toCharArray();
        for (int i = 0; i < len; i++) {
            int pos = i * 2;
            result[i] = (byte) (toByte(achar[pos]) << 4 | toByte(achar[pos + 1]));
        }
        return result;
    }

    private static int toByte(char c) {
        byte b = (byte) "0123456789ABCDEF".indexOf(c);
        return b;
    }
}
