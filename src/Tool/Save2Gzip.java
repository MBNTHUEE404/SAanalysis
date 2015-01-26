package Tool;


import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;


public class Save2Gzip{
	private List<String> FileList;
	private String Dir;
	public void SetDir(String dir){
		this.Dir= dir;
	}
	public Save2Gzip(){
		FileList= new ArrayList<String>();
		Dir="";
	}
	public  void Clean2TXT(){
		
		FileList = GetFileName.GetFileName(this.Dir);
		for(String file:FileList){
			System.out.println(new Date()+"\t"+file);
			WeiboList(file);
		}
	}
	private void WeiboList(String Filename){
		//List<String> wbList= new ArrayList<String>();
		String []tmp2 = Filename.split("/");
		
		String[] tmp1= tmp2[tmp2.length-1].split("-");
		String WFileName=tmp1[0]+"-"+tmp1[1]+"-"+tmp1[2]+".txt";
		try{
			BufferedReader BR= new BufferedReader(new FileReader(Filename));
			String FileString= "";
			String tmp ="";
	//		byte[] tmpb=null;
			while((tmp= BR.readLine())!=null){
				//文件太大，直接对每行进行处理。断行的直接扔掉。
	/*			tmpb=tmp.getBytes("UTF-8");
				FileString+=new String(tmpb,"UTF-8");*/
				FileString+=tmp;
			}	
			String [] Weibo = FileString.split("Status \\[");
			FileWriter FW = new FileWriter(WFileName,true);
			for(int i =0;i<Weibo.length-1;i++){
		//		wbList.add(s);
				FW.write(Weibo[i]+"\n");
			}
		}catch(IOException ioe){
			System.out.println(ioe.getMessage());
			System.out.println("IOExpection Error"+ioe.toString());
		}

	//	return wbList;
	}

	public static void ReadGIZP(String Filename){
		try {
			FileInputStream fin = new FileInputStream(Filename);
			GZIPInputStream gzis = new GZIPInputStream(fin);
			InputStreamReader xover = new InputStreamReader(gzis);
			BufferedReader br = new BufferedReader(xover);
			String line="";
			while((line=br.readLine())!=null){
				System.out.println(line);
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
	
	public static void GzipFile(String source_filepath, String destinaton_zip_filepath,int BUFFER) {

		byte[] buffer = new byte[BUFFER];

		try {
			FileOutputStream fileOutputStream = new FileOutputStream(destinaton_zip_filepath);
			GZIPOutputStream gzipOuputStream = new GZIPOutputStream(fileOutputStream);
			FileInputStream fileInput = new FileInputStream(source_filepath);
			int bytes_read;
			while ((bytes_read = fileInput.read(buffer)) > 0) {
				gzipOuputStream.write(buffer, 0, bytes_read);
			}
			fileInput.close();
			gzipOuputStream.flush();
			gzipOuputStream.finish();
			gzipOuputStream.close();
			System.out.println("The file was compressed successfully!");

		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	public static void unGzipFile(String compressedFile, String decompressedFile,int BUFFER) {
		byte[] buffer = new byte[BUFFER];
		try {
			FileInputStream fileIn = new FileInputStream(compressedFile);
			GZIPInputStream gZIPInputStream = new GZIPInputStream(fileIn);
			FileOutputStream fileOutputStream = new FileOutputStream(decompressedFile);
			int bytes_read;
			while ((bytes_read = gZIPInputStream.read(buffer)) > 0) {
				fileOutputStream.write(buffer, 0, bytes_read);
			}
			
			gZIPInputStream.close();
			fileOutputStream.close();
			System.out.println("The file was decompressed successfully!");
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	
}