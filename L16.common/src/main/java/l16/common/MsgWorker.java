package l16.common;

import java.io.Closeable;

public interface MsgWorker extends Closeable
{
    void send(Message msg);

    void ret(Message msg);
    
    Message poll();

    Message take() throws InterruptedException;

    void close();
}
