package it.polimi.deib.deepse.a3e.middleware.domains;

import it.polimi.deib.deepse.a3e.middleware.resolvers.JavaScriptInvocationResolver;
import it.polimi.deib.deepse.a3e.middleware.core.LocationRequirement;
import it.polimi.deib.deepse.a3e.middleware.core.A3EFunction;
import it.polimi.deib.deepse.a3e.middleware.resolvers.LocalnvocationResolver;

/**
 * Created by giovanniquattrocchi on 30/10/17.
 */

public class LocalDomain extends Domain {


    public LocalDomain() {
        super("127.0.0.1", "localhost", new LocalnvocationResolver(),
                LocationRequirement.LOCAL);
        this.latency = 1;
    }


    @Override
    public boolean ping() {
        return true;
    }

    @Override
    public long getComputationPower() {
        return 1;
    }

    @Override
    public void notifySelection(A3EFunction function) {
    }
}
