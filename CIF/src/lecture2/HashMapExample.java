package lecture2;

import java.util.HashMap;
import java.util.Map;

public class HashMapExample {
	
	private static final Integer ONE = new Integer(1);
    public static void main(String args[]) {
        Map<String, Integer> m = new HashMap<String, Integer>();
        // Initialize frequency table from command line
        for (int i=0; i < args.length; i++) {
            Integer freq = (Integer) m.get(args[i]);
            m.put(args[i], (freq==null ? ONE :
                            new Integer(freq.intValue() + 1)));
        }
        System.out.println(m.size()+ " distinct words detected:");
        System.out.println(m);
    }

}

