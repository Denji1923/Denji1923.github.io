package com.campus.lostfound.web;

import com.campus.lostfound.exception.LostFoundException;
import com.campus.lostfound.model.Item;
import com.campus.lostfound.model.Student;
import com.campus.lostfound.service.ClaimService;
import com.campus.lostfound.service.ItemService;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/student/home")
public class StudentHomeServlet extends BaseServlet {
    private final ItemService items = new ItemService();
    private final ClaimService claims = new ClaimService();

    @Override
    protected void get(HttpServletRequest req, HttpServletResponse resp)
            throws LostFoundException, ServletException, IOException {
        Student student = currentStudent(req);

        List<Item> mine = items.getByReporter(student);
        int pending = 0;
        for (Item item : mine) {
            if (item.isPending()) pending++;
        }
        List<Item> recent = items.search("");

        req.setAttribute("myReports", mine.size());
        req.setAttribute("pendingReports", pending);
        req.setAttribute("myClaims", claims.getByStudent(student).size());
        req.setAttribute("recentItems", recent.subList(0, Math.min(5, recent.size())));
        render(req, resp, "student-home");
    }
}
