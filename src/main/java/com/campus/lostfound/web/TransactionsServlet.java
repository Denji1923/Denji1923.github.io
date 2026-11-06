package com.campus.lostfound.web;

import com.campus.lostfound.exception.LostFoundException;
import com.campus.lostfound.model.TransactionRecord;
import com.campus.lostfound.service.TransactionService;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/admin/transactions")
public class TransactionsServlet extends BaseServlet {
    private final TransactionService transactions = new TransactionService();

    @Override
    protected void get(HttpServletRequest req, HttpServletResponse resp)
            throws LostFoundException, ServletException, IOException {
        String keyword = text(req, "q");
        String role = text(req, "role");                          // "" = everyone, "student", "admin"
        if (!role.equals("student") && !role.equals("admin")) role = "";

        List<TransactionRecord> rows = transactions.search(keyword, role.isEmpty() ? null : role);

        if ("csv".equals(text(req, "export"))) {
            try {
                sendCsv(resp, "transactions.csv", rows);          // File handling + generics
                return;
            } catch (LostFoundException e) {
                flashError(req, e.getMessage());
                redirect(req, resp, "/admin/transactions");
                return;
            }
        }
        req.setAttribute("keyword", keyword);
        req.setAttribute("roleFilter", role);
        req.setAttribute("rows", rows);
        render(req, resp, "transactions");
    }
}
