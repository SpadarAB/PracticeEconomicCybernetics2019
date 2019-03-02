import javax.swing.*;
import java.awt.*;

public class Window {
    private final static int WIDTH = 600;
    private final static int HEIGHT = 600;
    private static Shape shape;

    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setSize(WIDTH, HEIGHT);
        frame.setResizable(false);

        JPanel panel = new JPanel() {
            @Override
            public void paint(Graphics graphics) {
                super.paint(graphics);
                Graphics2D graphics2D = (Graphics2D) graphics;
                graphics2D.setColor(Color.WHITE);
                graphics2D.fillRect(0, 0, getWidth(), getHeight());
                graphics2D.setColor(Color.BLACK);
                graphics2D.setStroke(new Circuit(3));
                shape = new Line(0, 0, getWidth(), getHeight(), getWidth() / 4);
                graphics2D.draw(shape);
            }
        };

        frame.add(panel);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
