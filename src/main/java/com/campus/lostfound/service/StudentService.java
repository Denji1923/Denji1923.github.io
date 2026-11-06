package com.campus.lostfound.service;

import com.campus.lostfound.dao.StudentDAO;
import com.campus.lostfound.exception.LostFoundException;
import com.campus.lostfound.model.Claim;
import com.campus.lostfound.model.Item;
import com.campus.lostfound.model.Student;
import com.campus.lostfound.model.StudentSummary;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StudentService {
    private final StudentDAO dao = new StudentDAO();

    public List<Student> getAll() throws LostFoundException { return dao.findAll(); }

    /** HashMap: student id -> number of reports / claims (Week 14). */
    public List<StudentSummary> getSummaries() throws LostFoundException {
        Map<Integer, Integer> reportCount = new HashMap<>();
        for (Item item : new ItemService().getAll(null)) reportCount.merge(item.getReporterId(), 1, Integer::sum);
        Map<Integer, Integer> claimCount = new HashMap<>();
        for (Claim claim : new ClaimService().getAll()) claimCount.merge(claim.getStudentId(), 1, Integer::sum);

        List<StudentSummary> rows = new ArrayList<>();
        for (Student s : getAll()) {
            rows.add(new StudentSummary(s, reportCount.getOrDefault(s.getId(), 0), claimCount.getOrDefault(s.getId(), 0)));
        }
        return rows;
    }
}
