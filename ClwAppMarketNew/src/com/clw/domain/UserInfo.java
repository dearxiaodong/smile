package com.clw.domain;

import android.graphics.Bitmap;

public class UserInfo {
	private int userid;
	private String	username;
	private String	      password;
	private String	      userimagebase64;
	private String	      phone;
	private String	      email;
	private String	      sex;
	private String	      regdate;
	private String	      motifytime;
	private Bitmap	      userimage;
	public int getUserid() {
		return userid;
	}
	public void setUserid(int userid) {
		this.userid = userid;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getUserimagebase64() {
		return userimagebase64;
	}
	public void setUserimagebase64(String userimagebase64) {
		this.userimagebase64 = userimagebase64;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getRegdate() {
		return regdate;
	}
	public void setRegdate(String regdate) {
		this.regdate = regdate;
	}
	public String getMotifytime() {
		return motifytime;
	}
	public void setMotifytime(String motifytime) {
		this.motifytime = motifytime;
	}
	public Bitmap getUserimage() {
		return userimage;
	}
	public void setUserimage(Bitmap userimage) {
		this.userimage = userimage;
	}
	
	
}
