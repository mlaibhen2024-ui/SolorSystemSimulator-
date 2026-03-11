public class CollisionEffect {

    private final Planet planet1;
    private final Planet planet2;

    private final double startX1;
    private final double startY1;
    private final double startX2;
    private final double startY2;

    private final double driftVx1;
    private final double driftVy1;
    private final double driftVx2;
    private final double driftVy2;

    private final double newPlanetX;
    private final double newPlanetY;
    private final double newPlanetVx;
    private final double newPlanetVy;
    private final double newPlanetMass;
    private final double newPlanetRadius;
    private final String newPlanetColor;

    private final int totalFrames;
    private int framesLeft;

    private double x1;
    private double y1;
    private double x2;
    private double y2;



    public CollisionEffect(
            Planet planet1,
            Planet planet2,
            double x1,
            double y1,
            double x2,
            double y2,
            double driftVx1,
            double driftVy1,
            double driftVx2,
            double driftVy2,
            double newPlanetX,
            double newPlanetY,
            double newPlanetVx,
            double newPlanetVy,
            double newPlanetMass,
            double newPlanetRadius,
            String newPlanetColor,
            int framesLeft
    ) {
        this.planet1 = planet1;
        this.planet2 = planet2;

        this.startX1 = x1;
        this.startY1 = y1;
        this.startX2 = x2;
        this.startY2 = y2;

        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;

        this.driftVx1 = driftVx1;
        this.driftVy1 = driftVy1;
        this.driftVx2 = driftVx2;
        this.driftVy2 = driftVy2;

        this.newPlanetX = newPlanetX;
        this.newPlanetY = newPlanetY;
        this.newPlanetVx = newPlanetVx;
        this.newPlanetVy = newPlanetVy;
        this.newPlanetMass = newPlanetMass;
        this.newPlanetRadius = newPlanetRadius;
        this.newPlanetColor = newPlanetColor;

        this.framesLeft = framesLeft;
        this.totalFrames = framesLeft;
    }

    /**
     * Updates the planet's position for a single frame based on a time step and
     * easing function.
     *
     * <p>Uses cubic interjection to calculate
     * movement, applying driftVx and driftVy relative to the
     * remaining frames.
     *
     * @param dt The time step (delta time) for this frame.
     */
    public void update(double dt) {
        double progress = 1.0 - ((double) framesLeft / totalFrames);
        double eased = 1.0 - Math.pow(1.0 - progress, 3);

        x1 = startX1 + driftVx1 * dt * eased;
        y1 = startY1 + driftVy1 * dt * eased;
        x2 = startX2 + driftVx2 * dt * eased;
        y2 = startY2 + driftVy2 * dt * eased;

        framesLeft--;
    }

    /**
     * Checks if the planet's actions, animation, or simulation is finished.
     * A planet is considered finished if the remaining frames count is zero or less.
     *
     * @return true if no frames are left, false otherwise.
     */
    public boolean isFinished() {
        return framesLeft <= 0;
    }

    /**
     * Create a new planet from collided planets at a new location
     * have it own radius, color and mass
     * @return fragment
     */
    public Planet createResultPlanet() {
        Planet fragment = new Planet(
                newPlanetX,
                newPlanetY,
                newPlanetVx,
                newPlanetVy,
                0,
                0,
                newPlanetRadius,
                newPlanetColor,
                newPlanetMass
        );

        fragment.setComplete(true);
        fragment.getTrail().clear();
        fragment.getTrail().add(new double[]{fragment.getPositionX(), fragment.getPositionY()});

        return fragment;
    }

    /**
     * Get the first planet
     * @return planet1
     */
    public Planet getPlanet1() {
        return planet1;
    }

    /**
     * Get the second planet
     * @return planet2
     */
    public Planet getPlanet2() {
        return planet2;
    }

    /**
     * Get the first  position in the x-axis
     * @return return x1
     */
    public double getX1() {
        return x1;
    }

    /**
     * Get the first position in the y-axis
     * @return return y1
     */
    public double getY1() {
        return y1;
    }

    /**
     * Get the second position in the x-axis
     * @return return x2
     */
    public double getX2() {
        return x2;
    }

    /**
     * Get the second position in the Y-axis
     * @return y2
     */
    public double getY2() {
        return y2;
    }
}