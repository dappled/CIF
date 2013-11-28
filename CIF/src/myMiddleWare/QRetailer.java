package myMiddleWare;

import java.util.Properties;

import javax.jms.Queue;
import javax.jms.QueueConnectionFactory;
import javax.jms.Session;
import javax.jms.StreamMessage;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.jms.TopicConnectionFactory;
import javax.naming.InitialContext;

public class QRetailer implements javax.jms.MessageListener {
	private javax.jms.QueueConnection	qConnect		= null;
	private javax.jms.QueueSession		qSession		= null;
	private javax.jms.QueueSender		qSender			= null;
	private javax.jms.TopicConnection	tConnect		= null;
	private javax.jms.TopicSession		tSession		= null;
	private javax.jms.Topic				hotDealsTopic	= null;
	private javax.jms.TopicSubscriber	tsubscriber		= null;
	private static boolean				useJNDI			= false;
	private static String				uname			= null;

	public QRetailer(final String broker, final String username, final String password) {
		try {
			TopicConnectionFactory tFactory = null;
			QueueConnectionFactory qFactory = null;
			InitialContext jndi = null;
			QRetailer.uname = username;
			final Properties env = new Properties();
			// ... specify the JNDI properties specific to the vendor
			jndi = new InitialContext( env );
			tFactory =
					(TopicConnectionFactory) jndi.lookup( broker );
			qFactory =
					(QueueConnectionFactory) jndi.lookup( broker );
			tConnect =
					tFactory.createTopicConnection( username, password );
			qConnect =
					qFactory.createQueueConnection( username, password );
			tConnect.setClientID( username );
			qConnect.setClientID( username );
			tSession =
					tConnect.createTopicSession( false,
							Session.AUTO_ACKNOWLEDGE );
			qSession =
					qConnect.createQueueSession( false,
							javax.jms.Session.AUTO_ACKNOWLEDGE );
			hotDealsTopic = (Topic) jndi.lookup( "Hot Deals" );
			tsubscriber =
					tSession.createDurableSubscriber( hotDealsTopic,
							"Hot Deals Subscription" );
			tsubscriber.setMessageListener( this );
			tConnect.start();
		} catch (final javax.jms.JMSException jmse) {
			jmse.printStackTrace();
			System.exit( 1 );
		} catch (final javax.naming.NamingException jne) {
			jne.printStackTrace();
			System.exit( 1 );
		}
	}

	@Override
	public void onMessage(final javax.jms.Message aMessage) {
		try {
			autoBuy( aMessage );
		} catch (final java.lang.RuntimeException rte) {
			rte.printStackTrace();
		}
	}

	private void autoBuy(final javax.jms.Message message) {
		try {
			final StreamMessage strmMsg = (StreamMessage) message;
			final String dealDesc = strmMsg.readString();
			final String itemDesc = strmMsg.readString();
			final float oldPrice = strmMsg.readFloat();
			final float newPrice = strmMsg.readFloat();
			System.out.println( "Received Hot Buy: " + dealDesc );
			// If price reduction is greater than 10 percent, buy
			if (newPrice == 0 || oldPrice / newPrice > 1.1) {
				final int count = (int) (java.lang.Math.random() * 1000);
				System.out.println( "\nBuying " + count + " " + itemDesc );
				final TextMessage textMsg = tSession.createTextMessage();
				textMsg.setText( count + " " + itemDesc );
				textMsg.setIntProperty( "QTY", count );
				textMsg.setJMSCorrelationID( QRetailer.uname );
				final Queue buyQueue = (Queue) message.getJMSReplyTo();
				qSender = qSession.createSender( buyQueue );
				qSender.send( textMsg,
						javax.jms.DeliveryMode.PERSISTENT,
						javax.jms.Message.DEFAULT_PRIORITY,
						1800000 );
			} else {
				System.out.println( "\nBad Deal. Not buying" );
			}
		} catch (final javax.jms.JMSException jmse) {
			jmse.printStackTrace();
		}
	}

	private void exit(final String s) {
		try {
			if (s != null &&
					s.equalsIgnoreCase( "unsubscribe" ))
			{
				tsubscriber.close();
				tSession.unsubscribe( "Hot Deals Subscription" );
			}
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
			( "java QRetailer broker username password" );
			return;
		}
		final QRetailer retailer = new QRetailer( broker, username, password );
		try {
			System.out.println( "\nRetailer application started.\n" );
			// Read all standard input and send it as a message
			final java.io.BufferedReader stdin =
					new java.io.BufferedReader
					( new java.io.InputStreamReader( System.in ) );
			while (true) {
				final String s = stdin.readLine();
				if (s == null) {
					retailer.exit( null );
				} else if (s.equalsIgnoreCase( "unsubscribe" )) {
					retailer.exit( s );
				}
			}
		} catch (final java.io.IOException ioe) {
			ioe.printStackTrace();
		}
	}
}