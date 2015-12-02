package Services;

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

    public static RodizioDB getRodizioDB(String DBType, String connectionInfo, String user, String password)
    {
        if (DBType.equalsIgnoreCase("mysql"))
        {
            return new MySQLDB(connectionInfo, user, password);
        }
        return null;
    }
}
