package com.example.whothisperson;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.rekognition.AmazonRekognitionClient;
import com.amazonaws.services.rekognition.model.Celebrity;
import com.amazonaws.services.rekognition.model.Image;
import com.amazonaws.services.rekognition.model.RecognizeCelebritiesRequest;
import com.amazonaws.services.rekognition.model.RecognizeCelebritiesResult;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.downey);

        new processCelebrityTask().execute(bitmap);
    }

    public Image prepareImage( ByteBuffer bytes) {
        Image image = new Image();
        image.withBytes(bytes);
        Log.d("imageINFO",image.getBytes().toString());
        return image;
    }

    private void printCelebInfo(RecognizeCelebritiesResult result) {
        List<Celebrity> list = result.getCelebrityFaces();
        for (Celebrity celebrity : list) {
            Log.d("Celeb_Info", celebrity.getName());
        }
    }

    private class processCelebrityTask extends AsyncTask<Bitmap, Integer, RecognizeCelebritiesResult> {
        protected RecognizeCelebritiesResult doInBackground(Bitmap... bitmaps) {

            ByteArrayOutputStream file = new ByteArrayOutputStream();
            bitmaps[0].compress(Bitmap.CompressFormat.PNG, 100, file);
            ByteBuffer bytebuffer = ByteBuffer.wrap(file.toByteArray());

            BasicAWSCredentials credentials = new BasicAWSCredentials("AKIATQW2OYGQIU4YAEVQ", "rYENZIX3e9gbVCDhpJrMEg2fRr1HPp0YKRTIqTSU");
            AmazonRekognitionClient client = new AmazonRekognitionClient(credentials);
            RecognizeCelebritiesRequest request = new RecognizeCelebritiesRequest();


            Image requestImage = prepareImage(bytebuffer);
            request.setImage(requestImage);


            RecognizeCelebritiesResult celebResult = client.recognizeCelebrities(request);
            Log.d("SUCCESS", "The query was a success");
            return celebResult;
        }

        protected void onPostExecute(RecognizeCelebritiesResult result) {
            printCelebInfo(result);
        }
    }

}