package com.campus.lostfound.web;

import com.campus.lostfound.exception.LostFoundException;
import com.campus.lostfound.model.ItemType;
import com.campus.lostfound.service.ItemService;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/student/items")
public class ViewItemsServlet extends BaseServlet {
    private final ItemService items = new ItemService();

    @Override
    protected void get(HttpServletRequest req, HttpServletResponse resp)
            throws LostFoundException, ServletException, IOException {
        String keyword = text(req, "q");
        ItemType type = null;
        switch (text(req, "type")) {
            case "LOST":  type = ItemType.LOST;  break;
            case "FOUND": type = ItemType.FOUND; break;
            default:      break;                       // "" = All
        }
        req.setAttribute("keyword", keyword);
        req.setAttribute("typeFilter", type == null ? "" : type.name());
        req.setAttribute("items", items.search(keyword, type));          // overloaded search(String, ItemType)
        req.setAttribute("myItems", items.getByReporter(currentStudent(req)));
        render(req, resp, "view-items");
    }
}
