package it.polimi.deib.deepse.a3e.middleware.domains;

import android.content.Context;

import it.polimi.deib.deepse.a3e.middleware.core.A3EFunction;
import it.polimi.deib.deepse.a3e.middleware.core.Requirement;
import it.polimi.deib.deepse.a3e.middleware.resolvers.AWSLambdaInvocationResolver;
import it.polimi.deib.deepse.a3e.middleware.utils.Commons;

/**
 * Created by giovanniquattrocchi on 31/10/17.
 */

public class AWSDomain extends Domain {

    private A3EFunction pingFunction = new A3EFunction("ping", null);

    public AWSDomain(Context context) {
        super("72.21.214.144", new AWSLambdaInvocationResolver(context), Requirement.EVERYWHERE, Requirement.CLOUD, Requirement.FAST_COMPUTATION);
    }

    @Override
    public boolean ping() {
       return Commons.ping(getHost());
    }

    @Override
    public void notifySelection(A3EFunction function) {
        return;
    }
}
