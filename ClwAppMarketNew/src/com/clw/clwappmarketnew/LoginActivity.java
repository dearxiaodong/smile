package com.clw.clwappmarketnew;

import java.util.ArrayList;
import java.util.List;

import org.ksoap2.serialization.SoapObject;



import com.clw.data.MD5;
import com.clw.data.PrefsHelper;
import com.clw.domain.UserInfo;
import com.clw.utils.CommonUtils;
import com.clw.view.MyButton;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends Activity {
	private TextView top_title;
	private MyButton top_back;
	private EditText edt_usname;
	private EditText edt_psw;
	private Button btn_login;
	private TextView tv_reg;
	private TextView tv_forget;
	private ProgressDialog pdg;

	private static final int LOGIN_SUCC = 0;
	private static final int LOGIN_FAILURE = 1;

	private Object obj_user;
	private GetLoginThread loginThread;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		top_title = (TextView) this.findViewById(R.id.id_top_title);
		top_title.setText("登录");
		top_title.setTextSize(16);

		top_back = (MyButton) this.findViewById(R.id.id_top_back);
		top_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});

		edt_usname = (EditText) this.findViewById(R.id.edt_login_username);
		edt_psw = (EditText) this.findViewById(R.id.edt_login_psw);

		btn_login = (Button) this.findViewById(R.id.btn_login_login);
		tv_reg = (TextView) this.findViewById(R.id.tv_login_reg);
		tv_forget = (TextView) this.findViewById(R.id.tv_login_forget);

		btn_login.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (CommonUtils.isNetWorkConnected(getApplicationContext())) {

					if (login()) {
						if (pdg != null) {
							pdg.cancel();
							pdg = null;

						}
//						pdg = ProgressDialog.show(LoginActivity.this, "正在登录",
//								"登录中...",true);
//						pdg=new ProgressDialog(LoginActivity.this);
//						pdg.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//						pdg.setMessage("登录中...");
//						pdg.setTitle("登录");
//						pdg.setIcon(R.drawable.loading_throbber);
//						pdg.show();
						
						pdg = new ProgressDialog(LoginActivity.this,
								ProgressDialog.THEME_HOLO_LIGHT);
						pdg.setTitle("登录");
						pdg.setMessage("正在登录，请稍等...");
					
						pdg.setCanceledOnTouchOutside(false);
						pdg.setCancelable(true);
						pdg.show();
						
						if (loginThread!=null){
							
							loginThread.interrupt();loginThread=null;
							
						}
						loginThread=new GetLoginThread();
						loginThread.start();
						

					}else {
						CommonUtils.DisplayToast(getApplicationContext(),
								"未连接网络，请检查网络设置！");

					}
				}

			}
		});

		
		tv_reg.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				Intent intent =new Intent(getApplicationContext(), RegisterActivity.class);
				startActivity(intent);
				
			}
		});
	}

	void alertErr(String err) {
		Toast.makeText(this, err, Toast.LENGTH_SHORT).show();
	}

	public boolean login() {
		boolean result = true;
		String usname = edt_usname.getText().toString();

		if (usname.length() == 0) {
			alertErr("用户名不能为空！");
			result = false;
		}

		String psw = edt_usname.getText().toString();

		if (psw.length() == 0) {
			alertErr("密码不能为空！");
			result = false;
		}
		return result;
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
				} catch (Exception e) {
					// TODO: handle exception

				}

				try {
					userInfo.setUsername(((SoapObject) stmp.getProperty(0))
							.getProperty("username").toString());
				} catch (Exception e) {
					// TODO: handle exception

				}

				try {
					userInfo.setPassword(((SoapObject) stmp.getProperty(0))
							.getProperty("password").toString());
				} catch (Exception e) {
					// TODO: handle exception

				}

				try {
					userInfo.setPhone(((SoapObject) stmp.getProperty(0))
							.getProperty("phone").toString());
				} catch (Exception e) {
					// TODO: handle exception

				}

				try {
					userInfo.setEmail(((SoapObject) stmp.getProperty(0))
							.getProperty("email").toString());
				} catch (Exception e) {
					// TODO: handle exception

				}
				try {
					userInfo.setSex(((SoapObject) stmp.getProperty(0))
							.getProperty("sex").toString());
				} catch (Exception e) {
					// TODO: handle exception

				}

				PrefsHelper.get().saveString("clwusername",
						userInfo.getUsername());
				PrefsHelper.get().saveString("clwphone", userInfo.getPhone());
				PrefsHelper.get().saveString("clwpassword",
						userInfo.getPassword());
				PrefsHelper.get().saveString("clwemail", userInfo.getEmail());
				PrefsHelper.get().saveString("clwsex", userInfo.getSex());

				if (pdg != null) {
					pdg.cancel();
					pdg = null;

				}
				finish();
				Log.i("mes", msg.obj.toString());
				break;
			case LOGIN_FAILURE:

				PrefsHelper.get().saveString("clwusername", "");
				PrefsHelper.get().saveString("clwphone", "");
				PrefsHelper.get().saveString("clwpassword", "");
				PrefsHelper.get().saveString("clwemail", "");
				PrefsHelper.get().saveString("clwsex", "");
				if (pdg != null) {
					pdg.cancel();
					pdg = null;

				}

				Log.i("mes", msg.obj.toString());
				CommonUtils.DisplayToast(getApplicationContext(), "用户名或者密码有误！");
				break;
			}

			super.handleMessage(msg);
		}

	};
	
private class GetLoginThread extends Thread{
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			getLogin();
		} catch (Exception e) {
			// TODO: handle exception
		}
		
	}
	
	
}	
	
public void getLogin(){
	
	
	List<String> lst = new ArrayList<String>();

	lst.add("user");
	lst.add(edt_usname.getText().toString());
	lst.add("pwd");
	lst.add(MD5.md5(edt_psw.getText().toString()));

	Object obj = CommonUtils
			.GetWebserviceData("login", lst);
	 Log.i("psw", MD5.md5(edt_psw.getText().toString()));
	String sResult = "";
	sResult = ((SoapObject) obj).getProperty("loginResult")
			.toString();
	// Log.i("obj", sResult);

	if (sResult.equals("")) {
		CommonUtils.SendMsg(handler, LOGIN_FAILURE, "登录失败",
				0);

	} else if (sResult.equals("true")) {

		obj_user = CommonUtils.GetWebserviceData("getUser",
				lst);

		Log.i("obj_user", obj_user.toString());
		CommonUtils.SendMsg(handler, LOGIN_SUCC, "登录成功", 0);

	} else if (sResult.equals("false")) {
		CommonUtils.SendMsg(handler, LOGIN_FAILURE, "登录失败",
				0);

	}


	
}	
	
	

}
