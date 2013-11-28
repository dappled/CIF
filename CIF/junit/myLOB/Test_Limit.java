package myLOB;

import org.junit.Assert;
import org.junit.Test;

/**
 * Test {@link Limit} methods
 * @author Zhenghong Dong
 */
public class Test_Limit {

	@Test
	public void testLimit() throws Exception {
		// test constructor
		final Book bo = new Book();
		Limit lim = new Limit( 160, OrderSide.LONG, bo );
		Assert.assertTrue( lim.getBook() == bo );
		Assert.assertTrue( lim.getKey() == 160 );
		Assert.assertTrue( lim.getLeft() == null );
		Assert.assertTrue( lim.getParent() == null );
		Assert.assertTrue( lim.getRight() == null );
		Assert.assertTrue( lim.getVolumn() == 0 );
		Assert.assertTrue( lim.getSide() == OrderSide.LONG );

		final Exchange1 ex = new Exchange1();
		final Order ord1 = new Order( 1, 1, "GS", OrderSide.LONG, 160, 100, ex );
		final Order ord2 = new Order( 2, 1, "GS", OrderSide.LONG, 160, 170, ex );
		final Order ord3 = new Order( 3, 1, "GS", OrderSide.LONG, 160, 190, ex );

		// test limit constructor with order input
		lim = new Limit( ord1, bo );
		Assert.assertTrue( lim.getVolumn() == 100 );

		// test Limit.add
		lim.add( ord2 );
		lim.add( ord3 );
		Assert.assertTrue( lim.getVolumn() == 460 );
		Assert.assertTrue( ord1.getPrev() == null );
		Assert.assertTrue( ord1.getNext() == ord2 );
		Assert.assertTrue( ord2.getPrev() == ord1 );
		Assert.assertTrue( ord2.getNext() == ord3 );
		Assert.assertTrue( ord3.getPrev() == ord2 );
		Assert.assertTrue( ord3.getNext() == null );

		// test Limit.remove, after deleting ord1, limit should only have ord2 and ord3
		lim.remove( ord1 );
		Assert.assertTrue( lim.getVolumn() == 360 );
		Assert.assertTrue( ord2.getPrev() == null );
		Assert.assertTrue( ord2.getNext() == ord3 );
		Assert.assertTrue( ord3.getPrev() == ord2 );
		Assert.assertTrue( ord3.getNext() == null );

		// test Limit.replace
		lim.replace( ord2, 100 );
		Assert.assertTrue( lim.getVolumn() == 290 );
		Assert.assertTrue( ord2.getQuantity() == 100 );

		// test Limit.fill, after fill 160, should only leave 130 shares of ord3 in this limit
		Assert.assertTrue( lim.fill( 160 ) == 0 );
		Assert.assertTrue( lim.getVolumn() == 130 );
		Assert.assertTrue( ord3.getQuantity() == 130 );
		Assert.assertTrue( ord3.getPrev() == null );
		Assert.assertTrue( ord3.getNext() == null );
	}
}
