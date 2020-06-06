package zad1;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class DistributorServer {
    static int port;
    static Map<String, Integer> translationServers = new HashMap<>();

    public static void main(String[] args) throws IOException {
        port = Integer.parseInt(args[0]);
        System.out.println("DistributorServer starts listening on port: " + port);
        Server s = new Server();
        s.setSrh((command, socket) -> {
            System.out.println(command);
            String[] params = command.split(Pattern.quote("|"));
            if (params[0].equalsIgnoreCase("REG")) {
                translationServers.put(params[1], Integer.parseInt(params[2]));
                System.out.println("DistributorServer registered translation server " + socket.getPort());
                return "OK";
            }
            if (params[0].equalsIgnoreCase("LIST")) {
                new ArrayList<>(translationServers.keySet());
                System.out.println("DistributorServer list servers " + translationServers.keySet().stream().collect(Collectors.toList()));
                return translationServers.keySet().stream().collect(Collectors.joining(","));
            }
            if (params[0].equalsIgnoreCase("TRANS")) {

                Thread t = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Client client = new Client();
                        try {
                           // System.out.println("DistributorServer sends to " + translationServers.get(params[1]) + " phrase " + params[3]);
                            client.startConnection("localhost", translationServers.get(params[1]));
                            client.sendMessage(params[0] + "|" + params[2] + "|" + params[3]);
                            client.stopConnection();
                            System.out.println("DistributorServer sends to " + translationServers.get(params[1]) + " phrase " + params[3]);

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }


                });
                t.start();
            }
            return "";
        });
        s.startListening(port);
    }
}
