package l16.common.messages;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import l16.common.Address;
import l16.common.Message;

/**
 * Пинг 
 */
public class MsgPing extends Message
{
	private final String message;
	
	@JsonCreator
	public MsgPing(
		@JsonProperty("from") Address from,
		@JsonProperty("to") Address to,
		@JsonProperty("message") String message)
	{
		super(from, to);
		
		this.message = message;
	}
	
	public String getMessage()
	{
		return message;
	}
}