package com.clw.domain;

import java.util.Date;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

public class AppInfo {

	private int appId;
	// 搴旂敤绋嬪簭鏍囩
	private String appName;
	// 搴旂敤绋嬪簭鍥惧儚
	private Drawable drawable;
	// 鍥炬爣
	private Bitmap appIcon;
	// 鍚姩搴旂敤绋嬪簭鐨処ntent 锛屼竴鑸槸Action涓篗ain鍜孋ategory锟斤拷Lancher鐨凙ctivity
	private Intent intent;
	// 鍚姩鐨刟ctivity
	private String appActivity;
	// 搴旂敤鐗堟湰 versioncode
	private String version;
	// 搴旂敤绋嬪簭鎵�瀵瑰簲鐨勫寘鍚�
	private String pkgName;

	// 绫诲埆
	private String kind;
	// 搴旂敤璇︾粏浠嬬粛
	private String detailIntroduce;

	// 涓嬭浇閲�
	private String downloadCount;

	private Date addTime;
	private Date modifyTime;

	private String sAddTime;

	private String sModifyTime;
	// app鍐呭
	private String appcontent;
	// 鏂板姛鑳�
	private String appnewfun;
	// 寮�鍙戝晢
	private String devcompany;
	// 寮�鍙戣��
	private String developer;
	// 鐗堟湰versionname
	private String versionname;
	// apk 澶у皬
	private String size;
	// 鍏煎鎬�
	private String compatibility;
private Bitmap imgIntroduction;
	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public Drawable getDrawable() {
		return drawable;
	}

	public void setDrawable(Drawable drawable) {
		this.drawable = drawable;
	}

	public Bitmap getAppIcon() {
		return appIcon;
	}

	public void setAppIcon(Bitmap appIcon) {
		this.appIcon = appIcon;
	}

	public Intent getIntent() {
		return intent;
	}

	public void setIntent(Intent intent) {
		this.intent = intent;
	}

	public String getAppActivity() {
		return appActivity;
	}

	public void setAppActivity(String appActivity) {
		this.appActivity = appActivity;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getPkgName() {
		return pkgName;
	}

	public void setPkgName(String pkgName) {
		this.pkgName = pkgName;
	}

	public String getKind() {
		return kind;
	}

	public void setKind(String kind) {
		this.kind = kind;
	}

	public String getDetailIntroduce() {
		return detailIntroduce;
	}

	public void setDetailIntroduce(String detailIntroduce) {
		this.detailIntroduce = detailIntroduce;
	}

	public String getDownloadCount() {
		return downloadCount;
	}

	public void setDownloadCount(String downloadCount) {
		this.downloadCount = downloadCount;
	}

	public int getAppId() {
		return appId;
	}

	public void setAppId(int appId) {
		this.appId = appId;
	}

	public Date getAddTime() {
		return addTime;
	}

	public void setAddTime(Date addTime) {
		this.addTime = addTime;
	}

	public Date getModifyTime() {
		return modifyTime;
	}

	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}

	public String getsAddTime() {
		return sAddTime;
	}

	public void setsAddTime(String sAddTime) {
		this.sAddTime = sAddTime;
	}

	public String getsModifyTime() {
		return sModifyTime;
	}

	public void setsModifyTime(String sModifyTime) {
		this.sModifyTime = sModifyTime;
	}

	public String getAppcontent() {
		return appcontent;
	}

	public void setAppcontent(String appcontent) {
		this.appcontent = appcontent;
	}

	public String getAppnewfun() {
		return appnewfun;
	}

	public void setAppnewfun(String appnewfun) {
		this.appnewfun = appnewfun;
	}

	public String getDevcompany() {
		return devcompany;
	}

	public void setDevcompany(String devcompany) {
		this.devcompany = devcompany;
	}

	public String getDeveloper() {
		return developer;
	}

	public void setDeveloper(String developer) {
		this.developer = developer;
	}

	public String getVersionname() {
		return versionname;
	}

	public void setVersionname(String versionname) {
		this.versionname = versionname;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public String getCompatibility() {
		return compatibility;
	}

	public void setCompatibility(String compatibility) {
		this.compatibility = compatibility;
	}

	public Bitmap getImgIntroduction() {
		return imgIntroduction;
	}

	public void setImgIntroduction(Bitmap imgIntroduction) {
		this.imgIntroduction = imgIntroduction;
	}

}
