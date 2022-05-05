import processing.core.PApplet;
import processing.event.MouseEvent;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Main extends PApplet {

    float radius;
    int circlePoints;
    int xVal;
    List<Tuple> points;
    List<Float> piVals;
    int speed;
    int sampleSize;
    GameState gameState;
    float graphStep;
    int scrollVal;
    float xOffset;

    public void settings() {
        size(400,400);
    }

    public void setup() {
        surface.setResizable(true);
        textFont(createFont("AndaleMono.tff",16));
        xOffset = 0;
        scrollVal = 0;
        graphStep = 5;
        gameState = GameState.CIRCLE;
        speed = 1;
        sampleSize = 10000;
        points  = new ArrayList<>();
        piVals = new ArrayList<>();
        xVal = 0;
        radius = width/2;
        circlePoints = 0;
        strokeWeight(2);
        background(0);
        //draw circle
        noFill();
        stroke(255);
        ellipse(width/2,height/2,radius*2,radius*2);
    }

    public void draw() {
        if (piVals.size() > 0) print("PI APPROX: " + piVals.get(piVals.size()-1));
        print(" | SAMPLE SIZE: " + sampleSize);
        print(" | CURRENT NUMBER OF POINTS: " + points.size());
        println();

        radius = width/2;

        for (int i = 0; i < speed; i++) {
            float x = random(0,width);
            float y = random(0,width);

            float xDist = Math.abs(x - width/2);
            float yDist = Math.abs(y - width/2);
            float distanceFromCenter = (float) Math.sqrt(Math.pow(xDist,2) + Math.pow(yDist,2));

            //add points and info based on their pos.
            if (distanceFromCenter < radius) {
                points.add(new Tuple(x,y,true));
                circlePoints++;
            }
            else {
                points.add(new Tuple(x,y,false));
            }

            if (points.size() > sampleSize) {
                if (points.get(0).isInCircle()) circlePoints--;
                points.remove(0);
            }
        }

        float approxPi = (float) ((4.0*circlePoints)/points.size());
        piVals.add(approxPi);

        //daw circle and points
        if (gameState == GameState.CIRCLE) {
            //DRAW
            background(0);
            fill(0);
            strokeWeight(1);
            stroke(255);
            ellipse(width/2,width/2,radius*2,radius*2);
            noStroke();
            for (int i = 0; i < points.size(); i++) {
                Tuple point = points.get(i);
                //fill green if point in circle else red
                fill((point.isInCircle()) ? new Color(0,255,0).getRGB() : new Color(255,0,0).getRGB());
                ellipse(point.getX(),point.getY(),3,3);
            }
        }
        //bad pan/scroll graph code
        if (gameState == GameState.GRAPH) {
            background(255);
            drawGrid();
            fill(255,100,0);
            if (scrollVal != 0) {

                if (graphStep <= 1) {
                    //if graphStep is at 1 or less instead of subtracting the scrollVal(1) subtract 1/4 of itself or add
                    //so graphStep doesn't go directly to 0 and break graph
                    if (scrollVal < 0) graphStep -= graphStep/4;
                    if (scrollVal > 0) graphStep += graphStep/4;
                }
                else graphStep += scrollVal;
                scrollVal = 0;
            }
            //start graph at offset
            float xVal = xOffset;

            for (int i = 0; i < piVals.size(); i ++) {
                float yVal = map(piVals.get(i),0,4,height,0);

                ellipse(xVal,yVal,8,8);

                //draw lines between points if they are within screen
                if (xVal >= 0 && xVal < width && i != 0) {
                    stroke(255,100,0);
                    line(xVal - graphStep,map(piVals.get(i-1),0,4,height,0),xVal,yVal);
                    noStroke();
                }

                xVal += graphStep;
            }
        }
    }

    public void drawGrid() {
        //grid markings
        stroke(230);
        line(0,3*height/4,width,3*height/4);
        line(0,2*height/4,width,2*height/4);
        line(0,height/4,width,height/4);
        stroke(0);
        textSize(15);
        fill(50);
        text(1,4,3*height/4 - 5);
        text(2,4,2*height/4 - 5);
        text(3,4,height/4 - 5);
        text(4,4,5);
        noStroke();
    }

    public void keyPressed() {
        if (key == '1') gameState = GameState.CIRCLE;
        if (key == '2') gameState = GameState.GRAPH;
        if (key == 'r') {
            circlePoints = 0;
            points.clear();
            piVals.clear();
            xOffset = 0;
        }
    }

    public void mouseWheel(MouseEvent event) {
        scrollVal = event.getCount();
    }

    public void mouseDragged() {
        if (gameState != GameState.GRAPH) return;
        xOffset += mouseX - pmouseX;
    }




    public static void main(String[] args) {
        PApplet.main("Main");
    }
}
