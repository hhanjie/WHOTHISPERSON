package com.example.whothisperson;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private ImageView imageview;
    String currentPhotoPath;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    private static String AUTHORITY = BuildConfig.APPLICATION_ID+".fileprovider";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.imageview = (ImageView)this.findViewById(R.id.imageView);
        Button photoButton = (Button) this.findViewById(R.id.shutterButton);
        photoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent();
                Toast toast = Toast.makeText(getApplicationContext(),"There is response",(int)10);
                toast.show();
                Log.d("OnClick", "Entered");
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode,int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            imageview.setImageBitmap(imageBitmap);
        }
    }

    private void dispatchTakePictureIntent(){
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            /*
            Log.d("TakePic", "Entered");
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                Log.d("Error","Cachted");
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Log.d("entered successfully", "Intent");
                Uri photoURI;
                    photoURI = FileProvider.getUriForFile(this,
                            AUTHORITY,
                            photoFile);

                Log.d("runs on Uri", "successful");
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }*/
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "my_images");
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        if(storageDir == null || image == null){
            Log.d("ErrorCreate","Entered");
        }
        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        Log.d(currentPhotoPath, "CurrentPhotoPath");
        return image;
    }

}
