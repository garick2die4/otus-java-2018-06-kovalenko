package l16.common;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by tully.
 */
public class SocketMsgWorker implements MsgWorker
{
    private static final Logger LOGGER = Logger.getLogger(SocketMsgWorker.class.getName());
    private static final int WORKERS_COUNT = 2;
    private static final int QUEUE_CAPACITY = 10;
    
    private final BlockingQueue<Message> output = new LinkedBlockingQueue<>(QUEUE_CAPACITY);
    private final BlockingQueue<Message> input = new LinkedBlockingQueue<>(QUEUE_CAPACITY);

    private final ExecutorService executor;
    
    protected static final ObjectMapper MAPPER = new ObjectMapper();
    protected final Socket socket;

    public SocketMsgWorker(Socket socket)
    {
        this.socket = socket;
        this.executor = Executors.newFixedThreadPool(WORKERS_COUNT);
    }

    @Override
    public void send(Message msg)
    {
        output.add(msg);
    }

    @Override
    public void ret(Message msg)
    {
        input.add(msg);
    }
    
    @Override
    public Message poll()
    {
        return input.poll();
    }

    @Override
    public Message take() throws InterruptedException
    {
        return input.take();
    }

    @Override
    public void close()
    {
        executor.shutdown();
    }

    public void init()
    {
        executor.execute(this::sendMessage);
        executor.execute(this::receiveMessage);
    }

    private void sendMessage()
    {
        try (PrintWriter writer = new PrintWriter(socket.getOutputStream(), true))
        {
            while (socket.isConnected())
            {
                final Message msg = output.take(); //blocks
                
                final String json = writeMessage(msg);
                
                System.out.println("Sending message: " + json);
                
                writer.println(json);
                writer.println();//line with json + an empty line
            }
        }
        catch (InterruptedException | IOException e)
        {
        	LOGGER.log(Level.SEVERE, e.getMessage());
        }
    }

    private void receiveMessage()
    {
        try (final BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream())))
        {
            String inputLine;
            StringBuilder stringBuilder = new StringBuilder();
            //blocks
            while ((inputLine = reader.readLine()) != null)
            {
                stringBuilder.append(inputLine);
                //empty line is the end of the message
                if (inputLine.isEmpty())
                {
                    final String json = stringBuilder.toString();
                    System.out.println("Receiving message: " + json);
                    
                    final Message msg = readMessage(json);
                    
                    input.add(msg);
                    
                    stringBuilder = new StringBuilder();
                }
            }
        }
        catch (IOException e)
        {
        	LOGGER.log(Level.SEVERE, e.getMessage());
        }
        finally
        {
            close();
        }
    }
    
    protected String writeMessage(Message msg) throws JsonProcessingException
    {
    	return MAPPER.writeValueAsString(msg);
    }
    
    protected Message readMessage(String json) throws JsonParseException, JsonMappingException, IOException
    {
    	return MAPPER.readValue(json, Message.class);
    }
}
