package views;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.example.graphmaker.R;

import java.util.ArrayList;
import java.util.HashMap;

import model.Data;
import model.Player;
import model.Character;

public class LayoutPlayer extends FrameLayout {

    private Context context;
    private LinearLayout linearLayout;
    private AutoCompleteTextView team;
    private Button addCharacter;
    private Button removeCharacter;
    private ArrayList<LayoutCharacter> layoutCharacters;
    private int nbCharacters;

    // Teams data
    HashMap<String, String> teams;

    public LayoutPlayer(Context context, String template) {
        super(context);
        this.context = context;
        inflate(getContext(), R.layout.layout_player, this);

        this.linearLayout = findViewById(R.id.linearLayout);

        // Team
        teams = Data.getTeamMap();
        ArrayList<String> teamList = new ArrayList<>(teams.keySet());

        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.select_dialog_item, teamList);
        team = (AutoCompleteTextView) findViewById(R.id.team);
        team.setThreshold(1);
        team.setAdapter(adapter);


        // Default character
        LayoutCharacter layoutCharacter = new LayoutCharacter(context, template);
        this.linearLayout.addView(layoutCharacter);
        this.layoutCharacters = new ArrayList<>();
        this.layoutCharacters.add(layoutCharacter);
        this.nbCharacters = 1;

        // Add a character
        addCharacter = findViewById(R.id.add_character);
        addCharacter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutCharacter layoutCharacter = new LayoutCharacter(context, template);
                linearLayout.addView(layoutCharacter);
                layoutCharacters.add(layoutCharacter);
                nbCharacters++;

                removeCharacter.setEnabled(true);

                if (nbCharacters >= 3) { // Max of 3 characters displayed
                    addCharacter.setEnabled(false);
                }
            }
        });

        removeCharacter = findViewById(R.id.remove_character);
        removeCharacter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutCharacter lastCharacter = layoutCharacters.remove(nbCharacters-1);
                linearLayout.removeView(lastCharacter);
                nbCharacters--;

                addCharacter.setEnabled(true);

                if (nbCharacters <= 1) {
                    removeCharacter.setEnabled(false);
                }
            }
        });
    }

    public Player getPlayer() {
        EditText pseudo = findViewById(R.id.pseudo);

        ArrayList<Character> characters = new ArrayList<>();

        for (LayoutCharacter layoutCharacter : layoutCharacters) {
            characters.add(layoutCharacter.getCharacter());
        }

        String team = this.team.getText().toString();
        if (teams.get(this.team.getText().toString()) != null) {
            team = teams.get(this.team.getText().toString());
        }

        return new Player(team, pseudo.getText().toString(), characters);
    }
}
