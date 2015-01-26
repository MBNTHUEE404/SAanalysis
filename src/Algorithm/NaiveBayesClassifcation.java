package Algorithm;



import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Vector;
// the jar for chinese language analysiss

public class NaiveBayesClassifcation{
	private String label=null;
	private long trainTime=0;
	public String[] labelsName=null;
	public Vector<Label> labels=null;
	public Set<String> vocabulary=new HashSet<String>();
	public String trainPath=null;
	public String testPath=null;
	public String AnsPath=null;
	// if the input is Chinese the language is true, if the input is English the value is false
	public boolean language=false;
	
	public int findMax(double[] values){
		double max=values[0];
		int mark=0;
		for(int i=0;i<values.length;i++){
			if(values[i]>max){
				max=values[i];
				mark=i;
			}
		}
		return mark;
	}
	public String[] sort(String[] pData, int left, int right){
		String middle,strTemp;
		int i = left;
		int j = right;
		middle = pData[(left+right)/2];
		do{
			while((pData[i].compareTo(middle)<0) && (i<right))
				i++;
			while((pData[j].compareTo(middle)>0) && (j>left))
				j--;
			if(i<=j){
				strTemp = pData[i];
				pData[i] = pData[j];
				pData[j] = strTemp;
				i++;
				j--;
			}
		}while(i<j);	
		if(left<j)
			sort(pData,left,j); 
		if(right>i)
			sort(pData,i,right); 
		return pData;
	}
	
	private ArrayList<String>  Getstopwords(String Filename) throws IOException
	{
		ArrayList<String> Stopwords= new ArrayList<String>();
		FileReader FR =new FileReader(Filename);
		BufferedReader br =new BufferedReader(FR);
		String temp="";
		while((temp=br.readLine())!=null)
		{
			Stopwords.add(temp);
		}
		return Stopwords;
	}
	private Vector<String> WashString (String s,ArrayList<String > Stopwords)
    {
    	 Vector<String> ans=new Vector<String>();
		s = s.toLowerCase();
		s = s.replaceAll("[^A-Za-z]", "  ");
		s = s.replaceAll("\\s+", " ");
		String []temp =s.split("\\s+");

			for (int i =0;i<temp.length;i++)
			{
				if(!Stopwords.contains(temp[i])) 
				{ans.add(temp[i]);}
			}
	    	return ans;
	}
    


