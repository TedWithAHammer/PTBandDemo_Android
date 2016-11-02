package com.bluetooth.leo.bluetoothcommunication;

import android.app.Dialog;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.v4.util.Pair;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bluetooth.leo.bluetoothcommunication.dfu.DfuService;
import com.bluetooth.leo.bluetoothcommunication.util.CommandUtil;
import com.bluetooth.leo.bluetoothcommunication.util.DataDeSerializationUtil;
import com.bluetooth.leo.bluetoothcommunication.util.TransferUtil;
import com.leo.baseadapter.BaseViewHolder;
import com.leo.baseadapter.RecyclerAdapter;
import com.leo.potato.PotatoInjection;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import no.nordicsemi.android.dfu.DfuProgressListener;
import no.nordicsemi.android.dfu.DfuProgressListenerAdapter;
import no.nordicsemi.android.dfu.DfuServiceInitiator;
import no.nordicsemi.android.dfu.DfuServiceListenerHelper;


public class BluetoothDetailInfoActivity extends BaseActivity {
    private static final String Tag = "com.leo.wang";
    @PotatoInjection(id = R.id.btnSendCommand, click = "sendCommand")
    Button btnSendCommand;
    @PotatoInjection(id = R.id.etCommand)
    EditText etCommand;
    @PotatoInjection(id = R.id.rvData)
    RecyclerView rvData;
    @PotatoInjection(id = R.id.pbConnecting)
    ProgressBar pbConnecting;
    @PotatoInjection(id = R.id.tvCommandInstruction, click = "showCommandTip")
    TextView tvCommandInstruction;
    @PotatoInjection(id = R.id.tvPercentage)
    TextView tvPercentage;
    @PotatoInjection(id = R.id.layoutOtaTip)
    RelativeLayout layoutOtaTip;
    @PotatoInjection(id = R.id.tvClearData, click = "clearData")
    TextView tvClearData;
    @PotatoInjection(id = R.id.tvOutputFile, click = "outputFile")
    TextView tvOutputFile;
    @PotatoInjection(id = R.id.tvResult, click = "deserialize")
    TextView tvResult;


    private BluetoothDevice device;
    private BluetoothGatt mGatt;

    protected static String uuidQppService = "6e400001-b5a3-f393-e0a9-e50e24dcca9e";
    protected static String uuidQppCharWrite = "6e400002-b5a3-f393-e0a9-e50e24dcca9e";
    private static final String UUIDDes = "00002902-0000-1000-8000-00805f9b34fb";
    protected static String uuidQppCharNotify = "6e400003-b5a3-f393-e0a9-e50e24dcca9e";

    public static final int COMMAND_REQUEST_CODE = 1001;
    public static final String COMMAND_CONTENT = "command_content";
    public static final String COMMAND_NAME = "command_name";
    public static final String DATA_FILE_NAME = "BluetoothData";
    public static final String DATA_FILE_TYPE = ".txt";


    List<Pair<String, String>> pairList = new ArrayList<>();
    private BluetoothDataAdapter adapter;

    boolean isConnected = false;


    @Override
    protected void postInflate() {
        if (getIntent() != null) {
            Bundle bundle = getIntent().getBundleExtra(FindDeviceActivity.BUNDLE_INFO);
            device = bundle.getParcelable(FindDeviceActivity.DEVICE_INFO);
        }
        addTextWatcher();
        startConnect();
        initRecyclerView();
    }

