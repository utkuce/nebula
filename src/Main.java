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
            TextRecognition textRecognition = new TextRecognition();

            while (! ui.exitButton) {

                Image areaImage = screenCapture.capture();
                ui.setDisplayImage(areaImage);

                String recognizedText = textRecognition.recognize(areaImage);
                System.out.println(recognizedText);

                try {
                    Thread.sleep(16);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }
    }
}
