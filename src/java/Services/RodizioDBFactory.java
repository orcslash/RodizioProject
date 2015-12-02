package Services;

import static Services.Constants.MYSQLDATABASE;
import static Services.Constants.MYSQLHOST;
import static Services.Constants.MYSQLPASSWORD;
import static Services.Constants.MYSQLPORT;
import static Services.Constants.MYSQLUSER;

public class RodizioDBFactory
{

    public static RodizioDB getRodizioDB(String DBType, String connectionInfo)
    {

        if (DBType.equalsIgnoreCase("SQLITE"))
        {
            return new RodizioDB("jdbc:sqlite:" + connectionInfo, "org.sqlite.JDBC");
        }
        return null;
    }

    public static RodizioDB getRodizioDB(String DBType)
    {
        if (DBType.equalsIgnoreCase("mysql"))
        {
            return new MySQLDB(MYSQLHOST + ":" + MYSQLPORT + "/" + MYSQLDATABASE, MYSQLUSER, MYSQLPASSWORD);
        }
        return null;
    }
}
