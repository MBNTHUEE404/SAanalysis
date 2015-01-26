package model;
import java.util.ArrayList;
import java.util.List;

public class MyTweet{
	private List<String> Rt;
	private String Text;
	private List<String> URL;
	private List<String> Label;
	public MyTweet(){
		this.Rt = new ArrayList<String>();
		this.Text = "";
		this.URL = new ArrayList<String>();
		this.Label = new ArrayList<String>();
	}
	public MyTweet(String S){
		this.Rt = new ArrayList<String>();
		this.Text = "";
		this.URL = new ArrayList<String>();
		this.Label = new ArrayList<String>();
		String[] tmp=S.split("\\s+");
		for (String s:tmp){
			if(s.contains("@")){
				this.Rt.add(s);
			}
			else if(s.contains("http")){
				this.URL.add(s);
			}
			else if(s.contains("#")){
				this.Label.add(s);
			}
			else{
				this.Text=this.Text+" "+s;
			}
		}
	}
	public void SetTweet(String S){
		String[] tmp=S.split("\\s+");
		for (String s:tmp){
			if(s.contains("@")){
				this.Rt.add(s);
			}
			else if(s.contains("http")){
				this.URL.add(s);
			}
			else if(s.contains("#")){
				this.Label.add(s);
			}
			else{
				this.Text=this.Text+" "+s;
			}
		}
	}
	public List<String> GetRt(){
		return this.Rt;
	}
	public String GetTest(){
		return this.Text;
	}
	public List<String> GetURL(){
		return this.URL;
	}
	public List<String> GetLabel(){
		return this.Label;
	}
	public String toString(){
		String s=null;
		s="Test:\t"+this.Text+"\tRT\t"+this.Rt.toString()+"\tURL\t"+this.URL.toString()+"\tLabel\t"+this.Label.toString();
		return s;
	}
}