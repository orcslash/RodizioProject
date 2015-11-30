/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import Services.RodizioDatabase;
import Services.SQLiteDatabase;
import java.text.SimpleDateFormat;
import java.util.Date;
import model.Reservation;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author L
 */
public class TestRodizioDatabase
{

    private static RodizioDatabase database;

    @BeforeClass
    public static void setUpClass()
    {
        database = new SQLiteDatabase("Test.db");
    }

    @AfterClass
    public static void tearDownClass()
    {
        database.dropAllTables();
    }

    @Before
    public void setUp()
    {
        database.createAllTables();
        insertTestReservationValues(5);
    }

    @After
    public void tearDown()
    {
        database.dropAllTables();
    }

    @Test
    public void testCreateReservationsTable()
    {
        database.createReservationsTable();
        assertTrue(tableExists("RESERVATIONS"));
    }

    @Test
    public void testCreateUsersTable()
    {
        database.createUsersTable();
        assertTrue(tableExists("USERS"));
    }

    @Test
    public void testDropTable()
    {
        database.createReservationsTable();
        database.dropTable("RESERVATIONS");
        assertFalse(tableExists("RESERVATIONS"));
    }

    @Test
    public void testDropAllTables()
    {
        database.createAllTables();
        database.dropAllTables();
        assertTrue(database.getTableNames().isEmpty());
    }

    @Test
    public void testInsertReservation()
    {
        Reservation res = prepareTestReservation();
        assertFalse(database.insertReservation(res) == 0);
    }

    @Test
    public void testGetReservationByID()
    {
        int id = database.insertReservation(prepareTestReservation());
        Reservation tmp = database.getReservationById(id);
        assertTrue(tmp != null);
    }

    @Test
    public void testGetAllReservations()
    {
        assertTrue(database.getAllReservations().size() > 0);
    }

    private static Reservation prepareTestReservation()
    {
        SimpleDateFormat dateF = new SimpleDateFormat("dd-MM-yyyy");
        SimpleDateFormat timeF = new SimpleDateFormat("HH:mm");
        Reservation res = new Reservation("Test Subject", "test@test.com", "88 88 88 88", dateF.format(new Date(System.currentTimeMillis())),
                timeF.format(new Date(System.currentTimeMillis())), 2, "", true);
        return res;
    }

    private static void insertTestReservationValues(int amount)
    {
        Reservation res = prepareTestReservation();

        for (int i = 0; i < amount; i++)
        {
            database.insertReservation(res);
        }

    }

    private static boolean tableExists(String tableName)
    {
        return database.getTableNames().contains(tableName);
    }

}
