package myLOB.OrderMessage;

import myLOB.Exchange;

public interface OrderMessage {
	OrderMessageType getType();

	long getClientId();

	void getProccessedBy(Exchange exchange);
}
