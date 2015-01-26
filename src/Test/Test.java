package Test;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Tool.PreData;

import Algorithm.MyNativeBayes;

public class Test{
	public static void main(String [] argv) throws IOException, InterruptedException{
		Test.test4HashMap();
	}
	private void TestAlg() throws IOException{
		String TrainDatabas = "data/TrainData/baseball";
		String TrainDatahockey = "data/TrainData/hockey";
		PreData DP = new PreData(TrainDatabas,"StopWords.txt",1);
		DP.GetWords();
		DP.Save2LibSvm("D2SVM", false);
		DP.Save2NB("D2NB/baseball", false);
		PreData DPH = new PreData(TrainDatahockey,"StopWords.txt",-1);
		DPH.GetWords();
		DPH.Save2LibSvm("D2SVM", true);
		DPH.Save2NB("D2NB/hockey", false);
	}
	
	public static void Test4something() throws IOException{
		String TestData = "data/TestData";
		File TestF = new File (TestData);
		File[] Fl = TestF.listFiles();
		MyNativeBayes MNB = new MyNativeBayes();
		int right=0;
		int wrong =0;
		MNB.ReadFromPreData("D2NB");
		for (File f:Fl){
			String tf = PreData.File2String(f.getAbsolutePath());
			tf.toLowerCase();
			tf.replaceAll("[^A-Za-z]", "  ");
			tf.replaceAll("\\s+", " ");
			String []tmp=tf.split("\\s+");
			List<String> T_T = new ArrayList<String>();
			for (String t:tmp){
				T_T.add(t);
			}
			String ANS = MNB.Test(T_T);
//			System.out.println(String.valueOf(ANS[0]).toString()+"\t"+String.valueOf(ANS[1]).toString()+"\t"+f.getName());
			System.out.println(ANS+"   "+f.getName());
			if((ANS.contains("baseball")&&Double.valueOf(f.getName()).intValue()>90000)||
					ANS.contains("hockey")&&Double.valueOf(f.getName()).intValue()<90000)
				right++;
			else
				wrong++;
		}
		System.out.println("right:\t"+right);
		System.out.println("wrong:\t"+wrong);
	}
	public static void test4HashMap(){
		Map<String,List<Integer>> testMapList = new HashMap<String,List<Integer>>();
		testMapList.put("test", new ArrayList<Integer>());
		for (int i=0;i<10;i++){
			testMapList.get("test").add(i);
		}
		System.out.println(testMapList.get("test"));
	}
}


