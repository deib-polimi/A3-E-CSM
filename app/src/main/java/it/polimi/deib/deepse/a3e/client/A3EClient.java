package it.polimi.deib.deepse.a3e.client;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import it.polimi.deib.deepse.a3e.R;
import it.polimi.deib.deepse.a3e.middleware.core.A3E;
import it.polimi.deib.deepse.a3e.middleware.core.A3EFacade;
import it.polimi.deib.deepse.a3e.middleware.core.A3EFunction;
import it.polimi.deib.deepse.a3e.middleware.core.A3EFileFunction;
import it.polimi.deib.deepse.a3e.middleware.core.Requirement;
import it.polimi.deib.deepse.a3e.middleware.utils.A3ELog;

public class A3EClient extends AppCompatActivity implements A3ELog.Listener {

    private A3E a3e;
    A3EFunction f1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_a3_eclient);

        TextView logTextView = (TextView) findViewById(R.id.logTextView);
        logTextView.setMovementMethod(new ScrollingMovementMethod());

        // subscribe to the log
        A3ELog.addListener(this);
        // create A3E
        a3e = new A3EFacade(this);
        // create function (from file)
        f1 =  new A3EFileFunction(this, "ping", R.raw.ping, Requirement.NONE, Requirement.CLOUD);
        // register function
        a3e.registerFunction(f1);

    }

    public void execute(final View view){

        final EditText payloadEditText  = (EditText) findViewById(R.id.payloadEditText);
        String payload =  payloadEditText.getText().toString();

        a3e.executeFunction(this, f1, payload, new A3EFunction.Callback() {
            @Override
            public void onFunctionResult(final A3EFunction.FunctionResult result) {
                TextView textView = (TextView) A3EClient.this.findViewById(R.id.resultTextView);
                if (result.isSuccess()) {
                    textView.setText(result.getStringResult());
                }
                else {
                    textView.setText("");
                    Toast.makeText(A3EClient.this, "NO DOMAIN AVAILABLE", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onLogUpdate(final String message) {

        final TextView logTextView = (TextView) findViewById(R.id.logTextView);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                logTextView.append(message + "\n");
                logTextView.post(new Runnable() {
                    @Override
                    public void run() {
                        final int scrollAmount = logTextView.getLayout().getLineTop(logTextView.getLineCount())-logTextView.getHeight();
                        if (scrollAmount > 0)
                            logTextView.scrollTo(0, scrollAmount);
                        else
                            logTextView.scrollTo(0, 0);
                    }
                });
            }
        });
    }
}


/*
{
    "name" : "Giovanni",
    "place" : "Cyprus"
}
*/

// {'name' : 'Michele', 'place' : 'Cyprus'}
