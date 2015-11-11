/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import Services.Authentification;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import model.User;

/**
 *
 * @author L
 */
@Named(value = "login")
@SessionScoped
public class LoginBean implements Serializable
{

    private String userName;
    private String userPass;
    private User currentUser;

    /**
     * Validates user input
     *
     * @return
     */
    public String validateUser()
    {
        if (userName.length() == 0 || userPass.length() == 0)
        {
            return null;
        }
        if ((currentUser = Authentification.authenticate(new User(userName, userName))) != null)
        {
            return "confirmed";
        }
        return null;
    }

    public User getCurrentUser()
    {
        return currentUser;
    }

    public void setCurrentUser(User currentUser)
    {
        this.currentUser = currentUser;
    }

    public String getUserName()
    {
        return userName;
    }

    public void setUserName(String userName)
    {
        this.userName = userName;
    }

    public String getUserPass()
    {
        return userPass;
    }

    public void setUserPass(String userPass)
    {
        this.userPass = userPass;
    }

    /**
     * Creates a new instance of LoginBean
     */
    public LoginBean()
    {

//        Database.createUserTable();
//        ArrayList<User> users = new ArrayList<>();
//        users.add(new User("admin", "admin"));
//        users.add(new User("admin1", "admin"));
//        users.add(new User("admin2", "admin"));
//        users.add(new User("admin3", "admin"));
//        users.add(new User("qq", "qq"));
//
//        for (User u : users)
//        {
//            Database.insertUser(u);
//        }
    }

}
