package ru.example.chat.server;

import ru.example.network.TCPConnection;
import ru.example.network.TCPConnectionListener;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;

@SuppressWarnings("InfiniteLoopStatement")
public class ChatServer implements TCPConnectionListener {
    public static void main(String[] args) {
        new ChatServer();
    }

    private final ArrayList<TCPConnection> connections = new ArrayList<>();

    private ChatServer() {
        System.out.println("Server running...");
        try(ServerSocket serverSocket = new ServerSocket(8186)) {
            while(true) {
                try {
                    new TCPConnection(this, serverSocket.accept());
                } catch (IOException e) {
                    System.out.println("TCPConnection exception: " + e);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public synchronized void onConnectionReady(TCPConnection tcpConnection) {
        connections.add(tcpConnection);
        sendToAllConnections("Client connected: " + tcpConnection);
    }

    @Override
    public synchronized void onReceiveString(TCPConnection tcpConnection, String value) {
        if (value.startsWith("delete:")) {
            // Получаем сообщение для удаления
            String messageToDelete = value.substring(7);
            deleteMessage(messageToDelete);
            sendToAllConnections("Сообщение удалено: " + messageToDelete);
        } else {
            sendToAllConnections(value);
        }
    }

    @Override
    public synchronized void onDisconnect(TCPConnection tcpConnection) {
        connections.remove(tcpConnection);
        sendToAllConnections("Client disconnected: " + tcpConnection);
    }

    @Override
    public void onException(TCPConnection tcpConnection, IOException e) {
        System.out.println("TCPConnection exception: " + e);
    }

    private void sendToAllConnections(String value){
        System.out.println(value);
        for (TCPConnection connection : connections) {
            connection.sendString(value);
        }
    }

    // Метод для удаления сообщения
    private void deleteMessage(String message) {
        // Реализуйте логику удаления сообщения из списка сообщений на сервере
        // Например, можно использовать регулярные выражения для поиска и удаления сообщения
    }
}