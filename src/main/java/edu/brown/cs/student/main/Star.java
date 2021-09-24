package edu.brown.cs.student.main;

public final class Star {

    private final int starID;
    private final String properName;
    private final double x;
    private final double y;
    private final double z;

    /**
     * Regular constructor.
     * @param starID
     * @param properName
     * @param x
     * @param y
     * @param z
     */
    public Star(int starID, String properName, double x, double y, double z) {
        this.starID = starID;
        this.properName = properName;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }

    public final double getEuclidianDistance(double x, double y, double z) {
        return Math.sqrt(Math.pow(this.x - x, 2) + Math.pow(this.y - y, 2) + Math.pow(this.z - z, 2));
    }

    public final int getStarID() {
        return this.starID;
    }

    public final String getProperName() {
        return this.properName;
    }
}
