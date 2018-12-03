package l16.common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by tully.
 */
public class ProcessRunnerImpl implements ProcessRunner
{
    private final StringBuffer out = new StringBuffer();
    private Process process;

    public void start(Path workingDir, List<String> command) throws IOException
    {
        process = runProcess(workingDir, command);
        //System.out.println("PID: " + process.pid());
    }

    public void stop() {
        process.destroy();
    }

    public String getOutput() {
        return out.toString();
    }

    private Process runProcess(Path workingDir, List<String> command) throws IOException
    {
        ProcessBuilder pb = new ProcessBuilder(command);
        if (workingDir != null)
        	pb.directory(workingDir.toFile());
        pb.redirectErrorStream(true);
        Process p = pb.start();

        StreamListener output = new StreamListener(p.getInputStream(), "OUTPUT");
        output.start();

        return p;
    }

    private class StreamListener extends Thread
    {
        private final Logger logger = Logger.getLogger(StreamListener.class.getName());

        private final InputStream is;
        private final String type;

        private StreamListener(InputStream is, String type)
        {
            this.is = is;
            this.type = type;
        }

        @Override
        public void run()
        {
            try (InputStreamReader isr = new InputStreamReader(is))
            {
                BufferedReader br = new BufferedReader(isr);
                String line;
                while ((line = br.readLine()) != null)
                {
                    out.append(type).append('>').append(line).append('\n');
                }
            }
            catch (IOException e)
            {
                logger.log(Level.SEVERE, e.getMessage());
            }
        }
    }
}
