package com.hit.aircraftwar.aircraft;

import com.hit.aircraftwar.application.Activity.MainActivity;
import com.hit.aircraftwar.application.ImageManager;
import com.hit.aircraftwar.bullet.BaseBullet;

import java.util.List;

/**
 * 普通敌机
 * 不可射击
 *
 * @author hitsz
 */
public class MobEnemy extends AbstractAircraft {

    public MobEnemy(int locationX, int locationY, int speedX, int speedY, int hp) {
        super(locationX, locationY, speedX, speedY, hp);
        this.image = ImageManager.MOB_ENEMY_IMAGE;
    }

    @Override
    public List<BaseBullet> shoot() {
        return null;
    }

    @Override
    public void forward() {
        super.forward();
        // 判定 y 轴向下飞行出界
        if (locationY >= MainActivity.height) {
            vanish();
        }
    }

}
