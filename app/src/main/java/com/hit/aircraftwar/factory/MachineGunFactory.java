package com.hit.aircraftwar.factory;

import com.hit.aircraftwar.aircraft.AbstractAircraft;
import com.hit.aircraftwar.aircraft.MachineGun;
import com.hit.aircraftwar.aircraft.MobEnemy;
import com.hit.aircraftwar.application.Activity.MainActivity;
import com.hit.aircraftwar.application.Settings;

public class MachineGunFactory implements ProduceEnemy{
    @Override
    public AbstractAircraft produceEnemy() {
        int locationX = (int) (Math.random() * (MainActivity.width));
        int locationY = (int) (MainActivity.height * 0.9);
        int speedX = 10;
        int speedY = 0;
        return new MachineGun(locationX, locationY, speedX, speedY, 2000);
    }
}
