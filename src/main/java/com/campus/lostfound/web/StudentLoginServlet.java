package com.campus.lostfound.web;

import com.campus.lostfound.exception.AuthenticationException;
import com.campus.lostfound.exception.DatabaseException;
import com.campus.lostfound.model.Student;
import com.campus.lostfound.service.AuthService;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** Student login: student number + full name, NO password. */
@WebServlet("/student/login")
public class StudentLoginServlet extends BaseServlet {
    private final AuthService auth = new AuthService();

    @Override
    protected void get(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        render(req, resp, "student-login");
    }

    @Override
    protected void post(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            Student student = auth.loginStudent(text(req, "studentNumber"), text(req, "fullName"));
            req.changeSessionId();                                   // new session id after login (security)
            req.getSession().setAttribute(SecurityFilter.USER, student);
            redirect(req, resp, "/student/home");
        } catch (AuthenticationException | DatabaseException | IllegalArgumentException e) {
            req.setAttribute("error", e.getMessage());
            render(req, resp, "student-login");
        }
    }
}
