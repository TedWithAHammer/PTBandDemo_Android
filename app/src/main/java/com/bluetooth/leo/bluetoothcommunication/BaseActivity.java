package com.bluetooth.leo.bluetoothcommunication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.idescout.sql.SqlScoutServer;
import com.leo.potato.Potato;

/**
 * Created by leo on 2016/9/5.
 */

public abstract class BaseActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(inflateRootView());
        SqlScoutServer.create(this, getPackageName());
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        Potato.initInjection(this);
        postInflate();
    }

    /**
     * code after inflate the root view
     */
    protected abstract void postInflate();

    /**
     * @return root view's id
     */
    protected abstract int inflateRootView() ;

    /**
     * toast long msg
     * @param msg
     */
    public void toastMessageLong(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }

    /**
     * toast short msg
     * @param msg
     */
    public void toastMessageShort(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    public void startActivity(Class<? extends Activity> clzz, int requestCode){
        startActivityForResult(new Intent(this,clzz),requestCode);
    }
}
