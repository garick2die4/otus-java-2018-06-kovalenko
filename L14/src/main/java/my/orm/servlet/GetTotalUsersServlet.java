package my.orm.servlet;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import my.orm.dbservice.DBServiceException;
import my.orm.dbservice.IDBService;

@Configurable
public class GetTotalUsersServlet extends HttpServlet
{
	private static final long serialVersionUID = -7961585307980744080L;

	private static final String COUNT_VARIABLE = "count";
	
	private static final String USERS_COUNT_PAGE_TEMPLATE = "users_count.html";
	
	private static TemplateProcessor templateProcessor = new TemplateProcessor();
	
	@Autowired
	private IDBService dbService;
	
    public void init(ServletConfig config) throws ServletException
    {
        super.init(config);
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
    }
	
    public void doGet(HttpServletRequest request, HttpServletResponse response)
        	throws ServletException, IOException
    {
    	int count = 0;
    	try
    	{
			count = dbService.usersCount();
		}
    	catch (DBServiceException e)
    	{
    		response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
    		return;
		}
    	
    	Map<String, Object> pageVariables = new HashMap<>();
    	pageVariables.put(COUNT_VARIABLE, Integer.toString(count));
    	
        response.setContentType("text/html;charset=utf-8");
        
        String page = templateProcessor.getPage(USERS_COUNT_PAGE_TEMPLATE, pageVariables);
        
        response.getWriter().println(page);
        response.setStatus(HttpServletResponse.SC_OK);
    }
}
