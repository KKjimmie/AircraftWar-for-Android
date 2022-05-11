package com.hit.aircraftwar.props;

import com.hit.aircraftwar.aircraft.HeroAircraft;
import com.hit.aircraftwar.application.ImageManager;
import com.hit.aircraftwar.application.Settings;

/**
 * 血包道具类，用于加血
 * @author 柯嘉铭
 */
public class BloodProp extends AbstractProp {

//    private int plusHp = Settings.getInstance().hpPlus;
    private int plusHp = 10;

    public BloodProp(int locationX, int locationY, int speedX, int speedY) {
        super(locationX, locationY, speedX, speedY);
        this.bitmap = ImageManager.BLOOD_PROP_BITMAP;
    }

    @Override
    public void work() {
        if (HeroAircraft.getInstance().getHp() < Settings.getInstance().maxHeroHp) {
            HeroAircraft.getInstance().decreaseHp(- plusHp);
        }
    }
}
