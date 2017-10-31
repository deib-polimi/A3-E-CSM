package it.polimi.deib.deepse.a3e.middleware.domains;

import it.polimi.deib.deepse.a3e.middleware.resolvers.LocalJSInvocationResolver;
import it.polimi.deib.deepse.a3e.middleware.core.Requirement;
import it.polimi.deib.deepse.a3e.middleware.core.A3EFunction;

/**
 * Created by giovanniquattrocchi on 30/10/17.
 */

public class LocalDomain extends Domain {


    public LocalDomain() {
        super("localhost", new LocalJSInvocationResolver(),
                Requirement.NONE, Requirement.SMALL_LATENCY);
    }


    @Override
    public boolean ping() {
        return true;
    }

    @Override
    public void notifySelection(A3EFunction function) {

    }
}
