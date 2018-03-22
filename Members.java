package TankGame;
import java.util.Vector;
/**
 * 坦克游戏 成员 主要包含 坦克， 子弹等
 * Created by admin on 2017/7/29.
 */
// 坦克的基类,主要有以下属性, 坦克的x 轴 y 轴 坐标 坦克的方向
class Tanks{
    // 定义坦克的横坐标， 纵坐标
    int x=0;
    int y=0;
    boolean isLive=true;
    // 坦克的方向 0 表示向上 1 表示向右 2 表示 下 3 表示左
    private int direct = 0;
    // 设置坦克的速度
    int speed = 1;
    // 坦克的颜色
    private int color;
    public Tanks(int x, int y){
        this.x=x;
        this.y=y;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public int getDirect() {
        return direct;
    }

    public void setDirect(int direct) {
        this.direct = direct;
    }

    public int getX(){
        return x;
    }
    public void setX(int x){
        this.x=x;
    }
    public int getY(){
        return y;
    }
    public void setY(int y){
        this.y=y;
    }
}

// 敌人的坦克类, 每个敌人坦克类都是一个线程，都可以自己活动
class EneMyTank extends Tanks implements Runnable{
    // 初始化 并调用父类的构造方法
    // 敌人坦克是否死亡
    private int times=0;
    // 定义一个集合可以保存敌人的子弹
    Vector<Shot> fire = new Vector<Shot>();
    // 敌人发射子弹，应该是在刚刚创建敌人子弹时,或者是敌人子弹销毁时
    // 定义一个集合类，可以访问到Panel上所有坦克，这样才能知道每一辆坦克在哪，保证不会重叠
    private Vector<EneMyTank> allTank = new Vector<EneMyTank>();
    public EneMyTank(int x, int y){
        super(x, y);
    }

