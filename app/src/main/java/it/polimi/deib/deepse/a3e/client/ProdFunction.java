package it.polimi.deib.deepse.a3e.client;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Base64;

import org.apache.commons.io.IOUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;

import it.polimi.deib.deepse.a3e.R;
import it.polimi.deib.deepse.a3e.middleware.core.A3EJSFunction;
import it.polimi.deib.deepse.a3e.middleware.core.Requirement;
import it.polimi.deib.deepse.a3e.middleware.resolvers.means.AWSInvocationMean;
import it.polimi.deib.deepse.a3e.middleware.resolvers.means.JSInvocationMean;
import it.polimi.deib.deepse.a3e.middleware.resolvers.means.RestInvocationMean;

/**
 * Created by giovanniquattrocchi on 03/11/17.
 */

public class ProdFunction extends A3EJSFunction<String> {

    public ProdFunction(Context context) {
        super(context, "prod", R.raw.prod,  Requirement.CLOUD, Requirement.EDGE, Requirement.EVERYWHERE);
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
    public void visit(JSInvocationMean<String> mean) {
        mean.setScript("("+getCode()+")"+"(\""+mean.getPayload()+"\")");
        System.out.println(mean.getScript());
    }

    /*
    private String getEncoded64ImageStringFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream);
        byte[] byteFormat = stream.toByteArray();
        // get the base 64 string
        String imgString = Base64.encodeToString(byteFormat, Base64.NO_WRAP);

        return imgString;
    }*/
}
