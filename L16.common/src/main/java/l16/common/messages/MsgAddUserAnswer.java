package l16.common.messages;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import l16.common.Address;
import l16.common.Message;

/**
 * Сообщение с ответом о создании пользователя 
 */
public class MsgAddUserAnswer extends Message
{
	private final int sessionId;
    private final long userId;

    @JsonCreator
	public MsgAddUserAnswer(
		@JsonProperty("from") Address from,
		@JsonProperty("to") Address to,
		@JsonProperty("sessionId") int sessionId,
		@JsonProperty("userId") long userId)
	{
		super(from, to);
		
		this.sessionId = sessionId;
		this.userId = userId;
	}
	
	public int getSessionId()
	{
		return sessionId;
	}
	
	public long getUserId()
	{
		return userId;
	}
}
