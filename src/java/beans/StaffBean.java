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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import javax.faces.bean.ManagedProperty;
import model.Reservation;

/**
 *
 * @author L
 */
@Named(value = "staff")
@SessionScoped
public class StaffBean implements Serializable
{

    @ManagedProperty(value = "#{resBean}")
    private ReservationBean resBean;

    private Reservation reservation;
    private ArrayList<Reservation> reservations;
    private Date date;
    private String tableTitle;

    public String deleteReservation(Reservation res)
    {
        Database.deleteReservation(res);
        getAllReservations();
        return null;
    }

    public String getTableTitle()
    {
        return tableTitle;
    }

    public void setTableTitle(String tableTitle)
    {
        this.tableTitle = tableTitle;
    }

    public ReservationBean getResBean()
    {
        return resBean;
    }

    public void setResBean(ReservationBean resBean)
    {
        this.resBean = resBean;
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

    public void getPastReservations()
    {
        reservations = Database.getPastReservations();
    }

    public void getFutureReservations()
    {
        reservations = Database.getFutureReservations();
    }

    public void getTodaysReservations()
    {
        SimpleDateFormat dateF = new SimpleDateFormat("dd-MM-yyyy");
        String today = dateF.format(new Date(System.currentTimeMillis()));
        reservations = Database.getReservationsByDate(today);
    }

    public void getAllReservations()
    {
        reservations = Database.getAllReservations();
    }

    public String createReservation()
    {
        resBean.createReservation();
        getAllReservations();
        return null;
    }

    public StaffBean()
    {
        resBean = new ReservationBean();

        Database.createReservationsTable();
        Database.createUserTable();
        Database.addDummyValues();

        this.getTodaysReservations();
    }

    public ArrayList<Reservation> getReservations()
    {
        return reservations;
    }

    public Date getDate()
    {
        return date;
    }

    public void setDate(Date date)
    {
        this.date = date;
    }

    public void search()
    {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        if (date != null)
        {
            reservations = Database.getReservationsByDate(sdf.format(date));
        }
    }

}
