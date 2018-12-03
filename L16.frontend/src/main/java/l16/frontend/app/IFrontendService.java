package l16.frontend.app;

import java.util.List;

import org.eclipse.jetty.websocket.api.Session;

public interface IFrontendService
{
	int addSession(Session session);
	
	void removeSession(int sessionId);
	
	void handleRequest(int sessionId, String data);
	
	void sendTotalUsersCount(int sessionId, int count);
	
	void sendUserInfo(int sessionId, long userId, String name, short age, String address, List<String> phones);
	
	void sendAddUserInfo(int sessionId, long userId);
	
	void sendError(int sessionId, String text);
}
