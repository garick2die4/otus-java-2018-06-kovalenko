package my.orm.messages;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import my.orm.app.HibernateDBService;
import my.orm.datasets.UserDataSet;
import my.orm.dbservice.DBServiceException;
import my.orm.messageSystem.Address;
import my.orm.messageSystem.MessageSystem;

public class MsgGetUserInfo extends MsgToDB
{
	private final static Logger LOGGER = LoggerFactory.getLogger(MsgGetUserInfo.class);
	
    private final int sessionId;
    private final long userId;
    private final MessageSystem messageSystem;

	public MsgGetUserInfo(Address from, Address to, MessageSystem messageSystem, int sessionId, long userId)
	{
		super(from, to);

        this.messageSystem = messageSystem;
        this.sessionId = sessionId;
        this.userId = userId;
	}

    @Override
    public void exec(HibernateDBService dbService)
    {
    	UserDataSet userData;
    	try
    	{
			userData = dbService.load(userId);
		}
    	catch (DBServiceException e)
    	{
    		LOGGER.info("Error sessionId: {}, text: {}", sessionId, e.getMessage());
    		
    		messageSystem.sendMessage(new MsgError(getTo(), getFrom(), sessionId, e.getMessage())); 
    		return;
		}
    	
    	LOGGER.info("MsgGetUserInfo userId: {}, name: {}", userId, userData.getName());
    	
    	messageSystem.sendMessage(new MsgGetUserInfoAnswer(getTo(), getFrom(), sessionId, userData));
    }
}
