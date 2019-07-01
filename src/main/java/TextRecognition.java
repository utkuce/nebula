import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract1;
import net.sourceforge.tess4j.TesseractException;
import net.sourceforge.tess4j.util.LoadLibs;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

class TextRecognition {

    private ITesseract instance;
    boolean active = false;

    TextRecognition() {

        instance = new Tesseract1(); // JNA Direct Mapping

        // Maven build bundles English data
        File tessDataFolder = LoadLibs.extractTessResources("tessdata");
        instance.setDatapath(tessDataFolder.getPath());
    }

    String recognize(Image image) {

        try {
            return instance.doOCR((BufferedImage) image);
        } catch (TesseractException e) {
            e.printStackTrace();
        }

        return null;
    }
}