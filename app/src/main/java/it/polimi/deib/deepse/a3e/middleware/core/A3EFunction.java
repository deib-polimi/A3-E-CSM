package it.polimi.deib.deepse.a3e.middleware.core;

import android.content.Context;
import android.content.res.Resources;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import it.polimi.deib.deepse.a3e.middleware.resolvers.means.AWSInvocationMean;
import it.polimi.deib.deepse.a3e.middleware.resolvers.means.InvocationMeanVisitor;
import it.polimi.deib.deepse.a3e.middleware.resolvers.means.JSInvocationMean;
import it.polimi.deib.deepse.a3e.middleware.resolvers.means.RestInvocationMean;


/**
 * Created by giovanniquattrocchi on 30/10/17.
 */

public abstract class A3EFunction<T> implements InvocationMeanVisitor<T> {

    private Context context;
    private String uniqueName;
    private Set<Requirement> requirements;


    public A3EFunction(Context context, String uniqueName, Requirement... requirements){
        this.uniqueName = uniqueName;
        this.requirements = new HashSet<>(Arrays.asList(requirements));
        this.context = context;
    }

    protected void setCurrentContext(Context context){
        this.context = context;
    }

    public Context getContext() {
        return context;
    }

    public String getUniqueName() {
        return uniqueName;
    }

    public Set<Requirement> getRequirements() {
        return new HashSet<>(requirements);
    }

    @Override
    public void visit(RestInvocationMean<T> mean) {
        throw new RuntimeException("Not Supported");
    }

    @Override
    public void visit(AWSInvocationMean<T> mean) {
        throw new RuntimeException("Not Supported");
    }

    @Override
    public void visit(JSInvocationMean<T> mean) {
        throw new RuntimeException("Not Supported");
    }

    public interface Callback {
        public abstract void onFunctionResult(FunctionResult result);
    }

    public static class FunctionResult {
        private String value;
        private boolean success;

        public FunctionResult(){
            value = null;
            success = false;
        }

        public FunctionResult(String value){
            this.value = value;
            success = true;
        }

        public String getStringResult() {
            if (success)
                return value;
            else
                return null;
        }

        public boolean isSuccess() {
            return success;
        }
    }

    public int hashCode(){
        return uniqueName.hashCode();
    }

    public boolean equals(Object obj){
        if (!(obj instanceof A3EFunction))
            return false;

        A3EFunction function = (A3EFunction) obj;

        return uniqueName.equals(function.uniqueName);
    }

}
