package my.orm.app;


import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;

import org.eclipse.jetty.websocket.api.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import my.orm.messageSystem.Address;
import my.orm.messageSystem.Addressee;
import my.orm.messageSystem.Message;
import my.orm.messages.MsgAddUser;
import my.orm.messages.MsgGetTotalUsersCount;
import my.orm.messages.MsgGetUserInfo;

/**
 * Created by tully.
 */
@Service
public class FrontendServiceImpl implements Addressee, FrontendService
{
	private final static Logger LOGGER = LoggerFactory.getLogger(FrontendServiceImpl.class);
	
    private final Address address;

    private final Map<Integer, Session> sessions = new HashMap<>();
    
    private final MessageSystemContext context;
    private final TemplateProcessor templateProcessor;
    
    @Autowired
    public FrontendServiceImpl(MessageSystemContext context, TemplateProcessor templateProcessor)
    {
        this.address = new Address("FrontendService");
        this.context = context;
        this.templateProcessor = templateProcessor;

        context.setFrontAddress(this);
    }

    @Override
    public Address getAddress()
    {
        return address;
    }

    @Override
    public void handleRequest(int sessionId, String data)
    {
    	LOGGER.info("handleRequest data: {}", data);
    	
    	InputStream stream = new ByteArrayInputStream(data.getBytes(StandardCharsets.UTF_8));
    	JsonReader jsonReader = Json.createReader(stream);
    	JsonObject jsonObject = jsonReader.readObject();
    	String command = jsonObject.getString("command");
    	Message message = null;
    	if (command.equals("getTotalUsersCount"))
    	{
    		message = new MsgGetTotalUsersCount(
				getAddress(),
				context.getDbAddress(),
				context.getMessageSystem(),
				sessionId);
    	}
    	else if (command.equals("showUser"))
    	{
    		String idStr = jsonObject.getString("id");
    		
    		int id = Integer.parseInt(idStr);
    		
    		message = new MsgGetUserInfo(
				getAddress(),
				context.getDbAddress(),
				context.getMessageSystem(),
				sessionId,
				id);
    	}
    	else if (command.equals("addUser"))
    	{
    		String name = jsonObject.getString("name");
    		String ageStr = jsonObject.getString("age");
    		short age = Short.parseShort(ageStr);
    		String address = jsonObject.getString("address");
    		JsonArray jsonPhones = jsonObject.getJsonArray("phones");
    		List<String> phones = jsonPhones.stream().map(p -> p.toString()).collect(Collectors.toList()); 

    		message = new MsgAddUser(
				getAddress(),
				context.getDbAddress(),
				context.getMessageSystem(),
				sessionId,
				name, age, address, phones);
    	}
    	else
    	{
    		sendError(sessionId,"Unknown command " + command);
    		return;
    	}
    	context.getMessageSystem().sendMessage(message);
    }

    @Override
    public int addSession(Session session)
    {
    	int id = (int) (Math.random() * 1000);
    	sessions.put(id, session);
    	return id;
    }

    @Override
    public void removeSession(int sessionId)
    {
    	if (!sessions.containsKey(sessionId))
    		return;
    	sessions.remove(sessionId);
    }

    @Override
    public void sendTotalUsersCount(int sessionId, int count)
    {
    	LOGGER.info("sendTotalUsersCount id: {}, count: {} ", sessionId, count);
    	
        Map<String, Object> pageVariables = new HashMap<>();
        pageVariables.put("count", count);
        
        sendData(sessionId, "users-count.json", pageVariables);
    }
    
    @Override
    public void sendUserInfo(int sessionId,
		long userId,
		String name,
		short age,
		String address,
		List<String> phones)
    {
    	LOGGER.info("sendUserInfo id: {}, userId: {} name: {}", sessionId, userId, name);
    
        Map<String, Object> pageVariables = new HashMap<>();
        pageVariables.put("id", userId);
        pageVariables.put("name",name);
        pageVariables.put("age", age);
        pageVariables.put("address", address);
        String phonesStr = phones.stream().collect(Collectors.joining(","));
        pageVariables.put("phones", phonesStr);
        
        sendData(sessionId, "user-info.json", pageVariables);
    }
    
    @Override
    public void sendAddUserInfo(int sessionId, long userId)
    {
    	LOGGER.info("sendAddUserInfo id: {}, userId: {} ", sessionId, userId);
    	
        Map<String, Object> pageVariables = new HashMap<>();
        pageVariables.put("id", userId);
        
        sendData(sessionId, "user-added.json", pageVariables);
    }
    
    @Override
    public void sendError(int sessionId, String text)
    {
        Map<String, Object> pageVariables = new HashMap<>();
        pageVariables.put("text", text);
        
        sendData(sessionId, "error.json", pageVariables);
    }
    
    private void sendData(int sessionId, String templateName, Map<String, Object> pageVariables)
    {
        String data;
		try
		{
			data = templateProcessor.getPage(templateName, pageVariables);
		}
		catch (IOException e)
		{
			e.printStackTrace();
			return;
		}

    	Integer id = new Integer(sessionId);
    	Session session = sessions.get(id);
    	
    	if (session == null)
    	{
    		LOGGER.info("Sessiond {} not found", sessionId);
    		return;
    	}
    	
    	try
    	{
			session.getRemote().sendString(data);
		}
    	catch (IOException e)
    	{
			e.printStackTrace();
			return;
		}
    	
    	LOGGER.info("Data was sent");
    }
}