    // 判断是否碰到别的坦克
    private boolean isTouchOther(){
        boolean isTouch=false;
        switch (this.getDirect()){
            case 0:
                // 坦克方向向上,当坦克方向是向上时，则需要判断是否会碰到向上/向下 向右/左方向开的坦克
                for (int i = 0; i < allTank.size(); i++) {
                    EneMyTank tank = allTank.get(i);
                    // 取出面板上所有的坦克,并把自己排除掉
                    // this 代表自己
                    if (tank != this) {
                        // 判断该坦克方向是向上或者是向下
                        if (tank.getDirect() == 0 || tank.getDirect() == 2) {
                            if (this.getX() >= tank.getX() && this.getX() <= tank.getX() + 20
                                    && this.getY() >= tank.getY() && this.getY() <= tank.getY() + 30) {
                                return true;
                            }
                            if (this.getX() + 20 >= tank.getX() && this.getX() + 20 <= tank.getX() + 20
                                    && this.getY() >= tank.getY() && this.getY() <= tank.getY() + 30) {
                                return true;
                            }
                        }
                        // 其他坦克向左或者向右
                        if (tank.getDirect() == 3 || tank.getDirect() == 1) {

                            if (this.getX() >= tank.getX() && this.getX() <= tank.getX() + 30
                                    && this.getY() >= tank.getY() && this.getY() <= tank.getY() + 20) {
                                return true;
                            }
                            if (this.getX() + 20 >= tank.getX() && this.getX() + 20 <= tank.getX() + 30
                                    && this.getY() >= tank.getY() && this.getY() <= tank.getY() + 20) {
                                return true;
                            }

                        }
                    }
                }
                break;
            case 1:
                // 坦克向右
                for (EneMyTank tank : allTank) {
                    // 取出面板上所有的坦克,并把自己排除掉
                    // this 代表自己
                    if (tank != this) {
                        // 判断该坦克方向是向上或者是向下
                        if (tank.getDirect() == 0 || tank.getDirect() == 2) {
                            if (this.getX() >= tank.getX() && this.getX() <= tank.getX() + 20
                                    && this.getY() >= tank.getY() && this.getY() <= tank.getY() + 30) {
                                return true;
                            }
                            if (this.getX() >= tank.getX() && this.getX() <= tank.getX() + 20
                                    && this.getY() + 30 >= tank.getY() && this.getY() + 30 <= tank.getY() + 30) {
                                return true;
                            }
                        }
                        // 其他坦克向左或者向右
                        if (tank.getDirect() == 3 || tank.getDirect() == 1) {

                            if (this.getX() >= tank.getX() && this.getX() <= tank.getX() + 30
                                    && this.getY() >= tank.getY() && this.getY() <= tank.getY() + 20) {
                                return true;
                            }
                            if (this.getX()+20 >= tank.getX() && this.getX() <= tank.getX() + 30
                                    && this.getY() + 20 >= tank.getY() && this.getY() <= tank.getY() + 20) {
                                return true;
                            }

                        }
                    }
                }
                break;
            case 2:
                // 其他坦克向下
                for (int i = 0; i < allTank.size(); i++) {
                    EneMyTank tank = allTank.get(i);
                    // 取出面板上所有的坦克,并把自己排除掉
                    // this 代表自己
                    if (tank != this) {
                        // 判断该坦克方向是向上或者是向下
                        if (tank.getDirect() == 0 || tank.getDirect() == 2) {
                            if (this.getX() >= tank.getX() && this.getX() <= tank.getX() + 20
                                    && this.getY() + 30 >= tank.getY() && this.getY() + 30 <= tank.getY() + 30) {
                                return true;
                            }
                            if (this.getX() >= tank.getX() && this.getX() <= tank.getX() + 20
                                    && this.getY() + 30 >= tank.getY() && this.getY() + 30 <= tank.getY() + 30) {
                                return true;
                            }
                        }
                        // 其他坦克向左或者向右
                        if (tank.getDirect() == 3 || tank.getDirect() == 1) {

                            if (this.getX() + 20 >= tank.getX() && this.getX() + 20 <= tank.getX() + 30
                                    && this.getY() + 30 >= tank.getY() && this.getY() + 30 <= tank.getY() + 30) {
                                return true;
                            }
                            if (this.getX() + 20 >= tank.getX() && this.getX() + 20 <= tank.getX() + 30
                                    && this.getY() + 30 >= tank.getY() && this.getY() + 30 <= tank.getY() + 30) {
                                return true;
                            }

                        }
                    }
                }
                break;
            case 3:
                // 向左
                for (int i = 0; i < allTank.size(); i++) {
                    EneMyTank tank = allTank.get(i);
                    // 取出面板上所有的坦克,并把自己排除掉
                    // this 代表自己
                    if (tank != this) {
                        // 判断该坦克方向是向上或者是向下
                        if (tank.getDirect() == 0 || tank.getDirect() == 2) {
                            if (this.getX() >= tank.getX() && this.getX() <= tank.getX() + 20
                                    && this.getY() >= tank.getY() && this.getY() <= tank.getY() + 30) {
                                return true;
                            }
                            if (this.getX() >= tank.getX() && this.getX() <= tank.getX() + 20
                                    && this.getY() >= tank.getY() && this.getY() <= tank.getY() + 30) {
                                return true;
                            }
                        }
                        // 其他坦克向左或者向右
                        if (tank.getDirect() == 3 || tank.getDirect() == 1) {

                            if (this.getX() >= tank.getX() && this.getX() <= tank.getX() + 30
                                    && this.getY() + 20 >= tank.getY() && this.getY() + 20 <= tank.getY() + 20) {
                                return true;
                            }
                            if (this.getX()+20 >= tank.getX() && this.getX() <= tank.getX() + 30
                                    && this.getY() + 30 >= tank.getY() && this.getY() + 30 <= tank.getY() + 30) {
                                return true;
                            }

                        }
                    }
                }
                break;
        }
        return isTouch;
    }

    public void setAllTank(Vector<EneMyTank> v){

        this.allTank = v;
    }

