package com.clw.clwappmarketnew;



import java.io.File;

import com.clw.data.PrefsHelper;
import com.clw.utils.CommonUtils;
import com.clw.utils.DataCleanManager;
import com.clw.view.MyButton;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class SettingActivity extends Activity {
	
	private Button btn_quit;
	private Button btn_clear;
	private Button btn_update_psw;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
		MyButton lb =(MyButton) this.findViewById(R.id.id_top_back);
		lb.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		TextView tv_title=(TextView)this.findViewById(R.id.id_top_title);
		tv_title.setText("设置");
		tv_title.setTextSize(16);
		btn_quit= (Button)this.findViewById(R.id.btn_set_quit);
		btn_clear=(Button)this.findViewById(R.id.btn_clear_cach);
		btn_update_psw=(Button)this.findViewById(R.id.btn_edt_psw);
		
		if (	PrefsHelper.get().loadString("clwphone","").equals("")){
			btn_quit.setText("未登录！");
			btn_update_psw.setVisibility(View.GONE);
			
		}
		
		btn_quit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
			if (	PrefsHelper.get().loadString("clwphone","").equals("")){
				
				CommonUtils.DisplayToast(getApplicationContext(), "您未登录！");
				
			}else{
				dialog();	
			}	
				
			}
		});
		
		btn_clear.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				DataCleanManager.cleanInternalCache(getApplicationContext());
				String storageState = Environment.getExternalStorageState();		
				if(storageState.equals(Environment.MEDIA_MOUNTED)){
				String	filePath = Environment.getExternalStorageDirectory().getAbsolutePath()+"/clwmarketdownload/";
					File file = new File(filePath);
					if(!file.exists()){
						file.mkdirs();
					}else{
						DataCleanManager.cleanCustomCache(filePath);
						
					}
					
				}
				
				CommonUtils.DisplayToast(getApplicationContext(), "清理成功！");
			}
		});
		btn_update_psw.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent= new Intent(getApplicationContext(), ManagerPswActivity.class);
				startActivity(intent);
			}
		});
		
	}
	
	protected void dialog() {
		AlertDialog.Builder builder = new Builder(SettingActivity.this);
		builder.setMessage("确认退出吗？");
		builder.setTitle("提示");
		builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
			
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				PrefsHelper.get().saveString("clwusername", "");
				PrefsHelper.get().saveString("clwphone", "");
				PrefsHelper.get().saveString("clwpassword", "");
				PrefsHelper.get().saveString("clwemail","");
				PrefsHelper.get().saveString("clwsex", "");
				
				
				dialog.dismiss();
				
				
				SettingActivity.this.finish();
			}

			
		
		});
		
		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		});
		builder.create().show();

	}

	
	
}
