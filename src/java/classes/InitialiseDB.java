/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package classes;

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
public class InitialiseDB
{

    /*
    If your this is not working for you, you need to add SQLITE libarary to your libraries.
    Get the latest one from here https://bitbucket.org/xerial/sqlite-jdbc/downloads
    More thorough instructions here - http://www.tutorialspoint.com/sqlite/sqlite_java.htm

    To make a new database file, if it's not passed with git or if you dropped the tables,
    uncomment createTable();

     */
    public static void main(String[] args)
    {
//        createTable();
//        addDummyValues();
        dumpTable();
//        dropTables();
    }

    /**
     * Creates a new reservations table
     */
    public static void createTable()
    {
        Connection c = null;
        Statement stmt = null;
        try
        {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:RodizioDB.db");
            c.setAutoCommit(false);
            System.out.println("Databse file created and/or opened");

            stmt = c.createStatement();
            String sql = "CREATE TABLE RESERVATIONS "
                    + "(ID INT PRIMARY KEY,"
                    + " NAME           VARCHAR(25)    NOT NULL, "
                    + "EMAIL           VARCHAR(25)    NOT NULL, "
                    + "PHONE           VARCHAR(12)    NOT NULL, "
                    + "DATETIME           DATETIME NOT NULL,"
                    + " AMOUNT            INT     NOT NULL, "
                    + " NOTES        TEXT, "
                    + " BDAY        BOOLEAN NOT NULL )";
            stmt.executeUpdate(sql);
            stmt.close();
            c.close();
        } catch (ClassNotFoundException | SQLException e)
        {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }

        System.out.println("Table created successfully");
    }

    /**
     * Prints all the values of the reservations table
     */
    public static void dumpTable()
    {
        Connection c = null;
        Statement stmt = null;
        try
        {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:RodizioDB.db");
            c.setAutoCommit(false);
            System.out.println("Databse file created and/or opened");

            stmt = c.createStatement();

            ResultSet rs = stmt.executeQuery("SELECT * FROM RESERVATIONS;");
            while (rs.next())
            {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String email = rs.getString("email");
                String phone = rs.getString("phone");
                String date = rs.getString("datetime");
                int amount = rs.getInt("amount");
                String notes = rs.getString("notes");
                boolean bday = rs.getBoolean("bday");

                System.out.println("ID = " + id);
                System.out.println("NAME = " + name);
                System.out.println("EMAIL = " + email);
                System.out.println("PHONE = " + phone);
                System.out.println("datetime = " + date);
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
        SimpleDateFormat timeF = new SimpleDateFormat("hh:mm");
        Connection c = null;
        Statement stmt = null;
        try
        {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:RodizioDB.db");
            c.setAutoCommit(false);

            stmt = c.createStatement();
            String sql = "INSERT INTO RESERVATIONS (NAME,EMAIL,PHONE,DATETIME,AMOUNT,NOTES,BDAY) "
                    + "VALUES ( 'Paul', 'email@email.com', '888444', '" + dateF.format(new Date(System.currentTimeMillis())) + " " + timeF.format(new Date(System.currentTimeMillis()))
                    + "', 5, null, 0  );";
            System.out.println(sql);
            stmt.executeUpdate(sql);

            stmt.close();
            c.commit();
            c.close();
        } catch (ClassNotFoundException | SQLException e)
        {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        System.out.println("Records created successfully");
    }

    /**
     * Drops the reservation table
     */
    public static void dropTable()
    {
        Connection c = null;
        Statement stmt = null;
        try
        {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:RodizioDB.db");
            c.setAutoCommit(false);
            System.out.println("Databse file created and/or opened");

            stmt = c.createStatement();
            String sql = "drop table reservations";
            stmt.executeUpdate(sql);
            stmt.close();
            c.close();
        } catch (ClassNotFoundException | SQLException e)
        {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }

        System.out.println("Tables dropped");
    }
}
