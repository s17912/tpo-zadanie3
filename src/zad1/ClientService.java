package zad1;

import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.regex.Pattern;

public class ClientService {
    Client client;
    Server server;
    ClientGUI clientGUI;

    ClientService(ClientGUI cg) {
        clientGUI = cg;
        client = new Client();
        server = new Server();
        listen();
    }

    String sendMessage(String message) throws IOException {
        client.startConnection("localhost", 8989);
        String response = client.sendMessage(message);
        client.stopConnection();
        return response;
    }

    public String getLanguages() throws IOException {
        return sendMessage("LIST");
    }

    public void translate() throws IOException {
        sendMessage("TRANS|" + clientGUI.getSelectedLanguage() + "|" + clientGUI.getPort() + "|" + clientGUI.getPhrase());

    }
    public void listen(){
        server.setSrh((command, socket) -> {

            String[] params = command.split(Pattern.quote("|"));
            if (params[0].equalsIgnoreCase("COMPLETE")) {
                clientGUI.setResultTF(params[1]);
            }
            return "OK";
        });

            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        server.startListening(8090);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            t.start();


    }
}
