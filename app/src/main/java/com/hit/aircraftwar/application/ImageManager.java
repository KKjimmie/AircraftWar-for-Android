package com.hit.aircraftwar.application;


import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.hit.aircraftwar.R;
import com.hit.aircraftwar.aircraft.Boss;
import com.hit.aircraftwar.aircraft.EliteEnemy;
import com.hit.aircraftwar.aircraft.HeroAircraft;
import com.hit.aircraftwar.aircraft.MobEnemy;
import com.hit.aircraftwar.bullet.EnemyBullet;
import com.hit.aircraftwar.bullet.HeroBullet;
import com.hit.aircraftwar.props.BloodProp;
import com.hit.aircraftwar.props.BombProp;
import com.hit.aircraftwar.props.BulletProp;

import java.util.HashMap;
import java.util.Map;

public class ImageManager {
    /**
     * 类名-图片 映射，存储各基类的图片
     * 可使用 CLASSNAME_IMAGE_MAP.get( obj.getClass().getName() ) 获得 obj 所属基类对应的图片
     */
    private static final Map<String, Integer> CLASSNAME_IMAGE_MAP = new HashMap<>();

    public static int BACKGROUND_IMAGE;
    public static int HERO_IMAGE = R.drawable.hero;
    public static int BOSS_IMAGE = R.drawable.boss;
    public static int MOB_ENEMY_IMAGE = R.drawable.mob;
    public static int ELITE_ENEMY_IMAGE = R.drawable.elite;
    public static int HERO_BULLET_IMAGE = R.drawable.bullet_hero;
    public static int ENEMY_BULLET_IMAGE = R.drawable.bullet_enemy;
    public static int BLOOD_PROP_IMAGE = R.drawable.prop_blood;
    public static int BOMB_PROP_IMAGE = R.drawable.prop_bomb;
    public static int BULLET_PROP_IMAGE = R.drawable.prop_bullet;

    static {
        CLASSNAME_IMAGE_MAP.put(HeroAircraft.class.getName(), HERO_IMAGE);
        CLASSNAME_IMAGE_MAP.put(Boss.class.getName(), BOSS_IMAGE);
        CLASSNAME_IMAGE_MAP.put(MobEnemy.class.getName(), MOB_ENEMY_IMAGE);
        CLASSNAME_IMAGE_MAP.put(EliteEnemy.class.getName(), ELITE_ENEMY_IMAGE);
        CLASSNAME_IMAGE_MAP.put(HeroBullet.class.getName(), HERO_BULLET_IMAGE);
        CLASSNAME_IMAGE_MAP.put(EnemyBullet.class.getName(), ENEMY_BULLET_IMAGE);
        CLASSNAME_IMAGE_MAP.put(BloodProp.class.getName(), BLOOD_PROP_IMAGE);
        CLASSNAME_IMAGE_MAP.put(BombProp.class.getName(), BOMB_PROP_IMAGE);
        CLASSNAME_IMAGE_MAP.put(BulletProp.class.getName(), BULLET_PROP_IMAGE);
    }


    public static int get(String className) {
        return CLASSNAME_IMAGE_MAP.get(className);
    }
    public static int get(Object obj){
        if (obj == null){
            return -1;
        }
        return get(obj.getClass().getName());
    }

    public static int getWidth(int image){
        BitmapFactory.Options options = new BitmapFactory.Options();
        BitmapFactory.decodeResource(getResources(),image,options);

        //获取图片的宽高
        return options.outWidth;
    }

    public static int getHeight(int image){
        BitmapFactory.Options options = new BitmapFactory.Options();
        BitmapFactory.decodeResource(getResources(),image,options);

        //获取图片的宽高
        return options.outHeight;
    }

    private static Resources getResources() {
        Resources mResources = null;
        mResources = getResources();
        return mResources;
    }
}