	 Vector<String> readFile(String fileName,boolean language) throws IOException, FileNotFoundException{
		File f=new File(fileName);
		Vector<String> v=new Vector<String>();
		if(language ==true)
		{
			/*InputStreamReader isr=new InputStreamReader(new FileInputStream(f),"GBK");
			char[] cbuf=new char[(int) f.length()];
			isr.read(cbuf);
			Analyzer analyzer=new MMAnalyzer();
			TokenStream tokens=analyzer.tokenStream("Contents", new StringReader(new String(cbuf)));

			
			boolean hasnext= tokens.incrementToken();  
        	while (hasnext) {  
            	CharTermAttribute ta = tokens.getAttribute(CharTermAttribute.class);  
            	v.add(ta.toString());  
            	hasnext = tokens.incrementToken();  
        	}  */
		}
		else
		{
			FileReader Fr=new FileReader(fileName);
			BufferedReader BR= new BufferedReader(Fr);
			String temp="";
			String fi="";
			while((temp=BR.readLine())!=null) fi+=temp;
			ArrayList<String> stopwords=Getstopwords("./stopwords.txt");
			v=WashString(fi,stopwords);
		}
		return v;
	 }
	public void setTrainPath(String folderPat){
		this.trainPath=folderPat;
	}
	public void setTestPath(String testPat){
		this.testPath=testPat;
	}
	public void setAnsPath(String ansPat)
	{
		this.AnsPath=ansPat;
	}
	public void setLanguage(boolean lang)
	{
		this.language=lang;
	}
	public void train() {
		
		
		long startTime=System.currentTimeMillis();
		File folder=new File(trainPath);
		labelsName=folder.list();
		labels=new Vector<Label>();
		for(int i=0;i<labelsName.length;i++){
			labels.add(new Label());  
			File subFolder=new File(trainPath+"\\"+labelsName[i]);
			String[] files=subFolder.list();
			System.out.println("Processing:"+labelsName[i]);
			Vector<String> v=new Vector<String>();
			for(int j=0;j<files.length;j++){
				//System.out.print(files[j]+" ");
				try {
					v.addAll(readFile(trainPath+"\\"+labelsName[i]+"\\"+files[j],this.language));
				
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			// put all vocabulary into the set ��
			//to have the size of the vocabulary
			vocabulary.addAll(v);
			//sort all the vocabulary in the file to statistic the infomation of each vocabulary.
			String[] allWords=new String[v.size()];
			for(int j=0;j<v.size();j++)
				allWords[j]=v.elementAt(j);
			sort(allWords,0,v.size()-1);
			//statistic the vocabulary
			String previous=allWords[0];
			double count=1;
			Map<String,WordItem> m=new HashMap<String, WordItem>();
			for(int j=1;j<allWords.length;j++){
				if(allWords[j].equals(previous))
					count++;
				else{
					m.put(previous, new WordItem(count));
					previous=allWords[j];
					count=1;
				}
			}
			labels.elementAt(i).set(m, v.size(),files.length);
			long endTime=System.currentTimeMillis();
			trainTime=endTime-startTime;
		}
		// calculate the function of each vocabulary
		for(int i=0;i<labels.size();i++){
			Iterator iter=labels.elementAt(i).m.entrySet().iterator();
			while(iter.hasNext()){
				Map.Entry<String, WordItem> entry=(Map.Entry<String, WordItem>)iter.next();
				
				WordItem item=entry.getValue();
				
				item.setFrequency(Math.log10(item.count+1)/(labels.elementAt(i).wordCount+vocabulary.size()));
			}
		}
	}
	//	static void process(String folderPath) throws IOException{
	//		
	//	}
	public void test() throws IOException{
		
			Vector<String> v=null;
			File dir=new File(this.testPath);
			File[] testfile= dir.listFiles();
			FileWriter FW= new FileWriter(this.AnsPath,false);
			for (int k=0;k<testfile.length;k++)
			{
				try {

					v = readFile(testfile[k].toString(),this.language);
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				double values[]=new double[labelsName.length];
				for(int i=0;i<labels.size();i++){
					double tempValue=labels.elementAt(i).documentCount;
					for(int j=0;j<v.size();j++){
						if(labels.elementAt(i).m.containsKey(v.elementAt(j))){
							tempValue+=labels.elementAt(i).m.get(v.elementAt(j)).frequency;
						}
						else{
						
						
							tempValue+=Math.log10(1/(double)(labels.elementAt(i).wordCount+vocabulary.size()));
					
						}
					}
					values[i]=tempValue;
				}

				int maxIndex=findMax(values);
			
				System.out.println(testPath+"\\"+testfile[k].getName()+" belongs to "+labelsName[maxIndex]);
				if (this.AnsPath==null)
					AnsPath="./Ans.txt";
				
				FW.write(testPath+"\\"+testfile[k].getName()+" belongs to "+labelsName[maxIndex]+"\n");
			
				label=labelsName[maxIndex];
			}
			FW.close();
		}
		

	public String getLabelName(){
		return label;
	}
	public long getTrainingTime(){
		return trainTime;
	}
}
class Label{
	Map<String,WordItem> m=new HashMap<String,WordItem>();
	double wordCount;
	double documentCount;
	public Label() {
		this.m=null;
		this.wordCount=-1;
		this.documentCount=-1;
	}
	public void set(Map<String,WordItem> m,double wordCount,double documentCount) {
		this.m=m;
		this.wordCount=wordCount;
		this.documentCount=documentCount;
	}
}
class WordItem{
	double count;
	double frequency;
	public WordItem(double count) {
		this.count=count;
		this.frequency=-1;
	}
	public void setFrequency(double frequency){
		this.frequency=frequency;
	}
}

