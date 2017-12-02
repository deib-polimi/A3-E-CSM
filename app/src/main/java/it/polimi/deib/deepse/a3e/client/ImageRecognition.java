package it.polimi.deib.deepse.a3e.client;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.dnn.Dnn;
import org.opencv.dnn.Net;
import org.opencv.imgcodecs.Imgcodecs;

/**
 * Created by giovanniquattrocchi on 28/11/17.
 */

public class ImageRecognition {

    private static final String[] classNames = {"background",
            "aeroplane", "bicycle", "bird", "boat",
            "bottle", "bus", "car", "cat", "chair",
            "cow", "diningtable", "dog", "horse",
            "motorbike", "person", "pottedplant",
            "sheep", "sofa", "train", "tvmonitor"};

    private static final Net net = Dnn.readNetFromCaffe("MobileNetSSD_deploy.prototxt.txt",
            "MobileNetSSD_deploy.caffemodel");


    final static int IN_WIDTH = 300;
    final static int IN_HEIGHT = 300;
    final static float WH_RATIO = (float)IN_WIDTH / IN_HEIGHT;
    final static double IN_SCALE_FACTOR = 0.007843;
    final static double MEAN_VAL = 127.5;


    public static JsonObject main(JsonObject args) {

        byte[] bytes = args.getAsJsonPrimitive("image").getAsString().getBytes();

        Mat img1 = Imgcodecs.imdecode(new MatOfByte(bytes), Imgcodecs.CV_LOAD_IMAGE_UNCHANGED);

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

        return toJson(detections);
    }

    private static JsonObject toJson(Mat detections){
        JsonObject json = new JsonObject();
        JsonArray features = new JsonArray();
        json.add("Labels", features);

        for (int i = 0; i < detections.rows(); ++i) {
            double confidence = detections.get(i, 2)[0];
            int classId = (int)detections.get(i, 1)[0];

            String label = classNames[classId].substring(0, 1).toUpperCase() + classNames[classId].substring(1);

            JsonObject feature = new JsonObject();
            feature.addProperty("Name", label);
            feature.addProperty("Confidence", confidence*100);
            features.add(feature);
        }
        return json;
    }


}

