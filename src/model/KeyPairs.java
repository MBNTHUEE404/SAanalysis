package model;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

//public class KeyPairs<t1 extends Object, t2 extends Object> implements Iterable<KeyPair<t1, t2>>{
public class KeyPairs<t1 extends Object, t2 extends Object>{
	private Map<t1,KeyPair<t1, t2>> MyMap;
	public KeyPairs() {
		MyMap = new HashMap<t1,KeyPair<t1, t2>>();
	}
	
	public KeyPairs(KeyPairs<t1, t2> pairs) {
		this.MyMap =  pairs.MyMap;
	}
	
	public KeyPairs(Map<t1, t2> map) {
		this.MyMap = new HashMap<t1,KeyPair<t1, t2>>();
		Iterator<Map.Entry<t1, t2>> iterator = map.entrySet().iterator();
        while(iterator.hasNext()) {
                Map.Entry<t1, t2> entry = iterator.next();
                //Object key = entry.getKey();
                KeyPair<t1, t2> p = new KeyPair<t1, t2>(entry);
                this.add(p);
        }
	}
	
	/*
	 * if the KeyPairs Has all ready has KeyPair ,renew it 
	 * if not has the t1 ,input the Map
	 */
	public KeyPair<t1, t2> add(KeyPair<t1, t2> p) {

		return this.MyMap.put(p.getKey(),p);
	}
	public KeyPair<t1,t2> add(t1 key, t2 value) {
		return this.MyMap.put(key,new KeyPair<t1, t2>(key,value));
	}
	public KeyPair<t1, t2> get(Object t1) {
		return this.MyMap.get(t1);
	}
	
	
	public KeyPair<t1, t2> remove(KeyPair<t1, t2> p) {
		return this.MyMap.remove(p.getKey());
	}
		
 	public KeyPair<t1, t2> remove(Object key) {
		return this.MyMap.remove(key);
	}
	
	public void clear() {
		this.MyMap.clear();
	}
	
	public int size() {
		return this.MyMap.size();
	}
	
	public boolean containsValue(KeyPair<t1, t2> p) {
		return this.MyMap.containsValue(p);
	}
	public boolean containsKey(KeyPair<t1, t2> p) {
		return this.MyMap.containsKey(p.getKey());
	}
	public boolean containsKey(Object t1) {
		return this.MyMap.containsKey(t1);
	}
	public String toString() {
		return this.MyMap.toString();
	}
	
	public boolean equals(Object p) {
		if ((p instanceof KeyPairs<?, ?>) && this.MyMap.equals(((KeyPairs<?, ?>)p).MyMap)) {
			return true;
		}
		else {
			return false;
		}
	}
	
	public int hashCode() {
		return this.MyMap.toString().hashCode();
	}
	
	@SuppressWarnings("rawtypes")
	public List<KeyPair<t1,t2>> sortByValue(boolean isAscending) 
	{  //true���� false����
		@SuppressWarnings("rawtypes")
		List<KeyPair<t1,t2>> Ans = new ArrayList<KeyPair<t1,t2>>(this.MyMap.values());
		if (isAscending) 
		{
			Collections.sort(Ans, new Comparator<KeyPair<t1, t2>>() 
			{
	            public int compare(KeyPair<t1, t2> p1, KeyPair<t1, t2> p2) 
	            {
	                return p1.getValue().toString().compareTo(p2.getValue().toString());
	            }
	        }
					);
		}
		else {
			Collections.sort(Ans, new Comparator<KeyPair<t1, t2>>() {
	            public int compare(KeyPair<t1, t2> p1, KeyPair<t1, t2> p2) {
	                return p1.getValue().toString().compareTo(p2.getValue().toString()) * -1;
	            }
	        });
			
		}
		return Ans;
	}
	
	public List<KeyPair<t1,t2>> sortByFloatValue(boolean isAscending) {  //true���� false����
		List<KeyPair<t1,t2>> Ans = new ArrayList<KeyPair<t1,t2>>(this.MyMap.values());
		if (isAscending) { 
			Collections.sort(Ans, new Comparator<KeyPair<t1, t2>>() {
	            public int compare(KeyPair<t1, t2> p1, KeyPair<t1, t2> p2) {
	            	float res = Float.parseFloat(p1.getValue().toString()) - Float.parseFloat(p2.getValue().toString());
	            	if (res > 0) {
	            		return 1;
	            	}
	            	else if (res < 0 ) {
	            		return -1;
	            	}
	            	else {
	            		return 0;
	            	}
	            }
	        });
		}
		else {
			Collections.sort(Ans, new Comparator<KeyPair<t1, t2>>() {
	            public int compare(KeyPair<t1, t2> p1, KeyPair<t1, t2> p2) {
	            	float res = Float.parseFloat(p2.getValue().toString()) - Float.parseFloat(p1.getValue().toString());
	            	if (res > 0) {
	            		return 1;
	            	}
	            	else if (res < 0 ) {
	            		return -1;
	            	}
	            	else {
	            		return 0;
	            	}
	            }
	        });
			
		}
		return Ans;
	}
	
	public List<KeyPair<t1,t2>> sortByDoubleValue(boolean isAscending) {  //true���� false����
		List<KeyPair<t1,t2>> Ans = new ArrayList<KeyPair<t1,t2>>(this.MyMap.values());
		if (isAscending) {
			Collections.sort(Ans, new Comparator<KeyPair<t1, t2>>() {
	            public int compare(KeyPair<t1, t2> p1, KeyPair<t1, t2> p2) {
	            	double res = Double.parseDouble(p1.getValue().toString()) - Double.parseDouble(p2.getValue().toString());
	            	if (res > 0) {
	            		return 1;
	            	}
	            	else if (res < 0 ) {
	            		return -1;
	            	}
	            	else {
	            		return 0;
	            	}
	            }
	        });
		}
		else {
			Collections.sort(Ans, new Comparator<KeyPair<t1, t2>>() {
	            public int compare(KeyPair<t1, t2> p1, KeyPair<t1, t2> p2) {
	            	double res = Double.parseDouble(p2.getValue().toString()) - Double.parseDouble(p1.getValue().toString());
	            	if (res > 0) {
	            		return 1;
	            	}
	            	else if (res < 0 ) {
	            		return -1;
	            	}
	            	else {
	            		return 0;
	            	}
	            }
	        });
			
		}
		return Ans;
	}
	
	
	public List<KeyPair<t1,t2>> sortByKey(boolean isAscending) {  //true���� false����
		List<KeyPair<t1,t2>> Ans = new ArrayList<KeyPair<t1,t2>>(this.MyMap.values());	
		if (isAscending) {
			Collections.sort(Ans, new Comparator<KeyPair<t1, t2>>() {
	            public int compare(KeyPair<t1, t2> p1, KeyPair<t1, t2> p2) {
	                return p1.getKey().toString().compareTo(p2.getKey().toString());
	            }
	        });
		}
		else {
			Collections.sort(Ans, new Comparator<KeyPair<t1, t2>>() {
	            public int compare(KeyPair<t1, t2> p1, KeyPair<t1, t2> p2) {
	                return p1.getKey().toString().compareTo(p2.getKey().toString()) * -1;
	            }
	        });
		}
		return Ans;
	}


/*	
	public Iterator<KeyPair<t1, t2>> iterator() {
		return new Iterator<KeyPair<t1, t2>>() {
			private int index = 0;
			public boolean hasNext() {
				return (index < list.size());
			}

			public KeyPair<t1, t2> next() {
				return list.get(index++);
			}

			public void remove() {
				
			}
		};
	}
*/
	
}
