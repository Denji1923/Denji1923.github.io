package com.campus.lostfound.service;

import com.campus.lostfound.dao.AdminDAO;
import com.campus.lostfound.dao.StudentDAO;
import com.campus.lostfound.exception.AuthenticationException;
import com.campus.lostfound.exception.DatabaseException;
import com.campus.lostfound.model.Admin;
import com.campus.lostfound.model.Student;
import com.campus.lostfound.model.User;
import com.campus.lostfound.util.PasswordUtil;
import com.campus.lostfound.util.Validator;
import java.util.Optional;

/**
 * Login logic. On the web, "who is logged in" is remembered per visitor in the HttpSession
 * (see the web package), so this class only checks credentials and records the transaction.
 */
public class AuthService {
    private final StudentDAO studentDAO = new StudentDAO();
    private final AdminDAO adminDAO = new AdminDAO();
    private final TransactionService transactions = new TransactionService();

    /**
     * Creates the first admin the first time the system runs: username "admin", password from the
     * ADMIN_PASSWORD environment variable (or -Dadmin.password), or "admin123" if neither is set.
     * On a public website ALWAYS set ADMIN_PASSWORD before the first start.
     */
    public void ensureDefaultAdmin() throws DatabaseException {
        if (adminDAO.count() == 0) {
            String password = System.getProperty("admin.password");
            if (password == null || password.isBlank()) password = System.getenv("ADMIN_PASSWORD");
            if (password == null || password.isBlank()) password = "admin123";
            String salt = PasswordUtil.generateSalt();
            adminDAO.save(new Admin("admin", "System Administrator", PasswordUtil.hash(password, salt), salt));
        }
    }

    /** Student login: student number + full name only. New students are registered automatically. */
    public Student loginStudent(String number, String fullName) throws AuthenticationException, DatabaseException {
        String cleanNumber = Validator.requireStudentNumber(number);     // may throw ValidationException (unchecked)
        String cleanName = Validator.requireFullName(fullName);

        Optional<Student> existing = studentDAO.findByNumber(cleanNumber);
        Student student;
        if (existing.isPresent()) {
            student = existing.get();
            if (!student.getFullName().equalsIgnoreCase(cleanName)) {
                throw new AuthenticationException("The name does not match this student number.");
            }
        } else {
            student = studentDAO.save(new Student(cleanNumber, cleanName));
        }
        transactions.record(student, "Login", "Student " + cleanNumber + " logged in");
        return student;
    }

    public Admin loginAdmin(String username, String password) throws AuthenticationException, DatabaseException {
        String cleanUser = Validator.requireText(username, "Username", 50);
        Optional<Admin> found = adminDAO.findByUsername(cleanUser);
        if (!found.isPresent() || !found.get().verifyPassword(password)) {
            throw new AuthenticationException("Invalid username or password.");
        }
        Admin admin = found.get();
        transactions.record(admin, "Login", "Admin logged in");
        return admin;
    }

    public void logout(User user) {
        if (user != null) transactions.record(user, "Logout", "Logged out");
    }
}
