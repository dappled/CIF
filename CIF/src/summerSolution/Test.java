/**
 * 
 */
package summerSolution;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Test {
	@SuppressWarnings("unchecked")
	public static <T> T[] unTreeArray(final T[][] input) {
		final java.util.List<T> outList = new ArrayList<T>();
		java.util.List<T> tempList;

		if (input.length == 0) return null;

		T typeVar = null;
		for (final T[] subArray : input) {
			if (typeVar == null && subArray.length > 0) {
				typeVar = subArray[ 0 ];
			}
			tempList = java.util.Arrays.asList( subArray );
			outList.addAll( tempList );
		}

		return outList.toArray( (T[]) Array.newInstance( typeVar.getClass(), 0 ) );
	}

	public static void main(final String[] args) {
		/*double bDay = 0;
		int num=0, thurNum=0;
		LocalDate prev = LocalDate.parse( "1993-10-01" );
		for (LocalDate date = prev; date.isBefore( LocalDate.now() ); date = date.plusDays( 1 )) {
			if (prev.getMonthOfYear() < date.getMonthOfYear() || prev.getYear() < date.getYear()) {
				if (date.getDayOfMonth() < 10 && date.getDayOfWeek() != 6 && date.getDayOfWeek() != 7) {
					if (++bDay == 4) {
						System.out.println( date + ": day of week is " + date.getDayOfWeek() );
						prev = date;
						bDay = 0;
						++num;
						if (date.getDayOfWeek() == 4) ++thurNum;
					}
				}
			}
		}
		System.out.println((double) thurNum/num);*/
		double var = Math.pow( 152.35, 2)*Math.exp( 2*0.0001*252 )*(Math.exp( 0.0001*252 )-1);
		double vol = Math.sqrt( var );
		//double mean = 152.35*Math.exp( 0.0001*252 );
		System.out.printf("var: %.2f, vol: %.2f, n: %.2f",var,vol,Math.pow( (vol*2/0.01), 2));
	}
}