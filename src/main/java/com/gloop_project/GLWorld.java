package com.gloop_project;
import GLOOP.*;
import java.awt.Robot;
import java.awt.AWTException;
/*git add .
git commit -m "changes made"
git push
does this shit work or not??!
git pull
*/

public class GLWorld{
    
    GLKamera cam;
    GLTastatur keys;
    GLMaus mouse;
    GLLicht lighting;
    GLBoden floor;
    GLHimmel sky;
    GLVektor camVector, movementVector, snowmanMovementVector, facingDirection, viewpointVector;
    Robot robot;
    Snowman snowman;

    double speed = 5;
    double gravity = 2;
    double fallSpeed = 0, snowmanFallSpeed = 0;
    int terminalVelocity = 50;
    int characterHeight = 200;
    int[] mousePos; int[] mousePosOld = {0,0};
    boolean enableFlight = true;
    String libPath = "";
    
    public GLWorld(){
        cam = new GLKamera();
        camVector = new GLVektor(cam.gibBlickrichtung());
        movementVector = new GLVektor(0,0,0);
        snowmanMovementVector = new GLVektor(1,0,0);

        libPath = System.getProperty("user.dir");
        libPath = libPath + "\\lib\\";

        cam.setzePosition(800,1000,800);
        facingDirection = new GLVektor(1,0,0);
        viewpointVector = new GLVektor(0,0,0);

        try{
            robot = new Robot();
            robot.mouseMove(500, 300);
        } catch (AWTException e){}
        
        keys = new GLTastatur();
        mouse = new GLMaus();    
        lighting = new GLLicht();
        floor = new GLBoden(libPath + "floor.jpg");
        sky = new GLHimmel(libPath + "skybox.png");

        snowman = new Snowman(libPath,0,0,0,1);
        
        while(true){
            camVector = cam.gibBlickrichtung();
            movementVector = camVector;
            movementVector.subtrahiere(new GLVektor(0,movementVector.gibY(),0));
            flight();
            handleInput();
            cameraMovement();
            try{
                Thread.sleep(5);
            } catch(InterruptedException e){}
        }
    }

    void handleInput(){
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

        if(keys.istGedrueckt('t') && speed < 1000){
            speed *= 1.05;
        }
        if(keys.istGedrueckt('g') && speed > 0.05){
            speed *= 0.975;
        }

        SnowmanMovement();
        horizontalMovement();
        cam.verschiebe(movementVector);
    }

    void horizontalMovement(){
        double angle = 0;
        int keysPressed = 0;
        boolean isMoving = false;

        if(keys.istGedrueckt('a') &! keys.istGedrueckt('d')){
            angle += 90;
            isMoving = true;
            keysPressed++;
        }
        if(keys.istGedrueckt('d') &! keys.istGedrueckt('a')){
            angle += 270;
            isMoving = true;
            keysPressed++;
        }
        if(keys.istGedrueckt('w') &! keys.istGedrueckt('s')){
            isMoving = true;
            keysPressed++;
        }
        if(keys.istGedrueckt('s') &! keys.istGedrueckt('w')){
            angle += 180;
            isMoving = true;
            keysPressed++;
        }
        if(keys.istGedrueckt('w') && keys.istGedrueckt('d') &! keys.istGedrueckt('a') &! keys.istGedrueckt('s')){
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

    void snowmanGravity(){
        if(snowman.giveY() >= 0 || snowmanFallSpeed < 0){
            snowman.moveBy(0, -snowmanFallSpeed, 0);
            snowmanFallSpeed += 0.1*gravity*snowman.giveSize();
        }
        else{
            snowmanFallSpeed = 0;
        }
        if(fallSpeed > terminalVelocity*snowman.giveSize()){
            fallSpeed = terminalVelocity*snowman.giveSize();
        }
        if(snowman.giveY() < 0){
            snowman.moveBy(0, -snowman.giveY(), 0);
        }
    }

    public void toggleFlight(){
        enableFlight = !enableFlight;
    }

    void cameraMovement(){
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

    void SnowmanMovement(){
        double angle = 0;
        int keysPressed = 0;
        boolean isMoving = false;
        double snowmanRotationAngle = snowman.giveRotation();
        snowmanMovementVector.setzeKomponenten(1,0,0);

        if(keys.istGedrueckt('z')){
            snowman.rotate(0,2,0);
        }
        if(keys.istGedrueckt('n') && snowman.giveY() <= 0){
            snowmanFallSpeed = -5*gravity*snowman.giveSize();
        }
        if(keys.istGedrueckt('i')){
            snowman.rotate(0,-2,0);
        }
        if(keys.istGedrueckt('h') &! keys.istGedrueckt('k')){
            angle += 90;
            isMoving = true;
            keysPressed++;
        }
        if(keys.istGedrueckt('k') &! keys.istGedrueckt('h')){
            angle += 270;
            isMoving = true;
            keysPressed++;
        }
        if(keys.istGedrueckt('u') &! keys.istGedrueckt('j')){
            isMoving = true;
            keysPressed++;
        }
        if(keys.istGedrueckt('j') &! keys.istGedrueckt('u')){
            angle += 180;
            isMoving = true;
            keysPressed++;
        }
        if(keys.istGedrueckt('u') && keys.istGedrueckt('k') &! keys.istGedrueckt('h') &! keys.istGedrueckt('j')){
            angle = 630;
        }

        if(isMoving){
            snowmanMovementVector.drehe(0,(angle/keysPressed)+snowmanRotationAngle,0);
            snowmanMovementVector.normiere();
        }
        else{
        snowmanMovementVector.multipliziere(0);
        }
        snowmanMovementVector.multipliziere(speed);
        snowman.moveBy(snowmanMovementVector.gibX(),0,snowmanMovementVector.gibZ());
        snowmanGravity();
    }
}