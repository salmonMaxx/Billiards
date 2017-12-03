/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package billiards;

import java.awt.Color;
import java.awt.Graphics2D;

/**
 *
 * @author maxxw
 */
public class Hole {
    Coord position; 
    int RADIUS;
    Color COLOR;
    
    Hole(Coord thePosition){
        position = thePosition;
        RADIUS = 20;
        COLOR = Color.black;
    }
    void paint(Graphics2D g2D) {
       g2D.setColor(Color.black);
       g2D.fillOval(
               (int) (position.x - RADIUS + 0.5),
               (int) (position.y - RADIUS + 0.5),
               (int) 2*RADIUS,
               (int) 2*RADIUS);
//        if (isAiming()) {
//            paintAimingLine(g2D);
//        }
    }
     
//    boolean isBallFallIn(Ball ball){
//        double theDistance = Coord.distance(position, ball.position);
//        return (theDistance < 10);
//    }
}
