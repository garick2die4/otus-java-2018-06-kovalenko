package my.orm.messages;

import my.orm.app.FrontendService;
import my.orm.messageSystem.Address;

public class MsgError extends MsgToFrontend
{
	private final int sessionId;
	private final String message;
	
	public MsgError(Address from, Address to, int sessionId, String message)
	{
		super(from, to);
		
		this.sessionId = sessionId;
		this.message = message;
	}

	@Override
	public void exec(FrontendService frontendService)
	{
		frontendService.sendError(sessionId, message);
	}
}
