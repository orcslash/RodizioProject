package Services;

import model.User;

public class Authentification
{

    public static User authenticate(User user)
    {
        return Database.checkUser(user);
    }

}
