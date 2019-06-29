import java.awt.*;
import java.awt.image.BufferedImage;

class ScreenCapture {

    private Robot robot;

    ScreenCapture() throws AWTException {
        robot = new Robot();
    }

    BufferedImage getImage(Rectangle captureArea) {
        return robot.createScreenCapture(captureArea);
    }
}
