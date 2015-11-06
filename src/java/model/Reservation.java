/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.util.Date;

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

    public void setDate_time(String date_time)
    {
        this.date_time = date_time;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public int getPeople()
    {
        return people;
    }

    public void setPeople(int people)
    {
        this.people = people;
    }

    public String getAdditionalNotes()
    {
        return additionalNotes;
    }

    public void setAdditionalNotes(String additionalNotes)
    {
        this.additionalNotes = additionalNotes;
    }

    public boolean isIsBirthday()
    {
        return isBirthday;
    }

    public void setIsBirthday(boolean isBirthday)
    {
        this.isBirthday = isBirthday;
    }

    public String getPhoneNum()
    {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum)
    {
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
