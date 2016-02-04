package com.example.mydome.bean;

public class ChatInfo {
	private String id;  //聊天对象的id
	private String name_who; //这条信息的发送者的名字
	private String content;
	private String time;
	private int fromOrTo; //值为0是接收到信息，1为发送出去的消息
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName_who() {
		return name_who;
	}
	public void setName_who(String name_who) {
		this.name_who = name_who;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public int getFromOrTo() {
		return fromOrTo;
	}
	public void setFromOrTo(int fromOrTo) {
		this.fromOrTo = fromOrTo;
	}
	
	
}
