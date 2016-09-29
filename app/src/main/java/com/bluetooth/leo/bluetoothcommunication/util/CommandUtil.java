package com.bluetooth.leo.bluetoothcommunication.util;


import java.util.Date;

/**
 * Created by leo on 2016/8/26.
 */

public class CommandUtil {

    //command frame fix_start_tag+len(count from app to lrc)+app(fix "b1")+etf(fix "00")+data(type+content)+etx(fix "03")+lrc(varify the data correctness)

    public class Command {
        public static final String MOTION_DATA = "0201";//get motion data
        public static final String SLEEP_DATA = "0301";
        public static final String HEART_DATA_GET = "0401";//get heart data(1.motion heart date 2.specific time heart data)
        public static final String SINGLE_HEART_RATE = "0501";// get single heart data
        public static final String STOP_CURRENT_TIME_HEART_RATE = "F500";//current heart data(data part 1."00" stop 2."01" start 3."02" continue)
        public static final String START_CURRENT_TIME_HEART_RATE = "F501";
        public static final String CONTINUE_CURRENT_TIME_HEART_RATE = "F502";
        public static final String DEVICE_VOLTAGE = "0A01";
        public static final String DEVICE_FIRMWARE_VERSION = "0B01";
        public static final String CALORIE = "0C01";
        public static final String DELETE_CACHE_DATA = "0D01";
        public static final String MAKE_FRIENDS = "0701";
        public static final String DELETE_ALL_SPECIFIC_TIME_HEART_RATE = "F602";

        //UTC OR WITH TIME COMMAND
        public static final String TIME_SYNC = "00";//sync time
        public static final String PARAM_SYNC = "01";
        public static final String DELETE_SPECIFIC_TIME_HEART_RATE = "F600";//specific heart data(data part1:(operation type1."00" delete specific time 2."01" add specific time 3."02" delete all time part)data part2.utc timemills)
        public static final String ADD_SPECIFIC_TIME_HEART_RATE = "F601";
        public static final String SETTING_MOTION_WITHOUT_HEART_INTERVAL = "F700";//data part1. (1."00" motion without heart data 2."01" motion with heart data) part 2.interval unites minute
        public static final String SETTING_MOTION_WITH_HEART_INTERVAL = "F701";
        public static final String ALARM_DATA = "06";//alarm setting data part1(1."00" 2."01" 3."02") part2(1."" 2."" 3."" 4."" 5."") part3.(alarm repeat type)
        public static final String SLEEP_TRACE_TIME_SETTING = "F8";
//        public static final String TRANSPARENT_TRANSMISSION = "08";
//        public static final String GAME_HONOR_TAG = "09";
    }

    public class AlarmOperationType {
        public static final String ADD = "00";
        public static final String DELETE = "01";
        public static final String DELETE_ALL = "02";
    }

    public class SleepOperationType {
        public static final String ADD = "01";
        public static final String DELETE = "00";
        public static final String DELETE_ALL = "02";
    }

    public class AlarmType {
        public static final String GETTING_UP = "00";
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
        switch (command) {
            case Command.TIME_SYNC:
                utc = Long.toHexString(generateGMTTimeStamp(((Date) param[0]).getTime() / 1000));
                result.append(reverseHex(utc));
                break;
            case Command.PARAM_SYNC:
                if (param.length > 0) {
                    CharSequence name = (CharSequence) param[0];
                    String hexName = "";
                    for (int i = 0; i < name.length(); i++) {
                        hexName += Integer.toHexString(name.charAt(i));
                    }
                    if (hexName.length() < 8) {
                        int length = hexName.length();
                        for (int j = 0; j < 8 - length; j++) {
                            hexName = "0" + hexName;
                        }
                    }
                    result.append(hexName);
                    String weight = Integer.toHexString((int) param[1]);
                    String height = Integer.toHexString((int) param[2]);
                    result.append(weight);
                    result.append(height);
                    result.append("0000");
                } else {
                    return null;
                }
                break;
            case Command.DELETE_ALL_SPECIFIC_TIME_HEART_RATE:
            case Command.ADD_SPECIFIC_TIME_HEART_RATE:
            case Command.DELETE_SPECIFIC_TIME_HEART_RATE:
                if (param.length > 0) {
                    if (param[0] instanceof Date) {
                        //// TODO: 2016/9/11 toggle after 30 seconds
                        utc = Long.toHexString(generateGMTTimeStamp(((Date) param[0]).getTime() / 1000) + 60);
                        result.append(reverseHex(utc));
                    }
                } else {
                    return null;
                }
                break;
            case Command.SETTING_MOTION_WITH_HEART_INTERVAL:
            case Command.SETTING_MOTION_WITHOUT_HEART_INTERVAL:
                if (param.length > 0) {
                    if (param[0] instanceof Integer) {
                        utc = Integer.toHexString(((Integer) param[0]));
                        if (utc.length() < 4) {
                            int length = utc.length();
                            for (int i = 0; i < 4 - length; i++) {
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
                    result.append(param[0]);//operation type
                    if (param[0].equals(AlarmOperationType.DELETE_ALL))
                        break;
                    result.append(param[1]);//alarm type
                    result.append(param[2]);//alarm time
                    if (param[3] instanceof Date) {
                        utc = Long.toHexString(generateGMTTimeStamp(((Date) param[3]).getTime() / 1000 + 60));
                        result.append(reverseHex(utc));
                    }
                } else {
                    return null;
                }
                break;
            case Command.SLEEP_TRACE_TIME_SETTING:
                if (param.length > 0) {
                    result.append(param[0]);
                    Date date = (Date) param[1];
                    date.setHours(0);
                    date.setMinutes(0);
                    date.setSeconds(0);
                    String hexStartUTC = Integer.toHexString((int) generateGMTTimeStamp(date.getTime() / 1000));
                    date.setHours(24);
                    date.setMinutes(0);
                    date.setSeconds(0);
                    String hexEndUTC = Integer.toHexString((int) generateGMTTimeStamp(date.getTime() / 1000));
                    result.append(reverseHex(hexStartUTC));
                    result.append(reverseHex(hexEndUTC));
                } else {
                    return null;
                }
                break;
        }
        result.append(FIX_END_TAG);
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
