/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Services;

import model.User;

/**
 *
 * @author L
 */
public class Authentification
{

    public static User authenticate(User user)
    {
        return Database.checkUser(user);
    }

}
