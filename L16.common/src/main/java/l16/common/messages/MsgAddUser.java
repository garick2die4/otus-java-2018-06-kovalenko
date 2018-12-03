package l16.common.messages;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import l16.common.Address;
import l16.common.Message;

/**
 * Сообщение для создания пользователя 
 */
public class MsgAddUser extends Message
{
	private final int sessionId;
	private final String name;
	private final short age;
	private final String address;
	private final List<String> phones;

	@JsonCreator
	public MsgAddUser(
		@JsonProperty("from") Address from,
		@JsonProperty("to") Address to,
		@JsonProperty("sessionId") int sessionId,
		@JsonProperty("name") String name,
		@JsonProperty("age") short age,
		@JsonProperty("address") String address,
		@JsonProperty("phones") List<String> phones)
	{
		super(from, to);
		
		this.sessionId = sessionId;
		this.name = name;
		this.age = age;
		this.address = address;
		this.phones = phones;
	}

	public int getSessionId()
	{
		return sessionId;
	}
	
	public String getName()
	{
		return name;
	}
	
	public short getAge()
	{
		return age;
	}
	
	public String getAddress()
	{
		return address;
	}
	
	public List<String> getPhones()
	{
		return phones;
	}

}
