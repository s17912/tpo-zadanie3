package zad1;

import java.io.IOException;
import java.util.Arrays;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class TranslationServer {
    private Client client;
    private static int port;
    private static String lang;

    public static void main(String[] args) throws IOException {
        port = Integer.parseInt(args[0]);
        lang = args[1];
        log("TranslationServer starts listening on port: " + port);
        Server s = new Server();
        s.setSrh((command, socket) -> {
            String[] params = command.split(Pattern.quote("|"));
            log("TranslationServer received message " + Arrays.stream(params).collect(Collectors.toList()));
            if (params[0].equalsIgnoreCase("TRANS")) {
                log("TranslationServer received message on " + port);
                Client c = new Client();
                c.startConnection("localhost", Integer.parseInt(params[1]));
                c.sendMessage("COMPLETE|"+params[2].toUpperCase());
                c.stopConnection();
            }
            return "";
        });
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    s.startListening(port);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                ;
            }
        });

        t.start();
        register(8989);

        try {
            Thread.sleep(2000);
            list(8989);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    static void log(String log) {
        System.out.println(log);
    }

    static void register(int distServerIp) throws IOException {
        Client client = new Client();
        client.startConnection("localhost", distServerIp);
        client.sendMessage("REG|" + lang + "|" + port);
        log("TranslationServer sending registration message to sever on port: " + port + " with language " + lang);
        client.stopConnection();
    }


    static void list(int distServerIp) throws IOException {
        Client client = new Client();
        client.startConnection("localhost", distServerIp);
        client.sendMessage("LIST");
        client.stopConnection();
    }
}
