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
class Ball {

    protected Color  COLOR                     = Color.red;
    protected int    BORDER_THICKNESS          = 2;
    static final double RADIUS                 = 15;
    protected final double DIAMETER            = 2 * RADIUS;
    private final double FRICTION              = 0.015;   // its friction constant (normed for 100 updates/second)
    protected final double FRICTION_PER_UPDATE =          // friction applied each simulation step
                          1.0 - Math.pow(1.0 - FRICTION,       // don't ask - I no longer remember how I got to this
                                         100.0 / Billiards.UPDATE_FREQUENCY);           
    Coord position;
    Coord velocity;
    private final Table table;
    Hole[] holeArray;
    
    Ball(Coord initialPosition, Table theTable) {
        position = initialPosition;
        velocity = Coord.ZERO;       // WARNING! Are initial velocities clones or aliases??
        table = theTable;
        holeArray = table.holeArray;
    }
    
    public boolean isMoving() {                                // if moving too slow I am deemed to have stopped
        return velocity.magnitude() > FRICTION_PER_UPDATE;
    }
    
    void move(Ball[] theBalls, int noOfBalls) {
    	//System.out.println(theBalls+" inne i move");
    	//System.out.print(this.position.x+" ");
    	//System.out.print(this.position.y+"\n");
        if (this.isMoving()) {   
            this.position.increase(velocity);      
            this.velocity.decrease(Coord.mul(FRICTION_PER_UPDATE, velocity.norm()));
            if(this.isBallFallIn()){
                this.fallIn();
            }
            if(isHitEdge()){
                wallBounce();
            }
            for(Ball ball:theBalls){ //
                if(isCollision(ball) && !isHitEdge() 
                   && isMovingTowardsEachother(ball)                     
                   && ball != this){                 
                    impact(ball);                    
                }
            }
        }   
    }
    
    // paint: to draw the ball, first draw a black ball
    // and then a smaller ball of proper color inside
    // this gives a nice thick border
    void paint(Graphics2D g2D) {
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
//        if (isAiming()) {
//            paintAimingLine(g2D);
//        }
    }
    

    
    public boolean isHitEdge(){  
        return (position.x - RADIUS < table.getWallThickness()
                ||
                position.y - RADIUS < table.getWallThickness()
                ||
                position.x + RADIUS > table.getTheWidth() + table.getWallThickness()
                || 
                position.y + RADIUS > table.getTheHeight() + table.getWallThickness());                                             
    }
    
    public void wallBounce(){
        if(position.x - RADIUS < table.getWallThickness()
           || 
           position.x + RADIUS > table.getTheWidth()){

           velocity.x *= -1;
        
        }
        
        if(position.y - RADIUS < table.getWallThickness()
           ||
           position.y + RADIUS > table.getTheHeight()){
           
           velocity.y *= -1;
        
        }      
    }
    
    public boolean isCollision(Ball otherBall){
        return (Coord.distance(this.position, otherBall.position) <= 2*RADIUS);
    }
   
    public void impact(Ball otherBall){
        Coord diffCoords = Coord.sub(this.position, otherBall.position);
        Coord unitVector = diffCoords.norm();
      
        double impulse = Coord.scal(otherBall.velocity, unitVector) - 
        
        Coord.scal(this.velocity, unitVector);
        Coord impulseUnit = Coord.mul(impulse, unitVector);
        
        this.velocity.x += impulseUnit.x;
        this.velocity.y += impulseUnit.y;
        
        otherBall.velocity = Coord.sub(otherBall.velocity, impulseUnit);
    }
    
    public boolean isMovingTowardsEachother(Ball otherBall){
        Coord thisPosition = new Coord(this.position.x, this.position.y);
        Coord thisNextPosition = new Coord(this.position.x + velocity.x, this.position.y + velocity.y);
    
        Coord otherBallsPosition = new Coord(otherBall.position.x, otherBall.position.y);
        Coord otherBallsNextPosition = new Coord(otherBall.position.x + otherBall.velocity.x, otherBall.position.y + otherBall.velocity.y);
        
        double thisDistance = Coord.distance(thisPosition, otherBallsPosition);
        double nextDistance = Coord.distance(thisNextPosition, otherBallsNextPosition);
        
        return (thisDistance > nextDistance);
    }
    
    public void setColor(Color color){
        COLOR = color;
    }
    
    public int isMovingCounter(Ball[] ballArray, WhiteBall whiteBall){
    	int movingCounter = 0;
    	for (Ball ball : ballArray){
    		if(ball.isMoving()){
    			movingCounter++;
    		}
    	}
    	if(!whiteBall.isMoving()){
    		return movingCounter;
    	}
    	else{
    		return 1;
    	}
    }
    
    public boolean isBallFallIn(){
        //System.out.println(holeArray.length);
        for (Hole hole:holeArray){
            double theDistance = Coord.distance(hole.position, this.position);
            return (theDistance < 50);
            }
        return false;
    }
    
    public void fallIn(){
        this.position = new Coord(table.getTheWidth() + 100, table.getTheHeight() + 100);
        this.velocity = Coord.ZERO;
    }
}
