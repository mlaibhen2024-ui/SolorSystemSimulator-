/*
Abstract class representing a celestial body in our solar system simulator
 */
public abstract class CelestialBodies {

    protected double positionX;

    protected double positionY;

    protected double velocityX;

    protected double velocityY;

    protected double accelerationX;

    protected double accelerationY;

    protected double mass;

    protected String color;

    protected double radius;

    /**
     * Constructs a celestial body in the solar system simulator
     *
     * @param positionX initial x-position in the planet panel
     * @param positionY initial y-position in planet panel
     * @param velocityX initial horizontal velocity
     * @param velocityY initial vertical velocity
     * @param accelerationX initial horizontal acceleration
     * @param accelerationY initial vertical acceleration
     * @param mass mass of the body in kg
     * @param color display color as a hex string
     * @param radius radius in pixels (visual for the user)
     */
    public CelestialBodies(double positionX,
                           double positionY,
                           double velocityX,
                           double velocityY,
                           double accelerationX,
                           double accelerationY,
                           double mass,
                           String color,
                           double radius) {
        this.positionX = positionX;
        this.positionY = positionY;
        this.velocityX = velocityX;
        this.velocityY = velocityY;
        this.accelerationX = accelerationX;
        this.accelerationY = accelerationY;
        this.mass = mass;
        this.color = color;
        this.radius = radius;
    }

    /**
     * Updates the position of the body based on delta time
     *
     * @param dt change in time (change in frame per time)
     */
    public abstract void updatePosition(double dt);

    /**
     * Sets the mass of the celestial body in kg
     *
     * @param mass in kg
     */
    public abstract void setMass(double mass);

    /**
     * Sets the radius of the celestial Body
     *
     * @param radius in pixels
     */
    public abstract void setRadius(double radius);

    /**
     * Gets the mass of the Celestial Body
     *
     * @return mass in the kg
     */
    public abstract double getMass();

    /**
     * Gets the color as a hex code of the Celestial Body
     *
     * @return String of hex code of color
     */
    public abstract String getColor();

    /**
     * Gets the initial position x of the Celestial Body on Planet Panel
     *
     * @return Double of the x position of the Celestial Body
     */
    public abstract double getPositionX();

    /**
     * Gets the initial position y of the Celestial Body on Planet Panel
     *
     * @return Double of the y position of the Celestial Body
     */
    public abstract double getPositionY();

    /**
     * Gets the initial velocity x of the Celestial Body
     *
     * @return Double of the x velocity of the Celestial Body
     */
    public abstract double getVelocityX();

    /**
     * Gets the initial velocity y of the Celestial Body
     *
     * @return Double of the y velocity of the Celestial Body
     */
    public abstract double getVelocityY();

    /**
     * Gets the radius of the Celestial Body in pixels
     *
     * @return Double of the radius in pixels
     */
    public abstract double getRadius();

    /**
     * Sets the Acceleration of the Celestial Body in the x and y axis
     *
     * @param ax Double acceleration in the x-axis
     * @param ay Double acceleration in the y-axis
     */
    public abstract void setAcceleration(double ax, double ay);

    /**
     * Sets the velocity of the Celestial Body in the x and y axis
     *
     * @param vx Double velocity in the x-axis
     * @param vy Double velocity in the y-axis
     */
    public abstract void setVelocity(double vx, double vy);

    /**
     * Updates Velocity at delta time
     *
     * @param dt Double delta time
     */
    public void updateVelocity(double dt) {
        velocityX += accelerationX * dt;
        velocityY += accelerationY * dt;
    }

    /**
     * Adds accelerations in each the x and y axis
     *
     * @param ax acceleration in the x axis
     * @param ay accerlation in the y axis
     */
    public void addAcceleration(double ax, double ay) {
        accelerationX += ax;
        accelerationY += ay;
    }

    /**
     * Resets acceleration to zero so each frame can be recalculated correctly
     */
    public void resetAcceleration() {
        accelerationX = 0;
        accelerationY = 0;
    }
}