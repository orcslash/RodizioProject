package Services;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Reservation;

public class SQLiteDatabase extends RodizioDatabase
{

    private final String connectionURL;
    private Connection connection;
    private Statement stmt = null;
    private PreparedStatement preparedStmnt;
    String sql;

    /**
     * Source file has to end with .db
     */
    public SQLiteDatabase(String sourceFile)
    {
        registerDriver();
        connectionURL = "jdbc:sqlite:" + sourceFile;
    }

    @Override
    public int insertReservation(Reservation res)
    {
        sql = "INSERT INTO RESERVATIONS (NAME,EMAIL,PHONE,DATE,TIME,AMOUNT,NOTES,BDAY) "
                + "VALUES(?, ?, ?, ?, ?, ?, ?, ?)";

        return updateDatabaseWithPreparedStmnt(sql, res);
    }

    @Override
    public void dropTable(String table)
    {
        sql = "DROP TABLE IF EXISTS " + table;
        updateDatabase(sql);
    }

    @Override
    public void dropAllTables()
    {
        dropTable("USERS");
        dropTable("RESERVATIONS");
    }

    @Override
    public ArrayList<String> getTableNames()
    {
        createConnection();
        ArrayList<String> tables = getTableNamesFromDatabase();
        closeConnection();
        return tables;
    }

    private ArrayList<String> getTableNamesFromDatabase()
    {
        ArrayList<String> tables = new ArrayList<>();
        try
        {
            DatabaseMetaData md = connection.getMetaData();
            ResultSet rs = md.getTables(null, null, "%", null);
            while (rs.next())
            {
                tables.add(rs.getString(3));
            }
        } catch (SQLException ex)
        {
            Logger.getLogger(SQLiteDatabase.class.getName()).log(Level.SEVERE, null, ex);
        }
        return tables;
    }

    @Override
    public void createUsersTable()
    {
        sql = "CREATE TABLE IF NOT EXISTS USERS("
                + "id int(20)  PRIMARY KEY,"
                + "name VARCHAR(60) UNIQUE NOT NULL, "
                + "password VARCHAR(60) NOT NULL)";
        updateDatabase(sql);
    }

    @Override
    public void createReservationsTable()
    {
        sql = "CREATE TABLE IF NOT EXISTS RESERVATIONS "
                + "(ID INTEGER PRIMARY KEY,"
                + "NAME           VARCHAR(25)    NOT NULL, "
                + "EMAIL           VARCHAR(25)    NOT NULL, "
                + "PHONE           VARCHAR(12)    NOT NULL, "
                + "DATE           VARCHAR(9) NOT NULL,"
                + "TIME           VARCHAR(5) NOT NULL,"
                + " AMOUNT            INT     NOT NULL, "
                + " NOTES        TEXT, "
                + " BDAY        BOOLEAN NOT NULL)";
        updateDatabase(sql);
    }

    private int updateDatabaseWithPreparedStmnt(String sql, Reservation res)
    {
        createConnection();
        int id = executeReservationInsert(sql, res);
        closeConnection();

        return id;
    }

    private int executeReservationInsert(String sql1, Reservation res)
    {
        int id = 0;
        try
        {
            preparedStmnt = connection.prepareStatement(sql1);
            preparedStmnt.setString(1, res.getName());
            preparedStmnt.setString(2, res.getEmail());
            preparedStmnt.setString(3, res.getPhoneNum());
            preparedStmnt.setString(4, res.getDate());
            preparedStmnt.setString(5, res.getTime());
            preparedStmnt.setInt(6, res.getPeople());
            preparedStmnt.setString(7, res.getAdditionalNotes());
            preparedStmnt.setBoolean(8, res.isIsBirthday());
            preparedStmnt.executeUpdate();

            id = getLastInsertedIndex();

        } catch (SQLException ex)
        {
            Logger.getLogger(SQLiteDatabase.class.getName()).log(Level.SEVERE, null, ex);
        }
        return id;
    }

    private int getLastInsertedIndex() throws SQLException
    {
        int id;
        createStatement();
        ResultSet rs = stmt.executeQuery("SELECT last_insert_rowid() AS id;");
        id = rs.getInt("id");
        return id;
    }

    private void updateDatabase(String sql)
    {
        createConnection();
        createStatement();
        executeAndCloseUpdateStatement(sql);
        closeConnection();
    }

    private void executeAndCloseUpdateStatement(String sql)
    {
        try
        {
            stmt.executeUpdate(sql);
            stmt.close();

        } catch (SQLException ex)
        {
            Logger.getLogger(SQLiteDatabase.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void registerDriver()
    {
        try
        {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException ex)
        {
            Logger.getLogger(SQLiteDatabase.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void createAllTables()
    {
        createReservationsTable();
        createUsersTable();
    }

    private void createStatement()
    {
        try
        {
            stmt = connection.createStatement();
        } catch (SQLException ex)
        {
            Logger.getLogger(SQLiteDatabase.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void createConnection()
    {
        try
        {
            connection = DriverManager.getConnection(connectionURL);
        } catch (SQLException ex)
        {
            Logger.getLogger(SQLiteDatabase.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void closeConnection()
    {
        try
        {
            connection.close();
        } catch (SQLException ex)
        {
            Logger.getLogger(SQLiteDatabase.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
