package beans;

import Services.Database;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import model.Reservation;

@Named(value = "reservation")
@SessionScoped
public class ReservationBean implements Serializable
{

    private String name;
    private String email;
    private String date;
    private String time;
    private int people;
    private String additionalNotes;
    private boolean isBirthday;
    private String phoneNum;
    private int id = 0;

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String submit()
    {
        return "submit";
    }

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

    public String createReservation()
    {
        if (!validate())
        {
            return "home";
        }
        System.out.println("leng" + name.length());
        id = Database.createReservation(new Reservation(name, email, phoneNum, date, time, people, additionalNotes, isBirthday));
        resetValues();

        return "success";

    }

    /**
     * checks if all the required fields are not null or empty
     */
    private boolean validate()
    {
        return !(name == null
                || email == null
                || phoneNum == null
                || date == null
                || time == null)
                && !(name.length() == 0
                || email.length() == 0
                || phoneNum.length() == 0
                || date.length() == 0
                || time.length() == 0);
    }

    public String goToHome()
    {
        resetValues();
        return "home";
    }

    private void resetValues()
    {
        name = null;
        email = null;
        date = null;
        time = null;
        people = 1;
        additionalNotes = null;
        isBirthday = false;
        phoneNum = null;

    }

    public String getDate()
    {
        return date;
    }

    public void setDate(String date)
    {
        this.date = date;
        System.out.println(date);

    }

    public String getTime()
    {
        return time;
    }

    public void setTime(String time)
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

    public ReservationBean()
    {
        // try to create new table
//        Database.dropTable();
//        Database.createReservationsTable();

    }

}
