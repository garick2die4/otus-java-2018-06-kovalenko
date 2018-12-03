package l16.frontend.servlet;

import org.eclipse.jetty.websocket.servlet.ServletUpgradeRequest;
import org.eclipse.jetty.websocket.servlet.ServletUpgradeResponse;
import org.eclipse.jetty.websocket.servlet.WebSocketCreator;

import l16.frontend.app.IFrontendService;

import java.util.logging.Logger;

/**
 * @author v.chibrikov
 */
public class UsersDBSocketCreator implements WebSocketCreator
{
    private final static Logger log = Logger.getLogger(UsersDBSocketCreator.class.getName());
    
    private IFrontendService frontend;

    public UsersDBSocketCreator(IFrontendService frontend)
    {
    	this.frontend = frontend;

        log.info("WebSocketCreator created");
    }

    @Override
    public Object createWebSocket(ServletUpgradeRequest req, ServletUpgradeResponse resp)
    {
        UsersWebSocket socket = new UsersWebSocket(frontend);
        log.info("Socket created");
        return socket;
    }
}
