import java.awt.*;
import java.awt.image.*;
import java.awt.event.*;
import java.util.*;

public class SnakeMain{
    public static final int ubuntuMenuHeight = 37;
    public static void main(String[] args){
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


