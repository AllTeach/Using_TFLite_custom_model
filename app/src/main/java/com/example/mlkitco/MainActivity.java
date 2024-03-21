package com.example.mlkitco;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.mlkitco.tflite.Classifier;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private Classifier classifier;
    private ImageView imageView;
    private TextView textView;
    private ActivityResultLauncher<Void> mGetThumb;
    private ActivityResultLauncher<String[]> requestPermissionLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = findViewById(R.id.imageView);
        textView = findViewById(R.id.resultView);
        Button captureImageButton = findViewById(R.id.selectImageButton);
        captureImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePicture();

            }
            });
        registerLauncher();
        try {
            classifier = Classifier.create(this, Classifier.Device.CPU, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void takePicture() {
       if( ContextCompat.checkSelfPermission(
                this, android.Manifest.permission.CAMERA) ==
                PackageManager.PERMISSION_GRANTED)
           mGetThumb.launch(null);
        else {
            String[] permissions = {android.Manifest.permission.CAMERA};
            requestPermissionLauncher.launch(permissions);
        }


    }

    private void startCamera() {
        // Initialize the camera and get the camera preview
        // When an image is available, process it and display the results
    }

    private void processImage(Bitmap bitmap) {
        List<Classifier.Recognition> results = classifier.recognizeImage(bitmap, 0);
        Classifier.Recognition bestResult = results.get(0);
        textView.setText(bestResult.getTitle() + ": " + bestResult.getConfidence());
    }

    private void registerLauncher() {
        requestPermissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), new ActivityResultCallback<Map<String, Boolean>>() {
            @Override
            public void onActivityResult(Map<String, Boolean> result) {
                if (result != null) {
                    boolean write = result.get(android.Manifest.permission.CAMERA);
                    if (write)
                        takePicture();
                    else
                        Toast.makeText(MainActivity.this, "No permissions", Toast.LENGTH_LONG).show();

                }


            }

        });

        mGetThumb = registerForActivityResult(new ActivityResultContracts.TakePicturePreview(), new ActivityResultCallback<Bitmap>() {
            @Override
            public void onActivityResult(Bitmap result) {
                imageView.setImageBitmap(result);
                processImage(result);
            }
        });
    }

    }
