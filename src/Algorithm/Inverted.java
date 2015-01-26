package Algorithm;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.GZIPInputStream;

import model.Word;

import org.ansj.domain.Term;
import Tool.CutWords;
import Tool.GetFileName;
import Tool.Save2Gzip;
import Tool.Sorted2fileByKey;

public class Inverted{
	private Map<Word ,List<Long>> InvertedList;
	private String FileDir;
	private List<String> FileList;
	
	
	public Inverted(){
		this.InvertedList= new Hashtable<Word ,List<Long>>();
		this.FileList=new ArrayList<String>();
	}
	public String getFileDir() {
		return FileDir;
	}
	public void setFileDir(String fileDir) {
		FileDir = fileDir;
	}
	private void GetFileList(){
		FileList.addAll(GetFileName.GetFileName(this.FileDir));
	}
	private Long GetWeiboId(String wb){
		Long id =Long.valueOf(-1);
		Pattern p_MID=Pattern.compile("mid="+"(.*?),");
		Matcher m_MID = p_MID.matcher(wb);
		while(!m_MID.hitEnd()&&m_MID.find()){
			try{
			if(m_MID.group(1)==null||m_MID.group(1).isEmpty()){
				id=Long.valueOf(-1);
			}
			else
				id=Long.valueOf(m_MID.group(1)).longValue();
			}catch(NumberFormatException nfe){
				System.out.println(nfe.getMessage());
				System.out.println(wb);
				id =Long.valueOf(-1);
			}
		}
		return id;
		
	}
	public void run(){
		CutWords cw = new CutWords();
		cw.setSpeechDir("./Speech.txt");
		cw.setStopWordsDir("./Cstopword.txt");
		cw.setUserDictionaryDir("./library/default.dic");
		cw.initANSJ();
		List<Term> words=new ArrayList<Term>();
		for (String file:this.FileList){
			try {
				FileInputStream fin = new FileInputStream(file);
				GZIPInputStream gzis = new GZIPInputStream(fin);
				InputStreamReader xover = new InputStreamReader(gzis);
				BufferedReader br = new BufferedReader(xover);
				String line="";
				Long id =null;
				int cont =0;
				while((line=br.readLine())!=null){
					if(cont%10000==0) 
						System.out.println(cont);
					id = this.GetWeiboId(line);
					words.addAll(cw.SplitWords(Tool.GetWeibo.GetText(line)));
					for (Term term:words){
						Word word = new Word(term.getName(),term.getNatureStr());
						if(this.InvertedList.containsKey(word)){
							this.InvertedList.get(word).add(id);
						}
						else{
							this.InvertedList.put(word, new ArrayList<Long>());
							this.InvertedList.get(word).add(id);
						}
						word=null;
					}
					words.clear();
					cont++;
				}
				
				
				
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
	}
	
	public void Save2File(String Filename,Boolean kind){
		Sorted2fileByKey.Save2file2 (Filename, this.InvertedList);
		Save2Gzip.GzipFile(Filename, Filename+".gz",1024);
		File tmpfile= new File(Filename);
		tmpfile.delete();
		/*Set<Long> tmp = new HashSet<Long>();
		try {
			FileWriter FW = new FileWriter(Filename,kind);
			Iterator iter = this.InvertedList.entrySet().iterator();
			while (iter.hasNext()) {
				Map.Entry entry = (Map.Entry) iter.next();
				String key = (String) entry.getKey();
				List<Long> val = (List<Long>) entry.getValue();
				tmp.addAll(val);
				FW.write(key+"\t"+val.size()+"\t"+tmp.size());
				tmp.clear();
				for (Long s:val){
					FW.write("\t"+s);
				}
				FW.write("\n");
			}
			FW.close();	
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			}*/
	}
	public static void main(String []argvs){
		for (String s:argvs){
			System.out.println(s);
		}
		
		Inverted inv = new Inverted();
		inv.setFileDir(argvs[0]);
		inv.GetFileList();
		inv.run();
		inv.Save2File(argvs[1], false);
	}
}