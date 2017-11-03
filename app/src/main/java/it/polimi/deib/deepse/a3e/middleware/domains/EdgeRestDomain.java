package it.polimi.deib.deepse.a3e.middleware.domains;

import it.polimi.deib.deepse.a3e.middleware.core.Requirement;

/**
 * Created by giovanniquattrocchi on 03/11/17.
 */

public class EdgeRestDomain extends RestDomain {
    protected EdgeRestDomain(String ip, String host) {
        super(ip, host, Requirement.EVERYWHERE, Requirement.EDGE, Requirement.LOW_LATENCY, Requirement.FAST_COMPUTATION);
    }
}
