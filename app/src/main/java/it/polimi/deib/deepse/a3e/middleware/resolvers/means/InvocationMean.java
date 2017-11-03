package it.polimi.deib.deepse.a3e.middleware.resolvers.means;

/**
 * Created by giovanniquattrocchi on 03/11/17.
 */

public abstract class InvocationMean<T> {

    private T payload;

    public InvocationMean(T payload){
        this.payload = payload;
    }

    public abstract void accept(InvocationMeanVisitor<T> visitor);

    public T getPayload() {
        return payload;
    }

    protected void setPayload(T payload) {
        this.payload = payload;
    }
}
