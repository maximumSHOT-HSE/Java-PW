package com.example.pw15;

import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Client {

    @NotNull private SocketChannel socketChannel;

    public void connect(int port) throws IOException {
        socketChannel = SocketChannel.open();
        socketChannel.configureBlocking(false);
        socketChannel.connect(new InetSocketAddress(InetAddress.getLocalHost(), port));
    }

    public void disconnect() throws IOException {
        socketChannel.close();
    }

    private void sendListRequest(@NotNull String path) throws IOException {
        try (var dataOutputStream = new DataOutputStream(socketChannel.socket().getOutputStream())) {
            dataOutputStream.writeInt(1);
            dataOutputStream.writeUTF(path);
        }
    }

    private List<ServerFile> receiveListRequest() throws IOException {
        try (var dataInputStream = new DataInputStream(socketChannel.socket().getInputStream())) {
            int size = dataInputStream.readInt();
            if (size < 0) {
                return null;
            }
            List<ServerFile> serverFiles = new ArrayList<>();
            for (int i = 0; i < size; i++) {
                var name =  dataInputStream.readUTF();
                boolean isDirectory = dataInputStream.readBoolean();
                serverFiles.add(new ServerFile(name, isDirectory));
            }
            return serverFiles;
        }
    }

    public List<ServerFile> executeList(@NotNull String path) throws IOException {
        sendListRequest(path);
        return receiveListRequest();
    }

    private void sendGetRequest(@NotNull String path) throws IOException {
        try (var dataOutputStream = new DataOutputStream(socketChannel.socket().getOutputStream())) {
            dataOutputStream.writeInt(2);
            dataOutputStream.writeUTF(path);
        }
    }

    private byte[] receiveGetRequest() throws IOException {
        try (var dataInputStream = new DataInputStream(socketChannel.socket().getInputStream())) {
            long size = dataInputStream.readLong();
            if (size < 0) {
                return null;
            }
            byte[] content = dataInputStream.readAllBytes();
            return content;
        }
    }

    public byte[] executeGet(@NotNull String path) throws IOException {
        sendGetRequest(path);
        return receiveGetRequest();
    }

    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter port: ");

        int port = scanner.nextInt();

        Client client = new Client();
        client.connect(port);

        while (true) {
            int type = scanner.nextInt();
            String path = scanner.nextLine();

            if (type == 1) {
                var list = client.executeList(path);
                System.out.println("List: size = " + list.size());
                for (var serverFile : list) {
                    System.out.println(serverFile.getName() + " | " + (serverFile.isDirectory() ? "d" : "f"));
                }
            } else {
                var file = client.executeGet(path);
                System.out.println("Get: size = " + file.length);
                System.out.println(Arrays.toString(file));
            }
        }

    }
}
