package com.gloop_project;
import GLOOP.*;

public class Snowman/* extends GLObjekt*/{

    GLKugel bottomSphere, middleSphere, upperSphere;
    GLZylinder cylinder, cylinderRim;
    GLKegel cone;

    public Snowman(String libPath,double x,double y,double z,double size){
        bottomSphere = new GLKugel(x,(45 + y) * size,z,50 * size);
        middleSphere = new GLKugel(x,(125 + y) * size,z,38 * size);
        upperSphere = new GLKugel(x,(188 + y) * size,z,30 * size);

        cylinder = new GLZylinder(x,(240 + y) * size,z,19 * size, 50 * size);
        cylinderRim = new GLZylinder(x,(220 + y) * size,z,30 * size, 2 * size);
        cylinder.drehe(90,0,0);
        cylinderRim.drehe(90,0,0);
        cylinder.setzeTextur(libPath + "cylinder.png");
        cylinderRim.setzeTextur(libPath + "cylinder.png");

        cone = new GLKegel(x + (15 * size),(200 + y) * size,z,8 * size,25 * size);
        cone.drehe(0,0,270);
        cone.setzeTextur(libPath + "carrot.png");
    }

    public void zeichneObjekt(){}
}