package com.campus.lostfound.web;

import com.campus.lostfound.exception.LostFoundException;
import com.campus.lostfound.exception.ValidationException;
import com.campus.lostfound.model.ItemType;
import com.campus.lostfound.service.ItemService;
import com.campus.lostfound.util.AttachmentStore;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

@WebServlet("/student/report")
@MultipartConfig(maxFileSize = AttachmentStore.MAX_BYTES, maxRequestSize = AttachmentStore.MAX_BYTES + 512 * 1024)
public class ReportItemServlet extends BaseServlet {
    private final ItemService items = new ItemService();

    @Override
    protected void get(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        showForm(req, resp);
    }

    @Override
    protected void post(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String attachment = null;
        try {
            attachment = saveAttachment(req);
            ItemType type = "FOUND".equals(text(req, "type")) ? ItemType.FOUND : ItemType.LOST;
            LocalDate date = parseDate(text(req, "date"));
            items.reportItem(currentStudent(req), type, text(req, "name"), text(req, "category"),
                    text(req, "description"), text(req, "location"), date, attachment);
            flashSuccess(req, "Item submitted" + (attachment == null ? "" : " with your picture")
                    + "! It will appear in View Items once an admin approves it.");
            redirect(req, resp, "/student/items");
        } catch (LostFoundException | IllegalArgumentException e) {   // ValidationException is an IllegalArgumentException
            AttachmentStore.delete(attachment);                        // report failed -> do not keep an orphan picture
            req.setAttribute("error", e.getMessage());
            showForm(req, resp);
        }
    }

    /** Saves the optional picture; returns its stored name or null. */
    private String saveAttachment(HttpServletRequest req) throws IOException, ServletException {
        Part part;
        try {
            part = req.getPart("attachment");
        } catch (IllegalStateException e) {                           // bigger than the @MultipartConfig limit
            throw new ValidationException("The picture must be 5 MB or smaller.");
        }
        if (part == null || part.getSize() == 0) return null;
        try (InputStream in = part.getInputStream()) {
            return AttachmentStore.save(in, part.getSubmittedFileName());
        }
    }

    private void showForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("categories", ItemService.CATEGORIES);
        req.setAttribute("today", LocalDate.now().toString());
        render(req, resp, "report-item");
    }

    private LocalDate parseDate(String value) {
        try {
            return value.isBlank() ? null : LocalDate.parse(value);   // null -> Validator says "Date is required."
        } catch (DateTimeParseException e) {
            throw new ValidationException("Please enter a valid date.");
        }
    }
}
