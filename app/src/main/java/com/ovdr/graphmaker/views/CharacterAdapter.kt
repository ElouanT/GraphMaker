package com.ovdr.graphmaker.views

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import com.ovdr.graphmaker.R

class CharacterAdapter internal constructor(var c: Context, var items: IntArray) :
    BaseAdapter() {
    override fun getCount(): Int {
        return items.size
    }

    override fun getItem(i: Int): Any? {
        return null
    }

    override fun getItemId(i: Int): Long {
        return 0
    }

    override fun getView(i: Int, view: View?, viewGroup: ViewGroup): View {
        var view = view
        if (view == null) {
            val inflater = c.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            view = inflater.inflate(R.layout.character_icon, null)!!
        }
        val imageView = view.findViewById<ImageView>(R.id.imageView)
        imageView.setImageResource(items[i])
        return view
    }
}