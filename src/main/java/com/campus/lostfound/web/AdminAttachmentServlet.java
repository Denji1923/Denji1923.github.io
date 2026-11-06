package com.campus.lostfound.web;

import com.campus.lostfound.exception.LostFoundException;
import com.campus.lostfound.exception.RecordNotFoundException;
import com.campus.lostfound.model.Item;
import com.campus.lostfound.service.ItemService;
import com.campus.lostfound.util.AttachmentStore;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** Sends the full-size picture attached to an item. Only reachable by a logged-in admin (SecurityFilter guards /admin/*). */
@WebServlet("/admin/attachment")
public class AdminAttachmentServlet extends BaseServlet {
    private final ItemService items = new ItemService();

    @Override
    protected void get(HttpServletRequest req, HttpServletResponse resp)
            throws LostFoundException, ServletException, IOException {
        Path file = null;
        String type = null;
        try {
            Item item = items.getById(Integer.parseInt(text(req, "id")));
            file = AttachmentStore.find(item.getAttachment());
            type = AttachmentStore.contentType(item.getAttachment());
        } catch (NumberFormatException | RecordNotFoundException e) {
            // falls through to 404
        }
        if (file == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        resp.setContentType(type);
        resp.setContentLengthLong(Files.size(file));
        resp.setHeader("Content-Disposition", "inline");
        resp.setHeader("Content-Security-Policy", "default-src 'none'; style-src 'unsafe-inline'");
        Files.copy(file, resp.getOutputStream());
    }
}
