import javax.swing.*;
import javax.swing.plaf.metal.MetalLookAndFeel;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

class Interface {

    private JFrame frame = new JFrame("JPanel Test");
    private ImagePanel display = new ImagePanel();

    Interface(ScreenCapture screenCapture) {

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        //frame.setUndecorated(true);

        display.setBackground(Color.darkGray);

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        display.setPreferredSize(new Dimension(screenSize.width/2, screenSize.height/2));

        frame.add(display);

        frame.pack();
        frame.setVisible(true);

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

        void setImage(Image image) {
            this.image = image;
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(image, 0, 0, null);
        }

    }
}
