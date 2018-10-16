/**
 * 
 */
package com.clw.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;


/**
 * @author smile
 *
 */
public class PrefsHelper {

	/**
	 * 
	 */
	private PrefsHelper() {
		
	}
	static private PrefsHelper helper = new PrefsHelper();
	private long devID = 0;
	public static PrefsHelper get(){
		return helper;
	}
	Context context;
	public void init(Context context){
		this.context = context;
	}
	
	public void saveString(String key, String value){
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
		prefs.edit().putString(key, value).commit();
	}
	public String loadString(String key, String defautVal){
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
		return prefs.getString(key, defautVal);
	}
	
	public String getUserID() {
		return "";
	}
	
	public long getDevID() {
		if (devID == 0) {
			String did = loadString("userdevid", "0");
			devID = Long.parseLong(did);
			if (devID == 0) {
				devID = System.currentTimeMillis();
				did = String.format("%d", devID);
				saveString("userdevid", did);
			}
		}
		return devID;
	}
	
	
}
