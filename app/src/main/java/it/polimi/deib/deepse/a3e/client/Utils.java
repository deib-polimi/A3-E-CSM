package it.polimi.deib.deepse.a3e.client;

import android.os.Environment;
import android.util.Log;

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


}
