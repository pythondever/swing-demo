package TankGame;

import java.awt.*;
import java.awt.event.*;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.io.File;
import java.util.Vector;
/**
 * 坦克游戏, 一个简单的坦克主要有两个长矩形（坦克的轮子）, 一个正方形（坦克车身）, 一条直线(坦克炮筒)
 * 一个小圆圈（坦克头） 组成
 * 整体代码流程
 * 1. JFrame 放置一个JPanel
 * 2. JPanel 上画出我的坦克 敌人的坦克
 * 3. 敌人坦克定义了 5 辆 放置在集合Vector中
 * 4. 发射子弹，一个子弹就是一个线程，当子弹向上时就是 y 轴 -- 向下就是 y轴 ++  子弹向左就是 x 轴 -- 向右就是 x 轴 ++
 *    发射子弹之后需要一直重绘子弹的位置，当子弹 x 轴 y 轴的位置 为 0 或者 击中敌人坦克 或者 达到窗体的边缘位置需要销毁子弹
 * 5. 敌人坦克如何活动 敌人坦克每个坦克就是一个线程通过implements 继承线程，通过Math.random 随机改变 敌人坦克的方向
 * 6. 坦克方向 0 代表向上 1 代表向右 2 代表 向下 3 代表向左
 *
 * Created by admin on 2017/7/22.
 */
public class TankGameV1 extends JFrame{
    private Tank panel;
    public static void main(String[] args){
        TankGameV1 game = new TankGameV1();

    }
    public TankGameV1(){
        // 构造函数
        panel = new Tank();
        // 启动panel线程
        Thread t = new Thread(panel);
        t.start();
        this.add(panel);
        // 注册监听
        this.addKeyListener(panel);
        this.setTitle("GAME OVER");
        this.setResizable(false);
        this.setLocation(500, 250);
        this.setSize(400, 300);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
    }
}

