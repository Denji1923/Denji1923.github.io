package com.campus.lostfound.web;

import com.campus.lostfound.dao.DatabaseInitializer;
import com.campus.lostfound.exception.DatabaseException;
import com.campus.lostfound.service.AuthService;
import com.campus.lostfound.util.AppLogger;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/** Runs once when the website starts: creates the database/tables and the default admin account. */
@WebListener
public class AppStartupListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent event) {
        try {
            DatabaseInitializer.init();
            new AuthService().ensureDefaultAdmin();
            AppLogger.log("Website started - database ready");
            System.out.println("[CampusLostFound] Database ready.");
        } catch (DatabaseException e) {
            // Do not stop the website: the pages show a clear message until MySQL is available.
            AppLogger.log("Website started - DATABASE PROBLEM: " + e.getMessage());
            System.err.println("[CampusLostFound] DATABASE PROBLEM: " + e.getMessage());
            System.err.println("[CampusLostFound] Start MySQL in XAMPP and check src/main/resources/db.properties, then reload the page.");
        }
    }
}
