import java.awt.*;
import java.awt.image.*;
import java.awt.event.*;
import java.util.*;
public class DoubleBufferFrame{
    // NOTE: BufferedImage to store image data that will be drawn to the frame. 
    public static void main(String[] args) throws InterruptedException{
        GraphicsEnvironment ge = GraphicsEnvironment
            .getLocalGraphicsEnvironment();
        GraphicsDevice gd = ge.getDefaultScreenDevice();
        GraphicsConfiguration gc = gd.getDefaultConfiguration();
        
        BufferedImage current = gc.createCompatibleImage(960, 540);
        BufferedImage next = gc.createCompatibleImage(960, 540);
        MyFrame frame = new MyFrame(current, next);
        frame.setPreferredSize(new Dimension(960, 540));
        MyWindowListener windowListener = new MyWindowListener();
        frame.addWindowListener(windowListener);
        frame.pack();
        frame.setVisible(true);

        // NOTE: Get raw buffer from BufferedImage to write image data
        int[] currentBuffer = ((DataBufferInt)(current.getRaster()
                    .getDataBuffer())).getData();
        int[] nextBuffer = ((DataBufferInt)(next.getRaster()
                    .getDataBuffer())).getData();
        // NOTE: just filling the buffer with CYAN colour.
        
        boolean toggle = false;
        int colour = 0x0000FFFF;
        for(;;){
            // NOTE: Prepare next Buffer
            
            if(toggle){
                colour = 0x00FFFF00;
                toggle = false;
            }else{
                colour = 0x0000FFFF;
                toggle = true;
            } 
            Arrays.fill(nextBuffer, colour);

            // NOTE: Show Current Buffer
            frame.repaint();
            frame.swapBuffers();

            // NOTE: Flip Buffer

            int[] temp = currentBuffer;
            currentBuffer = nextBuffer;
            nextBuffer = temp;

            Thread.sleep(1000);
        }
    }
}

class MyFrame extends Frame{

    BufferedImage current;
    BufferedImage next;

    public MyFrame(BufferedImage current, BufferedImage next){
        this.current = current;
        this.next = next;
    }
    
    // NOTE: Override paint method to draw to the frame
    @Override
    public void paint(Graphics g){
        g.drawImage(current, 0, 0, null);
    }
    @Override
    public void repaint(){
        System.out.println("Repainting...");
        super.repaint();
    }
    public void swapBuffers(){
        BufferedImage temp = current;
        current = next;
        next = temp;
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
