import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import javax.sound.sampled.*;
import java.io.File;

/**
 * Main drawing and simulation panel for the solar system simulator
 *
 * This class handles UI, drawing, physics updates, collision
 * handling, animation effects, and audio as well as coversion between
 * pixels to simulation and simulation to pixels
 */
public class PlanetPanel extends JPanel {

    public static final double AU = 149.6e6 * 1000;
    public static final double SCALE = 250.0 / AU;
    public static final double TIMESTEP = 3600 * 24;

    private static final double COLLISION_FRAGMENT_MASS_FRACTION = 0.35;
    private static final double COLLISION_FRAGMENT_RADIUS_FRACTION = 0.60;
    private static final double COLLISION_KICK_FACTOR = 0.06;
    private static final double COLLISION_PUSH_SPEED_FACTOR = 0.45;
    private static final double COLLISION_VISUAL_DT = 2.0e6;
    private static final double MIN_COLLISION_DISTANCE_SIM = 1e3;
    private static final int COLLISION_ANIMATION_FRAMES = 28;

    private Star star;
    private Planet currentPlanet;

    private final List<Planet> planets = new ArrayList<>();
    private final Stack<Planet> planetStack = new Stack<>();
    private final List<CollisionEffect> collisionEffects = new ArrayList<>();

    private boolean starPlaced = false;
    private boolean simulationRunning = false;

    private Timer timer;
    private BufferedImage trailImage;

    /**
     * Constructs the main simulation panel
     */
    public PlanetPanel() {
        setBackground(Color.BLACK);

        timer = new Timer(16, new PhysicsTimerListener(this));
        addMouseListener(new PlanetPanelMouseListener(this));
    }

    /**
     * Handles a mouse click on the simulation panel
     *
     * @param mouseX x-coordinate of the click in screen space
     * @param mouseY y-coordinate of the click in screen space
     */
    private void handleClick(int mouseX, int mouseY) {
        if (!starPlaced) {
            star = new Star(0, 0, 0, 0, 0, 0, 1.98892e30, "#FFD700", 30);
            starPlaced = true;
            playLaserSound();
            repaint();
            return;
        }

        if (!starComplete()) {
            JOptionPane.showMessageDialog(this, "Set star mass and radius first!!");
            return;
        }

        if (currentPlanet != null && !currentPlanet.isComplete()) {
            JOptionPane.showMessageDialog(this, "Finish configuring the current planet first!!");
            return;
        }

        double simX = screenToSimX(mouseX);
        double simY = screenToSimY(mouseY);

        String color = String.format("#%06X", (int) (Math.random() * 0xFFFFFF));

        currentPlanet = new Planet(simX, simY, 0, 0, 0, 0, 8, color, 5.9742e24);
        planets.add(currentPlanet);
        planetStack.push(currentPlanet);

        playLaserSound();
        repaint();
    }

    /**
     * Starts the simulation if the required setup has been completed
     */
    public void startSimulation() {
        if (star == null || !starComplete()) {
            JOptionPane.showMessageDialog(this, "Place the star and apply its settings first!!");
            return;
        }

        boolean hasCompletePlanet = false;

        for (Planet p : planets) {
            if (p.isComplete()) {
                hasCompletePlanet = true;
                break;
            }
        }

        if (!hasCompletePlanet) {
            JOptionPane.showMessageDialog(this, "Add and configure at least one planet first!!");
            return;
        }

        simulationRunning = true;
        timer.start();
    }

    /**
     * Pauses the simulation and stops the timer
     */
    public void pauseSimulation() {
        simulationRunning = false;
        timer.stop();
    }

    /**
     * Resets the entire simulation state
     */
    public void resetSimulation() {
        timer.stop();
        simulationRunning = false;

        planets.clear();
        planetStack.clear();
        collisionEffects.clear();

        star = null;
        currentPlanet = null;
        starPlaced = false;
        trailImage = null;

        repaint();
    }

