package com.hit.aircraftwar.props;

import com.hit.aircraftwar.application.Activity.MainActivity;
import com.hit.aircraftwar.application.Settings;
import com.hit.aircraftwar.basic.AbstractFlyingObject;
/**
 * 抽象道具类
 * @author 柯嘉铭
 */

public abstract class AbstractProp extends AbstractFlyingObject {

    /**
    设置道具y轴反弹次数
     */
    protected int bounceNum = Settings.getInstance().propBounceNum;

    public AbstractProp(int locationX, int locationY, int speedX, int speedY) {
        super(locationX, locationY, speedX, speedY);
    }

    /**
     * 抽象道具作用
     */
    public abstract void work();

    @Override
    public void forward () {
        // 让道具能够反弹
        super.forward();
        if (bounceNum >= 0 && (locationY <= 0 || locationY >= MainActivity.height)) {
            speedY = -speedY;
            bounceNum --;
        }
        // 没有反弹次数时，道具超出y轴边界消失
        if (bounceNum < 0 && (locationY <= 0 || locationY >= MainActivity.height)) {
            vanish();
        }

    }

}
