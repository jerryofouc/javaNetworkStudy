package io.github.jerryofouc.react;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by xiaojiez on 4/6/17.
 */
public class ReadWriteEventLoop implements Runnable{
    private Selector readWriteSelector;
    private Lock lock = new ReentrantLock();
    private Condition registerCondition = lock.newCondition();
    private volatile boolean isRegister = false;

    public ReadWriteEventLoop() throws IOException {
        this.readWriteSelector = Selector.open();
    }

    public void register(SocketChannel socketChannel) throws IOException {
        socketChannel.configureBlocking(false);
        lock.lock();
        try{
            isRegister = true;
            this.readWriteSelector.wakeup();
            socketChannel.register(readWriteSelector, SelectionKey.OP_READ,ByteBuffer.allocate(1024));
            isRegister = false;
            registerCondition.signalAll();
        }finally {
            lock.unlock();
        }
    }


    @Override
    public void run() {
        while (true){
            try {
                lock.lock();
                try{
                    while (isRegister){
                        try {
                            registerCondition.await();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }finally {
                    lock.unlock();
                }
                readWriteSelector.select();

                Iterator<SelectionKey> keyIterator = readWriteSelector.selectedKeys().iterator();
                while (keyIterator.hasNext()) {
                    SelectionKey selectionKey = keyIterator.next();
                    keyIterator.remove();
                    if (!selectionKey.isValid()) {
                        continue;
                    }
                    if(selectionKey.isReadable()){
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
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
