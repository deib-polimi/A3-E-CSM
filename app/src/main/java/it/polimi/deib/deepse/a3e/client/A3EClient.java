package it.polimi.deib.deepse.a3e.client;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import it.polimi.deib.deepse.a3e.R;
import it.polimi.deib.deepse.a3e.middleware.core.A3E;
import it.polimi.deib.deepse.a3e.middleware.core.A3EFacade;
import it.polimi.deib.deepse.a3e.middleware.core.A3EFunction;
import it.polimi.deib.deepse.a3e.middleware.core.A3EJSFunction;
import it.polimi.deib.deepse.a3e.middleware.core.Requirement;
import it.polimi.deib.deepse.a3e.middleware.utils.A3ELog;

public class A3EClient extends AppCompatActivity implements A3ELog.Listener {

    private A3E a3e;
    private A3EFunction f1;

    private ExecutorService service = Executors.newFixedThreadPool(1);
    private File logFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_a3_eclient);

        TextView logTextView = (TextView) findViewById(R.id.logTextView);
        logTextView.setMovementMethod(new ScrollingMovementMethod());

        requestPermission(this);

    }

    private void startA3E(){

        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("YY.MM.dd.HH.mm.ss");
        logFile = Utils.createFile("experiment-" + sdf.format(date) + ".log");

        A3ELog.addListener(this);
        // create A3E
        a3e = new A3EFacade(this);
        // create function
        f1 = new ProdFunction(this);
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

        service.execute(new Runnable() {
            @Override
            public void run() {
                Utils.writeToFile(logFile, message+"\n");
            }
        });

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




    private void requestPermission(Activity context) {
        boolean hasPermission = (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
        if (!hasPermission) {
            ActivityCompat.requestPermissions(context,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    1);
        } else {
            startA3E();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            startA3E();
        } else {
            Toast.makeText(this, "The app was not allowed to write to your storage. Hence, it cannot function properly. Please consider granting it this permission", Toast.LENGTH_LONG).show();
        }

    }

}
/*
{
    "name" : "Giovanni",
    "place" : "Cyprus"
}
*/

// {'name' : 'Michele', 'place' : 'Cyprus'}