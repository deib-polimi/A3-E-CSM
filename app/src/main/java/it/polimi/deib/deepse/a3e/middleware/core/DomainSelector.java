package it.polimi.deib.deepse.a3e.middleware.core;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import it.polimi.deib.deepse.a3e.middleware.domains.Domain;
import it.polimi.deib.deepse.a3e.middleware.domains.ErrorDomain;
import it.polimi.deib.deepse.a3e.middleware.utils.A3ELog;

/**
 * Created by giovanniquattrocchi on 30/10/17.
 */

public class DomainSelector {


    public Domain selectDomainForRequirements(A3EFunction function, List<Domain> domains){
        int maxMatches = 0;
        int matchIndex = -1;

        for (int i = 0; i < domains.size(); i++){
            Domain domain = domains.get(i);
            Set<Requirement> intersection = new HashSet<>(domain.getRequirements());
            intersection.retainAll(function.getRequirements());
            int matches = intersection.size();
            if(maxMatches < matches){
                maxMatches = intersection.size();
                matchIndex = i;
            }
        }


        Domain domain;
        if (matchIndex >= 0)
            domain = domains.get(matchIndex);
        else
            domain = ErrorDomain.errorDomain();

        A3ELog.append("SELECTED DOMAIN "+domain+" for function "+function.getUniqueName());

        return domain;
    }

}
