// Credit youtube.com/@GamesWithGabe
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
    ML mouseListener = new ML();
    Rect playerOne, aiRect, ballRect;
    PlayerController playerController;
    AIPlayer ai;
    Ball ball;
    Image dbImage; 
    Graphics dbg;
    Font font = new Font("Arial", Font.PLAIN, 18);
    Font menuFont = new Font("Arial", Font.PLAIN, 50);
    Font titleFont = new Font("Arial", Font.PLAIN, 100);
    Score leftScore, rightScore;
    Text startGame, exitGame, pongTitle;
    boolean gameRunning;
    boolean appRunning = true;

    Window(){
        this.setSize(Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT);
        this.setTitle(Constants.SCREEN_TITLE);
        this.setResizable(false);
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.addKeyListener(keyListener);
        this.addMouseListener(mouseListener);
        this.addMouseMotionListener(mouseListener);
        Constants.INSETS_TOP = this.getInsets().top;
        Constants.INSETS_BOTTOM = this.getInsets().bottom;
        g2 = (Graphics2D)this.getGraphics();
        //----------------------------Game-------------------------------- 
        playerOne = new Rect(Constants.HZ_PADDING, 40, 
                Constants.PADDLE_WIDTH, Constants.PADDLE_HEIGHT, 
                Constants.PADDLE_COLOUR);
        playerController = new PlayerController(playerOne, keyListener);

        ballRect = new Rect(Constants.SCREEN_WIDTH/2, Constants.SCREEN_HEIGHT/2, 
                Constants.BALL_WIDTH, Constants.BALL_WIDTH, 
                Constants.BALL_COLOUR);

        aiRect = new Rect(Constants.SCREEN_WIDTH
                - Constants.HZ_PADDING
                - Constants.PADDLE_WIDTH, 40, 
                Constants.PADDLE_WIDTH, Constants.PADDLE_HEIGHT, 
                Constants.PADDLE_COLOUR);
        ai = new AIPlayer(new PlayerController(aiRect), ballRect);
        
        leftScore = new Score(font, 10.0f, 50.0f + (float)Constants.INSETS_TOP);
        rightScore = new Score(font, Constants.SCREEN_WIDTH - 10.0f -16.0f, 
                50.0f + (float)Constants.INSETS_TOP);

        ball = new Ball(ballRect, playerOne, aiRect, leftScore, rightScore);
        //---------------------------Menu---------------------------------
        startGame = new Text("Start Game", menuFont, Color.WHITE, 
                Constants.SCREEN_WIDTH/2 - 150, Constants.SCREEN_HEIGHT/2); 
        exitGame = new Text("Exit", menuFont, Color.WHITE, 
                Constants.SCREEN_WIDTH/2 - 50, Constants.SCREEN_HEIGHT/2 + 100); 
        pongTitle = new Text("PONG", titleFont, Color.WHITE, 
                Constants.SCREEN_WIDTH/2 - 150, 
                Constants.SCREEN_HEIGHT/2 - 100); 

        dbImage = createImage(getWidth(), getHeight());
        dbg = dbImage.getGraphics();

        gameRunning = false;


    }

    void update(double dt){
        if(gameRunning){
            playerController.update(dt);
            ai.update(dt);
            ball.update(dt);
            if(leftScore.score >= Constants.WIN_SCORE){
                gameRunning = false;
                System.out.println("Player 1 Wins!");
            }
            if(rightScore.score >= Constants.WIN_SCORE){
                gameRunning = false;
                System.out.println("Computer Wins!");
            }
        }else{
            if(mouseListener.x > startGame.x 
                    && mouseListener.x < startGame.x + startGame.width 
                    && mouseListener.y > startGame.y - startGame.height
                    && mouseListener.y < startGame.y){
                startGame.colour = Color.GRAY;
                if(mouseListener.isPressed){
                    leftScore.score = 0;
                    rightScore.score = 0;
                    gameRunning = true;
                }
            }else{
                startGame.colour = Color.WHITE;
            }
            if(mouseListener.x > exitGame.x 
                    && mouseListener.x < exitGame.x + exitGame.width 
                    && mouseListener.y > exitGame.y - exitGame.height
                    && mouseListener.y < exitGame.y){
                exitGame.colour = Color.GRAY;
                if(mouseListener.isPressed){
                    appRunning = false;
                }
            }else{
                exitGame.colour = Color.WHITE;
            }
        }
         
        draw(dbg);
        g2.drawImage(dbImage, 0, 0, this);


    }
    void draw(Graphics g){
        Graphics2D g2 = (Graphics2D)g;
        
        g2.setColor(Color.BLACK);
        g2.fillRect(0, 0, Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT);

        if(!gameRunning){
            pongTitle.draw(g2);
            startGame.draw(g2);
            exitGame.draw(g2);
        }else{
            leftScore.draw(g2);
            rightScore.draw(g2);

            playerOne.draw(g2);
            aiRect.draw(g2);
            ballRect.draw(g2);
        }
    }

    public void run(){
        double lastFrameTime = 0.0;
        System.out.println("Starting Game");
        while(appRunning){
            double time = Time.getTime();
            double deltaTime = time - lastFrameTime;
            lastFrameTime = time;
            update(deltaTime);
            try{
                Thread.sleep(16);
            }catch(Exception e){
            }
        }
        System.out.println("Ending Game");
        this.dispose();
    }
}

