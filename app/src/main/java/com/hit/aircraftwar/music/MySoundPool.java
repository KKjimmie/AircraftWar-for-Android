package com.hit.aircraftwar.music;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;

import androidx.appcompat.app.AppCompatActivity;

import com.hit.aircraftwar.R;
import com.hit.aircraftwar.application.Activity.MainActivity;
import com.hit.aircraftwar.application.Settings;

import java.util.HashMap;
import java.util.Set;

public class MySoundPool {

    // 设置为单例模式
    private volatile static MySoundPool instance = null;

    private static SoundPool soundPool = new SoundPool(50, AudioManager.STREAM_MUSIC,0);;
    private static HashMap<Integer, Integer> soundData= new HashMap<>();

    // 音乐资源
    public static int BGM = R.raw.bgm;
    public static int BGM_BOSS = R.raw.bgm_boss;
    public static int BOMB_EXPLOSION = R.raw.bomb_explosion;
    public static int BULLET = R.raw.bullet;
    public static int BULLET_HIT = R.raw.bullet_hit;
    public static int GAME_OVER = R.raw.game_over;
    public static int GET_SUPPLY = R.raw.get_supply;
    static {
        soundData.put(BGM, soundPool.load(MainActivity.mContext, BGM, 1));
        soundData.put(BGM_BOSS, soundPool.load(MainActivity.mContext, BGM_BOSS, 1));
        soundData.put(BOMB_EXPLOSION, soundPool.load(MainActivity.mContext, BOMB_EXPLOSION, 1));
        soundData.put(BULLET, soundPool.load(MainActivity.mContext, BULLET, 1));
        soundData.put(BULLET_HIT, soundPool.load(MainActivity.mContext, BULLET_HIT, 1));
        soundData.put(GAME_OVER, soundPool.load(MainActivity.mContext, GAME_OVER, 1));
        soundData.put(GET_SUPPLY, soundPool.load(MainActivity.mContext, GET_SUPPLY, 1));
    }

    private MySoundPool() {

    }


    public static void playSound(int sound, boolean isloop){
        if(! Settings.getInstance().getVideoState()){
            return;
        }
        new Thread(() -> {
            AudioManager am = (AudioManager) MainActivity.mContext.getSystemService(Context.AUDIO_SERVICE);
            float audioMaxVolumn = am.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
            float volumnCurrent = am.getStreamVolume(AudioManager.STREAM_MUSIC);
            float volumnRatio = volumnCurrent / audioMaxVolumn;
            // 循环次数
            int loopNum;
            if(isloop){
                loopNum = -1;
            }else loopNum = 0;
            soundPool.play(soundData.get(sound), volumnRatio,volumnRatio , 1, loopNum, 1);
        }).start();
    }
}
