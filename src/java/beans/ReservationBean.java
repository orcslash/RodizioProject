/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 *
 * @author L
 */
@Named(value = "reservation")
@SessionScoped
public class ReservationBean implements Serializable
{

    private String name;
    private String email;
    private LocalDate date;
    private LocalTime time;
    private int people;
    private String additionalNotes;
    private boolean isBirthday;
    private String phoneNum;

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

    public LocalDate getDate()
    {
        return date;
    }

    public void setDate(LocalDate date)
    {
        this.date = date;
    }

    public LocalTime getTime()
    {
        return time;
    }

    public void setTime(LocalTime time)
    {
        this.time = time;
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

    /**
     * Creates a new instance of ReservationBean
     */
    public ReservationBean()
    {
    }

}
