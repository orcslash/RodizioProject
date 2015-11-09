/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

/**
 *
 * @author L
 */
public class Reservation
{

    private String name;
    private String email;
    private String date_time;
    private int people;
    private String additionalNotes;
    private boolean isBirthday;
    private String phoneNum;
    private int id = 0;
    private boolean editable;
    private boolean edited;

    public boolean isEditable()
    {
        return editable;
    }

    public boolean isEdited()
    {
        return edited;
    }

    public void setEdited(boolean edited)
    {
        this.edited = edited;
    }

    public void setEditable(boolean editable)
    {
        this.editable = editable;
    }

    public Reservation(String name, String email, String phoneNum, String date_time, int people, String additionalNotes, boolean isBirthday)
    {
        this.name = name;
        this.email = email;
        this.date_time = date_time;
        this.people = people;
        this.additionalNotes = additionalNotes;
        this.isBirthday = isBirthday;
        this.phoneNum = phoneNum;
    }

    public String getDate_time()
    {
        return date_time;
    }

    private void edited()
    {
        edited = true;
    }

    public void setDate_time(String date_time)
    {
        edited();
        this.date_time = date_time;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        edited();
        this.name = name;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        edited();
        this.email = email;
    }

    public int getPeople()
    {
        return people;
    }

    public void setPeople(int people)
    {
        edited();
        this.people = people;
    }

    public String getAdditionalNotes()
    {
        return additionalNotes;
    }

    public void setAdditionalNotes(String additionalNotes)
    {
        edited();
        this.additionalNotes = additionalNotes;
    }

    public boolean isIsBirthday()
    {
        return isBirthday;
    }

    public void setIsBirthday(boolean isBirthday)
    {
        edited();
        this.isBirthday = isBirthday;
    }

    public String getPhoneNum()
    {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum)
    {
        edited();
        this.phoneNum = phoneNum;
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    @Override
    public String toString()
    {
        return "Reservation{" + "name=" + name + ", email=" + email + ", date_time=" + date_time + ", people=" + people + ", additionalNotes=" + additionalNotes + ", isBirthday=" + isBirthday + ", phoneNum=" + phoneNum + ", id=" + id + '}';
    }

}
