package my.orm.messages;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import my.orm.app.HibernateDBService;
import my.orm.dbservice.DBServiceException;
import my.orm.messageSystem.Address;
import my.orm.messageSystem.MessageSystem;

public class MsgGetTotalUsersCount extends MsgToDB
{
	private final static Logger LOGGER = LoggerFactory.getLogger(MsgGetTotalUsersCount.class);
	
    private final int sessionId;
    private final MessageSystem messageSystem;
    
    public MsgGetTotalUsersCount(Address from, Address to, MessageSystem messageSystem, int sessionId)
    {
        super(from, to);
        
        this.messageSystem = messageSystem;
        this.sessionId = sessionId;
    }

    @Override
    public void exec(HibernateDBService dbService)
    {
    	int count = 0;
    	try
    	{
			count = dbService.usersCount();
		}
    	catch (DBServiceException e)
    	{
    		LOGGER.info("Error sessionId: {}, text: {}", sessionId, e.getMessage());
    		
    		messageSystem.sendMessage(new MsgError(getTo(), getFrom(), sessionId, e.getMessage()));
    		return;
		}
    	
    	LOGGER.info("MsgGetTotalUsersCount count: {}", count);
    	
    	messageSystem.sendMessage(new MsgGetTotalUsersCountAnswer(getTo(), getFrom(), sessionId, count));
    }
}
