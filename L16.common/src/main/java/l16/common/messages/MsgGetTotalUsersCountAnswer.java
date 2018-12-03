package l16.common.messages;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import l16.common.Address;
import l16.common.Message;

public class MsgGetTotalUsersCountAnswer extends Message
{
	private final int sessionId;
    private final int count;

    @JsonCreator
    public MsgGetTotalUsersCountAnswer(
		@JsonProperty("from") Address from,
		@JsonProperty("to") Address to,
		@JsonProperty("sessionId") int sessionId,
		@JsonProperty("count") int count)
    {
        super(from, to);
        
        this.sessionId = sessionId;
        this.count = count;
    }
    
	public int getSessionId()
	{
		return sessionId;
	}
	
    public int getCount()
    {
    	return count;
    }
}
