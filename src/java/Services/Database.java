/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Services;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author L
 */
public class Database
{

    static Connection c = null;
    static Statement stmt = null;
    private static final String CONNECTION = "jdbc:sqlite:RodizioDB.db";

    /*
    If  this is not working for you, you need to add SQLITE libarary to your libraries.
    Get the latest one from here https://bitbucket.org/xerial/sqlite-jdbc/downloads
    More thorough instructions here - http://www.tutorialspoint.com/sqlite/sqlite_java.htm

    To make a new database file, if it's not passed with git or if you dropped the tables,
    uncomment createTable();
     */
    public static void main(String[] args)
    {
        dropTable();
        createTable();
        addDummyValues();
        dumpTable();
    }

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
        Database.executeCommand(sql);

        System.out.println("\nRecords created successfully");
    }

    public static void executeCommand(String command)
    {
        try
        {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection(CONNECTION);
            c.setAutoCommit(false);

            stmt = c.createStatement();

            System.out.println(command);
            stmt.executeUpdate(command);

            stmt.close();
            c.commit();
            c.close();
        } catch (ClassNotFoundException | SQLException e)
        {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
    }

    /**
     * Creates a new reservations table
     */
    public static void createTable()
    {
        String sql = "CREATE TABLE IF NOT EXISTS RESERVATIONS "
                + "(ID INTEGER PRIMARY KEY,"
                + "NAME           VARCHAR(25)    NOT NULL, "
                + "EMAIL           VARCHAR(25)    NOT NULL, "
                + "PHONE           VARCHAR(12)    NOT NULL, "
                + "DATE_TIME           DATETIME NOT NULL,"
                + " AMOUNT            INT     NOT NULL, "
                + " NOTES        TEXT, "
                + " BDAY        BOOLEAN NOT NULL)";
        executeCommand(sql);

        System.out.println("\nTable created successfully");
    }

    /**
     * Prints all the values of the reservations table
     */
    public static void dumpTable()
    {

        try
        {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection(CONNECTION);
            c.setAutoCommit(false);

            stmt = c.createStatement();

            ResultSet rs = stmt.executeQuery("SELECT * FROM RESERVATIONS;");
            while (rs.next())
            {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String email = rs.getString("email");
                String phone = rs.getString("phone");
                String date = rs.getString("date_time");
                int amount = rs.getInt("amount");
                String notes = rs.getString("notes");
                boolean bday = rs.getBoolean("bday");

                System.out.println("ID = " + id);
                System.out.println("NAME = " + name);
                System.out.println("EMAIL = " + email);
                System.out.println("PHONE = " + phone);
                System.out.println("date_time = " + date);
                System.out.println("amount = " + amount);
                System.out.println("notes = " + notes);
                System.out.println("bday = " + bday);
                System.out.println();
            }

            rs.close();
            stmt.close();
            c.close();
        } catch (ClassNotFoundException | SQLException e)
        {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
    }

    /**
     * Adds dummy values to the table
     */
    public static void addDummyValues()
    {
        SimpleDateFormat dateF = new SimpleDateFormat("dd-MM-yyyy");
        SimpleDateFormat timeF = new SimpleDateFormat("HH:mm");

        String sql = "INSERT INTO RESERVATIONS (NAME,EMAIL,PHONE,DATE_TIME,AMOUNT,NOTES,BDAY) "
                + "VALUES ( 'Paul', 'email@email.com', '888444', '" + dateF.format(new Date(System.currentTimeMillis())) + " " + timeF.format(new Date(System.currentTimeMillis()))
                + "', 5, null, 0  );";
        executeCommand(sql);
        System.out.println("\nRecord created successfully");
    }

    /**
     * Drops the reservation table
     */
    public static void dropTable()
    {
        String sql = "DROP TABLE IF EXISTS RESERVATIONS";
        executeCommand(sql);
        System.out.println("\nTable dropped");
    }
}
