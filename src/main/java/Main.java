import java.awt.*;

public class Main {

    public static void main(String[] args) {

        ScreenCapture screenCapture = null;
        TextRecognition textRecognition = new TextRecognition();
        Translator translator = new Translator();

        try {
            screenCapture = new ScreenCapture();
        } catch (AWTException e) {
            e.printStackTrace();
        }

        String previousText = "";

        if (screenCapture != null) {

            Interface ui = new Interface(screenCapture, textRecognition);

            while (! ui.exitButton) {

                Image areaImage = screenCapture.capture();
                ui.setDisplayImage(areaImage);

                if (textRecognition.active) {

                    String recognizedText = textRecognition.recognize(areaImage);
                    ui.setRecognizedText(recognizedText);

                    if ( !previousText.equals(recognizedText)) {

                        String translatedText = translator.translate(recognizedText);
                        ui.setTranslatedText(translatedText.replaceAll("\n", "<br>"));
                    }

                    previousText = recognizedText;
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
