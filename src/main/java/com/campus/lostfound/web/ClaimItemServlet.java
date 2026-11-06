package com.campus.lostfound.web;

import com.campus.lostfound.exception.LostFoundException;
import com.campus.lostfound.service.ClaimService;
import com.campus.lostfound.service.ItemService;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/student/claim")
public class ClaimItemServlet extends BaseServlet {
    private final ItemService items = new ItemService();
    private final ClaimService claims = new ClaimService();

    @Override
    protected void get(HttpServletRequest req, HttpServletResponse resp)
            throws LostFoundException, ServletException, IOException {
        showForm(req, resp);
    }

    @Override
    protected void post(HttpServletRequest req, HttpServletResponse resp)
            throws LostFoundException, ServletException, IOException {
        try {
            int itemId;
            try {
                itemId = Integer.parseInt(text(req, "item"));
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Please select an item.");
            }
            claims.submitClaim(currentStudent(req), itemId, text(req, "proof"));
            flashSuccess(req, "Claim submitted! Wait for admin approval.");
            redirect(req, resp, "/student/claim");
        } catch (LostFoundException | IllegalArgumentException e) {
            req.setAttribute("error", e.getMessage());
            showForm(req, resp);
        }
    }

    private void showForm(HttpServletRequest req, HttpServletResponse resp)
            throws LostFoundException, ServletException, IOException {
        req.setAttribute("available", items.getAvailableForClaim());
        req.setAttribute("myClaimList", claims.getByStudent(currentStudent(req)));
        render(req, resp, "claim-item");
    }
}
