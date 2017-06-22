package com.clw.clwappmarketnew;


import com.clw.data.PrefsHelper;
import com.clw.view.MyButton;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class UserDetailActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_detail);

		MyButton lb = (MyButton) this.findViewById(R.id.id_top_back);
		lb.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});

		TextView tv_back = (TextView) this.findViewById(R.id.id_top_title);
		tv_back.setText("用户详细信息");
		tv_back.setTextSize(16);

		TextView tv_name = (TextView) this.findViewById(R.id.tv_udet_uname);
		TextView tv_phone = (TextView) this.findViewById(R.id.tv_udet_phone);
		TextView tv_email = (TextView) this.findViewById(R.id.tv_udet_email);

		tv_name.setText(PrefsHelper.get().loadString("clwusername", ""));

		tv_phone.setText(PrefsHelper.get().loadString("clwphone", ""));
		tv_email.setText(PrefsHelper.get().loadString("clwemail", ""));

		Button btn_quit = (Button) this.findViewById(R.id.btn_quit);
		btn_quit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialog();
			}
		});

	}

	protected void dialog() {
		AlertDialog.Builder builder = new Builder(UserDetailActivity.this);
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
				
				
				UserDetailActivity.this.finish();
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
