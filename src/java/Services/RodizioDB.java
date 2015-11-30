package Services;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Reservation;
import model.User;

public class RodizioDB extends RodizioDBAbstract
{

    private final String connectionURL;
    private final String driver;
    private Connection connection;
    private Statement stmt = null;
    private PreparedStatement preparedStmnt;
    String sql;
    private static final SimpleDateFormat DATEF = new SimpleDateFormat("dd-MM-yyyy");

    /**
     * Source file has to end with .db
     */
    public RodizioDB(String sourcePath, String driver)
    {
        this.driver = driver;
        connectionURL = sourcePath;
        registerDriver();
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
        updateDatabase();
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
            Logger.getLogger(RodizioDB.class.getName()).log(Level.SEVERE, null, ex);
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
        updateDatabase();
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
        updateDatabase();
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
            Logger.getLogger(RodizioDB.class.getName()).log(Level.SEVERE, null, ex);
        }
        return id;
    }

    @Override
    public Reservation getReservationById(int id)
    {
        sql = "SELECT * FROM RESERVATIONS WHERE ID = " + id;
        ArrayList<Reservation> tmpList = reservationQuery();
        if (tmpList.isEmpty())
        {
            return null;
        }
        else
        {
            return tmpList.get(0);
        }
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
            Logger.getLogger(RodizioDB.class.getName()).log(Level.SEVERE, null, ex);
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

    private void updateDatabase()
    {
        createConnection();
        createStatement();
        executeAndCloseUpdateStatement();
        closeConnection();
    }

    private void executeAndCloseUpdateStatement()
    {
        try
        {
            stmt.executeUpdate(sql);
            stmt.close();

        } catch (SQLException ex)
        {
            Logger.getLogger(RodizioDB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void registerDriver()
    {
        try
        {
            Class.forName(driver);
        } catch (ClassNotFoundException ex)
        {
            Logger.getLogger(RodizioDB.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(RodizioDB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void createConnection()
    {
        try
        {
            connection = DriverManager.getConnection(connectionURL);
        } catch (SQLException ex)
        {
            Logger.getLogger(RodizioDB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void closeConnection()
    {
        try
        {
            connection.close();
        } catch (SQLException ex)
        {
            Logger.getLogger(RodizioDB.class.getName()).log(Level.SEVERE, null, ex);
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
        Reservation res = getReservationById(id);
        if (res == null || !res.getEmail().equals(email))
        {
            return null;
        }
        else
        {
            return res;
        }

    }

    @Override
    public ArrayList<Reservation> getFutureReservations()
    {
        return getPastOrFutureReservations("future");
    }

    @Override
    public ArrayList<Reservation> getPastReservations()
    {
        return getPastOrFutureReservations("past");
    }

    @Override
    public ArrayList<Reservation> getReservationsByDate(String date)
    {
        sql = "SELECT * FROM RESERVATIONS WHERE DATE= '" + date + "' ";
        return reservationQuery();
    }

    @Override
    public ArrayList<Reservation> getTodaysReservations()
    {
        return getReservationsByDate(DATEF.format(new Date()));
    }

    @Override
    public void updateReservation(Reservation res)
    {
        sql = "UPDATE RESERVATIONS SET NAME='" + res.getName() + "',EMAIL='" + res.getEmail() + "',PHONE='" + res.getPhoneNum()
                + "',DATE='" + res.getDate() + "',TIME='" + res.getTime() + "',AMOUNT='" + res.getPeople()
                + "',NOTES=" + (res.getAdditionalNotes() == null ? " null " : "'" + res.getAdditionalNotes() + "'") + ",BDAY='" + (res.isIsBirthday() ? 1 : 0)
                + "' WHERE ID=" + res.getId() + ";";
        updateDatabase();
    }

    @Override
    public void deleteReservation(Reservation res)
    {
        sql = "DELETE FROM RESERVATIONS WHERE ID = " + res.getId();
        updateDatabase();
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

    @Override
    public void insertUser(User user)
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void deleteUser(User user)
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * @param pastOrFuture "past" for past reservations, "future" for future
     */
    private ArrayList<Reservation> getPastOrFutureReservations(String pastOrFuture)
    {
        // slow workaround for SQLite not having a normal Date datatype
        SimpleDateFormat dateF = new SimpleDateFormat("dd-MM-yyyy");
        String today = dateF.format(new Date(System.currentTimeMillis()));
        ArrayList<Reservation> tmp = getAllReservations();
        ArrayList<Reservation> pastReservations = new ArrayList<>();

        for (Reservation r : tmp)
        {
            if (compareWIthToday(r, pastOrFuture, today))
            {
                pastReservations.add(r);
            }
        }
        return pastReservations;
    }

    private boolean compareWIthToday(Reservation r, String pastOrFuture, String today)
    {
        if (pastOrFuture.equals("past"))
        {
            return compareDates(r.getDate(), today) < 0;
        }

        if (pastOrFuture.equals("future"))
        {
            return compareDates(r.getDate(), today) > 0;
        }

        return false;
    }

    private int compareDates(String d1, String d2)
    {
        if (d1.equals(d2))
        {
            return 0;
        }

        int year1 = parseYear(d1);
        int year2 = parseYear(d2);

        if (year1 > year2)
        {
            return 1;
        }
        if (year1 < year2)
        {
            return -1;
        }

        int month1 = parseMonth(d1);
        int month2 = parseMonth(d2);

        if (month1 > month2)
        {
            return 1;
        }

        if (month1 < month2)
        {
            return -1;
        }

        int day1 = parseDay(d1);
        int day2 = parseDay(d2);

        if (day1 < day2)
        {
            return -1;
        }

        if (day1 > day2)
        {
            return 1;
        }
        return 0;
    }

    private int parseDay(String d1) throws NumberFormatException
    {
        int day1 = Integer.parseInt(d1.substring(0, 2));
        return day1;
    }

    private int parseMonth(String d1) throws NumberFormatException
    {
        int month1 = Integer.parseInt(d1.substring(3, 5));
        return month1;
    }

    private int parseYear(String date) throws NumberFormatException
    {
        int year = Integer.parseInt(date.substring(6));
        return year;
    }

}
