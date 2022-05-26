package com.hit.aircraftwar.music;

import android.content.Context;
import android.content.Intent;


public class MusicManager {

    private static int name = 0;

    public static int getName() {
        return name;
    }

    public static void setName(int name) {
        MusicManager.name = name;
    }

}

