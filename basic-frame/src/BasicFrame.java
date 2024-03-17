import java.awt.*;
import java.awt.image.*;
import java.awt.event.*;
import java.util.*;
public class BasicFrame{
    // NOTE: BufferedImage to store image data that will be drawn to the frame. 
    public static void main(String[] args){
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice gd = ge.getDefaultScreenDevice();
        GraphicsConfiguration gc = gd.getDefaultConfiguration();
        
        BufferedImage bi = gc.createCompatibleImage(960, 540);
        MyFrame frame = new MyFrame(bi);
        frame.setPreferredSize(new Dimension(960, 540));
        MyWindowListener windowListener = new MyWindowListener();
        frame.addWindowListener(windowListener);
        frame.pack();
        frame.setVisible(true);

        // NOTE: Get raw buffer from BufferedImage to write image data
        int[] biBuffer = ((DataBufferInt)(bi.getRaster().getDataBuffer())).getData();
        // NOTE: just filling the buffer with CYAN colour.
        Arrays.fill(biBuffer, 0x0000FFFF);
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
    // NOTE: implement WindowListener methods, only one that matters is windowClosing, this is the hook to end the app.

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
