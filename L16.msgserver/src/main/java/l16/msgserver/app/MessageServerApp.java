package l16.msgserver.app;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import l16.common.ProcessRunnerImpl;

/**
 * Запуск:
 * 	- путь к каталогу jetty (напр., D:\Progs\jetty-distribution-9.4.12.v20180830
 *  - базовый порт jetty (реальный порт jetty будет равен базовый порт + номер инстанса)
 * 	- количество пар инстансов frontend и dbserver (напр., 2 означает 2 пары)
 * 
 * Пример:
 * %JAVA_HOME%\bin\java -jar ..\target\l16.msgserver-1.0.jar D:\Progs\jetty-distribution-9.4.12.v20180830 9090 2
 */
public class MessageServerApp
{
    private static final Logger LOGGER = LoggerFactory.getLogger(MessageServerApp.class);

    private static final String JAVA_BIN_DIR = "\\bin";
    private static final String JAVA_COMMAND = "java.exe";
	private static final String JAVA_JAR_PARAM = "-jar";
    private static final String DBSERVER_START_COMMAND = "..\\L16.dbserver\\target\\l16.dbserver-1.0.jar";
    private static final int CLIENT_START_DELAY_SEC = 5;

    private static final String JETTY_COMMAND = "start.jar";
    private static final String JETTY_PORT_PARAM = "-Djetty.port=";
    private static final String FRONTEND_NAME = "-Dl16.frontend.name=";
    private static final String DBSERVER_NAME = "-Dl16.dbserver.name=";
    
    private final String jettyPath;
    private final int jettyBasePort;
    private final int instancesCount;
    
    public static void main(String[] args) throws Exception
    {
    	String jettyPath = args[0];
    	
    	Integer port = 0;
    	if (args.length > 1)
    	{
    		port = Integer.parseInt(args[1]);
    	}
    	
    	Integer instancesCount = 1;
    	if (args.length > 2)
    		instancesCount = Integer.parseInt(args[2]);
    	
        new MessageServerApp(jettyPath, port, instancesCount).start();
    }

    MessageServerApp(String jettyPath, Integer jettyPort, Integer instancesCount)
    {
    	this.jettyPath = jettyPath;
    	this.jettyBasePort = jettyPort;
    	this.instancesCount = instancesCount;
    	
    }
    private void start() throws Exception
    {
    	final String javaHome = System.getenv("JAVA_HOME");
    	
        List<ScheduledExecutorService> executorServices = new ArrayList<>();
    	for (int i = 0; i < instancesCount; i++)
    	{
    		final  String dbServiceName = "Back" + Integer.toString(i);
    		List<String> dbServerCommands = new ArrayList<>();
    		dbServerCommands.add(javaHome + JAVA_BIN_DIR + "\\" + JAVA_COMMAND);
    		dbServerCommands.add(JAVA_JAR_PARAM);
    		dbServerCommands.add(DBSERVER_START_COMMAND);
    		if (!dbServiceName.isEmpty())
    			dbServerCommands.add(dbServiceName);

    		ScheduledExecutorService executorService1 = Executors.newSingleThreadScheduledExecutor();
    		startClient(executorService1, null, dbServerCommands);
    		executorServices.add(executorService1);
    		
    		final String frontendServiceName = "Front" + Integer.toString(i);
    		
    		List<String> jettyCommands = new ArrayList<>();
    		jettyCommands.add(javaHome + JAVA_BIN_DIR + "\\" + JAVA_COMMAND);
    		jettyCommands.add(JAVA_JAR_PARAM);
    		jettyCommands.add(jettyPath + "\\" + JETTY_COMMAND);
    	
    		if (jettyBasePort != 0)
    			jettyCommands.add(JETTY_PORT_PARAM + Integer.toString(jettyBasePort + i));

    		if (!frontendServiceName.isEmpty())
    			jettyCommands.add(FRONTEND_NAME + frontendServiceName);

    		if (!dbServiceName.isEmpty())
    			jettyCommands.add(DBSERVER_NAME + dbServiceName);
    	
    		ScheduledExecutorService executorService2 = Executors.newSingleThreadScheduledExecutor();
    		Path workingDir = Paths.get(jettyPath);
    		startClient(executorService2, workingDir, jettyCommands);
    		executorServices.add(executorService2);
    	}

        MessageServer server = new MessageServer();
        server.start();
    	
    	for (ScheduledExecutorService executorService : executorServices)
    	{
    		executorService.shutdown();	
    	}
    }

    private void startClient(ScheduledExecutorService executorService,
    	Path workingDir,
		List<String> command)
    {
        executorService.schedule(() -> {
            try
            {
                new ProcessRunnerImpl().start(workingDir, command);
            }
            catch (IOException e)
            {
                LOGGER.error("Process start error: {}", e.getMessage());
            }
        },
		CLIENT_START_DELAY_SEC,
		TimeUnit.SECONDS);
    }
}