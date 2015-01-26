package Tool;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.ansj.domain.Term;
import org.ansj.splitWord.analysis.NlpAnalysis;
import org.ansj.util.FilterModifWord;


public class CutWords{
	private String StopWordsDir;
	private List<String> StopWords;
	private String UserDictionaryDir;
	private List<String> UserDic;
	private String SpeechDir;
	private List<String>Speechs;
	
	public CutWords(){
		this.StopWordsDir="";
		this.StopWords = new ArrayList<String>();
		this.UserDic=new ArrayList<String>();
		
		this.Speechs= new ArrayList<String>();
	}
	
	public String getStopWordsDir() {
		return StopWordsDir;
	}

	public void setStopWordsDir(String stopWordsDir) {
		StopWordsDir = stopWordsDir;
	}

	public String getUserDictionaryDir() {
		return UserDictionaryDir;
	}

	public void setUserDictionaryDir(String userDictionaryDir) {
		UserDictionaryDir = userDictionaryDir;
	}

	public String getSpeechDir() {
		return SpeechDir;
	}

	public void setSpeechDir(String speechDir) {
		SpeechDir = speechDir;
	}

	private int initStopWords(){
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new InputStreamReader(
					new FileInputStream(this.StopWordsDir), "UTF-8"));
			String line = reader.readLine();

			while ((line = reader.readLine()) != null) {
				this.StopWords.add(line);
			}
		} catch (Exception e) {
			System.out.println("创建停词表失败");
			e.printStackTrace();
		} finally {
			try {
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return this.StopWords.size();
	}
	private int initStopWordKinds(){
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new InputStreamReader(
					new FileInputStream(this.SpeechDir), "UTF-8"));
			String line = reader.readLine();

			while ((line = reader.readLine()) != null) {
				this.Speechs.add(line);
			}
		} catch (Exception e) {
			System.out.println("创建停词表失败");
			e.printStackTrace();
		} finally {
			try {
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return this.Speechs.size();
	}
	
	public void initANSJ(){
		System.out.println("Begin init stopwords and natures ...");
		this.initStopWords();
		this.initStopWordKinds();
		FilterModifWord.insertStopWords(this.StopWords);
		for (String s:this.Speechs){
			FilterModifWord.insertStopNatures(s);
		}
		System.out.println("init stopwords and natures success!!");
		
		
		
		
		
	}
	
	public  List<Term> SplitWords(String text){
		List<Term> words =FilterModifWord.modifResult(NlpAnalysis.parse(text));
		return words;
	}
	
	public static void main(String argvs[]){
		CutWords cw = new CutWords();
		cw.setSpeechDir("./Speech.txt");
		cw.setStopWordsDir("./Cstopword.txt");
		cw.setUserDictionaryDir("./library/default.dic");
		cw.initANSJ();
		String text="无论你多么爱他，多余的担心就是最差的礼物，不如给他祝福吧！ ——张德芳《遇见未知的自己》";
		List<Term> words = cw.SplitWords(text);
		for (Term t:words)
		System.out.println(t.getName()+"\t"+t.getNatureStr());
	}
	
}