package com.hit.aircraftwar.application.Activity;

import android.content.Intent;
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
import com.hit.aircraftwar.application.GameView;
import com.hit.aircraftwar.application.Settings;
import com.hit.aircraftwar.basic.AbstractFlyingObject;
import com.hit.aircraftwar.basic.CanBoom;
import com.hit.aircraftwar.bullet.BaseBullet;
import com.hit.aircraftwar.factory.BossFactory;
import com.hit.aircraftwar.factory.EliteEnemyFactory;
import com.hit.aircraftwar.factory.MobEnemyFactory;
import com.hit.aircraftwar.props.AbstractProp;
import com.hit.aircraftwar.props.BombProp;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

public class GameActivity extends AppCompatActivity {

    /**
     * 时间间隔(ms)，控制刷新频率
     */
    private int timeInterval = 40;

    private HeroAircraft heroAircraft;
    private List<AbstractAircraft> enemyAircrafts;
    private List<BaseBullet> heroBullets;
    private List<BaseBullet> enemyBullets;
    private List<AbstractProp> props;

    private int enemyMaxNumber = 5;

    private boolean gameOverFlag = false;
    protected boolean bossExistFlag = false; // 标志Boss是否存在
    public static int score = 0;
    private int time = 0;
    /**
     * 周期（ms)
     * 指示子弹的发射、敌机的产生频率
     */
    private int cycleDuration = 600;
    private int cycleTime = 0;
    /**
     * 创建三个工厂实例
     */
    protected final MobEnemyFactory mobEnemyFactory = new MobEnemyFactory();
    protected final EliteEnemyFactory eliteEnemyFactory = new EliteEnemyFactory();
    protected final BossFactory bossFactory = new BossFactory();
    private GameView gameView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 设置全屏显示
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Objects.requireNonNull(getSupportActionBar()).hide();

