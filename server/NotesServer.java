package server;

import ocsf.server.AbstractServer;
import ocsf.server.ConnectionToClient;

import common.NoteCommand;

public class NotesServer extends AbstractServer {

    private SharedDocument document = new SharedDocument();
    private HistoryManager history = new HistoryManager();

    public NotesServer(int port) {
        super(port);
    }

    @Override
    protected void handleMessageFromClient(Object msg, ConnectionToClient client) {

        System.out.println("Message received: " + msg);

        if (msg instanceof NoteCommand) {

            NoteCommand command = (NoteCommand) msg;

            if (command.getType().equals(NoteCommand.UPDATE)) {

                System.out.println("UPDATE received");

                history.push(document.save());
                document.setContent(command.getContent());

                sendToAllClients(document.getContent());
            }

            else if (command.getType().equals(NoteCommand.UNDO)) {

                System.out.println("UNDO received");

                if (history.hasHistory()) {

                    DocumentMemento m = history.undo();
                    document.restore(m);

                    sendToAllClients(document.getContent());
                }
            }
        }
    }
    public static void main(String[] args) {

        NotesServer server = new NotesServer(7000);

        try {
            server.listen();
            System.out.println("Server is running on port 7000");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
  }