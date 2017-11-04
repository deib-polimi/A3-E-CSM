package it.polimi.deib.deepse.a3e.middleware.resolvers.means;

/**
 * Created by giovanniquattrocchi on 04/11/17.
 */

public class JavaInvocationMean<T> extends InvocationMean  {

    private Runnable<T> runnable;

    public JavaInvocationMean(Object payload) {
        super(payload);
    }

    @Override
    public void accept(InvocationMeanVisitor visitor) {
        visitor.visit(this);
    }

    public Runnable<T> getRunnable() {
        return runnable;
    }

    public void setRunnable(Runnable<T> runnable) {
        this.runnable = runnable;
    }


    public interface Runnable<T> {
        public String run(T payload);
    }
}
