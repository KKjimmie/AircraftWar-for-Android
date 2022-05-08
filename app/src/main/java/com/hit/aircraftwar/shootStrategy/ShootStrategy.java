package com.hit.aircraftwar.shootStrategy;

import com.hit.aircraftwar.bullet.BaseBullet;


import java.util.List;

/**
 * 策略模式实现子弹射击效果
 * 策略接口
 * @author 柯嘉铭
 */
public interface ShootStrategy {
    /**
     * 射击模式
     * @param isHero 判断是否为英雄机
     * @param locationX x
     * @param locationY y
     * @param direction 方向
     * @param power 火力
     * @param shootNum 子弹数量
     * @return 子弹列表
     */
    List<BaseBullet> shootMode(boolean isHero, int locationX, int locationY, int direction, int power, int shootNum);
}
