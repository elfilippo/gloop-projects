package com.gloop_project;
import GLOOP.*;

public class Snowman{

    GLKugel bottomSphere, middleSphere, upperSphere, eye1, eye2;
    GLZylinder cylinder, cylinderRim;
    GLKegel cone;
    GLVektor movementVector;
    double size;

    public Snowman(String libPath,double x,double y,double z,double size){
        this.size = size;

        bottomSphere = new GLKugel(x,(45 + y) * size,z,50 * size);
        middleSphere = new GLKugel(x,(125 + y) * size,z,38 * size);
        upperSphere = new GLKugel(x,(188 + y) * size,z,30 * size);

        eye1 = new GLKugel(x + (18 * size),(202 + y) * size,z + (-15 * size),4 * size);
        eye2 = new GLKugel(x + (18 * size),(202 + y) * size,z + (15 * size),4 * size);
        eye1.setzeTextur(libPath + "coal.png");
        eye2.setzeTextur(libPath + "coal.png");

        cylinder = new GLZylinder(x,(240 + y) * size,z,19 * size, 50 * size);
        cylinderRim = new GLZylinder(x,(215 + y) * size,z,30 * size, 2 * size);
        cylinder.drehe(90,0,0);
        cylinderRim.drehe(90,0,0);
        cylinder.setzeTextur(libPath + "cylinder.png");
        cylinderRim.setzeTextur(libPath + "cylinder.png");

        cone = new GLKegel(x + (40 * size),(190 + y) * size,z,4 * size,25 * size);
        cone.drehe(0,270,0);
        cone.setzeTextur(libPath + "carrot.png");
    }

    public double[] attributes(){
        double[] attributes = {bottomSphere.gibX(),bottomSphere.gibY()-50*size,bottomSphere.gibZ(),size};
        return attributes;
    }

    public void moveBy(double x,double y,double z){
        bottomSphere.verschiebe(x, y, z);
        middleSphere.verschiebe(x,y,z);
        upperSphere.verschiebe(x,y,z);
        cylinder.verschiebe(x,y,z);
        cylinderRim.verschiebe(x,y,z);
        cone.verschiebe(x,y,z);
        eye1.verschiebe(x,y,z);
        eye2.verschiebe(x,y,z);
    }

    public void moveTo(double x,double y,double z){
        bottomSphere.setzePosition(x, y, z);
        middleSphere.setzePosition(x,y,z);
        upperSphere.setzePosition(x, y, z);
        cylinder.setzePosition(x, y, z);
        cylinderRim.setzePosition(x,y,z);
        cone.setzePosition(x, y, z);
        eye1.setzePosition(x, y, z);
        eye2.setzePosition(x, y, z);
    }

    public void rotate(double x,double y,double z){
        double[] attributes = attributes();
        bottomSphere.drehe(x,y,z,attributes[0],attributes[1],attributes[2]);
        middleSphere.drehe(x,y,z,attributes[0],attributes[1],attributes[2]);
        upperSphere.drehe(x,y,z,attributes[0],attributes[1],attributes[2]);
        cylinder.drehe(x,y,z,attributes[0],attributes[1],attributes[2]);
        cylinderRim.drehe(x,y,z,attributes[0],attributes[1],attributes[2]);
        cone.drehe(x,y,z,attributes[0],attributes[1],attributes[2]);
        eye1.drehe(x,y,z,attributes[0],attributes[1],attributes[2]);
        eye2.drehe(x,y,z,attributes[0],attributes[1],attributes[2]);
        //rotation.drehe(x,y,z);
    }

    public GLVektor giveRotation(){
        //return rotation;
        return new GLVektor(0,0,0);
    }
}