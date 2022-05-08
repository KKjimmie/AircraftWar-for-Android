package com.hit.aircraftwar.factory;

import com.hit.aircraftwar.aircraft.AbstractAircraft;
import com.hit.aircraftwar.aircraft.EliteEnemy;
import com.hit.aircraftwar.application.Activity.MainActivity;
import com.hit.aircraftwar.application.Settings;


/**
 * 精英机工厂
 * @author 柯嘉铭
 */
public class EliteEnemyFactory implements ProduceEnemy {

    @Override
    public AbstractAircraft produceEnemy() {
        int locationX = (int) (Math.random() * (MainActivity.width));
        int locationY = 0;
        int speedX = Settings.getInstance().eliteEnemySpeedX;
        int speedY = Settings.getInstance().eliteEnemySpeedY;
        int hp = Settings.getInstance().eliteEnemyHp;
        return new EliteEnemy(locationX, locationY, speedX, speedY, hp);
    }
}
