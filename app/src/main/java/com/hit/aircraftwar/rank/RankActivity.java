package com.hit.aircraftwar.rank;


import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
import java.util.concurrent.atomic.AtomicBoolean;

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
    // 记录当前时间
    String currentTime;
    // 排行榜listView
    ListView listView;

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
        // 显示数据库
        show();

        // 监控listview点击事件

        listView.setOnItemClickListener((adapterView, view, position, id) -> {
            // 弹窗确认操作
            AlertDialog alertDialog = new AlertDialog.Builder(RankActivity.this)
                    .setTitle("删除？")
                    .setMessage("确定删除这条数据？")
                    .setIcon(R.drawable.hero)
                    .setPositiveButton("确定", (dialog, which) -> delete(rankLineList.get(position).getTime()))
                    .setNegativeButton("取消", null)
                    .create();
            alertDialog.show();
        });

    }

    /**
     * 记录一条排名信息
     */
    public void RECORD(View view){
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
        currentTime = formatter.format(LocalDateTime.now());
        values.put("time", currentTime);
        writableDatabase = databaseHelper.getWritableDatabase();
        writableDatabase.insert(Settings.getInstance().getDiff() +"rank", null, values);
        // 标志已经记录过信息
        isRecord = true;
        show();
    }

    /**
     * 展现排行榜信息
     */
    public void show(){
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
        cursor.close();
        writableDatabase.close();
        // 拿到listview对象
        listView = (ListView) findViewById(R.id.list_view);
        // 设置适配器
        MyAdapter myAdapter = new MyAdapter(RankActivity.this, R.layout.rank_line, rankLineList);
        listView.setAdapter(myAdapter);
    }

    /**
     * 删除一条排行榜信息
     */
    public void delete(String time){
        writableDatabase = databaseHelper.getWritableDatabase();
        writableDatabase.delete(Settings.getInstance().getDiff() +"rank",
                "time=?", new String[]{time});
        Toast.makeText(getApplicationContext(), "再按一次退出游戏",
                Toast.LENGTH_SHORT).show();
        show();
    }

    /**
     * 返回开始页面
     */
    public void RETURN_MAIN(View view){
        finish();
    }


    /**
     * 适配器类
     */
    class MyAdapter extends ArrayAdapter<RankLine> {
        private int resourceId;

        public MyAdapter(Context context, int textViewResourceId, List<RankLine> object){
            super(context,textViewResourceId,object);
            resourceId=textViewResourceId;
        }

        //由系统调用，返回一个view对象作为listview的条目
        /*
         * position：本次getView方法调用所返回的view对象在listView中处于第几个条目，position的值就为多少
         * */
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            //获取集合中的元素
            RankLine rankLine = rankLineList.get(position);
            View view;
            ViewHolder viewHolder;
            if(convertView == null){
                view = LayoutInflater.from(getContext()).inflate(resourceId,parent,false);
                viewHolder = new ViewHolder();
                viewHolder.ranking = (TextView) view.findViewById(R.id.Ranking);
                viewHolder.name = (TextView) view.findViewById(R.id.UserName);
                viewHolder.score = (TextView) view.findViewById(R.id.Score);
                viewHolder.time = (TextView) view.findViewById(R.id.Time);
                view.setTag(viewHolder);
            }else {
                view = convertView;
                viewHolder=(ViewHolder)view.getTag();
            }

            TextView ranking = (TextView) view.findViewById(R.id.Ranking);
            TextView name = (TextView) view.findViewById(R.id.UserName);
            TextView score = (TextView) view.findViewById(R.id.Score);
            TextView time = (TextView) view.findViewById(R.id.Time);

            // 设置textview内容
            int rank = position + 1;
            String _rank = rank + "";
            String _score = rankLine.getScore() + "";
            ranking.setText(_rank);
            name.setText(rankLine.getName());
            score.setText(_score);
            time.setText(rankLine.getTime());

            viewHolder.ranking.setText(_rank);
            viewHolder.name.setText(rankLine.getName());
            viewHolder.score.setText(_score);
            viewHolder.time.setText(rankLine.getTime());

            // 将新录入的数据变红
            if(rankLine.getTime().equals(currentTime)){
                name.setTextColor(Color.RED);
            }else{
                name.setTextColor(Color.BLACK);
            }
            return view;
        }

    }

    // 用于保存几个textview的类
    static class ViewHolder{
        TextView ranking;
        TextView name;
        TextView score;
        TextView time;
    }
}