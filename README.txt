To run the solar system simulator make sure you have the root director downloaded [SolarSystemSimulator] and
 all files downloaded in the src, these include:
- SolarSystem
- MainFrame
- PlanetPanel
- SimulationPanel
- ControlPanel
- PhysicsEngine
- CollisionEffect
- CelestialBodies
- Star
- Planet
And in Tests:
- planetPanelTest
In the root directory, SolarSystemSimulator, there should be an Assets directory with:
- laserGun.wav
Once confirmed you have all of these files, make sure you have an IDE that you can run this simulator on.
We recommended using IntelliJ to run this program as this was the program we used to build this project.
In order to preserve the functionality of the project, we recommend not touching any code in order to not
break anything.
To run the simulation, run it from SolarSystem.java, let it build and the program should run and a window
should pop up.
To use the simulation, click anywhere on the screen to place the star. The star will always be centered in the center
of the screen as this was a design choice. You must set the star mass and radius to continue adding planets. Once you click
set you can begin to add as many planets. However, you must set each planet's mass, radius, and velocity and press
set before clicking the screen to add another planet. There is a margin around the sun that prevents the user from adding
a planet on top of or too close to the sun. If the user is unhappy with any of the positions they placed a planet,
they can click undo, and it will undo the most recent planet they have placed. Once the user is happy, they can press start and
watch the simulation run. The user can press pause and add more planets, press reset to clear the screen, start to resume, or observe
how planets collide. When a collision occurs between planets, the two planets will show a small bounce collision animation and
create a smaller planet with a new mass, radius, and velocity based off the two new planets and is depicted by a new
brighter color and trail.

The physics engine computes gravitational forces between all pairs of celestial bodies at each frame, resulting in
O(n²) time complexity since every body interacts with every other body.
To maintain numerical stability and realistic orbital motion, the simulation uses a leapfrog integration method.
In this approach, we first compute gravitational accelerations at the current positions, then update velocities by a half-time step,
update positions for a full time step, recompute accelerations at the new positions, and finally complete the velocity update with another half step.