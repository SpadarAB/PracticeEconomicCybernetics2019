import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

public class Line implements Shape {
    private int X;
    private int Y;
    private int width;
    private int height;
    private double aParam;

    public Line(final int x, final int y, final int width, final int height, final double aParam) {
        this.X = x;
        this.Y = y;
        this.width = width;
        this.height = height;
        this.aParam = aParam;
    }

    @Override
    public Rectangle getBounds() {
        return new Rectangle(X, Y, width, height);
    }

    @Override
    public Rectangle2D getBounds2D() {
        return new Rectangle2D.Float(X, Y, width, height);
    }

    public int getCenterX() {
        return (X + width / 2);
    }

    public int getCenterY() {
        return (Y + height / 2);
    }

    public double getR(double alpha) {
        double sin = Math.sin(alpha);
        double cos = Math.cos(alpha);
        return (3 * this.aParam * sin * cos / (sin * sin * sin + cos * cos * cos));
    }

    @Override
    public boolean contains(double x, double y) {
        if (!getBounds().contains(x, y)) {
            return false;
        }
        double x0 = x - getCenterX();
        double y0 = getCenterY() - y;
        if (x0 < 0 || y0 < 0) {
            return false;
        }
        double alpha = Math.atan(y0 / x0);
        double r = getR(alpha);
        return (x0 * x0 + y0 * y0 < r * r);
    }

    @Override
    public boolean contains(final Point2D point2D) {
        return contains(point2D.getX(), point2D.getY());
    }

    @Override
    public boolean intersects(final double x, final double y, final double width, final double height) {
        return getBounds().intersects(x, y, width, height);
    }

    @Override
    public boolean intersects(final Rectangle2D rectangle2D) {
        return getBounds().intersects(rectangle2D);
    }

    @Override
    public boolean contains(final double x, final double y, final double width, final double height) {
        return contains(x, y) && contains(x + width, y) && contains(x, y + height)
                && contains(x + width, y + height);
    }

    @Override
    public boolean contains(final Rectangle2D rectangle2D) {
        return contains(rectangle2D.getX(), rectangle2D.getY(), rectangle2D.getWidth(), rectangle2D.getHeight());
    }

    @Override
    public PathIterator getPathIterator(final AffineTransform at) {
        return new Decart(at);
    }

    @Override
    public PathIterator getPathIterator(final AffineTransform at, final double flatness) {
        return getPathIterator(at);
    }

    class Decart implements PathIterator {
        int index = 0;
        private boolean start = true;
        private boolean done = false;
        private double aParam1 = (-1) * Math.PI / 2;
        private double aParam2 = Math.PI / 90;
        private AffineTransform at;
        private boolean noTransform = false;
        private int y0 = getCenterY();
        private int x0 = getCenterX();

        public Decart(final AffineTransform at) {
            this.at = at;
        }

        @Override
        public int getWindingRule() {
            return WIND_NON_ZERO;
        }

        @Override
        public boolean isDone() {
            return done;
        }

        @Override
        public void next() {
            if (!start)
                aParam1 += aParam2;
        }

        @Override
        public int currentSegment(float[] coordinates) {
            if (start) {
                coordinates[0] = x0;
                coordinates[1] = y0;
                if (noTransform)
                    noTransform = false;
                else if (at != null)
                    at.transform(coordinates, 0, coordinates, 0, 1);
                start = false;
                return SEG_MOVETO;
            } else {
                double r = getR(aParam1);
                coordinates[0] = (float) (getCenterX() + r * Math.cos(aParam1));
                coordinates[1] = (float) (getCenterY() - r * Math.sin(aParam1));
                if (at != null)
                    at.transform(coordinates, 0, coordinates, 0, 1);
                checkForNext(coordinates[0], coordinates[1]);
                return SEG_LINETO;
            }
        }

        @Override
        public int currentSegment(double[] coordinates) {
            if (start) {
                coordinates[0] = getCenterX();
                coordinates[1] = getCenterY();
                at.transform(coordinates, 0, coordinates, 0, 1);
                start = false;
                return SEG_MOVETO;
            } else {
                double r = getR(aParam1);
                coordinates[0] = (float) (getCenterX() + r * Math.cos(aParam1));
                coordinates[1] = (float) (getCenterY() - r * Math.sin(aParam1));
                at.transform(coordinates, 0, coordinates, 0, 1);
                checkForNext(coordinates[0], coordinates[1]);
                return SEG_LINETO;
            }
        }

        private void checkForNext(double x, double y) {
            if (index == 2 && aParam1 >= Math.PI) {
                done = true;
                return;
            }
            if ((index == 1 && aParam1 >= Math.PI / 2) || (index == 0 && !getBounds().contains(x, y))) {
                index++;
                if (index == 1)
                    aParam1 = 0;
                else if (index == 2) {
                    aParam1 = 3 * Math.PI / 4 + aParam2;
                    x0 = -10;
                    y0 = -10;
                    noTransform = true;
                }
                start = true;
            }
        }
    }
}