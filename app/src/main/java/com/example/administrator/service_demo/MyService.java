package com.example.administrator.service_demo;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Environment;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

/**
 * Created by Administrator on 2016/5/26.
 */
public class MyService extends Service {
    @Nullable
    //用来操控音乐播放
    private MediaPlayer mediaPlayer;
    private static final String TAG = "MyService";

    private DownloadBinder mBinder = new DownloadBinder();

    //对下载功能进行管理
    class DownloadBinder extends Binder {
        public void startDownload(){
            Log.d(TAG, "startDownload: start download !!!!!");
        }
        public int getProgress(){
            Log.d(TAG, "getProgress: get progress !!!!!");
            return 0;
        }
    };

    public IBinder onBind(Intent intent) {
        Log.d(TAG, "onBind: start!!!!!!!!!");
        return mBinder;
    }

    public void onCreate(){
        Log.d(TAG, "onCreate: !!!!!!");
        super.onCreate();
        //使用前台服务
        foreground_Notification();

    }

    public int onStartCommand(Intent intent,int flags,int startId){
        Log.d(TAG, "onStartCommand: start!!!!!!!!!!");
        initMediaPlayer();
        return super.onStartCommand(intent,flags,startId);
    }
    private void  initMediaPlayer(){
        try{
            mediaPlayer = new MediaPlayer();
            File file = new File(Environment.getExternalStorageDirectory(),"music.mp3");
            //指定音频文件的路径
            mediaPlayer.setDataSource(file.getPath());
            if(!mediaPlayer.isPlaying()){
                mediaPlayer.prepare();
                mediaPlayer.start();
                Toast.makeText(this,"music start ",Toast.LENGTH_SHORT).show();
                Log.d(TAG, "initMediaPlayer music start!!!!!!!");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void foreground_Notification(){
        Notification.Builder build = new Notification.Builder(this).setContentText("前台服务").setContentTitle("服务标题").setSmallIcon(R.drawable.ic_launcher);
        Intent intent = new Intent(this,MainActivity.class);
        PendingIntent result = PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_CANCEL_CURRENT);
        build.setContentIntent(result);
        Notification notice = build.build();
        startForeground(1,notice);
        Log.d(TAG, "前台服务！！！！！！");
    }

    public void onDestroy(){

        Log.d(TAG, "onDestroy: stop!!!!!!!!!!");
        super.onDestroy();
        if(mediaPlayer != null){
            mediaPlayer.stop();
            mediaPlayer.release();
            Toast.makeText(this,"music stop",Toast.LENGTH_SHORT).show();
            Log.d(TAG, "onDestroy music release !!!!!!!!");
        }
    }
}
