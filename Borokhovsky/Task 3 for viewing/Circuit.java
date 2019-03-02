import java.awt.*;
import java.awt.geom.GeneralPath;
import java.awt.geom.PathIterator;

public class Circuit implements Stroke {
    private BasicStroke stroke;

    public Circuit(float width) {
        this.stroke = new BasicStroke(width);
    }

    public Shape createStrokedShape(Shape shape) {
        GeneralPath shape2 = new GeneralPath();
        float[] coordinates = new float[2];
        float[] prevCoordinates = new float[2];

        for (PathIterator i = shape.getPathIterator(null); !i.isDone(); i.next()) {
            int type = i.currentSegment(coordinates);
            switch (type) {
                case PathIterator.SEG_MOVETO:
                    shape2.moveTo(coordinates[0], coordinates[1]);
                    break;

                case PathIterator.SEG_LINETO:
                    double x1 = prevCoordinates[0];
                    double y1 = prevCoordinates[1];
                    double x2 = coordinates[0];
                    double y2 = coordinates[1];

                    double dx = x2 - x1;
                    double dy = y2 - y1;

                    shape2.lineTo(x1 + 2 * dy, y1 - 2 * dx);
                    shape2.lineTo(x2, y2);
                    break;
            }
            prevCoordinates[0] = coordinates[0];
            prevCoordinates[1] = coordinates[1];
        }
        return stroke.createStrokedShape(shape2);
    }
}