// KeyListener 事件监听  监听键盘的 A W S D键 让坦克移动
// 需要实现方法 keyPressed
class Tank extends JPanel implements KeyListener, Runnable{
    // 将来的坦克全部是在一个Panel上面活动的，所有把所有的坦克当成Panel的成员变量
    // 定义敌人的坦克集合
    private Vector<EneMyTank> eneMyTank = new Vector<EneMyTank>();
    // 定义敌人坦克数量
    private int enemyNumber = 5;
    private MyTank myTank;
    private Image image1;
    private Image image2;
    private Image image3;
    // 定义炸弹爆炸的集合
    private Vector<Bomb> bombs = new Vector<Bomb>();
    public Tank(){
        // 初始化的时候创建敌人的坦克, 以及我的坦克
        myTank = new MyTank(100, 100);
        for (int i=0; i < enemyNumber; i++){
            // 创建敌人坦克，并设置其初始位置, 设置颜色, 设置方向
            EneMyTank eTanks = new EneMyTank((i+1)*50, 0);
            eTanks.setColor(0);
            eTanks.setDirect(2);
            // 每个敌人坦克就是一个线程
            Thread t = new Thread(eTanks);
            t.start();
            // 获取到每一辆敌人坦克的信息
            eTanks.setAllTank(eneMyTank);
            // 创建坦克后给坦克添加子弹
            Shot fire = new Shot(eTanks.getX() + 10, eTanks.getY() + 30, eTanks.getDirect());
            eTanks.fire.add(fire);
            // 子弹加入集合
            Thread t1 = new Thread(fire);
            t1.start();
            // 加入到集合里面
            eneMyTank.add(eTanks);
        }
        // 初始化加载图片
        try {
            image1 = ImageIO.read(new File("src/TankGame/bomb_1.gif"));
            image2 = ImageIO.read(new File("src/TankGame/bomb_2.gif"));
            image3 = ImageIO.read(new File("src/TankGame/bomb_3.gif"));
        }catch (Exception e){
            e.printStackTrace();
        }
        // 初始化加载图片
//        image1 = Toolkit.getDefaultToolkit().getImage(Panel.class.getResource("/TankGame/bomb_1.gif"));
//        image2 = Toolkit.getDefaultToolkit().getImage(Panel.class.getResource("/TankGame/bomb_2.gif"));
//        image3 = Toolkit.getDefaultToolkit().getImage(Panel.class.getResource("/TankGame/bomb_3.gif"));
    }
    // paint 不需要显式的调用，系统会自动调用
    public void paint(Graphics graphics){
        super.paint(graphics);
        // 使用黑色填充背景
        graphics.fill3DRect(0, 0, 400, 300, false);
        // 画出我的坦克
        if (this.myTank.isLive) {
            this.drawTanks(myTank.getX(), myTank.getY(), graphics, this.myTank.getDirect(), 0);
        }
        // 遍历子弹集合,实现多个子弹连发
        for (int i=0; i<this.myTank.shots.size(); i++){
            Shot myShot = this.myTank.shots.get(i);
        // 画出子弹,
        if (myShot !=null && myShot.isLive){
            graphics.draw3DRect(myShot.x, myShot.y, 1, 1, false);
        }
            // 如果子弹已经销毁,则从集合中删除子弹
            assert myShot != null;
            if (!myShot.isLive){
            this.myTank.shots.remove(myShot);
        }

        }
        // 画出敌人的坦克以及敌人的子弹
        for (EneMyTank emt : eneMyTank) {
            if (emt.isLive) {
                // 每个敌人坦克就是一个线程
                this.drawTanks(emt.getX(), emt.getY(), graphics, emt.getDirect(), 1);
                // 画出敌人的子弹
                for (int x = 0; x < emt.fire.size(); x++) {
                    Shot emFire = emt.fire.get(x);
                    if (emFire.isLive) {
                        graphics.draw3DRect(emFire.getX(), emFire.getY(), 1, 1, false);

                    } else {
                        // 如果子弹销毁则从集合中删除
                        emt.fire.remove(emFire);
                    }

                }
            }

        }
        // 画出炸弹
        for (int i=0; i < bombs.size(); i++){
            Bomb b = bombs.get(i);
            if (b.life > 6){
                // 画出爆炸效果， 参数是图片图像，图片x y 坐标图片 大小 ，要在哪里画
                graphics.drawImage(image1, b.x, b.y, 30, 30, this);
            }else if (b.life > 3){
                graphics.drawImage(image2, b.x, b.y, 30, 30, this);
            }else {

                graphics.drawImage(image3, b.x, b.y, 30, 30, this);
            }
            // 让爆炸的生命值 减少
            b.lifeDown();
            // 如果炸弹的生命值为0 将其从集合里面删除
            if (b.life == 0){
                // b.isLive = false;
                bombs.remove(i);
            }
        }
    }
    // 该函数用于判断子弹是否击中敌人坦克, 需要两个参数, 子弹对象, 敌人坦克对象
    private void hitTarget(Shot s, Tanks tk){
        // 首先判断该坦克的方向
        switch (tk.getDirect()){
            case 0:
                // 坦克方向向上
            case 1:
                // 坦克方向向右
            case 2:
                // 敌人的方向向下
                if (s.x >= tk.getX() && s.x < tk.getX() + 20 && s.y >= tk.getY() && s.y < tk.getY() + 30){
                    // 击中, 需要让子弹销毁, 坦克 死亡
                    s.isLive = false;
                    tk.isLive = false;
                    // 创建一颗炸弹 放入Vector中
                    Bomb bomb = new Bomb(tk.getX(), tk.getY());
                    bombs.add(bomb);
                }
                break;
            case 3:
                // 坦克方向向左
                if (s.x > tk.x && s.x < tk.getX() + 30 && s.y > tk.getY() && s.y < tk.getY() + 20){
                    // 击中
                    s.isLive = false;
                    tk.isLive = false;
                    // 创建一颗炸弹 放入Vector中
                    Bomb bomb = new Bomb(tk.getX(), tk.getY());
                    bombs.add(bomb);
                }
                break;
        }
    }
    // 画坦克的方法
    private void drawTanks(int x, int y, Graphics graphics, int location, int type){
        /**
         * 画坦克的公共方法 x y 代表坦克的x轴 y轴位置 graphics 画笔 location 坦克的位置 type 坦克的类型
         *
         */
        switch (type){
            // 如果传入的类型是0 则代表是要创建我自己的坦克,给我自己的坦克设置画笔颜色是黄色
            case 0:
                graphics.setColor(Color.YELLOW);
                break;
            // 如果传入的类型是1 则代表是要创建敌人的坦克 给敌人的坦克设置画笔另外一种颜色 区分敌我
            case 1:
                graphics.setColor(Color.CYAN);
                break;
        }
        // 判断方向, 如果是我的坦克则设置我坦克面对的方向是向上, 然后画自己的坦克
        // 坦克的方向 0 表示向上 1 表示向右 2 表示 下 3 表示左
        switch (location){
            case 0:
                // 先画坦克左边的轮子
                graphics.fill3DRect(x, y, 5, 30, false);
                // 再画右边的轮子的坐标就等于左边轮子的宽度5 + 中间车身的宽度10 y轴不变
                graphics.fill3DRect(x + 15, y, 5, 30, false);
                // 画出中间的车身
                graphics.fill3DRect(x + 5, y + 5, 10, 20, false);
                // 再画圆形(车头)
                graphics.fillOval(x + 5, y + 10, 10, 10);
                // 再画炮筒
                graphics.drawLine(x + 10, y + 15, x + 10, y);
                break;
            case 1:
                // 将坦克方向设置为向右
                // 1. 画出 上面的矩形, x 轴 y 轴方向不变 将宽度 设置为30 高度设置为5
                graphics.fill3DRect(x, y, 30, 5, false);
                // 2. 画出 下面的矩形， x 轴 不变 y 轴 需要 15 将宽度 设置为30 高度设置为5
                graphics.fill3DRect(x, y+15, 30, 5, false);
                // 3. 画出中间的矩形 宽度是20 高度是 10
                graphics.fill3DRect(x+5, y+5, 20, 10, false);
                // 画出圆形
                graphics.fillOval(x+10, y+5, 10, 10);
                // 最后画直线
                graphics.drawLine(x+15, y+10, x, y+10);
                break;
            case 2:
                // 方向 下
                // 向下跟向上的区别只是炮筒方向的不同而已
                // 先画坦克左边的轮子
                graphics.fill3DRect(x, y, 5, 30, false);
                // 再画右边的轮子的坐标就等于左边轮子的宽度5 + 中间车身的宽度10 y轴不变
                graphics.fill3DRect(x + 15, y, 5, 30, false);
                // 画出中间的车身
                graphics.fill3DRect(x + 5, y + 5, 10, 20, false);
                // 再画圆形(车头)
                graphics.fillOval(x + 5, y + 10, 10, 10);
                // 再画炮筒
                graphics.drawLine(x + 10, y + 15, x + 10, y+30);
                break;
            case 3:
                // 向左 向左和向右的区别也只是炮筒方向而已
                // 1. 画出 上面的矩形, x 轴 y 轴方向不变 将宽度 设置为30 高度设置为5
                graphics.fill3DRect(x, y, 30, 5, false);
                // 2. 画出 下面的矩形， x 轴 不变 y 轴 需要 15 将宽度 设置为30 高度设置为5
                graphics.fill3DRect(x, y+15, 30, 5, false);
                // 3. 画出中间的矩形 宽度是20 高度是 10
                graphics.fill3DRect(x+5, y+5, 20, 10, false);
                // 画出圆形
                graphics.fillOval(x+10, y+5, 10, 10);
                // 最后画直线
                graphics.drawLine(x+15, y+10, x+30, y+10);
                break;
        }

    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        // 设置我的坦克方向
        // 坦克的方向 0 表示向上 1 表示向右 2 表示 下 3 表示左
        if (e.getKeyCode() == KeyEvent.VK_W){
            this.myTank.setDirect(0);
            this.myTank.moveUp();
        }else if (e.getKeyCode() == KeyEvent.VK_S){
            this.myTank.setDirect(2);
            this.myTank.moveDown();
        }else if (e.getKeyCode() == KeyEvent.VK_A){
            this.myTank.setDirect(1);
            this.myTank.moveRight();
        }else if (e.getKeyCode() == KeyEvent.VK_D){
            this.myTank.setDirect(3);
            this.myTank.moveLeft();

        }
        // 判断 j 键是否被按下，如果按下则发射子弹
        if (e.getKeyCode() == KeyEvent.VK_J){
            // 限制连发 一次只能 5 颗
            if (this.myTank.shots.size() <= 4 ) {
                this.myTank.shotting();
            }
        }
        // 重绘窗口
        this.repaint();
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
    // 判断是否击中敌人坦克的方法
    private void hitEnemyTank(){
        // 判断每一颗子弹是否击中坦克， 我们有多颗子弹， 以及多个敌人坦克
        for (int i=0; i< this.myTank.shots.size(); i++){
            Shot myshot = this.myTank.shots.get(i);
            if (myshot.isLive){
                // 取出每个敌人坦克 判断子弹是否击中了它
                eneMyTank.stream().filter(em -> em.isLive).forEach(em -> {
                    this.hitTarget(myshot, em);
                });
            }
        }
    }
    private void hitMyTank(){
        // 判断我的坦克是否被击中
        // 1. 取出每一个敌人坦克
        for (EneMyTank emt : eneMyTank) {
            //2. 取出敌人的子弹
            for (int shot = 0; shot < emt.fire.size(); shot++) {
                Shot enemyTankShot = emt.fire.get(shot);
                if (this.myTank.isLive) {
                    this.hitTarget(enemyTankShot, this.myTank);
                }
            }
        }
    }

    @Override
    public void run() {
        // 每隔100 毫秒去重画子弹位置
        while (true){
            try {
                Thread.sleep(100);
            }catch (InterruptedException e){
                e.printStackTrace();
            }
            this.hitEnemyTank();
            this.hitMyTank();
            this.repaint();
        }
    }
}
