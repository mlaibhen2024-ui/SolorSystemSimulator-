import java.util.List;

/*
    For the PhysicsEngine class, we used assistance from the help of these sources:
    https://www.youtube.com/watch?v=WTLPmUHTPqo&t=2217s
    https://github.com/techwithtim/Python-Planet-Simulation/blob/main/tutorial.py
    https://fiftyexamples.readthedocs.io/en/latest/gravity.html
*/


public class PhysicsEngine {

    public static final double G = 6.67428e-11;

    /**
     *  update the physical state of all celestial bodies in the simulation for a single time step
     * using a leepfrog intergration method (leap-frog, leap-frog)
     * This method perfoms the following steps:
     * <ol>
     *     <li>Resets acceleration for all bodies</li>
     *     <li>computes gravitational forces base on current position</li>
     *     <li>update velocity for the first first-hald step</li>
     *     <li>update position for the full step</li>
     *     <lil>reset accelaration again</lil>
     *     <lil>Recompute gravitational force at new position </lil>
     *     <lil>update velocity at the second half step </lil>
     * </ol>
     * @Param bodies the list of celestial bodies to update; if null or empty
     * @Param dt the tome step duration for the simulation
     *
    */

    public static void update(List<CelestialBodies> bodies, double dt) {
        if (bodies == null || bodies.isEmpty()){
            return;
        }

        // Reset acceleration
        for (CelestialBodies body : bodies) {
            body.resetAcceleration();
        }

        // Compute gravity at current positions
        for (int i = 0; i < bodies.size(); i++) {
            for (int j = i + 1; j < bodies.size(); j++) {
                applyGravity(bodies.get(i), bodies.get(j));
            }
        }

        // First half-step velocity update
        for (CelestialBodies body : bodies) {
            if (body instanceof Star) {
                continue; // keep star fixed in center
            }
            body.updateVelocity(dt * 0.5);
        }

        // Full position update
        for (CelestialBodies body : bodies) {
            if (body instanceof Star) {
                continue;
            }
            body.updatePosition(dt);
        }

        // Reset acceleration
        for (CelestialBodies body : bodies) {
            body.resetAcceleration();
        }

        // Recompute gravity at new positions
        for (int i = 0; i < bodies.size(); i++) {
            for (int j = i + 1; j < bodies.size(); j++) {
                applyGravity(bodies.get(i), bodies.get(j));
            }
        }

        // Second half-step velocity update
        for (CelestialBodies body : bodies) {
            if (body instanceof Star) {
                continue;
            }
            body.updateVelocity(dt * 0.5);
        }
    }

    /**
     * This method apply Gravity to two celestialbodies and update their accelaration
     *
     * use newton law of unviversal gravitation f = g(m1*m2)/r^2, determine the force componets in the x and y
     * directions and update the accelaration of both bodies base on their mass.
     * A minumim distance check is implimented to avoid singularity
     *
     * @Param CelestialBodies1 the first celestial bodies
     * @Param CelestialBodies2 the second celestial bodies
     * @Param G the Gravitational constant
     */

    private static void applyGravity(CelestialBodies b1, CelestialBodies b2) {
        double dx = b2.getPositionX() - b1.getPositionX();
        double dy = b2.getPositionY() - b1.getPositionY();

        double distanceSquared = dx * dx + dy * dy;
        double distance = Math.sqrt(distanceSquared);

        if (distance < 1e3) {
            return;
        }

        double force = G * b1.getMass() * b2.getMass() / distanceSquared;

        double fx = force * dx / distance;
        double fy = force * dy / distance;

        b1.addAcceleration(fx / b1.getMass(), fy / b1.getMass());
        b2.addAcceleration(-fx / b2.getMass(), -fy / b2.getMass());
    }
}