package com.campus.lostfound.web;

import com.campus.lostfound.exception.LostFoundException;
import com.campus.lostfound.model.Admin;
import com.campus.lostfound.model.Exportable;
import com.campus.lostfound.model.Student;
import com.campus.lostfound.service.ReportService;
import java.io.IOException;
import java.io.Writer;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * ABSTRACT class (Week 10): every page servlet extends this. A page overrides get() and/or post();
 * errors from the services (LostFoundException) are turned into a friendly error page here, once.
 * This plays the role the abstract Shell class had in the desktop version.
 */
public abstract class BaseServlet extends HttpServlet {
    private static final String VIEWS = "/WEB-INF/views/";

    /** Override to answer a normal page request. */
    protected void get(HttpServletRequest req, HttpServletResponse resp)
            throws LostFoundException, ServletException, IOException {
        resp.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
    }

    /** Override to answer a submitted form. */
    protected void post(HttpServletRequest req, HttpServletResponse resp)
            throws LostFoundException, ServletException, IOException {
        resp.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
    }

    @Override
    protected final void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            get(req, resp);
        } catch (LostFoundException e) {
            showError(req, resp, e);
        }
    }

    @Override
    protected final void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            post(req, resp);
        } catch (LostFoundException e) {
            showError(req, resp, e);
        }
    }

    private void showError(HttpServletRequest req, HttpServletResponse resp, LostFoundException e)
            throws ServletException, IOException {
        req.setAttribute("errorMessage", e.getMessage());
        resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        render(req, resp, "error");
    }

    // ---------------- helpers shared by all pages ----------------

    /** Shows /WEB-INF/views/{view}.jsp */
    protected void render(HttpServletRequest req, HttpServletResponse resp, String view)
            throws ServletException, IOException {
        req.getRequestDispatcher(VIEWS + view + ".jsp").forward(req, resp);
    }

    /** Redirect after a form was submitted (so pressing Refresh does not submit it again). */
    protected void redirect(HttpServletRequest req, HttpServletResponse resp, String path) throws IOException {
        resp.sendRedirect(req.getContextPath() + path);
    }

    protected void flashSuccess(HttpServletRequest req, String message) {
        req.getSession().setAttribute("flashSuccess", message);
    }

    protected void flashError(HttpServletRequest req, String message) {
        req.getSession().setAttribute("flashError", message);
    }

    /** The logged-in student (SecurityFilter already guarantees one exists on /student/* pages). */
    protected Student currentStudent(HttpServletRequest req) {
        return (Student) req.getSession().getAttribute(SecurityFilter.USER);
    }

    protected Admin currentAdmin(HttpServletRequest req) {
        return (Admin) req.getSession().getAttribute(SecurityFilter.USER);
    }

    protected static String text(HttpServletRequest req, String name) {
        String value = req.getParameter(name);
        return value == null ? "" : value;
    }

    protected static String urlEncode(String value) {
        return URLEncoder.encode(value == null ? "" : value, StandardCharsets.UTF_8);
    }

    /** Sends rows to the browser as a downloadable CSV file (Excel opens it). */
    protected void sendCsv(HttpServletResponse resp, String fileName, List<? extends Exportable> rows)
            throws LostFoundException, IOException {
        if (rows.isEmpty()) throw new LostFoundException("There is nothing to export.");
        resp.setContentType("text/csv");
        resp.setCharacterEncoding("UTF-8");
        resp.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
        Writer out = resp.getWriter();
        out.write('\uFEFF');                                  // byte-order mark so Excel reads UTF-8 correctly
        new ReportService().exportCsv(out, rows);
        out.flush();
    }
}
