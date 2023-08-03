package com.ovdr.graphmaker.views

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.apollographql.apollo3.ApolloClient
import com.example.EventStandingsQuery
import com.ovdr.graphmaker.BuildConfig
import com.ovdr.graphmaker.R
import com.ovdr.graphmaker.model.Player
import kotlinx.coroutines.*
import java.sql.Date
import java.text.SimpleDateFormat
import java.util.*
import kotlin.coroutines.CoroutineContext

class MainActivity : AppCompatActivity(), CoroutineScope {
    private val job: Job = Job()

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val urlText: EditText = findViewById(R.id.main_url)

        val apolloClient = ApolloClient.Builder()
            .serverUrl("https://api.start.gg/gql/alpha")
            .build()

        val newGraphButton: Button = findViewById(R.id.main_button_new_graph)
        val progressBar: ProgressBar = findViewById(R.id.main_progress_bar)
        progressBar.visibility = View.GONE
        newGraphButton.setOnClickListener {
            val intent = Intent(this, GraphFormActivity::class.java)
            val context = this
            async {
                newGraphButton.text = ""
                progressBar.visibility = View.VISIBLE
                val url = urlText.text.toString().substringAfter("start.gg/").substringBefore("/overview")

                if (url == "") {
                    startActivity(intent)
                } else {
                    // API Fetch
                    val response = apolloClient.query(EventStandingsQuery(eventSlug = url))
                        .addHttpHeader("Authorization", BuildConfig.TOKEN).execute()
                    if (response.data !== null && response.data!!.event !== null) {
                        intent.putExtra("title", response.data!!.event!!.name)
                        intent.putExtra("entrants", response.data!!.event!!.numEntrants.toString())
                        intent.putExtra(
                            "date", SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(
                                Date(response.data!!.event!!.startAt.toString().toLong() * 1000)
                            )
                        )

                        val standings = response.data!!.event!!.standings!!

                        for (i in 1..8) {
                            val name = standings.nodes!![i - 1]!!.entrant!!.name!!.split(" | ")
                            if (name.size > 1) {
                                intent.putExtra("player$i", Player(name[0], name[1]))
                            } else {
                                intent.putExtra("player$i", Player(pseudo = name[0]))
                            }
                        }
                        startActivity(intent)
                    } else {
                        Toast.makeText(context, R.string.api_error_message, Toast.LENGTH_SHORT)
                            .show()
                    }
                }
                newGraphButton.setText(R.string.new_graph)
                progressBar.visibility = View.GONE
            }.start()
        }
    }

}