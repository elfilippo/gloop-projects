package com.gloop_project;

import java.awt.event.InputEvent;
import java.awt.AWTException;
import java.awt.Robot;
import GLOOP.*;

public class GLWorld{
    
    private GLKamera cam;
    private GLTastatur keys;
    private GLMaus mouse;
    private GLVektor camVector, movementVector, facingDirection, viewpointVector;
    private Robot robot;
    private Snowman snowman;
    private Cuboid cuboid;

    private double speed = 5;
    private double gravity = 2;
    private double fallSpeed = 0;
    private int terminalVelocity = 50;
    private int characterHeight = 200;
    private int[] mousePosOld = {0,0};
    private boolean enableFlight = true;
    static String libPath = "";
    
    public GLWorld(){
        cam = new GLSchwenkkamera();
        camVector = new GLVektor(cam.gibBlickrichtung());
        movementVector = new GLVektor(0,0,0);

        libPath = System.getProperty("user.dir");
        libPath = libPath + "\\lib\\";

        cam.setzePosition(800,1000,800);
        cam.setzeBlickpunkt(799.99,1000,800);
        facingDirection = new GLVektor(1,0,0);
        viewpointVector = new GLVektor(0,0,0);

        try{
            robot = new Robot();
        } catch (AWTException e){}
        robot.mouseMove(500, 300);

        keys = new GLTastatur();
        mouse = new GLMaus();    
        new GLLicht();
        new GLBoden(libPath + "floor.jpg");
        new GLHimmel(libPath + "skybox.png");

        snowman = new Snowman(0,0,0,1);
        cuboid = new Cuboid(300,25,0,100,50,100);
        
        while(true){
            camVector.setzeKomponenten(cam.gibBlickrichtung());
            flight();
            handleInput();
            
            //cameraMovement();
            snowman.movement(keys,cuboid,gravity,terminalVelocity,speed);
            try{
                Thread.sleep(5);
            } catch(InterruptedException e){}
        }
    }

    void handleInput(){
        if(!mouse.gedruecktRechts()){
            robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
        }

        if(keys.istGedrueckt('o')){
            System.exit(0);
        }
        if(keys.istGedrueckt(' ')){
            if(cam.gibY()-characterHeight <= 0){
                fallSpeed = -20;
            }
        }

        if(keys.istGedrueckt('f')){
            toggleFlight();
        }
        if(keys.istGedrueckt('r')){
            resetScene();
        }

        if(keys.istGedrueckt('t') && speed < 1000){
            speed *= 1.05;
        }
        if(keys.istGedrueckt('g') && speed > 0.05){
            speed *= 0.975;
        }

        if(keys.istGedrueckt('b')){
            snowman.scaleBy(1.01);
        }
        if(keys.istGedrueckt('v')){
            snowman.scaleBy(0.975);
        }
        
        horizontalMovement();
        cam.verschiebe(movementVector);
    }

    void horizontalMovement(){
        movementVector.setzeKomponenten(camVector.gibX(),0,camVector.gibZ());
        movementVector.normiere();
        double angle = 0;
        int keysPressed = 0;
        boolean isMoving = false;

        if(keys.istGedrueckt('a') && !keys.istGedrueckt('d')){
            angle += 90;
            isMoving = true;
            keysPressed++;
        }
        if(keys.istGedrueckt('d') && !keys.istGedrueckt('a')){
            angle += 270;
            isMoving = true;
            keysPressed++;
        }
        if(keys.istGedrueckt('w') && !keys.istGedrueckt('s')){
            isMoving = true;
            keysPressed++;
        }
        if(keys.istGedrueckt('s') && !keys.istGedrueckt('w')){
            angle += 180;
            isMoving = true;
            keysPressed++;
        }
        if(keys.istGedrueckt('w') && keys.istGedrueckt('d') && !keys.istGedrueckt('a') && !keys.istGedrueckt('s')){
            angle = 630;
        }

        if(isMoving){
            movementVector.drehe(0,angle/keysPressed,0);
        }
        else{
            movementVector.multipliziere(0);
        }

        movementVector.normiere();
        movementVector.multipliziere(speed);
    }

    void flight(){
        if(enableFlight){
            if(keys.istGedrueckt(' ')){
                cam.verschiebe(new GLVektor(0,speed,0));
            }
            if(keys.shift()){
                cam.verschiebe(new GLVektor(0,-speed,0));
            }
        }
        else{
            gravity();
        } 
    }

    void gravity(){
        if(cam.gibY()-characterHeight >= 0){
            cam.verschiebe(0,-fallSpeed,0);
            fallSpeed += gravity/40;
        }
        else{
            fallSpeed = 0;
        }
        if(fallSpeed > terminalVelocity){
            fallSpeed = terminalVelocity;
        }
        if(cam.gibY()-characterHeight <= 0){
            cam.setzePosition(cam.gibX(),characterHeight,cam.gibY());
        }
    }

    public void toggleFlight(){
        enableFlight = !enableFlight;
    }

    void cameraMovement(){
        movementVector.subtrahiere(new GLVektor(0,movementVector.gibY(),0));
        int mousePos[] = {mouse.gibX(),mouse.gibY()};
        int mousePosDeltaX = mousePosOld[0]-mousePos[0];
        int mousePosDeltaY = mousePosOld[1]-mousePos[1];

        facingDirection.drehe(0,mousePosDeltaX,-mousePosDeltaY);
        
        viewpointVector.setzeKomponenten(facingDirection.gibX(),facingDirection.gibY(),facingDirection.gibZ());
        viewpointVector.addiere(cam.gibPosition());
        cam.setzeBlickpunkt(viewpointVector);

        mousePosOld[0] = mousePos[0];
        mousePosOld[1] = mousePos[1];
        /* if(mousePos[0] > 300 || mousePos[1] > 300 || mousePos[0] < -300 || mousePos[1] < -300){
            robot.mouseMove(500,500);
        }
        */
    }

    void resetScene(){
        cam.setzePosition(800,1000,800);
        snowman.moveTo(0,0,0);
        snowman.scaleBy(1/snowman.giveSize());
    }
    
    public static String getLibPath(){
        return libPath;
    }

}