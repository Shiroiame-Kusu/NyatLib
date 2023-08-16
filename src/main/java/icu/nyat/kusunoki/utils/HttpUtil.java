package icu.nyat.kusunoki.utils;

import icu.nyat.kusunoki.utils.NyatLibLogger;
import java.io.IOException;

import org.apache.hc.client5.http.fluent.Request;
import org.apache.hc.client5.http.fluent.Response;
import org.apache.hc.core5.util.Timeout;

public class HttpUtil {
    NyatLibLogger Logger = new NyatLibLogger();
    public String get(String url) {
        String result = null;
        try {
            Response response = Request.get(url).connectTimeout(Timeout.ofSeconds(3)).responseTimeout(Timeout.ofSeconds(5)).execute();
            result = response.returnContent().asString();
        } catch (IOException e) {
            Logger.logERROR(e.toString());
        }
        return result;
    }

}