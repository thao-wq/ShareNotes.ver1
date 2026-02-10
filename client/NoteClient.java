package client;

import ocsf.client.AbstractClient;

public class NoteClient extends AbstractClient {

    private NoteGUI gui;

    public NoteClient(String host, int port, NoteGUI gui) throws Exception {
        super(host, port);
        this.gui = gui;
        openConnection();
    }

    @Override
    protected void handleMessageFromServer(Object msg) {
        if (msg instanceof String) {
            gui.updateTextFromServer((String) msg);
        }
    }
}