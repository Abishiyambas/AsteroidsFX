package dk.sdu.cbse.common.data;

public class Entity {
    // A simple game object (player, enemy, bullet, asteroid).
    private String id;
    // Who made this (used for bullets and friendly fire).
    private String ownerId = "";
    // Shape points for drawing (x1,y1,x2,y2,...).
    private double[] polygonCoordinates;
    // Position.
   private double x;
   private double y;
    // Movement speed (not always used).
    private double dx;
    private double dy;
    // Direction the entity is facing (in degrees).
    private double rotation;
    // Size for collisions.
    private double radius;
    // How many hits it can take before it is removed.
    private int health = 1;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public void setPolygonCoordinates(double... coordinates) {
        this.polygonCoordinates = coordinates;
    }

    public double[] getPolygonCoordinates() {
        return polygonCoordinates;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getDx() {
        return dx;
    }

    public void setDx(double dx) {
        this.dx = dx;
    }

    public double getDy() {
        return dy;
    }

    public void setDy(double dy) {
        this.dy = dy;
    }

    public double getRotation() {
        return rotation;
    }

    public void setRotation(double rotation) {
        this.rotation = rotation;
    }

    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

}
