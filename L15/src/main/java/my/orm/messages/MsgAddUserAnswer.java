package my.orm.messages;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import my.orm.app.FrontendService;
import my.orm.messageSystem.Address;

public class MsgAddUserAnswer extends MsgToFrontend
{
	private final static Logger LOGGER = LoggerFactory.getLogger(MsgAddUserAnswer.class);
	
	private final int sessionId;
    private final long userId;

	public MsgAddUserAnswer(Address from, Address to, int sessionId, long userId)
	{
		super(from, to);
		
		this.sessionId = sessionId;
		this.userId = userId;
	}

	@Override
	public void exec(FrontendService frontendService)
	{
		LOGGER.info("MsgAddUserAnswer sessionId: {}, userId: {}", sessionId, userId);
		
		frontendService.sendAddUserInfo(sessionId, userId);
	}

}
