package com.gloop_project;
import GLOOP.*;

public class Snowman{

    GLWorld world;
    GLKugel bottomSphere, middleSphere, upperSphere, eye1, eye2;
    GLZylinder cylinder, cylinderRim;
    GLKegel nose;
    GLVektor movementVector, snowmanMovementVector;
    double size, angle;
    double x, y, z;
    double snowmanFallSpeed;
    record Attributes(double x, double y, double z, double size){}

    public Snowman(double x,double y,double z,double size){
        this.size = size;
        this.x = x;
        this.y = y;
        this.z = z;
        angle = 0;

        snowmanFallSpeed = 0;
        snowmanMovementVector = new GLVektor(1,0,0);
        world = new GLWorld();

        bottomSphere = new GLKugel(x,(45 + y) * size,z,50 * size);
        middleSphere = new GLKugel(x,(125 + y) * size,z,38 * size);
        upperSphere = new GLKugel(x,(188 + y) * size,z,30 * size);

        eye1 = new GLKugel(x + (18 * size),(202 + y) * size,z + (-15 * size),4 * size);
        eye2 = new GLKugel(x + (18 * size),(202 + y) * size,z + (15 * size),4 * size);
        eye1.setzeTextur(GLWorld.getLibPath() + "coal.png");
        eye2.setzeTextur(GLWorld.getLibPath() + "coal.png");

        cylinder = new GLZylinder(x,(240 + y) * size,z,19 * size, 50 * size);
        cylinderRim = new GLZylinder(x,(215 + y) * size,z,30 * size, 2 * size);
        cylinder.drehe(90,0,0);
        cylinderRim.drehe(90,0,0);
        cylinder.setzeTextur(GLWorld.getLibPath() + "cylinder.png");
        cylinderRim.setzeTextur(GLWorld.getLibPath() + "cylinder.png");

        nose = new GLKegel(x + (40 * size),(190 + y) * size,z,4 * size,25 * size);
        nose.drehe(0,270,0);
        nose.setzeTextur(GLWorld.getLibPath() + "carrot.png");
    }

    public Attributes getAttributes(){
        return new Attributes(this.x,this.y - 45 * this.size,this.z,this.size);
    }

    public void moveBy(double x,double y,double z){
        bottomSphere.verschiebe(x, y, z);
        middleSphere.verschiebe(x,y,z);
        upperSphere.verschiebe(x,y,z);
        cylinder.verschiebe(x,y,z);
        cylinderRim.verschiebe(x,y,z);
        nose.verschiebe(x,y,z);
        eye1.verschiebe(x,y,z);
        eye2.verschiebe(x,y,z);
        this.x += x;
        this.y += y;
        this.z += z;
    }

    public void moveTo(double x,double y,double z){
        bottomSphere.setzePosition(x * size,y + 45*size,z * size);
        middleSphere.setzePosition(x * size,y + 125*size, z * size);
        upperSphere.setzePosition(x * size, y + 188*size, z * size);
        cylinder.setzePosition(x * size, y + 240*size, z * size);
        cylinderRim.setzePosition(x * size, y + 215*size, z * size);
        nose.setzePosition(x + 40*size, y + 190*size, z * size);
        eye1.setzePosition(x + 18*size, y + 202*size, z + -15*size);
        eye2.setzePosition(x + 18*size, y + 202*size, z + 15*size);
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public void rotate(double x,double y,double z){
        Attributes attributes = this.getAttributes();
        bottomSphere.drehe(x,y,z,attributes.x(),attributes.y(),attributes.z());
        middleSphere.drehe(x,y,z,attributes.x(),attributes.y(),attributes.z());
        upperSphere.drehe(x,y,z,attributes.x(),attributes.y(),attributes.z());
        cylinder.drehe(x,y,z,attributes.x(),attributes.y(),attributes.z());
        cylinderRim.drehe(x,y,z,attributes.x(),attributes.y(),attributes.z());
        nose.drehe(x,y,z,attributes.x(),attributes.y(),attributes.z());
        eye1.drehe(x,y,z,attributes.x(),attributes.y(),attributes.z());
        eye2.drehe(x,y,z,attributes.x(),attributes.y(),attributes.z());
        angle += y;
    }

    public double giveRotation(){
        return angle;
    }

    public void movement(GLTastatur keys, Cuboid cuboid){
        double angle = 0;
        int keysPressed = 0;
        boolean isMoving = false;
        double snowmanRotationAngle = this.giveRotation();
        snowmanMovementVector.setzeKomponenten(1,0,0);

        if(keys.istGedrueckt('z')){
            this.rotate(0,2,0);
        }
        if(keys.istGedrueckt('n') && this.giveY() <= 0){
            snowmanFallSpeed = -5*world.getGravity()*this.giveSize();
        }
        if(keys.istGedrueckt('i')){
            this.rotate(0,-2,0);
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
        snowmanMovementVector.multipliziere(world.getSpeed());
        this.moveBy(snowmanMovementVector.gibX(),0,snowmanMovementVector.gibZ());
        
        while(cuboid.collidesWithSnowman(this)){
            this.moveBy(-snowmanMovementVector.gibX()/10,0,-snowmanMovementVector.gibZ()/10);
        }

        this.gravity();
    }

    void gravity(){
        if(this.giveY() > 0 || snowmanFallSpeed < 0){
            this.moveBy(0, -snowmanFallSpeed, 0);
            snowmanFallSpeed += 0.1*world.getGravity()*this.giveSize();
        }
        else{
            snowmanFallSpeed = 0;
        }
        if(snowmanFallSpeed > world.getTerminalVelocity()*this.giveSize()){
            snowmanFallSpeed = world.getTerminalVelocity()*this.giveSize();
        }
        if(this.giveY() < 0){
            this.moveBy(0, -this.giveY(), 0);
        }
    }

    public double giveY(){
        double snowmanY = (bottomSphere.gibY()/size)-45;
        return snowmanY;
    }

    public double giveSize(){
        return size;
    }

    public void scaleBy(double scaleFactor){
        bottomSphere.setzePosition(bottomSphere.gibX(),bottomSphere.gibY() * scaleFactor,bottomSphere.gibZ());
        middleSphere.setzePosition(middleSphere.gibX(),middleSphere.gibY() * scaleFactor,middleSphere.gibZ());
        upperSphere.setzePosition(upperSphere.gibX(),upperSphere.gibY() * scaleFactor,upperSphere.gibZ());
        eye1.setzePosition(eye1.gibX() * scaleFactor,eye1.gibY() * scaleFactor,eye1.gibZ() * scaleFactor);
        eye2.setzePosition(eye2.gibX() * scaleFactor,eye2.gibY() * scaleFactor,eye2.gibZ() * scaleFactor);
        cylinder.setzePosition(cylinder.gibX(),cylinder.gibY() * scaleFactor,cylinder.gibZ());
        cylinderRim.setzePosition(cylinderRim.gibX(),cylinderRim.gibY() * scaleFactor,cylinderRim.gibZ());
        nose.setzePosition(nose.gibX() * scaleFactor,nose.gibY() * scaleFactor,nose.gibZ());
        bottomSphere.skaliere(scaleFactor);
        middleSphere.skaliere(scaleFactor);
        upperSphere.skaliere(scaleFactor);
        eye1.skaliere(scaleFactor);
        eye2.skaliere(scaleFactor);
        cylinder.skaliere(scaleFactor);
        cylinderRim.skaliere(scaleFactor);
        nose.skaliere(scaleFactor);
        this.size *= scaleFactor;
    }

}