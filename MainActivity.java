package com.example.foodrecognizer;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class MainActivity extends AppCompatActivity {

    private static final int MY_CAMERA_REQUEST_CODE = 100;
    private ImageView imageView;
    private TextView textView;
    private FoodDatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = findViewById(R.id.imageView);
        textView = findViewById(R.id.textView);
        Button button = findViewById(R.id.button);

        databaseHelper = new FoodDatabaseHelper(this);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, MY_CAMERA_REQUEST_CODE);
        }

        button.setOnClickListener(v -> dispatchTakePictureIntent());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_CAMERA_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Camera Permission Granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Camera Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private final ActivityResultLauncher<Intent> activityResultLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK) {
                    Intent data = result.getData();
                    if (data != null && data.getExtras() != null) {
                        Bitmap imageBitmap = (Bitmap) data.getExtras().get("data");
                        if (imageBitmap != null) {
                            imageView.setImageBitmap(imageBitmap);
                            recognizeFood(imageBitmap);
                        } else {
                            Toast.makeText(this, "Failed to capture image", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(this, "No data returned", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, "Failed to capture image", Toast.LENGTH_SHORT).show();
                }
            });

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            activityResultLauncher.launch(takePictureIntent);
        }
    }

    private void recognizeFood(Bitmap imageBitmap) {
        new Thread(() -> {
            String recognizedFood = analyzeImage(imageBitmap); // Placeholder method
            runOnUiThread(() -> {
                if (recognizedFood != null) {
                    FoodItem foodItem = databaseHelper.getFoodItem(recognizedFood);
                    if (foodItem != null) {
                        String foodInfo = getString(R.string.food_info, foodItem.getName(), foodItem.getCalories());
                        textView.setText(foodInfo);
                    } else {
                        textView.setText(R.string.unknown_food);
                    }
                } else {
                    textView.setText(R.string.recognition_failed);
                }
            });
        }).start();
    }

    private String analyzeImage(Bitmap imageBitmap) {
        // Placeholder method for combined color and texture analysis
        // Example: Analyze both color and texture features to identify food items
        // Replace with actual image processing logic based on your requirements

        // Dummy logic to randomly return a food name from the database
        String[] foodNames = {"Apple", "Banana", "Orange", "Strawberries", "Grapes", "Carrot", "Potato", "Tomato", "Brown Rice", "Pasta"};
        int randomIndex = new java.util.Random().nextInt(foodNames.length);
        return foodNames[randomIndex];
    }
}
