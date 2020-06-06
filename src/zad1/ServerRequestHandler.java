package zad1;

import java.io.IOException;
import java.net.Socket;

public interface ServerRequestHandler {
    String handle(String command, Socket socket) throws IOException;
}
