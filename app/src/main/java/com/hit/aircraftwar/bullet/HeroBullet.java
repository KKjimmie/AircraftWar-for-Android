package com.hit.aircraftwar.bullet;

import com.hit.aircraftwar.application.ImageManager;

/**
 * @Author hitsz
 */
public class HeroBullet extends BaseBullet {

    public HeroBullet(int locationX, int locationY, int speedX, int speedY, int power) {
        super(locationX, locationY, speedX, speedY, power);
        this.image = ImageManager.HERO_BULLET_IMAGE;
    }

}
