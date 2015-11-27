package beans;

import Services.Database;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import model.Reservation;

@Named(value = "lookup")
@SessionScoped
public class LookupBean implements Serializable
{

    public LookupBean()
    {
    }

    private int id;
    private String email;
    private boolean notFound;
    private Reservation res;
    private String name;

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        System.out.println("setter called");
        this.name = name;
    }

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

    public String submit()
    {
        Database.updateReservation(res);
        clearFields();
        return "home";
    }

    public String cancel()
    {
        clearFields();
        return "home";
    }

    public void clearFields()
    {
        this.id = 0;
        this.email = null;
        res = null;
    }

    public String delete()
    {
        Database.deleteReservation(res);
        clearFields();
        return "home";
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
