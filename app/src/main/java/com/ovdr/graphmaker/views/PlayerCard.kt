package com.ovdr.graphmaker.views

import android.content.Context
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.content.res.TypedArrayUtils.getResourceId
import com.ovdr.graphmaker.R
import com.ovdr.graphmaker.model.Player

class PlayerCard(context: Context, placement: Int) : FrameLayout(context)  {
    private var player: Player = Player()
    private var placement: Int = placement

    init {
        inflate(context, R.layout.player_card, this)
        var playerPlacement: TextView = findViewById(R.id.player_placement)
        playerPlacement.text = placement.toString()
        var colorId = resources.getIdentifier("placement_$placement", "color", context.packageName)
        println(colorId)
        println(ContextCompat.getColor(context, colorId))
        playerPlacement.setBackgroundColor(ContextCompat.getColor(context, colorId))
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