import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.RescaleOp;

public class Canvas {
    //Exlamation mark position.
    private final int EXCLAM_MARK_X_POS = 380;
    private final int EXCLAM_MARK_Y_POS = 430;

    //Points of triangles, divided by pairs.
    private final int OUTTER_TRIANGLE_X_1_POS = 227;
    private final int OUTTER_TRIANGLE_Y_1_POS = 500;
    ////
    private final int OUTTER_TRIANGLE_X_2_POS = 400;
    private final int OUTTER_TRIANGLE_Y_2_POS = 200;
    ////
    private final int OUTTER_TRIANGLE_X_3_POS = 573;
    private final int OUTTER_TRIANGLE_Y_3_POS = 500;

    //Inner triangle points.
    private final int INNER_TRIANGLE_X_1_POS = 313;
    private final int INNER_TRIANGLE_Y_1_POS = 450;
    ////
    private final int INNER_TRIANGLE_X_2_POS = 400;
    private final int INNER_TRIANGLE_Y_2_POS = 300;
    ////
    private final int INNER_TRIANGLE_X_3_POS = 487;
    private final int INNER_TRIANGLE_Y_3_POS = 450;

    private final int[] oTriangleXPoint = {OUTTER_TRIANGLE_X_1_POS, OUTTER_TRIANGLE_X_2_POS, OUTTER_TRIANGLE_X_3_POS};
    private final int[] oTriangleYPoint = {OUTTER_TRIANGLE_Y_1_POS, OUTTER_TRIANGLE_Y_2_POS, OUTTER_TRIANGLE_Y_3_POS};
    private final int[] iTriangleXPoint = {INNER_TRIANGLE_X_1_POS, INNER_TRIANGLE_X_2_POS, INNER_TRIANGLE_X_3_POS};
    private final int[] iTriangleYPoint = {INNER_TRIANGLE_Y_1_POS, INNER_TRIANGLE_Y_2_POS, INNER_TRIANGLE_Y_3_POS};
    private final int[] shTriangleXPoint = {OUTTER_TRIANGLE_X_1_POS - 50, OUTTER_TRIANGLE_X_2_POS - 50, OUTTER_TRIANGLE_X_3_POS - 50};
    private final int[] shTriangleYPoint = {OUTTER_TRIANGLE_Y_1_POS - 50, OUTTER_TRIANGLE_Y_2_POS - 50, OUTTER_TRIANGLE_Y_3_POS - 50};

    JPanel panel = new JPanel() {
        @Override
        protected void paintComponent(Graphics graphics) {
            super.paintComponent(graphics);

            BufferedImage bufferedImage = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
            Graphics2D graphics2D = (Graphics2D) bufferedImage.getGraphics();
            GradientPaint gradientPaint = new GradientPaint(0, 0, Color.DARK_GRAY, 0, getHeight(), Color.LIGHT_GRAY);
            graphics2D.setPaint(gradientPaint);
            graphics2D.fillRect(0, 0, getWidth(), getHeight());

            Triangle shTriangle = new Triangle(shTriangleXPoint, shTriangleYPoint);
            Triangle oTriangle = new Triangle(oTriangleXPoint, oTriangleYPoint);
            Triangle inTriangle = new Triangle(iTriangleXPoint, iTriangleYPoint);

            shTriangle.paintTriangle(graphics2D, Color.darkGray);
            oTriangle.paintTriangle(graphics2D, Color.BLUE);
            inTriangle.paintTriangle(graphics2D, Color.cyan);

            paintText(graphics2D);

            RescaleOp op = new RescaleOp(-1f, 255f, null);
            bufferedImage = op.filter(bufferedImage, null);  // Фильтрация изображения(получается негативный вариант исходного.)

            graphics.drawImage(bufferedImage, 0, 0, null);
        }

        public void paintText(Graphics2D graphics2D) {
            graphics2D.setColor(Color.blue);
            graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            Font font = new Font("Serif", Font.PLAIN, 125);
            graphics2D.setFont(font);
            graphics2D.drawString("!", EXCLAM_MARK_X_POS, EXCLAM_MARK_Y_POS);
        }
    };
}