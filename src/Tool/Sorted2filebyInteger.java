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



public class Sorted2filebyInteger{
	private static class ValueComparator <TKey extends Object> implements Comparator<Map.Entry<TKey,Integer>>{

		@Override
		public int compare(Map.Entry<TKey, Integer> o1, Map.Entry<TKey, Integer> o2) {
			// TODO Auto-generated method stub
			return o2.getValue()-o1.getValue();
		}
		
/*		public <TKey extends Object ,TValue extends Object> int compare (Map.Entry<TKey,TValue> m ,Map.Entry<TKey,TValue> n){
			return n.getValue()-m.getValue();
			
		}*/

	}
	@SuppressWarnings("rawtypes")
	public static <TKey extends Object>void Save2file(String Filename,Map<TKey,Integer> map){
		List<Map.Entry<TKey, Integer>> sortlist= new ArrayList<Entry<TKey, Integer>>();
		sortlist.addAll(map.entrySet());
		Sorted2filebyInteger.ValueComparator vcE= new ValueComparator();
		Collections.sort(sortlist,vcE);
		String TMP ="";
		String TMPKey="";
		int TMPValue=0;
		try {
			FileWriter FW = new FileWriter(Filename); 
			for(Iterator<Entry<TKey, Integer>> iter= sortlist.iterator();iter.hasNext();){
				TMP=iter.next().toString();
				String[] p=TMP.split("=");
				TMPKey=p[0];
				for (int i=1;i<p.length-1;i++){
					TMPKey+="="+p[i];
				}
				TMPValue=Integer.valueOf(p[p.length-1]);
				FW.write(TMPKey+"\t"+TMPValue+"\n");					
			}
			FW.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		

	}
}