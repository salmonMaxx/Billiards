/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package billiards;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 *
 * @author maxxw
 */
@SuppressWarnings("serial")
class Table extends JPanel implements MouseListener, MouseMotionListener, ActionListener {

    private final int   HEIGHT         = 640;
    private final int   WIDTH          = 800;
    private final int   WALL_THICKNESS = 20;
    public final Color COLOR           = Color.green;
    private final Color WALL_COLOR     = Color.darkGray;
    private final int noOfBalls        = 0;
    private Ball[] ballArray;
    private final Timer simulationTimer;
    public WhiteBall whiteBall;
    private final Coord whiteBallInitPos;
    public final Hole[] topHoleArray = new Hole[3];
    public final Hole[] bottomHoleArray = new Hole[3];
    public Hole[] holeArray;
    
    Table() {
        setPreferredSize(new Dimension(WIDTH + 2 * WALL_THICKNESS,
                                       HEIGHT + 2 * WALL_THICKNESS));
        holeArray = createHoles();
        ballArray = createBallTriangle(10, WIDTH - WIDTH/1.6, HEIGHT/2);
        whiteBallInitPos = new Coord(WIDTH - WIDTH/8,HEIGHT/2);
        whiteBall = new WhiteBall(whiteBallInitPos,this);
        addMouseListener(this);
        addMouseMotionListener(this);
        simulationTimer = new Timer((int) (1000.0 / Billiards.UPDATE_FREQUENCY), this);
    }
    
    public void actionPerformed(ActionEvent e) {          
        for(Ball ball:ballArray){
            ball.move(ballArray, noOfBalls);
        }
        whiteBall.move(ballArray, noOfBalls);              //kolla hål-buggen!!!!!!!!!!!!!!!
        if(whiteBall.isBallFallIn()){
            whiteBall.fallIn();
        }
        repaint();
    }

    public void mousePressed(MouseEvent event) {
        Coord mousePosition = new Coord(event);
        whiteBall.setAimPosition(mousePosition);
        repaint();                          //  To show aiming line
    }

    public void mouseReleased(MouseEvent e) {
        whiteBall.shoot();
        if (!simulationTimer.isRunning()) {
            simulationTimer.start();      
        }
    }

    public void mouseDragged(MouseEvent event) {
        Coord mousePosition = new Coord(event);
        whiteBall.updateAimPosition(mousePosition);
        repaint();
    }
    
    public int getTheWidth(){
        return WIDTH;
    }
    
    public int getTheHeight(){
        return HEIGHT;
    }
    
    public int getWallThickness(){
        return WALL_THICKNESS;
    }
    

    // Obligatory empty listener methods
    public void mouseClicked(MouseEvent e) {}
    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}
    public void mouseMoved(MouseEvent e) {
//    	System.out.println("x: "+e.getX());
//    	System.out.println("y: "+e.getY()+"\n");
    }
    
    @Override
    public void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        Graphics2D g2D = (Graphics2D) graphics;
        g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, // This makes the graphics smoother
                RenderingHints.VALUE_ANTIALIAS_ON);

        g2D.setColor(WALL_COLOR);
        g2D.fillRect(0, 0, WIDTH + 2 * WALL_THICKNESS, HEIGHT + 2 * WALL_THICKNESS);

        g2D.setColor(COLOR);
        g2D.fillRect(WALL_THICKNESS, WALL_THICKNESS, WIDTH, HEIGHT);
        
        for (Ball balls : ballArray) {
            balls.paint(g2D);
        }
        for(Hole hole:holeArray){
            hole.paint(g2D);
        }
        whiteBall.paint(g2D);
    }   
    
    public Hole[] concatenateHoleArray(Hole[] firstArray, Hole[] secondArray) {
        int firstLength = firstArray.length;
        int secondLength = secondArray.length;
        Hole[] concatenatedArray = new Hole[firstLength+secondLength];
        System.arraycopy(firstArray, 0, concatenatedArray, 0, firstLength);
        System.arraycopy(secondArray, 0, concatenatedArray, firstLength, secondLength);
        return concatenatedArray;
    }
    
    public Ball[] createBallTriangle(int totalNoOfBalls, double startPosX, double startPosY){
        int row = 1;
        int noOfBalls = 1;
        int maxNoOffBalls = 1;
        double xPos;
        double yPos;
        Coord position;
        Ball[] theArray = new Ball[totalNoOfBalls];
        for(int index = 0; index < totalNoOfBalls ; index ++){
            yPos = startPosY + (row - 1) * 20 -(maxNoOffBalls - noOfBalls) * 40;
            xPos = startPosX - (row - 1) * 30;
            position = new Coord(xPos,yPos);
            theArray[index] = new Ball(position, this);
            noOfBalls ++;
            if (noOfBalls >= maxNoOffBalls + 1){
                row ++;
                maxNoOffBalls = maxNoOffBalls + row;
            }
        }
        System.out.println("Balls have been created");
        return theArray;
    }

    private Hole[] createHoles() {
        //create top holes
        for (int i = 0; i < 3; i++){
            Coord initPos = new Coord(WALL_THICKNESS + i*(WIDTH/2),WALL_THICKNESS);
            topHoleArray[i] = new Hole(initPos);
        }
        //create bottom holes
        for (int i = 0; i < 3; i++){
            Coord initPos = new Coord(WALL_THICKNESS + i*(WIDTH/2),WALL_THICKNESS + HEIGHT);
            bottomHoleArray[i] = new Hole(initPos);
        }
        //make them one array
        holeArray = concatenateHoleArray(topHoleArray,bottomHoleArray);
        
        return holeArray;
    }
    
    public boolean isBallFallIn(){
        //System.out.println(holeArray.length);
        for (Hole hole:holeArray){
            for(Ball ball:ballArray){
                int ballNo = 1;
                System.out.println(ballNo + ": " + ball.position);
                double theDistance = Coord.distance(hole.position, ball.position);
                return (theDistance < 50);
            }
        }
        return false;
    }
}
