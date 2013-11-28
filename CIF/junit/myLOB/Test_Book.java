package myLOB;

import org.junit.Test;
import org.junit.Assert;

/**
 * Test {@link Book} methods
 * @author Zhenghong Dong
 */
public class Test_Book {

	@Test
	public void testBook() throws Exception {
		//test constructor
		Book bo = new Book();
		Assert.assertTrue( bo.getHighestBuy() == null );
		Assert.assertTrue( bo.getLowestSell() == null );
		
		Exchange1 ex = new Exchange1(); 
		final Order ord1Buy = new Order( 1, 1, "GS", OrderSide.LONG, 160, 100, ex );
		final Order ord2Buy = new Order( 2, 1, "GS", OrderSide.LONG, 160, 170, ex );
		final Order ord3Buy = new Order( 4, 1, "GS", OrderSide.LONG, 260, 90, ex );
		
		final Order ord1Sell = new Order( 1, 1, "GS", OrderSide.SHORT, 360, 100, ex );
		final Order ord2Sell = new Order( 2, 1, "GS", OrderSide.SHORT, 360, 170, ex );
		final Order ord3Sell = new Order( 3, 1, "GS", OrderSide.SHORT, 300, 10, ex );
		
		// test Book.add (for buy order)
		// after first add, should set highestBuy to $160 with quantity 100
		bo.add( ord1Buy );
		Assert.assertTrue( bo.getHighestBuy().getKey() == 160 );
		Assert.assertTrue( bo.getHighestBuy().getVolumn() == 100 );		
		// after second add, should have same best price with quantity 270
		bo.add( ord2Buy );
		Assert.assertTrue( bo.getHighestBuy().getKey() == 160 );
		Assert.assertTrue( bo.getHighestBuy().getVolumn() == 270 );
		// after third add, should update highestBuy to $260 with quantity 90
		bo.add( ord3Buy );
		Assert.assertTrue( bo.getHighestBuy().getKey() == 260 );
		Assert.assertTrue( bo.getHighestBuy().getVolumn() == 90 );
		
		// test Book.add (for sell order)
		// after first add, should set lowestSell to $360 with quantity 100
		bo.add( ord1Sell );
		Assert.assertTrue( bo.getLowestSell().getKey() == 360 );
		Assert.assertTrue( bo.getLowestSell().getVolumn() == 100 );
		// after second add, should have same best price with quantity 270
		bo.add( ord2Sell );
		Assert.assertTrue( bo.getLowestSell().getKey() == 360 );
		Assert.assertTrue( bo.getLowestSell().getVolumn() == 270 );
		// after third add, should update lowestSell to $300 with quantity 10
		bo.add( ord3Sell );
		Assert.assertTrue( bo.getLowestSell().getKey() == 300 );
		Assert.assertTrue( bo.getLowestSell().getVolumn() == 10 );
		
		// test Book.remove
		// after remove ord3Buy, should update highestBuy to $160 with quantity 270
		ord3Buy.cancel(); // this will call bo.remove()
		Assert.assertTrue( bo.getHighestBuy().getKey() == 160 );
		Assert.assertTrue( bo.getHighestBuy().getVolumn() == 270 );
		// after remove ord2Sell, lowestSell will remains same
		ord2Sell.cancel();
		Assert.assertTrue( bo.getLowestSell().getKey() == 300 );
		Assert.assertTrue( bo.getLowestSell().getVolumn() == 10 );
		
		/** Book.fillBuy and Book.fillSell will be tested under {@Link Test_Exchange} */ 
	}
}
