package it.polimi.deib.deepse.a3e.client;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import org.opencv.android.OpenCVLoader;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.dnn.Dnn;
import org.opencv.dnn.Net;


import java.io.IOException;
import java.io.InputStream;

import it.polimi.deib.deepse.a3e.middleware.resolvers.means.JavaInvocationMean;

/**
 * Created by giovanniquattrocchi on 04/11/17.
 */

public class ProdCode implements JavaInvocationMean.Runnable<String> {

    static {

        if(!OpenCVLoader.initDebug()){
            Log.d("OPENCV", "OpenCV not loaded");
        } else {
            Log.d("OPENCV", "OpenCV loaded");
        }
    };

    private static final String[] classNames = {"background",
            "aeroplane", "bicycle", "bird", "boat",
            "bottle", "bus", "car", "cat", "chair",
            "cow", "diningtable", "dog", "horse",
            "motorbike", "person", "pottedplant",
            "sheep", "sofa", "train", "tvmonitor"};

    private Context context;

    public ProdCode(Context context){
        this.context = context;
    }

    @Override
    public String run(String imgPath) {

        final int IN_WIDTH = 300;
        final int IN_HEIGHT = 300;
        final float WH_RATIO = (float)IN_WIDTH / IN_HEIGHT;
        final double IN_SCALE_FACTOR = 0.007843;
        final double MEAN_VAL = 127.5;

        Mat img1 = new Mat();
        AssetManager assetManager = context.getAssets();
        InputStream istr = null;

        try {
            istr = assetManager.open(imgPath);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Bitmap bitmap = BitmapFactory.decodeStream(istr);
        org.opencv.android.Utils.bitmapToMat(bitmap, img1);

        Net net = Utils.getNet(context);

        net.setInput(Dnn.blobFromImage(img1, IN_SCALE_FACTOR,
                new Size(IN_WIDTH, IN_HEIGHT),
                new Scalar(MEAN_VAL, MEAN_VAL, MEAN_VAL), false, false));

        Mat detections = net.forward();

        int cols = img1.cols();
        int rows = img1.rows();

        Size cropSize;
        if ((float)cols / rows > WH_RATIO) {
            cropSize = new Size(rows * WH_RATIO, rows);
        } else {
            cropSize = new Size(cols, cols / WH_RATIO);
        }

        int y1 = (int)(rows - cropSize.height) / 2;
        int y2 = (int)(y1 + cropSize.height);
        int x1 = (int)(cols - cropSize.width) / 2;
        int x2 = (int)(x1 + cropSize.width);

        detections = detections.reshape(1, (int)detections.total() / 7);

        for (int i = 0; i < detections.rows(); ++i) {
            double confidence = detections.get(i, 2)[0];
            int classId = (int)detections.get(i, 1)[0];
            return classNames[classId] + ": " + confidence;
        }

        return "Unknown";
    }
}
