package com.facematch;

import java.util.ArrayList;

public class Result {
    Person synPerson;
    ArrayList<Person> persons;
    int foundMethod;

    public Result() {
        this.synPerson = null;
        this.persons = new ArrayList<Person>();
    }

    public Person getSynPerson() {
        return synPerson;
    }

    public void setSynPerson(Person synPhoto) {
        this.synPerson = synPhoto;
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

    public void split() {
        this.synPerson = this.persons.get(0);
        this.persons.remove(0);
    }
}
