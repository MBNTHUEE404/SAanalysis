package Algorithm;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.GZIPInputStream;

import model.Word;

import org.ansj.domain.Term;

import Tool.UnicodeReader;


public class PMI{
	private Map<String,List<Long>>  invertedtable;
	private Map<String,Double> SeedDic;
	private Map<String,Map<String,Double>> PMIvalue;
	private String SeeddicDir;
	private String invertedtableDir;
	private Map<Long,List<String>> weibo;
	private Set<String> wordspeech;
	private String  speechfile;
	int weiboNum;
	int wordNum;
	int Threshold;
	int Kind;
	
	public PMI(){
		this.invertedtable=new Hashtable<String,List<Long>>();
		this.SeedDic= new Hashtable<String,Double>();
		this.PMIvalue= new Hashtable<String,Map<String,Double>>();
		this.SeeddicDir="./Seeddic.txt";
		this.invertedtableDir="./invertedtable.txt";
		this.weibo = new HashMap<Long,List<String>>();
		this.wordspeech = new HashSet<String>();
		this.weiboNum=0;
		this.wordNum=0;
		
	}
	private void GetSpeech(){
		try {
			BufferedReader BR = new BufferedReader(new FileReader (this.speechfile));
			String tmp = "";
			while((tmp =BR.readLine())!=null){
				this.wordspeech.add(tmp);
			}
			BR.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	public String getSpeechfile() {
		return speechfile;
	}
	public void setSpeechfile(String speechfile) {
		this.speechfile = speechfile;
	}
	public void setKind(int kind){
		this.Kind= kind;
	}
	public void setThreshold(int threshold){
		this.Threshold = threshold;
	}
	public String getSeeddicDir() {
		return SeeddicDir;
	}

	public void setSeeddicDir(String seeddicDir) {
		SeeddicDir = seeddicDir;
	}

	public String getInvertedtableDir() {
		return invertedtableDir;
	}

	public void setInvertedtableDir(String invertedtableDir) {
		this.invertedtableDir = invertedtableDir;
	}
	public void showInvertedtable(){
		for (Iterator iter= this.invertedtable.entrySet().iterator();iter.hasNext();){
			System.out.println(iter.next());
		}
	}
	private void GetUsedWeibo(){
		Set <Long>Usedweibo = new HashSet<Long>();
		for (Iterator<Entry<String, Double>> iter = this.SeedDic.entrySet().iterator();iter.hasNext();){
			Entry<String, Double> entry =iter.next();
			if(!invertedtable.containsKey(entry.getKey())){
				System.out.println("the inverted table doesn't contian "+entry.getKey());
			}else{
				Usedweibo.addAll(this.invertedtable.get(entry.getKey()));
			}
		}
		System.out.println("the seed appears in\t"+Usedweibo.size()+"\t weibos");
		for(Iterator<Entry<String, List<Long>>> iter = this.invertedtable.entrySet().iterator();iter.hasNext();){
			Entry<String,List<Long>>  entry = iter.next();
			List<Long> tmp = entry.getValue();
			for(Long l:tmp){
				if(Usedweibo.contains(l)){
					if(this.weibo.containsKey(l)){
						this.weibo.get(l).add(entry.getKey());
					}else{
						this.weibo.put(l, new ArrayList<String>());
						this.weibo.get(l).add(entry.getKey());
					}
				}
			}
		}
		
	}
	private void GetInvertedtable(String Filename){
		try {
			FileInputStream fin = new FileInputStream(Filename);
			GZIPInputStream gzis = new GZIPInputStream(fin);
			InputStreamReader xover = new InputStreamReader(gzis);
			BufferedReader br = new BufferedReader(xover);
			String line="";
			Long id =null;
			Set <Long> weibolist = new HashSet<Long>();
			while((line=br.readLine())!=null){
				String Key = new String(line.split("\\s+")[0]);
				String natrue = new String (line.split("\\s+")[1]);
				if(!this.wordspeech.contains(natrue))
					continue;
	//			System.out.println(Key+"\t"+Key.contains(tmp));
				List<Long> Value = this.getID(line);
	//			System.out.println(Key+"\t"+Value.size());
				if(Value.size()<1) {
					System.out.println(Key+"\t input error!");
					continue;
				}else if(Value.size()>this.Threshold||this.SeedDic.containsKey(Key)){
					weibolist.addAll(Value);
					if(this.invertedtable.containsKey(Key)){
						this.invertedtable.get(Key).addAll(Value);
					}else{
						this.invertedtable.put(Key,new ArrayList<Long>());
						this.invertedtable.get(Key).addAll(Value);
					}
					
					
				}
				this.wordNum+=Value.size();

			}
			
			this.weiboNum=weibolist.size();
			
			br.close();
			xover.close();
			gzis.close();
			fin.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	

	
	
	
	public void initPMI(){
//		System.out.println("-----------init Seed word and invert table---------");
//		String bom = "\\65279";
		
		this.GetSpeech();
		System.out.println(new Date()+"-----init the seed word-----------------------");
		BufferedReader BRseed=null;
		try {
			BRseed = new BufferedReader(new UnicodeReader (new FileInputStream(this.SeeddicDir), "UTF-8"));
			String tmp ="";
			//while((tmp = BRseed.readLine())!=null)
			for(tmp = BRseed.readLine();tmp!=null ;tmp=BRseed.readLine()){
	//			System.out.println(tmp.split("\\s+")[0]+"\t"+ tmp.contains(bom));
				this.SeedDic.put(tmp.split("\\s+")[0],Double.valueOf(tmp.split("\\s+")[1]));
				/*if(!tmp.equals("\n")&&!tmp.equals("\r")&&(int)tmp.toCharArray()[0]!=65279){
					
				}*/
			}
			BRseed.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
/*		
  		for (Iterator<Entry<String, Double>> iter = this.SeedDic.entrySet().iterator();iter.hasNext();){
			System.out.println(iter.next().getKey());
		}
	*/	
		
		System.out.println(new Date()+"-----init the inverted table------------------------");
		
		this.GetInvertedtable(this.invertedtableDir);
		
		for (Iterator<Entry<String, Double>> iter = this.SeedDic.entrySet().iterator();iter.hasNext();){
			Entry<String, Double> entry = iter.next();
			
			System.out.print(entry.getKey()+"\t");
			System.out.print(this.invertedtable.containsKey(entry.getKey())+"\t");
			if (this.invertedtable.containsKey(entry.getKey()))
				System.out.print(this.invertedtable.get(entry.getKey()).size());
			System.out.print("\n");
		}
		
		
		System.out.println("size of word\t"+this.wordNum);
		System.out.println("size of weibo\t"+this.weiboNum);
			
/*		try {
			BufferedReader  BRinv= new BufferedReader(new UnicodeReader(new FileInputStream(this.invertedtableDir), "UTF-8"));
			String tmp ="";
			while((tmp=BRinv.readLine())!=null){
				String Key = new String(tmp.split("\\s+")[0]);
	//			System.out.println(Key+"\t"+Key.contains(tmp));
				List<Long> Value = this.getID(tmp);
	//			System.out.println(Key+"\t"+Value.size());
				if(Value.size()<1) {
					System.out.println(Key+"\t input error!");
					continue;
					}
				this.invertedtable.put(Key,new ArrayList<Long>());
				this.invertedtable.get(Key).addAll(Value);

				
			}
			BRinv.close();
			this.wordNum=this.CountWords();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		
		
		
	}
	


	public void Save2File(String Filename,boolean flag,int begin,int end){
		try {
			FileWriter FW = new FileWriter(Filename,flag);
			for (Iterator iter = this.PMIvalue.entrySet().iterator();iter.hasNext();){
				Map.Entry entry = (Map.Entry) iter.next();
				FW.write(entry.getKey()+":\t");
				List<Map.Entry<String,Double>> sortedlist = this.Sortedbydoublevalue((Map<String, Double>) entry.getValue());
				if (end-begin<1){
					FW.write(sortedlist.toString());
				}
				else {
					FW.write("["+sortedlist.get(begin));
					for (int i=begin+1;i<end;i++){
						FW.write(",\t"+sortedlist.get(i));
					}
					FW.write("]");
				}
				FW.write("\n");
			}
			FW.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void run(){
		System.out.println(new Date()+"-----begin Calculate the PMI value -------");
		
		this.GetUsedWeibo();
		System.out.println(new Date());
		for(Iterator iter = this.SeedDic.entrySet().iterator();iter.hasNext();){
			Map.Entry entry = (Map.Entry) iter.next();
			String seed = new String (entry.getKey().toString());
			
			
			if(!this.invertedtable.containsKey(seed)){
				System.out.println( "the Inverted table did not contain the seed word \t"+seed);
			}
			else{
			this.PMIvalue.put(seed,this.PMIValue(seed));
		}
		}
	}
	public void run2(){
		System.out.println(new Date()+"-----begin Calculate the PMI value -------");
		
		this.GetUsedWeibo();
		System.out.println(new Date());
		 
		List <String> seedlist = new ArrayList<String>();
		
		
		for(Iterator iter = this.SeedDic.entrySet().iterator();iter.hasNext();){
			Map.Entry entry = (Map.Entry) iter.next();
			seedlist.add(new String (entry.getKey().toString()));
		}	
		File fl = new File(this.SeeddicDir);
		String seedname = fl.getName();
			this.PMIvalue.put(seedname,this.PMIValue(seedlist));
		
	}
	
	
	private Map<String,Double> PMIValue (List<String> seedlist){
		Map<String ,Double> wordvalue = new Hashtable<String,Double>();
		List <Long> seedapp = new ArrayList<Long> ();
		for (String s:seedlist) {
			if(!this.invertedtable.containsKey(s)){
				System.out.println("the inverted table did not contain seed "+s);
				
			}else{
				seedapp.addAll(this.invertedtable.get(s));
			}
		}
		Set<String> tmp = new HashSet<String>();
		for(Long l:seedapp){
			tmp.addAll(this.weibo.get(l));
		}
		for(Iterator<String> iter = tmp.iterator();iter.hasNext();){
			String word = iter.next();
			wordvalue.put( word,pmifunciton(seedlist,word));
		}
		return wordvalue;
	}
	
	
	
	
	private Map<String ,Double> PMIValue(String seed){
		
		// p(x) is the frequence of weibo which contain the word x ;
		Map<String ,Double> wordvalue = new Hashtable<String ,Double>();
		
		List<Long> seedapp = this.invertedtable.get(seed);
		Set<String> tmp = new HashSet<String>();
	//	System.out.println(seed+"\t"+seedapp.size());
		for(Long l:seedapp){
			tmp.addAll(this.weibo.get(l));
		}
		System.out.println(new Date()+"\t"+seed+"\t"+tmp.size());
//		int cont = 0;
		for(Iterator<String> iter = tmp.iterator();iter.hasNext();){
			String word = iter.next();
			wordvalue.put( word,pmifunciton(seed,word));
/*			cont++;
			if (cont %1000==0)
				System.out.print("\t"+cont);*/
		}
/*		
		
		for(Iterator iter = this.invertedtable.entrySet().iterator();iter.hasNext();){
			
			Map.Entry entry = (Map.Entry) iter.next();
			String word= (String)entry.getKey();
			if(!seed.equals(word)){
				wordvalue.put(word, pmi2(seed,word));
			}
		
		}*/
		return wordvalue;
	}
	
	
	private double pmifunciton(List<String> seed ,String word ){
		if(this.Kind==1)
			return this.pmi1(seed, word);
		else if(this.Kind==2)
			return this.pmi2(seed, word);
		else if (this.Kind==3)
			return this.pmi3(seed, word);
		else
			return -10;
	}
	private double pmifunciton(String seed ,String word ){
		if(this.Kind==1)
			return this.pmi1(seed, word);
		else if(this.Kind==2)
			return this.pmi2(seed, word);
		else if (this.Kind==3)
			return this.pmi3(seed, word);
		else
			return -10;
	}
	
	private double pmi2(List<String> seed,String word){
		if(seed.contains(word))
			return 0;
		double joint =0.0;
		int cont= 0;
		List<Long> seedlist = new ArrayList<Long>();
		for (String s:seed){
			if(this.invertedtable.containsKey(s)){
			seedlist.addAll(this.invertedtable.get(s));
			}
		}
		List<Long> wordlist = this.invertedtable.get(word);
		for(Iterator<Long> iter = seedlist.iterator();iter.hasNext();){
			Long tmk=new Long(iter.next());
			if(wordlist.contains(tmk))
				cont++;
		}
		joint = (double)cont/(double)this.wordNum;
		double p_seed = (double)seedlist.size()/(double)this.wordNum;
		double p_word = (double)wordlist.size()/(double)this.wordNum;
		double pmi = Math.log(joint/(p_seed*p_word));
		return Math.log(joint/(p_seed*p_word))/Math.log(2);
	}
	private double pmi2(String seed ,String word){
		if(seed.equals(word)){
			return 0;
		}
		double joint =0.0;
		int cont=0;
	//	int wordnum= this.CountWords();
		List<Long> seedlist = this.invertedtable.get(seed);
		List<Long> wordlist = this.invertedtable.get(word);
		for(Iterator<Long> iter = seedlist.iterator();iter.hasNext();){
			Long tmk=new Long(iter.next());
			if(wordlist.contains(tmk))
				cont++;
		}
		joint = (double)cont/(double)this.wordNum;
		double p_seed = (double)seedlist.size()/(double)this.wordNum;
		double p_word = (double)wordlist.size()/(double)this.wordNum;
		return Math.log(joint/(p_seed*p_word))/Math.log(2);

	}
	private double  pmi3(List<String> seed,String word){
		if(seed.contains(word))
			return 0;
		double joint =0.0;
		int cont= 0;
		List<Long> seedlist = new ArrayList<Long>();
		for (String s:seed){
			if(this.invertedtable.containsKey(s)){
			seedlist.addAll(this.invertedtable.get(s));
			}
		}
		List<Long> wordlist = this.invertedtable.get(word);
		for(Iterator<Long> iter = seedlist.iterator();iter.hasNext();){
			Long tmk=new Long(iter.next());
			if(wordlist.contains(tmk))
				cont++;
		}
		joint = (double)cont/(double)this.wordNum;
		double p_seed = (double)seedlist.size()/(double)this.wordNum;
		double p_word = (double)wordlist.size()/(double)this.wordNum;
		double pmi = Math.log(joint/(p_seed*p_word));
		Set<Long> wordset = new HashSet<Long>();
		wordset .addAll(wordlist);
		double idf = Math.log(this.weiboNum/wordset.size());
		return pmi/idf;
	}
	private double pmi3(String seed ,String word){
		if(seed.equals(word)){
			return 0;
		}
		double joint =0.0;
		int cont=0;
	//	int wordnum= this.CountWords();
		List<Long> seedlist = this.invertedtable.get(seed);
		List<Long> wordlist = this.invertedtable.get(word);
		for(Iterator<Long> iter = seedlist.iterator();iter.hasNext();){
			Long tmk=new Long(iter.next());
			if(wordlist.contains(tmk))
				cont++;
		}
		joint = (double)cont/(double)this.wordNum;
		double p_seed = (double)seedlist.size()/(double)this.wordNum;
		double p_word = (double)wordlist.size()/(double)this.wordNum;
		double pmi = Math.log(joint/(p_seed*p_word));
		Set<Long> wordset = new HashSet<Long>();
		wordset .addAll(wordlist);
		double idf = Math.log(this.weiboNum/wordset.size());
		return pmi/idf;

	}
/*	private int CountWords(){
		int cont=0;
		for (Iterator<Entry<String, List<Long>>> iter = this.invertedtable.entrySet().iterator();iter.hasNext();){
			Entry<String, List<Long>> entry = iter.next();
			cont+=entry.getValue().size();
		}
		return cont;
	}*/
	
	private double pmi1(List<String> seed,String word){
		if(seed.contains(word))
			return 0;
		double joint =0.0;
		int cont=0;
	//	int wordnum= this.CountWords();

		Set<Long> seedSet=new HashSet();
		seedSet.addAll(this.invertedtable.get(seed));
		Set<Long> wordSet= new HashSet();
		wordSet.addAll(this.invertedtable.get(word));
		for(Iterator<Long> iter = seedSet.iterator();iter.hasNext();){
			Long tmk=new Long(iter.next());
			if(wordSet.contains(tmk))
				cont++;
		}
		joint = (double)cont/(double)this.wordNum;
		double p_seed = (double)seedSet.size()/(double)this.wordNum;
		double p_word = (double)wordSet.size()/(double)this.wordNum;
		return Math.log(joint/(p_seed*p_word))/Math.log(2);
	}
	private double pmi1(String seed,String word){
		if(seed.equals(word)){
			return 0;
		}
		double joint =0.0;
		int cont=0; 
		Set<Long> seedSet=new HashSet();

		seedSet.addAll(this.invertedtable.get(seed));
		Set<Long> wordSet= new HashSet();
		wordSet.addAll(this.invertedtable.get(word));


		for(Iterator<Long> iter = seedSet.iterator();iter.hasNext();){
			Long tmk=new Long(iter.next());
			if(wordSet.contains(tmk))
				cont++;
		}

		joint = (double)cont/(double)this.weiboNum;
		double p_seed = (double)seedSet.size()/(double)this.weiboNum;
		double p_word = (double)wordSet.size()/(double)this.weiboNum;
		return Math.log(joint/(p_seed*p_word))/Math.log(2);
		
	}
	private Map<String ,Double> PMIValue2(String seed){
		//p(x) is the frequence of word x appear in all words.
		Map<String ,Double> wordvalue = new Hashtable<String ,Double>();
		return wordvalue;
	}
	public List<Long> getWord(String key){
		return this.invertedtable.get(key);
		
	}
	private List<Long> getID(String line){
		List<Long> idlist= new ArrayList<Long>();
		Pattern p_MID=Pattern.compile("\\[(.*?)\\]");
		Matcher m_MID = p_MID.matcher(line);
		while(!m_MID.hitEnd() && m_MID.find()){
			
			String [] tmp = m_MID.group(1).replace(",","\t").split("\\s+");
			for (String s:tmp){
//				System.out.println(s);
				try{
				idlist.add(Long.valueOf(s));
				}catch(NumberFormatException nfe){
					System.out.println(nfe.getMessage());
				}
			}
		}
		return idlist;
	}
	private static class ValueComparator <TKey extends Object> implements Comparator<Map.Entry<TKey,Double>>{

		@Override
		public int compare(Map.Entry<TKey, Double> o1, Map.Entry<TKey, Double> o2) {
			// TODO Auto-generated method stub
			return o2.getValue().compareTo(o1.getValue());
		}
	}
	private List<Map.Entry<String,Double>> Sortedbydoublevalue (Map<String,Double> map){
		List<Map.Entry<String,Double>> sortlist= new ArrayList<Entry<String,Double>>();
		sortlist.addAll(map.entrySet());
		PMI.ValueComparator vcE= new ValueComparator();
		Collections.sort(sortlist,vcE);
		return sortlist;
	}
	
	private double TFIDFvalue (String word){
		double tf=0;
		double idf=0;
		List<Long> wordlist = this.invertedtable.get(word);
		Set<Long> wordset = new HashSet<Long>();
		wordset.addAll(wordlist);
		idf = Math.log(this.weiboNum/wordset.size())/Math.log(2.0);
		
		return tf*idf;
	}
	
	
	public static void main(String[]argvs){
		
  		for (String s:argvs){
			System.out.println(s);
		}
		
		if (argvs.length<5){
			System.out.println("Input Error");
			System.out.println("First is the input file dir");
			System.out.println("Second is the seed file dir");
			System.out.println("Third is the threshold value");
			System.out.println("Forth is the kind of the pmi funciotn");
			System.out.println("Fifth is the output file dir");

		}else{
			String InputFile = argvs[0];
			String Seed = argvs[1];
			int Thre = Integer.valueOf(String.valueOf(argvs[2]));
			int Kind = Integer.valueOf(String.valueOf(argvs[3]));
			PMI pmi = new PMI();
			pmi.setInvertedtableDir(InputFile);
			pmi.setSeeddicDir(Seed);
			pmi.setSpeechfile("./library/Pmispeech.txt");			
			pmi.setThreshold(Thre);
			pmi.setKind(Kind);
			pmi.initPMI();
		//	pmi.showInvertedtable();
		//	System.out.println("[泪]\t"+pmi.getWord("[泪]"));
			pmi.run2();
			pmi.Save2File(argvs[4], false,0,0);
		}
		
/*
		String InputFile = "./131015tablev2.gz";
		String Seed = "./data/EmotionSeed/happy";
		int Thre = Integer.valueOf("50");
		int Kind = Integer.valueOf("2");
		PMI pmi = new PMI();
		pmi.setInvertedtableDir(InputFile);
		pmi.setSeeddicDir(Seed);
		pmi.setSpeechfile("./library/Pmispeech.txt");			
		pmi.setThreshold(Thre);
		pmi.setKind(Kind);
		pmi.initPMI();
	//	pmi.showInvertedtable();
	//	System.out.println("[泪]\t"+pmi.getWord("[泪]"));
		pmi.run2();
		pmi.Save2File("./testpmi-0125-angry", false,0,0);
*/		
	}
}