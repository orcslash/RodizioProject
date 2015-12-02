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

    protected final String connectionURL;
    private final String driver;
    protected Connection connection;
    private Statement stmt = null;
    String sql;
    private static final SimpleDateFormat DATEF = new SimpleDateFormat("dd-MM-yyyy");

    /**
     * Source file has to end with .db
     */
    public RodizioDB(String connectionURL, String driver)
    {
        this.driver = driver;
        this.connectionURL = connectionURL;
        registerDriver();
    }

    @Override
    public int insertReservation(Reservation res)
    {
        createConnection();
        PreparedStatement preparedStmnt = prepareStatementForReservationInsert(res);
        int id = executeReservationInsert(preparedStmnt);
        closeConnection();

        return id;
    }

    @Override
    public void dropTable(String table)
    {
        String sql1 = "DROP TABLE IF EXISTS " + table;
        updateDatabase(sql1);
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

    @Override
    public User checkUser(User user)
    {
        createConnection();
        PreparedStatement preparedStmnt = prepareUserCheckStatement(user);
        ArrayList<User> users = userQuery(preparedStmnt);
        closeConnection();

        return returnedUserArrayCheck(users);
    }

    @Override
    public void insertUser(User user)
    {
        String sql1 = "INSERT INTO USERS (name, password) VALUES('" + user.getName() + "'," + "'" + user.getPassword()
                + "')";
        updateDatabase(sql1);
    }

    @Override
    public void deleteUser(User user)
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void createUsersTable()
    {
        String sql1 = "CREATE TABLE IF NOT EXISTS USERS"
                + "(ID INTEGER PRIMARY KEY,"
                + "NAME VARCHAR(60) UNIQUE NOT NULL, "
                + "PASSWORD VARCHAR(60) NOT NULL)";
        updateDatabase(sql1);
    }

    @Override
    public void createReservationsTable()
    {
        String sql1 = "CREATE TABLE IF NOT EXISTS RESERVATIONS "
                + "(ID INTEGER PRIMARY KEY,"
                + "NAME           VARCHAR(25)    NOT NULL, "
                + "EMAIL           VARCHAR(25)    NOT NULL, "
                + "PHONE           VARCHAR(12)    NOT NULL, "
                + "DATE           VARCHAR(9) NOT NULL,"
                + "TIME           VARCHAR(5) NOT NULL,"
                + " AMOUNT            INT     NOT NULL, "
                + " NOTES        TEXT, "
                + " BDAY        BOOLEAN NOT NULL)";
        updateDatabase(sql1);
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

        createConnection();
        PreparedStatement preparedStatement = prepareUpdateReservationStatement(res);

        executeAndCloseUpdateStatement(preparedStatement);
        closeConnection();

    }

    @Override
    public void deleteReservation(Reservation res)
    {
        String sql1 = "DELETE FROM RESERVATIONS WHERE ID = " + res.getId();
        updateDatabase(sql1);
    }

    @Override
    public ArrayList<User> getAllUsers()
    {
//        sql = "SELECT *  FROM USERS";
//        return userQuery();
        return null;

    }

    @Override
    public void createAllTables()
    {
        createReservationsTable();
        createUsersTable();
    }

    @Override
    public void dumpReservationTable()
    {
        for (Reservation r : getAllReservations())
        {
            System.out.println(r.toString());
        }
    }

    @Override
    public void dumpUserTable()
    {
        for (User u : getAllUsers())
        {
            System.out.println(u.toString());
        }
    }

    @Override
    public void addDummyValues()
    {
        insertUser(new User("qq", "qq"));
        for (int i = 0; i < 10; i++)
        {
            insertReservation(pastOrFutureRes("past"));
            insertReservation(pastOrFutureRes("future"));
            insertUser(new User("Admin" + i, "admin"));
        }
    }

    private PreparedStatement prepareUpdateReservationStatement(Reservation res)
    {
        PreparedStatement preparedStatement = null;
        try
        {
            String sql1 = "UPDATE RESERVATIONS SET NAME=?,EMAIL=?,PHONE=?,DATE=?,TIME=?,AMOUNT=?,NOTES=?,BDAY= ? WHERE ID=?;";
            preparedStatement = connection.prepareStatement(sql1);
            preparedStatement.setString(1, res.getName());
            preparedStatement.setString(2, res.getEmail());
            preparedStatement.setString(3, res.getPhoneNum());
            preparedStatement.setString(4, res.getDate());
            preparedStatement.setString(5, res.getTime());
            preparedStatement.setInt(6, res.getPeople());
            preparedStatement.setString(7, res.getAdditionalNotes());
            preparedStatement.setBoolean(8, res.isIsBirthday());
            preparedStatement.setInt(9, res.getId());

        } catch (SQLException ex)
        {
            Logger.getLogger(RodizioDB.class.getName()).log(Level.SEVERE, null, ex);
        }

        return preparedStatement;
    }

    private User returnedUserArrayCheck(ArrayList<User> users)
    {
        if (users.isEmpty())
        {
            return null;
        }
        else
        {
            return users.get(0);
        }
    }

    private PreparedStatement prepareUserCheckStatement(User user)
    {
        PreparedStatement preparedStmnt = null;
        try
        {
            String sql1 = "SELECT * FROM USERS WHERE NAME = ?  AND PASSWORD = ?";
            preparedStmnt = connection.prepareStatement(sql1);
            preparedStmnt.setString(1, user.getName());
            preparedStmnt.setString(2, user.getPassword());

        } catch (SQLException ex)
        {
            Logger.getLogger(RodizioDB.class.getName()).log(Level.SEVERE, null, ex);
        }
        return preparedStmnt;
    }

    private Reservation pastOrFutureRes(String pastOrFuture)
    {
        Reservation res = prepareTestReservation();

        if (pastOrFuture.equalsIgnoreCase("future"))
        {
            res.setDate(DATEF.format(new Date().getTime() + (1000 * 60 * 60 * 24)));
        }
        else
        {
            res.setDate(DATEF.format(new Date().getTime() - (1000 * 60 * 60 * 24)));
        }
        return res;
    }

    private Reservation prepareTestReservation()
    {
        SimpleDateFormat timeF = new SimpleDateFormat("HH:mm");
        Reservation res = new Reservation("Test Subject", "test@test.com", "88 88 88 88", DATEF.format(new Date(System.currentTimeMillis())),
                timeF.format(new Date(System.currentTimeMillis())), 2, "", true);
        return res;
    }

    private int executeReservationInsert(PreparedStatement preparedStmnt)
    {
        int id = 0;
        try
        {
            preparedStmnt.executeUpdate();

            id = getLastInsertedIndex();

        } catch (SQLException ex)
        {
            Logger.getLogger(RodizioDB.class.getName()).log(Level.SEVERE, null, ex);
        }
        return id;
    }

    private ArrayList<Reservation> reservationQuery()
    {
        createConnection();
        createStatement();

        ArrayList<Reservation> reservations = executeReservationQuery();

        closeConnection();
        return reservations;
    }

    private ArrayList<User> userQuery(PreparedStatement stmt)
    {

        ArrayList<User> users = new ArrayList<>();
        try
        {
            ResultSet rs = stmt.executeQuery();
            while (rs.next())
            {
                User tmp = parseUserValues(rs);
                users.add(tmp);
            }

            rs.close();
            stmt.close();
        } catch (SQLException ex)
        {
            Logger.getLogger(RodizioDB.class.getName()).log(Level.SEVERE, null, ex);
        }
        return users;
    }

    private ArrayList<User> executeUserQuery()
    {
        ArrayList<User> users = new ArrayList<>();
        try
        {
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next())
            {
                User tmp = parseUserValues(rs);
                users.add(tmp);
            }

            rs.close();
            stmt.close();
        } catch (SQLException ex)
        {
            Logger.getLogger(RodizioDB.class.getName()).log(Level.SEVERE, null, ex);
        }
        return users;
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

    private void updateDatabase(String sql1)
    {
        createConnection();
        createStatement();
        executeAndCloseUpdateStatement(sql1);
        closeConnection();
    }

    private void executeAndCloseUpdateStatement(PreparedStatement stmnt)
    {
        try
        {
            stmnt.executeUpdate();
            stmnt.close();

        } catch (SQLException ex)
        {
            Logger.getLogger(RodizioDB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void executeAndCloseUpdateStatement(String sql1)
    {
        try
        {
            stmt.executeUpdate(sql1);
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

    protected void createConnection()
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

    private User parseUserValues(ResultSet rs) throws SQLException
    {
        String name = rs.getString("name");
        String password = rs.getString("password");
        User tmp = new User(name, password);
        return tmp;
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

    private PreparedStatement prepareStatementForReservationInsert(Reservation res)
    {
        sql = "INSERT INTO RESERVATIONS (NAME,EMAIL,PHONE,DATE,TIME,AMOUNT,NOTES,BDAY) "
                + "VALUES(?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement preparedStmnt = null;
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

        } catch (SQLException ex)
        {
            Logger.getLogger(RodizioDB.class.getName()).log(Level.SEVERE, null, ex);
        }
        return preparedStmnt;
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
