package myLOB;

import myLOB.OrderMessage.Add;
import myLOB.OrderMessage.Cancel;
import myLOB.OrderMessage.OrderMessage;
import myLOB.OrderMessage.OrderType;
import myLOB.OrderMessage.Replace;

import org.junit.Assert;
import org.junit.Test;

/**
 * Test {@link Exchange} methods
 * @author Zhenghong Dong
 */
public class Test_Exchange {

	@Test
	public void testExchange() {
		// test constructor && Exchange.createBook
		final Exchange1 ex = new Exchange1();
		ex.createBook( "GS" );
		ex.createBook( "C" );
		Assert.assertTrue( ex != null );
		final long client1 = 1, client2 = 2, client3 = 3;
		long client1msg = 0, client2msg = 0, client3msg = 0;

		// test getBestBid, getBestOffer
		Assert.assertTrue( ex.getBestBid( "GS" ) == -1 ); // no bids yet, so getBestBid returns -1;
		Assert.assertTrue( ex.getBestBid( "GS" ) == -1 ); // no offers yet, so getBestOffer returns -1;

		// test Exchange.add with ordinary Long order
		final OrderMessage add1 = Add.newMessage( client1, ++client1msg, "GS", OrderSide.LONG, 1000, 160,
				OrderType.ORDINARY );
		final OrderMessage add2 = Add.newMessage( client1, ++client1msg, "GS", OrderSide.LONG, 800, 180,
				OrderType.ORDINARY );
		final OrderMessage add3 = Add.newMessage( client2, ++client2msg, "C", OrderSide.LONG, 100, 49,
				OrderType.ORDINARY );
		final OrderMessage add4 = Add.newMessage( client3, ++client3msg, "C", OrderSide.LONG, 200, 46,
				OrderType.ORDINARY );

		// add1 should update bestBid of GS
		ex.receiveMessage( add1 );
		Assert.assertTrue( ex.getBestBid( "GS" ) == 160 );
		// add2 should update bestBid of GS
		ex.receiveMessage( add2 );
		Assert.assertTrue( ex.getBestBid( "GS" ) == 180 );
		// add3 should update bestBid of C, bestBid of GS should remains
		ex.receiveMessage( add3 );
		Assert.assertTrue( ex.getBestBid( "GS" ) == 180 );
		Assert.assertTrue( ex.getBestBid( "C" ) == 49 );
		// add4 should not update bestBid of C
		ex.receiveMessage( add4 );
		Assert.assertTrue( ex.getBestBid( "C" ) == 49 );

		// test Exchange.add with ordinary Short order
		final OrderMessage add5 = Add.newMessage( client1, ++client1msg, "GS", OrderSide.SHORT, 300, 200,
				OrderType.ORDINARY );
		final OrderMessage add6 = Add.newMessage( client1, ++client1msg, "GS", OrderSide.SHORT, 200, 190,
				OrderType.ORDINARY );
		final OrderMessage add7 = Add.newMessage( client2, ++client2msg, "C", OrderSide.SHORT, 600, 59,
				OrderType.ORDINARY );
		final OrderMessage add8 = Add.newMessage( client3, ++client3msg, "C", OrderSide.SHORT, 900, 67,
				OrderType.ORDINARY );

		// add5 should update bestOffer of GS
		ex.receiveMessage( add5 );
		Assert.assertTrue( ex.getBestOffer( "GS" ) == 200 );
		// add6 should update bestOffer of GS
		ex.receiveMessage( add6 );
		Assert.assertTrue( ex.getBestOffer( "GS" ) == 190 );
		// add7 should update bestOffer of C, bestOffer of GS should remains
		ex.receiveMessage( add7 );
		Assert.assertTrue( ex.getBestOffer( "GS" ) == 190 );
		Assert.assertTrue( ex.getBestOffer( "C" ) == 59 );
		// add8 should not update bestOffer of C
		ex.receiveMessage( add8 );
		Assert.assertTrue( ex.getBestOffer( "C" ) == 59 );

		// test Add with fill( partial or fully) with ordinary order
		final OrderMessage add9 = Add.newMessage( client1, ++client1msg, "GS", OrderSide.LONG, 100, 190,
				OrderType.ORDINARY );
		final OrderMessage add10 = Add.newMessage( client1, ++client1msg, "GS", OrderSide.LONG, 200, 210,
				OrderType.ORDINARY );
		final OrderMessage add11 = Add.newMessage( client2, ++client2msg, "C", OrderSide.LONG, 1000, 60,
				OrderType.ORDINARY );

		// add9 should fill part of add6, add6 will remain 100 shares. bestOffer/Bid remains the same.
		ex.receiveMessage( add9 );
		Assert.assertTrue( ex.getBestBid( "GS" ) == 180 );
		Assert.assertTrue( ex.getBestOffer( "GS" ) == 190 );
		Assert.assertTrue( ex.getLimitVolumn( "GS", 190 ) == 100 );

		// add10 should fill the remaining of add6 and part of add5, add5 will remain 200 shares. bestOffer changes to
		// 200 and bestBid remains the same.
		ex.receiveMessage( add10 );
		Assert.assertTrue( ex.getBestBid( "GS" ) == 180 );
		Assert.assertTrue( ex.getBestOffer( "GS" ) == 200 );
		Assert.assertTrue( ex.getLimitVolumn( "GS", 200 ) == 200 );

		// add11 should fill add7 and remains 400 shares, which will be add to limit at 60. $60 will be the bestBid, $67
		// will be the bestOffer.
		ex.receiveMessage( add11 );
		Assert.assertTrue( ex.getBestBid( "C" ) == 60 );
		Assert.assertTrue( ex.getBestOffer( "C" ) == 67 );
		Assert.assertTrue( ex.getLimitVolumn( "C", 60 ) == 400 );

		// test Exchange.add with FOK order
		final OrderMessage add12 = Add.newMessage( client3, ++client3msg, "GS", OrderSide.SHORT, 400, 180,
				OrderType.FOK );
		final OrderMessage add13 = Add.newMessage( client2, ++client2msg, "GS", OrderSide.SHORT, 800, 150,
				OrderType.FOK );
		final OrderMessage add14 = Add.newMessage( client1, ++client1msg, "GS", OrderSide.SHORT, 601, 150,
				OrderType.FOK );
		final OrderMessage add15 = Add.newMessage( client2, ++client2msg, "C", OrderSide.SHORT, 10000, 70,
				OrderType.FOK );

		// add12 should fill part of add2, add2 will remain 400 shares. bestOffer/Bid remains the same.
		ex.receiveMessage( add12 );
		Assert.assertTrue( ex.getBestBid( "GS" ) == 180 );
		Assert.assertTrue( ex.getBestOffer( "GS" ) == 200 );
		Assert.assertTrue( ex.getLimitVolumn( "GS", 180 ) == 400 );

		// add13 should fill the remaining of add2 and part of add1, add1 will remain 600 shares. bestOffer remains the
		// same while bestBid becomes $160.
		ex.receiveMessage( add13 );
		Assert.assertTrue( ex.getBestBid( "GS" ) == 160 );
		Assert.assertTrue( ex.getBestOffer( "GS" ) == 200 );
		Assert.assertTrue( ex.getLimitVolumn( "GS", 160 ) == 600 );

		// add14 will try to fill the remaining of add1 but failed( because the remaining quantity is not enough for
		// add14). Since it is FOK, it will be cancelled totally with no effect on LOB.
		ex.receiveMessage( add14 );
		Assert.assertTrue( ex.getBestBid( "GS" ) == 160 );
		Assert.assertTrue( ex.getBestOffer( "GS" ) == 200 );
		Assert.assertTrue( ex.getLimitVolumn( "GS", 160 ) == 600 );

		// add15 will try to fill the remaining of add11 but failed( beacuse the price does not qualify). Since it is
		// FOK, it will be cancelled totally with no effect on LOB.
		ex.receiveMessage( add15 );
		Assert.assertTrue( ex.getBestBid( "C" ) == 60 );
		Assert.assertTrue( ex.getBestOffer( "C" ) == 67 );
		Assert.assertTrue( ex.getLimitVolumn( "C", 60 ) == 400 );
		Assert.assertTrue( ex.getLimitVolumn( "C", 70 ) == 0 );

		// test Exchange.add with IOC order
		final OrderMessage add16 = Add.newMessage( client3, ++client3msg, "GS", OrderSide.LONG, 100, 200,
				OrderType.IOC );
		final OrderMessage add17 = Add.newMessage( client3, ++client3msg, "GS", OrderSide.LONG, 150, 200,
				OrderType.IOC );
		final OrderMessage add18 = Add.newMessage( client3, ++client3msg, "GS", OrderSide.LONG, 1000, 68,
				OrderType.IOC );

		// add16 should fill part of remaining of add5, add5 will remain 100 shares. No changes to bestOffer/Bid.
		ex.receiveMessage( add16 );
		Assert.assertTrue( ex.getBestBid( "GS" ) == 160 );
		Assert.assertTrue( ex.getBestOffer( "GS" ) == 200 );
		Assert.assertTrue( ex.getLimitVolumn( "GS", 200 ) == 100 );

		// add17 should fill the remaining of add5, the remaining of add17 is cancelled( because it is IOC ). bestOffer
		// will be -1 beacuse there's no offer remaining, bestBid will remain the same.
		ex.receiveMessage( add17 );
		Assert.assertTrue( ex.getBestBid( "GS" ) == 160 );
		Assert.assertTrue( ex.getBestOffer( "GS" ) == -1 );
		Assert.assertTrue( ex.getLimitVolumn( "GS", 200 ) == 0 );

		// add18 will try to fill the remaining of add11 but failed( beacuse the price does not qualify), so it is
		// cancelled totally( because it is IOC ). bestOffer/Bid will have no change.
		ex.receiveMessage( add18 );
		Assert.assertTrue( ex.getBestBid( "C" ) == 60 );
		Assert.assertTrue( ex.getBestOffer( "C" ) == 67 );
		Assert.assertTrue( ex.getLimitVolumn( "C", 60 ) == 400 );

		// test Exchange.replace
		final OrderMessage add19 = Add.newMessage( client2, ++client2msg, "IBM", OrderSide.LONG, 1000, 184,
				OrderType.ORDINARY );
		final OrderMessage replace = Replace.newMessage( client2, 19, 600 );

		// replace will replace add19's amount to 600( from 1000 )
		ex.receiveMessage( add19 );
		ex.receiveMessage( replace );
		Assert.assertTrue( ex.getLimitVolumn( "IBM", 184 ) == 600 );

		// test Exchange.cancel
		final OrderMessage cancel = Cancel.newMessage( client2, 19 );

		// cancel will cancel add19, so IBM will have no bid remaining
		ex.receiveMessage( cancel );
		Assert.assertTrue( ex.getLimitVolumn( "IBM", 184 ) == 0 );
		Assert.assertTrue( ex.getBestBid( "IBM" ) == -1 );

		// cancel2 will try to cancel add19, which has been previously cancelled, so a Reject message will be printed
		final OrderMessage cancel2 = Cancel.newMessage( client2, 19 );
		ex.receiveMessage( cancel2 );

		// replace2 will try to replace add19, which has been previously cancelled, so a Reject message will be printed
		final OrderMessage replace2 = Replace.newMessage( client2, 19, 611 );
		ex.receiveMessage( replace2 );


		// cancel3 will try to cancel other user's order( client1 want to cancel order add19, which is created by
		// client2 ), which is illegal, so a Reject message will be printed
		final OrderMessage add20 = Add.newMessage( client2, ++client2msg, "IBM", OrderSide.LONG, 1000, 184,
				OrderType.ORDINARY );
		ex.receiveMessage( add20 );
		final OrderMessage cancel3 = Cancel.newMessage( client1, 20 );
		ex.receiveMessage( cancel3 );
		
		// replace3 will try to replace other user's order( client1 want to replace order add19, which is created by
		// client2 ), which is illegal, so a Reject message will be printed
		final OrderMessage replace3 = Replace.newMessage( client1, 20, 611 );
		ex.receiveMessage( replace3 );
		
		// add21 will try to add a new order into the order book with negative price, which is wrong, so a Reject message will be printed
		final OrderMessage add21 = Add.newMessage( client2, ++client2msg, "IBM", OrderSide.LONG, 1000, -184,
				OrderType.ORDINARY );
		ex.receiveMessage( add21 );
		
		// add21 will try to add a new order into the order book with negative quantity, which is wrong, so a Reject message will be printed
		final OrderMessage add22 = Add.newMessage( client2, ++client2msg, "IBM", OrderSide.LONG, -1000, 184,
				OrderType.FOK );
		ex.receiveMessage( add22 );
		
	}
}
