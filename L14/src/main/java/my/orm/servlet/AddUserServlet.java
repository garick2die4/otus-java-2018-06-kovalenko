package my.orm.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import my.orm.datasets.PhoneDataSet;
import my.orm.datasets.UserDataSet;
import my.orm.dbservice.DBServiceException;
import my.orm.dbservice.IDBService;

public class AddUserServlet extends HttpServlet
{
	private static final long serialVersionUID = -3249528806162203681L;
	
	private static final String USER_NAME_PARAMETER = "name";
	private static final String AGE_PARAMETER = "age";
	private static final String ADDRESS_PARAMETER = "address";
	private static final String PHONES_COUNT_PARAMETER = "phones_count";
	private static final String PHONE_PARAMETER = "phone%d";

	private static final String USER_ID_VARIABLE = "id";
	
	private static final String USER_ADDED_PAGE_TEMPLATE = "user_added.html";
	
	private static TemplateProcessor templateProcessor = new TemplateProcessor();
	
	private IDBService dbService;
	
    public void init()
    {
        //TODO: Create one context for the application. Inject beans.
        ApplicationContext context = new ClassPathXmlApplicationContext("SpringBeans.xml");
        dbService = (IDBService) context.getBean("dbService");
    }
 
    public void doPost(HttpServletRequest request, HttpServletResponse response)
        	throws ServletException, IOException
    {
    	String name = request.getParameter(USER_NAME_PARAMETER);
    	String strAge = request.getParameter(AGE_PARAMETER);
    	short age = 0;
    	try
    	{
    		age = Short.parseShort(strAge);
    	}
    	catch(NumberFormatException e)
    	{
    		response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Bad user age");
    		return;
    	}
    	
    	String address = request.getParameter(ADDRESS_PARAMETER);

    	String strPhonesCount = request.getParameter(PHONES_COUNT_PARAMETER);
    	short phonesCount = 0;
    	try
    	{
    		phonesCount = Short.parseShort(strPhonesCount);
    	}
    	catch(NumberFormatException e)
    	{
    		response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Bad phones count");
    		return;
    	}

    	List<PhoneDataSet> phones = new ArrayList<>();
    	for (short i = 0; i < phonesCount; i++)
    	{
    		String phone = request.getParameter(String.format(PHONE_PARAMETER, i + 1));
    		if (phone != null)
    			phones.add(new PhoneDataSet(phone));
    	}
    	
    	UserDataSet user = new UserDataSet(name, age, address, phones);
    	try
    	{
			dbService.save(user);
		}
    	catch (DBServiceException e)
    	{
    		response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
    		return;
		}

    	Map<String, Object> pageVariables = new HashMap<>();
    	pageVariables.put(USER_ID_VARIABLE, user.getId());
    	
        response.setContentType("text/html;charset=utf-8");

        String page = templateProcessor.getPage(USER_ADDED_PAGE_TEMPLATE, pageVariables);
        
        response.getWriter().println(page);
        response.setStatus(HttpServletResponse.SC_OK);
    }
}
