package it.polimi.deib.deepse.a3e.middleware.resolvers;

import android.graphics.Bitmap;
import android.util.Base64;


import org.apache.commons.io.IOUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import javax.net.ssl.HttpsURLConnection;

import it.polimi.deib.deepse.a3e.middleware.core.A3EFunction;
import it.polimi.deib.deepse.a3e.middleware.resolvers.means.RestInvocationMean;

/**
 * Created by giovanniquattrocchi on 03/11/17.
 */

public class RESTInvocationResolver implements InvocationResolver {

    public int TIMEOUT = 3;
    private String host;

    public RESTInvocationResolver(String host){
        this.host = host;
    }

    @Override
    public <T> A3EFunction.FunctionResult invoke(final A3EFunction<T> function, final T payload) {

            ExecutorService executor = Executors.newSingleThreadExecutor();
            Future<A3EFunction.FunctionResult> future = executor.submit(new Callable<A3EFunction.FunctionResult>() {
                @Override
                public A3EFunction.FunctionResult call() throws Exception {
                    try {
                        URL u = new URL(host+function.getUniqueName());
                        HttpURLConnection connection = (HttpURLConnection) u.openConnection();
                        connection.setConnectTimeout(TIMEOUT*1000);
                        connection.setReadTimeout(TIMEOUT*1000);
                        RestInvocationMean<T> mean = new RestInvocationMean<>(payload, connection);
                        mean.accept(function);
                        int status = connection.getResponseCode();
                        String res = IOUtils.toString(connection.getInputStream(), Charset.forName("UTF-8"));
                        return new A3EFunction.FunctionResult(res);
                    }
                    catch (Exception e) {
                       return new A3EFunction.FunctionResult();
                    }
                }});

        try {
            A3EFunction.FunctionResult out = future.get(TIMEOUT, TimeUnit.SECONDS);
            return out;
        } catch (Exception e) {
            return new A3EFunction.FunctionResult();
        }
    }
}