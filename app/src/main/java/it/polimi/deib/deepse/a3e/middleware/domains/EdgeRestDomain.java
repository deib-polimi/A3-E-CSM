package it.polimi.deib.deepse.a3e.middleware.domains;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import it.polimi.deib.deepse.a3e.middleware.core.A3EFunction;
import it.polimi.deib.deepse.a3e.middleware.core.LocationRequirement;
import it.polimi.deib.deepse.a3e.middleware.utils.A3ELog;
import it.polimi.deib.deepse.a3e.middleware.utils.UDPA3EFunctionRequest;
import it.polimi.deib.deepse.a3e.middleware.utils.WorldSimulator;

/**
 * Created by giovanniquattrocchi on 03/11/17.
 */

public class EdgeRestDomain extends RestDomain implements UDPA3EFunctionRequest.Callback {

    private Map<String, Boolean> functions = new HashMap<>();

    public EdgeRestDomain(String ip, String host) {
        super(ip, host, LocationRequirement.EDGE);
    }

    @Override
    public long getComputationPower() {
        return 4;
    }

    @Override
    public boolean ping(){
        return super.ping();
        //return WorldSimulator.getInstance()
         //       .isEdgeAvailable() ? super.ping() : false;
    }

    @Override
    public synchronized boolean notifyRequirements(final A3EFunction function){

        String simpleFunctionName = function.getUniqueName().split("\\?")[0];
        Boolean available = functions.get(simpleFunctionName);

        if (available == null){

            new UDPA3EFunctionRequest(function, getIp(), this);

            functions.put(simpleFunctionName, false);
            available = false;
        }

        return available.booleanValue();
    }


    @Override
    public synchronized void onFunctionAvailable(String functionName) {
        functions.put(functionName, true);
    }
}
