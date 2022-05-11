package com.hit.aircraftwar.aircraft;


import com.hit.aircraftwar.application.Activity.MainActivity;
import com.hit.aircraftwar.application.ImageManager;
import com.hit.aircraftwar.application.Settings;
import com.hit.aircraftwar.bullet.BaseBullet;
import com.hit.aircraftwar.shootStrategy.DirectShoot;

import java.util.List;

/**
 * 英雄飞机，游戏玩家操控
 * @author hitsz
 */
public class HeroAircraft extends AbstractAircraft {

    /**
     * shootNum 子弹一次发射的数量
     */
    private int shootNum = Settings.getInstance().initShootNum;
    /**
     * power 子弹伤害
     */
    private int power = Settings.getInstance().heroPower;
    /**
     * direction 子弹射击方向 (向上发射：-1，向下发射：1)
     */
    private int direction = -1;
    /**
     * maxShootNUm 子弹一次最多发射的数量
     */
    private final int maxShootNum = Settings.getInstance().maxShootNum;
    /**
     * maxHeroHp 英雄机最大血量
     */
    private final int maxHeroHp = Settings.getInstance().maxHeroHp;
    private volatile static HeroAircraft instance = null;


    /**
     * @param locationX 英雄机位置x坐标
     * @param locationY 英雄机位置y坐标
     * @param speedX 英雄机射出的子弹的基准速度（英雄机无特定速度）
     * @param speedY 英雄机射出的子弹的基准速度（英雄机无特定速度）
     * @param hp    初始生命值
     */
    private HeroAircraft(int locationX, int locationY, int speedX, int speedY, int hp) {
        super(locationX, locationY, speedX, speedY, hp);
        this.setStrategy(new DirectShoot());
        this.bitmap = ImageManager.HERO_BITMAP;
    }

    public static HeroAircraft getInstance(){
        if (instance == null) {
            synchronized (HeroAircraft.class){
                if (instance == null) {
                    int locationX = MainActivity.width / 2;
                    int locationY = MainActivity.height - 90;
                    int speedX = Settings.getInstance().propSpeedX;
                    int speedY = Settings.getInstance().heroSpeedY;
                    int hp = Settings.getInstance().heroHp;
                    instance = new HeroAircraft(locationX, locationY, speedX, speedY, hp);
                }
            }
        }
        return instance;
    }
    public static HeroAircraft getInstance(int locationX, int locationY, int speedX, int speedY, int hp){
        if (instance == null) {
            synchronized (HeroAircraft.class){
                if (instance == null) {
                    instance = new HeroAircraft(locationX, locationY, speedX, speedY, hp);
                }
            }
        }
        return instance;
    }

    public int getShootNum() {
        return shootNum;
    }

    public int getMaxShootNum() {
        return maxShootNum;
    }

    @Override
    public void forward() {
        // 英雄机由鼠标控制，不通过forward函数移动
    }

    @Override
    public List<BaseBullet> shoot() {
        return strategy.shootMode(true, this.locationX, this.locationY, this.direction, this.power, this.shootNum);
    }


    public void addShootNum(int num){
        if (this.shootNum <this.maxShootNum && this.shootNum >=1){
            this.shootNum += num;
        }
    }

    public void setShootNum(int num) {
        this.shootNum = num;
    }

    public void resetShootNum(){
        strategy = new DirectShoot();
    }

    public void setHeroHp(int hp){
        this.hp = hp;
    }

    public void decreaseShootNum() {
        shootNum --;
    }

}
