/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Services;

import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author L
 */
public class Database
{

    /*
      To be able to run this, read and execute the InitialiseDB file
     */
    public static void main(String[] args)
    {
        insertQuery("lukas", "emaik", "888", new SimpleDateFormat("dd-MM-yyyy").format(new Date(System.currentTimeMillis())),
                new SimpleDateFormat("HH:mm").format(new Date(System.currentTimeMillis())), 0, "some note", true);

        InitialiseDB.dumpTable();
    }

    /**
     * Prepares the statement for execution date and time are supposed to
     * already be formatted to string
     *
     * @param name
     * @param email
     * @param phone
     * @param date
     * @param time
     * @param amount
     * @param notes can be null
     * @param bday
     */
    public static void insertQuery(String name, String email, String phone, String date, String time, int amount, String notes, boolean bday)
    {
        String query = " '" + name + "',"
                + " '" + email + "',"
                + " '" + phone + "' ,"
                + " '" + date + " " + time + "' ,"
                + " '" + amount + "' ,"
                + " " + (notes == null ? "null" : "'" + notes + "'") + " ,"
                + " '" + (bday ? 1 : 0) + "' "; // SQLite does not allow bool values, only 0 or 1
        insert(query);
    }

    public static ResultSet selectQuery()
    {
        //TODO
        return null;
    }

    /**
     * Executes the query and inserts values
     *
     * @param query
     */
    private static void insert(String query)
    {

        String sql = "INSERT INTO RESERVATIONS (NAME,EMAIL,PHONE,DATE_TIME,AMOUNT,NOTES,BDAY) "
                + "VALUES(" + query + ")";
        InitialiseDB.executeCommand(sql);

        System.out.println("\nRecords created successfully");
    }
}
