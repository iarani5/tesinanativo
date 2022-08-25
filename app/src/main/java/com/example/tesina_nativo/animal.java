package com.example.tesina_nativo;

import android.widget.EditText;

public class animal {

    private String humanName;
    private String humanLastName;
    private String comments;
    private String humanPhone;
    private String humanMail;
    private String animalRace;
    private String animalState;
    private String animalSpecies;
    private boolean animalCastrated;


    public String getHumanName() {
        return humanName;
    }

    public void setHumanName(String humanName) {
        this.humanName = humanName;
    }

    public String getHumanLastName() {
        return humanLastName;
    }

    public void setHumanLastName(String humanLastName) {
        this.humanLastName = humanLastName;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getHumanPhone() {
        return humanPhone;
    }

    public void setHumanPhone(String humanPhone) {
        this.humanPhone = humanPhone;
    }

    public String getHumanMail() {
        return humanMail;
    }

    public void setHumanMail(String humanMail) {
        this.humanMail = humanMail;
    }

    public String getAnimalRace() {
        return animalRace;
    }

    public void setAnimalRace(String animalRace) {
        this.animalRace = animalRace;
    }

    public String getAnimalState() {
        return animalState;
    }

    public void setAnimalState(String animalState) {
        this.animalState = animalState;
    }

    public String getAnimalSpecies() {
        return animalSpecies;
    }

    public void setAnimalSpecies(String animalSpecies) {
        this.animalSpecies = animalSpecies;
    }

    public boolean isAnimalCastrated() { return animalCastrated; }

    public void setAnimalCastrated(boolean animalCastrated) { this.animalCastrated = animalCastrated; }


    animal() {
        // no-args constructor
    }

}
