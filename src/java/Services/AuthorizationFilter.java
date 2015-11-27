package Services;

import beans.LoginBean;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebFilter(filterName = "AuthFilter", urlPatterns =
{
    "*.xhtml"
})
public class AuthorizationFilter implements javax.servlet.Filter
{

    public AuthorizationFilter()
    {
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException
    {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException
    {
        HttpServletRequest reqt = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        HttpSession ses = reqt.getSession(false);
        String url = reqt.getRequestURI();

        /*
        if there's no session and request is for the staffpage redirect to the login page
        else send where the request is asking to go.
        if there's a session and request is for login, redirect to staff page

        also session is not neccesarilly null if no one has logged in, so it's important to check if
        username for the session has been set.
         */
        if (ses == null)
        {
            redirectToLoginFromStaffReq(url, resp, reqt);

            //  lets the request go through to the requested page
            chain.doFilter(request, response);
        }
        else
        {
            if (ses.getAttribute("username") == null)
            {
                redirectToLoginFromStaffReq(url, resp, reqt);
            }
            if (url.contains("login.xhtml") && ses.getAttribute("username") != null)
            {
                resp.sendRedirect(reqt.getServletContext().getContextPath() + "/faces/StaffPage.xhtml");
            }
            else
            {
                chain.doFilter(request, response);
            }
        }
    }

    private void redirectToLoginFromStaffReq(String url, HttpServletResponse resp, HttpServletRequest reqt) throws IOException
    {
        if (url.contains("StaffPage.xhtml"))
        {
            resp.sendRedirect(reqt.getServletContext().getContextPath() + "/faces/login.xhtml");
        }
    }

    @Override
    public void destroy()
    {

    }
}
