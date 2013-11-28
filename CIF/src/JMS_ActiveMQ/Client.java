package JMS_ActiveMQ;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 * Created by IntelliJ IDEA.
 * User: eran
 * Date: 12/8/11
 * Time: 12:43 PM
 * To change this template use File | Settings | File Templates.
 */
public class Client {

    public static void main(String[] args) throws Exception{
        Socket server = new Socket("localhost", 43434);
        InputStream stream = server.getInputStream();
        int x = 0;
        while ( (x = stream.read()) != -1 ){
            System.out.println((char) x);
        }
    }
}
