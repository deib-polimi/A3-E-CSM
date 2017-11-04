package it.polimi.deib.deepse.a3e.middleware.domains;

import android.content.Context;

import it.polimi.deib.deepse.a3e.middleware.core.A3EFunction;
import it.polimi.deib.deepse.a3e.middleware.core.LocationRequirement;
import it.polimi.deib.deepse.a3e.middleware.resolvers.AWSLambdaInvocationResolver;
import it.polimi.deib.deepse.a3e.middleware.utils.Commons;

/**
 * Created by giovanniquattrocchi on 31/10/17.
 */

public class AWSDomain extends Domain {

    public AWSDomain(Context context) {
        super("72.21.214.144", "aws", new AWSLambdaInvocationResolver(context), LocationRequirement.CLOUD);
    }

    @Override
    public synchronized boolean ping() {
        this.latency = Commons.ping(getHost());
        return this.latency > 0;
    }

    @Override
    public long getComputationPower() {
        return 5;
    }


    @Override
    public void notifySelection(A3EFunction function) {
        return;
    }
}
