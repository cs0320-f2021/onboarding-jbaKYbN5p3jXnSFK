package edu.brown.cs.student.main;

/**
 * Class for a star
 */
public class Star{
    int index;
    String starName;
    double xCoor;
    double yCoor;
    double zCoor;
    double distance;

    /**
     * Constructor for a new star
     * @param number the star index from the csv
     * @param name the star name
     * @param x coordinate
     * @param y coordinate
     * @param z coordinate
     */

    public Star(int number, String name, double x, double y, double z, double dist) {
        this.index = number;
        this.starName = name;
        this.xCoor = x;
        this.yCoor = y;
        this.zCoor = z;
        this.distance = dist;
    }

    public Double getDistance() {
        return this.distance;
    }


    /**
     * this method takes in coordinates and updates a comparison star with the distance away
     * @param xC given x coordinate
     * @param yC given y coordinate
     * @param zC given z coordinate
     */
    public void distanceGivenCoor(double xC, double yC, double zC) {
        double x;
        double y;
        double z;
        x = Math.pow((this.xCoor - xC), 2);
        y = Math.pow((this.yCoor - yC), 2);
        z = Math.pow(this.zCoor - zC, 2);
        this.distance = Math.sqrt(x + y + z);
    }

//given coordinates, we need to find the nearest star. this is much easier
    //csv make a new star object for each line and then call the distance method
    //given a star name, we need to find the nearest stars, only work with a dictionary. csv reader into dictionary


}
