package it.polimi.deib.deepse.a3e.middleware.domains;

import android.app.Activity;
import android.content.Context;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import it.polimi.deib.deepse.a3e.middleware.resolvers.InvocationResolver;
import it.polimi.deib.deepse.a3e.middleware.core.Requirement;
import it.polimi.deib.deepse.a3e.middleware.core.A3EFunction;
import it.polimi.deib.deepse.a3e.middleware.utils.Commons;

/**
 * Created by giovanniquattrocchi on 30/10/17.
 */

public abstract class Domain {

    private ExecutorService executorService = Executors.newFixedThreadPool(Commons.NUM_THREAD_PER_DOMAIN);
    private Set<Requirement> requirements;
    private String host;
    private InvocationResolver invocationResolver;

    protected Domain(String host, InvocationResolver resolver, Requirement... requirements){
        this.host = host;
        this.invocationResolver = resolver;
        this.requirements = new HashSet<>(Arrays.asList(requirements));
    }

    public void executeFunction(final Activity activity, final A3EFunction function, final String payload, final A3EFunction.Callback callback){
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                final A3EFunction.FunctionResult result = invocationResolver.invoke(function, payload);
                activity.runOnUiThread(
                        new Runnable() {
                            @Override
                            public void run() {
                                callback.onFunctionResult(result);
                            }
                        }
                );
            }
        });
    }

    public Set<Requirement>  getRequirements(){
        return new HashSet<>(requirements);
    }

    public InvocationResolver getInvocationResolver(){
        return invocationResolver;
    }

    public String getHost(){
        return host;
    }

    public abstract boolean ping();

    public boolean notifyRequirements(A3EFunction function){
        Set<Requirement> intersection = new HashSet<>(function.getRequirements());
        intersection.retainAll(requirements);
        return intersection.size() > 0;
    }

    public abstract void notifySelection(A3EFunction function);

    public String toString(){
        return host;
    }

}
