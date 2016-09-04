package com.bluetooth.leo.bluetoothcommunication.util;

import java.sql.ResultSet;

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
        public static final String HEART_RATE_DATA = "04";
        public static final String SINGALE_HEART_RATE_TEST = "05";
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

    public static String generateCommand(String type) {
        StringBuilder result = new StringBuilder();
        result.append(FIX_APP_ETF_TAG);
        result.append(type);
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


}
