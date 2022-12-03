package model;

import java.io.Serializable;

public class Character implements Serializable {
    public String name;
    public int alt;

    public Character(String name, int alt) {
        this.name = name;
        this.alt = alt;
    }

    public String getPath(String template) {

        if (this.name.matches("mii_brawler") // miis
                || this.name.matches("mii_gunner")
                || this.name.matches("mii_swordfighter")) {
            return "ultimate_" + this.name;
        }

        if (this.name.matches("random")) { // random
            return "random";
        }

        if (template.matches("mdtk")) {
            return "melee_" + this.name + "_alt" + this.alt;
        }

        return "ultimate_" + this.name + "_alt" + this.alt;
    }
}
