package Services;

public class RodizioDBFactory
{

    public static RodizioDB getRodizioDB(String DBType)
    {

        if (DBType.equalsIgnoreCase("SQLITE"))
        {
            return new RodizioDB("jdbc:sqlite:Test.db", "org.sqlite.JDBC");
        }

        return null;
    }
}
