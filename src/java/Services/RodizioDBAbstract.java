package Services;

import java.util.ArrayList;
import model.Reservation;
import model.User;

public abstract class RodizioDBAbstract
{

    public abstract void createReservationsTable();

    public abstract void createUsersTable();

    public abstract void createAllTables();

    public abstract ArrayList<String> getTableNames();

    public abstract void dropTable(String reservations);

    public abstract void dropAllTables();

    public abstract int insertReservation(Reservation res);

    public abstract Reservation getReservationByIdAndMail(int id, String email);

    public abstract Reservation getReservationById(int i);

    public abstract ArrayList<Reservation> getAllReservations();

    public abstract ArrayList<Reservation> getFutureReservations();

    public abstract ArrayList<Reservation> getPastReservations();

    public abstract ArrayList<Reservation> getTodaysReservations();

    public abstract ArrayList<Reservation> getReservationsByDate(String date);

    public abstract void updateReservation(Reservation res);

    public abstract void deleteReservation(Reservation res);

    public abstract ArrayList<User> getAllUsers();

    public abstract User checkUser(User user);

}