    void deserialize(View v) {
        pairList.clear();

        List<String> raw = new ArrayList<>();
        raw.add("020ab10103393f0d580403e7");
        raw.add("020ab1010392470d58010331");
        raw.add("020ab10103d0470d58020370");
        raw.add("020ab101037e550d580103cf");
        raw.add("020ab10103795b0d580003c7");
        raw.add("020ab10103bc600d58010338");
        raw.add("020ab1010327660d580203a6");
        raw.add("020ab101031b670d58010398");
        raw.add("020ab10103d2670d58000350");
        raw.add("020ab10103b9690d58010334");

        raw.add("020ab10103ac6a0d58000323");
        raw.add("020ab10103a16b0d5801032e");
        raw.add("020ab10103366f0d580003bc");
        raw.add("020ab1010344730d580103d3");
        raw.add("020ab10103ed750d58050378");
        raw.add("020ab10103d4be0d5804038b");
        raw.add("020ab1010384d70d580103b7");
        raw.add("020ab101033fd80d58020300");
        raw.add("020ab10103fadd0d580103c3");
        raw.add("020ab10103efde0d580203d6");

        raw.add("020ab101038fe10d5801038a");
        raw.add("020ab10103f4e30d580203f0");
        raw.add("020ab101037df90d58010360");
        raw.add("020ab10103bbf90d580203a5");
        raw.add("020ab1010371190e5801038f");
        raw.add("020ab10103281a0e580003d4");
        raw.add("020ab10103a81f0e58010350");
        raw.add("020ab1010329510e5802039c");
        raw.add("020ab101035e580e580103e1");
        raw.add("020ab1010318b50e5800034b");

        raw.add("020ab1010349b60e58010318");
        raw.add("020ab101033eb70e5800036f");
        raw.add("020ab1010366b90e58010338");
        raw.add("020ab1010311bb0e5800034c");
        raw.add("020ab10103d9bb0e58010385");
        raw.add("020ab1010384bd0e580003df");
        raw.add("020ab10103f3bf0e580103ab");
        raw.add("020ab101031ec70e5800033f");
        raw.add("020ab1000368c70e5805034d");
        for (String rawData : raw) {
            byte[] bytes = TransferUtil.hex2Bytes(rawData);
            String result = DataDeSerializationUtil.sleepTimeTest(bytes);
            pairList.add(new Pair<String, String>(rawData, result));
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    protected int inflateRootView() {
        return R.layout.activity_bluetooth_detail_info;
    }

    void showCommandTip(View v) {
        startActivity(CommandDetailActivity.class, COMMAND_REQUEST_CODE);
    }

    void clearData(View v) {
        pairList.clear();
        adapter.notifyDataSetChanged();
    }

    void outputFile(View v) {
        String result = new SimpleDateFormat("hh:mm:ss").format(new Date());
        String filePath = getExternalFilesDir(null).getAbsolutePath() + "/" + DATA_FILE_NAME + result + DATA_FILE_TYPE;
        File file = new File(filePath);
        try {
            FileWriter fw = new FileWriter(file);
            BufferedWriter writer = new BufferedWriter(fw);
            if (pairList.size() > 0) {
                for (int i = 0; i < pairList.size(); i++) {
                    Pair<String, String> data = pairList.get(i);
                    writer.write(data.first);
                    writer.newLine();
                    writer.write(data.second);
                    writer.newLine();
                }
            }
            writer.flush();
            fw.close();
            writer.close();
        } catch (Exception ex) {
            Toast.makeText(this, "导出失败 错误消息" + ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
        showDialog("文件导出成功", "请到" + filePath + "查看", "确认", null);
        Toast.makeText(this, "文件导出成功请到目录" + filePath + "查看", Toast.LENGTH_SHORT).show();
    }

    private void showDialog(String title, String message, String positiveContent, Dialog.OnClickListener pClick) {
        AlertDialog.Builder build = new AlertDialog.Builder(this);
        build.setTitle(title);
        build.setPositiveButton(positiveContent, pClick);
        build.setMessage(message);
        build.setCancelable(true);
        build.create();
        build.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == COMMAND_REQUEST_CODE) {
            if (data == null)
                return;
            String command = data.getStringExtra(COMMAND_CONTENT);
            String commandName = data.getStringExtra(COMMAND_NAME);
            etCommand.setText(command);
            if (command.contains("file")) {
                uploadOTC(command);
                return;
            }
            byte[] byteArray = TransferUtil.hex2Bytes(command);
            if (writeCharacteristic != null) {
                writeCharacteristic.setValue(byteArray);
                boolean isSucces = mGatt.writeCharacteristic(writeCharacteristic);
            }
        }
    }

    private void uploadOTC(String command) {
        Uri uri = Uri.parse(command);
        if (uri == null)
            return;
        final DfuServiceInitiator starter = new DfuServiceInitiator(device.getAddress())
                .setDeviceName(device.getName())
                .setKeepBond(false);
        starter.setZip(uri, uri.getPath());
        starter.start(this, DfuService.class);
    }

    private final DfuProgressListener mDfuProgressListener = new DfuProgressListenerAdapter() {
        @Override
        public void onDeviceConnecting(final String deviceAddress) {
            Log.i(Tag, "onDeviceConnecting" + " info:" + deviceAddress);
        }

        @Override
        public void onDfuProcessStarting(final String deviceAddress) {
            Log.i(Tag, "onDfuProcessStarting" + " info:" + deviceAddress);
            layoutOtaTip.setVisibility(View.VISIBLE);
        }

        @Override
        public void onEnablingDfuMode(final String deviceAddress) {
            Log.i(Tag, "onEnablingDfuMode" + " info:" + deviceAddress);
        }

        @Override
        public void onFirmwareValidating(final String deviceAddress) {
            Log.i(Tag, "onFirmwareValidating" + " info:" + deviceAddress);
        }

        @Override
        public void onDeviceDisconnecting(final String deviceAddress) {
            Log.i(Tag, "onDeviceDisconnecting" + " info:" + deviceAddress);
        }

        @Override
        public void onDfuCompleted(final String deviceAddress) {
            Log.i(Tag, "onDfuCompleted" + " info:" + deviceAddress);
            layoutOtaTip.setVisibility(View.GONE);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(BluetoothDetailInfoActivity.this, "DFU FINISH", Toast.LENGTH_SHORT).show();
                }
            });
        }

        @Override
        public void onDfuAborted(final String deviceAddress) {
            Log.i(Tag, "onDfuAborted" + " info:" + deviceAddress);
        }

        @Override
        public void onProgressChanged(final String deviceAddress, final int percent, final float speed, final float avgSpeed, final int currentPart, final int partsTotal) {
            Log.i(Tag, "onProgressChanged" + " info:" + deviceAddress + " percent" + percent);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    tvPercentage.setText("CURRENT PERCENTAGE:" + percent + "%");
                }
            });
        }

        @Override
        public void onError(final String deviceAddress, final int error, final int errorType, final String message) {
            Log.i(Tag, "onError" + " error:" + error + " message:" + message);
        }
    };

    private void initRecyclerView() {
        rvData.setLayoutManager(new LinearLayoutManager(this));
        adapter = new BluetoothDataAdapter(this, pairList, R.layout.item_bluetooth_data_recyclerview);
        rvData.setAdapter(adapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mGatt.disconnect();
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
            String hexString = etCommand.getText().toString();
            byte[] data = TransferUtil.hex2Bytes(hexString);
            if (writeCharacteristic != null) {
                writeCharacteristic.setValue(data);
                boolean isSucces = mGatt.writeCharacteristic(writeCharacteristic);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        DfuServiceListenerHelper.registerProgressListener(this, mDfuProgressListener);
    }

    @Override
    protected void onPause() {
        super.onPause();
        DfuServiceListenerHelper.unregisterProgressListener(this, mDfuProgressListener);
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
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        pbConnecting.setVisibility(View.GONE);
                    }
                });
                isConnected = true;
                gatt.discoverServices();
            } else if (newState == BluetoothProfile.STATE_DISCONNECTING) {
                Log.i(Tag, "onConnectionStateChange:STATE_DISCONNECTING");
                isConnected = false;
            }
        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            super.onServicesDiscovered(gatt, status);
            Log.i(Tag, "onServicesDiscovered");
            filterTheData(gatt);
        }


        private void filterTheData(BluetoothGatt gatt) {
            List<BluetoothGattService> services = gatt.getServices();
            if (services.size() == 0)
                return;
            for (BluetoothGattService server : services) {
                if (server.getUuid().toString().equals(uuidQppService)) {
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
            }
        }

        @Override
        public void onCharacteristicRead(BluetoothGatt gatt, final BluetoothGattCharacteristic characteristic, int status) {
            super.onCharacteristicRead(gatt, characteristic, status);
            Log.i(Tag, "onCharacteristicRead");
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
            byte[] data = characteristic.getValue();
            String hexString = TransferUtil.byte2SpecificFormatHexStr(data);
            Log.i(Tag, "onCharacteristicChanged hex data:" + hexString);
            String des = DataDeSerializationUtil.deSerializeData(data);
            if (!TextUtils.isEmpty(hexString)) {
                pairList.add(new Pair<String, String>(hexString, des));
            }
            handler.sendEmptyMessage(1);
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


    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if (msg.what == 1) {
                adapter.notifyDataSetChanged();
            }
            return false;
        }
    });

}
