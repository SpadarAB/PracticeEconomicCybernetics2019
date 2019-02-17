import java.applet.Applet;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.Timer;

/*<applet code="AppletEllipse" width=500 height=300>
<param name=borderColor value=#EFFF00>
<param name=backgroundColor value=#FF0044>
<param name=border value=2>
</applet>*/

public class Main extends Applet implements Runnable {
    private int centerX = 0;
    private int centerY = 0;
    private int radius = 40;

    private int stepX = 1;
    private int stepY = 2;

    private double g = 0.9;
    private double scaleX = 1;
    private double scaleY = 1;

    private float border;

    private Color borderColor;
    private Color backgroundColor;

    private Circle circle;

    private static int APPLET_WIDTH = 500;
    private static int APPLET_HEIGHT = 300;

    private Thread thread;

    @Override
    public void init() {
        setBackground(Color.WHITE);
        setSize(APPLET_WIDTH, APPLET_HEIGHT);
        centerX = (int)(0.75 * getWidth());
        centerY = (int)(0.75 * getHeight());
        try {
            border = Float.parseFloat(this.getParameter("border"));
        } catch (Exception e) {
            border = 1;
        }

        borderColor = getColor(this.getParameter("borderColor"), new Color(0,0,0));
        backgroundColor = getColor(this.getParameter("backgroundColor"), new Color(105,255,0));

        circle = new Circle(radius, borderColor, backgroundColor);
    }

    @Override
    public void start() {
        if (thread == null) {
            thread = new Thread(this);
        }
        thread.start();
    }

    @Override
    public void stop() {
        if (thread != null) {
            thread.interrupt();
            thread = null;
        }
    }

    @Override
    public void destroy() {
        if (thread != null) {
            thread.interrupt();
            thread = null;
        }
    }


    @Override
    public void paint(Graphics graphics) {
        Graphics2D graphics2D = (Graphics2D) graphics;
        graphics2D.setStroke(new BasicStroke(border));

        circle = new Circle(radius, borderColor, backgroundColor);
        AffineTransform at = new AffineTransform();
        at.translate(centerX ,centerY);

        double
                tmpScaleX = 1 - (double)(centerX + radius - getWidth()) / (double)radius,
                tmpScaleY = 1 - (double)(centerY + radius - getHeight()) / (double)radius;

        if(tmpScaleX <= 1 && tmpScaleX >= g)
            scaleX = tmpScaleX;
        at.scale(scaleX, 1);

        if(tmpScaleY <= 1 && tmpScaleY >= g)
            scaleY = tmpScaleY;
        at.scale(1, scaleY);

        tmpScaleX = 1 + (double)(centerX - radius) / (double)radius;
        tmpScaleY = 1 + (double)(centerY - radius) / (double)radius;

        if(tmpScaleX <= 1 && tmpScaleX >= g)
            scaleX = tmpScaleX;
        at.scale(scaleX, 1);

        if(tmpScaleY <= 1 && tmpScaleY >= g)
            scaleY = tmpScaleY;
        at.scale(1, scaleY);

        graphics2D.setTransform(at);
        circle.paint(graphics2D);

        if(centerX < g * radius || centerX > getWidth() - g * radius)
            stepX *= -1;
        if(centerY < g * radius || centerY > getHeight() - g * radius)
            stepY *= -1;

        centerX += stepX;
        centerY += stepY;
    }

    @Override
    public void run() {
        while (true) {
            repaint();
            try {
                Thread.sleep(25);
            } catch (InterruptedException ignored) {
                break;
            }
        }
    }

    private Color getColor(String strRGB, Color color) {
        if ((strRGB != null) && (strRGB.charAt(0) == '#')) {
            try {
                return new Color(Integer.parseInt(strRGB.substring(1), 16));
            } catch (NumberFormatException e) {
                return color;
            }
        }
        return color;
    }
}