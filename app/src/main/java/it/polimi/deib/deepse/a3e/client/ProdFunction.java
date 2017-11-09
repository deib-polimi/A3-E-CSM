package it.polimi.deib.deepse.a3e.client;

import android.content.Context;
import android.util.Base64;

import com.google.gson.JsonObject;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
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
        /*
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
        */

        try {
            HttpURLConnection connection = mean.getConnection();
            InputStream file = getContext().getAssets().open(mean.getPayload());
            byte[] bytes = IOUtils.toByteArray(file);

            String img = Base64.encodeToString(bytes, Base64.DEFAULT);

            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            connection.setRequestProperty("authorization", "Basic MjNiYzQ2YjEtNzFmNi00ZWQ1LThjNTQtODE2YWE0ZjhjNTAyOjEyM3pPM3haQ0xyTU42djJCS0sxZFhZRnBYbFBrY2NPRnFtMTJDZEFzTWdSVTRWck5aOWx5R1ZDR3VNREdJd1A=");
            connection.setRequestProperty("postman-token", "b650346a-9e5f-04ac-60bf-d7cd6f53b885");
            connection.setRequestProperty("cache-control", "no-cache");
            JsonObject body = new JsonObject();
            body.addProperty("msg", "hola");
            body.addProperty("img", img);
            connection.setDoInput(true);

            OutputStream os = connection.getOutputStream();
            os.write(body.toString().getBytes("UTF-8"));
            os.close();
            //IOUtils.copy(file, connection.getOutputStream());

            //connection.setRequestProperty("body", body.toString());

        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void visit(JavaInvocationMean<String> mean) {
        mean.setRunnable(new ProdCode(getContext()));
    }

}
