import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.OrientationRequested;
import javax.print.attribute.standard.Sides;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.*;
import java.util.ArrayList;
import java.util.logging.Logger;

public class Window extends JFrame implements Printable {
    private int centerX;
    private int centerY;
    private Shape pie;

    private Window() {
        initComponents();
        this.setSize(400, 470);
        centerX = getWidth() / 2;
        centerY = getHeight() / 2;
        pie = new Line(0, 0, getWidth(), getHeight(), getWidth() / 4);
        textLines = initTextLines(new File("src/DecartsLeaf.java"));
    }

    private void initComponents() {
        jPanel1 = new JPanel() {
            @Override
            public void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g.setColor(Color.white);
                g.fillRect(0, 0, getWidth(), getHeight());
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(Color.BLACK);
                g2d.draw(new Line2D.Double(0, centerY, getWidth(), centerY));
                g2d.draw(new Line2D.Double(centerX, 0, centerX, getHeight()));
                g2d.setColor(Color.RED);
                g2d.setStroke(new Circuit(2));
                g2d.draw(pie);
            }
        };
        jMenuBar1 = new JMenuBar();
        jMenu2 = new JMenu();
        jMenuItem1 = new JMenuItem();

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new Color(255, 255, 255));

        GroupLayout jPanel1Layout = new GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
                jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGap(0, 400, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
                jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGap(0, 279, Short.MAX_VALUE)
        );

        jMenu2.setText("Меню");

        jMenuItem1.setText("Печать");
        jMenuItem1.addActionListener(this::jMenuItem1ActionPerformed);
        jMenu2.add(jMenuItem1);

        jMenuBar1.add(jMenu2);

        setJMenuBar(jMenuBar1);

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(jPanel1, GroupLayout.DEFAULT_SIZE,
                                GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(jPanel1, GroupLayout.DEFAULT_SIZE,
                                GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        pack();
    }

    private void jMenuItem1ActionPerformed(ActionEvent event) {
        PrinterJob job = PrinterJob.getPrinterJob();
        PrintRequestAttributeSet printRequestAttributeSet = new HashPrintRequestAttributeSet();
        printRequestAttributeSet.add(Sides.DUPLEX);
        printRequestAttributeSet.add(OrientationRequested.LANDSCAPE);
        job.setPrintable(this);
        boolean ok = job.printDialog(printRequestAttributeSet);
        if (ok) {
            try {
                job.print(printRequestAttributeSet);
            } catch (PrinterException ex) {
                System.err.print(ex);
            }
        }
    }

    public static void main(String args[]) {
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Windows".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | UnsupportedLookAndFeelException | IllegalAccessException ex) {
            Logger.getLogger(Window.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        EventQueue.invokeLater(() -> new Window().setVisible(true));
    }

    private JMenu jMenu2;
    private JMenuBar jMenuBar1;
    private JMenuItem jMenuItem1;
    private JPanel jPanel1;
    private String[] textLines;
    private int[] pageBreaks;
    private BufferedImage b;

    @Override
    public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {
        int y = 0;
        Font font = new Font("Serif", Font.PLAIN, 10);
        FontMetrics metrics = graphics.getFontMetrics(font);
        int lineHeight = metrics.getHeight();
        if (pageIndex == 0) {
            BufferedImage bufferedImageAll = new BufferedImage(this.getWidth(), this.getHeight(),
                    BufferedImage.TYPE_INT_RGB);
            Graphics2D graphics2DForImage = bufferedImageAll.createGraphics();
            this.printAll(graphics2DForImage);
            double scale = pageFormat.getWidth() / this.getWidth();
            int newWidth = (int) (this.getWidth() * scale / 2.5);
            int newHeight = (int) (this.getHeight() * scale / 2.5);
            b = bufferedImageAll;
            Image scaledImage = bufferedImageAll.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
            graphics.drawImage(scaledImage, (int) (pageFormat.getImageableWidth() / 2 - 75),
                    (int) (pageFormat.getImageableHeight() / 40 + 100), null);
            return PAGE_EXISTS;
        }

        if (pageBreaks == null) {
            int linesPerPage = (int) (pageFormat.getImageableHeight() / lineHeight);
            int numBreaks = (textLines.length - 1) / linesPerPage + 1;
            pageBreaks = new int[numBreaks];
            for (int b = 0; b < numBreaks; b++) {
                pageBreaks[b] = b * linesPerPage;
            }
        }

        if (pageIndex > pageBreaks.length) {
            return NO_SUCH_PAGE;
        }
        Graphics2D g2D = (Graphics2D) graphics;
        g2D.translate(pageFormat.getImageableX(), pageFormat.getImageableY());
        int start = pageBreaks[pageIndex - 1];
        int end = (pageIndex == pageBreaks.length) ? textLines.length : pageBreaks[pageIndex];
        for (int line = start; line < end; line++) {
            y += lineHeight;
            graphics.drawString(textLines[line], 0, y);
        }
        return PAGE_EXISTS;
    }

    private String[] initTextLines(File file) {
        ArrayList<String> result = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                result.add(line);
            }
        } catch (FileNotFoundException ex) {
            return null;
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
        return result.toArray(new String[result.size()]);
    }
}