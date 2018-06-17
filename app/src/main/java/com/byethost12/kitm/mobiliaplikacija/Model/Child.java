package com.byethost12.kitm.mobiliaplikacija.Model;

/**
 * Created by mokytojas on 2018-02-05.
 */

public class Child {
    private int id;
    private String user;
    private String name;
    private String surname;
    private String personId;
    private String education;
    private String activities;
    private String group;

    //Naudojamas cloudui
    public Child(int id, String user, String name, String surname, String personId, String education, String activities, String group) {
        this.id = id;
        this.user = user;
        this.name = name;
        this.surname = surname;
        this.personId = personId;
        this.education = education;
        this.activities = activities;
        this.group = group;
    }

    public Child(String user, String name, String surname, String personId, String education, String activities, String group) {
        this.user = user;
        this.name = name;
        this.surname = surname;
        this.personId = personId;
        this.education = education;
        this.activities = activities;
        this.group = group;
    }

    public Child() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUser() {return user;}

    public void setUser(String user) {this.user = user;}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getPersonId() {
        return personId;
    }

    public void setPersonId(String personId) {
        this.personId = personId;
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public String getActivities() {
        return activities;
    }

    public void setActivities(String activities) {
        this.activities = activities;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    @Override
    public String toString() {
        return "Child{" +
                "id=" + id +
                ", user='" + user + '\'' +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", personId='" + personId + '\'' +
                ", education='" + education + '\'' +
                ", activities='" + activities + '\'' +
                ", group='" + group + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Child) {
            Child child = (Child) o;
            boolean name = child.getName().equals(getName());
            boolean surname = child.getSurname().equals(getSurname());
            boolean personId = child.getPersonId().equals(getPersonId());
            boolean education = child.getEducation().equals(getEducation());
            boolean activities = child.getActivities().equals(getActivities());
            boolean group = child.getGroup().equals(getGroup());
            return (name
                    &&surname
                    &&personId
                    && education
                    && activities
                    && group);
        }
    return false;
    }
}
