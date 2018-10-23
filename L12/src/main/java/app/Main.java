package app;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import my.orm.dbservice.IDBService;
import my.orm.hiber.HibernateDBService;
import my.orm.servlet.AddUserServlet;
import my.orm.servlet.GetTotalUsersServlet;
import my.orm.servlet.GetUserServlet;

public class Main
{
    private final static int PORT = 8282;
    private final static String PUBLIC_HTML = "public_html";
	
    public static void main(String... args) throws Exception
    {
        ResourceHandler resourceHandler = new ResourceHandler();
        resourceHandler.setResourceBase(PUBLIC_HTML);

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);

        try (IDBService dbService = createHibernateDbService("dbexample", "2die4", "12345"))
        {
        	context.addServlet(new ServletHolder(new AddUserServlet(dbService)), "/add");
        	context.addServlet(new ServletHolder(new GetUserServlet(dbService)), "/user");
        	context.addServlet(new ServletHolder(new GetTotalUsersServlet(dbService)), "/count");

        	Server server = new Server(PORT);
        	server.setHandler(new HandlerList(resourceHandler, context));

        	server.start();
        	server.join();
        }
    }

    private static IDBService createHibernateDbService(String dbName, String userName, String password)
    {
    	return new HibernateDBService(dbName, userName, password);
    }

}