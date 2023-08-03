package com.ovdr.graphmaker.views

import android.Manifest
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.widget.Button
import android.widget.FrameLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.ovdr.graphmaker.R
import com.ovdr.graphmaker.model.Graph
import com.ovdr.graphmaker.model.Player
import java.io.Serializable
import java.util.*

class SaveGraphActivity : AppCompatActivity() {
    private val REQUEST_CODE = 1
    private var graph: Graph? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_save_graph)

        // Graph
        val frameLayout = findViewById<FrameLayout>(R.id.save_frame)

        val title = intent.getStringExtra("title")!!
        val date = intent.getStringExtra("date")!!
        val entrants = intent.getStringExtra("entrants")!!
        val location = intent.getStringExtra("location")!!

        val players: ArrayList<Player> = ArrayList<Player>(8)
        for (i in 1..8) {
            val player: Player = intent.serializable("player$i")!!
            players.add(player)
        }

        // Graph
        graph = Graph(this, title, date, entrants, location, players)
        graph!!.layoutParams =
            FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT
            )
        frameLayout.addView(graph)

        // Save image
        val downloadButton = findViewById<Button>(R.id.save_button)

        downloadButton.setOnClickListener {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                saveImage()
            } else {
                ActivityCompat.requestPermissions(
                    this, arrayOf(
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    ), REQUEST_CODE
                )
            }
        }
    }

    private inline fun <reified T : Serializable> Intent.serializable(key: String): T? = when {
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> getSerializableExtra(key, T::class.java)
        else -> @Suppress("DEPRECATION") getSerializableExtra(key) as? T
    }

    private fun saveImage() {
        val contentResolver = contentResolver
        val images: Uri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
        } else {
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        }
        val contentValues = ContentValues()
        contentValues.put(
            MediaStore.Images.Media.DISPLAY_NAME,
            System.currentTimeMillis().toString() + ".jpg"
        )
        contentValues.put(MediaStore.Images.Media.MIME_TYPE, "images")
        contentValues.put(
            MediaStore.MediaColumns.RELATIVE_PATH,
            Environment.DIRECTORY_PICTURES + "/GraphMaker"
        )
        val uri = contentResolver.insert(images, contentValues)
        try {
            val bitmapDrawable = graph!!.background as BitmapDrawable
            val bitmap = bitmapDrawable.bitmap
            val outputStream = contentResolver.openOutputStream(Objects.requireNonNull(uri!!))
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
            Toast.makeText(this, R.string.save_success, Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            Toast.makeText(this, R.string.save_error, Toast.LENGTH_SHORT)
                .show()
            e.printStackTrace()
        }
    }
}