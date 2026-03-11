import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Panel containing the main simulation control buttons
 *
 * This class creates buttons for starting, pausing, resetting, and undoing
 * changes in the simulation
 */
public class ControlPanel extends JPanel{

    /**
     * Constructs the control panel and connects its buttons to the simulation
     *
     * @param planetPanel panel that manages the simulation
     */
    public ControlPanel(PlanetPanel planetPanel){

        setLayout(new FlowLayout(FlowLayout.LEFT));
        setBackground(Color.DARK_GRAY);

        JButton startButton = new JButton("START");
        startButton.setBackground(Color.decode(("#84b5ac")));

        JButton pauseButton = new JButton("PAUSE");
        pauseButton.setBackground(Color.decode("#de7b5a"));

        JButton resetButton = new JButton("RESET");
        resetButton.setBackground(Color.decode("#ae9ed2"));

        JButton undoButton = new JButton("UNDO");
        undoButton.setBackground(Color.decode(("#d1a554")));

        startButton.addActionListener(new StartButtonListener(planetPanel));
        pauseButton.addActionListener(new PauseButtonListener(planetPanel));
        undoButton.addActionListener(new UndoButtonListener(planetPanel));
        resetButton.addActionListener(new ResetButtonListener(planetPanel));

        add(startButton);
        add(pauseButton);
        add(resetButton);
        add(undoButton);
    }


    /**
     * Listener that undoes the most recently added planet
     */
    private class UndoButtonListener implements ActionListener {
        private PlanetPanel planetPanel;
        public UndoButtonListener(PlanetPanel planetPanel) {
            this.planetPanel = planetPanel;
        }

        /**
         * Handles the undo button click
         *
         * @param e action event
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            planetPanel.undoLastPlanet();
        }
    }

    /**
     * Listener that starts the simulation
     */
    private class StartButtonListener implements ActionListener {
        private PlanetPanel planetPanel;

        public StartButtonListener(PlanetPanel planetPanel){
            this.planetPanel = planetPanel;
        }

        /**
         * Handles the start button click
         *
         * @param e action event
         */
        @Override
        public void actionPerformed(ActionEvent e){
            planetPanel.startSimulation();
        }
    }

    private class PauseButtonListener implements ActionListener {
        private PlanetPanel planetPanel;

        public PauseButtonListener(PlanetPanel planetPanel){
            this.planetPanel = planetPanel;
        }

        @Override
        public void actionPerformed(ActionEvent e){
            planetPanel.pauseSimulation();
        }
    }

    /**
     * Listener that pauses the simulation
     */
    private class ResetButtonListener implements ActionListener {

        private PlanetPanel planetPanel;

        public ResetButtonListener(PlanetPanel planetPanel){
            this.planetPanel = planetPanel;
        }

        /**
         * Handles the pause button click
         *
         * @param e action event
         */
        @Override
        public void actionPerformed(ActionEvent e){
            planetPanel.resetSimulation();
        }
    }

}
