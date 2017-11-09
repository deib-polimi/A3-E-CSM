package it.polimi.deib.deepse.a3e.client;

import android.content.Context;

import com.amazonaws.services.lambda.model.InvokeRequest;

import org.apache.commons.io.IOUtils;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.nio.ByteBuffer;

import it.polimi.deib.deepse.a3e.R;
import it.polimi.deib.deepse.a3e.middleware.core.A3EJSFunction;
import it.polimi.deib.deepse.a3e.middleware.core.LocationRequirement;
import it.polimi.deib.deepse.a3e.middleware.resolvers.JavaScriptInvocationResolver;
import it.polimi.deib.deepse.a3e.middleware.resolvers.means.AWSInvocationMean;
import it.polimi.deib.deepse.a3e.middleware.resolvers.means.RestInvocationMean;

/**
 * Created by giovanniquattrocchi on 03/11/17.
 */

public class PingFunction extends A3EJSFunction<String> {

    public PingFunction(Context context) {
        super(context, "ping", R.raw.ping, LocationRequirement.LOCAL, LocationRequirement.CLOUD);
        setLocalInvocationResolver(new JavaScriptInvocationResolver());
    }

    @Override
    public void visit(RestInvocationMean<String> mean) {
        HttpURLConnection connection = mean.getConnection();
        try {
            connection.setRequestMethod("GET");
        } catch (ProtocolException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void visit(AWSInvocationMean<String> mean) {
        InvokeRequest request = mean.getRequest();
        String payload = mean.getPayload();
        request.withFunctionName(getUniqueName()).withPayload(ByteBuffer.wrap(payload.getBytes()));
    }

}
