package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class Word{
	private String Name;
	private String Nature;
	public Word (String name,String nature){
		this.Name=name;
		this.Nature=nature;
	}
	public Word(){
		this.Name="";
		this.Nature="";
	}
	public Word( Word w){
		this.Name=w.getName();
		this.Nature= w.getNature();
	}
	public String getName() {
		return this.Name;
	}
	public void setName(String name) {
		this.Name = name;
	}
	public String getNature() {
		return this.Nature;
	}
	public void setNature(String nature) {
		this.Nature = nature;
	}
	public boolean equals( Object obj){
		if (this==obj)
			return true;
		else if(!(obj instanceof Word)){
			return false;
		}
		else{
			Word word = (Word)obj;
			return this.Name.equals(word.getName())&&this.Nature.equals(word.getNature());
		}
	}
	public int compareTo(Word word){
		if (!this.Name.equals(word.getName())){
			return this.Name.compareTo(word.getName());
		}else if (!this.Nature.equals(word.getNature())){
			return this.Nature.compareTo(word.getNature());
		}else {
			return 0;
		}
	}
	public String toString(){
		return this.Name+"\t"+this.Nature;
	}
	public int hashCode(){
		return String.valueOf(this.Name+this.Nature).hashCode();
	}
/*	
	public static void main(String [] argvs){
		List<Word> tmp = new ArrayList<Word>();
		tmp.add(new Word("hope","n"));
		tmp.add(new Word("hope","n"));
		tmp.add(new Word("hope","v"));
		tmp.add(new Word("hopeless","adj"));
		tmp.add(new Word("希望","n"));
		tmp.add(new Word("希望","n"));
		for (Word w:tmp){
			System.out.println(w.hashCode());
		}
		Map<Word,Integer> tmk = new Hashtable<Word,Integer>();
		for(Word w:tmp){
			if (tmk.containsKey(w)){
				tmk.put(w, tmk.get(w)+1);
			}else{
				tmk.put(w, 1);
			}
		}
		for (Iterator<Entry<Word, Integer>> iter = tmk.entrySet().iterator();iter.hasNext();){
			Entry<Word,Integer> entry = iter.next();
			System.out.println(entry.getKey().ToString()+"\t"+entry.getValue());
		}
	}
	*/
}