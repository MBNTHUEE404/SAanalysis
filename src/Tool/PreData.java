package Tool;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import model.KeyPair;
import model.KeyPairs;

public class PreData{
	private String Dir;
	private List<String> FileName;
	private Map<String,Integer> Word2Id;
	private Map<Integer, String> Id2Word;
	private Map<String ,Integer> WordInFiles; //Recoding the number of files that each words appeared.
	private String StopWordsFile;
	private List <String> StopWords;
	private KeyPairs<String ,Integer> WordFre;
	private KeyPairs<Integer,Integer> IdFre;
	private int WordNum;
	private double DataKindValue;
	public PreData() throws IOException
	{
		this.StopWordsFile = "StopWords.txt";
		this.Dir = "";
		this.FileName =new ArrayList<String>();
		this.Word2Id = new HashMap<String,Integer>();
		this.Id2Word = new HashMap<Integer, String>();
		this.StopWords = this.WashString(this.File2String(this.StopWordsFile), null);
		this.WordFre = new KeyPairs<String ,Integer>();
		this.IdFre = new KeyPairs<Integer,Integer>();
		this.WordInFiles = new HashMap<String,Integer>();
		this.WordNum=0;
		this.DataKindValue=0;
	}

	public PreData(String FileDir,String StopDir,double Value) throws IOException
	{
		this.StopWordsFile = StopDir;
		this.Dir = FileDir;
		this.FileName = this.GetFile(this.Dir);
		this.Word2Id = new HashMap<String,Integer>();
		this.Id2Word = new HashMap<Integer, String>();
		this.StopWords = this.WashString(this.File2String(this.StopWordsFile), null);
		this.WordFre = new KeyPairs<String ,Integer>();
		this.IdFre = new KeyPairs<Integer,Integer>();
		this.WordInFiles = new HashMap<String,Integer>();
		this.WordNum=0;
		this.DataKindValue=Value;
	}

	public static String File2String(String Filename) throws IOException{
		String Words= new String();
		FileReader FR =new FileReader(Filename);
		BufferedReader br =new BufferedReader(FR);
		String temp="";
		while((temp=br.readLine())!=null)
		{
			Words=Words+" "+temp;
		}
		return Words;
	}
	/*
	 *  Wash the Words in the File,
	 */
	public static List<String> WashString (String s,List<String > Stopwords)
    {
    	 List<String> ans=new Vector<String>();
		s = s.toLowerCase();
		s = s.replaceAll("[^A-Za-z]", "  ");
		s = s.replaceAll("\\s+", " ");
		String []temp =s.split("\\s+");
		if (Stopwords!=null)
		{
			for (int i =0;i<temp.length;i++)
			{
				if(!Stopwords.contains(temp[i])) 
				{ans.add(temp[i]);}
			}
		}
		else{
			for (int i=0;i<temp.length;i++){
				ans.add(temp[i]);
			}
		}
	    return ans;
	}

	/*
	 * return the All File Absolute File Name under the Dir
	*/

	private List<String> GetFile(String Dir){
		List<String> FList= new ArrayList<String>();
		File Fd = new File(Dir);
		if (Fd.isDirectory()){
			File [] FileList = Fd.listFiles();
			for (int i=0;i<FileList.length;i++){
				if(FileList[i].isDirectory()){
					List<String> SubList= GetFile(FileList[i].toString());
					FList.addAll(SubList);
				}
				else{
					FList.add(FileList[i].getAbsoluteFile().toString());
				}
			}
		}
		else{
			FList.add(Fd.getAbsolutePath().toString());
		}
		return FList;
	}
	public void SetData(List<String> Data){
		for (String ShortDoc:Data){
			List <String> TmpList = this .WashString(ShortDoc, this.StopWords);
			HashSet tmp = new HashSet<String>(TmpList);
			List<String> TList = new ArrayList<String>(tmp);
			for (String word:TList){
				if (this.WordInFiles.containsKey(word)){
					this.WordInFiles.put(word, Integer.valueOf(this.WordInFiles.get(word).intValue()+1));
				}
				else{ 
					this.WordInFiles.put(word, Integer.valueOf(1));
				}
			}
			for (String S:TmpList){
				if(this.Word2Id.containsKey(S)){
					this.WordFre.add(S,Integer.valueOf(this.WordFre.get(S).getValue()+1));
					this.IdFre.add(this.Word2Id.get(S),Integer.valueOf(this.WordFre.get(S).getValue()+1));
				}
				else{
					this.Word2Id.put(S, Integer.valueOf(this.WordFre.size()+1));
					this.Id2Word.put(Integer.valueOf(this.IdFre.size()+1), S);
					this.WordFre.add(S,Integer.valueOf(1));
					this.IdFre.add(Integer.valueOf(this.IdFre.size()+1),Integer.valueOf(1));			
				}
			}
			this.WordNum+=TmpList.size();
		}
	}
	
