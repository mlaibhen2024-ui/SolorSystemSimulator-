import javax.swing.*;
import java.awt.*;

/**
 * Runner for the solar system simulator application
 *
 * This class creates the main window and adds the planet panel,
 * simulation panel, and control panel to the frame.
 */
public class SolarSystem extends JPanel {

    /**
     * Launches the solar system simulator application
     * @param args
     */
    public static void main (String[] args){
        MainFrame mainFrameGui = new MainFrame();
        PlanetPanel planetPanelGui = new PlanetPanel();
        SimulationPanel simulationPanelGui = new SimulationPanel(planetPanelGui);
        ControlPanel controlPanelGui = new ControlPanel(planetPanelGui);

        mainFrameGui.add(simulationPanelGui, BorderLayout.EAST);
        mainFrameGui.add(planetPanelGui);
        mainFrameGui.add(controlPanelGui, BorderLayout.SOUTH);
        mainFrameGui.setVisible(true);

    }

}
