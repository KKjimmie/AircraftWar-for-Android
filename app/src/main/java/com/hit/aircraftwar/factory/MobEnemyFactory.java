package com.hit.aircraftwar.factory;

import com.hit.aircraftwar.aircraft.AbstractAircraft;
import com.hit.aircraftwar.aircraft.MobEnemy;
import com.hit.aircraftwar.application.Activity.MainActivity;
import com.hit.aircraftwar.application.Settings;


/**
 * 普通敌机工厂
 * @author 柯嘉铭
 */
public class MobEnemyFactory implements ProduceEnemy {
    @Override
    public AbstractAircraft produceEnemy() {
        int locationX = (int) (Math.random() * (MainActivity.width));
        int locationY = 0;
        int speedX = Settings.getInstance().mobEnemySpeedX;
        int speedY = Settings.getInstance().mobEnemySpeedY;
        int hp = Settings.getInstance().mobEnemyHp;
        return new MobEnemy(locationX, locationY, speedX, speedY, hp);
    }
}
