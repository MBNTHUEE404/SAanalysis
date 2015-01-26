package Tool;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import model.Weibo;

public class GetWeibo{

	public static List<String> GetHashtag(String wb){
		List<String> HTList= new ArrayList<String>();
		Pattern p_label = Pattern.compile("#(.*?)#");
		Matcher m_label = p_label.matcher(wb);
		while(!m_label.hitEnd()&&m_label.find()){
			HTList.add(m_label.group(0));
		}
		return HTList;
	}
	public static String GetText(String wb){
		String text="";
		Pattern p_text= Pattern.compile("text=(.*?),");
		Matcher m_text= p_text.matcher(wb);
		while(!m_text.hitEnd() && m_text.find()){
			text=m_text.group(1);
		}
		return text;
	}
	public static String GetCleanText(String wb){
		String text = "";
		text=GetText(wb);
		Pattern p_url = Pattern.compile("http(.*?)\\]");
		Matcher m_url = p_url.matcher(text+"]");
		String URL= "";
		while(!m_url.hitEnd()&&m_url.find()){
			URL+="http"+m_url.group(1)+"\t";
		}
		return text;
	}
	public static List<String> GetFeature(String wb, String regular){
		List<String> FList= new ArrayList<String>();
		Pattern p_label = Pattern.compile(regular);
		Matcher m_label = p_label.matcher(wb);
		while(!m_label.hitEnd()&&m_label.find()){
			FList.add(m_label.group(0));
		}
		return FList;
	}
	public static List<Weibo> GetWeibofromFile(String Filename){
		List<Weibo> WList = new ArrayList<Weibo>();
		try{
		BufferedReader RB = new BufferedReader(new FileReader(Filename));
		String FileString= "";
		String tmp ="";
		byte[] tmpb=null;
		while((tmp= RB.readLine())!=null){
			//文件太大，直接对每行进行处理。断行的直接扔掉。
/*			tmpb=tmp.getBytes("UTF-8");
			FileString+=new String(tmpb,"UTF-8");*/
			FileString+=tmp;
		}		
		String [] Weibo = FileString.split("Status \\[");
		Weibo WBtmp=null;
		for (int i=0;i<Weibo.length-1;i++){
			WBtmp=cleanWeibo(Weibo[i]);
			if(WBtmp!=null)
			WList.add(WBtmp);
		}
		}catch(IOException e){
			System.out.println("File:\t"+Filename+"can not open!");
		}
		
		return WList;
	}
	
	
	public static Weibo cleanWeibo(String tmp){
		Weibo wb = new Weibo();
		String feature_user = "\\[id=";
		String feature_createDate="createdAt=";
		String feature_text="text=";
		String feature_source="source=Source";
		String feature_geo="geo=";
		String feature_retweeted_status="retweetedStatus=";
		String feature_repostsCount="repostsCount=";
		String feature_commentsCount="commentsCount=";
		String feature_mid ="mid="; 
		try{
			Pattern p_MID=Pattern.compile(feature_mid+"(.*?),");
			Matcher m_MID = p_MID.matcher(tmp);
			while(!m_MID.hitEnd()&&m_MID.find()){
				if(m_MID.group(1)==null||m_MID.group(1).isEmpty()){
					wb.setMid(-1);
				}
				else
					wb.setMid(Long.valueOf(m_MID.group(1)).longValue());
			}
			
			Pattern p_user= Pattern.compile(feature_user+"(.*?),");
			Matcher m_user= p_user.matcher(tmp);
			while(!m_user.hitEnd() && m_user.find()){				
				wb.setUser(m_user.group(1));				
			}
			
			Pattern p_createDate = Pattern.compile(feature_createDate+"(.*?),");
			Matcher m_createDate=p_createDate.matcher(tmp);
			while(!m_createDate.hitEnd() && m_createDate.find()){
				wb.setCreateDate(makeDate(m_createDate.group(1)));
			}
			
			Pattern p_text= Pattern.compile(feature_text+"(.*?),");
			Matcher m_text= p_text.matcher(tmp);
			while(!m_text.hitEnd() && m_text.find()){
				wb.setText(m_text.group(1));
			}
			Pattern p_source= Pattern.compile(feature_source+"(.*?)\\]");
			Matcher m_source= p_source.matcher(tmp);
			while(!m_source.hitEnd() && m_source.find()){
				wb.setSource(m_source.group(1)+"]");
			}
			Pattern p_geo = Pattern.compile(feature_geo+"(.*?),");
			Matcher m_geo = p_geo.matcher(tmp);
			while(!m_source.hitEnd() && m_source.find()){
				wb.setGeo(m_geo.group(1));
			}
			Pattern p_emotion = Pattern.compile("\\[(.*?)\\]");
			Matcher m_emotion = p_emotion.matcher(wb.getText());
			String Emotions="";
			while(!m_emotion.hitEnd()&&m_emotion.find()){
				Emotions+=m_emotion.group(0);
			}
			wb.setEmotions(Emotions);
			Pattern p_label = Pattern.compile("#(.*?)#");
			Matcher m_label = p_label.matcher(wb.getText());
			String Hashtage = "";
			while(!m_label.hitEnd()&&m_label.find()){
				Hashtage+=m_label.group(0);
			}
			wb.setLable(Hashtage);
			Pattern p_url = Pattern.compile("http(.*?)\\]");
			Matcher m_url = p_url.matcher(wb.getText()+"]");
			String URL= "";
			while(!m_url.hitEnd()&&m_url.find()){
				URL+="http"+m_url.group(1)+"\t";
				
			}
			wb.setUrl(URL); 
			Pattern p_retweet = Pattern.compile(feature_retweeted_status+"(.*?),");
			Matcher m_retweet = p_retweet.matcher(tmp);
			while(!m_retweet.hitEnd()&& m_retweet.find()){
				wb.setRetweeted_status(m_retweet.group(1));
			}
			Pattern p_rcount=Pattern.compile(feature_repostsCount+"(.*?),");
			Matcher m_rcount = p_rcount.matcher(tmp);
			while(!m_rcount.hitEnd()&&m_rcount.find()){
				if(m_rcount.group(1)==null||m_rcount.group(1).isEmpty()){
					wb.setReposts_count(-1);
				}
				else
					wb.setReposts_count(Integer.valueOf(m_rcount.group(1)).intValue());
			}
			Pattern p_comcount=Pattern.compile(feature_commentsCount+"(.*?),");
			Matcher m_comcount = p_comcount.matcher(tmp);
			while(!m_comcount.hitEnd()&&m_comcount.find()){
				if(m_comcount.group(1)==null||m_comcount.group(1).isEmpty()){
					wb.setComments_count(-1);
				}
				else
					wb.setComments_count(Integer.valueOf(m_comcount.group(1)).intValue());
			}
			
			
			
		}catch (NullPointerException e){
			System.out.println(tmp+"\t has null pointer !");
			return null;
		}catch(NumberFormatException nfe){
			System.out.println(nfe+tmp);
		}
		finally{
			//System.out.println(wb.toString());
			
		}
		return wb;
	}
	//Tue Oct 01 10:05:13 CST 2013
	@SuppressWarnings("deprecation")
	private static Date makeDate(String tmp){
		Date date  = new Date();
		String []da= tmp.split("\\s+");
		if(da[1].equals("Jan")) date.setMonth(0);
		if(da[1].equals("Feb")) date.setMonth(1);
		if(da[1].equals("Mar")) date.setMonth(2);
		if(da[1].equals("Apr")) date.setMonth(3);
		if(da[1].equals("May")) date.setMonth(4);
		if(da[1].equals("Jun")) date.setMonth(5);
		if(da[1].equals("Jul")) date.setMonth(6);
		if(da[1].equals("Aug")) date.setMonth(7);
		if(da[1].equals("Sept")) date.setMonth(8);
		if(da[1].equals("Oct")) date.setMonth(9);
		if(da[1].equals("Nov")) date.setMonth(10);
		if(da[1].equals("Dec")) date.setMonth(11);
		date.setDate(Integer.valueOf(da[2]).intValue());
		date.setYear(Integer.valueOf(da[5]).intValue()-1900);
		String [] time = da[3].split(":");
		date.setHours(Integer.valueOf(time[0]).intValue());
		date.setMinutes(Integer.valueOf(time[1]).intValue());
		date.setSeconds(Integer.valueOf(time[2]).intValue());
		return date;
	}
	 
}