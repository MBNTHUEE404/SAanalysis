package Tool;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.GZIPInputStream;

import model.InvertedEntry;

public class MergeInvertedtable{
	private List<String> FileList;
	private String FileDir;
	private String OutputFile;
	public static void Merge(List<String> InputFileName,String OutputFile) throws FileNotFoundException, IOException{
		List<BufferedReader> ReadFileList = new ArrayList<BufferedReader>();
		for (String file :InputFileName){
			ReadFileList.add(new BufferedReader(new InputStreamReader(new GZIPInputStream (new FileInputStream(file)))));
//			ReadFileList.add(new BufferedReader(new FileReader(file)));
		}
		List <Boolean> flag = new ArrayList<Boolean>();
		
		List<InvertedEntry> tmpEntry = new ArrayList<InvertedEntry>();

		
		FileWriter FW = new FileWriter(OutputFile,false);
		int cont = InputFileName.size();
		String tmp ="";
		for (int i =0;i<cont;i++){
//			tmp = ReadFileList.get(i).readLine();
			flag.add(true);
		}
		
		
		List<Integer> min =new ArrayList<Integer>();
		for (BufferedReader br :ReadFileList){
			InvertedEntry ie = new InvertedEntry();
			ie.SetInvertedEntry(br.readLine());
			tmpEntry.add(ie);
		}
//		FileWriter LOG = new FileWriter ("Loger",false);
		while (cont>0){
	//		System.out.println(cont);
/*			for(InvertedEntry ie :tmpEntry){
				if(ie!=null)
					LOG.write(ie.getWord().getName()+"\t");
			}
			LOG.write("\n");*/
			min = FindMax(tmpEntry);
			InvertedEntry a=new InvertedEntry ();
			a.copy(tmpEntry.get(min.get(0)));
			for (int i=1;i<min.size();i++){
				a.Addid(tmpEntry.get(min.get(i)).getIds());
			}
			FW.write(a.toString()+"\n");
			
			for(int i=0;i<min.size();i++){
				tmp =ReadFileList.get(min.get(i)).readLine();

				if(tmp!=null){
					if (tmp.contains("?")){
						while (tmp.contains("?"))
						tmp=ReadFileList.get(min.get(i)).readLine();
					}
					else{
						InvertedEntry b = new InvertedEntry();
						b.SetInvertedEntry(tmp);
						tmpEntry.set(min.get(i),b);
					}
				}
				else{
					flag.set(min.get(i), false);
					tmpEntry.set(min.get(i), null);
					cont--;
				}
				
			}
			
			
		}
		
		
		
		
		FW.close();
//		LOG.close();
		for (BufferedReader BR :ReadFileList){
			BR.close();
		}
	}
	private static List<Integer> FindMax (List<InvertedEntry> tmkie){
		List<Integer> ans = new ArrayList<Integer>();
		InvertedEntry tmp = new InvertedEntry();
		for (int i=0;i<tmkie.size();i++){
			if(tmkie.get(i)!=null)
				tmp.copy(tmkie.get(i));
				ans.add(i);
				break;
		}

		for (int i=1;i<tmkie.size();i++){	if(tmkie.get(i)!=null){
				if (tmp.compareTo(tmkie.get(i))<0){
					tmp.copy(tmkie.get(i));
					ans.clear();
					ans.add(i);
				}else if(tmp.compareTo(tmkie.get(i))==0){
					ans.add(i);
				}else
					continue;
			}
		}
		return ans;
	}
	
	public static void main(String [] argvs) throws IOException{
		for (String s:argvs){
			System.out.println(s);
		}
		
		List<String> inputfile = new ArrayList<String>();
		
		BufferedReader br= new BufferedReader (new FileReader(argvs[0]));
		String tmp ="";
		while ((tmp =br.readLine())!=null){
			inputfile.add(tmp);
		}
		MergeInvertedtable.Merge(inputfile, argvs[1]);
		
//		System.out.println(String.valueOf("??").compareTo("好"));
/*		inputfile.add("./131015tablev2.gz");
		inputfile.add("./131016tablev2.gz");
		MergeInvertedtable.Merge(inputfile,"./testmerge.txt");*/
	}
}