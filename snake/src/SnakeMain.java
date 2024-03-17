import java.awt.*;
import java.awt.image.*;
import java.awt.event.*;
import java.util.*;
import java.lang.reflect.*;
import java.nio.file.*;
import java.io.*;

public class SnakeMain{
    public static final int ubuntuMenuHeight = 37;
    public static void main(String[] args)
            throws IllegalAccessException, InvocationTargetException,
            InterruptedException{
        GraphicsEnvironment ge = GraphicsEnvironment
            .getLocalGraphicsEnvironment();
        GraphicsDevice gd = ge.getDefaultScreenDevice();
        GraphicsConfiguration gc = gd.getDefaultConfiguration();
        
        BufferedImage bi = gc.createCompatibleImage(960, 570);
        MyFrame frame = new MyFrame(bi);
        frame.setPreferredSize(new Dimension(960, 540));
        frame.addWindowListener(new MyWindowListener());
        frame.addKeyListener(new MyKeyListener());
        frame.pack();
        frame.setVisible(true);

        // NOTE: Get raw buffer from BufferedImage to write image data
        int[] biBuffer = ((DataBufferInt)(bi.getRaster().getDataBuffer()))
            .getData();
        // NOTE: just filling the buffer with CYAN colour.
        Arrays.fill(biBuffer, 0x00FF0000);

        String className = "SnakeGame";
        String methodName = "gameUpdateAndRender";
        for(;;){
            MyClassLoader myClassLoader = new MyClassLoader();
            Class klass = myClassLoader.getClass(className);
            if(klass!=null){
                try{
                    Method m = klass.getDeclaredMethod(methodName, int[].class, Integer.class);
                    m.invoke(null, biBuffer, 960);
                    frame.repaint();
                }catch(NoSuchMethodException e){
                    System.out.println("Waiting for impl of Method "
                            + methodName);
                }
            }
            Thread.sleep(2000);
        }

    }

}

class MyClassLoader extends ClassLoader{
    public Class getClass(String className){
        Path dynamicFile = null;
        try{
            dynamicFile = Paths.get(
                    System.getProperty("user.dir"), 
                    "build/" + className + ".class");
            byte[] data = Files.readAllBytes(dynamicFile);
            return defineClass(className, data, 0, data.length);
        }catch(IOException e){
            System.out.println("Failed to read data from file ["
                    + dynamicFile + "]");
            return null;
        }
    }
}

class MyFrame extends Frame{

    BufferedImage bi;

    public MyFrame(BufferedImage bi){
        this.bi = bi;
    }
    
    // NOTE: Override paint method to draw to the frame
    @Override
    public void paint(Graphics g){
        g.drawImage(bi, 0, 0, null);
    }

}

class MyWindowListener implements WindowListener{
    // NOTE: implement WindowListener methods, only one that matters is 
    // windowClosing, this is the hook to end the app.

    public void windowDeactivated(WindowEvent e){
        System.out.println("Window Deactivated");
    }
    public void windowActivated(WindowEvent e){
        System.out.println("Window Activated");
    }
    public void windowDeiconified(WindowEvent e){
        System.out.println("Window Deiconified");
    }
    public void windowIconified(WindowEvent e){
        System.out.println("Window Iconified");
    }
    public void windowClosed(WindowEvent e){
        System.out.println("Window Closed");
    }
    public void windowClosing(WindowEvent e){
        System.out.println("Window Closing");
        System.exit(0);
    }
    public void windowOpened(WindowEvent e){
        System.out.println("Window Opened");
    }

}

class MyKeyListener implements KeyListener{
    public void keyReleased(KeyEvent e){
        processKey(e);
    }
    public void keyPressed(KeyEvent e){
        processKey(e);
    }
    public void keyTyped(KeyEvent e){
        // Process keyPressed and keyReleased events OR keyTyped events
        // Dont process both.
    }
    Controller controller = new Controller();

    // Will only look at the letter characters for this example
    boolean[] wasDownKeys = new boolean[26];

    private void processKey(KeyEvent e){
        boolean isDown = e.getID()==KeyEvent.KEY_PRESSED;
        //VK_A = 65, we minus 65 to get 0 for first index
        //VK_Z = 90, we minus 65 to get 25 for last index
        //Any other keys, ignore
        int index = e.getKeyCode()-65;
        if( index < 0 || index > 25){
            return;
        }

        boolean wasDown = wasDownKeys[index];
        
        if(wasDown!=isDown){
            //Here we want to actually process the key
            if(e.getKeyCode()==KeyEvent.VK_W){
                processKeyMessage(controller.moveUp, isDown);
            }
            if(e.getKeyCode()==KeyEvent.VK_S){
                processKeyMessage(controller.moveDown, isDown);
            }
            if(e.getKeyCode()==KeyEvent.VK_A){
                processKeyMessage(controller.moveLeft, isDown);
            }
            if(e.getKeyCode()==KeyEvent.VK_D){
                processKeyMessage(controller.moveRight, isDown);
            }
            System.out.println("------------------");

            for(int i = 0; i < controller.buttonStates.length; ++i){
                System.out.println("button-" + i + " endedDown - " 
                        + controller.buttonStates[i].endedDown
                        );
            }

        }else{
            //Dont do anything for repeating events yet
        }
        if(isDown){
            wasDownKeys[index] = true;
        }else{
            wasDownKeys[index] = false;
        }
    }

    private void processKeyMessage(ButtonState buttonState, boolean isDown){
        if(buttonState.endedDown!=isDown){
            buttonState.endedDown = isDown;
            buttonState.halfTransitionCount++;
        }
    }
}


class ButtonState{
    // down = +1, up = + 1
    int halfTransitionCount;
    boolean endedDown;
}

class Controller{
    ButtonState[] buttonStates;
    ButtonState moveUp;
    ButtonState moveDown;
    ButtonState moveLeft;
    ButtonState moveRight;

    public Controller(){
        moveUp = new ButtonState();
        moveDown = new ButtonState();
        moveLeft = new ButtonState();
        moveRight = new ButtonState();
        buttonStates = new ButtonState[]{moveUp, moveDown, moveLeft, moveRight};
    }
}
