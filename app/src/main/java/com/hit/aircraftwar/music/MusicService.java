//package com.hit.aircraftwar.music;
//
//import android.app.Service;
//import android.content.Intent;
//import android.media.MediaPlayer;
//import android.os.Binder;
//import android.os.IBinder;
//
//
//import com.hit.aircraftwar.R;
//import com.hit.aircraftwar.application.Activity.Game.GameActivity;
//import com.hit.aircraftwar.application.Settings;
//
//
//public class MusicService  extends Service implements
//            MediaPlayer.OnCompletionListener {
//    // 实例化MediaPlayer对象
//    MediaPlayer bgmPlayer;
//    MediaPlayer bossBgmPlayer;
//    private final IBinder binder = new AudioBinder();
//
//    @Override
//    public IBinder onBind(Intent intent) {
//        return binder;
//    }
//
//    public void onCreate() {
//        super.onCreate();
//        // 从raw文件夹中获取一个应用自带的mp3文件
//        bgmPlayer = MediaPlayer.create(this, R.raw.bgm);
//        bgmPlayer.setOnCompletionListener(this);
//        bgmPlayer.setLooping(true);
//
//        bossBgmPlayer = MediaPlayer.create(this, R.raw.bgm_boss);
//        bossBgmPlayer.setOnCompletionListener(this);
//        bossBgmPlayer.setLooping(true);
//    }
//
//    @Override
//    public int onStartCommand(Intent intent, int flags, int startId) {
//        super.onStartCommand(intent, flags, startId);
//        if (!bgmPlayer.isPlaying()) {
//            new MusicPlayThread().start();
//        } else
//            bgmPlayer.isPlaying();
//        return START_STICKY;
//    }
//
//    /**
//     * 当Audio播放完的时候触发该动作
//     */
//    public void onCompletion(MediaPlayer mp) {
//        stopSelf();// 结束了，则结束Service
//
//    }
//
//    public void onDestroy() {
//        super.onDestroy();
//        if (bgmPlayer.isPlaying()) {
//            bgmPlayer.stop();
//        }
//        if(bossBgmPlayer.isPlaying()){
//            bossBgmPlayer.stop();
//        }
//        bgmPlayer.release();
//        bossBgmPlayer.release();
//    }
//
//    // 为了和Activity交互，我们需要定义一个Binder对象
//    public class AudioBinder extends Binder {
//        // 返回Service对象
//        public MusicService getService() {
//            return MusicService.this;
//        }
//    }
//
//    private class MusicPlayThread extends Thread {
//        public void run() {
//            if (Settings.getInstance().getVideoState()) {
//                if(GameActivity.bossExistFlag && !bossBgmPlayer.isPlaying()){
//                    bossBgmPlayer.start();
//                }else if( !GameActivity.bossExistFlag && !bgmPlayer.isPlaying()){
//                    bgmPlayer.start();
//                }
//            }
//        }
//    }
//}