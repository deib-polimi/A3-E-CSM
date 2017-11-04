package it.polimi.deib.deepse.a3e.middleware.resolvers;

import it.polimi.deib.deepse.a3e.middleware.core.A3EFunction;
import it.polimi.deib.deepse.a3e.middleware.resolvers.InvocationResolver;

/**
 * Created by giovanniquattrocchi on 04/11/17.
 */

public class LocalnvocationResolver implements InvocationResolver {
    @Override
    public <T> A3EFunction.FunctionResult invoke(A3EFunction<T> function, T payload) {
        return function.getLocalInvocationResolver().invoke(function, payload);
    }
}
