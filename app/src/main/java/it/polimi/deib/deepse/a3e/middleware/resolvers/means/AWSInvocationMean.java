package it.polimi.deib.deepse.a3e.middleware.resolvers.means;

import com.amazonaws.services.lambda.model.InvokeRequest;

/**
 * Created by giovanniquattrocchi on 03/11/17.
 */

public class AWSInvocationMean<T> extends InvocationMean<T> {

    private InvokeRequest request;

    public AWSInvocationMean(T payload, InvokeRequest request) {
        super(payload);
        this.request = request;
    }

    @Override
    public void accept(InvocationMeanVisitor visitor) {
        visitor.visit(this);
    }

    public InvokeRequest getRequest(){
        return request;
    }
}
