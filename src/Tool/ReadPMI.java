package Tool;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ReadPMI{
	public static void readpmifile(String file){
		try {
			BufferedReader  BRinv= new BufferedReader(new UnicodeReader(new FileInputStream(file), "UTF-8"));
			String tmp ="";
			File fl = new File(file);
			String []tmk = fl.getName().replace(".","\t").replace(File.separator, "\t").split("\\s+");
			String dir = "";
			if (tmk[tmk.length-1].contains("txt")||tmk[tmk.length-1].contains("gz")){
				dir=tmk[tmk.length-2];
			}
			else{
				dir=tmk[tmk.length-1];
			}
			File f2 =new File(dir);
			f2.mkdir();
			while ((tmp=BRinv.readLine())!=null){
				String[] tmp2 = tmp.split(":\t");
				String seed = tmp2[0];
				String []value = tmp2[1].split(",");
				System.out.println(seed);
				FileWriter FW = new FileWriter(f2.getAbsolutePath()+File.separator+seed,false);
				for (String s:value){
					FW.write(s+"\n");
				}
				FW.close();
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void main(String[] argvs){
		for (String s:argvs){
			System.out.println(s);
		}
		ReadPMI.readpmifile("./2013-10-12neutral" +
				".txt");
	}
}