package io.github.jerryofouc;

import java.nio.ByteBuffer;

/**
 * Created by xiaojiez on 4/5/17.
 */
public class ByteBufferTest {
    public static void main(String[] args) {
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        System.out.println(byteBuffer.limit());
        byteBuffer.put("123".getBytes());
        System.out.println(byteBuffer.position());
        byte[] aa = new byte[]{'1','1','3'};
        ByteBuffer byteBuffer1 = byteBuffer.get(aa);
        byteBuffer.array();
        byteBuffer.position();
        byteBuffer.rewind();
        byteBuffer.position();
        byteBuffer.compact();
        System.out.println("aaa");


    }
}
