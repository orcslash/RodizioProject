/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import Services.Database;
import static com.sun.org.apache.xalan.internal.lib.ExsltDatetime.date;
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

    public ReservationBean getResBean()
    {
        return resBean;
    }

    public void setResBean(ReservationBean resBean)
    {
        this.resBean = resBean;
    }

    private Reservation reservation;
    private ArrayList<Reservation> reservations;
    private Date date;

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
        return new Reservation("Lukas", "email", "8888", "04-11-2015", "11:00", 0, null, true);
    }

    public ArrayList<Reservation> getAllReservations()
    {
        reservations = Database.getAllReservations();
        return reservations;
    }

    public String createReservation()
    {
        resBean.createReservation();
        getAllReservations();
        return null;
    }

    /**
     * Creates a new instance of StaffBean
     */
    public StaffBean()
    {
        resBean = new ReservationBean();
        // for testing only

        Database.dropReservationTable();
        Database.createReservationsTable();
       for (int i = 0; i < 15; i++)
       {
            Database.addDummyValues();
        }
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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
    
     public void search(){    
          SimpleDateFormat sdf=new SimpleDateFormat("dd-MM-yyyy");
          reservations.clear();
          if(date!=null)
          {             
          reservations=Database.getAllReservations(sdf.format(date));
          }
          else
          {
          reservations=Database.getAllReservations();
          }
     }

}
