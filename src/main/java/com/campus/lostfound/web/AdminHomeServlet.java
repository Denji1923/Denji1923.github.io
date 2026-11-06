package com.campus.lostfound.web;

import com.campus.lostfound.exception.LostFoundException;
import com.campus.lostfound.service.ReportService;
import com.campus.lostfound.service.TransactionService;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/admin/home")
public class AdminHomeServlet extends BaseServlet {
    private final ReportService reports = new ReportService();
    private final TransactionService transactions = new TransactionService();

    @Override
    protected void get(HttpServletRequest req, HttpServletResponse resp)
            throws LostFoundException, ServletException, IOException {
        req.setAttribute("stats", reports.getAdminStats());          // LinkedHashMap keeps the tile order
        req.setAttribute("latest", transactions.latest(8));
        render(req, resp, "admin-home");
    }
}
