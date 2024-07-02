package icu.nyat.kusunoki.Utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpUtil {
    public String Fetch(String urlString) throws Exception {
        StringBuilder result = new StringBuilder();
        URL url = new URL(urlString);
        HttpURLConnection HttpRequest = (HttpURLConnection) url.openConnection();
        HttpRequest.setRequestMethod("GET");
        HttpRequest.setConnectTimeout(2000);
        HttpRequest.setReadTimeout(3000);
        BufferedReader reader = new BufferedReader(new InputStreamReader(HttpRequest.getInputStream()));
        String line;
        while ((line = reader.readLine()) != null) {
            result.append(line);
        }
        reader.close();
        return result.toString();
    }

}