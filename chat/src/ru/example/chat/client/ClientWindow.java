package ru.example.chat.client;

import ru.example.network.TCPConnection;
import ru.example.network.TCPConnectionListener;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.awt.event.*;
import java.io.IOException;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;
import javax.swing.border.Border;
import javax.swing.text.*;

public class ClientWindow extends JFrame implements ActionListener, TCPConnectionListener {
    private static final String IP_ADDR = "192.168.134.23";
    private static final int PORT = 8186;
    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;
    private static final Font DEFAULT_FONT = new Font("Verdana", Font.PLAIN, 14);
    private static final Color ACCENT_COLOR = new Color(230, 173, 173);
    private static final int MAX_MESSAGE_HISTORY = 1000;

    private final JTextPane chatArea;
    private final JTextField nickField;
    private final JTextField inputField;
    private final JTextField searchField;
    private final JButton sendButton;
    private final JButton connectButton;
    private final JButton disconnectButton;
    private final JButton clearChatButton;
    private final JButton searchButton;
    private final CopyOnWriteArrayList<Message> messageHistory;
    private final JPopupMenu messageMenu;
    private final AtomicInteger lastSearchPosition;

    private volatile TCPConnection connection;

    private static class Message {
        final String sender;
        String content;
        final long timestamp;
        final int id;
        private static volatile int nextId = 0;

        Message(String sender, String content) {
            this.sender = sender;
            this.content = content;
            this.timestamp = System.currentTimeMillis();
            this.id = nextId++;
        }

