package mcache;

import java.lang.ref.SoftReference;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.function.Function;

public class MCache<K, V> implements IMCache<K, V>
{
    private static final int TIME_THRESHOLD_MS = 5;

    private final int maxElements;
    private final long lifeTime;
    private final long idleTime;
    private final boolean isEternal;

    private final Map<K, MCacheElement<K, SoftReference<V>>> elements = new LinkedHashMap<>();
    private final Timer timer = new Timer();

    private int hit = 0;
    private int miss = 0;

    public MCache(int maxElements, long lifeTime, long idleTime)
    {
        this.maxElements = maxElements;
        this.lifeTime = lifeTime > 0 ? lifeTime : 0;
        this.idleTime = idleTime > 0 ? idleTime : 0;
        this.isEternal = lifeTime == 0 && idleTime == 0;
    }
    
	@Override
	public void put(K key, V val)
	{
        if (elements.size() == maxElements)
        {
            K firstKey = elements.keySet().iterator().next();
            elements.remove(firstKey);
        }

        elements.put(key, new MCacheElement<>(key, new SoftReference<>(val)));

        if (!isEternal)
        {
            if (lifeTime != 0)
            {
                TimerTask lifeTimerTask = getTimerTask(key, lifeElement -> lifeElement.getCreationTime() + lifeTime);
                timer.schedule(lifeTimerTask, lifeTime);
            }
            if (idleTime != 0)
            {
                TimerTask idleTimerTask = getTimerTask(key, idleElement -> idleElement.getLastAccessTime() + idleTime);
                timer.schedule(idleTimerTask, idleTime, idleTime);
            }
        }
	}

	@Override
	public V get(K key)
	{
		MCacheElement<K, SoftReference<V>> element = elements.get(key);
        if (element != null)
        {
            hit++;
            element.setAccessed();
            return element.getValue().get();
        }
        else
        {
            miss++;
            return null;
        }
	}

	@Override
	public int getHitCount()
	{
		return hit;
	}

	@Override
	public int getMissCount()
	{
        return miss;
	}

	@Override
	public void dispose()
	{
		timer.cancel();
	}

    private TimerTask getTimerTask(final K key, Function<MCacheElement<K, SoftReference<V>>, Long> timeFunction)
    {
        return new TimerTask()
        {
            @Override
            public void run()
            {
            	MCacheElement<K, SoftReference<V>> element = elements.get(key);
                if (element == null || isT1BeforeT2(timeFunction.apply(element), System.currentTimeMillis()))
                {
                    elements.remove(key);
                    this.cancel();
                }
            }
        };
    }

    private boolean isT1BeforeT2(long t1, long t2)
    {
        return t1 < t2 + TIME_THRESHOLD_MS;
    }
}
