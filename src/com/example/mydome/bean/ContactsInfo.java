package com.example.mydome.bean;

public class ContactsInfo {
	private String name;
	private String sortLetters;  //显示数据拼音的首字母 
	private String phoneNumber;
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the sortLetters
	 */
	public String getSortLetters() {
		return sortLetters;
	}
	/**
	 * @param sortLetters the sortLetters to set
	 */
	public void setSortLetters(String sortLetters) {
		this.sortLetters = sortLetters;
	}
	/**
	 * @return the phoneNumber
	 */
	public String getPhoneNumber() {
		return phoneNumber;
	}
	/**
	 * @param phoneNumber the phoneNumber to set
	 */
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	
}
