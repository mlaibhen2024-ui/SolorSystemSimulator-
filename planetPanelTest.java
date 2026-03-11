import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class planetPanelTest {
    private PlanetPanel panel;
    private List<Planet> planets;
    private List<?> effects;
    private Method collisionMethod;

    @BeforeEach
    void setUp() throws Exception {
        panel = new PlanetPanel();

        Field planetsField = PlanetPanel.class.getDeclaredField("planets");
        planetsField.setAccessible(true);
        planets = (List<Planet>) planetsField.get(panel);

        Field effectsField = PlanetPanel.class.getDeclaredField("collisionEffects");
        effectsField.setAccessible(true);
        effects = (List<?>) effectsField.get(panel);

        collisionMethod = PlanetPanel.class.getDeclaredMethod("handlePlanetCollisions");
        collisionMethod.setAccessible(true);
    }

    @Test
    void fewerThanTwoPlanets_noCollision() throws Exception {
        planets.clear();
        planets.add(new Planet(0,0,0,0,0,0,8,"#FFFFFF",100));

        collisionMethod.invoke(panel);

        assertEquals(1, planets.size());
        assertTrue(effects.isEmpty());
    }

    @Test
    void noCollision() throws Exception {
        planets.clear();
        collisionMethod.invoke(panel);

        assertEquals(0, planets.size());
        assertTrue(effects.isEmpty());
    }

    @Test
    void setVelocityToZero()  throws Exception{
        Planet p = new Planet(0,0,0,0,0,0,8,"#FFFFFF",100);

        p.setVelocity(5000,2000);
        p.setVelocity(0,0);

        assertEquals(0,p.getVelocityX());
        assertEquals(0,p.getVelocityY());
    }
    @Test
    void setVelocityXToNegative()  throws Exception{
        Planet p = new Planet(0,0,0,0,0,0,8,"#FFFFFF",100);
        p.setVelocity(-10, 2000);

        assertEquals(-10,p.getVelocityX());
        assertEquals(2000, p.getVelocityY());
    }

    @Test
    void setVelocityYToNegavite()  throws Exception{
        Planet p = new Planet(0,0,0,0,0,0,8, "#FFFFFF", 100);
        p.setVelocity(10, -2000);

        assertEquals(10, p.getVelocityX());
        assertEquals(-2000, p.getVelocityY());


    }
    @Test
    void setbothvelocityToNegative() throws Exception {
        Planet p = new Planet(0,0,0,0,0,0,8,"#FFFFFF", 100);

        p.setVelocity(-200, -600);

        assertEquals(-200, p.getVelocityX());
        assertEquals(-600, p.getVelocityY());
    }
}