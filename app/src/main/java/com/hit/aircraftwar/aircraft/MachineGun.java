package com.hit.aircraftwar.aircraft;

import com.hit.aircraftwar.application.Activity.MainActivity;
import com.hit.aircraftwar.application.ImageManager;
import com.hit.aircraftwar.application.Settings;
import com.hit.aircraftwar.bullet.BaseBullet;
import com.hit.aircraftwar.shootStrategy.DirectShoot;

import java.util.List;

public class MachineGun extends AbstractAircraft{
    private static int shootNum = 3;

    private static int power = Settings.getInstance().heroPower;

    private int direction = -1;


    public MachineGun(int locationX, int locationY, int speedX, int speedY, int hp) {
        super(locationX, locationY,speedX, speedY, hp);
        this.setStrategy(new DirectShoot());
        this.bitmap = ImageManager.GUN_BITMAP;
    }



    @Override
    public List<BaseBullet> shoot() {
        return strategy.shootMode(true, this.locationX, this.locationY, this.direction, this.power, 3);
    }

}
