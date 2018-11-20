package my.orm.servlet;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import my.orm.datasets.UserDataSet;
import my.orm.dbservice.DBServiceException;
import my.orm.dbservice.IDBService;

@Configurable
public class GetUserServlet extends HttpServlet
{
	private static final long serialVersionUID = 6942649348584272954L;

	private static final String USER_ID_PARAMETER_NAME = "id";
	
	private static final String USER_PAGE_TEMPLATE = "user_info.html";

	private static final String USER_NAME_VARIABLE = "name";
	private static final String AGE_VARIABLE = "age";
	private static final String ADDRESS_VARIABLE = "address";
	private static final String PHONES_VARIABLE = "phones";
	
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
    	String strUserID = request.getParameter(USER_ID_PARAMETER_NAME);
    	int userID = 0;
    	try
    	{
    		userID = Integer.parseInt(strUserID);
    	}
    	catch(NumberFormatException e)
    	{
    		response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Bad user id");
    		return;
    	}
    	
    	UserDataSet userData;
    	try
    	{
			userData = dbService.load(userID);
		}
    	catch (DBServiceException e)
    	{
    		response.sendError(HttpServletResponse.SC_NOT_FOUND, "User not found");
    		return;
		}
    	
    	Map<String, Object> pageVariables = new HashMap<>();
    	pageVariables.put(USER_NAME_VARIABLE, userData.getName());
    	pageVariables.put(AGE_VARIABLE, userData.getAge());
    	pageVariables.put(ADDRESS_VARIABLE, userData.getAddress().getStreet());
    	List<String> phones = userData.getPhones().stream().map(d -> d.getNumber()).collect(Collectors.toList());
    	pageVariables.put(PHONES_VARIABLE, phones);
    	
        response.setContentType("text/html;charset=utf-8");

        String page = templateProcessor.getPage(USER_PAGE_TEMPLATE, pageVariables);
        
        response.getWriter().println(page);
        response.setStatus(HttpServletResponse.SC_OK);
    }

}
