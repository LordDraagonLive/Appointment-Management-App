package com.bacdevelopers.appointmentmanagementapplication.models;

/**
 * Created by Buddhi Adhikari 16280809 on 28/04/2018.
 **/
public class Synonyms {
    private String synonym;
    private String category;

    public Synonyms() {}

    public String getSynonym() {
        return synonym;
    }

    public void setSynonym(String synonym) {
        this.synonym = synonym;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    @Override
    public String toString() {
        return "Synonyms{" +
                "synonym='" + synonym + '\'' +
                ", category='" + category + '\'' +
                '}';
    }
}