class Ball{
    Rect ball, leftPaddle, rightPaddle;
    Score leftScore, rightScore;

    private double vy = 0.0;
    private double vx = -150.0;

    Ball(Rect ball, Rect leftPaddle, Rect rightPaddle, 
            Score leftScore, Score rightScore){
        this.ball = ball;
        this.leftPaddle = leftPaddle;
        this.rightPaddle = rightPaddle;
        this.leftScore = leftScore;
        this.rightScore= rightScore;

    }

    double calculateVelocityAngle(Rect paddle){
        double relativeIntersect = (paddle.y + (paddle.height/2.0)) 
            - (ball.y + (ball.height/2.0));
        double normalIntersect = relativeIntersect *2.0 / paddle.height;
        double theta = normalIntersect * Constants.MAX_ANGLE;
        return Math.toRadians(theta);
    } 

    void update(double dt){
        if(vx < 0){
            if(ball.x <= leftPaddle.x + leftPaddle.width 
                    && ball.x + ball.width >= leftPaddle.x
                    && ball.y >= leftPaddle.y 
                    && ball.y <= leftPaddle.y + leftPaddle.height
                    ){
                double theta = calculateVelocityAngle(leftPaddle);
                double newVx = Math.abs(Math.cos(theta)) * Constants.BALL_SPEED;
                double newVy = (-Math.sin(theta)) * Constants.BALL_SPEED;
                double currentSign = Math.signum(vx);

                vx = newVx * (-1.0 * currentSign);
                vy = newVy;
                
               
            }else if(ball.x + ball.width < leftPaddle.x){
                rightScore.score++;
                resetBallLeft();
            }

        }else if(vx > 0){
            if(ball.x + ball.width >= rightPaddle.x 
                    && ball.x <= rightPaddle.x + rightPaddle.width
                    && ball.y >= rightPaddle.y 
                    && ball.y <= rightPaddle.y + rightPaddle.height
                    ){
                double theta = calculateVelocityAngle(rightPaddle);
                double newVx = Math.abs(Math.cos(theta)) * Constants.BALL_SPEED;
                double newVy = (-Math.sin(theta)) * Constants.BALL_SPEED;
                double currentSign = Math.signum(vx);

                vx = newVx * (-1.0 * currentSign);
                vy = newVy;
            }else if(ball.x > rightPaddle.x + rightPaddle.width){
                leftScore.score++;
                resetBallRight();
            }

        }

        if(vy < 0){
            if(ball.y <= Constants.INSETS_TOP){
                vy *=-1;
            }
        }else if(vy > 0){
            if(ball.y + ball.height 
                    >= Constants.SCREEN_HEIGHT - Constants.INSETS_BOTTOM){
                vy *=-1;
            }
        }

        this.ball.x += vx * dt;
        this.ball.y += vy * dt;

    }
    void resetBallLeft(){
        
        ball.x = Constants.SCREEN_WIDTH/2.0; 
        ball.y = Constants.SCREEN_HEIGHT/2.0;
        vx = -150;
        vy = 0.0;
    }
    void resetBallRight(){
        
        ball.x = Constants.SCREEN_WIDTH/2.0; 
        ball.y = Constants.SCREEN_HEIGHT/2.0;
        vx = 150;
        vy = 0.0;
    }
}

