package myLOB;

import myLOB.MessageFromExchange.Accepted;
import myLOB.MessageFromExchange.Fill;
import myLOB.MessageFromExchange.MessageFromExchange;
import myLOB.MessageFromExchange.OrderMessageType;
import myLOB.MessageFromExchange.Rejected;

/**
 * This class extends {@link Exchange} with sentToClient printing message information. For test usage only.
 */
public class Exchange1 extends Exchange{
	@Override
	public void sentToClient(final MessageFromExchange msgFromExchagne) {
		System.out.printf( "%s", msgFromExchagne.getType().toString() );
		final OrderMessageType type = msgFromExchagne.getType();
		if (type == OrderMessageType.Accept) {
			final Accepted accept = (Accepted) msgFromExchagne;
			System.out.printf( ", ClientId: %d, ClientMessageId: %d, ExchangeOrderId: %d\n", accept.getClientId(),
					accept.getClientMessageId(), accept.getExchangeOrderId() );
		}
		else if (type == OrderMessageType.Fill) {
			final Fill fill = (Fill) msgFromExchagne;
			System.out.printf( ", ExchangeOrderId: %d, Quantity: %d\n", fill.getExchangeOrderId(),
					fill.getQuantity() );
		}
		else {
			final Rejected reject = (Rejected) msgFromExchagne;
			System.out.printf( ", ClientId: %d, ClientMessageId: %d, Description: %s\n", reject.getClientId(),
					reject.getClientMessageId(), reject.getDescription() );
		}
	}
}