    @Override
    public void run() {
        while (true){
            // 让敌人的坦克可以移动, 每隔一段时间修改敌人的坐标
            // 根据敌人的方向来决定敌人如何移动, 方向有随机的变化
            switch (this.getDirect()){
                case 0:
                    // 敌人的方向向上 y 轴减小
                    for (int i=0; i<30; i++){
                        if (y > 0 && !this.isTouchOther()){
                            y-=speed;
                        }
                        try{
                            Thread.sleep(50);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case 1:
                    // 向右 x 轴 加大
                    for (int i=0; i<30; i++){
                        if (x >= 0 && ! this.isTouchOther()){
                            x-=speed;
                        }
                        try{
                            Thread.sleep(50);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                    break;
                case 2:
                    // 向下
                    for (int i=0; i<30; i++){
                        if (y < 245 && ! this.isTouchOther()){
                            y+=speed;
                        }
                        try{
                            Thread.sleep(50);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case 3:
                    // 向左
                    for (int i=0; i<30; i++){
                        if (x < 355 && ! this.isTouchOther()){
                            x+=speed;
                        }
                        try{
                            Thread.sleep(50);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
            }
            times ++;
                // 当敌人没有子弹时给他添加子弹
                if (isLive && fire.size()< 5 && times % 2 == 0){
                    Shot s=null;
                    switch (this.getDirect()){
                        case 0:
                            // 子弹向上
                            s = new Shot(this.getX()+10, this.getY(), 0);
                            // 将子弹放到集合中
                            fire.add(s);
                            break;
                        case 1:
                            // 子弹向右
                            s = new Shot(this.getX(), this.getY()+10, 3);
                            // 将子弹放到集合中
                            fire.add(s);
                            break;
                        case 2:
                            // 子弹向下
                            s = new Shot(this.getX()+10, this.getY()+30, 2);
                            // 将子弹放到集合中
                            fire.add(s);
                            break;
                        case 3:
                            // 子弹向左
                            s = new Shot(this.getX()+30, this.getY()+10, 1);
                            // 将子弹放到集合中
                            this.fire.add(s);
                            break;
                    }
                    // 子弹也是一个线程.记得要启动子弹线程
                    Thread ShotYou = new Thread(s);
                    ShotYou.start();
                }
            // 让坦克随机产生一个新的方向, Math.random() 产生0-1的随机数。*4 代表生成0-3的随机数
            this.setDirect((int) (Math.random()*4));
            if (!this.isLive){
                // 让坦克死亡后退出线程
                break;
            }
            }
        }

    }

// 定义一个我的坦克类, 继承坦克类
class MyTank extends Tanks{
    private Shot shot;
    // 创建一个子弹的集合
    Vector<Shot> shots = new Vector<Shot>();
    // 初始化 并调用父类的构造方法
    public MyTank(int x, int y){
        super(x, y);
    }
    // 发射子弹
    public void shotting(){

        switch (this.getDirect()){

            case 0:
                // 子弹向上
                shot = new Shot(x+10, y, 0);
                // 将子弹放到集合中
                shots.add(shot);
                break;
            case 1:
                // 子弹向右
                shot = new Shot(x, y+10, 3);
                // 将子弹放到集合中
                shots.add(shot);
                break;
            case 2:
                // 子弹向下
                shot = new Shot(x+10, y+30, 2);
                // 将子弹放到集合中
                shots.add(shot);
                break;
            case 3:
                // 子弹向左
                shot = new Shot(x+30, y+10, 1);
                // 将子弹放到集合中
                shots.add(shot);
                break;
        }
        // 启动子弹
        Thread thread = new Thread(shot);
        thread.start();


    }

    // 控制坦克向上移动, 向上移动, 应该是 y 轴 的值 减小
    public void moveUp(){
        this.y-= this.speed;
    }
    // 控制坦克向右移动, 向右移动 应该是 x 轴的值 加大
    public void moveRight(){
        this.x-=this.speed;

    }
    // 控制坦克向下移动 向下移动 应该是 y 轴的值 加大
    public void moveDown(){
        this.y+=speed;
    }
    // 控制坦克向左移动, 向左移动应该是 x 轴 减小
    public void moveLeft(){
        this.x+= this.speed;
    }
}

class Shot implements Runnable{
    // 子弹的坐标, 方向, 速度
    int x;

    public int getY() {
        return y;
    }

    int y;
    private int direct;
    private int speed=1;
    // 子弹是否已经销毁, 子弹打到边缘位置，或者是打到敌人子弹时就要对子弹进行销毁处理
    boolean isLive=true;
    public Shot(int x, int y, int direct){
        this.x = x;
        this.y = y;
        this.direct = direct;
    }
    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    @Override
    public void run() {
        while (true){
            try{
                Thread.sleep(50);
            }catch (InterruptedException e){
                e.printStackTrace();
            }
            switch (this.direct){
                case 0:
                    // 子弹向上
                    y-=speed;
                    break;
                case 1:
                    // 子弹向右
                    x+=speed;
                    break;
                case 2:
                    // 子弹向下
                    y+=speed;
                    break;
                case 3:
                    // 子弹向左
                    x-=speed;
                    break;
            }
            // 需要处理子弹销毁问题
            // 如果子弹到达x 轴 0 的位置 或者 子弹到达 y 轴 0的位置
            // 或者子弹大于 我们定义的JFrame的x 轴 y 轴大小 则子弹不再前进
            if (x < 0 || y < 0 || x > 400 || y > 300){
                this.isLive = false;
                break;
            }
        }
    }
}

class Bomb{
    // 炸弹爆炸类
    // 定义炸弹的坐标
    int x;
    int y;
    // 炸弹的生命
    int life = 9;
    boolean isLive = true;
    public Bomb(int x, int y){
        this.x = x;
        this.y = y;
    }
    public void lifeDown(){
        if (life>0){
            life--;
        }
        else {
            this.isLive = false;
        }
    }
}