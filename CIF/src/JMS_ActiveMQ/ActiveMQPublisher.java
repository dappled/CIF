package JMS_ActiveMQ;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQMapMessage;

import javax.jms.*;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: eran
 * Date: 12/8/11
 * Time: 1:26 PM
 * To change this template use File | Settings | File Templates.
 */
public class ActiveMQPublisher {

   protected int MAX_DELTA_PERCENT = 1;
   protected Map<String, Double> LAST_PRICES = new Hashtable<String, Double>();
   protected static int count = 10;
   protected static int total;

   protected static String brokerURL = "tcp://localhost:61616";
   protected static ConnectionFactory factory;
   protected Connection connection;
   protected Session session;
   protected MessageProducer producer;

   public ActiveMQPublisher() throws JMSException {
      factory = new ActiveMQConnectionFactory(brokerURL);
      connection = factory.createConnection();
      connection.start();
      session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
      producer = session.createProducer(null);
   }

   public void close() throws JMSException {
       if (connection != null) {
           connection.close();
       }
   }

   public static void main(String[] args) throws Exception {
      ActiveMQPublisher publisher = new ActiveMQPublisher();
      publisher.run();

   }


   public void run() throws Exception{
      String[] stocks = new String[]{"IBM","MSFT"};
      Destination[] topics = new Destination[stocks.length];
      double[] lastPrice = new double[stocks.length];
      Arrays.fill(lastPrice, 100.0);
      for ( int i = 0 ; i < stocks.length; ++i){
         topics[i] = session.createTopic(stocks[i]);
      }
      // Let's start to generate messages
      while (true){
         for ( int i = 0; i < stocks.length; ++i){
            TextMessage msg = session.createTextMessage("Stock: " + stocks[i] + " Price: " + lastPrice[i]);
            producer.send(topics[i],msg);
            lastPrice[i] *= (1 + Math.random()/100.0);
            System.out.println( "Sent: " + msg.getText());
         }
         Thread.sleep(1000);
      }
      //connection.close();
   }

}
