package it.polimi.deib.deepse.a3e.middleware.resolvers;

import android.util.Log;

import org.liquidplayer.webkit.javascriptcore.JSContext;
import org.liquidplayer.webkit.javascriptcore.JSFunction;
import org.liquidplayer.webkit.javascriptcore.JSValue;

import it.polimi.deib.deepse.a3e.middleware.core.A3EFunction;
import it.polimi.deib.deepse.a3e.middleware.resolvers.InvocationResolver;
import it.polimi.deib.deepse.a3e.middleware.resolvers.means.JSInvocationMean;

/**
 * Created by giovanniquattrocchi on 30/10/17.
 */

public class JavaScriptInvocationResolver implements InvocationResolver {

    @Override
    public <T> A3EFunction.FunctionResult invoke(A3EFunction<T> function, T payload) {

        JSContext context = new JSContext();
        JSInvocationMean<T> mean = new JSInvocationMean<>(payload, context);
        mean.accept(function);
        JSValue value = context.evaluateScript(mean.getScript());

        String resultValue;
        if (!value.isUndefined() && !value.isNull())
            resultValue = value.toJSON().toString();
        else
            resultValue = value.toString();

        return new A3EFunction.FunctionResult(resultValue);
    }


}
