import javax.swing.*;
import java.awt.*;

/**
 * Main application window for the solar system simulator.
 *
 * Frame sets the size, layout, title, background color, and close
 * behavior for the solar system simulator UI
 */
public class MainFrame extends JFrame {

    private final int WIDTH;
    private final int HEIGHT;

    /**
     * Constructs the main frame and sizes it relative to the current screen
     */
    public MainFrame() {
        super();

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        WIDTH = (int) (screenSize.width * 0.9);
        HEIGHT = (int) (screenSize.height * 0.8);

        setSize(WIDTH, HEIGHT);
        setTitle("Solar System Simulator");
        getContentPane().setBackground(Color.BLACK);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);


    }

}