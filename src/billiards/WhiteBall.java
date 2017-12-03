package billiards;

import java.awt.Color;
import java.awt.Graphics2D;

/**
 *
 * @author maxxw
 */
public class WhiteBall extends Ball{

    private Coord aimPosition;              // if aiming for a shot, ow null
    
    WhiteBall(Coord initialPosition, Table theTable){
        super(initialPosition, theTable);
        velocity = Coord.ZERO;
        COLOR = Color.white;
    }
    
    private boolean isAiming() {
        return aimPosition != null;
    }
    
    void shoot() {
        if (isAiming() && !isMoving()) {
            //System.out.println("shootie shootie");
            Coord aimingVector = Coord.sub(position, aimPosition);
            velocity = Coord.mul(Math.sqrt(10.0 * aimingVector.magnitude() / Billiards.UPDATE_FREQUENCY),
                                 aimingVector.norm());  // don't ask - determined by experimentation
            aimPosition = null;
        }
    }
    
    @Override
    void move(Ball[] theBalls, int noOfBalls) {
    	//System.out.println(this + " vit inne i move");
    	if (this.isMoving()) {   
            this.position.increase(velocity);      
            this.velocity.decrease(Coord.mul(FRICTION_PER_UPDATE, velocity.norm()));
            if(isHitEdge()){
                wallBounce();
            }
            for(Ball ball:theBalls){
                if(isCollision(ball) && !isHitEdge() 
                   && isMovingTowardsEachother(ball) 
                   && ball != this){
                    impact(ball);
                }
            }
        }
    }
  
    void setAimPosition(Coord grabPosition) {
        if (Coord.distance(position, grabPosition) <= RADIUS && !isMoving()) {
            aimPosition = grabPosition;
        }
    }
    
    void updateAimPosition(Coord newPosition) {
        if (isAiming()){
            aimPosition = newPosition;
        }
    }
    
    private void paintAimingLine(Graphics2D graph2D) {
        Coord.paintLine(graph2D, aimPosition, Coord.sub(Coord.mul(2, position), 
                        aimPosition));
    }
    
    @Override
    void paint(Graphics2D g2D){
    	g2D.setColor(Color.black);
        g2D.fillOval(
                (int) (position.x - RADIUS + 0.5),
                (int) (position.y - RADIUS + 0.5),
                (int) DIAMETER,
                (int) DIAMETER);
        g2D.setColor(COLOR);
        g2D.fillOval(
                (int) (position.x - RADIUS + 0.5 + BORDER_THICKNESS),
                (int) (position.y - RADIUS + 0.5 + BORDER_THICKNESS),
                (int) (DIAMETER - 2 * BORDER_THICKNESS),
                (int) (DIAMETER - 2 * BORDER_THICKNESS));
    	if (isAiming() && !isMoving()) {
            paintAimingLine(g2D);
        }
    }
}