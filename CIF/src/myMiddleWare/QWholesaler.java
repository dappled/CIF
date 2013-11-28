package myMiddleWare;

import java.util.Properties;
import java.util.StringTokenizer;

import javax.jms.Queue;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueReceiver;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.jms.TopicConnectionFactory;
import javax.naming.InitialContext;

public class QWholesaler implements javax.jms.MessageListener {
	private javax.jms.TopicConnection		tConnect		= null;
	private javax.jms.TopicSession			tSession		= null;
	private javax.jms.TopicPublisher		tPublisher		= null;
	private javax.jms.QueueConnection		qConnect		= null;
	private javax.jms.QueueSession			qSession		= null;
	private javax.jms.Queue					receiveQueue	= null;
	private javax.jms.Topic					hotDealsTopic	= null;
	private final javax.jms.TemporaryTopic	buyOrdersTopic	= null;

	public QWholesaler(final String broker, final String username, final String password) {
		try {
			TopicConnectionFactory tFactory = null;
			QueueConnectionFactory qFactory = null;
			InitialContext jndi = null;
			final Properties env = new Properties();
			// ... specify the JNDI properties specific to the vendor
			jndi = new InitialContext( env );
			tFactory =
					(TopicConnectionFactory) jndi.lookup( broker );
			qFactory =
					(QueueConnectionFactory) jndi.lookup( broker );
			tConnect = tFactory.createTopicConnection( username, password );
			qConnect = qFactory.createQueueConnection( username, password );
			tSession =
					tConnect.createTopicSession( false, Session.AUTO_ACKNOWLEDGE );
			qSession =
					qConnect.createQueueSession( false, Session.AUTO_ACKNOWLEDGE );
			hotDealsTopic = (Topic) jndi.lookup( "Hot Deals" );
			receiveQueue = (Queue) jndi.lookup( "SampleQ1" );
			tPublisher = tSession.createPublisher( hotDealsTopic );
			final QueueReceiver qReceiver = qSession.createReceiver( receiveQueue );
			qReceiver.setMessageListener( this );
			// Now that setup is complete, start the Connection
			qConnect.start();
			tConnect.start();
		} catch (final javax.jms.JMSException jmse) {
			jmse.printStackTrace();
			System.exit( 1 );
		} catch (final javax.naming.NamingException jne) {
			jne.printStackTrace();
			System.exit( 1 );
		}
	}

	private void publishPriceQuotes(final String dealDesc, final String username,
			final String itemDesc, final float oldPrice,
			final float newPrice) {
		try {
			final javax.jms.StreamMessage message =
					tSession.createStreamMessage();
			message.writeString( dealDesc );
			message.writeString( itemDesc );
			message.writeFloat( oldPrice );
			message.writeFloat( newPrice );
			message.setStringProperty( "Username", username );
			message.setStringProperty( "itemDesc", itemDesc );
			message.setJMSReplyTo( receiveQueue );
			tPublisher.publish(
					message,
					javax.jms.DeliveryMode.PERSISTENT,
					javax.jms.Message.DEFAULT_PRIORITY,
					1800000 );
		} catch (final javax.jms.JMSException jmse) {
			jmse.printStackTrace();
		}
	}

	@Override
	public void onMessage(final javax.jms.Message message) {
		try {
			final TextMessage textMessage = (TextMessage) message;
			final String text = textMessage.getText();
			System.out.println( "Order received - " + text +
					" from " + message.getJMSCorrelationID() );
		} catch (final java.lang.Exception rte) {
			rte.printStackTrace();
		}
	}

	public void exit() {
		try {
			tConnect.close();
			qConnect.close();
		} catch (final javax.jms.JMSException jmse) {
			jmse.printStackTrace();
		}
		System.exit( 0 );
	}

	public static void main(final String argv[]) {
		String broker, username, password;
		if (argv.length == 3) {
			broker = argv[ 0 ];
			username = argv[ 1 ];
			password = argv[ 2 ];
		} else {
			System.out.println( "Invalid arguments. Should be: " );
			System.out.println
			( "java QWholesaler broker username password" );
			return;
		}
		final QWholesaler wholesaler = new QWholesaler( broker, username, password );
		try {
			// Read all standard input and send it as a message
			final java.io.BufferedReader stdin = new java.io.BufferedReader
					( new java.io.InputStreamReader( System.in ) );
			System.out.println( "Enter: Item, Old Price, New Price" );
			System.out.println( "\ne.g. Bowling Shoes, 100.00, 55.00" );
			while (true) {
				final String dealDesc = stdin.readLine();
				if (dealDesc != null && dealDesc.length() > 0) {
					// Parse the deal description
					final StringTokenizer tokenizer =
							new StringTokenizer( dealDesc, "," );
					final String itemDesc = tokenizer.nextToken();
					String temp = tokenizer.nextToken();
					final float oldPrice =
							Float.valueOf( temp.trim() ).floatValue();
					temp = tokenizer.nextToken();
					final float newPrice =
							Float.valueOf( temp.trim() ).floatValue();
					wholesaler.publishPriceQuotes( dealDesc, username,
							itemDesc, oldPrice, newPrice );
				} else {
					wholesaler.exit();
				}
			}
		} catch (final java.io.IOException ioe) {
			ioe.printStackTrace();
		}
	}
}