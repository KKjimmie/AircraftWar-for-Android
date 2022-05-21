package com.hit.aircraftwar.rank;


import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.hit.aircraftwar.R;
import com.hit.aircraftwar.application.Settings;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * @author 柯嘉铭
 * 显示排行榜的activity
 */
public class RankActivity extends AppCompatActivity {

    private DatabaseHelper databaseHelper;
    List<RankLine> rankLineList;
    SQLiteDatabase writableDatabase = null;
    // 防止重复记录
    private boolean isRecord = false;
    // 排行榜textview
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rank);
        // 设置全屏显示
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Objects.requireNonNull(getSupportActionBar()).hide();

        // 设置排行榜名字
        textView = (TextView) findViewById(R.id.rank_text_view);
        switch(Settings.getInstance().getDiff()){
            case "easy": textView.setText("简单模式排行榜");
                break;
            case "common": textView.setText("普通模式排行榜");
                break;
            case "hard": textView.setText("困难模式排行榜");
                break;
            default: textView.setText("排行榜");
        }

        // 步骤1：创建DatabaseHelper对象
        // 注：此时还未创建数据库
        databaseHelper = new DatabaseHelper(RankActivity.this,Settings.getInstance().getDiff() +"rank", null, 1);

        // 步骤2：真正创建 / 打开数据库
        writableDatabase = databaseHelper.getWritableDatabase(); // 创建 or 打开 可读/写的数据库
    }

    /**
     * 记录一条排名信息
     */
    public void recordRank(View view){
        if(isRecord){
            Toast.makeText(this, "已经记录过信息了", Toast.LENGTH_SHORT).show();
            return;
        }
        ContentValues values = new ContentValues();
        // 获取数据
        Intent intent = getIntent();
        int score = intent.getIntExtra("score", 0);
        values.put("name", "testname");// 后续实现登录后，再获取用户名
        values.put("score", score);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd HH:mm:ss");
        String currentTime = formatter.format(LocalDateTime.now());
        values.put("time", currentTime);
        writableDatabase = databaseHelper.getWritableDatabase();
        writableDatabase.insert(Settings.getInstance().getDiff() +"rank", null, values);
        // 标志已经记录过信息
        isRecord = true;
    }

    /**
     * 展现排行榜信息
     */
    public void show(View view){
        writableDatabase = databaseHelper.getWritableDatabase();
        Cursor cursor = writableDatabase.query(Settings.getInstance().getDiff() +"rank",
                null, null, null, null, null, null);
        // 先清除原有数据
        rankLineList = new ArrayList<>();
        while (cursor.moveToNext()) {
            @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex("name"));
            @SuppressLint("Range") int score = cursor.getInt(cursor.getColumnIndex("score"));
            @SuppressLint("Range") String time = cursor.getString(cursor.getColumnIndex("time"));

            RankLine rankLine = new RankLine(name, score, time);
            rankLineList.add(rankLine);
        }
        Collections.sort(rankLineList);
        if(cursor != null) {
            cursor.close();
        }
        writableDatabase.close();
        // 拿到listview对象
        ListView listView = (ListView) findViewById(R.id.list_view);
        // 设置适配器
        listView.setAdapter(new MyAdapter());
    }


    //适配器类
    class MyAdapter extends BaseAdapter {

        //获取集合中有多少条元素,由系统调用
        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return rankLineList.size();
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return 0;
        }

        //由系统调用，返回一个view对象作为listview的条目
        /*
         * position：本次getView方法调用所返回的view对象在listView中处于第几个条目，position的值就为多少
         * */
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView tv = new TextView(RankActivity.this);
            tv.setTextSize(18);
            //获取集合中的元素
            RankLine rankLine = rankLineList.get(position);
            tv.setText(rankLine.toString());

            return tv;
        }

    }
}