package com.hit.aircraftwar.application.Activity.Game;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;

import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.WindowManager;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.hit.aircraftwar.R;
import com.hit.aircraftwar.aircraft.AbstractAircraft;
import com.hit.aircraftwar.aircraft.Boss;
import com.hit.aircraftwar.aircraft.EliteEnemy;
import com.hit.aircraftwar.aircraft.HeroAircraft;
import com.hit.aircraftwar.application.Activity.MainActivity;
import com.hit.aircraftwar.application.GameView;
import com.hit.aircraftwar.application.Settings;
import com.hit.aircraftwar.basic.AbstractFlyingObject;
import com.hit.aircraftwar.basic.CanBoom;
import com.hit.aircraftwar.bullet.BaseBullet;
import com.hit.aircraftwar.factory.BossFactory;
import com.hit.aircraftwar.factory.EliteEnemyFactory;
import com.hit.aircraftwar.factory.MachineGunFactory;
import com.hit.aircraftwar.factory.MobEnemyFactory;
import com.hit.aircraftwar.login.LoginActivity;
import com.hit.aircraftwar.music.MySoundPool;
import com.hit.aircraftwar.props.AbstractProp;
import com.hit.aircraftwar.props.BloodProp;
import com.hit.aircraftwar.props.BombProp;
import com.hit.aircraftwar.rank.RankActivity;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

public abstract class GameActivity extends AppCompatActivity {

    /**
     * 时间间隔(ms)，控制刷新频率
     */
    protected int timeInterval = Settings.getInstance().timeInterval;
    protected HeroAircraft heroAircraft;
    protected List<AbstractAircraft> enemyAircrafts;
    protected List<AbstractAircraft> MachineGun;
    protected List<BaseBullet> heroBullets;
    protected List<BaseBullet> enemyBullets;
    protected List<AbstractProp> props;
    protected int enemyMaxNumber = 5;

    public static boolean gameOverFlag = false;
    public static boolean bossExistFlag = false; // 标志Boss是否存在
    public static int score = 0;
    protected int time = 0;
    // 标记是否需要进行背景音乐的切换
    private boolean bgmChange = false;
    /**
     * 周期（ms)
     * 指示子弹的发射、敌机的产生频率
     */
    protected int cycleDuration = 600;
    protected int cycleTime = 0;
    /**
     * 创建三个工厂实例
     */
    protected final MobEnemyFactory mobEnemyFactory = new MobEnemyFactory();
    protected final EliteEnemyFactory eliteEnemyFactory = new EliteEnemyFactory();
    protected final BossFactory bossFactory = new BossFactory();
    protected final MachineGunFactory machineGunFactory = new MachineGunFactory();
    protected GameView gameView;
    // 背景图片
    public int background;

    // 背景音乐
    private MediaPlayer bgmPlayer;

