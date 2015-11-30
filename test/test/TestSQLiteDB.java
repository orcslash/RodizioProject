/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import Services.RodizioDatabase;
import Services.SQLiteDatabase;
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
public class TestSQLiteDB
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
        dropAllTables();
    }

    @Before
    public void setUp()
    {
    }

    @After
    public void tearDown()
    {
    }

    @Test
    public void testOpenAndCloseConnection()
    {
        database.createConnection();
        database.closeConnection();
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

    private static boolean tableExists(String tableName)
    {
        return database.getTableNames().contains(tableName);
    }

    private static void dropAllTables()
    {
        database.dropTable("RESERVATIONS");
        database.dropTable("USERS");
    }
}
