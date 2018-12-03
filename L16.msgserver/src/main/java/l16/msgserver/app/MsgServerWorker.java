package l16.msgserver.app;

import java.io.IOException;
import java.net.Socket;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;

import l16.common.Address;
import l16.common.Message;
import l16.common.SocketMsgWorker;

class MsgServerWorker extends SocketMsgWorker
{
	MsgServerWorker(String host, int port) throws IOException
    {
        this(new Socket(host, port));
    }

    MsgServerWorker(Socket socket)
    {
        super(socket);
    }
    
    @Override
    protected String writeMessage(Message msg) throws JsonProcessingException
    {
    	JsonMessage jsonMessage = (JsonMessage)msg;
    	return jsonMessage.getMessage();
    }
    
    @Override
    protected Message readMessage(String json) throws JsonParseException, JsonMappingException, IOException
    {
    	JsonNode jsonNodeRoot = MAPPER.readTree(json);
    	
    	Address from = MAPPER.readValue(jsonNodeRoot.get("from").toString(), Address.class); 
    	Address to = MAPPER.readValue(jsonNodeRoot.get("to").toString(), Address.class);
    	
    	return new JsonMessage(from, to, json);
    }

}