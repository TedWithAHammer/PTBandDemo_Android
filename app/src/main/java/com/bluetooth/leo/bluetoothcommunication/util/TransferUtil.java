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
            }
        }
        return sb.toString();
    }

    /**
     * hex字符串转byte数组<br/>
     * 2个hex转为一个byte
     *
     * @param src
     * @return
     */
    public static byte[] hex2Bytes(String src) {
        byte[] res = new byte[src.length() / 2];
        char[] chs = src.toCharArray();
        int[] b = new int[2];

        for (int i = 0, c = 0; i < chs.length; i += 2, c++) {
            for (int j = 0; j < 2; j++) {
                if (chs[i + j] >= '0' && chs[i + j] <= '9') {
                    b[j] = (chs[i + j] - '0');
                } else if (chs[i + j] >= 'A' && chs[i + j] <= 'F') {
                    b[j] = (chs[i + j] - 'A' + 10);
                } else if (chs[i + j] >= 'a' && chs[i + j] <= 'f') {
                    b[j] = (chs[i + j] - 'a' + 10);
                }
            }

            b[0] = (b[0] & 0x0f) << 4;
            b[1] = (b[1] & 0x0f);
            res[c] = (byte) (b[0] | b[1]);
        }

        return res;
    }

    private static int toByte(char c) {
        byte b = (byte) "0123456789ABCDEF".indexOf(c);
        return b;
    }
}
