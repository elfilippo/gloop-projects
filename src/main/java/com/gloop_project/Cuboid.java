package com.gloop_project;

public class Cuboid extends GLOOP.GLQuader{
    
    double width, height, length;
    double[] cuboidCoordinates;
    record CuboidBound(double xMin,double xMax,double yMin,double yMax,double zMin,double zMax){}
    CuboidBound cuboidBound;

    public Cuboid(double x, double y, double z, double width, double height, double length){
        super(x, y, z, width, height, length);
        this.cuboidCoordinates = new double[]{x,y,z};

        CuboidBound cuboidBound = new CuboidBound(
            cuboidCoordinates[0]-width/2,cuboidCoordinates[0]+width/2,
            cuboidCoordinates[1]-height/2,cuboidCoordinates[1]+height/2,
            cuboidCoordinates[2]-length/2,cuboidCoordinates[2]+length/2
        );

        this.width = width;
        this.height = height;
        this.length = length;
        this.cuboidBound = cuboidBound;
    }

    public boolean collidesWithSnowman(Snowman Snowman){
        Snowman.Attributes attrs = Snowman.getAttributes();
        double[] sphereCenter = new double[]{attrs.x(),attrs.y() + 45*attrs.size(),attrs.z()};
        double sphereRadius = 50 * attrs.size();
        double closestX = Math.max(cuboidBound.xMin(),Math.min(sphereCenter[0], cuboidBound.xMax()));
        double closestY = Math.max(cuboidBound.yMin(), Math.min(sphereCenter[1], cuboidBound.yMax()));
        double closestZ = Math.max(cuboidBound.zMin(), Math.min(sphereCenter[2], cuboidBound.zMax()));

        double dx = closestX - sphereCenter[0];
        double dy = closestY - sphereCenter[1];
        double dz = closestZ - sphereCenter[2];

        double distanceSquared = dx*dx + dy*dy + dz*dz;

        return distanceSquared <= sphereRadius*sphereRadius;
    }

    public CuboidBound getCuboidBound(){
        return this.cuboidBound;
    }

}