        @Override
        public String toString() {
            return String.format("%s: %s", sender, content);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                new ClientWindow();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private ClientWindow() {
        super("Чат с БабойЛесей");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(WIDTH, HEIGHT);
        setLocationRelativeTo(null);
        setResizable(true);

        messageHistory = new CopyOnWriteArrayList<>();
        lastSearchPosition = new AtomicInteger(0);

        chatArea = createChatArea();
        messageMenu = createMessageMenu();
        searchField = createSearchField();
        searchButton = createStyledButton("Поиск");
        nickField = createNickField();
        inputField = createInputField();
        sendButton = createStyledButton("Отправить");
        connectButton = createStyledButton("Присоединиться");
        disconnectButton = createStyledButton("Отсоединиться");
        clearChatButton = createStyledButton("Очистка чата");

        setupLayout();
        setupListeners();
        setVisible(true);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (connection != null) {
                    connection.disconnect();
                }
            }
        });
    }

    private JTextPane createChatArea() {
        JTextPane area = new JTextPane();
        area.setEditable(false);
        area.setBackground(Color.WHITE);
        area.setForeground(new Color(51, 51, 51));
        area.setFont(DEFAULT_FONT);
        return area;
    }

    private JPopupMenu createMessageMenu() {
        JPopupMenu menu = new JPopupMenu();
        JMenuItem deleteItem = new JMenuItem("Удалить сообщение");
        JMenuItem editItem = new JMenuItem("Редактировать");
        JMenuItem copyItem = new JMenuItem("Копировать");

        deleteItem.addActionListener(e -> deleteSelectedMessage());
        editItem.addActionListener(e -> editSelectedMessage());
        copyItem.addActionListener(e -> copySelectedMessage());

        menu.add(deleteItem);
        menu.add(editItem);
        menu.add(copyItem);
        return menu;
    }

    private JTextField createSearchField() {
        JTextField field = new JTextField(20);
        field.setFont(DEFAULT_FONT);
        return field;
    }

    private JTextField createNickField() {
        JTextField field = new JTextField("Бубра");
        field.setFont(new Font("Verdana", Font.BOLD, 16));
        field.setHorizontalAlignment(JTextField.CENTER);
        field.setBorder(createStyledBorder());
        return field;
    }

    private JTextField createInputField() {
        JTextField field = new JTextField();
        field.setFont(DEFAULT_FONT);
        field.setBorder(createStyledBorder());
        return field;
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(DEFAULT_FONT);
        button.setBackground(ACCENT_COLOR);
        return button;
    }

    private Border createStyledBorder() {
        return BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(ACCENT_COLOR, 2),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
        );
    }

    private void setupListeners() {
        inputField.addActionListener(this);
        sendButton.addActionListener(this);
        connectButton.addActionListener(this);
        disconnectButton.addActionListener(this);
        clearChatButton.addActionListener(this);
        searchButton.addActionListener(e -> searchMessages());

        chatArea.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                if (e.isPopupTrigger()) {
                    messageMenu.show(e.getComponent(), e.getX(), e.getY());
                }
            }
        });
    }

    private void setupLayout() {
        setLayout(new BorderLayout(10, 10));

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.add(searchField);
        searchPanel.add(searchButton);

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(searchPanel, BorderLayout.CENTER);

        JScrollPane scrollPane = new JScrollPane(chatArea);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        JPanel bottomPanel = new JPanel(new BorderLayout(10, 10));
        bottomPanel.add(nickField, BorderLayout.WEST);
        bottomPanel.add(inputField, BorderLayout.CENTER);
        bottomPanel.add(sendButton, BorderLayout.EAST);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(connectButton);
        buttonPanel.add(disconnectButton);
        buttonPanel.add(clearChatButton);
        bottomPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private void deleteSelectedMessage() {
        String selectedText = chatArea.getSelectedText();
        if (selectedText == null || selectedText.isEmpty()) return;

        messageHistory.removeIf(message ->
                selectedText.contains(message.toString()));
        refreshChatArea();

        if (connection != null) {
            connection.sendString("/delete " + selectedText);
        }
    }

    private void editSelectedMessage() {
        String selectedText = chatArea.getSelectedText();
        if (selectedText == null || selectedText.isEmpty()) return;

        String newMessage = JOptionPane.showInputDialog(this,
                "Редактировать сообщение:", selectedText);

        if (newMessage != null && !newMessage.isEmpty()) {
            for (Message message : messageHistory) {
                if (selectedText.contains(message.toString())) {
                    message.content = newMessage;
                    break;
                }
            }
            refreshChatArea();

            if (connection != null) {
                connection.sendString("/edit " + selectedText + " -> " + newMessage);
            }
        }
    }

    private void copySelectedMessage() {
        String selectedText = chatArea.getSelectedText();
        if (selectedText != null && !selectedText.isEmpty()) {
            Toolkit.getDefaultToolkit()
                    .getSystemClipboard()
                    .setContents(new StringSelection(selectedText), null);
        }
    }

    private void searchMessages() {
        String searchText = searchField.getText().toLowerCase();
        if (searchText.isEmpty()) return;

        String chatText = chatArea.getText().toLowerCase();
        int position = chatText.indexOf(searchText, lastSearchPosition.get());

        if (position == -1) {
            lastSearchPosition.set(0);
            position = chatText.indexOf(searchText);
        }

        if (position != -1) {
            chatArea.setCaretPosition(position);
            chatArea.moveCaretPosition(position + searchText.length());
            chatArea.requestFocusInWindow();
            lastSearchPosition.set(position + 1);
        } else {
            JOptionPane.showMessageDialog(this,
                    "Текст не найден: " + searchText,
                    "Результат поиска",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void refreshChatArea() {
        SwingUtilities.invokeLater(() -> {
            Document doc = chatArea.getDocument();
            try {
                doc.remove(0, doc.getLength());
                for (Message message : messageHistory) {
                    doc.insertString(doc.getLength(),
                            message.toString() + "\n", null);
                }
            } catch (BadLocationException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if (source == inputField || source == sendButton) {
            sendMessage();
        } else if (source == connectButton) {
            connect();
        } else if (source == disconnectButton) {
            disconnect();
        } else if (source == clearChatButton) {
            clearChat();
        }
    }

    private void sendMessage() {
        String msg = inputField.getText().trim();
        if (msg.isEmpty()) return;

        inputField.setText("");
        if (connection != null) {
            String sender = nickField.getText();
            Message message = new Message(sender, msg);

            while (messageHistory.size() >= MAX_MESSAGE_HISTORY) {
                messageHistory.remove(0);
            }

            messageHistory.add(message);
            connection.sendString(message.toString());
        } else {
            printMessage("Невозможно отправить сообщение. Соединение не установлено.");
        }
    }

    private void connect() {
        if (connection == null) {
            try {
                connection = new TCPConnection(this, IP_ADDR, PORT);
            } catch (IOException ex) {
                printMessage("Ошибка подключения: " + ex.getMessage());
            }
        }
    }

    private void disconnect() {
        if (connection != null) {
            connection.disconnect();
            connection = null;
        }
    }

    private void clearChat() {
        messageHistory.clear();
        refreshChatArea();
    }

    @Override
    public void onConnectionReady(TCPConnection tcpConnection) {
        printMessage("Соединение установлено.");
        for (Message message : messageHistory) {
            printMessage(message.toString());
        }
    }

    @Override
    public void onReceiveString(TCPConnection tcpConnection, String value) {
        if (value.startsWith("/")) {
            printMessage("Системное сообщение: " + value);
        } else {
            Message message = new Message("", value);
            messageHistory.add(message);
            printMessage(value);
        }
    }

    @Override
    public void onDisconnect(TCPConnection tcpConnection) {
        printMessage("Соединение закрыто.");
        connection = null;
    }

    @Override
    public void onException(TCPConnection tcpConnection, IOException e) {
        printMessage("Ошибка соединения: " + e.getMessage());
    }

    private void printMessage(String message) {
        SwingUtilities.invokeLater(() -> {
            try {
                Document doc = chatArea.getDocument();
                doc.insertString(doc.getLength(), message + "\n", null);
                chatArea.setCaretPosition(doc.getLength());
            } catch (BadLocationException e) {
                e.printStackTrace();
            }
        });
    }
}