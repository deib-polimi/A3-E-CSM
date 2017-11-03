package it.polimi.deib.deepse.a3e.middleware.resolvers;

import it.polimi.deib.deepse.a3e.middleware.core.A3EFunction;

/**
 * Created by giovanniquattrocchi on 30/10/17.
 */

public interface InvocationResolver {

    public <T> A3EFunction.FunctionResult invoke(A3EFunction<T> function, T payload);

}
