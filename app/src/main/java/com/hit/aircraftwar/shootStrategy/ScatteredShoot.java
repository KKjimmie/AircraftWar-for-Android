package com.hit.aircraftwar.shootStrategy;

import com.hit.aircraftwar.application.Settings;
import com.hit.aircraftwar.bullet.BaseBullet;
import com.hit.aircraftwar.bullet.EnemyBullet;
import com.hit.aircraftwar.bullet.HeroBullet;


import java.util.LinkedList;
import java.util.List;

/**
 * 散射，实现子弹发射方式策略接口
 * @author 柯嘉铭
 */
public class ScatteredShoot implements ShootStrategy {
    /**
     * 射击模式,三弹散射
     */
    @Override
    public List<BaseBullet> shootMode(boolean isHero, int locationX, int locationY, int direction, int power, int shootNum) {
        List<BaseBullet> res = new LinkedList<>();
        int x = locationX;
        int y = locationY + direction*2;
        int speedX = 1;
        int speedY = Settings.getInstance().bulletSpeedY * direction;
        BaseBullet baseBullet;
        for(int i=0; i<shootNum; i++){
            if(isHero){
                baseBullet = new HeroBullet(x + (i*2 - shootNum + 1)*10, y,  speedX*(i-1), speedY, power);
            }else{
                baseBullet = new EnemyBullet(x + (i*2 - shootNum + 1)*55, y,  speedX*(i-1), speedY, power);
            }
            res.add(baseBullet);
        }
        return res;
    }
}
