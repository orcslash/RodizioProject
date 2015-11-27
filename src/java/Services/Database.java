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
import model.User;

public class Database
{

    static Connection c = null;
    static Statement stmt = null;
    private static final String CONNECTION = "jdbc:sqlite:RodizioDB.db";

    public static void main(String[] args)
    {
//        dropReservationTable();
//        createReservationsTable();
//        addDummyValues();
//        dumpReservationTable();
//        Reservation res = (new Reservation("Lukas", "email", "8888", "04-11-2015", "11:00", 0, null, true));
//        res.setId(createReservation(res));
//        res.setName("ble");
//        updateReservation(res);
//        dumpTable();
//        dropUserTable();
//        createUserTable();
//        insertUser(new User("sss", "ble"));
//        dumpUserTable();
    }

    public static Reservation getReservationByIdAndMail(int id, String email)
    {
        ArrayList<Reservation> reserv = reservationQuery("SELECT * FROM RESERVATIONS WHERE ID=" + id + " AND EMAIL=" + "'" + email + "'");
        if (reserv.isEmpty())
        {
            return null;
        }
        else
        {
            return reserv.get(0);
        }
    }

    public static User checkUser(User user)
    {
        ArrayList<User> usrs = userQuery("SELECT * FROM USERS WHERE NAME ='" + user.getName() + "' AND PASSWORD = '" + user.getPassword()
                + "'");
        if (usrs.isEmpty())
        {
            return null;
        }
        else
        {
            return usrs.get(0);
        }
    }

    public static void createUserTable()
    {
        executeCommand("CREATE TABLE IF NOT EXISTS USERS("
                + "id int(20)  PRIMARY KEY,"
                + "name VARCHAR(60) UNIQUE NOT NULL, "
                + "password VARCHAR(60) NOT NULL)");
    }

    public static void insertUser(User user)
    {
        executeCommand("INSERT INTO USERS (name, password) VALUES('" + user.getName() + "'," + "'" + user.getPassword()
                + "')");
    }

    public static void deleteReservation(Reservation res)
    {
        executeCommand("DELETE FROM RESERVATIONS WHERE ID = " + res.getId());
    }

    public static void updateReservation(Reservation res)
    {
        executeCommand("UPDATE RESERVATIONS SET NAME='" + res.getName() + "',EMAIL='" + res.getEmail() + "',PHONE='" + res.getPhoneNum()
                + "',DATE='" + res.getDate() + "',TIME='" + res.getTime() + "',AMOUNT='" + res.getPeople()
                + "',NOTES=" + (res.getAdditionalNotes() == null ? " null " : "'" + res.getAdditionalNotes() + "'") + ",BDAY='" + (res.isIsBirthday() ? 1 : 0)
                + "' WHERE ID=" + res.getId() + ";"
        );
    }

    public static ArrayList<Reservation> getAllReservations()
    {
        return reservationQuery("SELECT * FROM RESERVATIONS;");
    }

    public static ArrayList<Reservation> getReservationsByDate(String date)
    {
        return reservationQuery("SELECT * FROM RESERVATIONS WHERE DATE= '" + date + "' ");
    }

    public static ArrayList<Reservation> getPastReservations()
    {
        return getPastOrFutureReservations("past");
    }

    public static ArrayList<Reservation> getFutureReservations()
    {
        return getPastOrFutureReservations("future");
    }

    /**
     * @param pastOrFuture "past" for past reservations, "future" for future
     */
    public static ArrayList<Reservation> getPastOrFutureReservations(String pastOrFuture)
    {
        // slow workaround for SQLite not having a normal Date datatype
        SimpleDateFormat dateF = new SimpleDateFormat("dd-MM-yyyy");
        String today = dateF.format(new Date(System.currentTimeMillis()));
        ArrayList<Reservation> tmp = getAllReservations();
        ArrayList<Reservation> pastReservations = new ArrayList<>();

        for (Reservation r : tmp)
        {
            System.out.print(r);
            if (compareWIthToday(r, pastOrFuture, today))
            {
                pastReservations.add(r);
            }
        }
        return pastReservations;
    }

    private static boolean compareWIthToday(Reservation r, String pastOrFuture, String today)
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

    private static int compareDates(String d1, String d2)
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

    private static int parseDay(String d1) throws NumberFormatException
    {
        int day1 = Integer.parseInt(d1.substring(0, 2));
        return day1;
    }

    private static int parseMonth(String d1) throws NumberFormatException
    {
        int month1 = Integer.parseInt(d1.substring(3, 5));
        return month1;
    }

