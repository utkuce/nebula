import java.awt.*;

public class Main {

    public static void main(String[] args) {

        ScreenCapture screenCapture = null;
        TextRecognition textRecognition = new TextRecognition();

        try {
            screenCapture = new ScreenCapture();
        } catch (AWTException e) {
            e.printStackTrace();
        }

        if (screenCapture != null) {

            Interface ui = new Interface(screenCapture, textRecognition);

            while (! ui.exitButton) {

                Image areaImage = screenCapture.capture();
                ui.setDisplayImage(areaImage);

                if (textRecognition.active) {

                    String recognizedText = textRecognition.recognize(areaImage);
                    System.out.println(recognizedText);
                }


                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }
    }
}
