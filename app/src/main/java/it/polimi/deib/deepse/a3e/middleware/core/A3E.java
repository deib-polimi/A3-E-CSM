package it.polimi.deib.deepse.a3e.middleware.core;

import android.app.Activity;

import it.polimi.deib.deepse.a3e.middleware.domains.Domain;

/**
 * Created by giovanniquattrocchi on 30/10/17.
 */

public interface A3E {

    public void start();

    public void registerDomain(Domain function);

    public void unRegisterDomain(Domain function);

    public void registerFunction(A3EFunction function);

    public <T> void executeFunction(Activity activity, A3EFunction<T> function, T payload, A3EFunction.Callback callback);

    public void quit();

}


