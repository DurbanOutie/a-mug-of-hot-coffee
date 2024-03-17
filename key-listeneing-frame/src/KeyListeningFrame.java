import java.awt.*;
import java.awt.image.*;
import java.awt.event.*;
import java.util.*;
public class KeyListeningFrame{
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
        System.out.println(e);
    }
    public void keyPressed(KeyEvent e){
        System.out.println(e);
    }
    public void keyTyped(KeyEvent e){
        // Process keyPressed and keyReleased events OR keyTyped events
        // Dont process both.
    }
}
