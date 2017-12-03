package billiards;

import javax.swing.*;

/**
 *
 * @author Joachim Parrow 2010 rev 2011, 2012, 2013, 2015, 2016
 *
 * Simluator for two balls
 * 
 * Ändrat av Maxx Wallberg 2017-11-15
 */

public class Billiards {

    final static int UPDATE_FREQUENCY = 100;    // GlobalÂ constant: fps, ie times per second to simulate

    public static void main(String[] args) {

        JFrame frame = new JFrame("Collisions!");          
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Table table = new Table();     

        frame.add(table);  
        frame.pack();
        frame.setVisible(true);
    }
}

