/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package classes;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author L
 */
public class InitialiseDB
{

    public static void main(String[] args)
    {
        Connection c = null;
        Statement stmt = null;
        try
        {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:RodizioDB.db");
            System.out.println("Databse file created and/or opened");

            stmt = c.createStatement();
            String sql = "CREATE TABLE RESERVATION "
                    + "(ID INT PRIMARY KEY     NOT NULL,"
                    + " NAME           VARCHAR(25)    NOT NULL, "
                    + "EMAIL   VARCHAR(25)    NOT NULL "
                    + "PHONE   VARCHAR(12)    NOT NULL "
                    + " AMOUNT            INT     NOT NULL, "
                    + " ADDRESS        CHAR(50), "
                    +
            )";
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
}
