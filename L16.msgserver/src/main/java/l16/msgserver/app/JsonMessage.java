package l16.msgserver.app;

import l16.common.Address;
import l16.common.Message;

public final class JsonMessage extends Message
{
	private final String fullMessage;
	
	public JsonMessage(Address from, Address to, String msg)
	{
		super(from, to);
		
		this.fullMessage = msg;
	}
	
	public String getMessage()
	{
		return fullMessage;
	}
	
}
