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

import javax.net.ssl.HttpsURLConnection;

import it.polimi.deib.deepse.a3e.middleware.core.A3EFunction;
import it.polimi.deib.deepse.a3e.middleware.resolvers.means.RestInvocationMean;

/**
 * Created by giovanniquattrocchi on 03/11/17.
 */

public class RESTInvocationResolver implements InvocationResolver {

    private String host;
    private boolean isHttps;
    public RESTInvocationResolver(String host){
        this.host = host;
        if (host.contains("https"))
            isHttps = true;
        else
            isHttps = false;
    }

    @Override
    public <T> A3EFunction.FunctionResult invoke(A3EFunction<T> function, T payload) {
        try {
            URL u = new URL(host+function.getUniqueName());
            HttpURLConnection connection = (HttpURLConnection) u.openConnection();
            RestInvocationMean<T> mean = new RestInvocationMean<>(payload, connection);
            mean.accept(function);
            int status = connection.getResponseCode();
            String res = IOUtils.toString(connection.getInputStream(), Charset.forName("UTF-8"));
            return new A3EFunction.FunctionResult(res);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new A3EFunction.FunctionResult();
    }
}
