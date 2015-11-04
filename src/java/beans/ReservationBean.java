/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import Services.Database;
import Services.Database;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

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
    private Date date;
    private Date time;
    private int people;
    private String additionalNotes;
    private boolean isBirthday;
    private String phoneNum;

    @Override
    public String toString()
    {
        return "Name - " + name
                + "\nEmail -  " + email
                + "\nDate: " + date
                + "\nTime: " + time
                + "\n amount: " + people
                + "\n| notes " + additionalNotes
                + "\nisbday : " + isBirthday
                + "\nphone " + phoneNum;
    }

    public void createReservation()
    {
        Database.insertQuery(name, email, phoneNum, printDate(), printTime(), people, additionalNotes, isBirthday);

        Database.dumpTable();
    }

    public String printDate()
    {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        return dateFormat.format(date);
    }

    public String printTime()
    {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        return format.format(time);
    }

    public void setDate(Date date)
    {
        this.date = date;
    }

    public Date getDate()
    {
        return date;
    }

    public Date getTime()
    {
        return time;
    }

    public void setTime(Date time)
    {
        this.time = time;
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

    /**
     * Creates a new instance of ReservationBean
     */
    public ReservationBean()
    {
        // try to create new table
//        Database.dropTable();
        Database.createTable();

    }

}
