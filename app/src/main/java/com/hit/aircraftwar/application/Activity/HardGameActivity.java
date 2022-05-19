package com.hit.aircraftwar.application.Activity;

import android.os.Bundle;

import com.hit.aircraftwar.aircraft.HeroAircraft;
import com.hit.aircraftwar.application.ImageManager;
import com.hit.aircraftwar.application.Settings;

public class HardGameActivity extends GameActivity{

    @Override
    protected void initGameMode() {
        // 获取背景
        background = ImageManager.BACKGROUND_IMAGE_5;

        Settings.getInstance().enemyMaxNumber = 10;
        // 英雄机
        HeroAircraft.getInstance().setHeroHp(5000);
        HeroAircraft.getInstance().setShootNum(1);
        Settings.getInstance().bulletSpeedY = 15;
        Settings.getInstance().enemyMaxNumber = 10;
        Settings.getInstance().isDecreaseShootNum = true;

        // 精英机
        Settings.getInstance().eliteEnemyHp = 60;
        Settings.getInstance().eliteEnemyPower = 10;
        Settings.getInstance().eliteEnemySpeedX = 5;
        Settings.getInstance().eliteShootNum = 1;

        // 普通敌机
        Settings.getInstance().mobEnemyHp = 60;
        Settings.getInstance().mobEnemySpeedY = 5;

        // 道具
        Settings.getInstance().propSpeedX = 5;
        Settings.getInstance().propDropRate = 0.5;
        Settings.getInstance().propBounceNum = 2;

        // Boss
        Settings.getInstance().isLeverUp = true;
        Settings.getInstance().bossHp = 600;
        Settings.getInstance().bossPower = 10;
        Settings.getInstance().scoreToBoss = 1000;
    }

    @Override
    protected void changeBackground() {
        switch ((bossFactory.getBossLevel() % 3)){
            case 0 :
                if(background != ImageManager.BACKGROUND_IMAGE_2) {
                    background = ImageManager.BACKGROUND_IMAGE_2;
                }
                break;
            case 1 :
                if(background != ImageManager.BACKGROUND_IMAGE_5){
                    background = ImageManager.BACKGROUND_IMAGE_5;
                }
                break;
            case 2 :
                if(background != ImageManager.BACKGROUND_IMAGE_4){
                    background = ImageManager.BACKGROUND_IMAGE_4;
                }
                break;
            default: background = ImageManager.BACKGROUND_IMAGE_5;
        }
    }

    @Override
    protected void difficultyLevelUp() {
        if(! bossExistFlag && time % Settings.getInstance().timeToGameLevelUp == 0 && time > 0){
            if(Settings.getInstance().timeToElite > 5 * Settings.getInstance().cycleDuration){
                Settings.getInstance().timeToElite -= (int )(1.5 * cycleDuration);
            }
            if(Settings.getInstance().eliteEnemySpeedX < Settings.getInstance().maxEliteSpeedX){
                Settings.getInstance().eliteEnemySpeedX ++;
            }
            if(Settings.getInstance().eliteEnemySpeedY < Settings.getInstance().maxEliteSpeedY){
                Settings.getInstance().eliteEnemySpeedY ++;
            }
            if(Settings.getInstance().mobEnemySpeedY <= Settings.getInstance().maxMobSpeedY){
                Settings.getInstance().mobEnemySpeedY ++;
            }
            if(time % (5 *Settings.getInstance().timeToGameLevelUp) == 0){
                if(Settings.getInstance().eliteEnemyHp <= Settings.getInstance().maxEliteHp){
                    Settings.getInstance().eliteEnemyHp += 10;
                }
                if(Settings.getInstance().eliteShootNum < Settings.getInstance().maxEliteShootNum){
                    Settings.getInstance().eliteShootNum ++;
                }
                if(Settings.getInstance().eliteEnemyPower < 40){
                    Settings.getInstance().eliteEnemyPower += 10;
                }
                if(Settings.getInstance().propDropRate > 0.2){
                    Settings.getInstance().propDropRate -= 0.05;
                }
            }
        }
    }
}
