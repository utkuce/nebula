import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Rectangle2D;

class Interface {

    private JFrame captureWindow = new JFrame("nebula - Screen capture");
    private JFrame translationWindow = new JFrame("nebula - Translator");
    private JLabel recognizedText = new JLabel("Recognized text will appear here.");

    private ImagePanel captureDisplay = new ImagePanel();
    private Dimension screenSize;

    private ScreenCapture screenCapture;

    boolean exitButton = false;

    Interface(ScreenCapture screenCapture, TextRecognition textRecognition) {

        this.screenCapture = screenCapture;

        // CAPTURE WINDOW
        captureWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        captureWindow.setAlwaysOnTop (true);

        screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        captureDisplay.setPreferredSize(new Dimension(screenSize.width/2, screenSize.height/2));
        captureDisplay.setBackground(Color.darkGray);
        captureWindow.add(captureDisplay);
        captureWindow.pack();

        JButton startButton = new JButton("Start");

        startButton.addActionListener(e -> {

            textRecognition.active = !textRecognition.active;
            startButton.setText(textRecognition.active ? "Stop" : "Start");
        });

        startButton.setMaximumSize(new Dimension(25, 25));
        startButton.setBackground(Color.white);
        startButton.setAlignmentX(0.0f);
        startButton.setAlignmentY(0.0f);
        captureDisplay.add(startButton);

        JButton resetButton = new JButton("Reset");
        resetButton.addActionListener(e -> {

            textRecognition.active = false;
            startButton.setText("Start");
            screenCapture.resetCaptureArea();
        });

        resetButton.setMaximumSize(new Dimension(25, 25));
        resetButton.setBackground(Color.white);
        resetButton.setAlignmentX(0.0f);
        resetButton.setAlignmentY(0.0f);
        captureDisplay.add(resetButton);

        captureWindow.addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent componentEvent) {

                // get original full resolution displayedImage and scale again
                Image capturedImage = screenCapture.getCapturedImage();
                if (capturedImage != null) {

                    Rectangle displayArea = getDisplayArea();
                    captureDisplay.setSize(displayArea.width, displayArea.height);
                    setDisplayImage(capturedImage);
                }
            }
        });

        captureWindow.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
               exitButton = true;
            }
        });

        captureWindow.setLocationRelativeTo(null);
        captureWindow.setVisible(true);

        // TRANSLATION WINDOW

        translationWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        translationWindow.setAlwaysOnTop (true);
        translationWindow.setLocation(captureWindow.getX() + captureWindow.getWidth(), captureWindow.getY());

        recognizedText.setPreferredSize(new Dimension(300, captureDisplay.getHeight()));
        recognizedText.setHorizontalAlignment(JLabel.LEFT);
        recognizedText.setVerticalAlignment(JLabel.TOP);
        translationWindow.add(recognizedText);

        translationWindow.pack();
        translationWindow.setVisible(true);
    }

    void setRecognizedText(String text) {
        recognizedText.setText("<html>"+ text +"</html>");
    }

    private Rectangle getDisplayArea() {

        Insets insets = captureWindow.getInsets();

        int x = captureWindow.getX() + insets.left;
        int y = captureWindow.getY() + insets.top;

        int height = captureWindow.getHeight() - insets.top - insets.bottom;
        int width = captureWindow.getWidth() - insets.left - insets.right;

        return new Rectangle(x, y , width, height);
    }

    void setDisplayImage(Image image) {

        Rectangle newDims = getDisplayArea();
        Image scaledImage = image.getScaledInstance(newDims.width, newDims.height, Image.SCALE_SMOOTH);
        captureDisplay.setDisplayedImage(scaledImage);
        captureDisplay.updateUI();
    }

    private class ImagePanel extends JPanel{

        private Image displayedImage;

        // selection area
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

                            shape = makeRectangle(startDrag.x, startDrag.y, e.getX(), e.getY());

                            System.out.println(shape);
                            Rectangle newCaptureArea = scaleSelectionToScreen(shape.getBounds());
                            screenCapture.setCaptureArea(newCaptureArea);

                            shape = null; // remove selection box

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
            g2.drawImage(displayedImage, 0, 0, null);
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

        void setDisplayedImage(Image displayedImage) {
            this.displayedImage = displayedImage;
        }

    }

    private Rectangle scaleSelectionToScreen(Rectangle newCaptureArea) {

        float scaleX = screenSize.width/getDisplayArea().width;
        float scaleY = screenSize.height/getDisplayArea().height;

        newCaptureArea.x *= scaleX;
        newCaptureArea.y *= scaleY;
        newCaptureArea.width *= scaleX;
        newCaptureArea.height *= scaleY;

        return newCaptureArea;
    }
}
