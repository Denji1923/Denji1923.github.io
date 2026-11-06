package com.campus.lostfound.web;

import com.campus.lostfound.model.User;
import com.campus.lostfound.service.AuthService;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/logout")
public class LogoutServlet extends BaseServlet {

    @Override
    protected void get(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        redirect(req, resp, "/");
    }

    @Override
    protected void post(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession session = req.getSession(false);
        if (session != null) {
            new AuthService().logout((User) session.getAttribute(SecurityFilter.USER));
            session.invalidate();
        }
        redirect(req, resp, "/");
    }
}
