package it.polimi.deib.deepse.a3e.middleware.resolvers;

import android.util.Log;

import org.liquidplayer.webkit.javascriptcore.JSContext;
import org.liquidplayer.webkit.javascriptcore.JSFunction;
import org.liquidplayer.webkit.javascriptcore.JSValue;

import it.polimi.deib.deepse.a3e.middleware.core.A3EFunction;
import it.polimi.deib.deepse.a3e.middleware.resolvers.InvocationResolver;

/**
 * Created by giovanniquattrocchi on 30/10/17.
 */

public class LocalJSInvocationResolver implements InvocationResolver {

    @Override
    public A3EFunction.FunctionResult invoke(A3EFunction function, String payload) {

        JSContext context = new JSContext();

        JSValue value = context.evaluateScript("("+function.getCode()+")"+"("+payload+")");

        String resultValue;
        if (!value.isUndefined() && !value.isNull())
            resultValue = value.toJSON().toString();
        else
            resultValue = value.toString();

        return new A3EFunction.FunctionResult(resultValue);
    }
}
