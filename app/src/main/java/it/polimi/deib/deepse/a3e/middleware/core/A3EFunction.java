package it.polimi.deib.deepse.a3e.middleware.core;

import android.content.Context;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import it.polimi.deib.deepse.a3e.middleware.resolvers.InvocationResolver;
import it.polimi.deib.deepse.a3e.middleware.resolvers.means.AWSInvocationMean;
import it.polimi.deib.deepse.a3e.middleware.resolvers.means.InvocationMeanVisitor;
import it.polimi.deib.deepse.a3e.middleware.resolvers.means.JSInvocationMean;
import it.polimi.deib.deepse.a3e.middleware.resolvers.means.JavaInvocationMean;
import it.polimi.deib.deepse.a3e.middleware.resolvers.means.RestInvocationMean;


/**
 * Created by giovanniquattrocchi on 30/10/17.
 */

public abstract class A3EFunction<T> implements InvocationMeanVisitor<T> {

    private Context context;
    private String uniqueName;
    private Set<LocationRequirement> locationRequirements;
    private ComputationRequirement computationRequirement;
    private LatencyRequirement latencyRequirement;
    private InvocationResolver localInvocationResolver;

    public A3EFunction(Context context, String uniqueName, ComputationRequirement computationRequirement,
                       LatencyRequirement latencyRequirement, LocationRequirement... locationRequirements){
        this.uniqueName = uniqueName;
        this.locationRequirements = new HashSet<>(Arrays.asList(locationRequirements));
        this.context = context;
        this.computationRequirement = computationRequirement;
        this.latencyRequirement = latencyRequirement;
    }

    public A3EFunction(Context context, String uniqueName, LocationRequirement... locationRequirements){
        this(context, uniqueName, ComputationRequirement.FAST, LatencyRequirement.LOW, locationRequirements);
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

    public ComputationRequirement getComputationRequirement() {
        return computationRequirement;
    }

    public LatencyRequirement getLatencyRequirement() {
        return latencyRequirement;
    }

    public Set<LocationRequirement> getLocationRequirements() {
        return new HashSet<>(locationRequirements);
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

    @Override
    public void visit(JavaInvocationMean<T> mean) {
        throw new RuntimeException("Not Supported");
    }

    public InvocationResolver getLocalInvocationResolver(){
        return localInvocationResolver;
    }

    protected void setLocalInvocationResolver(InvocationResolver localInvocationResolver){
        this.localInvocationResolver = localInvocationResolver;
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