	public void GetWords(){
		List<String> TmpList = new ArrayList<String>();
    	for (int i=0;i<this.FileName.size();i++)
    	{
    		try {
				TmpList = this.WashString(this.File2String(this.FileName.get(i)), this.StopWords);
				HashSet tmp = new HashSet<String>(TmpList);
				List<String> TList = new ArrayList<String>(tmp);
				for (String s:TList){
					if(this.WordInFiles.containsKey(s)){
						this.WordInFiles.put(s, Integer.valueOf(this.WordInFiles.get(s).intValue()+1));
					}
					else 
						this.WordInFiles.put(s, Integer.valueOf(1));
				}
				for(int j=0;j<TmpList.size();j++)
				{
					
					if(this.Word2Id.containsKey(TmpList.get(j))){
						WordFre.add(TmpList.get(j),Integer.valueOf(WordFre.get(TmpList.get(j)).getValue()+1));
						IdFre.add(Word2Id.get(TmpList.get(j)),Integer.valueOf(WordFre.get(TmpList.get(j)).getValue()+1));
					}
					else{
						this.Word2Id.put(TmpList.get(j), Integer.valueOf(this.WordFre.size()+1));
						this.Id2Word.put(Integer.valueOf(this.IdFre.size()+1), TmpList.get(j));
						this.WordFre.add(TmpList.get(j),Integer.valueOf(1));
						this.IdFre.add(Integer.valueOf(this.IdFre.size()+1),Integer.valueOf(1));
					}

//					??????
				}
				this.WordNum+=TmpList.size();
    		} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    		
    	}
    }
	public int TotalWord(){
		return this.WordNum;
	}
	public List<KeyPair<String,Double>> GetWordValue(){
		List<KeyPair<String,Double>> Ans= new ArrayList<KeyPair<String,Double>>();
		List <KeyPair<String,Integer>> Tmp= this.WordFre.sortByDoubleValue(false);
		for (int i=0;i<Tmp.size();i++){
			Ans.add( new KeyPair<String,Double>(Tmp.get(i).getKey(),Double.valueOf(Tmp.get(i).getValue().doubleValue()/this.WordNum)));
		}
			return Ans;
	}
	public List<String> GetFileList(){
		return this.FileName;
	}
	public String GetString(){
		return this.Dir;
	}
	public Map<String ,Integer>GetWord2Id(){
		return this.Word2Id;
	}
	public Map<Integer,String>GetId2Word(){
		return this.Id2Word;
	}
	public KeyPairs<String ,Integer> GetWordFre(){
		return this.WordFre;
	}
	public KeyPairs<Integer,Integer> GetIdFre(){
		return this.IdFre;
	}
	/*
	 * if isAsceding is true Sort Ascending ,else Sort Descending
	 */
	public void SaveWordFre(String Filename,boolean isAscending) throws IOException{
		List<KeyPair<String,Integer>> TMp= this.WordFre.sortByDoubleValue(isAscending);

		FileWriter FW = new FileWriter(Filename,false);
		for (int i=0;i<TMp.size();i++){
			FW.write(TMp.get(i).toString()+"\n");
		}
		FW.close();
		
	}
	/*
	 * if isAsceding is true Sort Ascending ,else Sort Descending
	 */
	public void SaveIdFre(String Filename,boolean isAscending) throws IOException{
		List<KeyPair<Integer,Integer>> TMp= this.IdFre.sortByDoubleValue(isAscending);
		FileWriter FW = new FileWriter(Filename,isAscending);
		for (int i=0;i<TMp.size();i++){
			FW.write(TMp.get(i).toString()+"\n");
		}
		FW.close();
	}
	public void SaveId2Word(String Filename,boolean isAscending) throws IOException{
		FileWriter FW = new FileWriter(Filename,isAscending);
		for (int i=1;i<this.Word2Id.size()+1;i++){
			FW.write(String.valueOf(i).toString()+"\t"+this.Id2Word.get(Integer.valueOf(i))+"\n");
		}
		FW.close();
	}
	public void Save2NB(String Dir,boolean isAscending)throws IOException{
		FileWriter FW = new FileWriter (Dir+String.valueOf(this.DataKindValue), isAscending);
		List<KeyPair<String,Integer>> TMp = this.WordFre.sortByKey(true);
		for (KeyPair<String,Integer> kep:TMp)
		{
			FW.write(kep.getKey()+"\t"+kep.getValue().doubleValue()/(this.WordNum)+"\n");
		}
		FW.close();
	}
	public void Save2LibSvm(String Filename,boolean isAscending) throws IOException{
		FileWriter FW= new FileWriter(Filename,isAscending);
		for (String File:FileName){
			FW.write(this.DataKindValue+" ");			
			List<String>TmpList = this.WashString(this.File2String(File), this.StopWords);
			HashMap<String ,Integer> FileWordFre = new HashMap<String,Integer>();
			
			for (String s:TmpList){
				if(FileWordFre.containsKey(s)){
					int tvalue=FileWordFre.get(s);
					FileWordFre.put(s, Integer.valueOf(tvalue+1));
				}
				else
					FileWordFre.put(s, Integer.valueOf(1));					
			}
						
			KeyPairs<Double,Integer> Tp= new KeyPairs<Double,Integer>();
			
			for (String s:TmpList){
				int id=this.Word2Id.get(s).intValue();
				double TF = FileWordFre.get(s).doubleValue() /TmpList.size();
				double IDF = Math.log((double)this.FileName.size()/this.WordInFiles.get(s).doubleValue());
				Tp.add(Double.valueOf(TF*IDF),Integer.valueOf(id));
				}
			List<KeyPair<Double,Integer>>TTP=Tp.sortByDoubleValue(true);
			for (KeyPair<Double,Integer> cao:TTP){
				FW.write(cao.getValue().intValue()+":"+cao.getKey().doubleValue()+" ");
				
			}
			FW.write("\n");
			TmpList.clear();
		}
		FW.close();
	}
}