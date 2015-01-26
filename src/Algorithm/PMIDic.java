package Algorithm;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import Tool.UnicodeReader;

public class PMIDic{
	private String FileDir ;
	//private String 
	private int Listnum ;
	private Map<String ,List<Double>> Dic;
	private List<String> FileList;
	private Set <String> Words;
	public PMIDic(){
		this.Dic= new Hashtable<String,List<Double>>();
		this.FileList = new ArrayList<String>();
		this.Words = new HashSet<String>();
		this.FileList.add("love");
		this.FileList.add("happy");
		this.FileList.add("angry");
		this.FileList.add("sad");
	//	this.FileList.add("neutral");
	}
	
	public String getFileDir() {
		return FileDir;
	}

	public void setFileDir(String fileDir) {
		FileDir = fileDir;
	}

	public int getListnum() {
		return Listnum;
	}

	public void setListnum(int listnum) {
		Listnum = listnum;
	}

	public void run(){
/*		for (Iterator<Entry<String, List<Double>>> iter =this.Dic.entrySet().iterator();iter.hasNext();){
			Entry<String, List<Double>> entry = iter.next();
			List<Double> tmp = entry.getValue();
			
		}*/
	}
	private boolean checkword(String word){
		List<Double> tmp = this.Dic.get(word);
		int cont =0;
		for (Double d:tmp ){
			if (d>0 ) cont++;
		}
		return cont<3;
	}
	public void Save2File(String Filename,boolean flag){
		try {
			FileWriter FW = new FileWriter(Filename,flag);
			FW.write("Word");
			for (String s :this.FileList){
				FW.write("\t"+s);
			}
			FW.write("\n");
			for (Iterator<Entry<String, List<Double>>> iter = this.Dic.entrySet().iterator();iter.hasNext();){
				Entry<String, List<Double>> entry = iter.next();
				if(this.checkword(entry.getKey())){
					FW.write(entry.getKey());
					for (Double d:entry.getValue()){
						FW.write("\t"+d);
					}
					FW.write("\n");
				}
				else{
					System.out.println(entry.getKey()+"\t"+entry.getValue());
				}
			}
			FW.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	public void GetWord(){
		List<Map<String,Double>> tmp = new ArrayList<Map<String,Double>>();
		for (int i=0;i<this.FileList.size();i++){
			System.out.println("---Begin read file : "+this.FileDir+File.separator+this.FileList.get(i)+"---");
			Map<String,Double> tmk= this.Readpmi(this.FileDir+File.separator+this.FileList.get(i));
			tmp.add(tmk);
			
		}
		for(Iterator<String> iter = this.Words.iterator();iter.hasNext();){
			String word = iter.next();
			List<Double> tcp = new ArrayList<Double>();
			for (int i=0;i<this.FileList.size();i++){
				if(tmp.get(i).containsKey(word)){
					tcp.add(tmp.get(i).get(word));
				}else{
					tcp.add((double) -1.0);
				}
			}
			this.Dic.put(word, tcp);
		}
	}
	@SuppressWarnings("finally")
	private Map<String,Double> Readpmi(String file){
		Map<String,Double> dic = new Hashtable<String,Double>();
		try {
			
			BufferedReader  BRinv= new BufferedReader(new UnicodeReader(new FileInputStream(file), "UTF-8"));
			String tmp ="";
			for (;(tmp=BRinv.readLine())!=null;){
				String[] tmp2 = tmp.split(":\t");
				String seed = tmp2[0];
				List<String> value = getdic(tmp);
				System.out.println(seed);
				String tkey = "";
				Double tvalue = 0.0;
				
				for (int i=0;i<this.Listnum;i++){
					try{
					tkey=value.get(i).split("=")[0];
					tvalue = Double.valueOf(value.get(i).split("=")[1]); 
					dic.put(tkey, tvalue);
					this.Words.add(tkey);
					}catch(ArrayIndexOutOfBoundsException aibe){
						System.out.println(value.get(i));
					}
					
				}
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return dic;
	}
	private List<String> getdic (String line){
		List<String> ls = new ArrayList<String>();
		String [] tmp =line.replace(",", "\t").split("\\s+");
		char[] tmk = tmp[1].toCharArray();
		ls.add(String.valueOf(tmk, 1, tmk.length-2));
		for (int i=2;i<tmp.length-1;i++){
			ls.add(tmp[i]);
		}
		tmk = null;
		tmk= tmp[tmp.length-1].toCharArray();
		ls.add(String.valueOf(tmk, 1, tmk.length-2));
		return ls;
	}
	public static void main(String []argvs){
		for(String s:argvs){
			System.out.println(s);
		}
/*		
 * String FileDir= argvs[0];
		String ListNum = argvs[2];
		String WriteFile = argvs[1];
		*/
		String FileDir = "./2013-10-12";
		String ListNum = "1000";
		String WriteFile = "Text-20150113";
		PMIDic pmidic = new PMIDic();
		pmidic.setFileDir(FileDir);
		pmidic.setListnum(Integer.valueOf(ListNum));
		pmidic.GetWord();
		pmidic.run();
		pmidic.Save2File(WriteFile, false);
	}
}