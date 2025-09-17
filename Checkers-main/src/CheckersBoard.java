

import javax.swing.JFrame;
import java.awt.event.*;
import java.awt.*;

public class CheckersBoard {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Checkers Game");
        frame.setSize(Globals.WIDTH, Globals.HEIGHT);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        frame.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                Globals.WIDTH = frame.getWidth();
                Globals.HEIGHT = frame.getHeight();
            }

            //frame.revalidate(); 
            //frame.repaint();
        });

        Board gameBoard = new Board();
        frame.add(gameBoard);

        frame.setVisible(true);
    }
}
