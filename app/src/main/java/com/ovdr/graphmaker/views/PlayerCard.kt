package com.ovdr.graphmaker.views

import android.content.Context
import android.widget.EditText
import android.widget.FrameLayout
import com.ovdr.graphmaker.R
import com.ovdr.graphmaker.model.Player

class PlayerCard(context: Context) : FrameLayout(context)  {
    private var player: Player = Player()

    init {
        inflate(context, R.layout.player_card, this)
    }

    fun setPlayer(player: Player) {
        this.player = player
        findViewById<EditText>(R.id.player_pseudo).setText(player.pseudo)
        findViewById<EditText>(R.id.player_team).setText(player.team)
    }

    fun getPlayer(): Player {
        player.pseudo = findViewById<EditText>(R.id.player_pseudo).text.toString()
        player.team = findViewById<EditText>(R.id.player_team).text.toString()
        return player
    }
}