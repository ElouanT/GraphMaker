package com.ovdr.graphmaker.views

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.ovdr.graphmaker.R
import com.ovdr.graphmaker.model.Player
import java.io.Serializable
import java.text.SimpleDateFormat
import java.util.*

class GraphFormActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_graph_form)

        // General
        var title: EditText = findViewById(R.id.form_title)
        var entrants: EditText = findViewById(R.id.form_entrants)
        var location: EditText = findViewById(R.id.form_location)
        var date: EditText = findViewById(R.id.form_date)

        // Instantiate
        title.setText(intent.getStringExtra("title"))
        entrants.setText(intent.getStringExtra("entrants"))
        date.setText(intent.getStringExtra("date"))

        // Players
        var playerCards = ArrayList<PlayerCard>(8)
        val playerList = findViewById<LinearLayout>(R.id.form_player_list)

        for (i in 1..8) {
            val playerCard = PlayerCard(this)
            val player: Player? = intent.serializable("player$i")
            if (player !== null) {
                println(player.pseudo)
                playerCard.setPlayer(player)
            }
            playerList.addView(playerCard)
            playerCards.add(playerCard)
        }

        // Button
        val generate = findViewById<Button>(R.id.form_button_generate)
        generate.setOnClickListener {
            val players = ArrayList<Player>();
            for (i in 1..8) {
                players.add(playerCards[i-1].getPlayer())
            }
            nextActivity(
                title.text.toString(),
                entrants.text.toString(),
                location.text.toString(),
                date.text.toString(),
                players
            )
        }
    }

    private inline fun <reified T : Serializable> Intent.serializable(key: String?): T? = when {
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> getSerializableExtra(key, T::class.java)
        else -> @Suppress("DEPRECATION") getSerializableExtra(key) as? T
    }

    fun nextActivity(title: String, entrants: String, location: String, date: String, players: ArrayList<Player>) {
        val intent = Intent(this, SaveGraphActivity::class.java)
        intent.putExtra("title", title)
        intent.putExtra(
            "entrants",
            if (entrants == "") "" else "$entrants entrants"
        )
        intent.putExtra("location", location)
        intent.putExtra("date", date)
        var i = 0
        for (player in players) {
            i++
            intent.putExtra("player$i", player)
        }
        startActivity(intent)
    }
}