    /**
     * Removes the most recently added planet if undo is allowed
     */
    public void undoLastPlanet() {
        if (simulationRunning) {
            JOptionPane.showMessageDialog(this, "Cannot undo while the simulation is running!!");
            return;
        }

        if (!planetStack.isEmpty()) {
            Planet removed = planetStack.pop();
            planets.remove(removed);

            if (removed == currentPlanet) {
                currentPlanet = null;
            }

            rebuildTrailImage();
            repaint();
        }
    }

    /**
     * Sets the star's mass if allowed
     *
     * @param mass new star mass in kg
     */
    public void setStarMass(double mass) {
        if (star != null && !simulationRunning) {
            star.setMass(mass);
        }
    }

    /**
     * Sets the star's visual radius if allowed
     *
     * @param radiusPx new radius in pixels
     */
    public void setStarRadius(double radiusPx) {
        if (star != null && !simulationRunning) {
            star.setRadius(radiusPx);
        }
    }

    /**
     * Sets the current planet's mass if allowed
     *
     * @param mass new planet mass in kg
     */
    public void setPlanetMass(double mass) {
        if (currentPlanet != null && !simulationRunning) {
            currentPlanet.setMass(mass);
        }
    }

    /**
     * Sets the current planet's radius if editing is allowed
     *
     * @param radiusPx new radius in pixels
     */
    public void setPlanetRadius(double radiusPx) {
        if (currentPlanet != null && !simulationRunning) {
            currentPlanet.setRadius(radiusPx);
        }
    }

    /**
     * Sets the orbital velocity of the current planet using a speed multiplier
     *
     * @param multiplier multiplier applied to orbital speed
     */
    public void setPlanetVelocity(double multiplier) {
        if (!starPlaced || currentPlanet == null || simulationRunning) {
            return;
        }

        double dx = star.getPositionX() - currentPlanet.getPositionX();
        double dy = star.getPositionY() - currentPlanet.getPositionY();
        double r = Math.sqrt(dx * dx + dy * dy);

        if (r < getMinimumStarPlanetDistance()) {
            JOptionPane.showMessageDialog(this, "Planet is too close to the star!!");
            return;
        }

        double orbitalVelocity = Math.sqrt(PhysicsEngine.G * star.getMass() / r);
        double speed = orbitalVelocity * multiplier;

        double vx = -dy / r * speed;
        double vy = dx / r * speed;

        currentPlanet.setVelocity(vx, vy);
        currentPlanet.setComplete(true);
    }

    /**
     * Computes the minimum safe distance between the star and current planet
     *
     * @return minimum separation in simulation units
     */
    private double getMinimumStarPlanetDistance() {
        return (star.getRadius() / SCALE) + (currentPlanet.getRadius() / SCALE) + (20.0 / SCALE);
    }

    /**
     * Returns whether the star has both mass and radius configured
     *
     * @return true if star is complete, false if otherwise
     */
    public boolean starComplete() {
        return star != null && star.isComplete();
    }

    /**
     * Returns whether the simulation is currently running
     *
     * @return true if simulation is running, false if otherwise
     */
    public boolean isSimulationRunning() {
        return simulationRunning;
    }

    /**
     * Returns whether there is a planet currently waiting for mass, radius, or velocity
     *
     * @return true if it is complete, false if otherwise
     */
    public boolean hasPendingPlanet() {
        return currentPlanet != null && !currentPlanet.isComplete();
    }

    /**
     * Returns whether a star has been placed
     *
     * @return true if a star has been placed, false if otherwise
     */
    public boolean hasPlacedStar() {
        return starPlaced;
    }

    /**
     * Updates simulation physics, collisions, and visuals for each body on
     * planet panel
     */
    private void updatePhysics() {
        if (!simulationRunning) {
            return;
        }

        List<CelestialBodies> bodies = new ArrayList<>();

        if (star != null) {
            bodies.add(star);
        }

        for (Planet p : planets) {
            if (p.isComplete()) {
                bodies.add(p);
            }
        }

        PhysicsEngine.update(bodies, TIMESTEP);
        handlePlanetCollisions();
        updateCollisionEffects();
        repaint();
    }

    /**
     *
     * Handles collision between completed planets
     * iterates through the planet list to check if pairs of planets are colliding.
     *
     *
     * <li>Check that both planets are finish forming </li>
     * <li>uses a temporary list from main list and the current tracking pointer</li>
     * <li>Handles removal from main list and the current tracking pointers</li>
     * <li>updates visual trails after collision </li>
     *
     */

