package l16.common;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

/**
 * Created by tully.
 */
public interface ProcessRunner
{
    void start(Path workingDir, List<String> command) throws IOException;

    void stop();

    String getOutput();
}
