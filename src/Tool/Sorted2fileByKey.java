package Tool;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import model.Word;


public class Sorted2fileByKey{
	private static class ValueComparatorByString <TValue extends Object> implements Comparator<Map.Entry<String,TValue>>{

		@Override
		public int compare(Map.Entry<String,TValue> o1, Map.Entry<String,TValue> o2) {
			// TODO Auto-generated method stub
			return o2.getKey().compareTo(o1.getKey());
		}
	}
	public static <TValue extends Object>void Save2file(String Filename,Map<String,TValue> map){
		List<Map.Entry<String,TValue>> sortlist= new ArrayList<Entry<String,TValue>>();
		sortlist.addAll(map.entrySet());
		Sorted2fileByKey.ValueComparatorByString vcE= new ValueComparatorByString();
		Collections.sort(sortlist,vcE);
		String TMPKey="";
		String TMPValue="";
		try {
			FileWriter FW = new FileWriter(Filename); 
			for(Iterator<Entry<String,TValue>> iter= sortlist.iterator();iter.hasNext();){
				TMPKey = iter.next().getKey();
				TMPValue= map.get(TMPKey).toString();

				FW.write(TMPKey+"\t"+TMPValue+"\n");					
			}
			FW.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private static class ValueComparatorByWord <TValue extends Object> implements Comparator<Map.Entry<Word,TValue>>{

		@Override
		public int compare(Map.Entry<Word,TValue> o1, Map.Entry<Word,TValue> o2) {
			// TODO Auto-generated method stub
			return o2.getKey().compareTo(o1.getKey());
		}
	}
	public static <TValue extends Object>void Save2file2(String Filename,Map<Word,TValue> map){
		List<Map.Entry<Word,TValue>> sortlist= new ArrayList<Entry<Word,TValue>>();
		sortlist.addAll(map.entrySet());
		Sorted2fileByKey.ValueComparatorByWord vcE= new ValueComparatorByWord();
		Collections.sort(sortlist,vcE);
		Word TMPKey=null;
		String TMPValue="";
		try {
			FileWriter FW = new FileWriter(Filename); 
			for(Iterator<Entry<Word,TValue>> iter= sortlist.iterator();iter.hasNext();){
				TMPKey = iter.next().getKey();
				TMPValue= map.get(TMPKey).toString();

				FW.write(TMPKey.toString()+"\t"+TMPValue+"\n");					
			}
			FW.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
}