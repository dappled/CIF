package utils1309;

import java.util.Comparator;

public class GenericQuicksort<ValType> implements I_SortAlgorithm<ValType> {

	private ValType[]			_values;
	private Comparator<ValType>	_comparator;

	public GenericQuicksort() {
	}

	@Override
	public void sort(final ValType[] values, final Comparator<ValType> comparator) throws Exception {

		// Check for null array
		if (values == null) throw new Exception("Array passed to sort method of quicksort is null");

		// Check for empty array - It's allowed but there's nothing to do
		if (values.length == 0) return;

		_values = values;
		_comparator = comparator;
		quicksort(0, values.length - 1);

	}

	private void quicksort(final int low, final int high) {
		int i = low, j = high;
		// Get the pivot element from the middle of the list
		final ValType pivot = _values[low + (high - low) / 2];

		// Divide into two lists
		while (i <= j) {
			// If the current value from the left list is smaller then the pivot
			// element then get the next element from the left list
			while (_comparator.compare(_values[i], pivot) < 0) {
				i++;
			}

			// If the current value from the right list is larger then the pivot
			// element then get the next element from the right list
			while (_comparator.compare(_values[j], pivot) > 0) {
				j--;
			}

			// If we have found a values in the left list which is larger then
			// the pivot element and if we have found a value in the right list
			// which is smaller then the pivot element then we exchange the
			// values.
			// As we are done we can increase i and j
			if (i <= j) {
				exchange(i, j);
				i++;
				j--;
			}
		}
		// Recursion
		if (low < j) {
			quicksort(low, j);
		}
		if (i < high) {
			quicksort(i, high);
		}
	}

	private void exchange(final int i, final int j) {
		final ValType temp = _values[i];
		_values[i] = _values[j];
		_values[j] = temp;
	}

}