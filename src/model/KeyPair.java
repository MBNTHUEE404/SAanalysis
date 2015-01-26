package model;


import java.util.Map;

public class KeyPair<tkey extends Object, tvalue extends Object> {
	private tkey key;
	private tvalue value;
	
	public KeyPair(){
	}
	
	public KeyPair(KeyPair<tkey, tvalue> p){
		this.key = p.key;
		this.value = p.value;
	}
	
	public KeyPair(tkey key, tvalue value){
		this.setKey(key);
		this.setValue(value);
	}
	
	public KeyPair(Map.Entry<tkey, tvalue> m) {
		this.setKey(m.getKey());
		this.setValue(m.getValue());
	}
	
	public void setKey(tkey key) {
		this.key = key;
	}
	
	public tkey getKey() {
		return this.key;
	}
	
	public void setValue(tvalue value) {
		this.value = value;
	}
	
	public tvalue getValue() {
		return this.value;
	}
	
	public String toString() {
		return (key.toString() + "=" + value.toString());
	}

	public boolean equals(Object p) {
		if ((p instanceof KeyPair<?, ?>) && this.key.equals(((KeyPair<?, ?>)p).key) && this.value.equals(((KeyPair<?, ?>)p).value)) {
			return true;
		}
		else {
			return false;
		}
	}
	
	public int hashCode() {
		return this.toString().hashCode();
	}
	
	public static void main(String args[]) {
		KeyPair<String, String> p1 = new KeyPair<String, String>();
		KeyPair<String, String> p2 = new KeyPair<String, String>();
		KeyPair<String, Integer> p3 = new KeyPair<String, Integer>();
		
		p1.setKey("key");
		p1.setValue("value1");
		p2.setKey("key");
		p2.setValue("value2");
		p3.setKey("key");
		p3.setValue(1);
		KeyPair<String, Integer> p4 = new KeyPair<String, Integer>(p3);
		System.out.println(p1.equals(p2));
		System.out.println(p1.equals(p3));
		System.out.println(p4);
		//System.out.println(p1.hashcode());
		System.out.println(p1.hashCode());
		System.out.println(p1.getClass().getSimpleName());
		System.out.println(p2.getClass().getGenericSuperclass());
		System.out.println(p1.getClass().equals(p3.getClass()));
	}
}

