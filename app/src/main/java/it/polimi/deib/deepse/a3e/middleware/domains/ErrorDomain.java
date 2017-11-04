package it.polimi.deib.deepse.a3e.middleware.domains;

import it.polimi.deib.deepse.a3e.middleware.core.LocationRequirement;
import it.polimi.deib.deepse.a3e.middleware.resolvers.InvocationResolver;
import it.polimi.deib.deepse.a3e.middleware.core.A3EFunction;

/**
 * Created by giovanniquattrocchi on 31/10/17.
 */

public class ErrorDomain extends Domain {

    private static ErrorDomain instance;

    private ErrorDomain() {
        super("error", "error_domain", new ErrorResolver(), LocationRequirement.LOCAL);
    }

    public static ErrorDomain errorDomain(){
        if (instance == null)
            instance = new ErrorDomain();
        return instance;
    }

    @Override
    public boolean ping() {
        return false;
    }

    @Override
    public long getComputationPower() {
        return -1;
    }

    @Override
    public void notifySelection(A3EFunction function) {

    }

    public static class ErrorResolver implements InvocationResolver {

        @Override
        public <T> A3EFunction.FunctionResult invoke(A3EFunction<T> function, T payload) {
            return new A3EFunction.FunctionResult();
        }
    }
}
