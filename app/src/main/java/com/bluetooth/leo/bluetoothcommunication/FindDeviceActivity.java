package com.bluetooth.leo.bluetoothcommunication;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.leo.potato.Potato;
import com.leo.potato.PotatoInjection;

import java.util.ArrayList;

public class FindDeviceActivity extends BaseActivity {
    @PotatoInjection(idStr = "begin_scan", click = "beginScanDevices")
    Button beginScan;
    @PotatoInjection(idStr = "recycleView")
    RecyclerView recycleView;

    boolean isBluetoothOpen = true;
    private BluetoothAdapter bluetoothAdapter;
    private static final int BLUETOOTH_REQUEST_CODE = 1001;
    public static String uniqueUuid = "";

    ArrayList<DeviceWithDis> devices = new ArrayList<>();
    public static final String DEVICE_INFO = "device_info";
    public static final String BUNDLE_INFO = "bundle_info";
    private Adapter chessAdapter;
//    private ScanBluetoothDevicesCallback scanCallback;


    @Override
    protected void postInflate() {
        init();
    }

    @Override
    protected int inflateRootView() {
        return R.layout.activity_main;
    }

    private void init() {
        checkBleSupport();
        initBluetoothAdapter();
        checkBlutoothOpen();
        recycleView.setLayoutManager(new LinearLayoutManager(FindDeviceActivity.this));
        chessAdapter = new Adapter();
        recycleView.setAdapter(chessAdapter);
    }

    private void initBluetoothAdapter() {
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        beginScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!bluetoothAdapter.isEnabled()) {
                    Toast.makeText(FindDeviceActivity.this, "蓝牙未开", Toast.LENGTH_SHORT).show();
                }
                devices.clear();
                chessAdapter.notifyDataSetChanged();
                beginScanDevices(v);
            }
        });
    }

    private void checkBlutoothOpen() {
        if (!bluetoothAdapter.isEnabled()) {
            isBluetoothOpen = false;
            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(intent, BLUETOOTH_REQUEST_CODE);
        } else {
            isBluetoothOpen = true;
        }
    }

    private void checkBleSupport() {
        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(FindDeviceActivity.this, "本设备不支持Ble数据传输", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == BLUETOOTH_REQUEST_CODE) {
//            checkBlutoothOpen();
        }
    }

    void beginScanDevices(View v) {
        if (bluetoothAdapter != null) {
            bluetoothAdapter.startLeScan(deviceScanResults);
            isScan = true;
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    isScan = false;
                    bluetoothAdapter.stopLeScan(deviceScanResults);
                    chessAdapter.notifyDataSetChanged();
                }
            }, 1000);
        }
    }

    boolean isScan = false;
    DeviceScanResults deviceScanResults = new DeviceScanResults();
    Handler handler = new Handler();


    class DeviceScanResults implements BluetoothAdapter.LeScanCallback {

        @Override
        public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
            if (isScan) {
                String macAddress = device.getAddress();
                if (!checkReplicated(macAddress)) {
                    devices.add(new DeviceWithDis(device, rssi));
                    chessAdapter.notifyDataSetChanged();
                }
            } else {
                devices.clear();
            }
        }
    }

    private boolean checkReplicated(String address) {
        for (DeviceWithDis device : devices) {
            if (address.equalsIgnoreCase(device.bluetoothDevice.getAddress())) {
                return true;
            }
        }
        return false;
    }

    class Adapter extends RecyclerView.Adapter<Adapter.ChessViewHolder> {
        @Override
        public ChessViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            ChessViewHolder chessViewHolder = new ChessViewHolder(LayoutInflater.from(FindDeviceActivity.this).inflate(R.layout.item_device_recyclerview, null));
            return chessViewHolder;
        }

        @Override
        public int getItemCount() {
            return devices.size();
        }

        @Override
        public int getItemViewType(int position) {
            return super.getItemViewType(position);
        }

        @Override
        public void onBindViewHolder(ChessViewHolder holder, final int position) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(FindDeviceActivity.this, BluetoothDetailInfoActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putParcelable(DEVICE_INFO, devices.get(position).bluetoothDevice);
                    intent.putExtra(BUNDLE_INFO, bundle);
                    startActivity(intent);
                }
            });
            holder.tvDes.setText(TextUtils.isEmpty(devices.get(position).bluetoothDevice.getName()) ? "null" : devices.get(position).bluetoothDevice.getName());
            holder.tvMacAddress.setText(devices.get(position).bluetoothDevice.getAddress());
            holder.tvDis.setText("距离:" + devices.get(position).rssi);
        }


        class ChessViewHolder extends RecyclerView.ViewHolder {

            public ChessViewHolder(View itemView) {
                super(itemView);
                tvDes = (TextView) itemView.findViewById(R.id.tvDes);
                tvMacAddress = (TextView) itemView.findViewById(R.id.tvMacAddress);
                tvDis = (TextView) itemView.findViewById(R.id.tvDis);
            }

            public TextView tvDes;
            public TextView tvMacAddress;
            public TextView tvDis;
        }

    }

    public static class DeviceWithDis {
        public DeviceWithDis(BluetoothDevice device, int dis) {
            this.bluetoothDevice = device;
            rssi = dis;
        }

        public BluetoothDevice bluetoothDevice;
        public int rssi;
    }

}
