import java.awt.*;

public class Main {

    public static void main(String[] args) {

        ScreenCapture screenCapture = null;
        try {
            screenCapture = new ScreenCapture();
        } catch (AWTException e) {
            e.printStackTrace();
        }

        if (screenCapture != null) {

            Interface ui = new Interface(screenCapture);

            while (! ui.exitButton) {

                Image areaImage = screenCapture.capture();
                ui.setDisplayImage(areaImage);

                try {
                    Thread.sleep(16);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }
    }
}
