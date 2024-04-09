package icu.nyat.kusunoki.utils;

import icu.nyat.kusunoki.utils.NyatLibLogger;

import java.io.Closeable;
import java.io.IOException;
import java.util.concurrent.Future;

import org.apache.hc.client5.http.fluent.Request;
import org.apache.hc.client5.http.fluent.Response;
import org.apache.hc.client5.http.impl.async.CloseableHttpAsyncClient;
import org.apache.hc.core5.concurrent.FutureCallback;
import org.apache.hc.core5.function.Supplier;
import org.apache.hc.core5.http.HttpHost;
import org.apache.hc.core5.http.nio.AsyncPushConsumer;
import org.apache.hc.core5.http.nio.AsyncRequestProducer;
import org.apache.hc.core5.http.nio.AsyncResponseConsumer;
import org.apache.hc.core5.http.nio.HandlerFactory;
import org.apache.hc.core5.http.protocol.HttpContext;
import org.apache.hc.core5.io.CloseMode;
import org.apache.hc.core5.reactor.IOReactorStatus;
import org.apache.hc.core5.util.TimeValue;
import org.apache.hc.core5.util.Timeout;

public class HttpUtil {
    NyatLibLogger Logger = new NyatLibLogger();
    public String get(String url) {
        String result = null;
        try {
            Response response = Request.get(url).responseTimeout(Timeout.ofSeconds(3)).execute();
            result = response.returnContent().asString();
        } catch (Exception e) {
            Logger.logERROR(e.toString());
        }
        return result;
    }

}