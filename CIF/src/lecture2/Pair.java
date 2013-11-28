package lecture2;

public class Pair <K,V>{
	
	private K k;
	private V v;
	
	public Pair(K key, V value){
		k = key;
		v = value;
	}
	
	public K getKey(){ return k;}
	public V getValue(){ return v;}
	
	public static void main(String[] args){
		Pair<String,String> pair = new Pair<String, String>("key", "value");
		String key = pair.getKey();
		String value = pair.getValue();
		//Integer i = pair.getKey();
	}

}
