package l16.dbserver.app;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import l16.common.Address;
import l16.common.Addressee;
import l16.common.ClientSocketMsgWorker;
import l16.common.Message;
import l16.common.SocketMsgWorker;
import l16.common.messages.MsgAddUser;
import l16.common.messages.MsgAddUserAnswer;
import l16.common.messages.MsgError;
import l16.common.messages.MsgGetTotalUsersCount;
import l16.common.messages.MsgGetTotalUsersCountAnswer;
import l16.common.messages.MsgGetUserInfo;
import l16.common.messages.MsgGetUserInfoAnswer;
import l16.common.messages.MsgPing;
import l16.dbserver.datasets.PhoneDataSet;
import l16.dbserver.datasets.UserDataSet;
import l16.dbserver.dbservice.DBServiceException;
import l16.dbserver.dbservice.IDBService;

/**
 * Параметры запуска:
 *  - имя сервиса БД
 *
 */
public class DBServerApp
{
    private static final Logger LOGGER = LoggerFactory.getLogger(DBServerApp.class);

    private static final String HOST = "localhost";
    private static final int PORT = 5050;
    private static final int MAX_MESSAGES_COUNT = 10;
    
    private Address dbServiceAddr;
    private IDBService dbService; 
    
    public static void main(String[] args) throws Exception
    {
        new DBServerApp(args[0]).start();
    }

    DBServerApp(String dbServiceName)
    {
    	dbServiceAddr = new Address(dbServiceName);
    	
        dbService = new HibernateDBService("dbexample", "2die4", "12345");
    	((Addressee)dbService).setAddress(dbServiceAddr);
    }
    
    private void start() throws Exception
    {
        SocketMsgWorker client = new ClientSocketMsgWorker(HOST, PORT);
        client.init();

        // посылаем серверу сообщений пинг о себе
        client.send(new MsgPing(dbServiceAddr, new Address("MessageServer"), "ping"));

        int count = 0;
        while (count < MAX_MESSAGES_COUNT)
        {
            final Message msg = client.take();
            
            LOGGER.info("Message received: {}", msg.toString());
            
            Message result = null;
            if (msg instanceof MsgAddUser)
            {
            	result = processAddUser(dbService, (MsgAddUser) msg);	
            }
            else if (msg instanceof MsgGetTotalUsersCount)
            {
            	result = processTotalUsersCount(dbService, (MsgGetTotalUsersCount) msg);
            }
            else if (msg instanceof MsgGetUserInfo)
            {
            	result = processGetUserInfo(dbService, (MsgGetUserInfo) msg);
            }
            
            if (result != null)
            {
            	client.send(result);
            	
            	LOGGER.info("Message send: {}", result.toString());
            }
            
            count++;
        }
        
        client.close();
    }
    
	private Message processAddUser(IDBService dbService, MsgAddUser msg)
	{
    	List<PhoneDataSet> phoneds = new ArrayList<>();
    	for (String phone : msg.getPhones())
    	{
    		if (phone != null)
    			phoneds.add(new PhoneDataSet(phone));
    	}
    	
    	UserDataSet userInfo = new UserDataSet(msg.getName(), msg.getAge(), msg.getAddress(), phoneds);
		
    	try
    	{
			dbService.save(userInfo);
		}
    	catch (DBServiceException e)
    	{
    		LOGGER.info("Error sessionId: {}, text: {}", msg.getSessionId(), e.getMessage());
    		
    		return new MsgError(msg.getTo(), msg.getFrom(), msg.getSessionId(), e.getMessage());
		}
    	
    	LOGGER.info("MsgAddUser userId: {}, name: {}", userInfo.getId(), userInfo.getName());
    	
    	return new MsgAddUserAnswer(msg.getTo(), msg.getFrom(), msg.getSessionId(), userInfo.getId());
	}

	
    private Message processTotalUsersCount(IDBService dbService, MsgGetTotalUsersCount msg)
    {
    	int count = 0;
    	try
    	{
			count = dbService.usersCount();
		}
    	catch (DBServiceException e)
    	{
    		LOGGER.info("Error sessionId: {}, text: {}", msg.getSessionId(), e.getMessage());
    		
    		return new MsgError(msg.getTo(), msg.getFrom(), msg.getSessionId(), e.getMessage());
		}
    	
    	LOGGER.info("MsgGetTotalUsersCount count: {}", count);
    	
    	return new MsgGetTotalUsersCountAnswer(msg.getTo(), msg.getFrom(), msg.getSessionId(), count);
    }
    
    private Message processGetUserInfo(IDBService dbService, MsgGetUserInfo msg)
    {
    	UserDataSet userInfo;
    	try
    	{
    		userInfo = dbService.load(msg.getUserId());
		}
    	catch (DBServiceException e)
    	{
    		LOGGER.info("Error sessionId: {}, text: {}", msg.getSessionId(), e.getMessage());
    		
    		return new MsgError(msg.getTo(), msg.getFrom(), msg.getSessionId(), e.getMessage());
		}
    	
    	LOGGER.info("MsgGetUserInfo userId: {}, name: {}", msg.getUserId(), userInfo.getName());
    	
    	List<String> phones = userInfo.getPhones().stream().map(p -> p.getNumber()).collect(Collectors.toList());
    	return new MsgGetUserInfoAnswer(msg.getTo(), msg.getFrom(),
			msg.getSessionId(),
			userInfo.getId(),
			userInfo.getName(),
			userInfo.getAge(),
			userInfo.getAddress().getStreet(),
			phones);
    }
}
