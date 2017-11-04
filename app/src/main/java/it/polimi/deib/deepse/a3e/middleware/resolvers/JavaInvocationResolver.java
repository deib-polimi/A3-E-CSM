package it.polimi.deib.deepse.a3e.middleware.resolvers;


import it.polimi.deib.deepse.a3e.middleware.core.A3EFunction;
import it.polimi.deib.deepse.a3e.middleware.resolvers.means.JavaInvocationMean;

/**
 * Created by giovanniquattrocchi on 04/11/17.
 */

public class JavaInvocationResolver<T> implements InvocationResolver {


    @Override
    public <T> A3EFunction.FunctionResult invoke(A3EFunction<T> function, T payload) {

        JavaInvocationMean<T> mean = new JavaInvocationMean<>(payload);
        mean.accept(function);
        return new A3EFunction.FunctionResult(mean.getRunnable().run(payload));
    }
}
