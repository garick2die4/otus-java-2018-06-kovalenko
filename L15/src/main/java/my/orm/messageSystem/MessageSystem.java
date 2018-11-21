package my.orm.messageSystem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * @author tully
 */
@SuppressWarnings("LoopStatementThatDoesntLoop")
@Service
public final class MessageSystem
{
    private final static Logger LOGGER = LoggerFactory.getLogger(MessageSystem.class);

    private final List<Thread> workers;
    private final Map<Address, LinkedBlockingQueue<Message>> messagesMap;
    private final Map<Address, Addressee> addresseeMap;
    private boolean started = false;
    
    public MessageSystem()
    {
        workers = new ArrayList<>();
        messagesMap = new HashMap<>();
        addresseeMap = new HashMap<>();
    }

    public synchronized void addAddressee(Addressee addressee)
    {
        addresseeMap.put(addressee.getAddress(), addressee);
        messagesMap.put(addressee.getAddress(), new LinkedBlockingQueue<>());
    }

    public synchronized void sendMessage(Message message)
    {
    	LOGGER.info("Send message from {} to {}", message.getFrom().getId(), message.getTo().getId());
    	
    	if (!started)
    		start();
    	
        messagesMap.get(message.getTo()).add(message);
    }

    public void start()
    {
        for (Map.Entry<Address, Addressee> entry : addresseeMap.entrySet())
        {
            String name = "MS-worker-" + entry.getKey().getId();
            Thread thread = new Thread(() ->
            {
                while (true) {
                    LinkedBlockingQueue<Message> queue = messagesMap.get(entry.getKey());
                    while (true)
                    {
                        try
                        {
                            Message message = queue.take();
                        	
                            LOGGER.info("Exec message from {} to {}", message.getFrom().getId(), message.getTo().getId());
                        	
                            message.exec(entry.getValue());
                        }
                        catch (InterruptedException e)
                        {
                        	LOGGER.info("Thread interrupted. Finishing: {0}", name);
                            return;
                        }
                    }
                }
            });
            thread.setName(name);
            thread.start();
            workers.add(thread);
        }
    }

    public void dispose()
    {
        workers.forEach(Thread::interrupt);
    }
}
