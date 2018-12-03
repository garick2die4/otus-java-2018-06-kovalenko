package l16.msgserver.app;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import l16.common.Address;
import l16.common.MsgWorker;

public class MessageServer
{
    private static final Logger LOGGER = LoggerFactory.getLogger(MessageServer.class.getName());

    private static final String MESSAGE_SERVER_NAME = "MessageServer";
    
    private static final int THREADS_NUMBER = 1;
    private static final int PORT = 5050;

    private final ExecutorService executor;
    private final Map<Address, MsgWorker> workersMap;
    private final List<MsgWorker> workers;

    public MessageServer()
    {
        executor = Executors.newFixedThreadPool(THREADS_NUMBER);
        workers = new CopyOnWriteArrayList<>();
        workersMap = new ConcurrentHashMap<>();
    }

    public void start() throws Exception
    {
        executor.submit(this::workLoop);

        try (ServerSocket serverSocket = new ServerSocket(PORT))
        {
        	serverSocket.setReuseAddress(true);
        	
        	LOGGER.info("Server started on port: " + serverSocket.getLocalPort());
        	
            while (!executor.isShutdown())
            {
                Socket socket = serverSocket.accept();
                MsgServerWorker worker = new MsgServerWorker(socket);
                worker.init();
                workers.add(worker);
            }
        }
    }

    @SuppressWarnings("InfiniteLoopStatement")
    private void workLoop()
    {
        while (true)
        {
            for (MsgWorker worker : workers)
            {
            	JsonMessage msg = (JsonMessage)worker.poll();
                while (msg != null)
                {
                	// если привязку отправитель-воркер
                	workersMap.putIfAbsent(msg.getFrom(), worker);
                	
                	if (workersMap.containsKey(msg.getTo()))
                	{
                		LOGGER.info("Message received: {}", msg);

                		// отправляем получателю
                		MsgWorker toWorker = workersMap.get(msg.getTo()); 
                		toWorker.send(msg);
                	}
                	else if (!msg.getTo().getId().equals(MESSAGE_SERVER_NAME))
                	{
                		// если не нашли получателя возвращаем в очередь
                		worker.ret(msg);
                	}
                	
                	msg = (JsonMessage)worker.poll();
                }
            }
            try
            {
                Thread.sleep(100);
            }
            catch (InterruptedException e)
            {
            	LOGGER.error("Interrupted {}", e.toString());
            }
        }
    }
}