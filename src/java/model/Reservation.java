package model;

public class Reservation
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
    private boolean editable;
    private boolean edited;

    public boolean isEditable()
    {
        return editable;
    }

    public boolean isEdited()
    {
        return edited;
    }

    public void setEdited(boolean edited)
    {
        this.edited = edited;
    }

    public void setEditable(boolean editable)
    {
        this.editable = editable;
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

    public Reservation(String name, String email, String phoneNum, String date, String time, int people, String additionalNotes, boolean isBirthday)
    {
        this.name = name;
        this.email = email;
        this.date = date;
        this.time = time;
        this.people = people;
        this.additionalNotes = additionalNotes;
        this.isBirthday = isBirthday;
        this.phoneNum = phoneNum;
    }

    private void edited()
    {
        edited = true;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        if (this.name.equals(name))
        {
            return;
        }
        edited();
        this.name = name;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        if (this.email.equals(email))
        {
            return;
        }
        edited();
        this.email = email;
    }

    public int getPeople()
    {
        return people;
    }

    public void setPeople(int people)
    {
        if (this.people == people)
        {
            return;
        }
        edited();
        this.people = people;
    }

    public String getAdditionalNotes()
    {
        return additionalNotes;
    }

    public void setAdditionalNotes(String additionalNotes)
    {

        if (this.additionalNotes == null && additionalNotes == null)
        {
            return;
        }

        if ((compare(this.additionalNotes, additionalNotes)))
        {
            return;
        }
        if (additionalNotes == null)
        {
            return;
        }
        edited();
        this.additionalNotes = additionalNotes;
    }

    public boolean isIsBirthday()
    {
        return isBirthday;
    }

    public void setIsBirthday(boolean isBirthday)
    {
        if (this.isBirthday == isBirthday)
        {
            return;
        }
        edited();
        this.isBirthday = isBirthday;
    }

    public String getPhoneNum()
    {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum)
    {
        if (this.phoneNum.equals(phoneNum))
        {
            return;
        }
        edited();
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

    public static boolean compare(String str1, String str2)
    {
        return (str1 == null ? str2 == null : str1.equals(str2));
    }

    @Override
    public String toString()
    {
        return "Reservation{" + "name=" + name + ", email=" + email + ", date=" + date + ", time=" + time + ", people="
                + people + ", additionalNotes=" + additionalNotes + ", isBirthday=" + isBirthday + ", phoneNum=" + phoneNum
                + ", id=" + id + ", editable=" + editable + ", edited=" + edited + '}';
    }

}
