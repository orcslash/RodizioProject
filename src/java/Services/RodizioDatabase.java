package Services;

import java.util.ArrayList;
import model.Reservation;

public abstract class RodizioDatabase
{

    public abstract void createConnection();

    public abstract void closeConnection();

    public abstract void createReservationsTable();

    public abstract void createUsersTable();

    public abstract ArrayList<String> getTableNames();

    public abstract void dropTable(String reservations);

    public abstract void dropAllTables();

    public abstract int insertReservation(Reservation res);

    public abstract void createAllTables();
}
