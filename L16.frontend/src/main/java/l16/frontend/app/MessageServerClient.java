package l16.frontend.app;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import l16.common.Address;
import l16.common.Addressee;
import l16.common.ClientSocketMsgWorker;
import l16.common.Message;
import l16.common.SocketMsgWorker;
import l16.common.messages.MsgAddUserAnswer;
import l16.common.messages.MsgError;
import l16.common.messages.MsgGetTotalUsersCountAnswer;
import l16.common.messages.MsgGetUserInfoAnswer;
import l16.common.messages.MsgPing;

@Service
public class MessageServerClient
{
    private static final Logger LOGGER = LoggerFactory.getLogger(MessageServerClient.class);

    private static final String HOST = "localhost";
    private static final int PORT = 5050;
    private static final int MAX_MESSAGES_COUNT = 10;

	private SocketMsgWorker client;
	
    private IFrontendService frontendService; 
    private ExecutorService executor;
    
    public MessageServerClient()
    {
    }
    
    public void setFrontend(IFrontendService frontendService)
    {
    	this.frontendService = frontendService;
    }
    
    public void send(Message msg)
    {
    	if (client != null)
    		client.send(msg);
    }
    
    public void tryStart()
    {
    	if (client != null)
    		return;

    	try
    	{
			client = new ClientSocketMsgWorker(HOST, PORT);
		}
    	catch (IOException e1)
    	{
			e1.printStackTrace();
			return;
		}

        client.init();
        client.send(new MsgPing(((Addressee)frontendService).getAddress(), new Address("MessageServer"), "ping"));
        
    	executor = Executors.newSingleThreadExecutor();
    	executor.execute(this::workLoop);
        //executor.shutdown();
    }

    private void workLoop()
    {
        int count = 0;
        while (count < MAX_MESSAGES_COUNT)
        {
            Message msg;
			try
			{
				msg = client.take();
			}
			catch (InterruptedException e)
			{
				e.printStackTrace();
				return;
			}
            
            LOGGER.info("Message received: {}", msg.toString());
            
            if (msg instanceof MsgAddUserAnswer)
            {
            	MsgAddUserAnswer ans = (MsgAddUserAnswer)msg;
            	frontendService.sendAddUserInfo(ans.getSessionId(), ans.getUserId());
            }
            else if (msg instanceof MsgGetTotalUsersCountAnswer)
            {
            	MsgGetTotalUsersCountAnswer ans = (MsgGetTotalUsersCountAnswer)msg; 
            	frontendService.sendTotalUsersCount(ans.getSessionId(), ans.getCount());
            }
            else if (msg instanceof MsgGetUserInfoAnswer)
            {
            	MsgGetUserInfoAnswer ans = (MsgGetUserInfoAnswer)msg;
            	frontendService.sendUserInfo(ans.getSessionId(), ans.getUserId(), ans.getName(), ans.getAge(), ans.getAddress(), ans.getPhones());
            }
            else if (msg instanceof MsgError)
            {
            	MsgError ans = (MsgError)msg;
            	frontendService.sendError(ans.getSessionId(), ans.getMessage());
            }
            
            count++;
        }
        
        client.close();
    }
}
