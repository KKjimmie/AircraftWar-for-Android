package com.hit.aircraftwar.application.Activity.Game;

import com.hit.aircraftwar.aircraft.HeroAircraft;
import com.hit.aircraftwar.application.ImageManager;
import com.hit.aircraftwar.application.Settings;

public class EasyGameActivity extends GameActivity{
    @Override
    protected void initGameMode() {
        Settings.getInstance().reset();
        // 获取背景
        background = ImageManager.BACKGROUND_IMAGE_1;
        // 英雄机
        Settings.getInstance().heroHp = 10000;
        HeroAircraft.getInstance().setShootNum(3);
        Settings.getInstance().maxHeroHp = 10000;
        Settings.getInstance().scoreToBoss = Integer.MAX_VALUE;
    }

    @Override
    protected void changeBackground() {

    }

    @Override
    protected void difficultyLevelUp() {
    }
}
