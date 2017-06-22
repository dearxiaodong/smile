package com.clw.clwappmarketnew;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.ksoap2.serialization.SoapObject;



import com.clw.utils.CommonUtils;


import com.clw.view.MyButton;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.SimpleAdapter.ViewBinder;
import android.widget.TextView;
import android.widget.Toast;

public class MyAppActivity extends Activity {
	private MyButton lb;
	private TextView top_tv;
	private ArrayList<HashMap<String, Object>> lstApp = null;
	// 服务端取的公司applist
	private List<String> lstPkg = null;
	private List<String> lstPra = null;
	private GetInstalledAppInfoThread getInstalledAppInfo = null;
	// 已安装的list
	private ArrayList<HashMap<String, Object>> lstInstalledPkg = null;

	private static final int DATA_FULL = 1;
	private static final int DATA_POOR = 2;

	private GridView gv;
	private TextView tv_tips;
	private Object obj_app;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.myapp);

		lb = (MyButton) this.findViewById(R.id.id_top_back);
		top_tv = (TextView) this.findViewById(R.id.id_top_title);
		top_tv.setText("未装应用");
		top_tv.setTextSize(16);
		lb.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});

		lstPkg = new ArrayList<String>();
		lstPra = new ArrayList<String>();
		lstInstalledPkg = new ArrayList<HashMap<String, Object>>();

		gv = (GridView) this.findViewById(R.id.gv_myapp);
		tv_tips = (TextView) this.findViewById(R.id.tv_myapp_tips);

		Log.i("myapp", "线程before");
		if (getInstalledAppInfo != null) {
			getInstalledAppInfo.interrupt();
			getInstalledAppInfo = null;

		}
		getInstalledAppInfo = new GetInstalledAppInfoThread();
		getInstalledAppInfo.start();
		Log.i("myapp", "线程after");
		tv_tips.setVisibility(View.GONE);
	}

	private class GetInstalledAppInfoThread extends Thread {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			try {
				// super.run();
				Log.i("myapp", "线程ing");
				GetInstalledAppInfo();
			} catch (Exception e) {
				// TODO: handle exception
			}

		}

	}

	private void GetInstalledAppInfo() {
		lstPra.add("strsql");
		//lstPra.add("select pkgname  FROM [clwappstore].[dbo].[t_appinfo]  order by appid");
		lstPra.add("select *  FROM t_appinfo  order by appid");
		obj_app = CommonUtils.GetWebserviceData("getData", lstPra);

		CommonUtils.SendMsg(handler, DATA_FULL, getString(R.string.str_prompt)
				.toString(), 0);

	}

	final Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			try {
				switch (msg.what) {
				case DATA_FULL:

					SoapObject tmpObj = CommonUtils.analysis(
							(SoapObject) obj_app, "getData");

					if (tmpObj != null) {
						// Log.i("myapp", tmpObj.toString());
						// Log.i("myapp",
						// String.valueOf(tmpObj.getPropertyCount()));
						for (int i = 0; i < tmpObj.getPropertyCount(); i++) {
							SoapObject tObj = (SoapObject) tmpObj
									.getProperty(i);
							lstPkg.add(tObj.getProperty("pkgname").toString());
							// Log.i("myapp", tObj.getProperty("pkgname")
							// .toString());
						}
					}

					PackageManager pm = getApplication().getPackageManager();
					List<PackageInfo> pakageinfos = pm
							.getInstalledPackages(PackageManager.GET_UNINSTALLED_PACKAGES);
					for (PackageInfo pi : pakageinfos) {

						if (lstPkg.size() > 0
								&& lstPkg.contains(pi.packageName)) {
							// Log.i("myapp", "lstpkg full");
							HashMap<String, Object> map = new HashMap<String, Object>();
							// lstInstalledPkg.add(pi.packageName);pi.applicationInfo.loadIcon(getPackageManager())
							BitmapDrawable bd = (BitmapDrawable) pi.applicationInfo
									.loadIcon(getPackageManager());

							map.put("appIcon", CommonUtils.zoomImage(
									bd.getBitmap(), 150, 150));
							map.put("appName", pi.applicationInfo
									.loadLabel(getPackageManager()));
							map.put("pkg", pi.packageName);
						
							lstInstalledPkg.add(map);
						} else {
							// Log.i("myapp", "lstpkg null");

						}

					}
					// Log.i("myapp", "ss");
					if (lstInstalledPkg.size() > 0) {
						Log.i("myapp", lstInstalledPkg.toString());
						tv_tips.setVisibility(View.GONE);
						SimpleAdapter sp = new SimpleAdapter(
								getApplicationContext(), lstInstalledPkg,
								R.layout.myapp_item, new String[] { "appIcon",
										"appName" }, new int[] {
										R.id.img_myapp_icon,
										R.id.tv_myapp_item_appname });
						gv.setAdapter(sp);
						sp.setViewBinder(new ViewBinder() {

							@Override
							public boolean setViewValue(View view, Object data,
									String textRepresentation) {
								// TODO Auto-generated method stub

								if (view instanceof ImageView
										&& data instanceof Bitmap) {

									ImageView iv = (ImageView) view;

									iv.setImageBitmap((Bitmap) data);

									return true;

								} else if (view instanceof TextView
										&& data instanceof String) {
									TextView tv = (TextView) view;
									tv.setText(data.toString());
									Log.i("tv", data.toString());
									Log.i("tv", (String) data);
									return true;

								} else {

									return false;
								}

							}
						});

						gv.setOnItemClickListener(new OnItemClickListener() {

							@Override
							public void onItemClick(AdapterView<?> parent,
									View view, int position, long id) {
								// TODO Auto-generated method stub

								Intent intent = new Intent();
								intent = getPackageManager()
										.getLaunchIntentForPackage(
												lstInstalledPkg.get(position)
														.get("pkg").toString());
								startActivity(intent);

							}
						});
					} else {
						Log.i("myapp", "kong");
						tv_tips.setVisibility(View.VISIBLE);
						Log.i("myapp", "poor");
						tv_tips.setText("你的设备上没有安装我公司的应用！");
					}

					break;
				case DATA_POOR:
					tv_tips.setVisibility(View.VISIBLE);
					Log.i("myapp", "poor");
					tv_tips.setText("你的设备上没有安装我公司的应用！");
					break;
				default:
					CommonUtils.DisplayToast(getApplicationContext(),
							(String) msg.obj);
					break;

				}

			} catch (Exception e) {
				// TODO: handle exception
			}

			super.handleMessage(msg);
		}

	};

}
