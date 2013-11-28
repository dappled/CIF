package utils1309;

import java.util.Comparator;

public interface I_SortAlgorithm<ValType> {

	public void sort( ValType[] values, Comparator<ValType> comparator ) throws Exception;

}