    private void handlePlanetCollisions() {
        if (planets.size() < 2) {
            return;
        }

        List<Planet> toRemove = new ArrayList<>();

        for (int i = 0; i < planets.size(); i++) {
            Planet p1 = planets.get(i);

            if (!p1.isComplete() || toRemove.contains(p1)) {
                continue;
            }

            for (int j = i + 1; j < planets.size(); j++) {
                Planet p2 = planets.get(j);

                if (!p2.isComplete() || toRemove.contains(p2)) {
                    continue;
                }

                if (areColliding(p1, p2)) {
                    CollisionEffect effect = createCollisionEffect(p1, p2);

                    if (effect != null) {
                        collisionEffects.add(effect);
                    }

                    toRemove.add(p1);
                    toRemove.add(p2);
                    break;
                }
            }
        }

        if (!toRemove.isEmpty()) {
            planets.removeAll(toRemove);
            planetStack.removeAll(toRemove);

            if (currentPlanet != null && toRemove.contains(currentPlanet)) {
                currentPlanet = null;
            }

            rebuildTrailImage();
        }
    }

    /**
     *This method update the collision effects based on the simulation time step
     * iterate through collision efect, updates their states,
     * and if an effect is finished it generates a new link planets fragment
     * adds it to planet and planetpanel and removed the effect from the active list
     *
     */
    private void updateCollisionEffects() {
        if (collisionEffects.isEmpty()) {
            return;
        }

        List<CollisionEffect> finished = new ArrayList<>();

        for (CollisionEffect effect : collisionEffects) {
            effect.update(COLLISION_VISUAL_DT);

            if (effect.isFinished()) {
                Planet fragment = effect.createResultPlanet();
                planets.add(fragment);
                planetStack.add(fragment);
                finished.add(effect);
            }
        }
        collisionEffects.removeAll(finished);
    }


    /**
     * check that the planets are colliding
     *
     * @param p1 the first planet
     * @param p2 the second planet
     * @return true if the distance between centers is less than or equal to the sum of radii;
     * false otherwise.
     */
    private boolean areColliding(Planet p1, Planet p2) {
        double dx = p2.getPositionX() - p1.getPositionX();
        double dy = p2.getPositionY() - p1.getPositionY();
        double distance = Math.sqrt(dx * dx + dy * dy);

        double collisionDistance = (p1.getRadius() / SCALE) + (p2.getRadius() / SCALE);
        return distance <= collisionDistance;
    }

    /**
     *
     * This method create the physical effects of a collision between two planets
     * This method calculate the new combined mass, position, and velocity
     * generates fragments, calculate kick forces if near a star and determines separation velocities for
     *  the collision animation
     *
     * @param p1 the first planet involved in  the collision
     * @param p2 the second planet involved in the collision
     * @return object containing parameters for rendering the collision animation or null if the t
     * total mass is zero or less 
     */

