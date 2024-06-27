package managers;

import java.io.IOException;

public class ManagerSaveException extends RuntimeException {
    public IOException ioException;
    public ManagerSaveException(IOException e) {
        ioException = e;
    }
}
