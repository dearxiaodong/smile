package com.clw.clwappmarketnew;

import java.util.ArrayList;
import java.util.List;

import org.ksoap2.serialization.SoapObject;



import com.clw.data.MD5;
import com.clw.data.PrefsHelper;
import com.clw.utils.CommonUtils;
import com.clw.view.MyButton;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.SwitchPreference;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ManagerPswActivity extends Activity {

	private EditText edt_old;
	private EditText edt_new;
	private EditText edt_new_con;
	private Button btn_tj;
	private UpdatePswThread pswThread;
	private static final int PSW_UPDATE_SUCC=0;
	private static final int PSW_UPDATE_FAILURE=1;
private Object obj;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_managerpsw);
		edt_old = (EditText) this.findViewById(R.id.edt_psw_old);

		edt_new = (EditText) this.findViewById(R.id.edt_psw_new);
		edt_new_con = (EditText) this.findViewById(R.id.edt_psw_new_con);

		btn_tj = (Button) this.findViewById(R.id.btn_psw_tj);
		btn_tj.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (managerPsw()) {
					if (CommonUtils.isNetWorkConnected(getApplicationContext())) {
						if (pswThread != null) {

							pswThread.interrupt();
							pswThread = null;
						}
						pswThread = new UpdatePswThread();
						pswThread.start();
						
						
					}else{
						
					CommonUtils.DisplayToast(getApplicationContext(), getString(R.string.net_state_disconnection));	
					}

				}

			}
		});
		
		MyButton lb=(MyButton) this.findViewById(R.id.id_top_back);
		lb.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		
		TextView tv_title=(TextView)this.findViewById(R.id.id_top_title);
		tv_title.setText("修改密码");
		tv_title.setTextSize(16);
		
	}

	public boolean managerPsw() {
		boolean result = true;
		String psw_old = MD5.md5(edt_old.getText().toString());

		if (psw_old.length() == 0) {
			alertErr("原密码不能为空！");
			result = false;
		}
		if (!psw_old.equals(PrefsHelper.get().loadString("clwpassword", ""))) {
			alertErr("原密码不正确！");
			result = false;
		}
		String psw_new = edt_new.getText().toString();
		String psw_new_con = edt_new_con.getText().toString();

		if (psw_new.length() == 0) {
			alertErr("新密码不能为空！");
			result = false;
		}
		if (psw_new_con.length() == 0) {
			alertErr("确认密码不能为空！");
			result = false;
		}
		if (!psw_new.endsWith(psw_new_con)) {
			alertErr("新密码与确认密码不一致！");
			result = false;

		}

		return result;
	}

	void alertErr(String err) {
		Toast.makeText(this, err, Toast.LENGTH_SHORT).show();
	}

	private class UpdatePswThread extends Thread {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			try {
				updatePsw();
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
	}

	public void updatePsw() {
		List<String> lst = new ArrayList<String>();
		lst.add("password");
		String sphone=PrefsHelper.get().loadString("clwphone", "");
	//	String sphone=PrefsHelper.get().loadString("clwpassword", "");
		if (sphone.equals("")){
		lst.add(MD5.md5(edt_new.getText().toString()));
		lst.add("phone");
		
//			String st=" update t_userinfo set password ='"+edt_new.getText().toString()+"' where phone ='"+sphone+"' ";
		lst.add(sphone);
	obj=	CommonUtils.GetWebserviceData("updatePsw", lst);
	
		CommonUtils.SendMsg(handler, PSW_UPDATE_SUCC, "修改成功", 0);
		
		}else{
			CommonUtils.SendMsg(handler, PSW_UPDATE_FAILURE, "修改失败", 0);	
			
		}
		

	}

	Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch (msg.what) {
			case PSW_UPDATE_SUCC:
			int iResult=-1;
			iResult=Integer.parseInt(((SoapObject) obj).getProperty("updatePswResult")
					.toString());
			if (iResult==1){
				
				CommonUtils.DisplayToast(getApplicationContext(), msg.obj.toString());
			finish();
			}else{
				CommonUtils.DisplayToast(getApplicationContext(), "修改失败");
			}	
				
				break;
			case PSW_UPDATE_FAILURE:
				CommonUtils.DisplayToast(getApplicationContext(), msg.obj.toString());
				
				break;
			default:
				break;
			}

			super.handleMessage(msg);
		}
	};

}
