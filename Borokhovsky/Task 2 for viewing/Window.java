import javax.swing.*;

public class Window extends JFrame {
    private final static int WIDTH = 800;
    private final static int HEIGHT = 800;

    public static void main(String[] args){
        Canvas canvas = new Canvas();
        JFrame frame = new JFrame();
        frame.setSize(WIDTH, HEIGHT);
        frame.setResizable(false);
        frame.add(canvas.panel);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }
}
