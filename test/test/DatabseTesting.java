/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import Services.Database;
import java.text.SimpleDateFormat;
import java.util.Date;
import model.Reservation;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class DatabseTesting
{

    SimpleDateFormat dateF = new SimpleDateFormat("dd-MM-yyyy");
    SimpleDateFormat timeF = new SimpleDateFormat("HH:mm");

    public DatabseTesting()
    {
    }

    @BeforeClass
    public static void setUpClass()
    {
    }

    @AfterClass
    public static void tearDownClass()
    {
    }

    @Before
    public void setUp()
    {
        Database.createReservationsTable();
    }

    @After
    public void tearDown()
    {
    }

    @Test
    public void insertReservation()
    {
        Reservation res = new Reservation("Test subject", "test@test.com", "88888888", dateF.format(new Date(System.currentTimeMillis())),
                timeF.format(new Date(System.currentTimeMillis())), 1, "Test notes", true);
        res.setId(Database.createReservation(res));

        assertTrue(Database.getReservationByIdAndMail(res.getId(), res.getEmail()) != null);
    }

    @Test(expected = Database.SqlException.class)
    public void reservationTableCanBeDropped()
    {
        Database.dropReservationTable();
        Database.getAllReservations();
    }
}
