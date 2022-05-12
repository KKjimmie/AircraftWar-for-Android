package com.hit.aircraftwar.props;

import com.hit.aircraftwar.application.ImageManager;
import com.hit.aircraftwar.basic.CanBoom;
import com.hit.aircraftwar.music.MySoundPool;

import java.util.ArrayList;
import java.util.List;

/**
 * 爆炸道具类
 * 使用观察者模式实现除boss机外的敌机以及子弹消失
 * @author 柯嘉铭
 */
public class BombProp extends AbstractProp{
    private List<CanBoom> canBoomList = new ArrayList<>();

    public BombProp(int locationX, int locationY, int speedX, int speedY) {
        super(locationX, locationY, speedX, speedY);
        this.bitmap = ImageManager.BOMB_PROP_BITMAP;
    }

    public void addCanBoom(CanBoom canBoom){
        canBoomList.add(canBoom);
    }

    public void removeCanBoom(CanBoom canBoom){
        canBoomList.remove(canBoom);
    }

    public void notifyAllToBoom(){
        for(CanBoom canBoom : canBoomList){
            canBoom.boom();
        }
    }

    @Override
    public void work() {
        notifyAllToBoom();
        // TODO:播放爆炸音效
//        MusicController.setBombExplosionBgm();
        MySoundPool.playSound(MySoundPool.BOMB_EXPLOSION, false);
    }
}
