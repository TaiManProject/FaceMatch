package com.facematch;

import android.graphics.Bitmap;

import java.util.ArrayList;

public class Result {
    Bitmap synPhoto;
    ArrayList<Person> persons;
    int foundMethod;

    public Result() {
        this.synPhoto = null;
        this.persons = new ArrayList<Person>();
    }

    public Bitmap getSynPhoto() {
        return synPhoto;
    }

    public void setSynPhoto(Bitmap synPhoto) {
        this.synPhoto = synPhoto;
    }

    public ArrayList<Person> getPersons() {
        return persons;
    }

    public void setPersons(ArrayList<Person> persons) {
        this.persons = persons;
    }

    public void addPerson(Person person) {
        persons.add(person);
    }

    public int getFoundMethod() {
        return foundMethod;
    }

    public void setFoundMethod(int foundMethod) {
        this.foundMethod = foundMethod;
    }
}
