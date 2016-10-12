package com.bluetooth.leo.bluetoothcommunication;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.util.Pair;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.bluetooth.leo.bluetoothcommunication.util.CommandUtil;
import com.leo.baseadapter.BaseViewHolder;
import com.leo.baseadapter.RecyclerAdapter;
import com.leo.potato.PotatoInjection;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CommandDetailActivity extends BaseActivity {
    @PotatoInjection(idStr = "rvCommands")
    RecyclerView rvCommands;

    public static final int COMMAND_RESULT_CODE = 1002;
    public static final int FILE_CHOOSE_CALLBACK = 1003;
    public static final String UPLOAD_OTC = "upload_otc";

    @Override
    protected void postInflate() {
        LinearLayoutManager llm = new LinearLayoutManager(this);
        rvCommands.setLayoutManager(llm);
        List<Pair<String, String>> commands = generateCommands();
        Adapter adapter = new Adapter(this, commands, R.layout.item_bluetooth_command_recyclerview);
        adapter.setListener(listener);
        rvCommands.setAdapter(adapter);
    }

    RecyclerViewClickListener listener = new RecyclerViewClickListener() {
        @Override
        public void onItemClick(View v, int position, Pair<String, String> data) {

            Intent intent = new Intent();
            if (data.second.equals(UPLOAD_OTC)) {
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("zip/*");
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                startActivityForResult(intent, FILE_CHOOSE_CALLBACK);
                return;
            }
            intent.putExtra(BluetoothDetailInfoActivity.COMMAND_CONTENT, data.second);
            intent.putExtra(BluetoothDetailInfoActivity.COMMAND_NAME, data.first);
            setResult(COMMAND_RESULT_CODE, intent);
            finish();
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == FILE_CHOOSE_CALLBACK) {
            Intent intent = new Intent();
            Uri uri = data.getData();
            intent.putExtra(BluetoothDetailInfoActivity.COMMAND_CONTENT, uri.toString());
            intent.putExtra(BluetoothDetailInfoActivity.COMMAND_NAME, "OTA升級");
            setResult(COMMAND_RESULT_CODE, intent);
            finish();
        }
    }

    private List<Pair<String, String>> generateCommands() {
        List<Pair<String, String>> ret = new ArrayList<>();
        Pair<String, String> syncTime = new Pair<>("同步时间", CommandUtil.generateCommand(CommandUtil.Command.TIME_SYNC, new Date()));
        ret.add(syncTime);
        Pair<String, String> syncParam = new Pair<>("同步参数", CommandUtil.generateCommand(CommandUtil.Command.PARAM_SYNC, "wly", 80, 180));
        ret.add(syncParam);
        Pair<String, String> moveData = new Pair<>("运动数据", CommandUtil.generateCommand(CommandUtil.Command.MOTION_DATA));
        ret.add(moveData);
        Pair<String, String> sleepData = new Pair<>("睡眠数据", CommandUtil.generateCommand(CommandUtil.Command.SLEEP_DATA));
        ret.add(sleepData);
        Pair<String, String> sleepTimeSetting = new Pair<>("睡眠检测时间设定(00:00-24:00)", CommandUtil.generateCommand(CommandUtil.Command.SLEEP_TRACE_TIME_SETTING, CommandUtil.SleepOperationType.ADD, new Date(), new Date()));
        ret.add(sleepTimeSetting);
        Pair<String, String> moveHeart = new Pair<>("获取心率数据", CommandUtil.generateCommand(CommandUtil.Command.HEART_DATA_GET));
        ret.add(moveHeart);
        Pair<String, String> startSingleHeart = new Pair<>("单次测心率", CommandUtil.generateCommand(CommandUtil.Command.SINGLE_HEART_RATE));
        ret.add(startSingleHeart);

        Pair<String, String> stopCurrentHeart = new Pair<>("关闭实时心率", CommandUtil.generateCommand(CommandUtil.Command.STOP_CURRENT_TIME_HEART_RATE));
        ret.add(stopCurrentHeart);
        Pair<String, String> openCurrentHeart = new Pair<>("开启实时心率", CommandUtil.generateCommand(CommandUtil.Command.START_CURRENT_TIME_HEART_RATE));
        ret.add(openCurrentHeart);
        Pair<String, String> continueCurrentHeart = new Pair<>("继续实时心率", CommandUtil.generateCommand(CommandUtil.Command.CONTINUE_CURRENT_TIME_HEART_RATE));
        ret.add(continueCurrentHeart);

        Pair<String, String> deleteSpecificTimeHeart = new Pair<>("删除定时心率", CommandUtil.generateCommand(CommandUtil.Command.DELETE_SPECIFIC_TIME_HEART_RATE, new Date()));
        ret.add(deleteSpecificTimeHeart);
        Pair<String, String> addSpecificTimeHeart = new Pair<>("添加定时心率", CommandUtil.generateCommand(CommandUtil.Command.ADD_SPECIFIC_TIME_HEART_RATE, new Date()));
        ret.add(addSpecificTimeHeart);
        Pair<String, String> deleteAllSpecificTimeHeart = new Pair<>("删除所有定时心率", CommandUtil.generateCommand(CommandUtil.Command.DELETE_ALL_SPECIFIC_TIME_HEART_RATE));
        ret.add(deleteAllSpecificTimeHeart);
        Pair<String, String> makeFriends = new Pair<>("获取交友数据", CommandUtil.generateCommand(CommandUtil.Command.MAKE_FRIENDS));
        ret.add(makeFriends);

        Pair<String, String> alarm = new Pair<>("工作日起床闹钟设置", CommandUtil.generateCommand(CommandUtil.Command.ALARM_DATA,
                CommandUtil.AlarmOperationType.ADD,
                CommandUtil.AlarmType.GETTING_UP,
                CommandUtil.AlarmRepeatType.WORKDAY,
                new Date()));
        ret.add(alarm);
        Pair<String, String> alarmLearning = new Pair<>("工作日学习闹钟设置", CommandUtil.generateCommand(CommandUtil.Command.ALARM_DATA,
                CommandUtil.AlarmOperationType.ADD,
                CommandUtil.AlarmType.LEARNING,
                CommandUtil.AlarmRepeatType.WORKDAY,
                new Date()));
        ret.add(alarmLearning);
        Pair<String, String> alarmMotion = new Pair<>("工作日运动闹钟设置", CommandUtil.generateCommand(CommandUtil.Command.ALARM_DATA,
                CommandUtil.AlarmOperationType.ADD,
                CommandUtil.AlarmType.MOTION,
                CommandUtil.AlarmRepeatType.WORKDAY,
                new Date()));
        ret.add(alarmMotion);
        Pair<String, String> alarmSleeping = new Pair<>("工作日睡觉闹钟设置", CommandUtil.generateCommand(CommandUtil.Command.ALARM_DATA,
                CommandUtil.AlarmOperationType.ADD,
                CommandUtil.AlarmType.SLEEPING,
                CommandUtil.AlarmRepeatType.WORKDAY,
                new Date()));
        ret.add(alarmSleeping);
        Pair<String, String> alarm1 = new Pair<>("双休日起床闹钟设置", CommandUtil.generateCommand(CommandUtil.Command.ALARM_DATA,
                CommandUtil.AlarmOperationType.ADD,
                CommandUtil.AlarmType.GETTING_UP,
                CommandUtil.AlarmRepeatType.WEEKDAY,
                new Date()));
        ret.add(alarm1);
        Pair<String, String> alarmLearning1 = new Pair<>("双休日学习闹钟设置", CommandUtil.generateCommand(CommandUtil.Command.ALARM_DATA,
                CommandUtil.AlarmOperationType.ADD,
                CommandUtil.AlarmType.LEARNING,
                CommandUtil.AlarmRepeatType.WEEKDAY,
                new Date()));
        ret.add(alarmLearning1);
        Pair<String, String> alarmMotion1 = new Pair<>("双休日运动闹钟设置", CommandUtil.generateCommand(CommandUtil.Command.ALARM_DATA,
                CommandUtil.AlarmOperationType.ADD,
                CommandUtil.AlarmType.MOTION,
                CommandUtil.AlarmRepeatType.WEEKDAY,
                new Date()));
        ret.add(alarmMotion1);
        Pair<String, String> alarmSleeping1 = new Pair<>("双休日睡觉闹钟设置", CommandUtil.generateCommand(CommandUtil.Command.ALARM_DATA,
                CommandUtil.AlarmOperationType.ADD,
                CommandUtil.AlarmType.SLEEPING,
                CommandUtil.AlarmRepeatType.WEEKDAY,
                new Date()));
        ret.add(alarmSleeping1);

        Pair<String, String> alarmDelete = new Pair<>("刪除所有闹钟", CommandUtil.generateCommand(CommandUtil.Command.ALARM_DATA,
                CommandUtil.AlarmOperationType.DELETE_ALL,
                CommandUtil.AlarmType.SLEEPING,
                CommandUtil.AlarmRepeatType.WORKDAY,
                new Date()));
        ret.add(alarmDelete);
        Pair<String, String> uploadOTC = new Pair<>("固件更新", UPLOAD_OTC);
        ret.add(uploadOTC);
        Pair<String, String> voltageAchieve = new Pair<>("电压获取", CommandUtil.generateCommand(CommandUtil.Command.DEVICE_VOLTAGE));
        ret.add(voltageAchieve);
        Pair<String, String> versionAchieve = new Pair<>("版本获取", CommandUtil.generateCommand(CommandUtil.Command.DEVICE_FIRMWARE_VERSION));
        ret.add(versionAchieve);
        Pair<String, String> deleteCache = new Pair<>("删除缓存", CommandUtil.generateCommand(CommandUtil.Command.DELETE_CACHE_DATA));
        ret.add(deleteCache);
        Pair<String, String> reset = new Pair<>("回复出场设置", CommandUtil.generateCommand(CommandUtil.Command.RESET));
        ret.add(reset);
        Pair<String, String> deleteMotionData = new Pair<>("清除运动数据", CommandUtil.generateCommand(CommandUtil.Command.CLEAR_MOTION_DATA));
        ret.add(deleteMotionData);
        Pair<String, String> deleteSleepData = new Pair<>("清除睡眠数据", CommandUtil.generateCommand(CommandUtil.Command.CLEAR_SLEEP_DATA));
        ret.add(deleteSleepData);
        Pair<String, String> deleteHeartData = new Pair<>("清除心率数据", CommandUtil.generateCommand(CommandUtil.Command.CLEAR_HEART_DATA));
        ret.add(deleteHeartData);
        Pair<String, String> deleteFriendData = new Pair<>("清除交友数据", CommandUtil.generateCommand(CommandUtil.Command.CLEAR_FRIEND_DATA));
        ret.add(deleteFriendData);
        Pair<String, String> bindDevice = new Pair<>("绑定手环", CommandUtil.generateCommand(CommandUtil.Command.BIND_DEVICE));
        ret.add(bindDevice);
        Pair<String, String> unBindDevice = new Pair<>("解绑手环", CommandUtil.generateCommand(CommandUtil.Command.UNBIND_DEVICE));
        ret.add(unBindDevice);
        Pair<String, String> lightScreen = new Pair<>("开启抬手亮屏", CommandUtil.generateCommand(CommandUtil.Command.LIGHT_SCREEN));
        ret.add(lightScreen);
        Pair<String, String> unlightScreen = new Pair<>("关闭抬手亮屏", CommandUtil.generateCommand(CommandUtil.Command.UNLIGHT_SCREEN));
        ret.add(unlightScreen);
        Pair<String, String> checkLightScreenStatus = new Pair<>("查询抬手亮屏状态", CommandUtil.generateCommand(CommandUtil.Command.CHECK_LIGHT_SCREEN));
        ret.add(checkLightScreenStatus);
        return ret;
    }

    @Override
    protected int inflateRootView() {
        return R.layout.activity_command_detail;
    }

    class Adapter extends RecyclerAdapter<Pair<String, String>> {
        public void setListener(RecyclerViewClickListener listener) {
            this.listener = listener;
        }

        RecyclerViewClickListener listener;

        public Adapter(Context context, List<Pair<String, String>> data, int layoutId) {
            super(context, data, layoutId);
        }

        @Override
        protected void onBind(int viewType, BaseViewHolder holder, final int position, final Pair<String, String> s) {
            super.onBind(viewType, holder, position, s);
            holder.setText(R.id.tvCommandDes, s.first);
            holder.setText(R.id.tvCommandDetail, s.second);
            holder.getItemView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(v, position, s);
                }
            });
        }
    }

    public interface RecyclerViewClickListener {
        void onItemClick(View v, int position, Pair<String, String> data);
    }
}
