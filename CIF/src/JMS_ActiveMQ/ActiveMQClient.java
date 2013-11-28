package JMS_ActiveMQ;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.transport.nio.SelectorManager;

import javax.jms.*;

/**
 * Created by IntelliJ IDEA.
 * User: eran
 * Date: 12/8/11
 * Time: 1:50 PM
 * To change this template use File | Settings | File Templates.
 */
public class ActiveMQClient {

   private class MessageList {

   }


   private static String brokerURL = "tcp://localhost:61616";
   private static ConnectionFactory factory;
   private Connection connection;
   private Session session;
   private String ticker;

   public ActiveMQClient(String ticker) throws Exception {
      factory = new ActiveMQConnectionFactory(brokerURL);
      connection = factory.createConnection();
      connection.start();
      session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
      this.ticker = ticker;
   }

   public static void main(String[] args) throws Exception {
      ActiveMQClient client = new ActiveMQClient(args[0]);
      client.run();
   }

   public void run() throws Exception{
      Destination destination = session.createTopic(ticker);
      MessageConsumer messageConsumer =session.createConsumer(destination);
      messageConsumer.setMessageListener(new MessageListener() {
         public void onMessage(Message message) {
            if (message instanceof TextMessage){
               try{
                  System.out.println( ((TextMessage) message).getText() );
               } catch (JMSException e ){
                  e.printStackTrace();
               }
            }
         }
      });
   }

  public Session getSession() {
     return session;
  }


}
