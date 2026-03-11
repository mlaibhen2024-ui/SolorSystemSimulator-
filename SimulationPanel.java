import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Hashtable;

/**
 * Side panel containing sliders, labels, and buttons for setting up the star
 * and planets in the solar system simulator
 */
public class SimulationPanel extends JPanel {

    private final PlanetPanel planetPanel;

    private JSlider starMassSlider;
    private JSlider starRadiusSlider;
    private JSlider planetMassSlider;
    private JSlider planetRadiusSlider;
    private JSlider velocitySlider;

    private JLabel starMassLabel;
    private JLabel starRadiusLabel;
    private JLabel planetMassLabel;
    private JLabel planetRadiusLabel;
    private JLabel velocityLabel;
    private JLabel statusLabel;

    /**
     * Constructs the simulation control panel
     *
     * @param planetPanel main simulation panel to control
     */
    public SimulationPanel(PlanetPanel planetPanel) {
        this.planetPanel = planetPanel;

        setPreferredSize(new Dimension(325, 800));
        setBackground(new Color(35, 33, 53));
        setLayout(new BorderLayout());

        add(createControls(), BorderLayout.CENTER);
    }

    /**
     * Builds and returns the panel containing all controls
     *
     * @return panel containing sliders, labels, and buttons
     */
    private JPanel createControls() {
        JPanel panel = new JPanel();
        panel.setBackground(new Color(35, 33, 53));
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(new EmptyBorder(18, 18, 18, 18));

        JLabel title = new JLabel("SOLAR SYSTEM SIMULATOR");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Arial", Font.BOLD, 18));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(title);

        panel.add(Box.createVerticalStrut(10));

        statusLabel = new JLabel("<html>1) Click once to place the star<br>2) Apply star settings<br>3) Click to place a planet<br>4) Apply planet settings</html>");
        statusLabel.setForeground(Color.LIGHT_GRAY);
        statusLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(statusLabel);

        panel.add(Box.createVerticalStrut(18));

        panel.add(sectionTitle("Star Settings"));

        starMassLabel = new JLabel("Star Mass: 2.0e30 kg");
        starMassLabel.setForeground(Color.WHITE);
        starMassLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(starMassLabel);

        starMassSlider = new JSlider(10, 30, 20);
        styleSlider(starMassSlider, makeLabelTable(new int[]{10, 20, 30}, new String[]{"1e30", "2e30", "3e30"}));
        starMassSlider.addChangeListener(new StarMassSliderListener(starMassLabel, starMassSlider));
        panel.add(starMassSlider);

        panel.add(Box.createVerticalStrut(16));

        starRadiusLabel = new JLabel("Star Radius: 30 px");
        starRadiusLabel.setForeground(Color.WHITE);
        starRadiusLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(starRadiusLabel);

        starRadiusSlider = new JSlider(10, 45, 30);
        styleSlider(starRadiusSlider, makeLabelTable(new int[]{10, 20, 30, 40}, new String[]{"10", "20", "30", "40"}));
        starRadiusSlider.addChangeListener(new StarRadiusSliderListener(starRadiusLabel, starRadiusSlider));
        panel.add(starRadiusSlider);

        panel.add(Box.createVerticalStrut(16));

        JButton applyStarButton = new JButton("Apply Star Settings");
        applyStarButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        applyStarButton.addActionListener(new ApplyStarSettingsListener(this));
        panel.add(applyStarButton);

        panel.add(Box.createVerticalStrut(24));

        panel.add(sectionTitle("Planet Settings"));

        planetMassLabel = new JLabel("Planet Mass: 6.0e24 kg");
        planetMassLabel.setForeground(Color.WHITE);
        planetMassLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(planetMassLabel);

        planetMassSlider = new JSlider(1, 100, 60);
        styleSlider(planetMassSlider, makeLabelTable(new int[]{1, 25, 50, 75, 100}, new String[]{"1e23", "2.5e24", "5e24", "7.5e24", "1e25"}));
        planetMassSlider.addChangeListener(new PlanetMassSliderListener(planetMassLabel, planetMassSlider));
        panel.add(planetMassSlider);

        panel.add(Box.createVerticalStrut(16));

        planetRadiusLabel = new JLabel("Planet Radius: 10 px");
        planetRadiusLabel.setForeground(Color.WHITE);
        planetRadiusLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(planetRadiusLabel);

        planetRadiusSlider = new JSlider(4, 18, 10);
        styleSlider(planetRadiusSlider, makeLabelTable(new int[]{4, 8, 12, 16}, new String[]{"4", "8", "12", "16"}));
        planetRadiusSlider.addChangeListener(new PlanetRadiusSliderListener(planetRadiusLabel, planetRadiusSlider));
        panel.add(planetRadiusSlider);

        panel.add(Box.createVerticalStrut(16));

        velocityLabel = new JLabel("Orbit Speed Multiplier: 1.00x");
        velocityLabel.setForeground(Color.WHITE);
        velocityLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(velocityLabel);

        velocitySlider = new JSlider(50, 150, 100);
        styleSlider(velocitySlider, makeLabelTable(new int[]{50, 75, 100, 125, 150}, new String[]{"0.5x", "0.75x", "1.0x", "1.25x", "1.5x"}));
        velocitySlider.addChangeListener(new VelocitySliderListener(velocityLabel, velocitySlider));
        panel.add(velocitySlider);

        panel.add(Box.createVerticalStrut(16));

        JButton applyPlanetButton = new JButton("Apply To Newest Planet");
        applyPlanetButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        applyPlanetButton.addActionListener(new ApplyPlanetSettingsListener(this));
        panel.add(applyPlanetButton);

        panel.add(Box.createVerticalGlue());

        return panel;
    }

