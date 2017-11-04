package it.polimi.deib.deepse.a3e.middleware.domains;

import it.polimi.deib.deepse.a3e.middleware.core.LocationRequirement;

/**
 * Created by giovanniquattrocchi on 03/11/17.
 */

public class EdgeRestDomain extends RestDomain {
    protected EdgeRestDomain(String ip, String host) {
        super(ip, host, LocationRequirement.EDGE);
    }

    @Override
    public long getComputationPower() {
        return 4;
    }
}
