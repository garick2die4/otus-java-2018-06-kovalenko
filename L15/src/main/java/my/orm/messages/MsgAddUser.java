package my.orm.messages;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import my.orm.app.HibernateDBService;
import my.orm.datasets.PhoneDataSet;
import my.orm.datasets.UserDataSet;
import my.orm.dbservice.DBServiceException;
import my.orm.messageSystem.Address;
import my.orm.messageSystem.MessageSystem;

public class MsgAddUser extends MsgToDB
{
	private final static Logger LOGGER = LoggerFactory.getLogger(MsgAddUser.class);
	
	private final MessageSystem messageSystem;
	private final int sessionId;
	private final String name;
	private final short age;
	private final String address;
	private final List<String> phones;


	public MsgAddUser(Address from, Address to,
		MessageSystem messageSystem,
		int sessionId,
		String name,
		short age,
		String address,
		List<String> phones)
	{
		super(from, to);
		
		this.messageSystem = messageSystem;
		this.sessionId = sessionId;
		this.name = name;
		this.age = age;
		this.address = address;
		this.phones = phones;
	}

	@Override
	public void exec(HibernateDBService dbService)
	{
    	List<PhoneDataSet> phoneds = new ArrayList<>();
    	for (String phone : phones)
    	{
    		if (phone != null)
    			phoneds.add(new PhoneDataSet(phone));
    	}
    	
    	UserDataSet userInfo = new UserDataSet(name, age, address, phoneds);
		
    	try
    	{
			dbService.save(userInfo);
		}
    	catch (DBServiceException e)
    	{
    		LOGGER.info("Error sessionId: {}, text: {}", sessionId, e.getMessage());
    		
    		messageSystem.sendMessage(new MsgError(getTo(), getFrom(), sessionId, e.getMessage())); 
    		return;
		}
    	
    	LOGGER.info("MsgAddUser userId: {}, name: {}", userInfo.getId(), userInfo.getName());
    	
    	messageSystem.sendMessage(new MsgAddUserAnswer(getTo(), getFrom(), sessionId, userInfo.getId()));
	}
}
