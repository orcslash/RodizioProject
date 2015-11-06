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

    public Reservation getRes()
    {
        return new Reservation("Lukas", "email", "8888", "04-11-2015 11:00", 0, null, true);
    }

    public ArrayList<Reservation> getAllReservations()
    {
        return Database.getAllReservations();
    }

    /**
     * Creates a new instance of StaffBean
     */
    public StaffBean()
    {
        // for testing only

        Database.createTable();
        for (int i = 0; i < 15; i++)
        {
            Database.addDummyValues();
        }
    }

}
