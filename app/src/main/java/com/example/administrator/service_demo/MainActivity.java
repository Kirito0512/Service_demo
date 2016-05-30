package com.example.administrator.service_demo;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "MainActivity";
    private Button startService;
    private Button stopService;
    private Button bindService;
    private Button unbindService;
    private MyService.DownloadBinder downloadBinder;
    private ServiceConnection connection = new ServiceConnection(){
        @Override
        //活动与服务绑定的时候自动调用
        //IBinder service即是MyService中的onBind返回的值
        public void onServiceConnected(ComponentName name, IBinder service) {
            //通过向下转型，获取Myservice中的DownloadBinder对象
            downloadBinder = (MyService.DownloadBinder) service;
            Log.d(TAG, "onServiceConnected: 绑定成功！！！！！！");
            downloadBinder.startDownload();
            downloadBinder.getProgress();
        }

        @Override
        //解绑时调用
        public void onServiceDisconnected(ComponentName name) {
            Log.d(TAG, "onServiceDisconnected: 已解除绑定！！！！！！");
        }
    };

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startService = (Button) findViewById(R.id.start_service);
        stopService = (Button) findViewById(R.id.stop_service);
        bindService = (Button) findViewById(R.id.bind_service);
        unbindService = (Button) findViewById(R.id.unbind_service);
        //返回主外部存储目录（根目录下）
        String path = Environment.getExternalStorageDirectory().getPath();
        String filename = "xuqi.txt";
        //文件的完整路径
        path = path +"/"+filename;
        Log.d(TAG, "path = "+path);
        //创建名为xuqi.txt的文件
        Create_file(path);
        //按钮
        startService.setOnClickListener(this);
        stopService.setOnClickListener(this);
        bindService.setOnClickListener(this);
        unbindService.setOnClickListener(this);
    }
    public void Create_file(String content){
        String path = content;
        File file = new File(path);
        try {
            //getParentFile() 从该文件的父路径返回一个新的文件。
            if (!file.getParentFile().exists()) {
                //mkdirs()创建此文件命名的目录，如有必要，创建缺少的父目录。
                file.getParentFile().mkdirs();
            }
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            Log.d(TAG, "Create_file   :   "+path);
            FileWriter fw = new FileWriter(file, true);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write("Ships from an EU task force and Italy's coast guard raced " +
                    "   to the scene 35 nautical miles (65km) off the coast" +
                    " as survivors clung to the hull or swam.");
            bw.flush();
            bw.close();
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.start_service:
                Intent startIntent = new Intent(this,MyService.class);
                startService(startIntent);
                Toast.makeText(this,"service start!!!!!",Toast.LENGTH_SHORT).show();
                break;

            case R.id.stop_service:
                Intent stopIntent = new Intent(this,MyService.class);
                stopService(stopIntent);
                Toast.makeText(this,"service stop!!!!!",Toast.LENGTH_SHORT).show();
                break;

            case R.id.bind_service:
                Intent bindIntent = new Intent(this,MyService.class);
                bindService(bindIntent,connection,BIND_AUTO_CREATE);
                Toast.makeText(this,"bind service!!!!!",Toast.LENGTH_SHORT).show();
                break;

            case R.id.unbind_service:
                unbindService(connection);
                Toast.makeText(this,"unbind service!!!!!",Toast.LENGTH_SHORT).show();
                break;

            default:
                break;
        }
    }
}