    private CollisionEffect createCollisionEffect(Planet p1, Planet p2) {
        double m1 = p1.getMass();
        double m2 = p2.getMass();
        double totalMass = m1 + m2;

        if (totalMass <= 0) {
            return null;
        }

        double newX = (m1 * p1.getPositionX() + m2 * p2.getPositionX()) / totalMass;
        double newY = (m1 * p1.getPositionY() + m2 * p2.getPositionY()) / totalMass;

        double newVx = (m1 * p1.getVelocityX() + m2 * p2.getVelocityX()) / totalMass;
        double newVy = (m1 * p1.getVelocityY() + m2 * p2.getVelocityY()) / totalMass;

        double fragmentMass = totalMass * COLLISION_FRAGMENT_MASS_FRACTION;
        double fragmentRadiusPx = Math.max(3.0, Math.max(p1.getRadius(), p2.getRadius()) * COLLISION_FRAGMENT_RADIUS_FRACTION);

        if (star != null) {
            double rx = newX - star.getPositionX();
            double ry = newY - star.getPositionY();
            double r = Math.sqrt(rx * rx + ry * ry);

            if (r > MIN_COLLISION_DISTANCE_SIM) {
                double kickMagnitude = Math.sqrt(newVx * newVx + newVy * newVy) * COLLISION_KICK_FACTOR;
                double kickX = -ry / r * kickMagnitude;
                double kickY = rx / r * kickMagnitude;
                newVx += kickX;
                newVy += kickY;
            }
        }

        String collisionColor = randomCollisionColor();

        double dx = p2.getPositionX() - p1.getPositionX();
        double dy = p2.getPositionY() - p1.getPositionY();
        double d = Math.sqrt(dx * dx + dy * dy);

        if (d < 1e-9) {
            dx = 1;
            dy = 1;
            d = Math.sqrt(2);
        }

        double nx = dx / d;
        double ny = dy / d;

        double px = -ny;
        double py = nx;

        double relativeSpeed = Math.sqrt(Math.pow(p1.getVelocityX() - p2.getVelocityX(), 2) + Math.pow(p1.getVelocityY() - p2.getVelocityY(), 2));

        double pushSpeed = Math.max(1500, relativeSpeed * COLLISION_PUSH_SPEED_FACTOR);

        double dir1x = (-nx + 0.7 * px);
        double dir1y = (-ny + 0.7 * py);
        double dir2x = (nx - 0.7 * px);
        double dir2y = (ny - 0.7 * py);

        double len1 = Math.sqrt(dir1x * dir1x + dir1y * dir1y);
        double len2 = Math.sqrt(dir2x * dir2x + dir2y * dir2y);

        dir1x /= len1;
        dir1y /= len1;
        dir2x /= len2;
        dir2y /= len2;

        double pushVx1 = dir1x * pushSpeed;
        double pushVy1 = dir1y * pushSpeed;
        double pushVx2 = dir2x * pushSpeed;
        double pushVy2 = dir2y * pushSpeed;

        return new CollisionEffect(
                p1,
                p2,
                p1.getPositionX(),
                p1.getPositionY(),
                p2.getPositionX(),
                p2.getPositionY(),
                pushVx1,
                pushVy1,
                pushVx2,
                pushVy2,
                newX,
                newY,
                newVx,
                newVy,
                fragmentMass,
                fragmentRadiusPx,
                collisionColor,
                COLLISION_ANIMATION_FRAMES
        );
    }

    /**
     * This method randomized colors for the new planets that are formed after the collision
     *
     * @return collision colors
     */

    private String randomCollisionColor() {
        String[] collisionColors = {"#FF00FF", "#00FFFF", "#39FF14", "#FF4500", "#FFD700", "#FFFFFF"};

        int index = (int) (Math.random() * collisionColors.length);
        return collisionColors[index];
    }

    /**
     * Gets the simulation coordinates on x-axis and translates them into screen coordinates
     * @param simX simulation coordinates on x-axis
     * @return screen x-coordinates
     */
    private double simToScreenX(double simX) {
        return simX * SCALE + getWidth() / 2.0;
    }

    /**
     * Gets the simulation coordinated on the y-axis and translates them into screen coordinates
     * @param simY simulation coordinates on the y-axis
     * @return screen y-coordinates
     */
    private double simToScreenY(double simY) {
        return simY * SCALE + getHeight() / 2.0;
    }

    /**
     * Gets the screen coordinates on the x-axis and translates them into simulation coordinates
     * @param screenX screen coordinates on the x-axis
     * @return simulation x-coordinates
     */
    private double screenToSimX(double screenX) {
        return (screenX - getWidth() / 2.0) / SCALE;
    }

    /**
     * Gets the screen coordinates on the y-axis and translates them into simulation coordinates
     * @param screenY screen coordinates on the y-axis
     * @return simulation y-coordinates
     */
    private double screenToSimY(double screenY) {
        return (screenY - getHeight() / 2.0) / SCALE;
    }

    /**
     * Rebuilds the trails by setting it to null
     */
    private void rebuildTrailImage() {
        trailImage = null;
    }

