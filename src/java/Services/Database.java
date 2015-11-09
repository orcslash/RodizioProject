package Services;

import static Services.Database.c;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import model.Reservation;

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
//        dropTable();
//        createTable();
//        addDummyValues();
        Reservation res = (new Reservation("Lukas", "email", "8888", "04-11-2015 11:00", 0, null, true));
        res.setId(createReservation(res));
        res.setName("ble");
        updateReservation(res);
        dumpTable();
    }

    public static void deleteReservation(Reservation res)
    {
        executeCommand("DELETE FROM RESERVATIONS WHERE ID = " + res.getId());
    }

    public static void updateReservation(Reservation res)
    {
        executeCommand("UPDATE RESERVATIONS SET NAME='" + res.getName() + "',EMAIL='" + res.getEmail() + "' ,PHONE='" + res.getPhoneNum()
                + "',DATE_TIME='" + res.getDate_time() + "',AMOUNT='" + res.getPeople()
                + "',NOTES='" + res.getAdditionalNotes() + "',BDAY='" + (res.isIsBirthday() ? 1 : 0)
                + "' WHERE ID=" + res.getId()
        );
    }

    /**
     * Returns all the reservations from the DB
     *
     * @return
     */
    public static ArrayList<Reservation> getAllReservations()
    {
        return query("SELECT * FROM RESERVATIONS;");
    }

    /**
     * Creates reservation in the DB returns the ID of the newly created
     * reservation
     *
     * @param res
     * @return
     */
    public static int createReservation(Reservation res)
    {
        String query = " '" + res.getName() + "',"
                + " '" + res.getEmail() + "',"
                + " '" + res.getPhoneNum() + "' ,"
                + " '" + res.getDate_time() + "',"
                + " '" + res.getPeople() + "' ,"
                + " " + (res.getAdditionalNotes() == null ? "null" : "'" + res.getAdditionalNotes() + "'") + " ,"
                + " '" + (res.isIsBirthday() ? 1 : 0) + "' "; // SQLite does not allow bool values, only 0 or 1

        return insert(query);
    }

    /**
     * Executes the query and inserts values
     *
     * @param query
     */
    private static int insert(String query)
    {

        String sql = "INSERT INTO RESERVATIONS (NAME,EMAIL,PHONE,DATE_TIME,AMOUNT,NOTES,BDAY) "
                + "VALUES(" + query + ")";

        return Database.executeCommand(sql);
    }

    public static int executeCommand(String command)
    {
        int id = 0;
        try
        {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection(CONNECTION);

            // this means that this whole query is going to be executed as a transaction
            c.setAutoCommit(false);

            stmt = c.createStatement();

            // insert values in the DB
            System.out.println(command);
            stmt.executeUpdate(command);

            // return the ID of the last inserted record
            ResultSet rs = stmt.executeQuery("SELECT last_insert_rowid() AS id;");

            id = rs.getInt("id");

            stmt.close();
            c.commit();
            c.close();
        } catch (ClassNotFoundException | SQLException e)
        {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        return id;
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
     * Prints all the reservations returned from the DB
     */
    public static void dumpTable()
    {
        for (Reservation r : getAllReservations())
        {
            System.out.println(r.toString());
        }
    }

    /**
     * Prints all the values of the reservations table
     *
     * @param query
     * @return
     */
    public static ArrayList<Reservation> query(String query)
    {
        ArrayList<Reservation> reservations = new ArrayList<>();
        try
        {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection(CONNECTION);
            c.setAutoCommit(false);

            stmt = c.createStatement();

            ResultSet rs = stmt.executeQuery(query);
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

                Reservation tmp = new Reservation(name, email, phone, date, amount, notes, bday);
                tmp.setId(id);
                reservations.add(tmp);
            }

            rs.close();
            stmt.close();
            c.close();
        } catch (ClassNotFoundException | SQLException e)
        {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        return reservations;
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
