package io.github.jerryofouc;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.*;

/**
 * Created by xiaojiez on 4/5/17.
 */
public class Nio {
    private static Map<SocketChannel,List> dataMapper = new HashMap<>();
    public static void main(String[] args) throws IOException {
        Selector selector = Selector.open();
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.configureBlocking(false);
        serverSocketChannel.bind(new InetSocketAddress(11113));
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        while (true){
            selector.select();
            Iterator<SelectionKey> keyIterator = selector.selectedKeys().iterator();
            while (keyIterator.hasNext()){
                SelectionKey selectionKey = keyIterator.next();
                keyIterator.remove();
                if(!selectionKey.isValid()){
                    continue;
                }
                if(selectionKey.isAcceptable()){
                    ServerSocketChannel serverSocketChannel1 = (ServerSocketChannel)selectionKey.channel();
                    SocketChannel socketChannel = serverSocketChannel1.accept();
                    System.out.println(socketChannel.socket().getRemoteSocketAddress()+" created");;

                    List list = new ArrayList();
                    dataMapper.put(socketChannel,list);
                    socketChannel.configureBlocking(false);
                    socketChannel.register(selector,SelectionKey.OP_READ,ByteBuffer.allocate(1024));
                }else if(selectionKey.isReadable()){
                    SocketChannel socketChannel = (SocketChannel)selectionKey.channel();
                    ByteBuffer byteBuffer = (ByteBuffer)selectionKey.attachment();
                    socketChannel.read(byteBuffer);
                    selectionKey.interestOps(SelectionKey.OP_WRITE);
                }else if(selectionKey.isWritable()){
                    ByteBuffer byteBuffer = (ByteBuffer)selectionKey.attachment();
                    byteBuffer.flip();
                    byte[] dest = new byte[byteBuffer.remaining()];
                    byteBuffer.get(dest);
                    byteBuffer.clear();
                    System.out.print(new String(dest));
                    selectionKey.interestOps(SelectionKey.OP_READ);
                }

            }
        }
    }


}
