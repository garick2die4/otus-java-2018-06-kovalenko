package my.orm.servlet;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import my.orm.app.FrontendService;

@WebSocket
public class UsersWebSocket
{
	private final static Logger LOGGER = LoggerFactory.getLogger(UsersWebSocket.class);
	
	private FrontendService frontend;
    private int id;
    
    public UsersWebSocket(FrontendService frontend)
    {
        this.frontend = frontend;
    }

    @OnWebSocketMessage
    public void onMessage(String data)
    {
    	LOGGER.info("data: {}", data);
    	
    	frontend.handleRequest(id, data);
    }

    @OnWebSocketConnect
    public void onOpen(Session session)
    {
    	id = frontend.addSession(session);
    
    	LOGGER.info("onOpen: {}", id);
    }


    @OnWebSocketClose
    public void onClose(int statusCode, String reason)
    {
    	frontend.removeSession(id);
    	
    	LOGGER.info("onClose: {}", id);
    }
}
