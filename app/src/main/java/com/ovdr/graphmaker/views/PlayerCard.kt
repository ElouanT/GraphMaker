package com.ovdr.graphmaker.views

import android.content.Context
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.ovdr.graphmaker.R
import com.ovdr.graphmaker.model.Player

class PlayerCard(context: Context, placement: Int) : FrameLayout(context)  {
    private var player: Player = Player()
    private var characters: MutableList<String> = MutableList(3) {""}

    init {
        inflate(context, R.layout.player_card, this)

        // Placement
        val playerPlacement: TextView = findViewById(R.id.player_placement)
        playerPlacement.text = placement.toString()
        val colorId = resources.getIdentifier("placement_$placement", "color", context.packageName)
        playerPlacement.setBackgroundColor(ContextCompat.getColor(context, colorId))

        // Character
        val characterButtons = ArrayList<ImageButton>()
        characterButtons.add(findViewById(R.id.player_character1))
        characterButtons.add(findViewById(R.id.player_character2))
        characterButtons.add(findViewById(R.id.player_character3))

        for (i in 1..3) {
            characterButtons[i-1].setOnClickListener {
                val characterPicker = CharacterPicker(characterButtons[i-1])
                characterPicker.show((context as AppCompatActivity).supportFragmentManager, "character_picker")
                characterPicker.onResult = object : CharacterPicker.OnResult {
                    override fun action(result: String) {
                        characters[i-1] = result
                    }
                }
            }
        }
    }

    fun setPlayer(player: Player) {
        this.player = player
        findViewById<EditText>(R.id.player_pseudo).setText(player.pseudo)
        findViewById<EditText>(R.id.player_team).setText(player.team)
    }

    fun getPlayer(): Player {
        player.pseudo = findViewById<EditText>(R.id.player_pseudo).text.toString()
        player.team = findViewById<EditText>(R.id.player_team).text.toString()
        player.characters = characters
        return player
    }
}