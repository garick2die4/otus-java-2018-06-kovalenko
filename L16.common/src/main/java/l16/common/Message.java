package l16.common;

import com.fasterxml.jackson.annotation.JsonTypeInfo;


/**
 * @author tully
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS)
public abstract class Message
{
    private final Address from;
    private final Address to;
    
    public Message(Address from, Address to)
    {
        this.from = from;
        this.to = to;
    }

    public Address getFrom()
    {
        return from;
    }

    public Address getTo()
    {
        return to;
    }
}
