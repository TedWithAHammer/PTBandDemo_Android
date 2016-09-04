package com.bluetooth.leo.bluetoothcommunication.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by leo on 2016/9/4.
 */

public class DataDeSerializationUtil {

    private static String pattern = "yyyy-MM-dd HH:mm:ss";

    public static String deSerializeData(byte[] origin) {
        String result = "";
        switch (origin[4]) {
            case 2:
                result = deSerializationMoveData(origin);
                break;
            case 3:
                break;
            case 4:
                result = deSerializeMoveHeartData(origin);
                break;
            case 5:
                result = deSerializeSingleHeartData(origin);
                break;
            case -11:
                result = deSerializeCurrentHeartData(origin);
                break;
        }
        return result;
    }

    public static String deSerializationSleepData(byte[] origin) {
        StringBuilder sb = new StringBuilder();
        if (origin.length < 12)
            return null;
        byte[] utcByte = new byte[]{
                origin[5], origin[6],
                origin[7], origin[8]
        };
        String utcTime = formatTimeMills(reverseByteArray2String(utcByte));
        sb.append("Time:" + utcTime);
        switch (origin[9]) {
            case 0:
                sb.append(" status:熟睡");
                break;
            case 1:
                sb.append(" status:睡着");
                break;
            case 2:
                sb.append(" status:醒着");
                break;
        }
        return sb.toString();
    }

    /**
     * de
     *
     * @param origin
     * @return
     */
    public static String deSerializationMoveData(byte[] origin) {
        StringBuilder sb = new StringBuilder();
        if (origin.length < 16)
            return null;
        byte[] utcByte = new byte[]{
                origin[5], origin[6],
                origin[7], origin[8]
        };
        String utcTime = formatTimeMills(reverseByteArray2String(utcByte));
        sb.append("Time:" + utcTime);

        byte[] steps = new byte[]{
                origin[9], origin[10],
                origin[11], origin[12]
        };
        String hexStep = reverseByteArray2String(steps);
        String intStep = Integer.valueOf(hexStep, 16) + "";
        sb.append(" step:" + intStep);
        String hexStatus = TransferUtil.byte2HexStr(new byte[]{origin[13]});
        sb.append(" status:" + hexStatus);
        return sb.toString();
    }

    public static String deSerializeMoveHeartData(byte[] origin) {
        StringBuilder sb = new StringBuilder();
        byte[] utcByte = new byte[]{
                origin[5], origin[6],
                origin[7], origin[8]
        };
        String utcTime = formatTimeMills(reverseByteArray2String(utcByte));
        sb.append("Time:" + utcTime);
        sb.append("Heart rate:" + (origin[9] & 0xff));
        sb.append("status:" + (origin[10] == 1 ? "走" : "跑"));
        return sb.toString();
    }

    public static String deSerializeSingleHeartData(byte[] origin) {
        return "Single Heart Rate:" + (origin[5] & 0xff);
    }

    public static String deSerializeCurrentHeartData(byte[] origin) {
        return "Current Heart Rate:" + (origin[5] & 0xff);
    }


    public static String formatTimeMills(String mills) {
        long timeMills = Long.valueOf(mills, 16) * 1000;
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
//        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date date = new Date(timeMills);
        return sdf.format(date);
    }

    public static String reverseByteArray2String(byte[] origin) {
        StringBuilder sb = new StringBuilder();
        for (int i = origin.length - 1; i > -1; i--) {
            String hexStr = Integer.toHexString(origin[i] & 0xff);
            if (hexStr.length() == 1) {
                hexStr = "0" + hexStr;
            }
            sb.append(hexStr);
        }
        return sb.toString();
    }
}
