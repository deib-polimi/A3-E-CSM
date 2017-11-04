package it.polimi.deib.deepse.a3e.middleware.resolvers.means;


/**
 * Created by giovanniquattrocchi on 03/11/17.
 */

public interface InvocationMeanVisitor<T> {

    public void visit(RestInvocationMean<T> mean);
    public void visit(AWSInvocationMean<T> mean);
    public void visit(JSInvocationMean<T> mean);
    public void visit(JavaInvocationMean<T> mean);

}
