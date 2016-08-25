package com.bluetooth.leo.bluetoothcommunication;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.os.Handler;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bluetooth.leo.bluetoothcommunication.util.TransferUtil;
import com.leo.baseadapter.BaseViewHolder;
import com.leo.baseadapter.RecyclerAdapter;
import com.leo.potato.Potato;
import com.leo.potato.PotatoInjection;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static android.R.id.list;


public class BluetoothDetailInfoActivity extends AppCompatActivity {
    private static final String Tag = "com.leo.wang";
    @PotatoInjection(id = R.id.btnSendCommand, click = "sendCommand")
    Button btnSendCommand;
    @PotatoInjection(id = R.id.lostPackageNum)
    TextView lostPackageNum;
    @PotatoInjection(id = R.id.etCommand)
    EditText etCommand;
    @PotatoInjection(id = R.id.rvData)
    RecyclerView rvData;


    private BluetoothDevice device;
    private BluetoothGatt mGatt;

    protected static String uuidQppService = "0000fee9-0000-1000-8000-00805f9b34fb";
    protected static String uuidQppCharWrite = "d44bc439-abfd-45a2-b575-925416129600";
    private static final String UUIDDes = "00002902-0000-1000-8000-00805f9b34fb";

    List<Pair<String, String>> pairList = new ArrayList<>();
    private BluetoothDataAdapter adapter;

