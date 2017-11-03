package it.polimi.deib.deepse.a3e.middleware.core;

import android.content.Context;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import it.polimi.deib.deepse.a3e.middleware.resolvers.means.JSInvocationMean;


/**
 * Created by giovanniquattrocchi on 31/10/17.
 */

public abstract class A3EJSFunction<T> extends A3EFunction<T> {

    private String code;

    public A3EJSFunction(final Context context, String uniqueName, final int resId, Requirement... requirements) {
        super(context, uniqueName, requirements);
        this.code = readRawTextFile(context, resId);
    }

    private static String readRawTextFile(Context ctx, int resId)
    {
        InputStream inputStream = ctx.getResources().openRawResource(resId);

        InputStreamReader inputreader = new InputStreamReader(inputStream);
        BufferedReader buffreader = new BufferedReader(inputreader);
        String line;
        StringBuilder text = new StringBuilder();

        try {
            while (( line = buffreader.readLine()) != null) {
                text.append(line);
                text.append('\n');
            }
        } catch (IOException e) {
            return null;
        }
        return text.toString();
    }

    protected String getCode() {
        return code;
    }

    @Override
    public void visit(JSInvocationMean<T> mean) {
        mean.setScript("("+code+")"+"("+mean.getPayload()+")");
    }
}
