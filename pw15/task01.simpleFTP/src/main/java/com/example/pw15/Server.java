package com.example.pw15;

import java.io.Closeable;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.Set;

public class Server implements Closeable, Runnable {
    private ServerSocketChannel serverSocketChannel;
    private int port;

    public Server(int port) {
        this.port = port;
    }

    public static void main(String[] args) throws IOException {
        list(Paths.get("/home/anastasiya/AU/ftp/Java-PW/pw15/task01.simpleFTP"));
        get(Paths.get("/home/anastasiya/AU/ftp/Java-PW/pw15/task01.simpleFTP/gradlew.bat"));
    }

    private static void list(Path path) throws IOException {
        System.out.println(Files.list(path).count());
        Files.list(path).forEach(path1 -> {
            System.out.println("name: " + path1.getFileName() + " " +
                                       "is_dir: " + Files.isDirectory(path1));
        });
    }

    private static void get(Path path) throws IOException {
        if (!Files.isRegularFile(path)) {
            return;
        }
        byte[] bytes = Files.readAllBytes(path);
        System.out.println(bytes.length);
    }

    @Override
    public void close() throws IOException {
        serverSocketChannel.close();
    }

    @Override
    public void run() {

        try {
            serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.socket().bind(new InetSocketAddress(port));
            serverSocketChannel.configureBlocking(false);
            serverSocketChannel.socket().setReuseAddress(true);
            Selector selector = Selector.open();

            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
            while (!Thread.currentThread().isInterrupted()) {
                if (selector.isOpen()) {
                    int numKeys = selector.select();
                    if (numKeys > 0) {
                        handleKeys(serverSocketChannel, selector.selectedKeys());
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleKeys(ServerSocketChannel serverSocketChannel, Set<SelectionKey> keys) {
        final Iterator<SelectionKey> iterator = keys.iterator();

        while (iterator.hasNext()) {
            final SelectionKey key = iterator.next();
            try {
                if (key.isValid()) {
                    if (key.isAcceptable()) {
                        accept(serverSocketChannel, key);
                    } else if (key.isAcceptable()) {
                        read(key);
                    }
                    else {
                        throw new UnsupportedOperationException("Key not supported by server.");
                    }
                } else {
                    throw new UnsupportedOperationException("Key not valid.");
                }
            } finally {
            }

            iterator.remove();
        }
    }

    private void read(SelectionKey key) {
        SocketChannel client = (SocketChannel) key.channel();
        //ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);
    }

    private void accept(ServerSocketChannel channel, SelectionKey key) {
        SocketChannel client = null;
        try {
            client = channel.accept();
            client.configureBlocking(false);
            client.register(key.selector(), SelectionKey.OP_READ);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
