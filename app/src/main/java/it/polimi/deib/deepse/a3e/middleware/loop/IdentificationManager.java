package it.polimi.deib.deepse.a3e.middleware.loop;

import java.util.ArrayList;
import java.util.List;

import it.polimi.deib.deepse.a3e.middleware.core.A3EFunction;
import it.polimi.deib.deepse.a3e.middleware.domains.Domain;
import it.polimi.deib.deepse.a3e.middleware.utils.A3ELog;

/**
 * Created by Giovanni on 05/11/17.
 */

public class IdentificationManager {

    public List<Domain> identify(A3EFunction function, List<Domain> domains){

        List<Domain> availableDomains = new ArrayList<>();

        for (Domain domain : domains) {
            if (domain.notifyRequirements(function)){
                availableDomains.add(domain);
            }
        }

        A3ELog.append("*Identification*", "available domains: " + availableDomains);


        return availableDomains;
    }

}
