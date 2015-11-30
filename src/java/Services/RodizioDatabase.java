package Services;

import java.util.ArrayList;

public abstract class RodizioDatabase
{

    public abstract void createConnection();

    public abstract void closeConnection();

    public abstract void createReservationsTable();

    public abstract void createUsersTable();

    public abstract ArrayList<String> getTableNames();

    public abstract void dropTable(String reservations);
}
