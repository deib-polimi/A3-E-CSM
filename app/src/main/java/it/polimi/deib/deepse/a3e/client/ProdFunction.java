package it.polimi.deib.deepse.a3e.client;

import android.content.Context;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;

import it.polimi.deib.deepse.a3e.R;
import it.polimi.deib.deepse.a3e.middleware.core.A3EFunction;
import it.polimi.deib.deepse.a3e.middleware.core.A3EJSFunction;
import it.polimi.deib.deepse.a3e.middleware.core.LocationRequirement;
import it.polimi.deib.deepse.a3e.middleware.resolvers.JavaInvocationResolver;
import it.polimi.deib.deepse.a3e.middleware.resolvers.means.JSInvocationMean;
import it.polimi.deib.deepse.a3e.middleware.resolvers.means.JavaInvocationMean;
import it.polimi.deib.deepse.a3e.middleware.resolvers.means.RestInvocationMean;

/**
 * Created by giovanniquattrocchi on 03/11/17.
 */

public class ProdFunction extends A3EFunction<String> {

    public ProdFunction(Context context) {
        super(context, "prod",  LocationRequirement.CLOUD, LocationRequirement.EDGE, LocationRequirement.LOCAL);
        setLocalInvocationResolver(new JavaInvocationResolver());
    }

    @Override
    public void visit(RestInvocationMean<String> mean) {

        try {
            HttpURLConnection connection = mean.getConnection();
            InputStream file = getContext().getAssets().open(mean.getPayload());
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "image/jpeg");
            connection.setDoInput(true);
            IOUtils.copy(file, connection.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void visit(JavaInvocationMean<String> mean) {
        mean.setRunnable(new ProdCode(getContext()));
    }

}
