import java.awt.*;
import java.awt.image.*;
import java.awt.event.*;
import java.util.*;
public class ControllerMappingExample{
    public static void main(String[] args){
        Frame frame = new Frame();
        frame.setPreferredSize(new Dimension(100, 100));
        frame.addWindowListener(new MyWindowListener());
        frame.addKeyListener(new MyKeyListener());
        frame.pack();
        frame.setVisible(true);
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

    ////////////// new Code ////////////////

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
