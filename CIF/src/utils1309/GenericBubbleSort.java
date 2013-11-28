package utils1309;

import java.util.Comparator;

/**
 * This is a buble sort I pulled off the Internet. I wrote a test for it
 * 
 * @author minddrill
 * 
 */
public class GenericBubbleSort<ValType> implements I_SortAlgorithm<ValType> {

	public GenericBubbleSort() {
	}

	public void sort(ValType[] values, Comparator<ValType> comparator) throws Exception {
		if (values == null) throw new Exception("Array passed to sort method of bubble sort is null");
		boolean swapped;
		ValType swap;
		do {
			swapped = false;
			for (int i = 1; i < values.length; i++) {
				int j = i - 1;
				if (comparator.compare(values[j], values[i]) > 0) {
					swap = values[j];
					values[j] = values[i];
					values[i] = swap;
					swapped = true;
				}
			}
		} while (swapped == true);
	}

}
