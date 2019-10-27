package com.example.whothisperson;

import android.graphics.Bitmap;
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
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.List;
import android.content.Intent;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class MainActivity extends AppCompatActivity {
    private ImageView imageview;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    //Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.imageview = this.findViewById(R.id.celebPic);
        Button photoButton = this.findViewById(R.id.shutterButton);
        photoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imageview != null) {
                    //startActivity(new Intent(MainActivity.this, ));
                }
                dispatchTakePictureIntent();
                Toast toast = Toast.makeText(getApplicationContext(),"There is response",(int)10);
                toast.show();
                Log.d("OnClick", "Entered");
            }
        });
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
            new celebrityInfoTask().execute(celebrity);


        }
    }
    @Override
    protected void onActivityResult(int requestCode,int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            new processCelebrityTask().execute(imageBitmap);
            imageview.setImageBitmap(imageBitmap);
        }
    }

    private void dispatchTakePictureIntent(){
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);

        }
    }

    private class celebrityInfoTask extends AsyncTask<Celebrity, Void, Void> {
        @Override
        protected Void doInBackground(Celebrity... celebrities) {
            try {
                List<String> list = celebrities[0].getUrls();
                String url = list.get(0);
                Document document = Jsoup.connect(url).get();

                Element recentWorks = document.getElementById("filmography");
                Element knownFor = document.getElementsByClass("knownfor-title").first();
                Element namePoster = document.getElementById("name-poster");
                Element resumeTeaser = document.getElementById("name-bio-text");

                //Name Poster
                String src = namePoster.attr("src");
                ImageView view = findViewById(R.id.celebPic);
                Picasso.get().load(src).into(view);

                //Name
                String name = celebrities[0].getName();
                ((TextView)findViewById(R.id.celebName)).setText(name);

                //Person Accuracy
                String accuracy = celebrities[0].getMatchConfidence().toString();
                ((TextView)findViewById(R.id.celebAcc)).setText(accuracy);

                //Person Background
                String bio = resumeTeaser.ownText();
                ((TextView)findViewById(R.id.background)).setText(bio);


                //Known For Movies
                //Role
                Element knownforTitleRole = knownFor.getElementsByClass("knownfor-title-role").first();

                //Movie Poster
                Element image = knownFor.getElementsByTag("img").first();
                String imagesrc = image.attr("src");
                ImageView imageview = findViewById(R.id.imageView0);
                Picasso.get().load(imagesrc).into(imageview);

                //Title
                String title = knownforTitleRole.attr("title");
                ((TextView)findViewById(R.id.name0)).setText(title);

                //Character
                Element knownforelipsis = knownforTitleRole.getElementsByClass("knownfor-elipsis").first();
                String character = knownforelipsis.ownText();
                ((TextView)findViewById(R.id.char0)).setText(character);

                //Date
                Element knownforyear = knownFor.getElementsByClass("knownfor-year").first();
                String date = knownforyear.ownText();
                ((TextView)findViewById(R.id.date0)).setText(date);


                //Second Movie
                knownforTitleRole = knownforTitleRole.nextElementSibling();

                //Movie Poster
                image = image.nextElementSibling();
                imagesrc = image.attr("src");
                imageview = findViewById(R.id.imageView1);
                Picasso.get().load(imagesrc).into(imageview);

                //Title
                title = knownforTitleRole.attr("title");
                ((TextView)findViewById(R.id.name1)).setText(title);

                //Character
                knownforelipsis = knownforTitleRole.getElementsByClass("knownfor-elipsis").first();
                character = knownforelipsis.ownText();
                ((TextView)findViewById(R.id.char1)).setText(character);

                //Date
                knownforyear = knownforyear.nextElementSibling();
                date = knownforyear.ownText();
                ((TextView)findViewById(R.id.date1)).setText(date);

                //Third Movie
                knownforTitleRole = knownforTitleRole.nextElementSibling();

                //Movie Poster
                image = image.nextElementSibling();
                imagesrc = image.attr("src");
                imageview = findViewById(R.id.imageView2);
                Picasso.get().load(imagesrc).into(imageview);

                //Title
                title = knownforTitleRole.attr("title");
                ((TextView)findViewById(R.id.name2)).setText(title);

                //Character
                knownforelipsis = knownforTitleRole.getElementsByClass("knownfor-elipsis").first();
                character = knownforelipsis.ownText();
                ((TextView)findViewById(R.id.char2)).setText(character);

                //Date
                knownforyear = knownforyear.nextElementSibling();
                date = knownforyear.ownText();
                ((TextView)findViewById(R.id.date2)).setText(date);

                //Fourth Movie
                knownforTitleRole = knownforTitleRole.nextElementSibling();

                //Movie Poster
                image = image.nextElementSibling();
                imagesrc = image.attr("src");
                imageview = findViewById(R.id.imageView3);
                Picasso.get().load(imagesrc).into(imageview);

                //Title
                title = knownforTitleRole.attr("title");
                ((TextView)findViewById(R.id.name3)).setText(title);

                //Character
                knownforelipsis = knownforTitleRole.getElementsByClass("knownfor-elipsis").first();
                character = knownforelipsis.ownText();
                ((TextView)findViewById(R.id.char3)).setText(character);

                //Date
                knownforyear = knownforyear.nextElementSibling();
                date = knownforyear.ownText();
                ((TextView)findViewById(R.id.date3)).setText(date);


                //Recent Works Movies

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void atask) {

        }
    }

    private class processCelebrityTask extends AsyncTask<Bitmap, Integer, RecognizeCelebritiesResult> {
        protected RecognizeCelebritiesResult doInBackground(Bitmap... bitmaps) {

            ByteArrayOutputStream file = new ByteArrayOutputStream();
            bitmaps[0].compress(Bitmap.CompressFormat.PNG, 100, file);
            ByteBuffer bytebuffer = ByteBuffer.wrap(file.toByteArray());

            BasicAWSCredentials credentials = new BasicAWSCredentials("ACCESSKEY", "SECRETKEY");
            AmazonRekognitionClient client = new AmazonRekognitionClient(credentials);
            RecognizeCelebritiesRequest request = new RecognizeCelebritiesRequest();


            Image requestImage = prepareImage(bytebuffer);
            request.setImage(requestImage);


            RecognizeCelebritiesResult celebResult = client.recognizeCelebrities(request);
            Log.d("SUCCESS", "The query was a success");
            return celebResult;
        }

        protected void onPostExecute(RecognizeCelebritiesResult result) {
            Log.d("PostProcess", "moving to printCelebInfo");
            printCelebInfo(result);
        }
    }
}
