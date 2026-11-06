package com.campus.lostfound.web;

import com.campus.lostfound.exception.LostFoundException;
import com.campus.lostfound.service.ClaimService;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/admin/claims")
public class ManageClaimsServlet extends BaseServlet {
    private final ClaimService claims = new ClaimService();

    @Override
    protected void get(HttpServletRequest req, HttpServletResponse resp)
            throws LostFoundException, ServletException, IOException {
        req.setAttribute("rows", claims.getAll());
        render(req, resp, "manage-claims");
    }

    @Override
    protected void post(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            int id = Integer.parseInt(text(req, "id"));
            switch (text(req, "action")) {
                case "approve": claims.approve(currentAdmin(req), id); flashSuccess(req, "Claim approved. The item is now marked as claimed."); break;
                case "reject":  claims.reject(currentAdmin(req), id);  flashSuccess(req, "Claim rejected."); break;
                default:        flashError(req, "Unknown action.");
            }
        } catch (NumberFormatException e) {
            flashError(req, "Invalid claim.");
        } catch (LostFoundException e) {
            flashError(req, e.getMessage());
        }
        redirect(req, resp, "/admin/claims");
    }
}