    /**
     * Creates a stylized section heading label
     *
     * @param text heading text
     * @return title
     */
    private JLabel sectionTitle(String text) {
        JLabel label = new JLabel(text);
        label.setForeground(new Color(132, 181, 172));
        label.setFont(new Font("Arial", Font.BOLD, 15));
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        return label;
    }

    /**
     * Formatting for the slider
     *
     * @param slider slider to style
     * @param labelTable label table for tick labels
     */
    private void styleSlider(JSlider slider, Hashtable<Integer, JLabel> labelTable) {
        slider.setBackground(new Color(35, 33, 53));
        slider.setForeground(Color.WHITE);
        slider.setMajorTickSpacing(Math.max(1, (slider.getMaximum() - slider.getMinimum()) / 4));
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);
        slider.setLabelTable(labelTable);
        slider.setAlignmentX(Component.CENTER_ALIGNMENT);
        slider.setMaximumSize(new Dimension(260, 55));
    }

    /**
     * Builds a label table for slider tick marks
     *
     * @param values slider positions
     * @param labels visible text labels
     * @return table
     */
    private Hashtable<Integer, JLabel> makeLabelTable(int[] values, String[] labels) {
        Hashtable<Integer, JLabel> table = new Hashtable<>();

        for (int i = 0; i < values.length; i++) {
            JLabel label = new JLabel(labels[i]);
            label.setForeground(Color.WHITE);
            table.put(values[i], label);
        }

        return table;
    }

    /**
     * Formats a numeric mass value in scientific notation
     *
     * @param value mass value to format
     * @return formatted mass string
     */
    private String formatSci(double value) {
        return String.format("%.2e kg", value);
    }

    /**
     * Applies the current star settings from the UI to the simulation
     */
    private void applyStarSettings() {
        if (!planetPanel.hasPlacedStar()) {
            statusLabel.setText("Click inside the space panel once to place the star first!");
            return;
        }

        double starMass = starMassSlider.getValue() * 1.0e29;
        double starRadius = starRadiusSlider.getValue();

        planetPanel.setStarMass(starMass);
        planetPanel.setStarRadius(starRadius);

        statusLabel.setText("<html>Star ready!<br>Now click in space to place a planet, then apply planet settings!</html>");
    }

    /**
     * Applies the current planet settings from the UI to the most recent planet
     */
    private void applyPlanetSettings() {
        if (!planetPanel.starComplete()) {
            statusLabel.setText("Apply the star settings first!");
            return;
        }

        if (!planetPanel.hasPendingPlanet()) {
            statusLabel.setText("Click in space to place a new planet first!");
            return;
        }

        double planetMass = planetMassSlider.getValue() * 1.0e23;
        double planetRadius = planetRadiusSlider.getValue();
        double velocityMultiplier = velocitySlider.getValue() / 100.0;

        planetPanel.setPlanetMass(planetMass);
        planetPanel.setPlanetRadius(planetRadius);
        planetPanel.setPlanetVelocity(velocityMultiplier);

        statusLabel.setText("<html>Planet ready!<br>Click again to add another planet, or press START!</html>");
    }


    /**
     * Listener that applies the current star settings
     */
    public class ApplyStarSettingsListener implements ActionListener {
        private SimulationPanel panel;

        public ApplyStarSettingsListener(SimulationPanel panel) {
            this.panel = panel;
        }

        /**
         * Handles the apply-star button click
         *
         * @param e action event
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            panel.applyStarSettings();
        }
    }

    /**
     * Listener that applies the current planet settings
     */
    public class ApplyPlanetSettingsListener implements ActionListener {
        private SimulationPanel panel;

        public ApplyPlanetSettingsListener(SimulationPanel panel) {
            this.panel = panel;
        }

        /**
         * Handles the apply-planet button click
         *
         * @param e action event
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            panel.applyPlanetSettings();
        }
    }

    /**
     * Listener that updates the star mass label when the slider changes
     */
    public class StarMassSliderListener implements ChangeListener {
        private JLabel label;
        private JSlider slider;

        public StarMassSliderListener(JLabel label, JSlider slider) {
            this.label = label;
            this.slider = slider;
        }

        /**
         * Handles a star mass slider change
         *
         * @param e action event
         */
        @Override
        public void stateChanged(ChangeEvent e) {
            double value = slider.getValue() * 1.0e29;
            label.setText("Star Mass: " + formatSci(value));
        }
    }

    /**
     * Listener that updates the star radius label when the slider changes
     */
    public class StarRadiusSliderListener implements ChangeListener {
        private JLabel label;
        private JSlider slider;

        public StarRadiusSliderListener(JLabel label, JSlider slider) {
            this.label = label;
            this.slider = slider;
        }

        /**
         * Handles a star radius slider change.
         *
         * @param e action event
         */
        @Override
        public void stateChanged(ChangeEvent e) {
            label.setText("Star Radius: " + slider.getValue() + " px");
        }
    }

    /**
     * Listener that updates the planet mass label when the slider changes
     */
    public class PlanetMassSliderListener implements ChangeListener {
        private JLabel label;
        private JSlider slider;

        public PlanetMassSliderListener(JLabel label, JSlider slider) {
            this.label = label;
            this.slider = slider;
        }

        /**
         * Handles a planet mass slider change.
         *
         * @param e action event
         */
        @Override
        public void stateChanged(ChangeEvent e) {
            double value = slider.getValue() * 1.0e23;
            label.setText("Planet Mass: " + formatSci(value));
        }
    }

    /**
     * Listener that updates the planet radius label when the slider changes
     */
    public class PlanetRadiusSliderListener implements ChangeListener {
        private JLabel label;
        private JSlider slider;

        public PlanetRadiusSliderListener(JLabel label, JSlider slider) {
            this.label = label;
            this.slider = slider;
        }

        /**
         * Handles a planet radius slider change
         *
         * @param e action event
         */
        @Override
        public void stateChanged(ChangeEvent e) {
            label.setText("Planet Radius: " + slider.getValue() + " px");
        }
    }

    /**
     * Listener that updates the orbit speed label when the slider changes
     */
    public class VelocitySliderListener implements ChangeListener {
        private JLabel label;
        private JSlider slider;

        public VelocitySliderListener(JLabel label, JSlider slider) {
            this.label = label;
            this.slider = slider;
        }

        /**
         * Handles an orbit speed slider change
         *
         * @param e action event
         */
        @Override
        public void stateChanged(ChangeEvent e) {
            double value = slider.getValue() / 100.0;
            label.setText(String.format("Orbit Speed Multiplier: %.2fx", value));
        }
    }
}