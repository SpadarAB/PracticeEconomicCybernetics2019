import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

public class Triangle implements Shape {
    private int[] xPoints;
    private int[] yPoints;

    Triangle(int[] inXPoints, int[] inYPoints){
        xPoints = new int [3];
        yPoints = new int [3];
        for(int i=0; i<3; i++) {
            xPoints[i] = inXPoints[i];
            yPoints[i] = inYPoints[i];
        }
    }

    public void paintTriangle (final Graphics2D graphics2D, final Color color){
        graphics2D.setColor(color);
        graphics2D.fillPolygon(xPoints, yPoints, 3);
    }

    @Override
    public Rectangle getBounds() {
        return null;
    }

    @Override
    public Rectangle2D getBounds2D() {
        return null;
    }

    @Override
    public boolean contains(final double x, final double y) {
        return false;
    }

    @Override
    public boolean contains(final Point2D point2D) {
        return false;
    }

    @Override
    public boolean intersects(final double x, final double y, final double width, final double height) {
        return false;
    }

    @Override
    public boolean intersects(final Rectangle2D rectangle2D) {
        return false;
    }

    @Override
    public boolean contains(final double x, final double y, final double width, final double height) {
        return false;
    }

    @Override
    public boolean contains(final Rectangle2D rectangle2D) {
        return false;
    }

    @Override
    public PathIterator getPathIterator(final AffineTransform affineTransform) {
        return null;
    }

    @Override
    public PathIterator getPathIterator(final AffineTransform affineTransform, final double flatness) {
        return null;
    }
}
