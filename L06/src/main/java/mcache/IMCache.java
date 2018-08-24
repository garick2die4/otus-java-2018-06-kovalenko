package mcache;

public interface IMCache<K, V>
{
    void put(K key, V val);

    V get(K key);

    int getHitCount();

    int getMissCount();

    void dispose();

}