class AIPlayer{
    PlayerController controller;
    Rect ball;

    AIPlayer(PlayerController controller, Rect ball){
        this.controller = controller;
        this.ball = ball;
    }
    void update(double dt){
        controller.update(dt);
        if(ball.y < controller.rect.y){
            controller.moveUp(dt);
        }
        if(ball.y + ball.height > controller.rect.y + controller.rect.height){
            controller.moveDown(dt);
        }
    }
}

class PlayerController{
    Rect rect;
    KL keyListener;
    
    PlayerController(Rect rect, KL keyListener){
        this.rect = rect;
        this.keyListener = keyListener;
    }
    PlayerController(Rect rect){
        this.rect = rect;
        this.keyListener = null;

    }

    void update(double dt){
        if(keyListener!=null){
            if(keyListener.isKeyPressed(KeyEvent.VK_DOWN)){
                moveDown(dt);
            }
            if(keyListener.isKeyPressed(KeyEvent.VK_UP)){
                moveUp(dt);
            }
        }else{

        }
    }
    void moveUp(double dt){
        if((rect.y - Constants.PADDLE_SPEED*dt) > Constants.INSETS_TOP){
            rect.y -= Constants.PADDLE_SPEED*dt;
        }
    }
    
    void moveDown(double dt){
        if((rect.y + rect.height + Constants.PADDLE_SPEED*dt) 
                < Constants.SCREEN_HEIGHT - Constants.INSETS_BOTTOM){
            rect.y += Constants.PADDLE_SPEED*dt;
        }
    }
}

class Rect{

    double x, y, width, height;
    Color colour;

    Rect(double x, double y, double width, double height, Color colour){
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.colour = colour;
    }

    void draw(Graphics2D g2){
        g2.setColor(colour);
        g2.fill(new Rectangle2D.Double(x, y, width, height));
    }

}

class Text{
    String text;
    Font font;
    Color colour;
    float x, y;
    double width, height;
    

    Text(String text, Font font, Color colour, float x, float y){
        this.text = text;
        this.font = font;
        this.colour = colour;
        this.x = x;
        this.y = y;
        width = font.getSize() * text.length()*0.6;
        height = font.getSize();
    }

    void draw(Graphics2D g2){
        g2.setColor(colour);
        g2.setFont(font);
        g2.drawString(text, x, y);
    }
}

class Score{
    int score = 0;
    Font font;
    float x, y;
    

    Score(Font font, float x, float y){
        this.font = font;
        this.x = x;
        this.y = y;
    }

    void draw(Graphics2D g2){
        g2.setColor(Constants.PADDLE_COLOUR);
        g2.setFont(font);
        g2.drawString(""+score, x, y);
    }
}

class ML extends MouseAdapter implements MouseMotionListener{
    boolean isPressed;
    double x, y;
   
    @Override 
    public void mousePressed(MouseEvent e){
        isPressed = true;
    }

    @Override 
    public void mouseReleased(MouseEvent e){
        isPressed = false;
    }
    
    @Override 
    public void mouseMoved(MouseEvent e){
        x = e.getX();
        y = e.getY();
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

    boolean isKeyPressed(int keyCode){
        return keyPressed[keyCode];
    }
}

class Time{
    static double timeStarted = System.nanoTime();

    static double getTime(){
        return (System.nanoTime() - timeStarted)*1E-9;
    }
}

class Constants{
    static final int SCREEN_WIDTH = 800;
    static final int SCREEN_HEIGHT = 600;
    static final String SCREEN_TITLE = "Pong";
    static final double PADDLE_SPEED = 200;
    static final double PADDLE_WIDTH = 10;
    static final double PADDLE_HEIGHT = 100;
    static final Color PADDLE_COLOUR = Color.WHITE;

    static final double BALL_WIDTH = 10;
    static final Color BALL_COLOUR = Color.WHITE;

    static final double HZ_PADDING = 40;

    static double INSETS_TOP;
    static double INSETS_BOTTOM;
    
    static final double MAX_ANGLE = 45;
    static final double BALL_SPEED = 350;


    static final int WIN_SCORE = 11;

}
