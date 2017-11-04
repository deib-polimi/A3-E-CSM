package it.polimi.deib.deepse.a3e.middleware.domains;

import it.polimi.deib.deepse.a3e.middleware.core.LocationRequirement;

/**
 * Created by giovanniquattrocchi on 03/11/17.
 */

public class AWSRestDomain extends RestDomain {

    public AWSRestDomain(String host) {
        super("72.21.214.144", host, LocationRequirement.CLOUD);
    }


    @Override
    public long getComputationPower() {
        return 5;
    }
}
