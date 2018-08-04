package app;

import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import javax.management.Notification;
import javax.management.NotificationEmitter;
import javax.management.NotificationListener;
import javax.management.openmbean.CompositeData;

import com.sun.management.GarbageCollectionNotificationInfo;


@SuppressWarnings("restriction")
public class GCListener
{
	private Timer mTimer;
	private MyTimerTask mMyTimerTask;
	private Map<String, GCInfo> stats = new HashMap<>();
	
	public void start()
	{
		mTimer = new Timer();
		mMyTimerTask = new MyTimerTask();
		mTimer.schedule(mMyTimerTask, 1000, 60000);
		
		createListener();
	}
	
	private void createListener()
	{
		List<GarbageCollectorMXBean> gcbeans = ManagementFactory.getGarbageCollectorMXBeans();

		for (GarbageCollectorMXBean gcbean : gcbeans)
		{
			System.out.println(gcbean);
			NotificationEmitter emitter = (NotificationEmitter) gcbean;
			NotificationListener listener = new MyNotificationListener();
			emitter.addNotificationListener(listener, null, null);
		}
	}
	
	private class MyNotificationListener implements NotificationListener
	{
		@Override
		public void handleNotification(Notification notification, Object handback)
		{
			if (!notification.getType().equals(GarbageCollectionNotificationInfo.GARBAGE_COLLECTION_NOTIFICATION))
				return;
			
			CompositeData cd = (CompositeData) notification.getUserData();
			GarbageCollectionNotificationInfo info = GarbageCollectionNotificationInfo.from(cd);

			String gctype = info.getGcAction();
			if ("end of minor GC".equals(gctype))
			{
				gctype = "Young";
			}
			else if ("end of major GC".equals(gctype))
			{
				gctype = "Old";
			}
			if (!stats.containsKey(gctype))
				stats.put(gctype, new GCInfo(info.getGcName()));
			GCInfo gcinfo = stats.get(gctype);
			gcinfo.addTime(info.getGcInfo().getDuration());
		}
	}
	
    class MyTimerTask extends TimerTask
    {
		@Override
		public void run()
		{
			System.out.println();
			stats.entrySet().stream().forEach(entry -> {
				System.out.println(entry.getKey() +
						" " + entry.getValue().name() +
						" время: " + entry.getValue().time() + " мс" + 
						" запуски: " + entry.getValue().count());
			});
			System.out.println();
			stats.clear();
		}
    }
    
    class GCInfo
    {
    	private long time;
    	private long count;
    	private String name;
    	
    	GCInfo(String name)
    	{
    		this.name = name;
    	}
    	
    	public String name()
    	{
    		return name;
    	}
    	
    	public long time()
    	{
    		return time;
    	}
    	
    	public long count()
    	{
    		return count;
    	}
    	
    	public void addTime(long tm)
    	{
    		time += tm;
    		++count;
    	}
    }
}
