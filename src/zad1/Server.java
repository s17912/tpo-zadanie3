package zad1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

class Server {
    private ServerSocket serverSocket;
    static ServerRequestHandler srh;


    public void setSrh(ServerRequestHandler srh) {
        this.srh = srh;

    }

    public ServerSocket getServerSocket() {
        return serverSocket;
    }

    public void setServerSocket(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }

    public void startListening(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        while (true)
            new RequestHandler(serverSocket.accept()).start();
    }

    protected void stop() throws IOException {
        serverSocket.close();
    }

    protected static class RequestHandler extends Thread {
        private final Socket clientSocket;

        public RequestHandler(Socket socket) {
            this.clientSocket = socket;
        }

        public void run() {
            try {
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(clientSocket.getInputStream()));

                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                //    System.out.println("DistributorServer received message "+ inputLine);
                    if (".".equals(inputLine)) {
                        out.println("bye");
                        break;
                    }

                    out.println(srh.handle(inputLine, clientSocket));
                }

                in.close();
                out.close();
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}