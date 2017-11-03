package it.polimi.deib.deepse.a3e.middleware.resolvers.means;

import org.liquidplayer.webkit.javascriptcore.JSContext;

/**
 * Created by giovanniquattrocchi on 03/11/17.
 */

public class JSInvocationMean<T> extends InvocationMean<T> {

    private JSContext context;
    private String script;

    public JSInvocationMean(T payload, JSContext context) {
        super(payload);
        this.context = context;
    }

    public JSContext getContext() {
        return context;
    }

    public void setScript(String script) {
        this.script = script;
    }

    public String getScript() {
        return script;
    }

    @Override
    public void accept(InvocationMeanVisitor visitor) {
        visitor.visit(this);
    }
}
