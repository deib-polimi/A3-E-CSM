package it.polimi.deib.deepse.a3e.middleware.core;

import android.app.Activity;

/**
 * Created by giovanniquattrocchi on 30/10/17.
 */

public interface A3E {

    public void registerFunction(A3EFunction function);

    public void executeFunction(Activity activity, A3EFunction function, String payload, A3EFunction.Callback callback);
}


