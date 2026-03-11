/**
 * Represent a star in our solar system simulator
 */

public class Star extends CelestialBodies {

    private boolean massSet = false;
    private boolean radiusSet = false;

    /**
     * Constructs a star in our solar system simulator
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
    public Star(
            double x,
            double y,
            double vx,
            double vy,
            double ax,
            double ay,
            double mass,
            String color,
            double radius
    ) {
        super(x, y, 0, 0, 0, 0, mass, color, radius);
    }

    /**
     * Sets the mass of the star in kg
     *
     * @param mass in kg
     */
    @Override
    public void setMass(double mass) {
        this.mass = mass;
        massSet = true;
    }

    /**
     * Sets the radius of the star
     *
     * @param radius in pixels
     */
    @Override
    public void setRadius(double radius) {
        this.radius = radius;
        radiusSet = true;
    }

    /**
     * Returns whether the star is fully set up
     *
     * @return true if the star is complete, false if otherwise
     */
    public boolean isComplete() {
        return massSet && radiusSet;
    }

    /**
     * Star position does not change so this method is not used
     *
     * @param dt change in time (change in frame per time)
     */
    @Override
    public void updatePosition(double dt) {
    }

    /**
     * Gets the mass of the star
     *
     * @return mass in the kg
     */
    @Override
    public double getMass() {
        return mass;
    }

    /**
     * Gets the color as a hex code of the star
     *
     * @return String of hex code of color
     */
    @Override
    public String getColor() {
        return color;
    }

    /**
     * Gets the initial position x of the star on Planet Panel
     *
     * @return Double of the x position of the star
     */
    @Override
    public double getPositionX() {
        return positionX;
    }

    /**
     * Gets the initial position y of the star on Planet Panel
     *
     * @return Double of the y position of the star
     */
    @Override
    public double getPositionY() {
        return positionY;
    }

    /**
     * Gets the initial velocity x of the star
     *
     * @return Double of the x velocity of the star
     */
    @Override
    public double getVelocityX() {
        return velocityX;
    }

    /**
     * Gets the initial velocity y of the star
     *
     * @return Double of the y velocity of the star
     */
    @Override
    public double getVelocityY() {
        return velocityY;
    }

    /**
     * Gets the radius of the star in pixels
     *
     * @return Double of the radius in pixels
     */
    @Override
    public double getRadius() {
        return radius;
    }

    /**
     * Sets the Acceleration of the star in the x and y axis
     *
     * @param ax Double acceleration in the x-axis
     * @param ay Double acceleration in the y-axis
     */
    @Override
    public void setAcceleration(double ax, double ay) {
        this.accelerationX = ax;
        this.accelerationY = ay;
    }

    /**
     * Sets the velocity of the star in the x and y axis
     *
     * @param vx Double velocity in the x-axis
     * @param vy Double velocity in the y-axis
     */
    @Override
    public void setVelocity(double vx, double vy) {
        this.velocityX = vx;
        this.velocityY = vy;
    }
}
