package io.github.jerryofouc.react;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

/**
 * Created by xiaojiez on 4/6/17.
 */
public class  EchoServer {
    private final Selector acceptSelector;
    private final ServerSocketChannel serverSocketChannel;
    private static EchoServer instance;
    private ReadWriteEventLoop readWriteEventLoop;

    private EchoServer(int port) throws IOException {
        this.acceptSelector = Selector.open();
        this.serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.configureBlocking(false);
        serverSocketChannel.bind(new InetSocketAddress(port));
        serverSocketChannel.register(acceptSelector, SelectionKey.OP_ACCEPT);
        readWriteEventLoop = new ReadWriteEventLoop();
        new Thread(readWriteEventLoop).start();
        startEventLoop();
    }

    public static EchoServer getInstance() throws IOException {
        if(instance != null){
            return instance;
        }
        synchronized (EchoServer.class){
            if(instance != null){
                return instance;
            }
            instance = new EchoServer(11113);
            return instance;
        }
    }




    private void startEventLoop() throws IOException {
        while (true){
            this.acceptSelector.select();
            Iterator<SelectionKey> keyIterator = acceptSelector.selectedKeys().iterator();
            while (keyIterator.hasNext()){
                SelectionKey selectionKey = keyIterator.next();
                keyIterator.remove();
                if(!selectionKey.isValid()){
                    continue;
                }
                if(selectionKey.isAcceptable()){
                    ServerSocketChannel serverSocketChannel1 = (ServerSocketChannel)selectionKey.channel();
                    SocketChannel socketChannel = serverSocketChannel1.accept();
                    System.out.println(socketChannel.socket().getRemoteSocketAddress()+" created");
                    readWriteEventLoop.register(socketChannel);
                }
            }
        }
    }

    public static void main(String[] args) throws IOException {
        EchoServer echoServer = EchoServer.getInstance();
    }
}
