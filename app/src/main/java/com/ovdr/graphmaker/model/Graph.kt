package com.ovdr.graphmaker.model

import android.content.Context
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.text.TextPaint
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import com.ovdr.graphmaker.R

class Graph(context: Context, title: String, date: String, entrants: String, location: String, players: ArrayList<Player>): View(context) {
    // Main infos
    val title: String = title
    val date: String = date
    val entrants: String = entrants
    val location: String = location
    // Players
    val players: ArrayList<Player> = players
    // Drawable rendered
    lateinit var drawable: BitmapDrawable

    init {
        createDrawable()
    }

    fun createDrawable() {
        // Bitmap création
        val bitmap = Bitmap.createBitmap(1600, 2263, Bitmap.Config.ARGB_8888)
        val c = Canvas(bitmap)

        // Template background
        val background = ContextCompat.getDrawable(context, R.drawable.template_man_background)!!
        val bounds = c.clipBounds
        background.bounds = bounds
        background.draw(c)


        // Players
        val playerText: Paint = TextPaint()
        playerText.isAntiAlias = true
        playerText.typeface = ResourcesCompat.getFont(context, R.font.montserrat_bold)
        playerText.color = Color.WHITE
        playerText.textAlign = Paint.Align.LEFT
        playerText.textSize = 120f

        var p: Int = 0

        for (player in players) {
            // Team

            // Pseudo
            c.drawText(player.pseudo, 520f, 685f+p*(35f+153f), playerText)

            // Characters

            p++
        }

        // Template foreground
        val foreground = ContextCompat.getDrawable(context, R.drawable.template_man_foreground)!!
        foreground.bounds = bounds
        foreground.draw(c)

        // Title
        val titleText: Paint = TextPaint()
        titleText.isAntiAlias = true
        titleText.typeface = ResourcesCompat.getFont(context, R.font.montserrat_bold)
        titleText.color = ContextCompat.getColor(context, R.color.green_400)
        titleText.textAlign = Paint.Align.LEFT
        titleText.textSize = 70f
        c.drawText(title, 67f, 196f, titleText)

        // Texts
        val text: Paint = TextPaint()
        text.isAntiAlias = true
        text.typeface = ResourcesCompat.getFont(context, R.font.montserrat_bold)
        text.color = Color.WHITE
        text.textAlign = Paint.Align.LEFT
        text.textSize = 64f
        c.drawText(location, 66f, 200f, text)
        c.drawText(entrants, 66f, 275f, text)
        text.textAlign = Paint.Align.RIGHT
        c.drawText(date, 1535f, 275f, text)

        // Set canvas bitmap to background
        drawable = BitmapDrawable(resources, bitmap)
    }

    override fun draw(c: Canvas?) {
        super.draw(c)
        this.background = drawable
    }
}