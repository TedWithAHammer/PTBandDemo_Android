package com.bluetooth.leo.bluetoothcommunication.util;


import com.bluetooth.leo.bluetoothcommunication.util.TransferUtil;

import java.util.Date;

/**
 * Created by leo on 2016/8/26.
 */

public class CommandUtil {

    //command frame fix_start_tag+len(count from app to lrc)+app(fix "b1")+etf(fix "00")+data(type+content)+etx(fix "03")+lrc(varify the data correctness)

    public class Command {
        public static final String PARAM_SYNC = "01";
        public static final String MOTION_DATA = "02";//get motion data
        public static final String SLEEP_DATA = "03";
        public static final String HEART_DATA_GET = "04";//get heart data(1.motion heart date 2.specific time heart data)
        public static final String SINGALE_HEART_RATE = "05";// get single heart data
        public static final String STOP_CURRENT_TIME_HEART_RATE = "F500";//current heart data(data part 1."00" stop 2."01" start 3."02" continue)
        public static final String START_CURRENT_TIME_HEART_RATE = "F501";
        public static final String CONTINUE_CURRENT_TIME_HEART_RATE = "F502";
        //UTC OR WITH TIME COMMAND
        public static final String TIME_SYNC = "00";//sync time
        public static final String DELETE_SPECIFIC_TIME_HEART_RATE = "F600";//specific heart data(data part1:(operation type1."00" delete specific time 2."01" add specific time 3."02" delete all time part)data part2.utc timemills)
        public static final String ADD_SPECIFIC_TIME_HEART_RATE = "F601";
        public static final String DELETE_ALL_SPECIFIC_TIME_HEART_RATE = "F602";
        public static final String SETTING_MOTION_WITHOUT_HEART_INTERVAL = "F700";//data part1. (1."00" motion without heart data 2."01" motion with heart data) part 2.interval unites minute
        public static final String SETTING_MOTION_WITH_HEART_INTERVAL = "F701";
        public static final String ALARM_DATA = "06";//alarm setting data part1(1."00" 2."01" 3."02") part2(1."" 2."" 3."" 4."" 5."") part3.(alarm repeat type)
        public static final String MAKE_FRIENDS = "07";
        public static final String TRANSPARENT_TRANSMISSION = "08";
        public static final String GAME_HONOR_TAG = "09";
    }

    public class AlarmOperationType {
        public static final String ADD = "00";
        public static final String DELETE = "01";
        public static final String DELETE_ALL = "02";
    }

    public class AlarmType {
        public static final String GETING_UP = "00";
        public static final String LEARNING = "01";
        public static final String MOTION = "02";
        public static final String SLEEPING = "03";
        public static final String CUSTOM = "04";
    }

    public class AlarmRepeatType {
        public static final String WORKDAY = "F8";
        public static final String WEEKDAY = "06";
    }

    /**
     * STX TAG
     */
    public static final String FIX_START_TAG = "02";
    /**
     * APP TAG ATTACH ETF TAG
     */
    public static final String FIX_APP_ETF_TAG = "B100";
    /**
     * ETX TAG
     */
    public static final String FIX_END_TAG = "03";

    public static String generateVerifyByte(byte[] origin) {
        byte check = 0;
        for (int i = 0; i < origin.length; i++) {
            check = (byte) (check ^ origin[i]);
        }
        return TransferUtil.byte2HexStr(new byte[]{check});
    }

    public static String generateCommand(String command) {
        StringBuilder result = new StringBuilder();
        result.append(FIX_APP_ETF_TAG);
        result.append(command);
        result.append(FIX_END_TAG);
        byte[] temp = new byte[1];
        String lrc = generateVerifyByte(TransferUtil.hex2Bytes1(result.toString()));
        result.append(lrc);
        String len = Integer.toHexString(result.length() / 2);
        if (len.length() == 1) {
            len = "0" + len;
        }
        result.insert(0, len);
        result.insert(0, FIX_START_TAG);
        return result.toString();
    }

    public static String generateCommand(String command, Object... param) {
//        SimpleDateFormat sdf=new SimpleDateFormat(pattern);
        StringBuilder result = new StringBuilder();
        result.append(FIX_APP_ETF_TAG);
        result.append(command);
        String utc = "";
        switch (command.substring(0, 2)) {
            case Command.TIME_SYNC:
                utc = Long.toHexString(generateGMTTimeStamp(System.currentTimeMillis() / 1000));
                result.append(reverseHex(utc));
                break;

            case Command.DELETE_ALL_SPECIFIC_TIME_HEART_RATE:
            case Command.ADD_SPECIFIC_TIME_HEART_RATE:
            case Command.DELETE_SPECIFIC_TIME_HEART_RATE:
                if (param.length > 0) {
                    if (param[0] instanceof Date) {
                        //// TODO: 2016/9/11 toggle after 30 seconds
                        utc = Long.toHexString(generateGMTTimeStamp(((Date) param[0]).getTime() / 1000)+30);
                        result.append(reverseHex(utc));
                    }
                } else {
                    return null;
                }
                break;
            case Command.SETTING_MOTION_WITH_HEART_INTERVAL:
            case Command.SETTING_MOTION_WITHOUT_HEART_INTERVAL:
                if (param.length > 0) {
                    if (param[0] instanceof Long) {
                        utc = Long.toHexString(((long) param[0]));
                        if (utc.length() < 4) {
                            for (int i = 0; i < 4 - utc.length(); i++) {
                                utc = "0" + utc;
                            }
                        }
                        result.append(reverseHex(utc));
                    }
                } else {
                    return null;
                }
                break;
            case Command.ALARM_DATA:
                if (param.length > 3) {
                    result.append(param[0]);
                    result.append(param[1]);
                    result.append(param[2]);
                    if (param[3] instanceof Date) {
                        utc = Long.toHexString(generateGMTTimeStamp(((Date) param[3]).getTime() / 1000));
                        result.append(reverseHex(utc));
                    }
                } else {
                    return null;
                }
            case Command.PARAM_SYNC:
                break;

        }
        result.append(FIX_END_TAG);
        byte[] temp = new byte[1];
        String lrc = generateVerifyByte(TransferUtil.hex2Bytes1(result.toString()));
        result.append(lrc);
        String len = Integer.toHexString(result.length() / 2);
        if (len.length() == 1) {
            len = "0" + len;
        }
        result.insert(0, len);
        result.insert(0, FIX_START_TAG);
        return result.toString();
    }

    private static String reverseHex(String utc) {
        StringBuffer sb = new StringBuffer();
        for (int i = utc.length() - 1; i > -1; i = i - 2) {
            sb.append(utc.substring(i - 1, i + 1));
        }
        return sb.toString();
    }

    public static long generateGMTTimeStamp(long utc) {
        return utc + 8 * 60 * 60;
    }
}
