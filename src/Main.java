import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Main {

    public static void main(String[] args) {

        Rectangle allScreen = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());

        ScreenCapture sc = null;
        try {
            sc = new ScreenCapture();
        } catch (AWTException e) {
            e.printStackTrace();
        }

        if ( sc != null) {

            BufferedImage areaImage = sc.getImage(allScreen);

            File outputFile = new File("image.jpg");
            try {
                ImageIO.write(areaImage, "jpg", outputFile);
            } catch (IOException e) {
                e.printStackTrace();
            }


        }

    }
}