    boolean isConnected = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth_detail_info);
        Potato.initInjection(this);
        if (getIntent() != null) {
            Bundle bundle = getIntent().getBundleExtra(FindDeviceActivity.BUNDLE_INFO);
            device = bundle.getParcelable(FindDeviceActivity.DEVICE_INFO);
        }
        addTextWatcher();
        startConnect();
        initRecyclerView();
    }

    private void initRecyclerView() {
        rvData.setLayoutManager(new LinearLayoutManager(this));
        adapter = new BluetoothDataAdapter(this, pairList, R.layout.item_bluetooth_data_recyclerview);
        rvData.setAdapter(adapter);
    }

    private void addTextWatcher() {
        etCommand.addTextChangedListener(new TextWatcher() {
            boolean isAuto = false;
            int i = 0;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                if (isAuto) {
//                    etCommand.setSelection(s.length());
//                    isAuto = false;
//                    return;
//                }
//                i += count-before;
//                if (i == 4) {
//                    i = 0;
//                    isAuto = true;
//                    etCommand.setText(s.toString() + " ");
//                }
            }

            @Override
            public void afterTextChanged(Editable s) {


//                if (s.length() % 4)
//                int length = s.length();
//                if (length <= 0)
//                    return;
//                char lastWord = s.charAt(length - 1);
//
//                if ((lastWord >= 48 && lastWord <= 57)
//                        || (lastWord >= 97 && lastWord <= 102) || (lastWord >= 65 && lastWord <= 70)) {
////                    etCommand.setText(s.toString());
//                } else {
//                    Toast.makeText(BluetoothDetailInfoActivity.this, "输入不合法", Toast.LENGTH_SHORT).show();
//                    if (length > 1) {
//                        CharSequence result = s.subSequence(0, length - 1);
//                        etCommand.setText(result);
//                    } else {
//                        etCommand.setText("");
//                    }
//                }
            }
        });
    }

    private void startConnect() {
        mGatt = device.connectGatt(BluetoothDetailInfoActivity.this, true, new ChessBluetoothGatt());
    }

    void sendCommand(View v) {
        if (!isConnected) {
            Toast.makeText(this, "还未连接上", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!TextUtils.isEmpty(etCommand.getText().toString())) {
            String hexString = etCommand.getText().toString().replace(" ", "").toUpperCase();
            byte[] data = TransferUtil.hex2Byte(hexString);
            if (writeCharacteristic != null) {
                writeCharacteristic.setValue(data);
                boolean isSucces = mGatt.writeCharacteristic(writeCharacteristic);
            }
        }
    }

    BluetoothGattCharacteristic writeCharacteristic;

    class BluetoothDataAdapter extends RecyclerAdapter<Pair<String, String>> {

        public BluetoothDataAdapter(Context context, List<Pair<String, String>> data, int layoutId) {
            super(context, data, layoutId);
        }

        @Override
        protected void onBind(int viewType, BaseViewHolder holder, int position, Pair<String, String> stringStringPair) {
            super.onBind(viewType, holder, position, stringStringPair);
            holder.setText(R.id.tvData, stringStringPair.first);
            holder.setText(R.id.tvTime, stringStringPair.second);
        }
    }

    class ChessBluetoothGatt extends BluetoothGattCallback {
        public ChessBluetoothGatt() {
            super();
        }

        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            super.onConnectionStateChange(gatt, status, newState);
            if (newState == BluetoothProfile.STATE_CONNECTED) {
                Log.i(Tag, "onConnectionStateChange:STATE_CONNECTED");
                isConnected = true;
                mGatt.discoverServices();
            } else if (newState == BluetoothProfile.STATE_DISCONNECTING) {
                Log.i(Tag, "onConnectionStateChange:STATE_DISCONNECTING");
                isConnected = false;
            }
        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            super.onServicesDiscovered(gatt, status);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(BluetoothDetailInfoActivity.this, "服务已打开，可发送数据", Toast.LENGTH_SHORT).show();
                }
            });
            Log.i(Tag, "onServicesDiscovered");
            filterTheData(gatt);
        }


        private void filterTheData(BluetoothGatt gatt) {
            BluetoothGattService server = mGatt.getService(UUID.fromString(uuidQppService));
            List<BluetoothGattCharacteristic> datas = server.getCharacteristics();
            for (BluetoothGattCharacteristic data : datas) {
                if (data.getUuid().toString().equals(uuidQppCharWrite)) {
                    writeCharacteristic = data;
                } else if (data.getProperties() == BluetoothGattCharacteristic.PROPERTY_NOTIFY) {
                    gatt.setCharacteristicNotification(data, true);
                    BluetoothGattDescriptor descriptor = data.getDescriptor(UUID.fromString(UUIDDes));
                    descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
                    gatt.writeDescriptor(descriptor);
                }
            }
        }

        @Override
        public void onCharacteristicRead(BluetoothGatt gatt, final BluetoothGattCharacteristic characteristic, int status) {
            super.onCharacteristicRead(gatt, characteristic, status);
            Log.i(Tag, "onCharacteristicRead");
            handler.post(new Runnable() {
                @Override
                public void run() {
                    byte[] data = characteristic.getValue();
                    String hexString = TransferUtil.byte2HexStr(data);
                    String time = new SimpleDateFormat("hh:mm:ss").format(new Date(System.currentTimeMillis()));
                    if (!TextUtils.isEmpty(hexString)) {
                        pairList.add(new Pair<String, String>(hexString, time));
                    }
                    Log.i(Tag, "onCharacteristicRead data:" + hexString);
                    adapter.notifyDataSetChanged();
//                    int lastNum = data[data.length - 1];
//                    lastNum = lastNum & 0xff;
//                    decodeData(lastNum);
                }
            });
        }

        @Override
        public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            super.onCharacteristicWrite(gatt, characteristic, status);
            String writeCommand = TransferUtil.byte2HexStr(characteristic.getValue());
            Log.i(Tag, "onCharacteristicWrite characteristic:" + writeCommand);

        }

        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt, final BluetoothGattCharacteristic characteristic) {
            super.onCharacteristicChanged(gatt, characteristic);
//            Log.i(Tag, "onCharacteristicChanged");
            handler.post(new Runnable() {
                @Override
                public void run() {
                    if (pairList.size() == 50)
                        pairList.clear();
                    byte[] data = characteristic.getValue();
                    String hexString = TransferUtil.byte2SpecificFormatHexStr(data);
                    String time = new SimpleDateFormat("hh:mm:ss").format(new Date(System.currentTimeMillis()));
                    if (!TextUtils.isEmpty(hexString)) {
                        pairList.add(new Pair<String, String>(hexString, time));
                    }
                    Log.i(Tag, "onCharacteristicChanged data:" + hexString);
                    adapter.notifyDataSetChanged();
//                    int lastNum = data[data.length - 1];
//                    lastNum = lastNum & 0xff;
//                    decodeData(lastNum);
                }
            });
        }

        @Override
        public void onDescriptorRead(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
            super.onDescriptorRead(gatt, descriptor, status);
            Log.i(Tag, "onDescriptorRead");
        }

        @Override
        public void onDescriptorWrite(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
            super.onDescriptorWrite(gatt, descriptor, status);
            Log.i(Tag, "onDescriptorWrite");
        }

        @Override
        public void onReliableWriteCompleted(BluetoothGatt gatt, int status) {
            super.onReliableWriteCompleted(gatt, status);
            Log.i(Tag, "onReliableWriteCompleted");
        }

        @Override
        public void onReadRemoteRssi(BluetoothGatt gatt, int rssi, int status) {
            super.onReadRemoteRssi(gatt, rssi, status);
            Log.i(Tag, "onReadRemoteRssi");
        }

        @Override
        public void onMtuChanged(BluetoothGatt gatt, int mtu, int status) {
            super.onMtuChanged(gatt, mtu, status);
            Log.i(Tag, "onMtuChanged");
        }
    }

    int counter = 1;
    int preNum = 0;
    int receiveNum = 0;

    private void decodeData(int lastNum) {
        if (lastNum < preNum) {
            lostPackageNum.setText(counter * 256 + "个包收到" + receiveNum + "个包");
            counter++;
        }
        preNum = lastNum;
        receiveNum++;
    }

    Handler handler = new Handler();

}
