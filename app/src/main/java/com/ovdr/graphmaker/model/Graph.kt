package com.ovdr.graphmaker.model

import android.content.Context
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.text.TextPaint
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import com.ovdr.graphmaker.R

class Graph(
    context: Context,
    private val title: String,
    private val date: String,
    private val entrants: String,
    private val location: String,
    private val players: ArrayList<Player>
    ): View(context) {
    private lateinit var drawable: BitmapDrawable

    init {
        createDrawable()
    }

    private fun createDrawable() {
        // Bitmap creation
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
        playerText.textAlign = Paint.Align.LEFT
        playerText.textSize = 100f

        // Character scale map
        val characterMap = resources.getStringArray(R.array.character_array).associateBy({ it.split("|")[0]}, {it.split("|")[1]})

        for ((playerIndex, player) in players.withIndex()) {
            // Characters
            val characters = player.characters.filter { character -> character !== ""}
            var offset = 0
            for (character in characters.reversed()) {
                // Get drawable
                val characterId = context.resources.getIdentifier(
                    "character_$character",
                    "drawable",
                    context.packageName
                )

                // Create bitmap
                var characterBitmap = BitmapFactory.decodeResource(context.resources, characterId)
                val width = characterBitmap.width
                val height = characterBitmap.height

                // Crop
                val data = characterMap[character]!!
                val x = data.substringAfter("(").substringBefore(";").toInt() * height / 500
                val y = data.substringAfter(";").substringBefore(")").toInt() * height / 500
                val h = data.substringAfter(":").substringBefore("-").toInt() * height / 500
                val w = data.substringAfter("-").toInt() * height / 500

                characterBitmap = Bitmap.createBitmap(characterBitmap,
                        0,
                        maxOf(0, (y-(h/2))),
                        width,
                        minOf(height-(y-(h/2)), h)
                    )

                // Scale and draw
                val characterDrawable = BitmapDrawable(resources, characterBitmap)
                characterDrawable.setBounds(
                    (1250+50*characters.size) - (x+w/2)*153/h - offset,
                    578+playerIndex*(35+153),
                    (1250+50*characters.size) - offset + (width-x-w/2)*153/h,
                    578+playerIndex*(35+153) + 153,
                )
                offset += w*153/h +  (60/characters.size)
                characterDrawable.draw(c)
            }

            // Shadow
            playerText.color = ContextCompat.getColor(context, R.color.green_400)
            c.drawText(player.pseudo, 405f, 690f+playerIndex*(35f+153f), playerText)
            // Pseudo
            playerText.color = Color.WHITE
            c.drawText(player.pseudo, 400f, 685f+playerIndex*(35f+153f), playerText)
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
        titleText.textAlign = Paint.Align.CENTER
        titleText.textSize = 70f
        c.drawText(title, 800f, 520f, titleText)

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