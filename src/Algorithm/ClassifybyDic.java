package Algorithm;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.ansj.domain.Term;

import Tool.UnicodeReader;

public class ClassifybyDic{
	private Map<String ,List<Double>> Dic ;
	private Map<String,List<String>> Seed;
	private String FileDir ;
	private String SeedDir ;
	private String DicDir;
	private List<String> seedfilename;
	
	public String getFileDir() {
		return FileDir;
	}
	public void setFileDir(String fileDir) {
		FileDir = fileDir;
	}
	public String getSeedDir() {
		return SeedDir;
	}
	public void setSeedDir(String seedDir) {
		SeedDir = seedDir;
	}
	public String getDicDir() {
		return DicDir;
	}
	public void setDicDir(String dicDir) {
		DicDir = dicDir;
	}
	public ClassifybyDic(){
		this.Dic= new Hashtable<String,List<Double>>();
		this.Seed = new Hashtable<String,List<String>>();
		this.seedfilename = new ArrayList<String>();
		this.seedfilename.add("love");
		this.seedfilename.add("happy");
		this.seedfilename.add("angry");
		this.seedfilename.add("sad");
	}
	private void initDic(){
		try {
			BufferedReader  BRinv= new BufferedReader(new UnicodeReader(new FileInputStream(this.DicDir), "UTF-8"));
			String tmp = "";
			tmp = BRinv.readLine();
			String [] tmk;
			while((tmp = BRinv.readLine())!=null){
				tmk = tmp.split("\\s+");
				String word = tmk[0];
				List<Double> value = new ArrayList<Double>();
				for (int i=1;i<tmk.length;i++){
					value.add(Double.valueOf(tmk[i]));
				}
				this.Dic.put(word, value);
			}
			BRinv.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private void initSeed(){
		for ( int i=0;i<this.seedfilename.size();i++){
			List <String> tmk = new ArrayList<String>();
			try {
				BufferedReader  BRinv= new BufferedReader(new UnicodeReader(new FileInputStream(this.SeedDir+File.separator+this.seedfilename.get(i)), "UTF-8"));
				String tmp = "";
				tmp = BRinv.readLine();
				
				while((tmp = BRinv.readLine())!=null){
					tmk.add(tmp.split("\\s+")[0]);
				}
				
				BRinv.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			this.Seed.put(this.seedfilename.get(i), tmk);
			tmk.clear();
		}
	}
	public void init (){
		this.initDic();
		this.initSeed();
	}
	private int findMax(double[]d){
		if (d.length>0){
		int ans = 0;
		double max =d[0];
		for(int i=0;i<d.length;i++){
			if(max<d[i]){
				max=d[i];
				ans=i;
			}
		}
		return ans;
		}else{
			return -1;
		}
	}
	private int Emotionkind (String line){
		double [] tmp = new double[this.Seed.size()];
		int cont=0;
		for (Iterator<Entry<String, List<String>>> iter = this.Seed.entrySet().iterator();iter.hasNext();){
			List<String> tmk=iter.next().getValue();
			int value =0;
			for (String s:tmk){
				if(line.contains(s))
					value++;
			}
			tmp [cont]=(double)value;
			cont++;
		}
		int ans = this.findMax(tmp);
		if ( tmp[ans]>0)
			return ans;
		else return -1;
	}
	
	private int Dickind(List<Term> words){
		 double[] tmp = new double[this.Dic.size()];
		 List <String> tmk = new ArrayList<String>();
		 for (Term t:words){
			 tmk.add(t.getName());
		 }
		 for (String s:tmk){
			 if (this.Dic.containsKey(s)){
				 List<Double> value = this.Dic.get(s);
				 if (value.size()==tmp.length){
					 for (int i=0;i<value.size();i++){
						 tmp[i]+=value.get(i);
						
					 }
				 }
			 }
		 }
		 int ans= this.findMax(tmp);
			if ( tmp[ans]>0)
				return ans;
			else return -1;
	}
	
}