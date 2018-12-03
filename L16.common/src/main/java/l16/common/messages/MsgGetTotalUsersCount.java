package l16.common.messages;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import l16.common.Address;
import l16.common.Message;

public class MsgGetTotalUsersCount extends Message
{
    private final int sessionId;
    
    @JsonCreator
    public MsgGetTotalUsersCount(
		@JsonProperty("from") Address from,
		@JsonProperty("to") Address to,
		@JsonProperty("sessionId") int sessionId)
    {
        super(from, to);
        
        this.sessionId = sessionId;
    }
    
	public int getSessionId()
	{
		return sessionId;
	}
}
