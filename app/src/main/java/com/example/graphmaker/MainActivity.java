package com.example.graphmaker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.material.imageview.ShapeableImageView;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    String selectedTemplate = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ShapeableImageView man = findViewById(R.id.image_man);
        ShapeableImageView mdtk = findViewById(R.id.image_mdtk);
        ShapeableImageView sun = findViewById(R.id.image_sun);
        ShapeableImageView cash = findViewById(R.id.image_cash);

        HashMap<String, ShapeableImageView> images = new HashMap<>();
        images.put("man", man);
        images.put("mdtk", mdtk);
        images.put("sun", sun);
        images.put("cash", cash);

        Button next = findViewById(R.id.button);

        for (HashMap.Entry<String, ShapeableImageView> entry : images.entrySet()) {
            String template = entry.getKey();
            ShapeableImageView image = entry.getValue();

            image.setOnClickListener(new View.OnClickListener() {
                //@Override
                public void onClick(View v) {
                    for (HashMap.Entry<String, ShapeableImageView> entry : images.entrySet()) {
                        entry.getValue().setStrokeWidth(0);
                    }

                    selectedTemplate = template;
                    image.setStrokeWidth(8);
                    next.setEnabled(true);
                }
            });
        }

        // ---


        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nextActivity();
            }
        });
    }

    private void nextActivity() {
        Intent intent = new Intent(this, GenerateGraph.class);
        intent.putExtra("template", this.selectedTemplate);
        startActivity(intent);
    }
}