package com.bluetooth.leo.bluetoothcommunication.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by leo on 2016/9/4.
 */

public class DataDeSerializationUtil {

    private static String pattern = "yyyy-MM-dd HH:mm:ss";

    private static int[] voltageRangeArray = new int[]{
            4140, 4111, 4101, 4092, 4083, 4073, 4065, 4056, 4040, 4033,
            4026, 4019, 4012, 4004, 3996, 3987, 3979, 3970, 3954, 3946,
            3940, 3932, 3926, 3919, 3913, 3907, 3901, 3895, 3884, 3878,
            3873, 3867, 3862, 3856, 3851, 3846, 3841, 3835, 3825, 3820,
            3816, 3811, 3807, 3802, 3798, 3794, 3790, 3786, 3779, 3775,
            3772, 3768, 3765, 3762, 3759, 3756, 3753, 3751, 3746, 3743,
            3741, 3739, 3737, 3735, 3733, 3731, 3730, 3728, 3725, 3723,
            3722, 3720, 3719, 3717, 3715, 3714, 3712, 3710, 3705, 3703,
            3698, 3695, 3692, 3688, 3684, 3680, 3676, 3667, 3662, 3656,
            3650, 3644, 3638, 3631, 3625, 3619, 3612, 3596, 3585, 3570,
            3550
    };      //the range of voltage to calculate the percentage of battery

    public static String deSerializeData(byte[] origin) {
        String result = "";
        switch (origin[4] & 0xff) {
            case 0:
                result = deserializeTimeSyncData(origin);
                break;
            case 1:
                result = deserializeParamSyncData(origin);
                break;
            case 2:
                result = deSerializationMotionData(origin);
                break;
            case 3:
                break;
            case 4:
                result = deSerializeMoveHeartData(origin);
                break;
            case 5:
                result = deSerializeSingleHeartData(origin);
                break;
            case 245:
                result = deSerializeCurrentHeartData(origin);
                break;
            case 246:
                result = deSerializeSpecificHeartData(origin);
                break;
            case 6:
                result = deSerializeAlarmData(origin);
                break;
            case 10:
                result = deSerializeVoltageData(origin);
                break;
            case 11:
                result = deSerializationFirmwareVersion(origin);
                break;
            case 12:
                result = deserializeCalorieData(origin);
                break;
        }
        return result;
    }

    /**
     * param sync result
     *
     * @param origin
     * @return
     */
    private static String deserializeParamSyncData(byte[] origin) {
        if (origin[5] == 1)
            return "success";
        else
            return "failed";
    }

    private static String deserializeCalorieData(byte[] origin) {
        byte[] calorie = new byte[]{
                origin[5], origin[6],
                origin[7], origin[8]
        };
        String hex = TransferUtil.byte2HexStr(origin);
        int calorieNum = Integer.valueOf(hex, 16);
        return "calorie " + calorieNum;
    }

    private static String deSerializationFirmwareVersion(byte[] origin) {
        int version = origin[11] & 0xff;
        return "version :" + version;
    }

    private static String deSerializeVoltageData(byte[] origin) {
        byte[] voltageByte = new byte[]{
                origin[5], origin[6]
        };
        String originHex=TransferUtil.byte2HexStr(origin);
        String hexStr = TransferUtil.byte2HexStr(voltageByte);
        int voltage = Integer.valueOf(hexStr, 16)/1000;
        return "VOLTAGE "+voltage+"V";
    }

    private static String analyseVoltagePecentage(int voltage) {
        for (int i = 0; i < voltageRangeArray.length - 1; i++) {
            if (voltage < voltageRangeArray[i] && voltage > voltageRangeArray[i + 1]) {
                return "voltage:" + (100 - i) + "%";
            }
        }
        return "voltage:0%";
    }

    private static String deSerializeAlarmData(byte[] origin) {
        if (origin[5] == 1) {
            return "收到，正常开始设置闹钟流程";
        } else {
            return "收到，但不处理：超出闹钟设置最大个数";
        }
    }

    private static String deSerializeSpecificHeartData(byte[] origin) {
        if (origin[6] == 1) {
            return "收到，正常开始设置定时测心率流程";
        } else {
            return "收到，但不处理：超出定时测心率的最大个数";
        }
    }

    private static String deserializeTimeSyncData(byte[] origin) {
        if (origin.length < 8)
            return null;
        if (origin[5] == 1) {
            return "success";
        } else {
            return "failed";
        }
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
    public static String deSerializationMotionData(byte[] origin) {
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
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
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
