import java.awt.*;
import java.util.ArrayDeque;
import java.util.Deque;

/**
 * Represent a planet in our solar system simulator
 */
public class Planet extends CelestialBodies {

    private boolean complete = false;
    private final Deque<double[]> trail = new ArrayDeque<>();

    /**
     * Constructs a planet in our solar system simulator
     *
     * @param x initial x-position in the planet panel
     * @param y initial y-position in the planet panel
     * @param vx initial horizontal x-velocity
     * @param vy initial vertical y-velocity
     * @param ax initial horizontal x-acceleration
     * @param ay initial y-acceleration
     * @param radius radius in pixels (visual for the user)
     * @param color display color as a hex string
     * @param mass mass of planet in kg
     */
    public Planet(double x,
                  double y,
                  double vx,
                  double vy,
                  double ax,
                  double ay,
                  double radius,
                  String color,
                  double mass) {
        super(x, y, vx, vy, ax, ay, mass, color, radius);
        // store starting position
        trail.add(new double[]{x, y});
    }

    /**
     * Marks whether the planet is fully set up
     *
     * @param complete true if the planet is complete, false if otherwise
     */
    public void setComplete(boolean complete) {
        this.complete = complete;
    }


    /**
     * Returns whether the planet is fully set up
     *
     * @return true if the planet is complete, false if otherwise
     */
    public boolean isComplete() {
        return complete;
    }

    /**
     * Returns the trail of the planet in positions {x,y}
     * @return queue of the recent positions of the planet
     */
    public Deque<double[]> getTrail() {
        return trail;
    }

    /**
     * Updates the planet position using the current velocity and adds the
     * current position to the trail
     *
     * @param dt change in time (change in frame per time)
     */
    @Override
    public void updatePosition(double dt) {
        positionX += velocityX * dt;
        positionY += velocityY * dt;

        trail.add(new double[]{positionX, positionY});
        if (trail.size() > 30){
            trail.remove();
        }
    }

    /**
     * Draws the planets as a filled in circle
     *
     * @param g2d graphics used for drawing
     */
    public void draw(Graphics2D g2d) {
        int diameter = (int) (radius * 2);
        g2d.setColor(Color.decode(color));
        g2d.fillOval((int) (positionX - radius), (int) (positionY - radius), diameter, diameter);
    }

    /**
     * Sets the mass of the planet in kg
     *
     * @param mass in kg
     */
    @Override
    public void setMass(double mass) {
        this.mass = mass;
    }

    /**
     * Sets the radius of the planet
     *
     * @param radius in pixels
     */
    @Override
    public void setRadius(double radius) {
        this.radius = radius;
    }

    /**
     * Gets the mass of the planet
     *
     * @return mass in the kg
     */
    @Override
    public double getMass() {
        return mass;
    }

    /**
     * Gets the color as a hex code of the planet
     *
     * @return String of hex code of color
     */
    @Override
    public String getColor() {
        return color;
    }

    /**
     * Gets the initial position x of the planet on Planet Panel
     *
     * @return Double of the x position of the planet
     */
    @Override
    public double getPositionX() {
        return positionX;
    }

    /**
     * Gets the initial position y of the planet on Planet Panel
     *
     * @return Double of the y position of the planet
     */
    @Override
    public double getPositionY() {
        return positionY;
    }

    /**
     * Gets the initial velocity x of the planet
     *
     * @return Double of the x velocity of the planet
     */
    @Override
    public double getVelocityX() {
        return velocityX;
    }

    /**
     * Gets the initial velocity y of the planet
     *
     * @return Double of the y velocity of the planet
     */
    @Override
    public double getVelocityY() {
        return velocityY;
    }

    /**
     * Gets the radius of the planet in pixels
     *
     * @return Double of the radius in pixels
     */
    @Override
    public double getRadius() {
        return radius;
    }

    /**
     * Sets the Acceleration of the planet in the x and y axis
     *
     * @param ax Double acceleration in the x-axis
     * @param ay Double acceleration in the y-axis
     */
    @Override
    public void setAcceleration(double ax, double ay) {
        accelerationX = ax;
        accelerationY = ay;
    }

    /**
     * Sets the velocity of the planet in the x and y axis
     *
     * @param vx Double velocity in the x-axis
     * @param vy Double velocity in the y-axis
     */
    @Override
    public void setVelocity(double vx, double vy) {
        velocityX = vx;
        velocityY = vy;
    }
}