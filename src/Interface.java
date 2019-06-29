import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

class Interface {

    private JFrame frame = new JFrame("JPanel Test");
    private ImagePanel display = new ImagePanel();

    Interface(ScreenCapture screenCapture) {

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        display.setPreferredSize(new Dimension(screenSize.width/2, screenSize.height/2));
        display.setBackground(Color.darkGray);
        frame.add(display);
        frame.pack();

        frame.addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent componentEvent) {

                // get original full resolution image and scale again
                Image capturedImage = screenCapture.getCapturedImage();
                if (capturedImage != null) {

                    Rectangle displayArea = getDisplayArea();
                    display.setSize(displayArea.width, displayArea.height);
                    setDisplayImage(capturedImage);
                }
            }
        });

        frame.setLocationRelativeTo(null);
        //frame.setUndecorated(true);

        frame.setVisible(true);
    }

    private Rectangle getDisplayArea() {

        Insets insets = frame.getInsets();

        int x = frame.getX() + insets.left;
        int y = frame.getY() + insets.top;

        int height = frame.getHeight() - insets.top - insets.bottom;
        int width = frame.getWidth() - insets.left - insets.right;

        return new Rectangle(x, y , width, height);
    }

    void setDisplayImage(Image image) {

        Rectangle newDims = getDisplayArea();
        Image scaledImage = image.getScaledInstance(newDims.width, newDims.height, Image.SCALE_SMOOTH);
        display.setImage(scaledImage);
        display.updateUI();
    }

    private class ImagePanel extends JPanel{

        private Image image;

        // selection area
        private Rectangle selection;
        private Shape shape = null;
        Point startDrag, endDrag;

        ImagePanel() {

            addMouseListener(new MouseAdapter() {
                public void mousePressed(MouseEvent e) {
                    startDrag = new Point(e.getX(), e.getY());
                    endDrag = startDrag;
                    repaint();
                }

                public void mouseReleased(MouseEvent e) {
                    if(endDrag!=null && startDrag!=null) {
                        try {
                            shape = makeRectangle(startDrag.x, startDrag.y, e.getX(),
                                    e.getY());
                            startDrag = null;
                            endDrag = null;
                            repaint();
                        } catch (Exception e1) {
                            e1.printStackTrace();
                        }
                    }
                }
            });

            addMouseMotionListener(new MouseMotionAdapter() {
                public void mouseDragged(MouseEvent e) {
                    endDrag = new Point(e.getX(), e.getY());
                    repaint();
                }
            });

        }

        protected void paintComponent(Graphics g) {

            super.paintComponent(g);

            Graphics2D g2 = (Graphics2D) g;
            g2.drawImage(image, 0, 0, null);
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            g2.setStroke(new BasicStroke(2));
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.50f));

            if (shape != null) {

                g2.setPaint(Color.BLACK);
                g2.draw(shape);
                g2.setPaint(Color.YELLOW);
                g2.fill(shape);
            }

            if (startDrag != null && endDrag != null) {

                g2.setPaint(Color.LIGHT_GRAY);
                Shape r = makeRectangle(startDrag.x, startDrag.y, endDrag.x, endDrag.y);
                g2.draw(r);
            }

        }

        private Rectangle2D.Float makeRectangle(int x1, int y1, int x2, int y2) {
            return new Rectangle2D.Float(Math.min(x1, x2), Math.min(y1, y2),
                    Math.abs(x1 - x2), Math.abs(y1 - y2));
        }

        void setImage(Image image) {
            this.image = image;
        }

    }
}
