package it.polimi.deib.deepse.a3e.middleware.core;

import java.util.ArrayList;
import java.util.List;

import it.polimi.deib.deepse.a3e.middleware.domains.Domain;
import it.polimi.deib.deepse.a3e.middleware.domains.ErrorDomain;
import it.polimi.deib.deepse.a3e.middleware.utils.A3ELog;

/**
 * Created by giovanniquattrocchi on 30/10/17.
 */

public class DomainSelector {

    private static final float BASE = 5.0f;

    protected Domain selectDomainForRequirements(A3EFunction function, List<Domain> domains){

        Domain res = null;
        float maxLatency = 0;
        float maxComputation = 0;
        float maxScore = 0;

        List<Domain> candidates = new ArrayList<>();

        for(Domain domain : domains){
            if(function.getLocationRequirements().contains(domain.getLocationRequirement())) {
                candidates.add(domain);
                if(domain.getLatency() > maxLatency)
                    maxLatency = domain.getLatency();
                if(domain.getComputationPower() > maxComputation)
                    maxComputation = domain.getComputationPower();
            }
        }

        for(Domain candidate : candidates){
            int latencyWeight = function.getLatencyRequirement().value;
            int computationWeight = function.getComputationRequirement().value;

            float latency = candidate.getLatency();
            float computationPower = candidate.getComputationPower();

            float latencyScore = latencyWeight*((BASE-1)*(1.0f - latency/maxLatency)+1.0f);
            float computationScore = computationWeight*(BASE)*computationPower/maxComputation;

            float score = (latencyScore + computationScore) / (latencyWeight + computationWeight);

            if (score > maxScore){
                maxScore = score;
                res = candidate;
            }

        }

        if (res == null)
            res = ErrorDomain.errorDomain();

        A3ELog.append("*Domain Selection*", "host: "+res+" for function: "+function.getUniqueName());

        return res;
    }



}
