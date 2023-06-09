package com.ovdr.graphmaker.model

import java.io.Serializable

class Player: Serializable {
    var pseudo: String = ""
    var team: String = ""
    var characters: MutableList<String> = mutableListOf()

    fun setCharacter(index: Int, character: String) {
        this.characters[index] = character
    }
}