    /**
     * Paint component to draw the star, planet, and the trail of each planet
     * @param g the <code>Graphics</code> object to protect
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (trailImage == null || trailImage.getWidth() != getWidth() || trailImage.getHeight() != getHeight()) {
            trailImage = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
        }

        Graphics2D g2 = (Graphics2D) g;
        Graphics2D trailGraphics = trailImage.createGraphics();
        trailGraphics.setColor(Color.WHITE);

        trailGraphics.setComposite(AlphaComposite.Clear);
        trailGraphics.fillRect(0, 0, trailImage.getWidth(), trailImage.getHeight());
        trailGraphics.setComposite(AlphaComposite.SrcOver);
        trailGraphics.setColor(new Color(255, 255, 255, 160));

        for (Planet p : planets) {
            double[] prev = null;

            for (double[] pt : p.getTrail()) {
                if (prev != null) {
                    int x1 = (int) simToScreenX(prev[0]);
                    int y1 = (int) simToScreenY(prev[1]);
                    int x2 = (int) simToScreenX(pt[0]);
                    int y2 = (int) simToScreenY(pt[1]);
                    trailGraphics.drawLine(x1, y1, x2, y2);
                }
                prev = pt;
            }
        }

        trailGraphics.dispose();
        g2.drawImage(trailImage, 0, 0, null);

        if (star != null) {
            int d = (int) (star.getRadius() * 2);
            int x = (int) (simToScreenX(star.getPositionX()) - star.getRadius());
            int y = (int) (simToScreenY(star.getPositionY()) - star.getRadius());

            g2.setColor(Color.decode(star.getColor()));
            g2.fillOval(x, y, d, d);
        }

        for (Planet p : planets) {
            int d = (int) (p.getRadius() * 2);
            int x = (int) (simToScreenX(p.getPositionX()) - p.getRadius());
            int y = (int) (simToScreenY(p.getPositionY()) - p.getRadius());

            g2.setColor(Color.decode(p.getColor()));
            g2.fillOval(x, y, d, d);
        }

        for (CollisionEffect effect : collisionEffects) {
            Planet p1 = effect.getPlanet1();
            Planet p2 = effect.getPlanet2();

            int d1 = (int) (p1.getRadius() * 2);
            int x1 = (int) (simToScreenX(effect.getX1()) - p1.getRadius());
            int y1 = (int) (simToScreenY(effect.getY1()) - p1.getRadius());

            g2.setColor(Color.decode(p1.getColor()));
            g2.fillOval(x1, y1, d1, d1);

            int d2 = (int) (p2.getRadius() * 2);
            int x2 = (int) (simToScreenX(effect.getX2()) - p2.getRadius());
            int y2 = (int) (simToScreenY(effect.getY2()) - p2.getRadius());

            g2.setColor(Color.decode(p2.getColor()));
            g2.fillOval(x2, y2, d2, d2);
        }
    }

    /**
     * Laser sound to be played when method is called
     */
    private void playLaserSound() {
        try {
            File file = new File("Assets/laserGun.wav");

            if (!file.exists()) {
                return;
            }

            AudioInputStream stream = AudioSystem.getAudioInputStream(file);
            Clip clip = AudioSystem.getClip();
            clip.open(stream);
            clip.start();
        } catch (Exception ignored) {
        }
    }

    /**
     * Listener that updates the physics of the planet panel
     */
    public class PhysicsTimerListener implements ActionListener {
        private PlanetPanel panel;

        public PhysicsTimerListener(PlanetPanel panel) {
            this.panel = panel;
        }

        /**
         * Handles updating the physics on the panel
         *
         * @param e action event
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            panel.updatePhysics();
        }
    }

    /**
     * Listener that handles adding planets
     */
    public class PlanetPanelMouseListener extends MouseAdapter {
        private PlanetPanel panel;

        public PlanetPanelMouseListener(PlanetPanel panel) {
            this.panel = panel;
        }

        /**
         * Handles the mouse click to add planets
         *
         * @param e action event
         */
        @Override
        public void mouseClicked(MouseEvent e) {
            if (panel.simulationRunning) {
                JOptionPane.showMessageDialog(panel, "Pause the simulation before adding more planets!!");
                return;
            }

            panel.handleClick(e.getX(), e.getY());
        }
    }
}