package Algorithm;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import model.KeyPairs;

public class MyNativeBayes{
	private List<KeyPairs<String,Double>> Data;
	private List<String> Label;
	private List<Double> prior;
	public MyNativeBayes(){
		this.Data = new ArrayList<KeyPairs<String,Double>>();
		this.Label = new ArrayList<String>();
		this.prior = new ArrayList<Double>();
	}
	public void train(){
		
	}
	public void ReadFromPreData(String Dir) throws IOException{
		File F = new File(Dir);
		File[] Fl = F.listFiles();
		for (int i=0;i<Fl.length;i++){
			this.Data.add(i, new KeyPairs<String,Double>());
			this.Label.add(Fl[i].toString());
			FileReader FR = new FileReader(Fl[i]);
			BufferedReader BR = new BufferedReader(FR);
			String tmp ="";
			while((tmp=BR.readLine())!=null){
				String[] ts = tmp.split("\\s+");
				this.Data.get(i).add(ts[0],Double.valueOf(ts[1]));
			}
			FR.close();
			BR.close();
			this.prior.add(Double.valueOf(1.0));	
		}		
	}
	public String Test(List<String>Doc){
		double [] value = new double[Label.size()];
		for (int i=0;i<this.Label.size();i++)
		{
			value[i]=this.prior.get(i);
			for (String s:Doc){
				if (this.Data.get(i).containsKey(s)){
					value[i]=value[i]+this.Data.get(i).get(s).getValue().doubleValue();
				}
			}
			
		}
		return this.Label.get(this.findMax(value));
	//	return value;
	}
	
	private int findMax(double []tmp){
		int max=-1;
		double t=-1.0;
		for (int i=0;i<tmp.length;i++){
			if(t<tmp[i]){
				max=i;
				t=tmp[i];
			}
		}
		return max;
	}
}