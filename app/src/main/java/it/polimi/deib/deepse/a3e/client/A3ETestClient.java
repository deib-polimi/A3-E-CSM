package it.polimi.deib.deepse.a3e.client;

import android.Manifest;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import it.polimi.deib.deepse.a3e.R;
import it.polimi.deib.deepse.a3e.middleware.core.A3E;
import it.polimi.deib.deepse.a3e.middleware.core.A3EFacade;
import it.polimi.deib.deepse.a3e.middleware.core.A3EFunction;
import it.polimi.deib.deepse.a3e.middleware.domains.AWSRestDomain;
import it.polimi.deib.deepse.a3e.middleware.domains.Domain;
import it.polimi.deib.deepse.a3e.middleware.domains.EdgeRestDomain;
import it.polimi.deib.deepse.a3e.middleware.domains.LocalDomain;
import it.polimi.deib.deepse.a3e.middleware.utils.A3ELog;

public class A3ETestClient extends AppCompatActivity implements A3ELog.Listener, A3ETest.Listener {

    private A3E a3e;
    private A3EFunction prodFunction;
    private File logFile;

    Map<Integer, Domain> domains = new HashMap<>();

    private ExecutorService service = Executors.newFixedThreadPool(1);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        requestPermission(this);
        Intent intent = new Intent(this, A3ETestService.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);

        CheckBox box = findViewById(R.id.localCheckBox);
        domains.put(box.getId(), new LocalDomain());
        box = findViewById(R.id.edgeLocalCheckBox);
        domains.put(box.getId(), new EdgeRestDomain("10.0.1.27", "http://10.0.1.27:3002/api/v1/namespaces/guest/actions/"));
        box = findViewById(R.id.mobileEdgeCheckBox);
        domains.put(box.getId(), new EdgeRestDomain("131.175.135.184", "http://131.175.135.184:3002/api/v1/namespaces/guest/actions/"));
        box = findViewById(R.id.cloudCheckBox);
        domains.put(box.getId(), new AWSRestDomain("https://q8i5t5834m.execute-api.us-west-2.amazonaws.com/"));

    }

    public void runTest(View view){
        if (mBound) {
            EditText editText = (EditText) findViewById(R.id.callInterval);
            float tCall = Float.parseFloat(editText.getText().toString());
            editText = (EditText) findViewById(R.id.numCalls);
            int numCalls = Integer.parseInt(editText.getText().toString());
            /*editText = (EditText) findViewById(R.id.phaseInterval);
            int tPhase = Integer.parseInt(editText.getText().toString());
            editText = (EditText) findViewById(R.id.numPhases);
            int numPhases = Integer.parseInt(editText.getText().toString());
            */
            A3ETest.Parameters parameters = new A3ETest.Parameters(tCall, numCalls, 1, 0);

            A3ETest test = new A3ETest(a3e, this, prodFunction, "person.jpg");
            //OverheadTest test = new OverheadTest(a3e, this, prodFunction, "person.jpg");
            test.setParameters(parameters);
            test.addListener(this);

            mService.startTest(test);

            view.setEnabled(false);
            ((Button)view).setText("Running...");
        }
    }

    public void manageCheckbox(View view){
        if(((CheckBox) view).isChecked()){
            a3e.registerDomain(domains.get(view.getId()));
        }
        else {
            a3e.unRegisterDomain(domains.get(view.getId()));
        }
    }

    private void startA3E(){

        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yy.MM.dd.HH.mm.ss");
        logFile = Utils.createFile("experiment-" + sdf.format(date) + ".log");

        A3ELog.addListener(this);
        // create A3E
        a3e = new A3EFacade(this);

        a3e.start();

        // create function
        prodFunction = new ProdFunction(this);
        //pingFunction = new PingFunction(this);
        // register function
        a3e.registerFunction(prodFunction);


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

    @Override
    public void onLogUpdate(final String message) {

        service.execute(new Runnable() {
            @Override
            public void run() {
                Utils.writeToFile(logFile, message+"\n");
            }
        });
    }

    @Override
    public void onTestEnd() {
        finish();
    }

    private A3ETestService mService;
    boolean mBound = false;

    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            A3ETestService.LocalBinder binder = (A3ETestService.LocalBinder) service;
            mService = binder.getService();
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
        }
    };

}
