package com.clw.clwappmarketnew;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.ksoap2.serialization.SoapObject;



import com.clw.data.MD5;
import com.clw.utils.CommonUtils;
import com.clw.view.MyButton;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

public class RegisterActivity extends Activity {
	private List<String> lPhone;
	private List<String> lEmail;
	private Object oMes;
	private Object oReg;
	private static final int GET_MES_SUCC = 0;
	private static final int GET_MES_FAILURE = 1;
	private static final int GET_REG_SUCC = 2;
	private static final int GET_REG_FAILURE = 3;
	private GetMesThread mesThread;
	private RegisterThread registerThread;

	private EditText edt_phone;
	private EditText edt_email;
	private EditText edt_psw;
	private EditText edt_psw_con;
	private String sex;
	private RadioButton rb_men;
	private RadioButton rb_women;
	private Button btn_reg;
	private final static Pattern emailer = Pattern
			.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");
	private ProgressDialog pdg;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_register);

		MyButton lb = (MyButton) this.findViewById(R.id.id_top_back);
		lb.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		lPhone = new ArrayList<String>();
		lEmail = new ArrayList<String>();
		TextView tv_title = (TextView) this.findViewById(R.id.id_top_title);
		tv_title.setText("注册");
		tv_title.setTextSize(16);
		if (mesThread != null) {

			mesThread.interrupt();
			mesThread.start();
		}
		mesThread = new GetMesThread();
		mesThread.start();
		edt_phone = (EditText) this.findViewById(R.id.ed_reg_phone);
		edt_email = (EditText) this.findViewById(R.id.ed_reg_email);
		edt_psw = (EditText) this.findViewById(R.id.ed_reg_password);
		edt_psw_con = (EditText) this.findViewById(R.id.ed_reg_password_conf);
		// sex;
		rb_men = (RadioButton) this.findViewById(R.id.radio_men);
		rb_women = (RadioButton) this.findViewById(R.id.radio_women);
		// rb_men.setOnCheckedChangeListener(new OnCheckedChangeListener() {
		//
		// @Override
		// public void onCheckedChanged(CompoundButton buttonView, boolean
		// isChecked) {
		// // TODO Auto-generated method stub
		//
		// }
		// });
		btn_reg = (Button) this.findViewById(R.id.btn_reg);
		btn_reg.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				if (rb_men.isChecked()) {
					sex = "男";
				}
				if (rb_women.isChecked()) {
					sex = "女";
				}
				// if (CommonUtils.isNetWorkConnected(getApplicationContext()))
				// {
				if (beforeReg()) {
					if (pdg != null) {
						pdg.cancel();
						pdg = null;

					}
					pdg = ProgressDialog.show(RegisterActivity.this, "正在注册",
							"注册中...", true);
					if (registerThread != null) {
						registerThread.interrupt();
						registerThread = null;
					}
					registerThread = new RegisterThread();
					registerThread.start();
					// } else {
					// CommonUtils.DisplayToast(getApplicationContext(),
					// getString(R.string.net_state_disconnection));
					//
					// }
				}else{
					
					Log.i("false", "false");
				}
			}
		});

	}

	private class GetMesThread extends Thread {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			try {
				getMes();
			} catch (Exception e) {
				// TODO: handle exception
			}
		}

	}

	private void getMes() {
		List<String> lstPra = new ArrayList<String>();
		lstPra.add("strsql");
		lstPra.add("2");
		oMes = CommonUtils.GetWebserviceData("getUserList", lstPra);
		CommonUtils.SendMsg(handler, GET_MES_SUCC, "获取成功", 0);

	}

	Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch (msg.what) {
			case GET_MES_SUCC:
				try {
					SoapObject tpObj = CommonUtils.analysis((SoapObject) oMes,
							"getUserList");
					for (int i = 0; i < tpObj.getPropertyCount(); i++) {
						SoapObject tmpObj = (SoapObject) tpObj.getProperty(i);

						lPhone.add(tmpObj.getProperty("phone").toString());

						lEmail.add(tmpObj.getProperty("email").toString());

					}
					Log.i("lphone", lPhone.toString());
					Log.i("lEmail", lEmail.toString());
				} catch (Exception e) {
					// TODO: handle exception

				}

				break;
			case GET_MES_FAILURE:

				break;
			case GET_REG_SUCC:
				Log.i("oReg", oReg.toString());
				String sResult = "";
				sResult = ((SoapObject) oReg).getProperty("insertUserResult")
						.toString();
				if (sResult.equals("success")) {

					CommonUtils.DisplayToast(getApplicationContext(), "注册成功");

				} else {
					CommonUtils.DisplayToast(getApplicationContext(), "注册失败");

				}
				if (pdg != null) {
					pdg.cancel();
					pdg = null;

				}
				finish();

				break;
			case GET_REG_FAILURE:

				CommonUtils.DisplayToast(getApplicationContext(),
						msg.obj.toString());
				break;

			default:
				break;
			}
			super.handleMessage(msg);
		}
	};

	private class RegisterThread extends Thread {
		@Override
		public void run() {
			// TODO Auto-generated method stub
			try {
				register();
			} catch (Exception e) {
				// TODO: handle exception
			}
		}

	}

	public void register() {
		List<String> lstPra = new ArrayList<String>();
//		 lstPra.add("strsql");
//		 String
//		 sql="insert into t_userinfo( username,password,phone,email,sex,regdate,motifytime) values('"
//		 +edt_phone.getText().toString()+"','"+edt_psw.getText().toString()+"','"+edt_phone.getText().toString()+"','"+edt_email.getText().toString()
//		 +"','"+sex+"',getDate(),getdate())";
//		 lstPra.add(sql);

		lstPra.add("username");
		lstPra.add(edt_phone.getText().toString());
		lstPra.add("pwd");
		lstPra.add(MD5.md5(edt_psw.getText().toString()));
		lstPra.add("phone");
		lstPra.add(edt_phone.getText().toString());
		lstPra.add("email");
		lstPra.add(edt_email.getText().toString());
		lstPra.add("sex");
		lstPra.add(sex);

		oReg = CommonUtils.GetWebserviceData("insertUser", lstPra);
		CommonUtils.SendMsg(handler, GET_REG_SUCC, "注册成功", 0);
	}

	void alertErr(String err) {
		Toast.makeText(this, err, Toast.LENGTH_SHORT).show();
	}

	public boolean beforeReg() {
		boolean result = true;
		String sPhone = edt_phone.getText().toString();
		String sEmail = edt_email.getText().toString();
		String sPsw = edt_psw.getText().toString();
		String sPsw_con = edt_psw_con.getText().toString();
		Log.i("phone", sPhone);
		Log.i("sEmail", sEmail);
		Log.i("sPsw", sPsw);
		Log.i("sPsw_con", sPsw_con);
		Log.i("sex", sex);

		if (sPhone.length() == 0) {
			alertErr("手机号不能为空！");
			result = false;
			return result;
		}
		if (sEmail.length() == 0) {
			alertErr("邮箱不能为空！");
			result = false;
			return result;
		}
		if (!isEmail(sEmail)) {
			alertErr("邮箱格式不正确！");
			result = false;
			return result;
		}
		if (sPsw.length() == 0) {
			alertErr("密码不能为空！");
			result = false;
			return result;
		}
		if (sPsw_con.length() == 0) {
			alertErr("确认密码不能为空！");
			result = false;
			return result;
		}
		if (!sPsw.equals(sPsw_con)) {
			alertErr("密码不一致！");
			result = false;
			return result;
		}
		if (lPhone.size() > 0 && lPhone.contains(sPhone)) {
			alertErr("手机号已经注册！");
			result = false;
			return result;

		}
		if (lEmail.size() > 0 && lEmail.contains(sEmail)) {
			alertErr("邮箱已经注册！");
			result = false;
			return result;

		}
		return result;

	}

	/**
	 * 判断是不是一个合法的电子邮件地址
	 * 
	 * @param email
	 * @return
	 */
	public boolean isEmail(String email) {
		if (email == null || email.trim().length() == 0)
			return false;
		return emailer.matcher(email).matches();
	}

}
