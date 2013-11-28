package myLOB;

import org.junit.Assert;
import org.junit.Test;

/**
 * Test {@link Order} methods
 * @author Zhenghong Dong
 */
public class Test_Order {

	@Test
	public void testOrder() throws Exception {
		// test constructor
		final Exchange1 ex = new Exchange1();
		final Order ord = new Order( 1, 1, "GS", OrderSide.LONG, 160, 100, ex );
		Assert.assertTrue( ord.getExchange() == ex );
		Assert.assertTrue( ord.getId() == 1 );
		Assert.assertTrue( ord.getLimit() == null );
		Assert.assertTrue( ord.getPrice() == 160 );
		Assert.assertTrue( ord.getQuantity() == 100 );
		Assert.assertTrue( ord.getSide() == OrderSide.LONG );
		Assert.assertTrue( ord.getTicker().equals( "GS" ) );
		Assert.assertTrue( ord.getNext() == null );
		Assert.assertTrue( ord.getPrev() == null );
	
		final Order ord1 = new Order( 1, 1, "GS", OrderSide.LONG, 160, 100, ex );
		final Order ord2 = new Order( 2, 1, "GS", OrderSide.LONG, 160, 170, ex );
		final Order ord3 = new Order( 3, 1, "GS", OrderSide.LONG, 160, 190, ex );
		final Book bo = new Book();
		Limit lim = new Limit( ord1, bo );
		lim.add( ord2 );
		lim.add( ord3 );
		
		// test Order.cancel, after cancelling ord1, limit should only have ord2 and ord3
		ord1.cancel();
		Assert.assertTrue( lim.getVolumn() == 360 );
		Assert.assertTrue( ord2.getPrev() == null );
		Assert.assertTrue( ord2.getNext() == ord3 );
		Assert.assertTrue( ord3.getPrev() == ord2 );
		Assert.assertTrue( ord3.getNext() == null );
	
		// test Order.fill
		Assert.assertTrue( ord2.fill( 100 ) == 0 );
		Assert.assertTrue( lim.getVolumn() == 260 );
		Assert.assertTrue( ord2.getQuantity() == 70);

		// test Order.replace
		ord3.replace( 100 );
		Assert.assertTrue( lim.getVolumn() == 170 );	
		Assert.assertTrue( ord3.getQuantity() == 100 );
	}
}
