package com.hit.aircraftwar.bullet;

import com.hit.aircraftwar.application.ImageManager;
import com.hit.aircraftwar.basic.CanBoom;



/**
 * @Author hitsz
 */
public class EnemyBullet extends BaseBullet implements CanBoom {

    public EnemyBullet(int locationX, int locationY, int speedX, int speedY, int power) {
        super(locationX, locationY, speedX, speedY, power);
        this.bitmap = ImageManager.ENEMY_BULLET_BITMAP;
    }

    @Override
    public void boom() {
        vanish();
    }
}
