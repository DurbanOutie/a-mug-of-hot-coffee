import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;

public class Main{
    public static void main(String[] args){
        Window window = new Window();
        Thread t1 = new Thread(window);
        t1.start();
    }
}

class Window extends JFrame implements Runnable{

    Graphics2D g2;
    KL keyListener = new KL();
    Rect playerOne, ai, ball;
    PlayerController playerController;
    Image dbImage; 
    Graphics dbg;

    public Window(){
        this.setSize(Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT);
        this.setTitle(Constants.SCREEN_TITLE);
        this.setResizable(false);
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.addKeyListener(keyListener);
        Constants.INSETS_TOP = this.getInsets().top;
        Constants.INSETS_BOTTOM = this.getInsets().bottom;
        g2 = (Graphics2D)this.getGraphics();
        
        playerOne = new Rect(Constants.HZ_PADDING, 40, 
                Constants.PADDLE_WIDTH, Constants.PADDLE_HEIGHT, 
                Constants.PADDLE_COLOUR);
        playerController = new PlayerController(playerOne, keyListener);

        ai = new Rect(Constants.SCREEN_WIDTH
                - Constants.HZ_PADDING
                - Constants.PADDLE_WIDTH, 40, 
                Constants.PADDLE_WIDTH, Constants.PADDLE_HEIGHT, 
                Constants.PADDLE_COLOUR);

        ball = new Rect(Constants.SCREEN_WIDTH/2, Constants.SCREEN_HEIGHT/2, 
                Constants.BALL_WIDTH, Constants.BALL_WIDTH, 
                Constants.BALL_COLOUR);

        dbImage = createImage(getWidth(), getHeight());
        dbg = dbImage.getGraphics();
    }

    public void update(double dt){
        playerController.update(dt);
        draw(dbg);
        g2.drawImage(dbImage, 0, 0, this);
    }
    public void draw(Graphics g){
        Graphics2D g2 = (Graphics2D)g;
        
        g2.setColor(Color.BLACK);
        g2.fillRect(0, 0, Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT);

        playerOne.draw(g2);
        ai.draw(g2);
        ball.draw(g2);
    }

    public void run(){
        double lastFrameTime = 0.0;
        while(true){
            double time = Time.getTime();
            double deltaTime = time - lastFrameTime;
            lastFrameTime = time;
            update(deltaTime);
            try{
                Thread.sleep(16);
            }catch(Exception e){
            }
        }
    }
}

class PlayerController{
    public Rect rect;
    public KL keyListener;
    
    public PlayerController(Rect rect, KL keyListener){
        this.rect = rect;
        this.keyListener = keyListener;
    }

    public void update(double dt){
        if(keyListener.isKeyPressed(KeyEvent.VK_DOWN)){
            if((rect.y + rect.height + Constants.PADDLE_SPEED*dt) 
                    < Constants.SCREEN_HEIGHT - Constants.INSETS_BOTTOM){
                rect.y += Constants.PADDLE_SPEED*dt;
            }
        }
        if(keyListener.isKeyPressed(KeyEvent.VK_UP)){
            if((rect.y - Constants.PADDLE_SPEED*dt) > Constants.INSETS_TOP){
                rect.y -= Constants.PADDLE_SPEED*dt;
            }
        }
    }
}

class Rect{

    public double x, y, width, height;
    public Color colour;

    public Rect(double x, double y, double width, double height, Color colour){
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.colour = colour;
    }

    public void draw(Graphics2D g2){
        g2.setColor(colour);
        g2.fill(new Rectangle2D.Double(x, y, width, height));
    }

}

class KL implements KeyListener{

    public void keyTyped(KeyEvent e){
    }

    private boolean[] keyPressed = new boolean[128];
     
    public void keyReleased(KeyEvent e){
        keyPressed[e.getKeyCode()] = false;
    }

    public void keyPressed(KeyEvent e){
        keyPressed[e.getKeyCode()] = true;
    }

    public boolean isKeyPressed(int keyCode){
        return keyPressed[keyCode];
    }
}

class Time{
    public static double timeStarted = System.nanoTime();

    public static double getTime(){
        return (System.nanoTime() - timeStarted)*1E-9;
    }
}

class Constants{
    public static final int SCREEN_WIDTH = 800;
    public static final int SCREEN_HEIGHT = 600;
    public static final String SCREEN_TITLE = "Pong";
    public static final double PADDLE_SPEED = 400;
    public static final double PADDLE_WIDTH = 10;
    public static final double PADDLE_HEIGHT = 100;
    public static final Color PADDLE_COLOUR = Color.WHITE;

    public static final double BALL_WIDTH = 10;
    public static final Color BALL_COLOUR = Color.WHITE;

    public static final double HZ_PADDING = 40;

    public static double INSETS_TOP;
    public static double INSETS_BOTTOM;
}
