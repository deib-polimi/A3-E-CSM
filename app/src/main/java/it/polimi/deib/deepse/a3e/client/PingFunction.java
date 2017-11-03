package it.polimi.deib.deepse.a3e.client;

import android.content.Context;

import com.amazonaws.services.lambda.model.InvokeRequest;

import java.nio.ByteBuffer;

import it.polimi.deib.deepse.a3e.R;
import it.polimi.deib.deepse.a3e.middleware.core.A3EJSFunction;
import it.polimi.deib.deepse.a3e.middleware.core.Requirement;
import it.polimi.deib.deepse.a3e.middleware.resolvers.means.AWSInvocationMean;
import it.polimi.deib.deepse.a3e.middleware.resolvers.means.JSInvocationMean;
import it.polimi.deib.deepse.a3e.middleware.resolvers.means.RestInvocationMean;

/**
 * Created by giovanniquattrocchi on 03/11/17.
 */

public class PingFunction extends A3EJSFunction<String> {

    public PingFunction(Context context) {
        super(context, "ping", R.raw.ping, Requirement.EVERYWHERE, Requirement.CLOUD);
    }



    @Override
    public void visit(AWSInvocationMean<String> mean) {
        InvokeRequest request = mean.getRequest();
        String payload = mean.getPayload();
        request.withFunctionName(getUniqueName()).withPayload(ByteBuffer.wrap(payload.getBytes()));
    }

}
