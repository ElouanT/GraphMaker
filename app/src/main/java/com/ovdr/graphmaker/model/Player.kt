package com.ovdr.graphmaker.model

import java.io.Serializable

class Player(team: String = "", pseudo: String = ""): Serializable {
    var pseudo: String = pseudo
    var team: String = team
    var characters: MutableList<String> = mutableListOf()

    fun setCharacter(index: Int, character: String) {
        this.characters[index] = character
    }
}