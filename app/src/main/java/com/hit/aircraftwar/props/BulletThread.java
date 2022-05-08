package com.hit.aircraftwar.props;

import com.hit.aircraftwar.aircraft.HeroAircraft;
import com.hit.aircraftwar.shootStrategy.ScatteredShoot;

/**
 * 子弹线程，用于实现子弹散射一定时间，还原为直射
 * @author 柯嘉铭
 */
public class BulletThread extends Thread{
    @Override
    public void run() {
        synchronized (this){
            HeroAircraft.getInstance().setStrategy(new ScatteredShoot());
            try {
                wait(10 * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            HeroAircraft.getInstance().resetShootNum();
        }
    }
}
