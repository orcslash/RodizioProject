package beans;

import Services.Authentification;
import Services.Session;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import model.User;

@Named(value = "login")
@SessionScoped
public class LoginBean implements Serializable
{

    private String userName;
    private String userPass;
    private User currentUser;

    public String validateUser()
    {
        if (userName.length() == 0 || userPass.length() == 0)
        {
            return null;
        }
        if ((currentUser = Authentification.authenticate(new User(userName, userName))) != null)
        {
            Session.getSession().setAttribute("username", currentUser.getName());
            return "confirmed";
        }
        return null;
    }

    public String logout()
    {
        Session.getSession().invalidate();
        resetFields();
        return "login";
    }

    private void resetFields()
    {
        this.userName = null;
        this.currentUser = null;
        this.userPass = null;
    }

    public User getCurrentUser()
    {
        return currentUser;
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

    public LoginBean()
    {
        //      Database.createUserTable();
        //      ArrayList<User> users = new ArrayList<>();
        //      users.add(new User("admin", "admin"));
        //    users.add(new User("admin1", "admin"));
        //    users.add(new User("admin2", "admin"));
        //    users.add(new User("admin3", "admin"));
        //   users.add(new User("qq", "qq"));
        //   for (User u : users)
        //   {
        //       Database.insertUser(u);
        //    }
    }

}
