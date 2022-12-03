package views;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.FrameLayout;
import android.widget.Spinner;

import com.example.graphmaker.R;

import java.util.ArrayList;
import java.util.HashMap;

import model.Data;
import model.Character;

public class LayoutCharacter extends FrameLayout {
    private Context context;
    private AutoCompleteTextView character;
    private Spinner alt;

    // Characters data
    HashMap<String, String> characters;

    // Spinner
    ArrayList<String> items;
    String prefix;

    public LayoutCharacter(Context context, String template) {
        super(context);
        this.context = context;
        inflate(getContext(), R.layout.layout_character, this);

        if (template.matches("mdtk")) {
            characters = Data.getMeleeMap();
        } else {
            characters = Data.getUltimateMap();
        }

        // AutoComplete Character
        ArrayList<String> characterList = new ArrayList<>(characters.keySet());

        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.select_dialog_item, characterList);
        character = (AutoCompleteTextView) findViewById(R.id.character);
        character.setThreshold(1);
        character.setAdapter(adapter);

        // Spinner Alt
        alt = findViewById(R.id.spinner);

        // research of available alt for the character
        prefix = "ultimate_";

        if (template.matches("mdtk")) {
            prefix = "melee_";
        }

        // refresh when character change
        character.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                items = new ArrayList<>();

                for (int i=1; i<=8; i++) {
                    if (getContext().getResources().getIdentifier(prefix + characters.get(s.toString()) + "_alt" + i, "drawable", getContext().getPackageName()) !=0 ) {
                        items.add(i+"");
                    }
                }

                ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(context, R.layout.dialog_spinner_item, items);
                spinnerAdapter.setDropDownViewResource(R.layout.dialog_dropdown_spinner_item); // change the color of the text
                alt.setAdapter(spinnerAdapter);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    public Character getCharacter() {
        String name = character.getText().toString();

        if (!characters.containsKey(name) || alt.getSelectedItem() == null){
            return null;
        }

        if (characters.get(name).matches("mii_brawler") // miis
                || characters.get(name).matches("mii_gunner")
                || characters.get(name).matches("mii_swordfighter")) {
            return new Character(characters.get(name), 0);
        }

        if (characters.get(name).matches("random")){ // random
            return new Character("random", 0);
        }

        return new Character(characters.get(name), Integer.valueOf(alt.getSelectedItem().toString()));
    }
}
