import java.awt.*;
import java.awt.image.BufferedImage;

public class Main {

    public static void main(String[] args) {

        Interface ui = new Interface();

        Rectangle captureArea = ui.getDisplayArea();

        ScreenCapture screenCapture = null;
        try {
            screenCapture = new ScreenCapture();
        } catch (AWTException e) {
            e.printStackTrace();
        }

        if (screenCapture != null) {

            BufferedImage areaImage = screenCapture.getImage(captureArea);
            ui.setDisplayImage(areaImage);
        }
    }
}
