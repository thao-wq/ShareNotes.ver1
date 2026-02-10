package server;

import ocsf.server.AbstractServer;
import ocsf.server.ConnectionToClient;

public class NotesServer extends AbstractServer {

    private String sharedText = "";

    public NotesServer(int port) {
        super(port);
    }

    @Override
    protected void handleMessageFromClient(Object msg, ConnectionToClient client) {

        if (msg instanceof String) {
            sharedText = (String) msg;
            sendToAllClients(sharedText);
        }
    }

    public static void main(String[] args) throws Exception {
        NotesServer server = new NotesServer(9999);
        server.listen();
        System.out.println("Server Version 1 running...");
    }
}