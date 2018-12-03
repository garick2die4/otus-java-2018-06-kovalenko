package l16.common.messages;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import l16.common.Address;
import l16.common.Message;

public class MsgError extends Message
{
	private final int sessionId;
	private final String message;
	
	@JsonCreator
	public MsgError(
		@JsonProperty("from") Address from,
		@JsonProperty("to") Address to,
		@JsonProperty("sessionId") int sessionId,
		@JsonProperty("message") String message)
	{
		super(from, to);
		
		this.sessionId = sessionId;
		this.message = message;
	}
	
	public int getSessionId()
	{
		return sessionId;
	}
	
	public String getMessage()
	{
		return message;
	}
}
