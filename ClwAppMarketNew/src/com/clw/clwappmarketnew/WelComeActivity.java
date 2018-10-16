package com.clw.clwappmarketnew;




import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;




import org.ksoap2.serialization.SoapObject;

import com.clw.data.DevAttribute;
import com.clw.data.PrefsHelper;


import com.clw.domain.UserInfo;
import com.clw.utils.CommonUtils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;

public class WelComeActivity extends Activity {
	private static final int LOGIN_SUCC = 0;
	private static final int LOGIN_FAILURE = 1;

	private Object obj_user=null;
	
	private LoginThread loginThread;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_welcome);
		PrefsHelper.get().init(getApplicationContext());
		PrefsHelper.get().getDevID();
		initDevAttribute();
		//µÇÂ¼Ò»´Î ´úÂë´ýÔö
		if (CommonUtils.isNetWorkConnected(getApplicationContext())){
			if (obj_user==null){
			if (loginThread!=null){
				loginThread.interrupt();
				loginThread=null;
				
			}
			loginThread=new LoginThread();
			loginThread.start();
			
			}
			
		}else{
			
			//CommonUtils.DisplayToast(getApplicationContext(), "ÎÞÍøÂç£¬Çë¼ì²éÍøÂç×´Ì¬£¡");
		}
	}
	
	
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		new Timer().schedule(new TimerTask() {
			
			@Override
			public void run() {
				startActivity(new Intent(WelComeActivity.this, MainActivity1.class));
				finish();
			}
		}, 2000);
		super.onStart();
	}
	Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub

			switch (msg.what) {

			case LOGIN_SUCC:
				SoapObject stmp = CommonUtils.analysis((SoapObject) obj_user,
						"getUser");
				Log.i("stmp", stmp.toString());
				UserInfo userInfo = new UserInfo();
				try {
					userInfo.setUserid(Integer.parseInt(((SoapObject) stmp
							.getProperty(0)).getProperty("userid").toString()));
	
					userInfo.setUsername(((SoapObject) stmp
							.getProperty(0)).getProperty("username").toString());
		
					userInfo.setPassword(((SoapObject) stmp
							.getProperty(0)).getProperty("password").toString());
		
					userInfo.setPhone(((SoapObject) stmp
							.getProperty(0)).getProperty("phone").toString());
		
					userInfo.setEmail(((SoapObject) stmp
							.getProperty(0)).getProperty("email").toString());
		
					userInfo.setSex(((SoapObject) stmp
							.getProperty(0)).getProperty("sex").toString());
				} catch (Exception e) {
					// TODO: handle exception

				}
				
				PrefsHelper.get().saveString("clwusername", userInfo.getUsername());
				PrefsHelper.get().saveString("clwphone", userInfo.getPhone());
				PrefsHelper.get().saveString("clwpassword", userInfo.getPassword());
				PrefsHelper.get().saveString("clwemail", userInfo.getEmail());
				PrefsHelper.get().saveString("clwsex", userInfo.getSex());
				
				
			
				//finish();
				Log.i("mes", msg.obj.toString());
				break;
			case LOGIN_FAILURE:
				
				PrefsHelper.get().saveString("clwusername", "");
				PrefsHelper.get().saveString("clwphone", "");
				PrefsHelper.get().saveString("clwpassword", "");
				PrefsHelper.get().saveString("clwemail", "");
				
			

				Log.i("mes", msg.obj.toString());
				//CommonUtils.DisplayToast(getApplicationContext(), "ÓÃ»§Ãû»òÕßÃÜÂëÓÐÎó£¡");
				break;
			}

			super.handleMessage(msg);
		}

	};

	private class LoginThread extends Thread{
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			try {
				login();		
			} catch (Exception e) {
				// TODO: handle exception
			}
		
		}
	}
	
	public void login(){
		String sPhone=PrefsHelper.get().loadString("clwphone", "");
		String sEmail=PrefsHelper.get().loadString("clwemail", "");
		String sPwd=PrefsHelper.get().loadString("clwpassword", "");
		
		if (sPhone.length()>0){
		List<String> lst = new ArrayList<String>();

		lst.add("user");
		lst.add(sPhone);
		lst.add("pwd");
		lst.add(sPwd);
		Object obj = CommonUtils
				.GetWebserviceData("login", lst);
		
		String sResult = "";
		sResult = ((SoapObject) obj).getProperty("loginResult")
				.toString();
		// Log.i("obj", sResult);

		if (sResult.equals("")) {
			CommonUtils.SendMsg(handler, LOGIN_FAILURE, "µÇÂ¼Ê§°Ü",
					0);

		} else if (sResult.equals("true")) {

			obj_user = CommonUtils.GetWebserviceData("getUser",
					lst);

			Log.i("obj_user", obj_user.toString());
			CommonUtils.SendMsg(handler, LOGIN_SUCC, "µÇÂ¼³É¹¦", 0);

		} else if (sResult.equals("false")) {
			CommonUtils.SendMsg(handler, LOGIN_FAILURE, "µÇÂ¼Ê§°Ü",
					0);

		}
		
		}
		
		
		
	}
	
	 private void initDevAttribute() {
	        DisplayMetrics dm = new DisplayMetrics();
	        dm = this.getApplicationContext().getResources().getDisplayMetrics(); 
	        DevAttribute.getInstance().setScreenWidth(dm.widthPixels);
	        DevAttribute.getInstance().setScreenHeight(dm.heightPixels);
	        DevAttribute.getInstance().setDensity(dm.density);
	        
	    }
	
}
