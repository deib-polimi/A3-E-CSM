package it.polimi.deib.deepse.a3e.middleware.core;

import android.app.Activity;
import android.content.Context;

import it.polimi.deib.deepse.a3e.middleware.domains.Domain;
import it.polimi.deib.deepse.a3e.middleware.utils.A3ELog;

/**
 * Created by giovanniquattrocchi on 30/10/17.
 */

public class A3EFacade implements A3E {

    private DomainManager manager;
    private DomainSelector selector = new DomainSelector();

    public A3EFacade(Context context){
        manager = new DomainManager(context);
    }

    @Override
    public void registerFunction(A3EFunction function) {
        manager.registerFunction(function);
    }

    @Override
    public void executeFunction(Activity activity, A3EFunction function, String payload, A3EFunction.Callback callback) {
        payload = payload.replace("'", "\"");
        Domain domain = selector.selectDomainForRequirements(function,  manager.getAvailableDomainsForFunction(function));
        domain.notifySelection(function);
        domain.executeFunction(activity, function, payload, callback);
    }
}