        // 重置一下分数
        score = 0;
        heroAircraft = HeroAircraft.getInstance();
        // 由于英雄机为单例模式，因此需要重置一下英雄机设置
        HeroAircraft.getInstance().resetHeroAircraft();
        enemyAircrafts = new LinkedList<>();
        heroBullets = new LinkedList<>();
        enemyBullets = new LinkedList<>();
        props = new LinkedList<>();
//        Log.d("1111111111","1111111111111");
        gameView = new GameView(this, R.drawable.bg, heroAircraft, enemyAircrafts, heroBullets, enemyBullets, props);
        setContentView(gameView);
        action();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        heroAircraft.setLocation((int)event.getRawX(),(int)(event.getRawY() - 100));
        return true;
    }

    // 游戏入口
    public void action() {
        initGameMode();

        // 定时任务：绘制、对象产生、碰撞判定、击毁及结束判定
        Runnable task = () -> {
            // TODO:游戏bgm
//            MusicController.setBgm(bossExistFlag);
            time += timeInterval;

            Log.d("222", time + "");

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

            //每个时刻重绘界面
            //            this.repaint();

            // 游戏结束检查以及打印排行榜
            //        try {
            //            this.isGameOver();
            //        } catch (IOException e) {
            //            e.printStackTrace();
            //        }
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
        Log.d("111", cycleTime + "");
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
            Log.d("1111111111","1111111111111");
        }
    }

    /**
     * 检查是否达到产生Boss条件
     */
    private void gotoBoss(){
        if (! bossExistFlag && score >= Settings.getInstance().scoreToBoss * bossFactory.getBossLevel()){
            enemyAircrafts.add(bossFactory.produceEnemy());
            bossExistFlag = true;
        }
        // TODO:播放boss音乐
//        MusicController.setBossBgm(bossExistFlag);
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
//        // TODO:英雄射击音效
//        MusicController.setBulletBgm();
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
//                MusicController.setBulletHitBgm();
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
                    // TODO:播放音乐
//                    MusicController.setBulletHitBgm();
                    // 敌机撞击到英雄机子弹
                    // 敌机损失一定生命值
                    enemyAircraft.decreaseHp(bullet.getPower());
                    bullet.vanish();
                    if (enemyAircraft.notValid()) {
                        if (enemyAircraft instanceof EliteEnemy){
                            AbstractProp prop = ((EliteEnemy) enemyAircraft).genProp();
                            if (prop != null){
                                props.add(prop);
                            }
                            score += 30;
                        }else if (enemyAircraft instanceof Boss){
                            AbstractProp prop = ((Boss) enemyAircraft).genProp();
                            if (prop != null){
                                props.add(prop);
                            }
                            score += 100;
                            bossExistFlag = false;
                        }else{
                            score += 10;
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

        for (AbstractProp prop : props) {
            if (prop.notValid()){
                continue;
            }
            if (heroAircraft.crash(prop)){
                // TODO:播放吃到道具音效
//                MusicController.setGetSupplyBgm();
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
        if ((heroAircraft.getHp() <= 0 || gameOverFlag))
        {
            // TODO:gameover音效
            // 游戏结束
            gameOverFlag = true;
            gameView.surfaceDestroyed(gameView.getHolder());
            this.onDestroy();
//            finish();
        }
    }
//    private void isGameOver() throws IOException {
//        if (heroAircraft.getHp() <= 0) {
//            // 游戏结束
//            executorService.shutdown();
//            gameOverFlag = true;
//            MusicController.setGameOverBgm();
//            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd HH:mm");
//            // 弹窗输入姓名
//            String currentTime = formatter.format(LocalDateTime.now());
//            String userName = JOptionPane.showInputDialog(null,
//                    "游戏结束，你的得分为"+score+".\n请输入名字记录得分：", "输入",
//                    JOptionPane.PLAIN_MESSAGE);
//            if ("".equals(userName) || userName == null){
//                userName = "unknown user";
//            }
//            RankLine rankList = new RankLine(userName.strip(), score, currentTime);
//            rankDaoImpl.add(rankList);
//            printRankings();
//            System.out.println("Game Over!");
//        }
//    }


    // TODO
    /**
     * 打印排行榜
     * @throws IOException
     */
//    private void printRankings () throws IOException {
//        // 获取屏幕窗口信息
//        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
//        JFrame rankingFrame = new JFrame("排行榜");
//        rankingFrame.setSize(MainFrame.WINDOW_WIDTH, MainFrame.WINDOW_HEIGHT);
//        rankingFrame.setResizable(false);
//        //设置窗口的大小和位置,居中放置
//        rankingFrame.setBounds(((int) screenSize.getWidth() - MainFrame.WINDOW_WIDTH) / 2, 0,
//                MainFrame.WINDOW_WIDTH, MainFrame.WINDOW_HEIGHT);
//        rankingFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//
//        rankingFrame.add(new Ranking().mainPanel);
//        rankingFrame.setVisible(true);
//    }

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
                        back.set(true);
                        finish();
                    })

                    .setNegativeButton("取消", (dialog, which) -> {back.set(false);})
                    .create();
            alertDialog.show();
        }
        if (back.get()){
            this.onDestroy();
            Intent intent = new Intent(GameActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            return super.onKeyDown(keyCode, event);
        }
        else return false;
    }





    //***********************
    //      Paint 各部分
    //***********************

//    /**
//     * 重写paint方法
//     * 通过重复调用paint方法，实现游戏动画
//     *
//     * @param  g
//     */
//    @Override
//    public void paint(Graphics g) {
//        super.paint(g);
//
//        // 绘制背景,图片滚动
//        g.drawImage(backGround, 0, this.backGroundTop - MainFrame.WINDOW_HEIGHT, null);
//        g.drawImage(backGround, 0, this.backGroundTop, null);
//        this.backGroundTop += 1;
//        if (this.backGroundTop == MainFrame.WINDOW_HEIGHT) {
//            this.backGroundTop = 0;
//        }
//
//        // 先绘制子弹，后绘制飞机
//        // 这样子弹显示在飞机的下层
//        paintImageWithPositionRevised(g, enemyBullets);
//        paintImageWithPositionRevised(g, heroBullets);
//
//        paintImageWithPositionRevised(g, enemyAircrafts);
//        paintImageWithPositionRevised(g, props);
//
//        // 绘制敌机爆炸动画
//        paintVanish(g);
//
//        g.drawImage(ImageManager.HERO_IMAGE, heroAircraft.getLocationX() - ImageManager.HERO_IMAGE.getWidth() / 2,
//                heroAircraft.getLocationY() - ImageManager.HERO_IMAGE.getHeight() / 2, null);
//
//        //绘制得分和生命值
//        paintScoreAndLife(g);
//    }
//
//    /**
//     * 绘制敌机爆炸动画,并对敌机进行后处理
//     * @param g
//     */
//    private void paintVanish(Graphics g){
//        for (var enemyAircraft : enemyAircrafts) {
//            if (enemyAircraft.notValid()){
//                g.drawImage(ImageManager.VANISH_IMAGES,
//                        enemyAircraft.getLocationX() - enemyAircraft.getWidth()/2,
//                        enemyAircraft.getLocationY() - enemyAircraft.getHeight()/2,
//                        null);
//            }
//        }
//        enemyAircrafts.removeIf(AbstractFlyingObject::notValid);
//    }
//
//    private void paintImageWithPositionRevised(Graphics g, List<? extends AbstractFlyingObject> objects) {
//        if (objects.size() == 0) {
//            return;
//        }
//
//        for (AbstractFlyingObject object : objects) {
//            BufferedImage image = object.getImage();
//            assert image != null : objects.getClass().getName() + " has no image! ";
//            g.drawImage(image, object.getLocationX() - image.getWidth() / 2,
//                    object.getLocationY() - image.getHeight() / 2, null);
//        }
//    }
//
//    private void paintScoreAndLife(Graphics g) {
//        int x = 10;
//        int y = 25;
//        g.setColor(new Color(16711680));
//        g.setFont(new Font("SansSerif", Font.BOLD, 22));
//        g.drawString("SCORE:" + this.score, x, y);
//        y = y + 20;
//        g.drawString("LIFE:" + this.heroAircraft.getHp(), x, y);
//    }
//
//}
}