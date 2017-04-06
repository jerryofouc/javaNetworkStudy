package io.github.jerryofouc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by xiaojiez on 4/5/17.
 */
public class Bio {
    public static void main(String[] args) throws IOException {
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        ServerSocket serverSocket = new ServerSocket(11112);
        while (true){
            Socket clientSocker = serverSocket.accept();
            executorService.submit(new Worker(clientSocker));
        }
    }


    static class Worker implements Runnable{
        private Socket clientSocker;

        public Worker(Socket clientSocker) {
            this.clientSocker = clientSocker;
        }

        @Override
        public void run() {
            try {
                PrintWriter printWriter = new PrintWriter(clientSocker.getOutputStream());
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(clientSocker.getInputStream()));
                String a ;
                while ((a=bufferedReader.readLine()) != null){
                    printWriter.println(a);
                    printWriter.flush();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                try {
                    clientSocker.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
