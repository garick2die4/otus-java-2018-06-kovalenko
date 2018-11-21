package my.orm.messages;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import my.orm.app.FrontendService;
import my.orm.messageSystem.Address;

/**
 * Created by tully.
 */
public class MsgGetTotalUsersCountAnswer extends MsgToFrontend
{
	private final static Logger LOGGER = LoggerFactory.getLogger(MsgGetTotalUsersCountAnswer.class);
	
	private final int sessionId;
    private final int count;

    public MsgGetTotalUsersCountAnswer(Address from, Address to, int sessionId, int count)
    {
        super(from, to);
        
        this.sessionId = sessionId;
        this.count = count;
    }

    @Override
    public void exec(FrontendService frontendService)
    {
    	LOGGER.info("MsgGetTotalUsersCountAnswer sessionId: {}, count: {}", sessionId, count);
    	
        frontendService.sendTotalUsersCount(sessionId, count);
    }
}
