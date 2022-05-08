package com.hit.aircraftwar.factory;

import com.hit.aircraftwar.aircraft.AbstractAircraft;
import com.hit.aircraftwar.aircraft.Boss;
import com.hit.aircraftwar.application.Activity.MainActivity;
import com.hit.aircraftwar.application.Settings;


/**
 * Boss工厂
 * @author 柯嘉铭
 */
public class BossFactory implements ProduceEnemy {

    private int bossLevel = 1;

    private void bossLevelUp() {
        System.out.println("Boss等级：" + bossLevel);
        this.bossLevel ++;
        if(Settings.getInstance().isLeverUp){
            if(Settings.getInstance().bossPower < 100){
                Settings.getInstance().bossPower += 10;
            }
        }
    }

    public int getBossLevel() {
        return bossLevel;
    }

    public void setBossLevel(int bossLevel) {
        this.bossLevel = bossLevel;
    }

    @Override
    public AbstractAircraft produceEnemy() {
        int locationX = (int) (Math.random() * (MainActivity.width));
        int locationY = (int) (Math.random() * MainActivity.height * 0.1);
        int speedX = Settings.getInstance().bossSpeedX;
        int speedY = Settings.getInstance().bossSpeedY;
        int hp = Settings.getInstance().bossHp * bossLevel;
        bossLevelUp();
        return new Boss(locationX, locationY, speedX, speedY, hp);
    }
}
