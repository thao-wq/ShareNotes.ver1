package client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class NoteGUI extends JFrame {

    private JTextArea textArea;
    private NoteClient client;
    private boolean isLocalTyping = false;

    public NoteGUI() {
        setTitle("Shared Notes - Version 1");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        textArea = new JTextArea();
        textArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        JScrollPane scrollPane = new JScrollPane(textArea);
        add(scrollPane, BorderLayout.CENTER);

        textArea.addKeyListener(new KeyAdapter() {

            @Override
            public void keyPressed(KeyEvent e) {
                isLocalTyping = true;

                if (e.getKeyCode() == KeyEvent.VK_ENTER ||
                    e.getKeyCode() == KeyEvent.VK_BACK_SPACE ||
                    e.getKeyCode() == KeyEvent.VK_DELETE) {

                    sendToServer();
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                isLocalTyping = false;
            }
        });

        try {
            client = new NoteClient("localhost", 9999, this);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Không kết nối được server");
            System.exit(0);
        }

        setVisible(true);
    }

    private void sendToServer() {
        try {
            client.sendToServer(textArea.getText());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateTextFromServer(String newText) {
        if (isLocalTyping) return;

        SwingUtilities.invokeLater(() -> {
            int caret = textArea.getCaretPosition();
            textArea.setText(newText);

            if (caret <= textArea.getText().length()) {
                textArea.setCaretPosition(caret);
            }
        });
    }
}