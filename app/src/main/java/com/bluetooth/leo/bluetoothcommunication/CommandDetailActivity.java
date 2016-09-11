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
            setResult(COMMAND_RESULT_CODE, intent);
            finish();
        }
    }

    private List<Pair<String, String>> generateCommands() {
        List<Pair<String, String>> ret = new ArrayList<>();
        Pair<String, String> syncTime = new Pair<>("同步时间", CommandUtil.generateCommand(CommandUtil.Command.TIME_SYNC, new Date()));
        ret.add(syncTime);
//        Pair<String,String> syncParam=new Pair<>("同步参数", CommandUtil.generateCommand("0101"));
        Pair<String, String> moveData = new Pair<>("运动数据", CommandUtil.generateCommand(CommandUtil.Command.MOTION_DATA));
        ret.add(moveData);
//        Pair<String,String> sleepData=new Pair<>("睡眠数据", CommandUtil.generateCommand("0301"));
//        ret.add(sleepData);
        Pair<String, String> moveHeart = new Pair<>("获取心率数据", CommandUtil.generateCommand(CommandUtil.Command.HEART_DATA_GET));
        ret.add(moveHeart);
        Pair<String, String> startSingleHeart = new Pair<>("单次测心率", CommandUtil.generateCommand(CommandUtil.Command.SINGALE_HEART_RATE));
        ret.add(startSingleHeart);

        Pair<String, String> stopCurrentHeart = new Pair<>("关闭实时心率", CommandUtil.generateCommand(CommandUtil.Command.STOP_CURRENT_TIME_HEART_RATE));
        ret.add(stopCurrentHeart);
        Pair<String, String> openCurrentHeart = new Pair<>("开启实时心率", CommandUtil.generateCommand(CommandUtil.Command.START_CURRENT_TIME_HEART_RATE));
        ret.add(openCurrentHeart);
        Pair<String, String> continueCurrentHeart = new Pair<>("继续实时心率", CommandUtil.generateCommand(CommandUtil.Command.CONTINUE_CURRENT_TIME_HEART_RATE));
        ret.add(continueCurrentHeart);

        Pair<String, String> deleteSpecificTimeHeart = new Pair<>("删除此时间定时心率", CommandUtil.generateCommand(CommandUtil.Command.DELETE_SPECIFIC_TIME_HEART_RATE, new Date()));
        ret.add(deleteSpecificTimeHeart);
        Pair<String, String> addSpecificTimeHeart = new Pair<>("添加此时间定时心率", CommandUtil.generateCommand(CommandUtil.Command.ADD_SPECIFIC_TIME_HEART_RATE, new Date()));
        ret.add(addSpecificTimeHeart);
        Pair<String, String> deleteAllSpecificTimeHeart = new Pair<>("删除所有时间定时心率", CommandUtil.generateCommand(CommandUtil.Command.DELETE_ALL_SPECIFIC_TIME_HEART_RATE, new Date()));
        ret.add(deleteAllSpecificTimeHeart);
        Pair<String, String> motionWithoutHeart = new Pair<>("运动时不测心率", CommandUtil.generateCommand(CommandUtil.Command.SETTING_MOTION_WITHOUT_HEART_INTERVAL, 1));
        ret.add(motionWithoutHeart);
        Pair<String, String> motionWithHeart = new Pair<>("运动时测心率", CommandUtil.generateCommand(CommandUtil.Command.SETTING_MOTION_WITH_HEART_INTERVAL, 1));
        ret.add(motionWithHeart);

        Pair<String, String> alarm = new Pair<>("起床闹钟设置", CommandUtil.generateCommand(CommandUtil.Command.ALARM_DATA,
                CommandUtil.AlarmOperationType.ADD,
                CommandUtil.AlarmType.GETING_UP,
                CommandUtil.AlarmRepeatType.WEEKDAY,
                new Date()));
        ret.add(alarm);
        Pair<String, String> alarmLearning = new Pair<>("学习闹钟设置", CommandUtil.generateCommand(CommandUtil.Command.ALARM_DATA,
                CommandUtil.AlarmOperationType.ADD,
                CommandUtil.AlarmType.LEARNING,
                CommandUtil.AlarmRepeatType.WEEKDAY,
                new Date()));
        ret.add(alarmLearning);
        Pair<String, String> alarmMotion = new Pair<>("运动闹钟设置", CommandUtil.generateCommand(CommandUtil.Command.ALARM_DATA,
                CommandUtil.AlarmOperationType.ADD,
                CommandUtil.AlarmType.MOTION,
                CommandUtil.AlarmRepeatType.WEEKDAY,
                new Date()));
        ret.add(alarmMotion);
        Pair<String, String> alarmSleeping = new Pair<>("睡觉闹钟设置", CommandUtil.generateCommand(CommandUtil.Command.ALARM_DATA,
                CommandUtil.AlarmOperationType.ADD,
                CommandUtil.AlarmType.SLEEPING,
                CommandUtil.AlarmRepeatType.WEEKDAY,
                new Date()));
        ret.add(alarmSleeping);

        Pair<String, String> alarmDelete = new Pair<>("刪除所有闹钟闹钟设置", CommandUtil.generateCommand(CommandUtil.Command.ALARM_DATA,
                CommandUtil.AlarmOperationType.DELETE_ALL,
                CommandUtil.AlarmType.SLEEPING,
                CommandUtil.AlarmRepeatType.WEEKDAY,
                new Date()));
        ret.add(alarmDelete);
        Pair<String, String> uploadOTC = new Pair<>("固件更新", UPLOAD_OTC);
        ret.add(uploadOTC);
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
