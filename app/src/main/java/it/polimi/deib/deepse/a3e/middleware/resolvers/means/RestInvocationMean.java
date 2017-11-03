package it.polimi.deib.deepse.a3e.middleware.resolvers.means;

import java.net.HttpURLConnection;

/**
 * Created by giovanniquattrocchi on 03/11/17.
 */

public class RestInvocationMean<T> extends InvocationMean<T> {

    private HttpURLConnection connection;

    public RestInvocationMean(T payload, HttpURLConnection connection) {
        super(payload);
        this.connection = connection;
    }

    @Override
    public void accept(InvocationMeanVisitor<T> visitor) {
        visitor.visit(this);
    }

    public HttpURLConnection getConnection() {
        return connection;
    }
}
