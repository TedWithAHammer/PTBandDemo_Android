package com.bluetooth.leo.bluetoothcommunication.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by leo on 2016/9/4.
 */

public class DataDeSerializationUtil {

    private static String pattern = "yyyy-MM-dd HH:mm:ss";

    private static double[] voltageRangeArray = new double[]{
            4183.4, 4160.7, 4146.5, 4134.4, 4124.8, 4113.3, 4102.8, 4093.1, 4083.2, 4073.3,
            4065.2, 4055.6, 4048.2, 4041.4, 4035.8, 4027.4, 4015.9, 4004.2, 3989.6, 3977.2,
            3968.2, 3959.8, 3953, 3948.1, 3943.7, 3939.4, 3934.1, 3929.1, 3922.6, 3915.8,
            3909, 3901.5, 3894.1, 3887, 3880.8, 3873.3, 3866.5, 3860.6, 3854.7, 3847.9,
            3842.6, 3837.4, 3832.1, 3826.5, 3822.2, 3817.5, 3809.2, 3804.8, 3800.8, 3797.4,
            3793.4, 3789.9, 3786.5, 3783.4, 3780.3, 3776.9, 3774.1, 3770.7, 3768.2, 3765.8,
            3763, 3760.2, 3756.8, 3754.6, 3752.1, 3750, 3747.5, 3745, 3742.5, 3740,
            3737.5, 3734.4, 3731, 3728.6, 3725.1, 3721.4, 3718.6, 3714.9, 3710.9, 3706.5,
            3701.9, 3696.6, 3690.4, 3685.5, 3679.3, 3671.8, 3667.5, 3664.7, 3662.2, 3660,
            3655.7, 3651.1, 3642.1, 3626.3, 3592.1, 3543.5, 3489.2, 3408.9, 3283.7, 3018.9,
            2998.8
    };
    /*the range of voltage to calculate the percentage of battery*/

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
                result = deSerializeMotionData(origin);
                break;
            case 3:
                result = deSerializeSleepData(origin);
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
            case 7:
                result = deSerializeFriendData(origin);
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
            case 15:
                result = deserializeScreenData(origin);
                break;
            case 16:
                result = deserializeExistSettingData(origin);
                break;
            case 17:
                result = deserializeUnresponseCommand(origin);
                break;
        }
        return result;
    }

    private static String deserializeUnresponseCommand(byte[] origin) {
        StringBuilder sb = new StringBuilder();
        int command = origin[5] & 0xff;
        switch (command) {
            case 0:
                sb.append("同步时间");
                break;
            case 1:
                sb.append("同步参数");
                break;
            case 2:
                sb.append("运动数据");
                break;
            case 3:
                sb.append("睡眠数据");
                break;
            case 4:
                sb.append("心率数据");
                break;
            case 5:
                sb.append("单次心率");
                break;
            case 6:
                sb.append("闹钟");
                break;
            case 7:
                sb.append("NFC交友y");
                break;
            case 8:
                sb.append("透传");
                break;
            case 9:
                sb.append("游戏荣耀标志");
                break;
            case 10:
                sb.append("电压值");
                break;
            case 11:
                sb.append("固件版本号");
                break;
            case 12:
                sb.append("卡路里消耗");
                break;
            case 13:
                sb.append("清楚数据");
                break;
            case 14:
                sb.append("绑定，解绑");
                break;
            case 15:
                sb.append("亮屏");
                break;
            case 16:
                sb.append("已设置数据获取");
                break;
            case 17:
                sb.append("未响应指令");
                break;
            case 245:
                sb.append("实时心率");
                break;
            case 246:
                sb.append("定时测心率");
                break;
            case 247:
                sb.append("设定运动心率间隔");
                break;
            case 248:
                sb.append("设定睡眠时间间隔");
                break;
        }
        int temp6 = origin[6] & 0xff;
        sb.append(temp6 == 0 ? " 未响应" : " 响应");
        sb.append(" 原因:");
        int temp7 = origin[7] & 0xff;
        switch (temp7) {
            case 0:
                sb.append(temp6 == 0 ? "手机处于关机提醒状态" : "关机提醒中断");
                break;
            case 1:
                sb.append(temp6 == 0 ? "手环处于低电量状态" : "低电量中断");
                break;
            case 2:
                sb.append(temp6 == 0 ? "手环处于充电状态" : "充电中断");
                break;
            case 3:
                sb.append(temp6 == 0 ? "手环处于rawdata状态" : "rawdata中断");
                break;
            case 4:
                sb.append(temp6 == 0 ? "手环处于蓝牙交友状态" : "蓝牙交友中断");
                break;
            case 5:
                sb.append(temp6 == 0 ? "手环处于心率测试状态" : "心率测试中断");
                break;
            case 6:
                sb.append(temp6 == 0 ? "手环处于闹钟提醒状态" : "闹钟提醒中断");
                break;
            case 255:
                sb.append(temp6 == 0 ? "手环处于未知状态" : "未知中断");
                break;
        }
        return sb.toString();
    }

    private static String deserializeExistSettingData(byte[] origin) {
        StringBuilder sb = new StringBuilder();
        if (origin.length <= 9 && (origin[6] & 0xff) == 255) {
            if ((origin[5] & 0xff) == 0)
                sb.append("闹钟数据为空");
            if ((origin[5] & 0xff) == 1)
                sb.append("定时心率设置为空");
            if ((origin[5] & 0xff) == 2)
                sb.append("睡眠监测时间段为空");
        } else {
            /*0 alrm data |1 heart data|2 sleep data */
            if ((origin[5] & 0xff) == 0) {
                sb.append("闹钟");
                int temp6 = origin[6] & 0xff;
                String repeat = Integer.toBinaryString(temp6);
                sb.append("重复时间:" + repeat);
                int temp7 = origin[7] & 0xff;
                switch (temp6) {
                    case 0:
                        sb.append(" 起床提醒");
                        break;
                    case 1:
                        sb.append(" 学习提醒");
                        break;
                    case 2:
                        sb.append(" 运动提醒");
                        break;
                    case 3:
                        sb.append(" 睡觉提醒");
                        break;
                    case 4:
                        sb.append(" 自定义提醒");
                        break;
                }
                int temp8 = origin[8] & 0xff;
                sb.append(" 时间:" + temp8 + "时");
                int temp9 = origin[9] & 0xff;
                sb.append(" " + temp9 + "分");
            } else if ((origin[5] & 0xff) == 1) {
                sb.append("心率");
                int temp6 = origin[6] & 0xff;
                sb.append(" 时间:" + temp6 + "时");
                int temp7 = origin[7] & 0xff;
                sb.append(" " + temp7 + "分");
            } else if ((origin[5] & 0xff) == 2) {
                sb.append("睡眠时间检测");
                int temp6 = origin[6] & 0xff;
                sb.append(" 开始时间:" + temp6 + "时");
                int temp7 = origin[7] & 0xff;
                sb.append(" " + temp7 + "分");
                int temp8 = origin[8] & 0xff;
                sb.append(" 结束时间:" + temp8 + "时");
                int temp9 = origin[9] & 0xff;
                sb.append(" " + temp9 + "分");
            }
        }
        return sb.toString();
    }

    private static String deserializeScreenData(byte[] origin) {
        switch (origin[5] & 0xff) {
            case 241:
                return (origin[6] & 0xff) == 1 ? "success" : "fail";
            case 242:
                if ((origin[6] & 0xff) == 1)
                    return "screen status:on";
                else
                    return "screen status:off";
            default:
                return null;
        }
    }

    private static String deSerializeSleepData(byte[] origin) {
        if (origin.length < 12)
            return null;
        byte[] startUTCArray = new byte[]{
                origin[5], origin[6],
                origin[7], origin[8]
        };
        String startUTC = "start time:" + formatTimeMills(reverseByteArray2String(startUTCArray));
        String status = "status: " + (origin[9] & 0xff);
        return startUTC + status;
    }

    public static String sleepTimeTest(byte[] origin) {
        byte[] startUTCArray = new byte[]{
                origin[5], origin[6],
                origin[7], origin[8]
        };
        String startUTC = reverseByteArray2String(startUTCArray);
        long utcTime = Long.valueOf(startUTC, 16);
        utcTime -= 8 * 60 * 60;
        Date date = new Date(utcTime * 1000);
        return date.toString();
    }

    private static String deSerializeFriendData(byte[] origin) {
        if (origin.length < 20)
            return null;
        StringBuilder sb = new StringBuilder();
        byte[] macByteArray = new byte[]{
                origin[5], origin[6],
                origin[7], origin[8],
                origin[9], origin[10]
        };
        String macHex = reverseByteArray2String(macByteArray);
        sb.append("MAC:" + macHex);
        String day = (origin[11] & 0xff) + "";
        sb.append("DAYS:" + day);
        byte[] timesByteArray = new byte[]{
                origin[12], origin[13]
        };
        String hex = reverseByteArray2String(timesByteArray);
        int times = Integer.valueOf(hex, 16);
        sb.append("TIMES:" + times);
        byte[] utcByteArray = new byte[]{
                origin[14],
                origin[15], origin[16],
                origin[17]
        };
        String utcHex = formatTimeMills(reverseByteArray2String(utcByteArray));
        sb.append("UTC:" + utcHex);
        return sb.toString();
    }

    /**
     * param sync result
     *
     * @param origin
     * @return
     */
    private static String deserializeParamSyncData(byte[] origin) {
        if (origin.length < 8)
            return null;
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
        if (origin.length < 14)
            return null;
        int version = origin[11] & 0xff;
        return "version :" + version;
    }

    private static String deSerializeVoltageData(byte[] origin) {
        if (origin.length < 9)
            return null;
        byte[] voltageByte = new byte[]{
                origin[5], origin[6]
        };
        String originHex = TransferUtil.byte2HexStr(origin);
        String hexStr = reverseByteArray2String(voltageByte);
        int voltage = Integer.valueOf(hexStr, 16) / 10;
        double percise = (double) voltage;
        return "voltage:" + percise + " percentage:" + analyseVoltagePecentage(voltage);
    }

    private static String analyseVoltagePecentage(int voltage) {
        for (int i = 0; i < voltageRangeArray.length - 1; i++) {
            if (voltage < voltageRangeArray[i] && voltage > voltageRangeArray[i + 1]) {
                return "voltage:" + (100 - i) + "%";
            }
        }
        return "0%";
    }


    private static String deSerializeAlarmData(byte[] origin) {
        if (origin.length < 8)
            return null;
        if (origin[5] == 1) {
            return "收到，正常开始设置闹钟流程";
        } else {
            return "收到，但不处理：超出闹钟设置最大个数";
        }
    }

    private static String deSerializeSpecificHeartData(byte[] origin) {
        if (origin.length < 8)
            return null;
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
        switch (origin[9] & 0xff) {
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
    public static String deSerializeMotionData(byte[] origin) {
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
        if (origin.length < 13)
            return null;
        StringBuilder sb = new StringBuilder();
        byte[] utcByte = new byte[]{
                origin[5], origin[6],
                origin[7], origin[8]
        };
        String utcTime = formatTimeMills(reverseByteArray2String(utcByte));
        sb.append("Time:" + utcTime);
        sb.append("Heart rate:" + (origin[9] & 0xff));
        sb.append("status:" + (origin[10] & 0xff));
        return sb.toString();
    }

    public static String deSerializeSingleHeartData(byte[] origin) {
        if (origin.length < 8)
            return null;
        if ((origin[5] & 0xff) == 255)
            return "Single heart measure start";
        return "Single Heart Rate:" + (origin[5] & 0xff);
    }

    public static String deSerializeCurrentHeartData(byte[] origin) {
        if (origin.length < 8)
            return null;
        return "Current Heart Rate:" + (origin[5] & 0xff);
    }


    public static String formatTimeMills(String mills) {
        long timeMills = (Long.valueOf(mills, 16) - 8 * 60 * 60) * 1000;
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
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
