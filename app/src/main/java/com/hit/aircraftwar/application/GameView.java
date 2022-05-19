package com.hit.aircraftwar.application;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.hit.aircraftwar.aircraft.AbstractAircraft;
import com.hit.aircraftwar.aircraft.HeroAircraft;
import com.hit.aircraftwar.application.Activity.GameActivity;
import com.hit.aircraftwar.application.Activity.MainActivity;
import com.hit.aircraftwar.basic.AbstractFlyingObject;
import com.hit.aircraftwar.bullet.BaseBullet;
import com.hit.aircraftwar.props.AbstractProp;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class GameView extends SurfaceView implements SurfaceHolder.Callback, Runnable
{
    // SurfaceHolder
    private SurfaceHolder mSurfaceHolder;
    // 画布
    private Canvas mCanvas;
    //画笔
    Paint paint = new Paint();
    // 子线程标志位
    private boolean isDrawing;
    // 用于绘制的线程
    private Thread thread;

    private int screenWidth = MainActivity.width;
    private int screenHeight = MainActivity.height;

    private HeroAircraft heroAircraft;
    private List<AbstractAircraft> enemyAircrafts;
    private List<BaseBullet> heroBullets;
    private List<BaseBullet> enemyBullets;
    private List<AbstractProp> props;
    private int bg;

    public GameView(Context context, int bg, HeroAircraft heroAircraft, List<AbstractAircraft> enemyAircrafts, List<BaseBullet> heroBullets, List<BaseBullet> enemyBullets, List<AbstractProp> props)
    {
        super(context);

        this.heroAircraft = heroAircraft;
        this.enemyAircrafts = enemyAircrafts;
        this.heroBullets = heroBullets;
        this.enemyBullets = enemyBullets;
        this.props = props;
        this.bg = bg;

        init();
    }

    private void init()
    {
        mSurfaceHolder = getHolder();
        mSurfaceHolder.addCallback(this);
        setFocusable(true);
        setFocusableInTouchMode(true);
        this.setKeepScreenOn(true);
        //设置是否抗锯齿
        paint.setAntiAlias(true);
        // 处理屏幕闪烁问题
        paint.setDither(true);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder)
    {//创建
        isDrawing = true;
        MainActivity.executorService.schedule(this, 0, TimeUnit.MILLISECONDS);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height)
    {//改变
        screenHeight = height;
        screenWidth = width;
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder)
    {
        //销毁
        isDrawing = false;
    }

    @Override
    public void run()
    {
        while (isDrawing){
            draw();
        }
    }


    Bitmap bitmap;
    private void draw()
    {
        try
        {
            mCanvas = mSurfaceHolder.lockCanvas();
            if(mSurfaceHolder == null || mCanvas == null){
                return;
            }
            synchronized (mSurfaceHolder)
            {
                // 这里进行内容的绘制
                paintBackground();

                // 先绘制子弹和道具，后绘制飞机
                // 这样子弹显示在飞机的下层
                paintImageWithPositionRevised(enemyBullets);
                paintImageWithPositionRevised(heroBullets);
                paintImageWithPositionRevised(props);
                paintImageWithPositionRevised(enemyAircrafts);

                bitmap = heroAircraft.getBitmap();
                mCanvas.drawBitmap(bitmap, heroAircraft.getLocationX() - bitmap.getWidth() / 2, heroAircraft.getLocationY() - bitmap.getHeight() / 2, paint);

                //绘制得分和生命值
                paintScoreAndLife();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        mSurfaceHolder.unlockCanvasAndPost(mCanvas);
    }

    private int backgroundTop = 0;
    /**
     * 绘制背景，图片滚动
     */
    private void paintBackground(){
        bitmap = BitmapFactory.decodeResource(getResources(), bg);
        mCanvas.drawBitmap(bitmap, 0, backgroundTop - screenHeight, paint);
        mCanvas.drawBitmap(bitmap, 0, backgroundTop, paint);
        backgroundTop += 5;
        if (backgroundTop >= screenHeight)
        {
            backgroundTop -= screenHeight;
        }
    }

    private void paintImageWithPositionRevised(List<? extends AbstractFlyingObject> objects)
    {
        if (objects.size() == 0)
        {
            return;
        }
        for (AbstractFlyingObject object : objects)
        {
            bitmap = object.getBitmap();
            mCanvas.drawBitmap(bitmap, object.getLocationX() - bitmap.getWidth() / 2, object.getLocationY() - bitmap.getHeight() / 2, paint);
        }
    }

    /**
     * 绘制得分和生命值
     */
    private void paintScoreAndLife()
    {
        int x = 30;
        int y = 75;
        Paint txt = new Paint();
        txt.setColor(Color.RED);
        txt.setTextSize(50);
        txt.setAntiAlias(true);
        mCanvas.drawText("SCORE:" + GameActivity.score, x, y, txt);
        y = y + 50;
        mCanvas.drawText("LIFE:" + heroAircraft.getHp(), x, y, txt);
    }
}
