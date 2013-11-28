package JMS_ActiveMQ;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by IntelliJ IDEA.
 * User: eran
 * Date: 12/7/11
 * Time: 5:30 PM
 * To change this template use File | Settings | File Templates.
 */
public class Server implements Runnable{

    public static void main(String[] args)throws Exception{
        // starting a server socket
        ServerSocket server = new ServerSocket(43434);
        while (true){
            Socket socket = server.accept();
            Server individualServer = new Server(socket);
            (new Thread(individualServer)).start();
        }

    }

    private Socket socket;
    public Server(Socket socket){
        this.socket = socket;
    }
    

    public void run() {
        try{
            System.out.println("Distributing to: " + socket.getInetAddress());
            PrintWriter writer = new PrintWriter(socket.getOutputStream());
            while (true){
                writer.println(Math.random() );
                writer.flush();
                Thread.sleep(1000);
            }

        } catch (Exception e){
            e.printStackTrace();

        }



    }
}
