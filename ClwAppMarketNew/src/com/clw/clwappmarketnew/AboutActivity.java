package com.clw.clwappmarketnew;

import java.util.List;





import com.clw.utils.CommonUtils;
import com.clw.view.MyButton;

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class AboutActivity extends Activity {
private EditText et; 
private MyButton lb;
private TextView tv;
private ImageView img;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about);
		PackageManager pm=this.getPackageManager(); 
			String packageName="com.clw.appmarket";
			et=(EditText)this.findViewById(R.id.editText2);
			img=(ImageView)this.findViewById(R.id.img_about_appicon);
		List<PackageInfo> pakageinfos = pm
				.getInstalledPackages(PackageManager.GET_UNINSTALLED_PACKAGES);
		for (PackageInfo pi : pakageinfos) {
			String pi_packageName = pi.packageName;
			String pi_versionName = pi.versionName;
			BitmapDrawable bd = (BitmapDrawable) pi.applicationInfo
					.loadIcon(pm);
			if (packageName.endsWith(pi_packageName)) {
				
			et.setText("版本号："+pi_versionName);
			
//			img.setImageBitmap(CommonUtils.zoomImage(
//										bd.getBitmap(), 150, 150));
//			
			}
		}
		
		lb=(MyButton)this.findViewById(R.id.id_top_back);
		tv=(TextView)this.findViewById(R.id.id_top_title);
		
		tv.setText("关于车联网应用市场");
		tv.setTextSize(16);
		
		lb.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		
		
		
		
		
	}
		
}
