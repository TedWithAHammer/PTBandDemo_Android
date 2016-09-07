package com.bluetooth.leo.bluetoothcommunication.util;

import java.sql.ResultSet;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.SimpleTimeZone;
import java.util.TimeZone;
import java.util.concurrent.CopyOnWriteArrayList;


/**
 * Created by leo on 2016/8/26.
 */

public class CommandUtil {

    //command frame fix_start_tag+len(count from app to lrc)+app(fix "b1")+etf(fix "00")+data(type+content)+etx(fix "03")+lrc(varify the data correctness)

    public class Command {
        public static final String TIME_SYNC = "00";
        public static final String PARAM_SYNC = "01";
        public static final String MOTION_DATA = "02";
        public static final String SLEEP_DATA = "03";
        public static final String MOVE_HEART_RATE = "04";
        public static final String SINGALE_HEART_RATE = "05";
        public static final String CURRENT_TIME_HEART_RATE = "F5";
        public static final String SPECIFIC_TIME_HEART_RATE = "F6";
        public static final String ALARM_DATA = "06";
        public static final String MAKE_FRIENDS = "07";
        public static final String TRANSPARENT_TRANSMISSION = "08";
        public static final String GAME_HONOR_TAG = "09";
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
        switch (command.substring(0, 2)) {
            case Command.TIME_SYNC:
                result.append(Command.TIME_SYNC);
                String utc = Long.toHexString(generateGMTTimeStamp(System.currentTimeMillis() / 1000));
                utc = reverseHex(utc);
                if (utc.length() < 8) {
                    for (int i = 0; i < 8 - utc.length(); i++) {
                        utc = "0" + utc;
                    }
                }
                result.append(utc);
                break;
            case Command.PARAM_SYNC:
                result.append(Command.PARAM_SYNC);

                break;
            case Command.SPECIFIC_TIME_HEART_RATE:
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

    public static long generateGMTTimeStamp(long utc){
        return utc+8*60*60;
    }

//    public static String generateSyncTimeCommand(String ){
//
//    }

}
