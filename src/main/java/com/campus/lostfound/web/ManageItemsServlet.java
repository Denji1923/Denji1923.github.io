package com.campus.lostfound.web;

import com.campus.lostfound.exception.LostFoundException;
import com.campus.lostfound.model.ApprovalStatus;
import com.campus.lostfound.model.Item;
import com.campus.lostfound.service.ItemService;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** The administrator approves, rejects or deletes reported items here. */
@WebServlet("/admin/items")
public class ManageItemsServlet extends BaseServlet {
    private final ItemService items = new ItemService();

    @Override
    protected void get(HttpServletRequest req, HttpServletResponse resp)
            throws LostFoundException, ServletException, IOException {
        ApprovalStatus status = parseStatus(text(req, "status"));
        List<Item> rows = items.getAll(status);

        if ("csv".equals(text(req, "export"))) {
            try {
                sendCsv(resp, "items.csv", rows);
                return;
            } catch (LostFoundException e) {
                flashError(req, e.getMessage());
                redirect(req, resp, "/admin/items");
                return;
            }
        }
        req.setAttribute("statusFilter", status == null ? "" : status.name());
        req.setAttribute("rows", rows);
        render(req, resp, "manage-items");
    }

    @Override
    protected void post(HttpServletRequest req, HttpServletResponse resp)
            throws LostFoundException, ServletException, IOException {
        ApprovalStatus status = parseStatus(text(req, "status"));
        String back = "/admin/items" + (status == null ? "" : "?status=" + status.name());
        try {
            int id = Integer.parseInt(text(req, "id"));
            switch (text(req, "action")) {
                case "approve": items.approve(currentAdmin(req), id); flashSuccess(req, "Item approved."); break;
                case "reject":  items.reject(currentAdmin(req), id);  flashSuccess(req, "Item rejected.");  break;
                case "delete":  items.delete(currentAdmin(req), id);  flashSuccess(req, "Item deleted.");   break;
                default:        flashError(req, "Unknown action.");
            }
        } catch (NumberFormatException e) {
            flashError(req, "Invalid item.");
        } catch (LostFoundException e) {
            flashError(req, e.getMessage());
        }
        redirect(req, resp, back);
    }

    private ApprovalStatus parseStatus(String value) {
        switch (value) {
            case "PENDING":  return ApprovalStatus.PENDING;
            case "APPROVED": return ApprovalStatus.APPROVED;
            case "REJECTED": return ApprovalStatus.REJECTED;
            default:         return null;               // "" = All
        }
    }
}
