package com.hit.aircraftwar.music;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;

import androidx.appcompat.app.AppCompatActivity;

import com.hit.aircraftwar.R;
import com.hit.aircraftwar.application.Settings;

import java.util.HashMap;

public class MySoundPool extends AppCompatActivity {

    // 设置为单例模式
    private volatile static MySoundPool instance = null;

    private SoundPool soundPool;
    private HashMap<Integer, Integer> soundData;

    // 音乐资源
    public static int BGM = R.raw.bgm;
    public static int BGM_BOSS = R.raw.bgm_boss;
    public static int BOMB_EXPLOSION = R.raw.bomb_explosion;
    public static int BULLET = R.raw.bullet;
    public static int BULLET_HIT = R.raw.bullet_hit;
    public static int GAME_OVER = R.raw.game_over;
    public static int GET_SUPPLY = R.raw.get_supply;


    private MySoundPool() {
        soundPool = new SoundPool(10, AudioManager.STREAM_MUSIC,0);
        soundData = new HashMap<Integer, Integer>();
        soundData.put(BGM, soundPool.load(this, BGM, 1));


    }

    public static MySoundPool getInstance(){
        if(instance == null){
            synchronized (Settings.class){
                if(instance == null) {
                    instance = new MySoundPool();
                }
            }
        }
        return instance;
    }

    public void playSound(int sound, boolean isloop){
        AudioManager am = (AudioManager) this.getSystemService(Context.AUDIO_SERVICE);
        float audioMaxVolumn = am.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        float volumnCurrent = am.getStreamVolume(AudioManager.STREAM_MUSIC);
        float volumnRatio = volumnCurrent / audioMaxVolumn;

        soundPool.play(soundData.get(BGM), volumnRatio, volumnRatio, 1, -1, 1);
    }
}
