import ui.ApotekGUI;
import javax.swing.SwingUtilities;

public class main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ApotekGUI gui = new ApotekGUI();
            gui.setVisible(true);
        });
    }
}