package model;

import java.util.Date;

public class Weibo{
	private String user;
	private Date createDate;
	private String text;
	private String source;
	private String geo;
	private String retweeted_status;
	private int reposts_count;
	private int comments_count;
	private long mid;
	private String emotions;
	
	public String getEmotions() {
		return emotions;
	}
	public void setEmotions(String emotions) {
		emotions = emotions;
	}
	public long getMid() {
		return mid;
	}
	public void setMid(long mid) {
		this.mid = mid;
	}
	private String lable;
	private String url;
	public String toString(){
		String wb="";
		wb+="MID:\t"+this.mid;
		if (this.user!=null)
			wb+="\tUSER:\t"+this.user;
		if (this.createDate!=null)
			wb+="\tCREATETIME\t"+this.createDate.toString();
		if(this.text!=null)
			wb+="\tCONTENT:\t"+this.text;
		if (this.lable!=null)
			wb+="\tLABEL:\t"+this.lable;
		if (this.url!=null)
			wb+="\tURL:\t"+this.url;
		if(this.source!=null)
			wb+="\tSOURCE:\t"+this.source;
		if(this.geo!=null)
			wb+="\tGEO:\t"+this.geo;
		if(this.retweeted_status!=null)
			wb+="\tRE:\t"+this.retweeted_status;
		if(this.emotions!=null)
			wb+="\tEmotions:\t"+this.emotions;
		wb+="\tRecount:\t"+this.reposts_count+"\tCOcount:\t"+
			this.comments_count;
		
		return wb;
	}
	public Weibo(){
		this.mid=-1;
		this.user=null;
		this.createDate=new Date();
		this.text=null;
		this.source=null;
		this.geo=null;
		this.retweeted_status=null;
		this.reposts_count=-1;
		this.comments_count=-1;
		
		this.lable=null;
		this.url=null;
		this.emotions=null;
	}
	public Weibo(long MID,String User,Date CreateDate,String Text,String Source,String Geo,String Retweeted_status,int Reposts_count ,int Comments_count,String Lable, String Url,String Emotions){
		this.mid=MID;
		this.user=User;
		this.createDate=CreateDate;
		this.text=Text;
		this.source=Source;
		this.geo=Geo;
		this.retweeted_status=Retweeted_status;
		this.reposts_count=Reposts_count;
		this.comments_count=Comments_count;
		this.lable=Lable;
		this.url=Url;
		this.emotions=Emotions;
	}
	public Weibo(String MID,String User,Date CreateDate,String Text,String Source,String Geo,String Retweeted_status,String Reposts_count ,String Comments_count,String Lable, String Url){
		this.mid= Long.valueOf(MID).longValue();
		this.user=User;
		this.createDate=CreateDate;
		this.text=Text;
		this.source=Source;
		this.geo=Geo;
		this.retweeted_status=Retweeted_status;
		this.reposts_count=Integer.valueOf(Reposts_count).intValue();
		this.comments_count=Integer.valueOf(Comments_count).intValue();
		this.lable=Lable;
		this.url=Url;
	}
	public String getUser() {
		return user;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public String getGeo() {
		return geo;
	}
	public void setGeo(String geo) {
		this.geo = geo;
	}
	public String getRetweeted_status() {
		return retweeted_status;
	}
	public void setRetweeted_status(String retweeted_status) {
		this.retweeted_status = retweeted_status;
	}
	public int getReposts_count() {
		return reposts_count;
	}
	public void setReposts_count(int reposts_count) {
		this.reposts_count = reposts_count;
	}
	public int getComments_count() {
		return comments_count;
	}
	public void setComments_count(int comments_count) {
		this.comments_count = comments_count;
	}

	public String getLable() {
		return lable;
	}
	public void setLable(String lable) {
		this.lable = lable;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	
}