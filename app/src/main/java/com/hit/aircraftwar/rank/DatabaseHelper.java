package com.hit.aircraftwar.rank;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.hit.aircraftwar.application.Settings;

/**
 * @author 柯嘉铭
 * 可以看成创建数据库的工厂，是工具类
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    // 数据库版本号
    private static Integer Version = 1;
    public DatabaseHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        // 参数说明
        // context：上下文对象
        // name：数据库名称
        // param：一个可选的游标工厂（通常是 Null）
        // version：当前数据库的版本，值必须是整数并且是递增的状态
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //创建数据库sql语句 并 执行
        String sql = "create table " + Settings.getInstance().getDiff() +"rank" +"(_id integer primary key autoincrement, name varchar(64), score integer, time varchar(64))";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//        // 参数说明：
//        // db ： 数据库
//        // oldVersion ： 旧版本数据库
//        // newVersion ： 新版本数据库
    }

}
