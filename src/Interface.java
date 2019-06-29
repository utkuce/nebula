import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

class Interface extends JFrame{

    private JFrame frame = new JFrame("JPanel Test");
    private ImagePanel display = new ImagePanel();

    Interface() {

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //frame.setLocationRelativeTo(null);
        //frame.setUndecorated(true);

        display.setBackground(Color.darkGray);
        display.setPreferredSize(new Dimension(640, 480));

        frame.add(display);

        frame.pack();
        frame.setVisible(true);
    }

    Rectangle getDisplayArea() {

        Insets insets = frame.getInsets();

        int x = frame.getX() + insets.left;
        int y = frame.getY() + insets.top;

        int height = frame.getHeight() - insets.top - insets.bottom;
        int width = frame.getWidth() - insets.left - insets.right;

        return new Rectangle(x, y , width, height);
    }

    void setDisplayImage(BufferedImage image) {

        display.setImage(image);
        display.updateUI();
    }

    private class ImagePanel extends JPanel{

        private BufferedImage image;

        void setImage(BufferedImage image) {
            this.image = image;
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(image, 0, 0, null);
        }

    }
}
