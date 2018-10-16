package com.clw.clwappmarketnew;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.kobjects.base64.Base64;
import org.ksoap2.serialization.SoapObject;

import com.clw.data.PrefsHelper;
import com.clw.domain.UserInfo;
import com.clw.request.UpdateManager;
import com.clw.utils.CommonUtils;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class MineMainFra extends Fragment {
	private View mMainView;
	private TextView tv;
	private List<String> lst = null;
	private Object oResult;
	private List<UserInfo> lstUser = null;
	private String b64str;
	private Button btn_login;
	private Button btn_myapp;
	private Button btn_syscon;
	private Button btn_update;
	private Button btn_about;
	private Object obj;
	private static final int GET_VERSION_SUCC = 0;

	private static final int DOWN_SUCC = 1;

	private static final int DOWN_FAILURE = 2;
	private CheckVersionThread versionThread;
	private DownApkThread downApkThread;

	private ProgressDialog pdg;

	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		LayoutInflater inflater=getActivity().getLayoutInflater();
		mMainView=inflater.inflate(R.layout.mine_main, (ViewGroup)getActivity().findViewById(R.id.vp_main), false);
		
	}
	// ClwAppMarket
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		btn_about = (Button) getView().findViewById(R.id.btn_mine_about);
		lst = new ArrayList<String>();

		btn_about.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(getActivity(), AboutActivity.class);
				startActivity(i);
			}
		});

		btn_login = (Button) getView().findViewById(R.id.btn_mine_login);
		btn_login.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String sPhone = PrefsHelper.get().loadString("clwphone", "");
				if (sPhone.length() > 0) {
					Intent intent = new Intent(getActivity(),
							UserDetailActivity.class);

					startActivity(intent);

				} else {
					Intent intent = new Intent(getActivity(),
							LoginActivity.class);

					startActivity(intent);

				}

			}
		});

		btn_myapp = (Button) getView().findViewById(R.id.btn_mine_myapp);
		btn_myapp.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(getActivity(), MyAppActivity.class);
				startActivity(i);

			}
		});

		btn_update = (Button) getView().findViewById(R.id.btn_mine_update);
		btn_update.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				if (CommonUtils.isNetWorkConnected(getView().getContext())) {
					if (versionThread != null) {

						versionThread.interrupt();
						versionThread = null;
					}
					versionThread = new CheckVersionThread();
					versionThread.start();

				} else {

					CommonUtils.DisplayToast(getView().getContext(),
							"无网络，请检查网络状态！");
				}

			}
		});
		btn_syscon = (Button) getView().findViewById(R.id.btn_mine_syscon);
		btn_syscon.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(getActivity(), SettingActivity.class);
				startActivity(i);

			}
		});
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		ViewGroup v=(ViewGroup)mMainView.getParent();
		if (v!=null){
			v.removeAllViewsInLayout();
			
		}
		return mMainView;
		//return inflater.inflate(R.layout.mine_main, container, false);
		// return super.onCreateView(inflater, container, savedInstanceState);
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		String sPhone = PrefsHelper.get().loadString("clwphone", "");
		String sEmail = PrefsHelper.get().loadString("clwemail", "");
		String sUserName = PrefsHelper.get().loadString("clwusername", "");

		if (sPhone.length() > 0) {
			String sex = PrefsHelper.get().loadString("clwsex", "男");
			if (sex.equals("男")) {
				Drawable dr = getResources().getDrawable(R.drawable.icon_man);
				dr.setBounds(0, 0, dr.getMinimumWidth(), dr.getMinimumHeight());
				btn_login.setCompoundDrawables(dr, null, null, null);

			} else {
				Drawable dr = getResources().getDrawable(R.drawable.icon_woman);
				dr.setBounds(0, 0, dr.getMinimumWidth(), dr.getMinimumHeight());
				btn_login.setCompoundDrawables(dr, null, null, null);

			}

		}
		if (sUserName.length() > 0) {

			btn_login.setText(sUserName);
		} else {
			btn_login.setText("未登录");
		}

		//Log.i("resume", "WwW");
		// Log.i("resume", sUserName);

		super.onResume();
	}

	private class CheckVersionThread extends Thread {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			try {
				checkVersion();
			} catch (Exception e) {
				// TODO: handle exception
			}
		}

	}

	public void checkVersion() {

		List<String> lst = new ArrayList<String>();
		lst.add("ss");
		lst.add("1");
		obj = CommonUtils.GetWebserviceData("getVersion", lst);
		//Log.i("update", obj.toString());
if (obj!=null){
		CommonUtils.SendMsg(handler, GET_VERSION_SUCC, "获取数据成功", 0);
}
	}

	Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub

			switch (msg.what) {
			case GET_VERSION_SUCC:
				int serverVersion=0;
				serverVersion= Integer.parseInt(((SoapObject) obj)
						.getProperty("getVersionResult").toString());

				boolean hasUpdate=UpdateManager.getUpdateManager().checkAppUpdate(getActivity(), serverVersion,"ClwAppMarket");
				
				if (!hasUpdate){
					
					CommonUtils.DisplayToast(getActivity(), "已是最新版本");
				}
//				int version = 0;
//				try {
//					version = getActivity()
//							.getApplicationContext()
//							.getPackageManager()
//							.getPackageInfo(
//									getActivity().getApplicationContext()
//											.getPackageName(), 0).versionCode;
//				} catch (NameNotFoundException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//				if (serverVersion > version) {
//					// 待增加下载源码
//					AlertDialog.Builder builder = new Builder(getView().getContext());
//					builder.setMessage("有新版本，确认下载吗？");
//					builder.setTitle("提示");
//					builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
//						
//						public void onClick(DialogInterface dialog, int which) {
//							// TODO Auto-generated method stub
//							dialog.dismiss();
////							if (pdg != null) {
////								pdg.cancel();
////								pdg = null;
////
////							}
////							// pdg = ProgressDialog.show(getView().getContext(), "正在下载",
////							// "请稍后...", false);
////							pdg = new ProgressDialog(getActivity(),
////									ProgressDialog.THEME_HOLO_LIGHT);
////							pdg.setTitle("下载");
////							pdg.setMessage("正在下载，请稍等...");
////						
////							pdg.setCanceledOnTouchOutside(false);
////							pdg.setCancelable(true);
////							pdg.show();
////
////							if (downApkThread != null) {
////								downApkThread.interrupt();
////								downApkThread = null;
////
////							}
////
////							downApkThread = new DownApkThread();
////							downApkThread.start();
//							
//							
//							
//							
//							
//							
//							
//						}
//
//						
//					
//					});
//					
//					builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
//						
//						public void onClick(DialogInterface dialog, int which) {
//							// TODO Auto-generated method stub
//							dialog.dismiss();
//						}
//					});
//					builder.create().show();
//					
//
//				} else {
//					CommonUtils.DisplayToast(getActivity(), "已是最新版本，无需更新！");
//
//				}

				break;

			case DOWN_SUCC:
				byte[] ops = Base64.decode(((SoapObject) oResult)
						.getProperty(0).toString());
				// Log.i("ops", ((SoapObject)
				// oResult).getProperty(0).toString());
				try {
					File file = null;
					if (CommonUtils.isExitsSdcard()) {
//						File dir = Environment.getExternalStorageDirectory();
//						String path = dir.getPath() + "/ClwMarket";
//						Log.i("dir", path);
//						File file_dir = new File(path);
//						if (!file_dir.exists()) {
//							dir.mkdir();
//						}

						
						CommonUtils.decoderBase64File(((SoapObject) oResult)
								.getProperty(0).toString(),
								"mnt/sdcard/ClwAppMarket.apk");

						file = new File(
								"mnt/sdcard/ClwAppMarket.apk");
					} else {
						CommonUtils.decoderBase64File(((SoapObject) oResult)
								.getProperty(0).toString(), "ClwAppMarket.apk");

						file = new File("ClwAppMarket.apk");

					}
					if (pdg != null) {
						pdg.cancel();
						pdg = null;

					}

					Intent intent = new Intent();

					intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					intent.setAction(android.content.Intent.ACTION_VIEW);
					intent.setDataAndType(Uri.fromFile(file),
							"application/vnd.android.package-archive");
					startActivity(intent);
					// file.delete();

				} catch (Exception e) {
					// TODO Auto-generated catch block

					e.printStackTrace();

				}

				break;

			default:
				break;
			}

			super.handleMessage(msg);
		}

	};

	private class DownApkThread extends Thread {
		@Override
		public void run() {
			// TODO Auto-generated method stub
			try {
				DownApk();
			} catch (Exception e) {
				// TODO: handle exception
				CommonUtils.SendMsg(handler, DOWN_FAILURE, "下载失败", 0);
			}
		}

	}

	public void DownApk() {
		List<String> lst = new ArrayList<String>();
		lst.add("strFilePath");
		lst.add("ClwAppMarket.apk");
		
		//lst.add("8061.apk");
		oResult = CommonUtils.GetWebserviceData("downloadFile", lst);


		CommonUtils.SendMsg(handler, DOWN_SUCC, "下载数据成功", 0);

	}

}
