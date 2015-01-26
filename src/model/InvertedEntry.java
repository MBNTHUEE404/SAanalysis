package model;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InvertedEntry{
	private Word Word;
	private List<Long> Ids;
	public InvertedEntry(){
		this.Ids=new ArrayList<Long>();
		this.Word= new Word();
	}
	public InvertedEntry(String name ,String natural ,List<Long>ids){
		this.Word=new Word(name,natural);
		this.Ids=new ArrayList<Long>();
		this.Ids.addAll(ids);
	}
	public void copy(InvertedEntry ie){
//		System.out.println(ie.getWord().toString());
		this.Word=new Word(ie.getWord());
//		this.Word.setName(ie.getWord().getName());
//		this.Word.setNature(ie.getWord().getNature());
		this.Ids.clear();
		this.Ids.addAll(ie.getIds());
	}
	public void Addid(List<Long> id){
		this.Ids.addAll(id);
	}
	public void Addid(Long id){
		this.Ids.add(id);
	}
	public void SetInvertedEntry(String line){
		this.Word.setName(line.split("\\s+")[0]);
		this.Word.setNature(line.split("\\s+")[1]);
		this.Ids.clear();
		this.Ids.addAll(this.getID(line));
	}
	public Word getWord(){
		return this.Word;
	}
	public List<Long> getIds(){
		return this.Ids;
	}
	public String toString(){
		return this.Word.toString()+"\t"+this.Ids.toString();
	}
	public int compareTo(InvertedEntry ie){
		return this.Word.compareTo(ie.getWord());
	}
	private List<Long> getID(String line){
		List<Long> idlist= new ArrayList<Long>();
		Pattern p_MID=Pattern.compile("\\[(.*?)\\]");
		Matcher m_MID = p_MID.matcher(line);
		while(!m_MID.hitEnd() && m_MID.find()){
			
			String [] tmp = m_MID.group(1).replace(",","\t").split("\\s+");
			for (String s:tmp){
//				System.out.println(s);
				try{
				idlist.add(Long.valueOf(s));
				}catch(NumberFormatException nfe){
					System.out.println(nfe.getMessage());
				}
			}
		}
		return idlist;
	}
}