    // 道具使用开关
    protected  boolean itemflag1;
    protected  boolean itemflag2;
    protected  boolean itemflag3;
    protected  boolean itemflag4;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initGameMode();
        initGame();
        action();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        heroAircraft.setLocation((int)event.getRawX(),(int)(event.getRawY() - 100));
        return true;
    }


    /**
     * 初始化相关设置
     */
    protected void initGame() {
        // 设置全屏显示
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Objects.requireNonNull(getSupportActionBar()).hide();
        // 设置道具使用
        if (Settings.getInstance().getItemState()){
            if(LoginActivity.gameUser.getItem1()>0){
                LoginActivity.gameUser.useItem1();
                itemflag1 = true;
            }else{
                itemflag1 = false;
            }
            if(LoginActivity.gameUser.getItem2()>0){
                LoginActivity.gameUser.useItem2();
                itemflag2 = true;
            }else{
                itemflag2 = false;
            }
            if(LoginActivity.gameUser.getItem3()>0){
                LoginActivity.gameUser.useItem3();
                itemflag3 = true;
            }else{
                itemflag3 = false;
            }
            if(LoginActivity.gameUser.getItem4()>0){
                LoginActivity.gameUser.useItem4();
                itemflag4 = true;
            }else{
                itemflag4 = false;
            }
        }
        // 重置一下分数
        score = 0;
        gameOverFlag = false;
        heroAircraft = HeroAircraft.getInstance();
        // 由于英雄机为单例模式，因此需要重置一下英雄机设置
        HeroAircraft.getInstance().resetHeroAircraft();
        enemyAircrafts = new LinkedList<>();
        heroBullets = new LinkedList<>();
        enemyBullets = new LinkedList<>();
        MachineGun = new LinkedList<>();
        props = new LinkedList<>();
        gameView = new GameView(this, background, heroAircraft, enemyAircrafts, heroBullets, enemyBullets, props,MachineGun);
        setContentView(gameView);
    }

    // 游戏入口
    public void action() {

        // 定时任务：绘制、对象产生、碰撞判定、击毁及结束判定
        Runnable task = () -> {
            time += timeInterval;
//            Log.d("222", time + "");

            // 周期性执行（控制频率）
            if (timeCountAndNewCycleJudge()) {
                // 新敌机产生
                this.produceEnemy();
                // 飞机射出子弹
                this.shootAction();
                // 检查是否达到产生Boss条件并根据条件产生Boss
                this.gotoBoss();
                this.changeBackground();

            }

            // 子弹移动
            this.bulletsMoveAction();

            // 飞机移动
            this.aircraftsMoveAction();

            // 道具移动
            this.propsMoveAction();

            // 撞击检测
            this.crashCheckAction();

            // 后处理
            this.postProcessAction();

            // 游戏结束检查
            this.isGameOver();

            // 难度提升
            difficultyLevelUp();
        };
        /**
         * 以固定延迟时间进行执行
         * 本次任务执行完成后，需要延迟设定的延迟时间，才会执行新的任务
         */
        MainActivity.executorService.scheduleWithFixedDelay(task, timeInterval, timeInterval, TimeUnit.MILLISECONDS);
    }

    /**
     * 初始化游戏相关参数设置
     */
    protected void initGameMode() {

    }

    /**
     * 根据Boss等级修改游戏背景
     */
    protected void changeBackground() {

    }

    /**
     * 难度提升
     */
    protected void difficultyLevelUp() {

    }

    //***********************
    //      Action 各部分
    //***********************

    private boolean timeCountAndNewCycleJudge() {
        cycleTime += timeInterval;
//        Log.d("111", cycleTime + "");
        if (cycleTime >= cycleDuration && cycleTime - timeInterval < cycleTime) {
            // 跨越到新的周期
            cycleTime %= cycleDuration;
            return true;
        } else {
            return false;
        }
    }

    /**
     * 产生敌机
     */
    private void produceEnemy() {
        if (!bossExistFlag) {
            // 隔一定的时间周期，产生精英敌机
            if (time % Settings.getInstance().timeToElite == 0){
                enemyAircrafts.add(eliteEnemyFactory.produceEnemy());
            }else if (enemyAircrafts.size() < enemyMaxNumber){
                enemyAircrafts.add(mobEnemyFactory.produceEnemy());
            }
        }
    }

    /**
     * 检查是否达到产生Boss条件
     */
    private void gotoBoss(){
        if (! bossExistFlag && score >= Settings.getInstance().scoreToBoss * bossFactory.getBossLevel()){
            enemyAircrafts.add(bossFactory.produceEnemy());
            bossExistFlag = true;
            bgmChange = true;
        }
        // 播放boss音乐
        playBgm(bgmChange);

    }

    /**
     * 射击
     */
    private void shootAction() {
        for(AbstractAircraft enemyAircraft : enemyAircrafts){
            if (enemyAircraft instanceof EliteEnemy || enemyAircraft instanceof Boss){
                enemyBullets.addAll(enemyAircraft.shoot());
            }
        }

        // 英雄射击
        heroBullets.addAll(heroAircraft.shoot());
        MySoundPool.playSound(MySoundPool.BULLET, false);
        for(AbstractAircraft gun:MachineGun){
         heroBullets.addAll(gun.shoot());
        }
    }

    /**
     * 子弹移动
     */
    private void bulletsMoveAction() {
        for (BaseBullet bullet : heroBullets) {
            bullet.forward();
        }
        for (BaseBullet bullet : enemyBullets) {
            bullet.forward();
        }
    }

    /**
     * 敌机移动
     */
    private void aircraftsMoveAction() {
        for (AbstractAircraft enemyAircraft : enemyAircrafts) {
            enemyAircraft.forward();
        }
        for(AbstractAircraft gun:MachineGun){
            gun.forward();
        }
    }

    /**
     * 道具移动
     */
    private void propsMoveAction() {
        for (AbstractProp prop : props){
            prop.forward();
        }

    }


    /**
     * 碰撞检测：
     * 1. 敌机攻击英雄
     * 2. 英雄攻击/撞击敌机
     * 3. 英雄获得补给
     */
    private void crashCheckAction() {
        for (BaseBullet bullet : enemyBullets) {
            if (bullet.notValid()) {
                continue;
            }
            if (heroAircraft.crash(bullet)) {
                MySoundPool.playSound(MySoundPool.BULLET_HIT, false);
                heroAircraft.decreaseHp(bullet.getPower());
                if(Settings.getInstance().isDecreaseShootNum && heroAircraft.getShootNum() > 1){
                    heroAircraft.decreaseShootNum();
                }
                bullet.vanish();
            }
        }

        // 英雄子弹攻击敌机
        for (BaseBullet bullet : heroBullets) {
            if (bullet.notValid()) {
                continue;
            }
            for (AbstractAircraft enemyAircraft : enemyAircrafts) {
                if (enemyAircraft.notValid()) {
                    // 已被其他子弹击毁的敌机，不再检测
                    // 避免多个子弹重复击毁同一敌机的判定
                    continue;
                }
                if (enemyAircraft.crash(bullet)) {
                    MySoundPool.playSound(MySoundPool.BULLET_HIT, false);
                    // 敌机撞击到英雄机子弹
                    // 敌机损失一定生命值
                    enemyAircraft.decreaseHp(bullet.getPower());
                    bullet.vanish();
                    if (enemyAircraft.notValid()) {
                        if (enemyAircraft instanceof EliteEnemy) {
                            AbstractProp prop = ((EliteEnemy) enemyAircraft).genProp();
                            if (prop != null) {
                                props.add(prop);
                            }
                            score += 30;
                        } else if (enemyAircraft instanceof Boss) {
                            AbstractProp prop = ((Boss) enemyAircraft).genProp();
                            if (prop != null) {
                                props.add(prop);
                            }
                            score += 100;
                            bossExistFlag = false;
                            LoginActivity.gameUser.addCredits();
                            SharedPreferences sp = getSharedPreferences("user", Context.MODE_PRIVATE);
                            sp.edit()
                                    .putInt("credits", LoginActivity.gameUser.getCredits())
                                    .apply();
                            // 切换背景音乐
                            bgmChange = true;
                        } else {
                            score += 10;
                        }
                        if (itemflag2) {
                            if (heroAircraft.getHp() < Settings.getInstance().maxHeroHp) {
                                heroAircraft.decreaseHp(-10);
                            }
                        }
                        if (itemflag4) {
                            heroAircraft.addHeropower(1);
                        }
                    }
                }
                // 英雄机 与 敌机 相撞，均损毁
                if (enemyAircraft.crash(heroAircraft) || heroAircraft.crash(enemyAircraft)) {
                    enemyAircraft.vanish();
                    heroAircraft.decreaseHp(Integer.MAX_VALUE);
                }
            }
        }
        for (AbstractAircraft gun : MachineGun){
            gun.decreaseHp(1);
            if(gun.getHp()<=0){
                gun.vanish();
            }
        }
        for (AbstractProp prop : props) {
            if (prop.notValid()){
                continue;
            }
            if (heroAircraft.crash(prop)){
                MySoundPool.playSound(MySoundPool.GET_SUPPLY, false);
                // 吃到道具加分
                score += 10;
                if (prop instanceof BombProp){
                    for(AbstractAircraft enemyAircraft : enemyAircrafts){
                        if (! (enemyAircraft instanceof  Boss)){
                            ((BombProp) prop).addCanBoom((CanBoom) enemyAircraft);
                            if (enemyAircraft instanceof EliteEnemy){
                                score += 20;
                            }else {
                                score += 10;
                            }
                        }
                    }
                    for(BaseBullet enemyBullet : enemyBullets){
                        ((BombProp) prop).addCanBoom((CanBoom) enemyBullet);
                    }
                }else if (prop instanceof BloodProp){
                    if(itemflag3) {
                        if (heroAircraft.getHp() < Settings.getInstance().maxHeroHp) {
                            heroAircraft.decreaseHp(-20);
                        }
                        heroAircraft.addHeropower(10);
                    }
                }
                if(itemflag1) {
                    MachineGun.add(machineGunFactory.produceEnemy());
                }
                prop.work();
                prop.vanish();
            }
        }
    }

    /**
     * 游戏结束检查以及打印排行榜
     */
    private void isGameOver(){
        if (heroAircraft.getHp() <= 0)
        {
            MySoundPool.playSound(MySoundPool.GAME_OVER, false);
            // 游戏结束
            gameOverFlag = true;
            Intent intent = new Intent(this, RankActivity.class);
            intent.putExtra("score", score);
            startActivity(intent);
        }
        if(gameOverFlag){
            finish();
            this.onDestroy();
            gameView.surfaceDestroyed(gameView.getHolder());
        }
    }


    /**
     * 后处理：
     * 1. 删除无效的子弹
     * 2. 删除无效的敌机----》 移动到paintVanish中执行
     * 3. 删除无效道具
     * 4. 检查英雄机生存
     * <p>
     * 无效的原因可能是撞击或者飞出边界
     */
    private void postProcessAction() {
        enemyBullets.removeIf(AbstractFlyingObject::notValid);
        heroBullets.removeIf(AbstractFlyingObject::notValid);
        enemyAircrafts.removeIf(AbstractFlyingObject::notValid); // ?由于要绘制爆炸动画，此部分的后处理放在paintVanish中
        MachineGun.removeIf(AbstractFlyingObject::notValid);
        props.removeIf(AbstractFlyingObject::notValid);
    }


    // TODO:监听返回按键,弹窗提示
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        AtomicReference<Boolean> back = new AtomicReference<>(false);
        if(keyCode == KeyEvent.KEYCODE_BACK){
            AlertDialog alertDialog = new AlertDialog.Builder(this)
                    .setTitle("确认退出？")
                    .setMessage("退出将结束游戏且不保存数据。")
                    .setIcon(R.drawable.hero)
                    .setPositiveButton("确定", (dialog, which) -> {
                        gameOverFlag = true;
                        Intent intent = new Intent(GameActivity.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        back.set(true);
                        finish();
                    })

                    .setNegativeButton("取消", (dialog, which) -> {back.set(false);})
                    .create();
            alertDialog.show();
        }
        if (back.get()){
            this.onDestroy();
            gameView.surfaceDestroyed(gameView.getHolder());
            return super.onKeyDown(keyCode, event);
        }
        else return false;
    }

    /**
     * bgm播放
     * @param isChange 是否需要切换bgm
     */
    private void playBgm(boolean isChange){
        if( ! Settings.getInstance().getVideoState()){
            return;
        }
        if(bgmPlayer == null) {
            if (bossExistFlag) {
                bgmPlayer = MediaPlayer.create(this, R.raw.bgm_boss);
            } else {
                bgmPlayer = MediaPlayer.create(this, R.raw.bgm);
            }
            bgmPlayer.setLooping(true);
        }else if(isChange){
            stopBgm();
            if (bossExistFlag) {
                bgmPlayer = MediaPlayer.create(this, R.raw.bgm_boss);
            } else {
                bgmPlayer = MediaPlayer.create(this, R.raw.bgm);
            }
        }
        bgmPlayer.start();
        bgmChange = false;
    }

    private void stopBgm(){
        if(bgmPlayer != null){
            bgmPlayer.stop();
            bgmPlayer.reset();
            bgmPlayer.release();
            bgmPlayer = null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 释放音乐资源
        stopBgm();
    }
}