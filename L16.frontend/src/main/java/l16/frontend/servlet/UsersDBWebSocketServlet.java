package l16.frontend.servlet;

import org.eclipse.jetty.websocket.servlet.WebSocketServlet;
import org.eclipse.jetty.websocket.servlet.WebSocketServletFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import l16.frontend.app.IFrontendService;

import java.util.concurrent.TimeUnit;

/**
 * This class represents a servlet starting a webSocket application
 */
@Configurable
public class UsersDBWebSocketServlet extends WebSocketServlet
{
	private static final long serialVersionUID = -3012361873325984452L;

	private final static long LOGOUT_TIME = TimeUnit.MINUTES.toMillis(10);

    @Autowired
    private IFrontendService frontend; 

    
    @Override
    public void configure(WebSocketServletFactory factory)
    {
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
        
        factory.getPolicy().setIdleTimeout(LOGOUT_TIME);
        factory.setCreator(new UsersDBSocketCreator(frontend));
    }
}
