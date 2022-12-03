package model;

import java.io.Serializable;
import java.util.ArrayList;

import model.Character;

public class Player implements Serializable {
    public String team;
    public String pseudo;
    public ArrayList<Character> characters;

    public Player(String team, String pseudo, ArrayList<Character> characters){
        this.team = team;
        this.pseudo = pseudo;
        this.characters = characters;
    }
}
