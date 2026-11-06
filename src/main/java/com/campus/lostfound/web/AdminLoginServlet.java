package com.campus.lostfound.web;

import com.campus.lostfound.exception.AuthenticationException;
import com.campus.lostfound.exception.DatabaseException;
import com.campus.lostfound.model.Admin;
import com.campus.lostfound.service.AuthService;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** Admin login: username + PASSWORD. */
@WebServlet("/admin/login")
public class AdminLoginServlet extends BaseServlet {
    private final AuthService auth = new AuthService();

    @Override
    protected void get(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        render(req, resp, "admin-login");
    }

    @Override
    protected void post(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            auth.ensureDefaultAdmin();       // in case MySQL was started after the website
            Admin admin = auth.loginAdmin(text(req, "username"), text(req, "password"));
            req.changeSessionId();
            req.getSession().setAttribute(SecurityFilter.USER, admin);
            redirect(req, resp, "/admin/home");
        } catch (AuthenticationException | DatabaseException | IllegalArgumentException e) {
            req.setAttribute("error", e.getMessage());
            render(req, resp, "admin-login");
        }
    }
}
