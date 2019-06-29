import java.awt.*;

class ScreenCapture {

    private Robot robot;
    private Rectangle wholeScreen;
    private Rectangle captureArea;

    private Image capturedImage;

    ScreenCapture() throws AWTException {

        wholeScreen = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
        captureArea = wholeScreen;

        robot = new Robot();
    }

    Image capture() {

        if (captureArea.width > 0 && captureArea.height > 0) {
            capturedImage = robot.createScreenCapture(captureArea);
        }

        return capturedImage;
    }

    Image getCapturedImage()  { // full resolution
        return capturedImage;
    }

    void resetCaptureArea() {

        captureArea = wholeScreen;
        capture();
    }

    void setCaptureArea(Rectangle area){
        captureArea = area;
    }
}
