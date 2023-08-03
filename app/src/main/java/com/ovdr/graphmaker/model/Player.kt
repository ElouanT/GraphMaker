package com.ovdr.graphmaker.model

import java.io.Serializable

class Player(var team: String = "", var pseudo: String = ""): Serializable {
    var characters: MutableList<String> = mutableListOf()
}