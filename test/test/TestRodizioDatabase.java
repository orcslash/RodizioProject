/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import Services.RodizioDBAbstract;
import Services.RodizioDBFactory;
import java.text.SimpleDateFormat;
import java.util.Date;
import model.Reservation;
import model.User;
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

    private static RodizioDBAbstract database;
    private static final SimpleDateFormat dateF = new SimpleDateFormat("dd-MM-yyyy");

    @BeforeClass
    public static void setUpClass()
    {
        database = RodizioDBFactory.getRodizioDB("sqlite");
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

    @Test
    public void testGetReservationByIdAndMail()
    {
        Reservation res = prepareTestReservation();
        res.setId(database.insertReservation(res));
        Reservation tmp = database.getReservationByIdAndMail(res.getId(), res.getEmail());
        assertTrue(tmp.getId() == res.getId() && tmp.getEmail().equals(res.getEmail()));
    }

    @Test
    public void testGetFutureReservations()
    {
        database.insertReservation(pastOrFutureRes("future"));
        assertTrue(database.getFutureReservations().size() > 0);
    }

    @Test
    public void testGetPastReservations()
    {
        database.insertReservation(pastOrFutureRes("past"));
        assertTrue(database.getPastReservations().size() > 0);
    }

    @Test
    public void testGetReservationByDate()
    {
        assertTrue(database.getReservationsByDate(dateF.format(new Date())).size() > 0);
    }

    @Test
    public void testGetTodaysReservations()
    {
        assertTrue(database.getTodaysReservations().size() > 0);
    }

    @Test
    public void testUpdateReservation()
    {
        Reservation res = database.getReservationById(1);
        res.setName("New name");
        database.updateReservation(res);
        Reservation tmp = database.getReservationById(res.getId());
        assertTrue(res.getName().equals(tmp.getName()));
    }

    @Test
    public void testDeleteReservation()
    {
        Reservation res = prepareTestReservation();
        res.setId(database.insertReservation(res));
        database.deleteReservation(res);
        assertTrue(database.getReservationById(res.getId()) == null);
    }

    @Test
    public void testInsertUser()
    {
        User user = new User("admin", "admin");
        database.insertUser(user);

    }

    @Test
    public void testGetAllUsers()
    {
        insertTestUserValues(5);
        assertTrue(database.getAllUsers().size() >= 5);
    }

    @Test
    public void testCheckUser()
    {
        User user = new User("Test", "test");
        database.insertUser(user);
        assertTrue(database.checkUser(user) != null);
        user.setName("TestWrong");
        assertTrue(database.checkUser(user) == null);

    }

    private void insertTestUserValues(int amount)
    {
        for (int i = 0; i < amount; i++)
        {
            database.insertUser(new User("Test" + i, "test"));
        }
    }

    private Reservation pastOrFutureRes(String pastOrFuture)
    {
        Reservation res = prepareTestReservation();

        if (pastOrFuture.equalsIgnoreCase("future"))
        {
            res.setDate(dateF.format(new Date().getTime() + (1000 * 60 * 60 * 24)));
        }
        else
        {
            res.setDate(dateF.format(new Date().getTime() - (1000 * 60 * 60 * 24)));
        }
        return res;
    }

    private static Reservation prepareTestReservation()
    {
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
