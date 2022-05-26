package com.hit.aircraftwar.application.Activity;

import android.os.Bundle;

import com.hit.aircraftwar.aircraft.HeroAircraft;
import com.hit.aircraftwar.application.ImageManager;
import com.hit.aircraftwar.application.Settings;

public class CommonGameActivity extends GameActivity{
    @Override
    public void initGameMode() {
        // 获取背景
        background = ImageManager.BACKGROUND_IMAGE_3;

        // 同一时间出现敌机最大值
        Settings.getInstance().enemyMaxNumber = 8;
        // 英雄机
        HeroAircraft.getInstance().setHeroHp(2000);
        HeroAircraft.getInstance().setShootNum(2);
        // 精英机
        Settings.getInstance().eliteEnemyHp = 120;
        Settings.getInstance().eliteEnemyPower = 20;
        Settings.getInstance().eliteShootNum = 2;

        // 普通敌机
        Settings.getInstance().mobEnemyHp = 60;

        // 道具
        Settings.getInstance().propDropRate = 0.5;
        // Boss
        Settings.getInstance().isLeverUp = false;
        Settings.getInstance().bossHp = 1200;
        Settings.getInstance().bossPower = 20;
        Settings.getInstance().scoreToBoss = 100;
        Settings.getInstance().bossPower = 20;
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
                if(background != ImageManager.BACKGROUND_IMAGE_3){
                    background = ImageManager.BACKGROUND_IMAGE_3;
                }
                break;
            case 2 :
                if(background != ImageManager.BACKGROUND_IMAGE_4){
                    background = ImageManager.BACKGROUND_IMAGE_4;
                }
                break;
            default: background = ImageManager.BACKGROUND_IMAGE_3;
        }
    }

    @Override
    protected void difficultyLevelUp() {
        if(time % Settings.getInstance().timeToGameLevelUp == 0 && time > 0){
            if(!bossExistFlag && Settings.getInstance().mobEnemySpeedY < Settings.getInstance().maxMobSpeedY){
                Settings.getInstance().mobEnemySpeedY ++;
            }
        }
    }
}
