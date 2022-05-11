package com.hit.aircraftwar.props;


import com.hit.aircraftwar.aircraft.HeroAircraft;
import com.hit.aircraftwar.application.ImageManager;

/**
 * 子弹道具类，用于加强英雄机子弹
 * @author 柯嘉铭
 */
public class BulletProp extends AbstractProp{

    private int bulletNumAdd = 1; //增加子弹数目

    public BulletProp(int locationX, int locationY, int speedX, int speedY) {
        super(locationX, locationY, speedX, speedY);
        this.bitmap = ImageManager.BULLET_PROP_BITMAP;
    }

    @Override
    public void work (){
        if(HeroAircraft.getInstance().getShootNum() < HeroAircraft.getInstance().getMaxShootNum()) {
            HeroAircraft.getInstance().addShootNum(bulletNumAdd);
        }else{
            new BulletThread().start();
        }
    }
}