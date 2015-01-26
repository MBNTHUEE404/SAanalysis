package Tool;


import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GetFileName{
	public static List<String> GetFileName(String FileDir){
		List<String> FileList= new ArrayList<String>();
		File file = new File(FileDir);
		if (file.isFile()){
			FileList.add(file.getAbsolutePath());
		}
		else{
			File [] array = file.listFiles();
			for (int i=0;i<array.length;i++){
				if(array[i].isFile()){
					FileList.add(array[i].getAbsolutePath());
				}else if(array[i].isDirectory()){
					FileList.addAll(GetFileName(array[i].getPath()));
				}
			}
		}
		Collections.sort(FileList);
		return FileList;
	}
	
	
}