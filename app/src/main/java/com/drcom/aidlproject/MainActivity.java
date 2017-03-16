package com.drcom.aidlproject;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    private IAdditionService service;
    private AdditionServiceConnection connection;
    @Bind(R.id.value1)EditText value1;
    @Bind(R.id.value2)EditText value2;
    @Bind(R.id.result)TextView result;
    @Bind(R.id.buttonCalc)Button calc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aidl_layout);
        ButterKnife.bind(this);
        initService();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        releaseService();
    }

    @OnClick(R.id.buttonCalc)
    public void onClick(){
        int v1, v2, res = -1;
        try{
            v1 = Integer.parseInt(value1.getText().toString());
            v2 = Integer.parseInt(value2.getText().toString());
            res = service.add(v1,v2);
        } catch (RemoteException e) {
            e.printStackTrace();
        }catch (NumberFormatException e){
            Toast.makeText(this,"请输入数字",Toast.LENGTH_SHORT).show();
    }
        result.setText(String.valueOf(res));
    }

    private class AdditionServiceConnection implements ServiceConnection{
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            service = IAdditionService.Stub.asInterface(iBinder);
            Log.i("aidl","Service Connected");
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            service = null;
            Log.i("aidl","Service Disconnected");
        }
    }

    private void initService(){
        connection = new AdditionServiceConnection();
        Intent intent = new Intent();
        intent.setClassName("com.drcom.aidlproject",com.drcom.aidlproject.AdditionService.class.getName());
        bindService(intent,connection,Context.BIND_AUTO_CREATE);
    }

    private void releaseService(){
        unbindService(connection);
        connection = null;
    }
}
