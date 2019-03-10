import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

public class Line implements Shape {
    private int leftX;
    private int leftY;
    private int width;
    private int height;
    private double a;
    private double xScale;
    private double yScale;

    public Line(int x, int y, int width, int height, double a) {
        this(x, y, width, height, a, 1, 1);
    }

    public Line(int x, int y, int width, int height, double a,
                double xScale, double yScale) {
        super();
        this.leftX = x;
        this.leftY = y;
        this.width = width;
        this.height = height;
        this.a = a;
        this.xScale = xScale;
        this.yScale = yScale;
    }

    @Override
    public Rectangle getBounds() {
        return new Rectangle(leftX, leftY, width, height);
    }

    @Override
    public Rectangle2D getBounds2D() {
        return new Rectangle2D.Float(leftX, leftY, width, height);
    }

    public int getCenterX() {
        return leftX + width / 2;
    }

    public int getCenterY() {
        return leftY + height / 2;
    }

    public double getR(double alpha) {
        double s = Math.sin(alpha);
        double c = Math.cos(alpha);
        return 3 * a * s * c / (s * s * s + c * c * c);
    }

    @Override
    public boolean contains(double x, double y) {
        if (!getBounds().contains(x, y))
            return false;
        double x0 = x - getCenterX();
        double y0 = getCenterY() - y;
        if (x0 < 0 || y0 < 0)
            return false;
        double alpha = Math.atan(y0 / x0);
        double r = getR(alpha);
        return x0 * x0 + y0 * y0 < r * r;
    }

    @Override
    public boolean contains(Point2D p) {
        return contains(p.getX(), p.getY());
    }

    @Override
    public boolean intersects(double x, double y, double w, double h) {
        return getBounds().intersects(x, y, w, h);
    }

    @Override
    public boolean intersects(Rectangle2D r) {
        return getBounds().intersects(r);
    }

    @Override
    public boolean contains(double x, double y, double w, double h) {
        return contains(x, y) && contains(x + w, y) && contains(x, y + h)
                && contains(x + w, y + h);
    }

    @Override
    public boolean contains(Rectangle2D r) {
        return contains(r.getX(), r.getY(), r.getWidth(), r.getHeight());
    }

    @Override
    public PathIterator getPathIterator(AffineTransform at) {
        return new DecartsLeafIterator(at);
    }

    @Override
    public PathIterator getPathIterator(AffineTransform at, double flatness) {
        return getPathIterator(at);
    }

    class DecartsLeafIterator implements PathIterator {
        private int index = 0;
        private boolean start = true;
        private boolean done = false;
        private double curAngle = -Math.PI / 2;
        private double angleStep = Math.PI / 90;
        private AffineTransform at;
        private boolean noTransform = false;
        private int y0 = getCenterY();
        private int x0 = getCenterX();

        private DecartsLeafIterator(AffineTransform at) {
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
                curAngle += angleStep;
        }

        @Override
        public int currentSegment(float[] coords) {
            if (start) {
                coords[0] = x0;
                coords[1] = y0;
                if (noTransform)
                    noTransform = false;
                else if (at != null)
                    at.transform(coords, 0, coords, 0, 1);
                start = false;
                return SEG_MOVETO;
            } else {
                double r = getR(curAngle);
                coords[0] = (float) (getCenterX() + xScale * r
                        * Math.cos(curAngle));
                coords[1] = (float) (getCenterY() - yScale * r
                        * Math.sin(curAngle));
                if (at != null)
                    at.transform(coords, 0, coords, 0, 1);
                checkForNext(coords[0], coords[1]);
                return SEG_LINETO;
            }
        }

        @Override
        public int currentSegment(double[] coords) {
            if (start) {
                coords[0] = getCenterX();
                coords[1] = getCenterY();
                at.transform(coords, 0, coords, 0, 1);
                start = false;
                return SEG_MOVETO;
            } else {
                double r = getR(curAngle);
                coords[0] = (float) (getCenterX() + r * Math.cos(curAngle));
                coords[1] = (float) (getCenterY() - r * Math.sin(curAngle));
                at.transform(coords, 0, coords, 0, 1);
                checkForNext(coords[0], coords[1]);
                return SEG_LINETO;
            }
        }

        private void checkForNext(double x, double y) {
            if (index == 2 && curAngle >= Math.PI) {
                done = true;
                return;
            }
            if ((index == 1 && curAngle >= Math.PI / 2)
                    || (index == 0 && !getBounds().contains(x, y))) {
                index++;
                if (index == 1)
                    curAngle = 0;
                else if (index == 2) {
                    curAngle = 3 * Math.PI / 4 + angleStep;
                    x0 = -10;
                    y0 = -10;
                    noTransform = true;
                }
                start = true;

            }
        }

    }
}
