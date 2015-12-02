package Services;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MySQLDB extends RodizioDB
{

    private String user;
    private String password;

    /**
     * @param connectionURL host:port/dbName
     */
    public MySQLDB(String connectionURL, String user, String password)
    {
        super(connectionURL, "com.mysql.jdbc.Driver");
        this.user = user;
        this.password = password;

    }

    @Override
    protected void createConnection()
    {
        try
        {
            connection = DriverManager.getConnection("jdbc:mysql://" + connectionURL, user, password);
        } catch (SQLException ex)
        {
            Logger.getLogger(MySQLDB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void setUser(String user)
    {
        this.user = user;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

}
