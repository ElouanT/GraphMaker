package com.example.graphmaker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Objects;

import model.Player;
import views.Graph;

public class SaveImage extends AppCompatActivity {

    private static final int REQUEST_CODE = 1;
    private Graph graph;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_image);

        // Graph
        FrameLayout frameLayout = findViewById(R.id.frameLayout);

        String template = getIntent().getStringExtra("template");
        String title = getIntent().getStringExtra("title");
        String date = getIntent().getStringExtra("date");
        String entrants = getIntent().getStringExtra("entrants");
        String location = getIntent().getStringExtra("location");

        ArrayList<Player> players = new ArrayList<>(8);
        for (int i = 1; i<=8; i++) {
            Player player = (Player) getIntent().getSerializableExtra("player"+i);
            players.add(player);
        }

        graph = new Graph(SaveImage.this, template, title, date, entrants, location, players);
        graph.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
        frameLayout.addView(graph);

        // Save image
        Button downloadButton = findViewById(R.id.download_button);

        downloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ContextCompat.checkSelfPermission(SaveImage.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    saveImage();
                } else {
                    ActivityCompat.requestPermissions(SaveImage.this, new String[]{
                            Manifest.permission.WRITE_EXTERNAL_STORAGE
                            }, REQUEST_CODE);
                }
            }
        });
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                saveImage();
            } else {
                Toast.makeText(SaveImage.this, "Une autorisation est requise afin d'enregistrer l'image'", Toast.LENGTH_SHORT).show();
            }
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void saveImage() {

        Uri images;
        ContentResolver contentResolver = getContentResolver();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            images = MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY);
        } else {
            images = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        }

        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.Images.Media.DISPLAY_NAME, System.currentTimeMillis() + ".jpg");
        contentValues.put(MediaStore.Images.Media.MIME_TYPE, "images");
        contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES + "/GraphMaker");
        Uri uri = contentResolver.insert(images, contentValues);

        try {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) graph.getBackground();
            Bitmap bitmap = bitmapDrawable.getBitmap();

            OutputStream outputStream = contentResolver.openOutputStream(Objects.requireNonNull(uri));

            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);

            Toast.makeText(SaveImage.this, "Image enregistrée", Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            Toast.makeText(SaveImage.this, "Erreur lors de l'enregistrement", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

}