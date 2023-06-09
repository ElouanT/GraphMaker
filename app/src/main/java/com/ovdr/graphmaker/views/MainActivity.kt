package com.ovdr.graphmaker.views

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.ovdr.graphmaker.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var newGraphButton: Button = findViewById<Button>(R.id.main_button_new_graph)
        newGraphButton.setOnClickListener {
            val intent = Intent(this, GraphFormActivity::class.java)
            startActivity(intent)
        }
    }
}