package com.campus.lostfound.web;

import com.campus.lostfound.model.Admin;
import com.campus.lostfound.model.Student;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Runs before EVERY request. It
 *  1. makes the page text UTF-8 and adds basic security headers,
 *  2. gives each visitor a secret CSRF token and checks it on every form (POST),
 *  3. keeps visitors out of /student/* and /admin/* unless they are logged in with the right role.
 */
@WebFilter("/*")
public class SecurityFilter implements Filter {
    public static final String USER = "currentUser";
    public static final String CSRF = "csrfToken";
    private final SecureRandom random = new SecureRandom();

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        String path = req.getServletPath();

        if (path.startsWith("/assets/")) {              // css / images need no checks
            chain.doFilter(request, response);
            return;
        }

        req.setCharacterEncoding("UTF-8");
        resp.setHeader("X-Content-Type-Options", "nosniff");
        resp.setHeader("X-Frame-Options", "DENY");
        resp.setHeader("Referrer-Policy", "same-origin");
        resp.setHeader("Cache-Control", "no-store");    // the Back button must not show private pages after logout

        HttpSession session = req.getSession(true);
        String token = (String) session.getAttribute(CSRF);
        if (token == null) {
            byte[] bytes = new byte[24];
            random.nextBytes(bytes);
            token = Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
            session.setAttribute(CSRF, token);
        }
        req.setAttribute(CSRF, token);

        if ("POST".equalsIgnoreCase(req.getMethod())) {
            String sent = req.getParameter("_csrf");
            boolean ok = sent != null && MessageDigest.isEqual(
                    sent.getBytes(StandardCharsets.UTF_8), token.getBytes(StandardCharsets.UTF_8));
            if (!ok) {
                resp.sendError(HttpServletResponse.SC_FORBIDDEN,
                        "Your page expired or the security token was missing. Go back, reload the page and try again.");
                return;
            }
        }

        Object user = session.getAttribute(USER);
        if (path.startsWith("/student/") && !path.equals("/student/login") && !(user instanceof Student)) {
            resp.sendRedirect(req.getContextPath() + "/student/login");
            return;
        }
        if (path.startsWith("/admin/") && !path.equals("/admin/login") && !(user instanceof Admin)) {
            resp.sendRedirect(req.getContextPath() + "/admin/login");
            return;
        }
        chain.doFilter(request, response);
    }
}
