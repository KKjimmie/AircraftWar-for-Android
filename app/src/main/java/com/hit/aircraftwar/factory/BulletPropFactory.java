package com.hit.aircraftwar.factory;

import com.hit.aircraftwar.application.Settings;
import com.hit.aircraftwar.props.AbstractProp;
import com.hit.aircraftwar.props.BulletProp;



/**子弹道具类工厂
 * @author 柯嘉铭
 */
public class BulletPropFactory implements ProduceProp {
    @Override
    public AbstractProp produceProp(int locationX, int locationY) {
        int speedX = Settings.getInstance().propSpeedX;
        int speedY = Settings.getInstance().propSpeedY;
        return new BulletProp(locationX, locationY, speedX, speedY);
    }
}
