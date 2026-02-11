package client;

import ocsf.client.AbstractClient;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import common.NoteCommand;

import java.awt.*;

public class NotesClient extends AbstractClient {

    private JTextArea textArea;
    private boolean updatingFromServer = false;
    private Timer typingTimer;

    public NotesClient(String host, int port) {
        super(host, port);
    }

    @Override
    protected void handleMessageFromServer(Object msg) {

        if (msg instanceof String) {

            SwingUtilities.invokeLater(() -> {

                String newContent = (String) msg;

                if (textArea.getText().equals(newContent))
                    return;

                updatingFromServer = true;

                int caretPosition = textArea.getCaretPosition();

                textArea.setText(newContent);

                textArea.setCaretPosition(
                        Math.min(caretPosition, newContent.length())
                );

                updatingFromServer = false;
            });
        }
    }

    public void initGUI() {

        JFrame frame = new JFrame("Shared Notes - Version 2");
        frame.setSize(600, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        textArea = new JTextArea();
        textArea.setFont(new Font("Arial", Font.PLAIN, 16));

        JScrollPane scrollPane = new JScrollPane(textArea);

        JButton btnUndo = new JButton("Undo");

        typingTimer = new Timer(300, e -> {
            if (!updatingFromServer) {
                try {
                    sendToServer(new NoteCommand(
                            NoteCommand.UPDATE,
                            textArea.getText()));
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
        typingTimer.setRepeats(false);

        textArea.getDocument().addDocumentListener(new DocumentListener() {

            private void restartTimer() {
                if (!updatingFromServer) {
                    typingTimer.restart();
                }
            }

            public void insertUpdate(DocumentEvent e) { restartTimer(); }
            public void removeUpdate(DocumentEvent e) { restartTimer(); }
            public void changedUpdate(DocumentEvent e) { }
        });

        btnUndo.addActionListener(e -> {
            try {
                sendToServer(new NoteCommand(NoteCommand.UNDO, null));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        frame.setLayout(new BorderLayout());
        frame.add(scrollPane, BorderLayout.CENTER);
        frame.add(btnUndo, BorderLayout.SOUTH);

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public static void main(String[] args) {

        NotesClient client = new NotesClient("localhost", 7000);

        try {
            client.openConnection();
            client.initGUI();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}