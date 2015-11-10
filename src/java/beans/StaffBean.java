/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import Services.Database;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.ArrayList;
import model.Reservation;

/**
 *
 * @author L
 */
@Named(value = "staff")
@SessionScoped
public class StaffBean implements Serializable
{

    private String name;
    private String email;
    private String date;
    private String time;
    private int people = 1;
    private String additionalNotes;
    private boolean isBirthday = false;
    private String phoneNum;
    private Reservation reservation;
    private ArrayList<Reservation> reservations;

    public String createReservation()
    {
        System.out.println("creating res");
        Database.createReservation(new Reservation(name, email, phoneNum, (date + " " + time), people, additionalNotes, isBirthday));
        Database.dumpTable();
        getAllReservations();
        return null;
    }

    public String deleteReservation(Reservation res)
    {
        Database.deleteReservation(res);
        getAllReservations();
        return null;
    }

    public String saveReservation(Reservation res)
    {
        res.setEditable(false);
        if (res.isEdited())
        {
            Database.updateReservation(res);
        }
        res.setEdited(false);
        return null;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        System.out.println(name);
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

    public String getDate()
    {
        return date;
    }

    public void setDate(String date)
    {
        this.date = date;
    }

    public String getTime()
    {
        return time;
    }

    public void setTime(String time)
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

    public String editReservation(Reservation res)
    {
        res.setEditable(true);
        return null;
    }

    public Reservation getReservation()
    {
        return reservation;
    }

    public void setReservation(Reservation reservation)
    {
        this.reservation = reservation;
    }

    public Reservation getRes()
    {
        return new Reservation("Lukas", "email", "8888", "04-11-2015 11:00", 0, null, true);
    }

    public ArrayList<Reservation> getAllReservations()
    {
        reservations = Database.getAllReservations();
        return reservations;
    }

    /**
     * Creates a new instance of StaffBean
     */
    public StaffBean()
    {
        // for testing only

//        Database.dropTable();
        Database.createTable();
//        for (int i = 0; i < 15; i++)
//        {
//            Database.addDummyValues();
//        }

        getAllReservations();
    }

    public ArrayList<Reservation> getReservations()
    {
        return reservations;
    }

    public void setReservations(ArrayList<Reservation> reservations)
    {
        this.reservations = reservations;
    }

}
