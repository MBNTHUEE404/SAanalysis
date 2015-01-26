package Statistics;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import Tool.GetFileName;
import Tool.Sorted2filebyInteger;

public class CountEmotions{
	private Map<String ,Integer> Emotions;
	private List<String> FileList;
	private String FileDir;
	public CountEmotions(){
		this.Emotions = new Hashtable<String,Integer>();
		this.FileList= new ArrayList<String>();
	}
	public void setDir(String filedir){
		this.FileDir= filedir;
	}
	public void run(){
		this.FileList.addAll(GetFileName.GetFileName(this.FileDir));
		BufferedReader BR= null;
		String tmp="";
		String tKey;
		Integer tValue;
		for (String s:FileList){
			try {
				BR = new BufferedReader(new FileReader(s));
				while((tmp=BR.readLine())!=null){
					tKey= tmp.split("\\s+")[0];
					tValue= Integer.valueOf(tmp.split("\\s+")[1]);
					if(this.Emotions.containsKey(tKey)){
						this.Emotions.put(tKey, this.Emotions.get(tKey)+tValue);
					}
					else{
						this.Emotions.put(tKey, tValue);
					}
				}
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		Sorted2filebyInteger.Save2file("EomtionCount.txt", this.Emotions);
	}
	
	public static void main(String[] argvs){
		CountEmotions ce= new CountEmotions();
		ce.setDir("X:/workspaceForMyeclipse/SAPAPER/DataClean/Analysis/Emotions/");
		ce.run();
	}
}