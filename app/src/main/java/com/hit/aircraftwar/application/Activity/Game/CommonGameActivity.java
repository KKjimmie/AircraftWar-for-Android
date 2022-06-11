package com.hit.aircraftwar.application.Activity.Game;

import com.hit.aircraftwar.aircraft.HeroAircraft;
import com.hit.aircraftwar.application.GameView;
import com.hit.aircraftwar.application.ImageManager;
import com.hit.aircraftwar.application.Settings;

public class CommonGameActivity extends GameActivity{
    @Override
    public void initGameMode() {
        // 获取背景
        Settings.getInstance().reset();

        background = ImageManager.BACKGROUND_IMAGE_3;

        // 同一时间出现敌机最大值
        Settings.getInstance().enemyMaxNumber = 8;
        // 英雄机
        Settings.getInstance().heroHp = 2000;
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
        Settings.getInstance().scoreToBoss = 500;
        Settings.getInstance().bossPower = 20;
    }

    @Override
    protected void changeBackground() {
        switch ((bossFactory.getBossLevel() % 3)){
            case 0 :
                if(GameView.background != ImageManager.BACKGROUND_IMAGE_2) {
                    GameView.background = ImageManager.BACKGROUND_IMAGE_2;
                }
                break;
            case 1 :
                if(GameView.background != ImageManager.BACKGROUND_IMAGE_3){
                    GameView.background = ImageManager.BACKGROUND_IMAGE_3;
                }
                break;
            case 2 :
                if(GameView.background != ImageManager.BACKGROUND_IMAGE_4){
                    GameView.background = ImageManager.BACKGROUND_IMAGE_4;
                }
                break;
            default: GameView.background = ImageManager.BACKGROUND_IMAGE_3;
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
