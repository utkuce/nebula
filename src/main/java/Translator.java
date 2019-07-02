import com.google.cloud.translate.Translate;
import com.google.cloud.translate.Translate.TranslateOption;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;


class Translator {

    private Translate translate;

    Translator() {

        // Instantiates a client
        translate = TranslateOptions.getDefaultInstance().getService();
    }

    String translate(String text) {

        Translation translation =
                translate.translate(
                        text,
                        TranslateOption.sourceLanguage("fr"),
                        TranslateOption.targetLanguage("en"));


        return translation.getTranslatedText();
    }
}