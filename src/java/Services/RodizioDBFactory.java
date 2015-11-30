package Services;

public class RodizioDBFactory
{

    public static RodizioDB getRodizioDB(String DBType, String fileName)
    {

        if (DBType.equalsIgnoreCase("SQLITE"))
        {
            return new RodizioDB("jdbc:sqlite:" + fileName, "org.sqlite.JDBC");
        }

        return null;
    }
}
