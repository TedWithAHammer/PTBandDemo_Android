package com.bluetooth.leo.bluetoothcommunication;

import android.content.Context;
import android.content.Intent;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.bluetooth.leo.bluetoothcommunication.util.CommandUtil;
import com.leo.baseadapter.BaseAdapter;
import com.leo.baseadapter.BaseViewHolder;
import com.leo.baseadapter.RecyclerAdapter;
import com.leo.potato.PotatoInjection;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CommandDetailActivity extends BaseActivity {
    @PotatoInjection(idStr = "rvCommands")
    RecyclerView rvCommands;

    public static final int COMMADN_RESULT_CODE=1002;

    @Override
    protected void postInflate() {
        LinearLayoutManager llm=new LinearLayoutManager(this);
        rvCommands.setLayoutManager(llm);
        List<Pair<String,String>> commands=generateCommands();
        Adapter adapter=new Adapter(this,commands,R.layout.item_bluetooth_command_recyclerview);
        adapter.setListener(listener);
        rvCommands.setAdapter(adapter);
    }

    RecyclerViewClickListener listener=new RecyclerViewClickListener() {
        @Override
        public void onItemClick(View v, int position, Pair<String, String> data) {
            Intent intent=new Intent();
            intent.putExtra(BluetoothDetailInfoActivity.COMMAND_CONTENT,data.second);
//            String command=data.second;
//            sw(command.substring(9,11);
            setResult(COMMADN_RESULT_CODE,intent);
            finish();
        }
    };

    private List<Pair<String, String>> generateCommands() {
        List<Pair<String,String>> ret=new ArrayList<>();
        Pair<String,String> syncTime=new Pair<>("同步时间", CommandUtil.generateCommand("0001",new Date()));
        ret.add(syncTime);
//        Pair<String,String> syncParam=new Pair<>("同步参数", CommandUtil.generateCommand("0101"));
        Pair<String,String> moveData=new Pair<>("运动数据", CommandUtil.generateCommand("0201"));
        ret.add(moveData);
        Pair<String,String> sleepData=new Pair<>("睡眠数据", CommandUtil.generateCommand("0301"));
        ret.add(sleepData);
        Pair<String,String> moveHeart=new Pair<>("运动心率", CommandUtil.generateCommand("0401"));
        ret.add(moveHeart);
        Pair<String,String> singleHeart=new Pair<>("单次测心率", CommandUtil.generateCommand("0501"));
        ret.add(singleHeart);
        Pair<String,String> stopCurrentHeart=new Pair<>("关闭实时心率", CommandUtil.generateCommand("F500"));
        ret.add(stopCurrentHeart);
        Pair<String,String> openCurrentHeart=new Pair<>("开启实时心率", CommandUtil.generateCommand("F501"));
        ret.add(openCurrentHeart);
        Pair<String,String> continueCurrentHeart=new Pair<>("继续实时心率", CommandUtil.generateCommand("F502"));
        ret.add(continueCurrentHeart);
//        Pair<String,String> syncTime=new Pair<>("同步时间", CommandUtil.generateCommand("0001"));
//        Pair<String,String> syncTime=new Pair<>("同步时间", CommandUtil.generateCommand("0001"));

        return ret;
    }

    @Override
    protected int inflateRootView() {
        return R.layout.activity_command_detail;
    }

    class Adapter extends RecyclerAdapter<Pair<String,String>>{
        public void setListener(RecyclerViewClickListener listener) {
            this.listener = listener;
        }

        RecyclerViewClickListener listener;

        public Adapter(Context context, List<Pair<String,String>> data, int layoutId) {
            super(context, data, layoutId);
        }

        @Override
        protected void onBind(int viewType, BaseViewHolder holder,final int position,final Pair<String,String> s) {
            super.onBind(viewType, holder, position, s);
            holder.setText(R.id.tvCommandDes,s.first);
            holder.setText(R.id.tvCommandDetail,s.second);
            holder.getItemView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(v,position,s);
                }
            });
        }
    }
    public interface RecyclerViewClickListener{
        void onItemClick(View v,int position,Pair<String,String> data);
    }
}
