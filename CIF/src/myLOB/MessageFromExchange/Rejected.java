package myLOB.MessageFromExchange;

public class Rejected implements MessageFromExchange {
	private final OrderMessageType	_type;
	private final long				_clientId;
	private final long				_clientMessageId;
	private final String			_description;

	/***********************************************************************
	 * Static Factory
	 ***********************************************************************/
	private Rejected(final long clinetId, final long clientMessageId, final String description) {
		_type = OrderMessageType.Reject;
		_clientId = clinetId;
		_clientMessageId = clientMessageId;
		_description = description;
	}

	public static Rejected newMessage(final long clientId, final long clientMessageId, final String description) {
		return new Rejected( clientId, clientMessageId, description );
	}

	/***********************************************************************
	 * Getter and Setter
	 ***********************************************************************/
	public long getClientId() 			{ return _clientId; }
	public long getClientMessageId() 	{ return _clientMessageId; }
	public String getDescription() 		{ return _description; }

	/***********************************************************************
	 * {@link MessageFromExchange} methods
	 ***********************************************************************/
	@Override
	public OrderMessageType getType() 	{ return _type; }

}
