/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import Services.Database;
import model.Reservation;

/**
 *
 * @author L
 */
public class LookupBean
{

    private int id;
    private String email;
    private boolean notFound;
    private Reservation res;

    public Reservation getRes()
    {
        return res;
    }

    public void setRes(Reservation res)
    {
        this.res = res;
    }

    public boolean isNotFound()
    {
        return notFound;
    }

    public void setNotFound(boolean notFound)
    {
        this.notFound = notFound;
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public String lookup()
    {
        res = Database.getReservationByIdAndMail(this.id, this.email);
        if (res == null)
        {
            notFound = true;
            return null;
        }
        else
        {
            notFound = false;
            return "found";
        }
    }

}
