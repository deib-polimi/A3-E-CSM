package it.polimi.deib.deepse.a3e.client;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.Environment;
import android.util.Log;

import org.opencv.dnn.Dnn;
import org.opencv.dnn.Net;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by giovanniquattrocchi on 03/11/17.
 */

public class Utils {

    public static void writeToFile(File file, String data) {
        try {
            FileOutputStream outputStreamWriter = new FileOutputStream(file , true);
            outputStreamWriter.write(data.getBytes());
            outputStreamWriter.close();
        }
        catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }

    public static File createFile(String fileName) {
        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root + "");
        myDir.mkdirs();

        File file = new File (myDir, fileName);

        return file;
    }

    public static String getPath(String file, Context context) {
        AssetManager assetManager = context.getAssets();

        BufferedInputStream inputStream = null;
        try {
            // Read data from assets.
            inputStream = new BufferedInputStream(assetManager.open(file));
            byte[] data = new byte[inputStream.available()];
            inputStream.read(data);
            inputStream.close();

            // Create copy file in storage.
            File outFile = new File(context.getFilesDir(), file);
            FileOutputStream os = new FileOutputStream(outFile);
            os.write(data);
            os.close();
            // Return a path to file which may be read in common way.
            return outFile.getAbsolutePath();
        } catch (IOException ex) {
            Log.i("Error", "Failed to upload a file");
        }
        return "";
    }

    private static Net net = null;

    public static Net getNet(Context context){
        if(net == null)
            net = Dnn.readNetFromCaffe(Utils.getPath("MobileNetSSD_deploy.prototxt.txt", context),
                Utils.getPath("MobileNetSSD_deploy.caffemodel", context));

        return net;
    }


}