    private static int parseYear(String date) throws NumberFormatException
    {
        int year = Integer.parseInt(date.substring(6));
        return year;
    }

    public static ArrayList<User> getAllUsers()
    {
        return userQuery("SELECT * FROM USERS;");
    }

    public static int createReservation(Reservation res)
    {
        String query = " '" + res.getName() + "',"
                + " '" + res.getEmail() + "',"
                + " '" + res.getPhoneNum() + "' ,"
                + " '" + res.getDate() + "',"
                + " '" + res.getTime() + "',"
                + " '" + res.getPeople() + "' ,"
                + " " + (res.getAdditionalNotes() == null ? "null" : "'" + res.getAdditionalNotes() + "'") + " ,"
                + " '" + (res.isIsBirthday() ? 1 : 0) + "' "; // SQLite does not allow bool values, only 0 or 1

        return insert(query);
    }

    private static int insert(String query)
    {

        String sql = "INSERT INTO RESERVATIONS (NAME,EMAIL,PHONE,DATE,TIME,AMOUNT,NOTES,BDAY) "
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

    public static void createReservationsTable()
    {
        String sql = "CREATE TABLE IF NOT EXISTS RESERVATIONS "
                + "(ID INTEGER PRIMARY KEY,"
                + "NAME           VARCHAR(25)    NOT NULL, "
                + "EMAIL           VARCHAR(25)    NOT NULL, "
                + "PHONE           VARCHAR(12)    NOT NULL, "
                + "DATE           VARCHAR(9) NOT NULL,"
                + "TIME           VARCHAR(5) NOT NULL,"
                + " AMOUNT            INT     NOT NULL, "
                + " NOTES        TEXT, "
                + " BDAY        BOOLEAN NOT NULL)";
        executeCommand(sql);
    }

    public static void dumpReservationTable()
    {
        for (Reservation r : getAllReservations())
        {
            System.out.println(r.toString());
        }
    }

    public static void dumpUserTable()
    {
        for (User u : getAllUsers())
        {
            System.out.println(u.toString());
        }
    }

    public static ArrayList<Reservation> reservationQuery(String query)
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
                Reservation tmp = parseReservationValues(rs);
                reservations.add(tmp);
            }

            rs.close();
            stmt.close();
            c.close();
        } catch (ClassNotFoundException | SQLException e)
        {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            throw new SqlException();
        }
        return reservations;
    }

    private static Reservation parseReservationValues(ResultSet rs) throws SQLException
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

    public static ArrayList<User> userQuery(String query)
    {
        ArrayList<User> users = new ArrayList<>();
        try
        {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection(CONNECTION);
            c.setAutoCommit(false);

            stmt = c.createStatement();

            ResultSet rs = stmt.executeQuery(query);
            while (rs.next())
            {
                String name = rs.getString("name");
                String password = rs.getString("password");

                User user = new User(name, password);
                users.add(user);
            }

            rs.close();
            stmt.close();
            c.close();
        } catch (ClassNotFoundException | SQLException e)
        {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            throw new SqlException(); //            System.exit(0);
        }
        return users;
    }

    public static void addDummyValues()
    {
        SimpleDateFormat dateF = new SimpleDateFormat("dd-MM-yyyy");
        SimpleDateFormat timeF = new SimpleDateFormat("HH:mm");
        SimpleDateFormat dateF1 = new SimpleDateFormat("26-01-2016");
        String sql = "INSERT INTO RESERVATIONS (NAME,EMAIL,PHONE,DATE,TIME,AMOUNT,NOTES,BDAY) "
                + "VALUES ( 'Paul', 'email@email.com', '888444', '" + dateF.format(new Date(System.currentTimeMillis())) + "','" + timeF.format(new Date(System.currentTimeMillis()))
                + "', 5, null, 0  );";
        executeCommand(sql);
        String sql1 = "INSERT INTO RESERVATIONS (NAME,EMAIL,PHONE,DATE,TIME,AMOUNT,NOTES,BDAY) "
                + "VALUES ( 'Robert', 'eemail@email.com', '888447', '" + dateF1.format(new Date()) + "','" + timeF.format(new Date(System.currentTimeMillis()))
                + "', 5, null, 0  );";
        executeCommand(sql1);
    }

    public static void dropReservationTable()
    {
        dropTable("reservations");
    }

    public static void dropUserTable()
    {
        dropTable("users");
    }

    public static void dropTable(String table)
    {
        String sql = "DROP TABLE IF EXISTS " + table;
        executeCommand(sql);
    }

    public static class SqlException extends RuntimeException
    {

    }
}
