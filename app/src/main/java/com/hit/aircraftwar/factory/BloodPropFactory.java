package com.hit.aircraftwar.factory;

import com.hit.aircraftwar.application.Settings;
import com.hit.aircraftwar.props.AbstractProp;
import com.hit.aircraftwar.props.BloodProp;


/**
 * 加血道具工厂
 * @author 柯嘉铭
 */
public class BloodPropFactory implements ProduceProp {
    @Override
    public AbstractProp produceProp(int locationX, int locationY) {
        int speedX = Settings.getInstance().propSpeedX;
        int speedY = Settings.getInstance().propSpeedY;
        return new BloodProp(locationX, locationY, speedX, speedY);
    }
}
