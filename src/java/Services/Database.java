package Services;

import java.util.ArrayList;
import model.Reservation;
import model.User;

public class Database
{

    private static final RodizioDBAbstract DB = RodizioDBFactory.getRodizioDB("sqlite", "RodizioDB.DB");

    public static Reservation getReservationByIdAndMail(int id, String email)
    {
        return DB.getReservationByIdAndMail(id, email);
    }

    public static User checkUser(User user)
    {
        return DB.checkUser(user);
    }

    public static void createUserTable()
    {
        DB.createUsersTable();
    }

    public static void insertUser(User user)
    {
        DB.insertUser(user);
    }

    public static void deleteReservation(Reservation res)
    {
        DB.deleteReservation(res);
    }

    public static void updateReservation(Reservation res)
    {
        DB.updateReservation(res);
    }

    public static ArrayList<Reservation> getAllReservations()
    {
        return DB.getAllReservations();
    }

    public static ArrayList<Reservation> getReservationsByDate(String date)
    {
        return DB.getReservationsByDate(date);
    }

    public static ArrayList<Reservation> getPastReservations()
    {
        return DB.getPastReservations();
    }

    public static ArrayList<Reservation> getFutureReservations()
    {
        return DB.getFutureReservations();
    }

    public static ArrayList<User> getAllUsers()
    {
        return DB.getAllUsers();
    }

    public static int createReservation(Reservation res)
    {
        return DB.insertReservation(res);
    }

    public static void createReservationsTable()
    {
        DB.createReservationsTable();
    }

    public static void dumpReservationTable()
    {
        DB.dumpReservationTable();
    }

    public static void dumpUserTable()
    {
        DB.dumpUserTable();
    }

    public static void addDummyValues()
    {
        DB.addDummyValues();
    }

    public static void dropReservationTable()
    {
        DB.dropTable("RESERVATIONS");
    }

    public static void dropUserTable()
    {
        DB.dropTable("USERS");
    }

    public static void dropTable(String table)
    {
        DB.dropTable(table);
    }

    public static ArrayList<Reservation> getTodaysReservations()
    {
        return DB.getTodaysReservations();
    }

}
