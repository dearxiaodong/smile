package com.clw.clwappmarketnew;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.ksoap2.serialization.SoapObject;

import com.clw.request.UpdateManager;
import com.clw.utils.CommonUtils;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.SimpleAdapter.ViewBinder;

public class MyAppFra extends Fragment {
	private View mMainView;

	private static final int GET_VERSION_SUCC = 0;
	private Object obj = null;
	private GetAllAppThread appThread;
	// 服务端取的公司applist
	private List<String> lstPkg = null;
	private List<String> lstPra = null;
	private ArrayList<HashMap<String, Object>> lstInstalledPkg = null;
	private static final int DATA_FULL = 1;
	private static final int DATA_POOR = 2;
	private GridView gv;
	private TextView tv_tips;
	private Object obj_app = null;

	private CheckVersionThread versionThread;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		LayoutInflater inflater = getActivity().getLayoutInflater();
		mMainView = inflater.inflate(R.layout.fram_myapp,
				(ViewGroup) getActivity().findViewById(R.id.vp_main), false);

	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);

		gv = (GridView) getView().findViewById(R.id.gv_myapp);
		tv_tips = (TextView) getView().findViewById(R.id.tv_tips);

		lstPkg = new ArrayList<String>();
		lstPra = new ArrayList<String>();
		lstInstalledPkg = new ArrayList<HashMap<String, Object>>();

		tv_tips.setVisibility(View.GONE);

		// Log.i("myapp", "线程before");

		// Log.i("myapp", "线程after");
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		if (obj == null) {
			if (versionThread != null) {

				versionThread.interrupt();
				versionThread = null;
			}
			versionThread = new CheckVersionThread();
			versionThread.start();
		}
		if (obj_app == null) {
			if (appThread != null) {

				appThread.interrupt();
				appThread = null;
			}

			appThread = new GetAllAppThread();
			appThread.start();
		}
		super.onResume();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		// return super.onCreateView(inflater, container, savedInstanceState);

		ViewGroup v = (ViewGroup) mMainView.getParent();
		if (v != null) {
			v.removeAllViewsInLayout();

		}
		return mMainView;
		// return inflater.inflate(R.layout.fram_myapp, container, false);

	}

	private class GetAllAppThread extends Thread {
		@Override
		public void run() {
			// TODO Auto-generated method stub
			try {
				// Log.i("myapp", "线程ing1");
				getAllApp();
			} catch (Exception e) {
				// TODO: handle exception
				// Log.i("e", e.getMessage());
			}
		}
	}

	public void getAllApp() {
		// Log.i("myapp", "线程ing2");
		lstPra.add("strsql");
		lstPra.add("2");

		obj_app = CommonUtils.GetWebserviceData("getPkgName", lstPra);
		// Log.i("myapp", obj_app.toString());
		CommonUtils.SendMsg(handler, DATA_FULL, getString(R.string.str_prompt)
				.toString(), 0);

	}

	final Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			try {
				switch (msg.what) {
				case GET_VERSION_SUCC:
					int serverVersion = 0;
					serverVersion = Integer.parseInt(((SoapObject) obj)
							.getProperty("getVersionResult").toString());

					boolean hasUpdate = UpdateManager.getUpdateManager()
							.checkAppUpdate(getActivity(), serverVersion,
									"ClwAppMarket");

					// if (!hasUpdate){
					//
					// CommonUtils.DisplayToast(getActivity(), "已是最新版本");
					// }

					break;
				case DATA_FULL:

					SoapObject tmpObj = CommonUtils.analysis(
							(SoapObject) obj_app, "getPkgName");

					if (tmpObj != null) {
						// Log.i("myapp", tmpObj.toString());

						// String.valueOf(tmpObj.getPropertyCount()));
						for (int i = 0; i < tmpObj.getPropertyCount(); i++) {
							SoapObject tObj = (SoapObject) tmpObj
									.getProperty(i);
							lstPkg.add(tObj.getProperty("pkgname").toString());
							// Log.i("myapp", tObj.getProperty("pkgname")
							// .toString());
						}
					}

					PackageManager pm = getActivity().getPackageManager();
					lstInstalledPkg.clear();
					List<PackageInfo> pakageinfos = pm
							.getInstalledPackages(PackageManager.GET_UNINSTALLED_PACKAGES);
					for (PackageInfo pi : pakageinfos) {

						if (lstPkg.size() > 0
								&& lstPkg.contains(pi.packageName)) {
							// Log.i("myapp", "lstpkg full");
							HashMap<String, Object> map = new HashMap<String, Object>();
							// lstInstalledPkg.add(pi.packageName);pi.applicationInfo.loadIcon(getPackageManager())
							BitmapDrawable bd = (BitmapDrawable) pi.applicationInfo
									.loadIcon(pm);

							map.put("appIcon", CommonUtils.zoomImage(
									bd.getBitmap(), 150, 150));
							map.put("appName", pi.applicationInfo.loadLabel(pm));
							map.put("pkg", pi.packageName);

							lstInstalledPkg.add(map);
						} else {
							// Log.i("myapp", "lstpkg null");

						}

					}

					// Log.i("myapp", "ss");
					if (lstInstalledPkg.size() > 0) {
						// Log.i("myapp", lstInstalledPkg.toString());
						tv_tips.setVisibility(View.GONE);
						SimpleAdapter sp = new SimpleAdapter(getView()
								.getContext(), lstInstalledPkg,
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
									// Log.i("tv", data.toString());
									// Log.i("tv", (String) data);
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
								intent = getActivity().getPackageManager()
										.getLaunchIntentForPackage(
												lstInstalledPkg.get(position)
														.get("pkg").toString());
								startActivity(intent);

							}
						});
					} else {
						// Log.i("myapp", "kong");
						tv_tips.setVisibility(View.VISIBLE);
						// Log.i("myapp", "poor");
						// tv_tips.setText("你的设备上没有安装我公司的应用！");
					}

					// 临时插入图标
					// Runnable Refe = new Runnable() {
					//
					// @Override
					// public void run() {
					// // TODO Auto-generated method stub
					// for (int i = 0; i < lstInstalledPkg.size(); i++) {
					// List<String> tmpLis = new ArrayList<String>();
					// String sId="";
					//
					// if (lstInstalledPkg.get(i).get("pkg")
					// .toString()
					// .equals("com.astrob.lishuitransit")) {
					// sId="1";
					// }
					// if (lstInstalledPkg.get(i).get("pkg")
					// .toString()
					// .equals("hoperun.zotye.app.android")) {
					// sId="2";
					// }
					// if (lstInstalledPkg.get(i).get("pkg")
					// .toString()
					// .equals("com.teewoo.iparking")) {
					// sId="3";
					// }
					// if (lstInstalledPkg.get(i).get("pkg")
					// .toString()
					// .equals("com.example.smarttraffic")) {
					// sId="4";
					// }
					// if (lstInstalledPkg.get(i).get("pkg")
					// .toString()
					// .equals("com.hoperun.bj.sr.android")) {
					// sId="5";
					// }
					//
					// if (!sId.equals("")){
					// Log.i("pkg",lstInstalledPkg.get(i).get("pkg")
					// .toString() );
					// Bitmap bm = (Bitmap) lstInstalledPkg
					// .get(i).get("appIcon");
					// tmpLis.add("id");
					// tmpLis.add(sId);
					// tmpLis.add("img");
					// tmpLis.add(CommonUtils
					// .bitmaptoString(bm));
					// try {
					// Object ob = CommonUtils
					// .GetWebserviceData(
					// "update_appico",
					// tmpLis);
					//
					// } catch (Exception e) {
					// // TODO: handle exception
					// }
					// }
					//
					// }
					// }
					//
					// };
					// new Thread(Refe).start();
					break;
				case DATA_POOR:
					tv_tips.setVisibility(View.VISIBLE);
					// Log.i("myapp", "poor");
					// tv_tips.setText("你的设备上没有安装我公司的应用！");
					break;

				default:
					CommonUtils.DisplayToast(getView().getContext(),
							(String) msg.obj);
					break;

				}

			} catch (Exception e) {
				// TODO: handle exception
			}

			super.handleMessage(msg);
		}

	};

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
		// Log.i("update", obj.toString());
		if (obj != null) {
			CommonUtils.SendMsg(handler, GET_VERSION_SUCC, "获取数据成功", 0);
		}
	}

}
