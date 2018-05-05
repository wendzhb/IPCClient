package com.zhbstudy.ipcclient;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.zhbstudy.ipcdemo.R;
import com.zhbstudy.ipcdemo.UserAidl;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private UserAidl mUserAidl;
    private Button accoutButton, pwdButton;

    //客户端一定要获取aidl的实例
    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            //连接好了 service就是服务端给我们的IBinder
            Log.e("client", "onServiceConnected");
            mUserAidl = UserAidl.Stub.asInterface(service);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            //断开连接
            Log.e("client", "onServiceDisconnected");
            mUserAidl = null;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        accoutButton = (Button) findViewById(R.id.bt_account);
        pwdButton = (Button) findViewById(R.id.bt_pwd);

        accoutButton.setOnClickListener(this);
        pwdButton.setOnClickListener(this);

        //1.客户端的代码（a应用）
        //bindService(Intent service, ServiceConnection conn, int flags)
        //2.隐式意图
        Intent intent = new Intent();
        intent.setAction("com.study.aidl.user");
        // 在Android 5.0之后google出于安全的角度禁止了隐式声明Intent来启动Service.也禁止使用Intent filter.否则就会抛个异常出来
        intent.setPackage("com.zhbstudy.ipcdemo");

        bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE);
    }



    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.bt_account:
                try {
                    Log.e("test", mUserAidl.getUserName());
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.bt_pwd:
                try {
                    Log.e("test", mUserAidl.getUserPwd());
                } catch (RemoteException e) {
                    e.printStackTrace();
                }

                break;
        }

    }
}
