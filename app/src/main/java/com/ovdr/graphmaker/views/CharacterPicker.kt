package com.ovdr.graphmaker.views

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.GridView
import android.widget.ImageButton
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.DialogFragment
import com.ovdr.graphmaker.R

class CharacterPicker(characterButton: ImageButton): DialogFragment() {
    var characterButton: ImageButton = characterButton
    lateinit var onResult: OnResult

    override fun onStart() {
        super.onStart()
        val dialog: Dialog? = dialog
        if (dialog != null) {
            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.MATCH_PARENT
            dialog.window!!.setLayout(width, height)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view: View = inflater.inflate(R.layout.character_picker, container, false)
        // Remove icon button
        view.findViewById<ImageButton>(R.id.remove_icon).setOnClickListener {
            characterButton.setImageResource(R.drawable.icon_character)
            onResult.action("")
            dismiss()
        }

        // Character list
        val characters = resources.getStringArray(R.array.character_array).map{ character -> character.split("|")[0]}
        val characterStrings = characters.map{ character ->
            var id = resources.getIdentifier("character_$character", "string", requireContext().packageName)
            resources.getString(id)}
        val characterDrawables = characters.map{ character ->
            resources.getIdentifier("icon_$character", "drawable", requireContext().packageName)}.toIntArray()
        var filteredCharacters = characters

        // Add icons to grid
        val grid: GridView = view.findViewById(R.id.grid_icons)
        val gridAdapter = CharacterAdapter(requireContext(), characterDrawables)
        grid.adapter = gridAdapter

        // Icon on click
        grid.setOnItemClickListener { _, _, index, _ ->
            var character = filteredCharacters[index]
            characterButton.setImageResource(
                resources.getIdentifier("icon_$character", "drawable", requireContext().packageName))
            onResult.action(character)
            dismiss()
        }

        // Search view
        val searchAdapter = ArrayAdapter(requireContext(),
            android.R.layout.simple_dropdown_item_1line,
            characterStrings)
        val search: AutoCompleteTextView = view.findViewById(R.id.search_character)
        search.setAdapter(searchAdapter)
        search.doOnTextChanged { text, _, _, _ ->
            filteredCharacters = characters.filter { character ->
                val id = resources.getIdentifier("character_$character", "string", requireContext().packageName)
                resources.getString(id).contains(text.toString())}
            gridAdapter.items = filteredCharacters.map{ character ->
                resources.getIdentifier("icon_$character", "drawable", requireContext().packageName)}.toIntArray()
            gridAdapter.notifyDataSetChanged()
        }

        return view
    }

    public interface OnResult {
        public fun action(result: String)
    }
}