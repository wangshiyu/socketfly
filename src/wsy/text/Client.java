package wsy.text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.UnknownHostException;

public class Client extends Thread {
    Socket socket;
    BufferedReader in;
    PrintWriter out;
    String ip = "localhost";
    int port = 8888;

    Client(int port) {
        //this.port=port;
    }

    @Override
    public void run() {

        try {
            socket = new Socket(ip, port);
            //socket.close();
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream());
            // socket.setSoTimeout(1000);

            label:
            try {
                SocketAddress socketAddress = new InetSocketAddress(port);
                socket.bind(socketAddress);
            } catch (Exception e) {
                e.printStackTrace();
                break label;
            }
            try {
                this.sleep(1000l);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            try {
                this.sleep(10000000000000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //socket.close();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) throws IOException, InterruptedException {
        for (int i = 0; i < 10; i++) {
            new Client(8888).start();

            System.err.println("客户端：第" + i + "个");
            Thread.sleep(2);
    		/*new Client(8889).start();
    		System.err.println("客户端：第"+i+"个");
    		Thread.sleep(2);*/
        }
    }
}  
