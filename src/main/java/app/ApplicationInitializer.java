package app;

import app.Command.*;
import app.Singleton.CommandBagSingleton;
import app.Singleton.SQLiteSingleton;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletContextEvent;
import java.sql.SQLException;

public class ApplicationInitializer implements ServletContextListener {
    public static String DBPath = "/WEB-INF/classes/app.db";

    public void contextInitialized(ServletContextEvent event) {
        try {
            SQLiteSingleton.Init(event.getServletContext().getRealPath(DBPath));
        }
        catch (SQLException err) {
            err.printStackTrace();
        }

        CommandBagSingleton.Init(new GetByParent(), new Delete(), new InsertContainer(), new InsertEnd(), new MoveTo(), new Rename());
    }

    public void contextDestroyed(ServletContextEvent event) {

    }
}
