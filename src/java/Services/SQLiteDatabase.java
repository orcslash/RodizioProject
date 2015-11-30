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
import model.User;

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

        return updateDatabaseWithPreparedStmnt(res);
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

    private int updateDatabaseWithPreparedStmnt(Reservation res)
    {
        createConnection();
        int id = executeReservationInsert(res);
        closeConnection();

        return id;
    }

    private int executeReservationInsert(Reservation res)
    {
        int id = 0;
        try
        {
            preparedStmnt = connection.prepareStatement(sql);
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

    @Override
    public Reservation getReservationById(int id)
    {
        sql = "SELECT * FROM RESERVATIONS WHERE ID = " + id;
        return reservationQuery().get(0);
    }

    @Override
    public ArrayList<Reservation> getAllReservations()
    {
        sql = "SELECT * FROM RESERVATIONS";
        return reservationQuery();
    }

    private ArrayList<Reservation> reservationQuery()
    {

        createConnection();
        createStatement();

        ArrayList<Reservation> reservations = executeReservationQuery();

        closeConnection();
        return reservations;
    }

    private ArrayList<Reservation> executeReservationQuery()
    {
        ArrayList<Reservation> reservations = new ArrayList<>();
        try
        {
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next())
            {
                Reservation tmp = parseReservationValues(rs);
                reservations.add(tmp);
            }

            rs.close();
            stmt.close();
        } catch (SQLException ex)
        {
            Logger.getLogger(SQLiteDatabase.class.getName()).log(Level.SEVERE, null, ex);
        }
        return reservations;
    }

    private int getLastInsertedIndex() throws SQLException
    {
        int id;
        createStatement();
        ResultSet rs = stmt.executeQuery("SELECT last_insert_rowid() AS id;");
        id = rs.getInt("id");
        stmt.close();
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

    private void createConnection()
    {
        try
        {
            connection = DriverManager.getConnection(connectionURL);
        } catch (SQLException ex)
        {
            Logger.getLogger(SQLiteDatabase.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void closeConnection()
    {
        try
        {
            connection.close();
        } catch (SQLException ex)
        {
            Logger.getLogger(SQLiteDatabase.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private Reservation parseReservationValues(ResultSet rs) throws SQLException
    {
        int id = rs.getInt("id");
        String name = rs.getString("name");
        String email = rs.getString("email");
        String phone = rs.getString("phone");
        String date = rs.getString("date");
        String time = rs.getString("time");
        int amount = rs.getInt("amount");
        String notes = rs.getString("notes");
        boolean bday = rs.getBoolean("bday");
        Reservation tmp = new Reservation(name, email, phone, date, time, amount, notes, bday);
        tmp.setId(id);
        return tmp;
    }

    @Override
    public Reservation getReservationByIdAndMail(int id, String email)
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ArrayList<Reservation> getFutureReservations()
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ArrayList<Reservation> getPastReservations()
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ArrayList<Reservation> getTodaysReservations()
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ArrayList<Reservation> getReservationsByDate(String date)
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void updateReservation(Reservation res)
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void deleteReservation(Reservation res)
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ArrayList<User> getAllUsers()
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public User checkUser(User user)
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
