package com.clw.clwappmarketnew;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.kobjects.base64.Base64;
import org.ksoap2.serialization.SoapObject;

import com.clw.domain.AppInfo;
import com.clw.request.UpdateManager;
import com.clw.service.ImageService;
import com.clw.utils.CommonUtils;
import com.clw.view.MyButton;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class AppDetailActivity extends FragmentActivity {
	private int appId;
	private TextView tvName;
	private RelativeLayout rlMain;
	private TextView top_title;
	private MyButton top_back;
	private ImageView iv_install;
	private Button btn_install;
	private Button btn_install_bottom;
	private ImageView iv_icon;
	private AppInfo appInfo;
	private TextView tvCount;
	private Object obj = null;

	private Object obj_img = null;

	private List<String> lst;
	private List<String> lst_name;
	private static int EVENT_TIME_TO_LOAD_M;
	private String sApkName;
	private String sResource;
	private DownloadApkThread downloadApkThread;
	private UpdateDlCountThread dlCountThread;
	private GetAppDetailThread detailThread;
	private GetImgThread getImgThread;
	private Object objApk;

	private static final int EVENT_DOWNLOAD_APK_SUC = 0;
	private static final int EVENT_DOWNLOAD_APK_SUC_I = 5;
	private static final int EVENT_DOWNLOAD_APK_FAIL = 1;
	private static final int EVENT_GET_APPDETAIL = 2;

	private static final int EVENT_GET_IMGNAME_SUC = 8;
	private static final int EVENT_GET_IMGNAME_FAIL = 9;
	private int curVersion = 0;
	private List<Bitmap> lBm = new ArrayList<Bitmap>();

	private ProgressDialog pdg;

	private String sPicName;

	private ImageView img_em;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.app_detail);
		Bundle bundle = getIntent().getExtras();
		appId = bundle.getInt("appid");
		// Log.i("appId", String.valueOf(appId));
		sResource = bundle.getString("resource");

		String pkgName = "";

		if (sResource.equals("AppListMain")) {
			EVENT_TIME_TO_LOAD_M = 99;

		} else if (sResource.equals("ListRanking")) {
			EVENT_TIME_TO_LOAD_M = 100;

		} else if (sResource.equals("AppSearchFra")) {
			EVENT_TIME_TO_LOAD_M = 101;

		}
		img_em = (ImageView) findViewById(R.id.img_detail_em);

	

		// btn_selected = res.getDrawable(R.drawable.clwapp_m);
		// Log.i("EVENT_TIME_TO_LOAD_M", String.valueOf(EVENT_TIME_TO_LOAD_M));

		appInfo = new AppInfo();
		lst = new ArrayList<String>();
		lst_name = new ArrayList<String>();

		iv_icon = (ImageView) this.findViewById(R.id.img_detail_icon);

		top_title = (TextView) this.findViewById(R.id.id_top_title);
		top_title.setText("应用详细");

		top_title.setTextSize(16);
		top_back = (MyButton) this.findViewById(R.id.id_top_back);
		top_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});

		btn_install = (Button) this.findViewById(R.id.btn_detail_install);
		btn_install_bottom = (Button) this
				.findViewById(R.id.btn_install_bottom);
		btn_install.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				int tmpResult = Integer.parseInt(v.getContentDescription()
						.toString());
				if (tmpResult == 0) {
					Intent intent = new Intent();
					intent = getPackageManager().getLaunchIntentForPackage(
							appInfo.getPkgName());
					startActivity(intent);

				} else {
					if (CommonUtils.isNetWorkConnected(getApplicationContext())) {
						if (pdg != null) {
							pdg.cancel();
							pdg = null;

						}
						// pdg = ProgressDialog.show(AppDetailActivity.this,
						// "正在下载", "请稍后...",false);

						pdg = new ProgressDialog(AppDetailActivity.this,
								ProgressDialog.THEME_HOLO_LIGHT);
						pdg.setTitle("正在下载");
						pdg.setMessage("请稍后...");

						pdg.setCanceledOnTouchOutside(false);
						pdg.setCancelable(true);
						pdg.show();
						if (downloadApkThread != null) {
							downloadApkThread.interrupt();
							downloadApkThread = null;

						}
						downloadApkThread = new DownloadApkThread();
						downloadApkThread.start();

					} else {
						CommonUtils.DisplayToast(getApplicationContext(),
								getString(R.string.net_state_disconnection));

					}
				}
			}
		});

		btn_install_bottom.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				int tmpResult = Integer.parseInt(v.getContentDescription()
						.toString());
				if (tmpResult == 0) {
					Intent intent = new Intent();
					intent = getPackageManager().getLaunchIntentForPackage(
							appInfo.getPkgName());
					startActivity(intent);

				} else {
					if (CommonUtils.isNetWorkConnected(getApplicationContext())) {

						boolean hasUpdate = UpdateManager.getUpdateManager()
								.checkAppUpdate(AppDetailActivity.this, 3,
										appInfo.getAppName());

						if (!hasUpdate) {

							CommonUtils.DisplayToast(AppDetailActivity.this,
									"已是最新版本");
						} else {
							Log.i("12", "sas");
							if (dlCountThread != null) {

								dlCountThread.interrupt();
								dlCountThread = null;
							}
							dlCountThread = new UpdateDlCountThread();
							dlCountThread.start();
						}

					} else {
						CommonUtils.DisplayToast(getApplicationContext(),
								getString(R.string.net_state_disconnection));

					}
				}
			}
		});
		iv_install = (ImageView) this.findViewById(R.id.img_detail_install);

		iv_install.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				int tmpResult = Integer.parseInt(v.getContentDescription()
						.toString());
				if (tmpResult == 0) {
					Intent intent = new Intent();
					intent = getPackageManager().getLaunchIntentForPackage(
							appInfo.getPkgName());
					startActivity(intent);

				} else {
					if (CommonUtils.isNetWorkConnected(getApplicationContext())) {
						if (pdg != null) {
							pdg.cancel();
							pdg = null;

						}
						// pdg = ProgressDialog.show(AppDetailActivity.this,
						// "正在下载", "请稍后...",false);

						pdg = new ProgressDialog(AppDetailActivity.this,
								ProgressDialog.THEME_HOLO_LIGHT);
						pdg.setTitle("正在下载");
						pdg.setMessage("请稍后...");

						pdg.setCanceledOnTouchOutside(false);
						pdg.setCancelable(true);
						pdg.show();
						if (downloadApkThread != null) {
							downloadApkThread.interrupt();
							downloadApkThread = null;

						}
						downloadApkThread = new DownloadApkThread();
						downloadApkThread.start();

					} else {
						CommonUtils.DisplayToast(getApplicationContext(),
								getString(R.string.net_state_disconnection));

					}
				}
				// else if (tmpResult==2){if (tmpResult==1)
				//
				//
				// }

			}
		});
		tvCount = (TextView) this.findViewById(R.id.tv_detail_dlcount);

		tvName = (TextView) this.findViewById(R.id.tv_detail_appname);
		tvName.setContentDescription(String.valueOf(appId));
		rlMain = (RelativeLayout) this.findViewById(R.id.relativeLayout1);

		Message message = mHandler.obtainMessage(EVENT_TIME_TO_LOAD_M);
		mHandler.sendMessage(message);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		appId = Integer.parseInt(tvName.getContentDescription().toString());
		if (CommonUtils.isNetWorkConnected(getApplicationContext())) {
			if (detailThread != null) {

				detailThread.interrupt();
				detailThread = null;
			}
			detailThread = new GetAppDetailThread();
			detailThread.start();
		} else {
			CommonUtils.DisplayToast(getApplicationContext(),
					getString(R.string.net_state_disconnection));

		}
		super.onResume();

	}

	private void addView(AppInfo app, List<Bitmap> lBm) {

		LayoutInflater inflater = (LayoutInflater) this
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		final View item = (View) inflater.inflate(R.layout.app_detail_info,
				null);
		TextView tvContent = (TextView) item
				.findViewById(R.id.tv_deinfo_content_main);
		tvContent.setText(app.getDetailIntroduce());
		LinearLayout lay_pic = (LinearLayout) item
				.findViewById(R.id.lay_detail_pic);

		TextView tvNewFun = (TextView) item.findViewById(R.id.tv_info_newfun);
		tvNewFun.setText(app.getAppnewfun());
		TextView tvCom = (TextView) item.findViewById(R.id.tv_info_devcom);
		tvCom.setText(app.getDevcompany());
		TextView tvPeo = (TextView) item.findViewById(R.id.tv_info_devpeo);
		tvPeo.setText(app.getDeveloper());
		TextView tvLb = (TextView) item.findViewById(R.id.tv_info_lb);
		tvLb.setText(app.getKind());
		TextView tvLastTime = (TextView) item
				.findViewById(R.id.tv_info_lasttime);

		tvLastTime.setText(app.getsModifyTime().subSequence(0, 10));

		TextView tvVersion = (TextView) item.findViewById(R.id.tv_info_version);
		tvVersion.setText(app.getVersionname());
		TextView tvSize = (TextView) item.findViewById(R.id.tv_info_size);
		tvSize.setText(app.getSize());
		TextView tvJrx = (TextView) item.findViewById(R.id.tv_info_jrx);
		tvJrx.setText(app.getCompatibility());
		// 增加应用介绍图片
		lay_pic.removeAllViews();
		for (Bitmap bitmap : lBm) {
			lay_pic.addView(insertImage(bitmap));

		}
		rlMain.removeAllViews();
		rlMain.addView(item);
	}

	private View insertImage(Bitmap zoomImage) {
		// TODO Auto-generated method stub
		LinearLayout layout = new LinearLayout(this);
		layout.setLayoutParams(new LayoutParams(370, 650));
		layout.setGravity(Gravity.CENTER);

		ImageView imageView = new ImageView(this);
		imageView.setLayoutParams(new LayoutParams(360, 640));

		imageView.setImageBitmap(zoomImage);
		// 增加放大图片（启动一个intent显示全屏图片）

		layout.addView(imageView);
		return layout;
	}

	private View insertImage(Integer id) {
		LinearLayout layout = new LinearLayout(this);
		layout.setLayoutParams(new LayoutParams(220, 280));
		layout.setGravity(Gravity.CENTER);

		ImageView imageView = new ImageView(this);
		imageView.setLayoutParams(new LayoutParams(200, 280));
		imageView.setBackgroundResource(id);
		// imageView.setImageBitmap(bm);
		layout.addView(imageView);
		return layout;
	}

	private Bitmap getBitmap(int id) {
		Bitmap originalBitmap = BitmapFactory
				.decodeResource(getResources(), id);
		int originalWidth = originalBitmap.getWidth();
		int originalHeight = originalBitmap.getHeight();
		int newWidth = 48;
		int newHeight = 140;

		float scale = ((float) newHeight) / originalHeight;
		Matrix matrix = new Matrix();
		matrix.postScale(scale, scale);
		Bitmap changedBitmap = Bitmap.createBitmap(originalBitmap, 0, 0,
				originalWidth, originalHeight, matrix, true);

		return changedBitmap;

	}

	private Handler mHandler = new Handler() {

		public void handleMessage(Message msg) {

			switch (msg.what) {
			case EVENT_DOWNLOAD_APK_FAIL:
				if (pdg != null) {
					pdg.cancel();
					pdg = null;

				}
				CommonUtils.tostx(getApplicationContext(), msg.obj.toString());
				break;
			case EVENT_DOWNLOAD_APK_SUC:
				if (dlCountThread != null) {

					dlCountThread.interrupt();
					dlCountThread = null;
				}
				dlCountThread = new UpdateDlCountThread();
				dlCountThread.start();

				byte[] ops = Base64.decode(((SoapObject) objApk).getProperty(0)
						.toString());
				// Log.i("ops", ((SoapObject)
				// objApk).getProperty(0).toString());
				try {
					File file = null;
					if (CommonUtils.isExitsSdcard()) {
						CommonUtils.decoderBase64File(((SoapObject) objApk)
								.getProperty(0).toString(), "/mnt/sdcard/"
								+ sApkName + ".apk");

						file = new File("/mnt/sdcard/" + sApkName + ".apk");
					} else {
						CommonUtils.decoderBase64File(((SoapObject) objApk)
								.getProperty(0).toString(), sApkName + ".apk");

						file = new File(sApkName + ".apk");

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
				if (pdg != null) {
					pdg.cancel();
					pdg = null;

				}
				break;
			case EVENT_GET_APPDETAIL:
				SoapObject tmpObj = CommonUtils.analysis((SoapObject) obj,
						"getAppDetail");

				SoapObject childObj = (SoapObject) tmpObj.getProperty(0);

				appInfo.setAppId(appId);
				try {
					appInfo.setAppName(childObj.getProperty("appname")
							.toString());
				} catch (Exception e) {
					// TODO: handle exception
					appInfo.setAppName("");
				}
				String base64 = "";
				try {
					base64 = childObj.getProperty("appicon").toString();
				} catch (Exception e) {
					// TODO: handle exception
				}

				if (!base64.equals("") || base64 != "") {
					appInfo.setAppIcon(CommonUtils.zoomImage(
							CommonUtils.stringtoBitmap(base64), 80, 80));
					// Log.i("base64", "base64 not null");
				} else {
					appInfo.setAppIcon(CommonUtils.zoomImage(
							getBitmap(R.drawable.noicon), 80, 80));

				}

				try {
					appInfo.setVersion(childObj.getProperty("version")
							.toString());

					curVersion = Integer.parseInt(childObj.getProperty(
							"version").toString());
				} catch (Exception e) {
					// TODO: handle exception
					appInfo.setVersion("");
				}
				try {
					appInfo.setKind(childObj.getProperty("kind").toString());
				} catch (Exception e) {
					// TODO: handle exception
					appInfo.setKind("");
				}

				try {
					appInfo.setPkgName(childObj.getProperty("pkgname")
							.toString());
				} catch (Exception e) {
					// TODO: handle exception
					appInfo.setPkgName("");
				}
				try {
					appInfo.setDetailIntroduce(childObj.getProperty(
							"detailIntroduce").toString());
				} catch (Exception e) {
					// TODO: handle exception
					appInfo.setDetailIntroduce("暂无");
				}
				try {
					appInfo.setAppActivity(childObj.getProperty("appActivity")
							.toString());
				} catch (Exception e) {
					// TODO: handle exception
					appInfo.setAppActivity("暂无");
				}
				try {
					appInfo.setsAddTime(childObj.getProperty("addtime")
							.toString());
				} catch (Exception e) {
					// TODO: handle exception
					appInfo.setsAddTime(CommonUtils.getSysDate());
				}
				try {
					appInfo.setsModifyTime(childObj.getProperty("modifytime")
							.toString());
				} catch (Exception e) {
					// TODO: handle exception
					appInfo.setsModifyTime(CommonUtils.getSysDate());
				}

				try {
					appInfo.setAppcontent(childObj.getProperty("appcontent")
							.toString());
				} catch (Exception e) {
					// TODO: handle exception
					appInfo.setAppcontent("暂无");
				}
				try {
					appInfo.setAppnewfun(childObj.getProperty("appnewfun")
							.toString());
				} catch (Exception e) {
					// TODO: handle exception
					appInfo.setAppnewfun("暂无");
				}
				try {
					appInfo.setDevcompany(childObj.getProperty("devcompany")
							.toString());
				} catch (Exception e) {
					// TODO: handle exception
					appInfo.setDevcompany("暂无");
				}
				try {
					appInfo.setDeveloper(childObj.getProperty("developer")
							.toString());
				} catch (Exception e) {
					// TODO: handle exception
					appInfo.setDeveloper("暂无");
				}
				try {
					appInfo.setCompatibility(childObj.getProperty(
							"compatibility").toString());
				} catch (Exception e) {
					// TODO: handle exception
					appInfo.setCompatibility("暂无");
				}
				try {
					appInfo.setSize(childObj.getProperty("size").toString());
				} catch (Exception e) {
					// TODO: handle exception
					appInfo.setSize("暂无");
				}
				try {
					appInfo.setVersionname(childObj.getProperty("versionname")
							.toString());
				} catch (Exception e) {
					// TODO: handle exception
					appInfo.setVersionname("暂无");
				}
				try {
					appInfo.setDownloadCount(childObj.getProperty("dlcount")
							.toString());
				} catch (Exception e) {
					// TODO: handle exception
					appInfo.setDownloadCount(String.valueOf(0));
				}

				SoapObject tmpObj_img = CommonUtils.analysis(
						(SoapObject) obj_img, "getAppImg");

				// Log.i("obj_img", obj_img.toString());
				lBm.clear();
				lst_name.clear();

				if (tmpObj_img != null) {
					for (int i = 0; i < tmpObj_img.getPropertyCount(); i++) {
						SoapObject temObj = (SoapObject) tmpObj_img
								.getProperty(i);

						// String base64_img = "";
						// try {
						// base64_img = temObj.getProperty("img").toString();
						// } catch (Exception e) {
						// // TODO: handle exception
						//
						// }
						//
						// if (!base64_img.equals("") && base64_img != "") {
						// lBm.add(CommonUtils.zoomImage(
						// CommonUtils.stringtoBitmap(base64_img),
						// 360, 640));
						//
						// // Log.i("base64", "base64 not null");
						// }
						try {
							lst_name.add(temObj.getProperty("imgname")
									.toString());
						} catch (Exception e) {
							// TODO: handle exception
						}
					}
					if (lst_name.size() > 0) {

						if (getImgThread != null) {
							getImgThread.interrupt();
							getImgThread = null;

						}
						getImgThread = new GetImgThread();
						getImgThread.start();

						//
					} else {

						lBm.add(CommonUtils.zoomImage(
								getBitmap(R.drawable.nopic), 360, 640));

						CommonUtils.SendMsg(mHandler, EVENT_GET_IMGNAME_FAIL,
								"获取失败", 0);
					}

				} else {
					Log.i("lbm", "null");
					lBm.add(CommonUtils.zoomImage(getBitmap(R.drawable.nopic),
							360, 640));

					CommonUtils.SendMsg(mHandler, EVENT_GET_IMGNAME_FAIL,
							"获取失败", 0);

				}

				// addView(appInfo, lBm);

				int doResult = 0;
				doResult = CommonUtils.doType(getApplicationContext(),
						appInfo.getPkgName(), curVersion);

				if (doResult == 0) {
					iv_install.setImageResource(R.drawable.open);
					btn_install.setText("打开");
					btn_install_bottom.setText("打开");

				} else if (doResult == 1) {
					iv_install.setImageResource(R.drawable.install);
					btn_install.setText("下载");
					btn_install_bottom.setText("下载");

				} else if (doResult == 2) {
					iv_install.setImageResource(R.drawable.update);
					btn_install.setText("更新");
					btn_install_bottom.setText("更新");

				}
				btn_install_bottom.setVisibility(View.VISIBLE);
				// iv_install.setContentDescription(String.valueOf(doResult));
				btn_install.setContentDescription(String.valueOf(doResult));
				btn_install_bottom.setContentDescription(String
						.valueOf(doResult));

				tvCount.setText("下载量：" + appInfo.getDownloadCount());
				tvName.setText(appInfo.getAppName());
				iv_icon.setImageBitmap(CommonUtils.zoomImage(
						appInfo.getAppIcon(), 120, 120));
				sApkName = appInfo.getAppName();
				
				
				Resources res = getResources();
				
				Bitmap bm = null;
				
				if (appInfo.getAppName().equals("丽水公交")){
					bm=BitmapFactory.decodeResource(res, R.drawable.lsgj_em);
					
				}
				if (appInfo.getAppName().equals("i停车")){
					bm=BitmapFactory.decodeResource(res, R.drawable.itc_em);
					
				}
				if (appInfo.getAppName().equals("众泰车联网")){
					bm=BitmapFactory.decodeResource(res, R.drawable.ztclw_em);
					
				}
				if (appInfo.getAppName().equals("智慧车联网")){
					bm=BitmapFactory.decodeResource(res, R.drawable.zhclw_em);
					
				}
				if (appInfo.getAppName().equals("禾行通")){
					bm=BitmapFactory.decodeResource(res, R.drawable.hxt_em);
					
				}
				
				
				if (bm!=null){
				img_em.setImageBitmap(CommonUtils.zoomImage(bm, 120, 120));
				}
				
				break;
			case EVENT_GET_IMGNAME_SUC:
				addView(appInfo, lBm);
				break;
			case EVENT_GET_IMGNAME_FAIL:
				addView(appInfo, lBm);
				break;
			case EVENT_DOWNLOAD_APK_SUC_I:
				if (dlCountThread != null) {

					dlCountThread.interrupt();
					dlCountThread = null;
				}
				dlCountThread = new UpdateDlCountThread();
				dlCountThread.start();
				break;
			case 99:
				// AppListMain.closeProgressDialog();
				break;
			case 100:
				ListRankingFragmet.closeProgressDialog();
				break;
			case 101:
				SearchActivity.closeProgressDialog();
				break;
			}

		}

	};

	private class DownloadApkThread extends Thread {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			try {
				DownloadApk();
			} catch (Exception e) {
				// TODO: handle exception
				Log.i("ApkThreadex", e.getMessage());
			}

		}

	}

	public void DownloadApk() {
		List<String> lstPra = new ArrayList<String>();

		lstPra.add("strFilePath");
		lstPra.add(sApkName + ".apk");
		// Log.i("apkobj","android_gridView.apk");
		// lstPra.add("android_gridView.apk");
		objApk = CommonUtils.GetWebserviceData("downloadFile", lstPra);
		// Log.i("apkobj",objApk.toString());

		Log.i("apkobj_count",
				String.valueOf(((SoapObject) objApk).getPropertyCount()));
		SoapObject sObjApk = null;
		try {
			sObjApk = (SoapObject) ((SoapObject) objApk)
					.getProperty("downloadFileResult");

			if (sObjApk.equals("anyType[]")) {

				CommonUtils.SendMsg(mHandler, EVENT_DOWNLOAD_APK_FAIL,
						getString(R.string.str_download_fail), 0);
			} else {

				CommonUtils.SendMsg(mHandler, EVENT_DOWNLOAD_APK_SUC,
						getString(R.string.str_download_suc), 0);
			}

		} catch (Exception e) {
			// TODO: handle exception
			CommonUtils.SendMsg(mHandler, EVENT_DOWNLOAD_APK_SUC,
					getString(R.string.str_download_suc), 0);
		}

	}

	private class UpdateDlCountThread extends Thread {
		@Override
		public void run() {
			// TODO Auto-generated method stub
			try {
				updateDlCount();
			} catch (Exception e) {
				// TODO: handle exception

			}

		}

	}

	public void updateDlCount() {
		List<String> ls1 = new ArrayList<String>();
		ls1.add("appId");
		ls1.add(String.valueOf(appInfo.getAppId()));
		Object obj1 = CommonUtils.GetWebserviceData("updateDlmes", ls1);
		// Log.i("obj1", String.valueOf(appInfo.getAppId()));
		// Log.i("obj1", obj1.toString());

	}

	private class GetAppDetailThread extends Thread {
		@Override
		public void run() {
			// TODO Auto-generated method stub
			try {
				getAppDetail();
			} catch (Exception e) {
				// TODO: handle exception
			}
		}

	}

	public void getAppDetail() {

		lst.clear();
		obj = null;
		// Log.i("appid", String.valueOf(appId));
		lst.add("appId");
		// String strsql =
		// "SELECT  a.appid,a.appname,a.appicon,a.version,a.kind,a.pkgname,a.detailIntroduce,a.appActivity,a.addtime,a.modifytime,a.appcontent,"
		// +
		// "a.appnewfun,a.devcompany,a.developer,a.versionname,a.size,a.compatibility,b.dlcount  FROM t_appinfo a left join  t_dlmes b on a.appid=b.appid where a.appid="
		// + String.valueOf(appId);
		lst.add(String.valueOf(appId));

		obj = CommonUtils.GetWebserviceData("getAppDetail", lst);

		lst.clear();
		lst.add("appId");
		// lst.add("SELECT img,appid FROM T_imglist  where sfsy ='0' and appid ="
		// + String.valueOf(appId) + " order by appid");
		lst.add(String.valueOf(appId));
		obj_img = CommonUtils.GetWebserviceData("getAppImg", lst);

		CommonUtils.SendMsg(mHandler, EVENT_GET_APPDETAIL, "获取成功", 0);

	}

	private class GetImgThread extends Thread {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			GetImg();
		}
	}

	public void GetImg() {

		String storageState = Environment.getExternalStorageState();
		if (storageState.equals(Environment.MEDIA_MOUNTED)) {
			String savePath = Environment.getExternalStorageDirectory()
					.getAbsolutePath() + "/clwmarketdownload/";
			File file = new File(savePath);
			if (!file.exists()) {
				file.mkdirs();
			}

			for (int i = 0; i < lst_name.size(); i++) {

				try {
					File f = new File(savePath + "/" + lst_name.get(i)
							+ ".jpeg");
					if (f.exists()) {

						// f.delete();

						Bitmap bitmap1 = BitmapFactory.decodeFile(savePath
								+ "/" + lst_name.get(i) + ".jpeg");

						lBm.add(CommonUtils.zoomImage(bitmap1, 360, 640));
						continue;
					}
					String urlPathContent = CommonUtils.sDownloadURL + "/"
							+ lst_name.get(i) + ".jpeg";
					byte[] data = ImageService.getImage(urlPathContent);
					Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0,
							data.length);
					FileOutputStream out = null;
					try {
						try {
							out = new FileOutputStream(f);
						} catch (FileNotFoundException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

						bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
					} finally {
						if (out != null) {

							out.close();

						}

					}
					lBm.add(CommonUtils.zoomImage(bitmap, 360, 640));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

		} else {
			for (int i = 0; i < lst_name.size(); i++) {
				try {

					String urlPathContent = CommonUtils.sDownloadURL + "/"
							+ lst_name.get(i) + ".jpeg";
					byte[] data = ImageService.getImage(urlPathContent);

					Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0,
							data.length);

					lBm.add(CommonUtils.zoomImage(bitmap, 360, 640));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}

		CommonUtils.SendMsg(mHandler, EVENT_GET_IMGNAME_SUC, "获取图片成功", 0);

	}

}
