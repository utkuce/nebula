import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import com.squareup.okhttp.*;
import org.json.JSONArray;


class Translator {

    private String subscriptionKey ;
    private String url = "https://api.cognitive.microsofttranslator.com/translate?api-version=3.0&to=en";

    // Instantiates the OkHttpClient.
    private OkHttpClient client = new OkHttpClient();

    Translator() {

        subscriptionKey = readFile("key.txt");
    }

    String translate(String text) {

            MediaType mediaType = MediaType.parse("application/json");
            RequestBody body = RequestBody.create(mediaType,  "[{\n\t\"Text\": \"" + text + "\"\n}]");
            Request request = new Request.Builder()
                    .url(url).post(body)
                    .addHeader("Ocp-Apim-Subscription-Key", subscriptionKey)
                    .addHeader("Content-type", "application/json").build();


        try {

            Response response = client.newCall(request).execute();
            return getTranslation(response);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    private String getTranslation(Response response) throws IOException {

        String responseString = response.body().string();
        //System.out.println(responseString);

        return new JSONArray(responseString).getJSONObject(0)
                .getJSONArray("translations").getJSONObject(0)
                .get("text").toString();
    }

    private String readFile(String fileName) {

        try {

            File file = new File( getClass().getClassLoader().getResource(fileName).getFile());
            FileInputStream fis = new FileInputStream(file);
            byte[] data = new byte[(int) file.length()];
            fis.read(data);
            fis.close();

            return new String(data, StandardCharsets.UTF_8);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}