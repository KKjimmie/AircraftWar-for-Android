package com.hit.aircraftwar.application;


import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.hit.aircraftwar.R;
import com.hit.aircraftwar.aircraft.Boss;
import com.hit.aircraftwar.aircraft.EliteEnemy;
import com.hit.aircraftwar.aircraft.HeroAircraft;
import com.hit.aircraftwar.aircraft.MobEnemy;
import com.hit.aircraftwar.application.Activity.MainActivity;
import com.hit.aircraftwar.bullet.EnemyBullet;
import com.hit.aircraftwar.bullet.HeroBullet;
import com.hit.aircraftwar.props.BloodProp;
import com.hit.aircraftwar.props.BombProp;
import com.hit.aircraftwar.props.BulletProp;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class ImageManager {
    /**
     * 类名-图片 映射，存储各基类的图片
     * 可使用 CLASSNAME_IMAGE_MAP.get( obj.getClass().getName() ) 获得 obj 所属基类对应的图片
     */
    private static final Map<String, Bitmap> CLASSNAME_IMAGE_MAP = new HashMap<>();

    public static Bitmap BACKGROUND_IMAGE = getBitmap(R.drawable.bg);
    public static Bitmap HERO_BITMAP = getBitmap(R.drawable.hero);
    public static Bitmap BOSS_BITMAP = getBitmap(R.drawable.boss);
    public static Bitmap MOB_ENEMY_BITMAP = getBitmap(R.drawable.mob);
    public static Bitmap ELITE_ENEMY_BITMAP = getBitmap(R.drawable.elite);
    public static Bitmap HERO_BULLET_BITMAP = getBitmap(R.drawable.bullet_hero);
    public static Bitmap ENEMY_BULLET_BITMAP = getBitmap(R.drawable.bullet_enemy);
    public static Bitmap BLOOD_PROP_BITMAP = getBitmap(R.drawable.prop_blood);
    public static Bitmap BOMB_PROP_BITMAP = getBitmap(R.drawable.prop_bomb);
    public static Bitmap BULLET_PROP_BITMAP = getBitmap(R.drawable.prop_bullet);

    static {
        CLASSNAME_IMAGE_MAP.put(HeroAircraft.class.getName(), HERO_BITMAP);
        CLASSNAME_IMAGE_MAP.put(Boss.class.getName(), BOSS_BITMAP);
        CLASSNAME_IMAGE_MAP.put(MobEnemy.class.getName(), MOB_ENEMY_BITMAP);
        CLASSNAME_IMAGE_MAP.put(EliteEnemy.class.getName(), ELITE_ENEMY_BITMAP);
        CLASSNAME_IMAGE_MAP.put(HeroBullet.class.getName(), HERO_BULLET_BITMAP);
        CLASSNAME_IMAGE_MAP.put(EnemyBullet.class.getName(), ENEMY_BULLET_BITMAP);
        CLASSNAME_IMAGE_MAP.put(BloodProp.class.getName(), BLOOD_PROP_BITMAP);
        CLASSNAME_IMAGE_MAP.put(BombProp.class.getName(), BOMB_PROP_BITMAP);
        CLASSNAME_IMAGE_MAP.put(BulletProp.class.getName(), BULLET_PROP_BITMAP);
    }


    private static Bitmap loadBitmap(String filename){
        File file = new File(filename);
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
        return bitmap;
    }

    public static Bitmap getBitmap(int image) {
        return BitmapFactory.decodeResource(MainActivity.baseActivity.getResources(), image);
    }
    public static Bitmap get(String className) {
        return CLASSNAME_IMAGE_MAP.get(className);
    }
    public static Bitmap get(Object obj){
        if (obj == null){
            return null;
        }
        return get(obj.getClass().getName());
    }

    public static int getWidth(int image){
        BitmapFactory.Options options = new BitmapFactory.Options();
        BitmapFactory.decodeResource(MainActivity.baseActivity.getResources(),image,options);

        //获取图片的宽高
        return options.outWidth;
    }

    public static int getHeight(int image){
        BitmapFactory.Options options = new BitmapFactory.Options();
        BitmapFactory.decodeResource(MainActivity.baseActivity.getResources(),image,options);

        //获取图片的宽高
        return options.outHeight;
    }
}
