package it.polimi.deib.deepse.a3e.middleware.core;

import android.content.Context;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by giovanniquattrocchi on 30/10/17.
 */

public class A3EFunction {

    private String uniqueName;
    private String code;
    private Set<Requirement> requirements;

    public A3EFunction(String uniqueName, String code, Requirement... requirements){
        this.uniqueName = uniqueName;
        this.code = code;
        this.requirements = new HashSet<>(Arrays.asList(requirements));
    }



    public String getCode() {
        return code;
    }

    protected void setCode(String code){
        this.code = code;
    }

    public String getUniqueName() {
        return uniqueName;
    }

    public Set<Requirement> getRequirements() {
        return new HashSet<>(requirements);
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
