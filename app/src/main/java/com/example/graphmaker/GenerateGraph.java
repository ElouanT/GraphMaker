package com.example.graphmaker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import model.Character;
import model.Data;
import model.Player;
import views.DateWatcher;
import views.LayoutPlayer;

public class GenerateGraph extends AppCompatActivity {
    String selectedTemplate = "";
    EditText title;
    EditText entrants;
    EditText location;
    EditText date;
    ArrayList<LayoutPlayer> players;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate_graph);

        this.selectedTemplate = getIntent().getStringExtra("template");

        // Titles section

        this.title = findViewById(R.id.title);

        if (this.selectedTemplate.matches("man")) {
            this.title.setText(R.string.title_man);
        } else if (this.selectedTemplate.matches("mdtk")) {
            this.title.setText(R.string.title_mdtk);
        } else if (this.selectedTemplate.matches("sun")) {
            this.title.setText(R.string.title_sun);
        }

        this.entrants = findViewById(R.id.entrants);
        this.location = findViewById(R.id.location);

        this.date = findViewById(R.id.date);
        this.date.setText(new SimpleDateFormat("dd/MM/yyyy").format(new Date()));
        this.date.addTextChangedListener(new DateWatcher(this.date));

        // Players
        this.players = new ArrayList<>(8);
        LinearLayout linearLayout = findViewById(R.id.linearLayout);

        for (int i=1; i<=8; i++) {
            LayoutPlayer layoutPlayer = new LayoutPlayer(this, this.selectedTemplate);
            linearLayout.addView(layoutPlayer);
            this.players.add(layoutPlayer);
        }

        // Button

        Button generate = findViewById(R.id.button);

        generate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nextActivity();
            }
        });
    }

    private void nextActivity() {
        Intent intent = new Intent(this, SaveImage.class);
        intent.putExtra("template", this.selectedTemplate);
        intent.putExtra("title", this.title.getText().toString());
        intent.putExtra("entrants", (this.entrants.getText().toString().matches("") ? "" : this.entrants.getText().toString() + " entrants"));
        intent.putExtra("location", this.location.getText().toString());
        intent.putExtra("date", this.date.getText().toString());
        int i = 0;
        for (LayoutPlayer p : players) {
            i++;
            intent.putExtra("player"+i, p.getPlayer());
        }
        startActivity(intent);
    }
}