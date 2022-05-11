package com.hit.aircraftwar.aircraft;

import com.hit.aircraftwar.application.ImageManager;
import com.hit.aircraftwar.bullet.BaseBullet;
import com.hit.aircraftwar.factory.BloodPropFactory;
import com.hit.aircraftwar.factory.BombPropFactory;
import com.hit.aircraftwar.factory.BulletPropFactory;
import com.hit.aircraftwar.props.AbstractProp;
import com.hit.aircraftwar.shootStrategy.ScatteredShoot;

import java.util.List;
import java.util.Random;

/**
 * Boss敌机
 * 可以射击
 * @author 柯嘉铭
 */

public class Boss extends AbstractAircraft {

    private int shootNum = 3;
    private int power = 10;
    private int direction = 1;

    private final BloodPropFactory bloodPropFactory = new BloodPropFactory();
    private final BombPropFactory bombPropFactory = new BombPropFactory();
    private final BulletPropFactory bulletPropFactory = new BulletPropFactory();

    public Boss(int locationX, int locationY, int speedX, int speedY, int hp) {
        super(locationX, locationY, speedX, speedY, hp);
        this.setStrategy(new ScatteredShoot());
        this.bitmap = ImageManager.BOSS_BITMAP;
    }

    @Override
    public List<BaseBullet> shoot() {
            return strategy.shootMode(false, this.locationX, this.locationY, this.direction, this.power, this.shootNum);
    }


    public AbstractProp genProp(){
        // 击败boss，必掉落装备
        Random r = new Random();
        int randProp = r.nextInt(3);
        switch (randProp){
            case 0 : return bloodPropFactory.produceProp(this.locationX, this.locationY);
            case 1 : return bombPropFactory.produceProp(this.locationX, this.locationY);
            case 2 : return bulletPropFactory.produceProp(this.locationX, this.locationY);
            default: return null;
        }
    }
}
