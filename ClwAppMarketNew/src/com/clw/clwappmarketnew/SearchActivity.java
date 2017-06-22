package com.clw.clwappmarketnew;

import java.util.ArrayList;
import java.util.List;

import org.ksoap2.serialization.SoapObject;

import com.clw.domain.AppInfo;
import com.clw.utils.CommonUtils;
import com.clw.view.MyButton;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class SearchActivity extends Activity {

	private GetDataThread getDataThread = null;
	private GetAppNameThread getAppNameThread = null;
	private String[] ar_app_name = null;
	private String[] ar_appid = null;
	private List<String> lstPra = new ArrayList<String>();

	private List<String> lstName = new ArrayList<String>();

	private AutoCompleteTextView actv;
	private ImageView img_delte;
	private ImageView img_do;
	private static final int GETNAME_SUCCESS = 0;
	private static final int GETNAME_FAILURE = 1;

	private static final int GETAPP_SUCCESS = 2;
	private static final int GETAPP_FAILURE = 3;

	private LinearLayout lay_search_main;

	private Object obj;
	public static ProgressDialog myDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search_main);

		MyButton mb = (MyButton) this.findViewById(R.id.id_top_back);
		mb.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		TextView tv_title = (TextView) this.findViewById(R.id.id_top_title);

		tv_title.setText("搜索");
		tv_title.setTextSize(16);

		actv = (AutoCompleteTextView) this
				.findViewById(R.id.autoCompleteTv_search);
		lay_search_main = (LinearLayout) this
				.findViewById(R.id.lay_search_main);

		img_delte = (ImageView) this.findViewById(R.id.img_delete);
		img_do = (ImageView) this.findViewById(R.id.img_search_do);

		img_do.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				InputMethodManager imm = (InputMethodManager) getApplicationContext()
						.getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
				if (CommonUtils.isNetWorkConnected(SearchActivity.this)) {

					Log.i("actv.getText()", actv.getText().toString());
					if (actv.getText().length() > 0) {
						if (getDataThread != null) {

							getDataThread.interrupt();
							getDataThread = null;
						}
						getDataThread = new GetDataThread();
						getDataThread.start();
					} else {
						CommonUtils.DisplayToast(SearchActivity.this,
								"请输入搜索关键字！");
					}

				} else {

					CommonUtils.DisplayToast(SearchActivity.this,
							"无网络，请检查网络状态！");
				}

			}

		});

		if (CommonUtils.isNetWorkConnected(getApplicationContext())) {

			if (getAppNameThread != null) {

				getAppNameThread.interrupt();
				getAppNameThread = null;
			}
			getAppNameThread = new GetAppNameThread();
			getAppNameThread.start();
		} else {

			CommonUtils.DisplayToast(getApplicationContext(), "无网络，请检查网络状态！");

		}

		actv.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				if (s.length() == 0) {
					img_delte.setVisibility(View.GONE);
				} else {
					img_delte.setVisibility(View.VISIBLE);
				}
			}
		});
		img_delte.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				actv.setText("");
				lay_search_main.removeAllViews();

			}
		});
	}

	private class GetAppNameThread extends Thread {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			try {
				getAppName();
			} catch (Exception e) {
				// TODO: handle exception
			}

		}

	}

	private void getAppName() {
		lstPra.add("strsql");
		lstPra.add("2");
		Object obj = CommonUtils.GetWebserviceData("getAppNameList", lstPra);
		Log.i("obj", obj.toString());
		SoapObject sObj = CommonUtils.analysis((SoapObject) obj, "getAppNameList");
		if (sObj != null) {
			for (int i = 0; i < sObj.getPropertyCount(); i++) {
				SoapObject tmpObj = (SoapObject) sObj.getProperty(i);
				if (tmpObj.getPropertyCount() > 0) {

					lstName.add(tmpObj.getProperty(0).toString());
					// lstName.add("lishui" + String.valueOf(i));
				}

			}

		}

		// Log.i("lstName", lstName.toString());
		if (lstName.size() > 0) {
			CommonUtils.SendMsg(handler, GETNAME_SUCCESS,
					getString(R.string.str_app_name).toString(), 0);
		} else {

			CommonUtils.SendMsg(handler, GETNAME_FAILURE,
					getString(R.string.str_app_no_name).toString(), 0);
		}

	}

	final Handler handler = new Handler() {

		@SuppressLint("ResourceAsColor")
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			try {
				switch (msg.what) {
				case GETNAME_SUCCESS:

					ar_app_name = lstName.toArray(new String[lstName.size()]);

					Log.i("ar_app_name", ar_app_name[0].toString());
					ArrayAdapter<String> aa = new ArrayAdapter<String>(
							SearchActivity.this,
							android.R.layout.simple_dropdown_item_1line,
							ar_app_name);
					actv.setAdapter(aa);
					actv.setThreshold(1);
					// actv.setOnEditorActionListener(new
					// OnEditorActionListener() {
					//
					// @Override
					// public boolean onEditorAction(TextView v, int actionId,
					// KeyEvent event) {
					// // TODO Auto-generated method stub
					// if (keyCode == KeyEvent.KEYCODE_ENTER
					// && event.getAction() == KeyEvent.ACTION_DOWN){
					// if (getDataThread != null) {
					//
					// getDataThread.interrupt();
					// getDataThread = null;
					// }
					// getDataThread = new GetDataThread();
					// getDataThread.start();
					// }
					// return false;
					// }
					// });
					actv.setOnKeyListener(new OnKeyListener() {

						@Override
						public boolean onKey(View v, int keyCode, KeyEvent event) {
							// TODO Auto-generated method stub

							if (keyCode == KeyEvent.KEYCODE_ENTER
									&& event.getAction() == KeyEvent.ACTION_DOWN) {
								Log.i("key", "ssadsa");
								InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
								imm.toggleSoftInput(0,
										InputMethodManager.HIDE_NOT_ALWAYS);
								if (actv.getText().length() > 0) {
									if (CommonUtils
											.isNetWorkConnected(SearchActivity.this)) {
										if (getDataThread != null) {

											getDataThread.interrupt();
											getDataThread = null;
										}
										getDataThread = new GetDataThread();
										getDataThread.start();
									} else {

										CommonUtils.DisplayToast(
												SearchActivity.this,
												"无网络，请检查网络状态！");
									}
								} else {

									CommonUtils.DisplayToast(
											SearchActivity.this, "请输入搜索关键字！");
								}

							}
							return false;
						}
					});

					actv.setOnItemClickListener(new OnItemClickListener() {

						@Override
						public void onItemClick(AdapterView<?> parent,
								View view, int position, long id) {
							// TODO Auto-generated method stub
							InputMethodManager imm = (InputMethodManager) getApplicationContext()
									.getSystemService(
											Context.INPUT_METHOD_SERVICE);
							imm.toggleSoftInput(0,
									InputMethodManager.HIDE_NOT_ALWAYS);
							if (CommonUtils
									.isNetWorkConnected(SearchActivity.this)) {
								if (getDataThread != null) {

									getDataThread.interrupt();
									getDataThread = null;
								}
								getDataThread = new GetDataThread();
								getDataThread.start();
							} else {

								CommonUtils.DisplayToast(SearchActivity.this,
										"无网络，请检查网络状态！");
							}
						}
					});

					break;
				case GETNAME_FAILURE:
					CommonUtils.DisplayToast(SearchActivity.this,
							(String) msg.obj);
					break;
				case GETAPP_SUCCESS:
					SoapObject sObj = CommonUtils.analysis((SoapObject) obj,
							"getAppSearchInfo");
					lay_search_main.removeAllViews();
					for (int i = 0; i < sObj.getPropertyCount(); i++) {
						AppInfo temInfo = new AppInfo();
						SoapObject obj_tp = (SoapObject) sObj.getProperty(i);
						try {
							temInfo.setAppId(Integer.parseInt(obj_tp
									.getProperty("appid").toString()));
						} catch (Exception e) {
							// TODO: handle exception
							temInfo.setAppId(0);
						}
						try {
							temInfo.setAppName(obj_tp.getProperty("appname")
									.toString());
						} catch (Exception e) {
							// TODO: handle exception
							temInfo.setAppName("无");
						}
						String base64 = "";
						try {
							base64 = obj_tp.getProperty("appicon").toString();
						} catch (Exception e) {
							// TODO: handle exception
						}

						if (!base64.equals("") && base64 != "") {
							temInfo.setAppIcon(CommonUtils.zoomImage(
									CommonUtils.stringtoBitmap(base64), 120,
									120));
							// Log.i("base64", "base64 not null");
						} else {
							temInfo.setAppIcon(CommonUtils.zoomImage(
									getBitmap(R.drawable.noicon), 120, 120));

						}

						base64 = "";
						try {
							base64 = obj_tp.getProperty("imgIntroduction")
									.toString();
						} catch (Exception e) {
							// TODO: handle exception
						}

						if (!base64.equals("") && base64 != "") {
							temInfo.setImgIntroduction(CommonUtils.zoomImage(
									CommonUtils.stringtoBitmap(base64), 200,
									400));
							// Log.i("base64", "base64 not null");
						} else {
							temInfo.setImgIntroduction(CommonUtils.zoomImage(
									getBitmap(R.drawable.nopic), 200, 400));

						}

						try {
							temInfo.setDownloadCount("下载量："+obj_tp.getProperty(
									"dlcount").toString());
						} catch (Exception e) {

							temInfo.setDownloadCount("下载量："+String.valueOf(0));
						}
						

						try {
							temInfo.setKind(obj_tp.getProperty(
									"kind").toString());
						} catch (Exception e) {

							temInfo.setKind("无");
						}
						
						try {
							temInfo.setDetailIntroduce(obj_tp.getProperty(
									"detailIntroduce").toString());
						} catch (Exception e) {

							temInfo.setDetailIntroduce("无");
						}
						

						
						
						lay_search_main.addView(getView(temInfo,
								temInfo.getImgIntroduction()));

						// LinearLayout space = new LinearLayout(
						// SearchActivity.this);
						// space.setLayoutParams(new LinearLayout.LayoutParams(
						// LayoutParams.FILL_PARENT, 1));
						// space.setBackgroundColor(R.color.blue);
						// lay_search_main.addView(space);

					}

					break;
				case GETAPP_FAILURE:

					CommonUtils.DisplayToast(SearchActivity.this, "未搜寻到此应用");

					break;
				default:
					CommonUtils.DisplayToast(SearchActivity.this,
							(String) msg.obj);
					break;
				}

			} catch (Exception e) {
				// TODO: handle exception
			}

			super.handleMessage(msg);
		}

	};

	private class GetDataThread extends Thread {

		@Override
		public void run() {
			// TODO Auto-generated method stub

			try {
				getData();
			} catch (Exception e) {
				// TODO: handle exception
				// Log.e("e", e.getMessage());
			}

		}

	}

	private void getData() {
		Log.i("acom", actv.getText().toString());
		List<String> lsTmp = new ArrayList<String>();
//		String strsql = "SELECT  a.appid,a.appname,a.appicon,a.version,a.kind,a.pkgname,a.detailIntroduce,a.appActivity,a.addtime,a.modifytime,a.appcontent,"
//				+ "a.appnewfun,a.devcompany,a.developer,a.versionname,a.size,a.compatibility,a.imgIntroduction,b.dlcount  FROM t_appinfo a left join  t_dlmes b on a.appid=b.appid where a.appname like '%"
//				+ actv.getText() + "%'";
		lsTmp.add("appName");
		lsTmp.add(actv.getText().toString());

		obj = CommonUtils.GetWebserviceData("getAppSearchInfo", lsTmp);

		Log.i("obj", obj.toString());
		try {
			SoapObject sObj = CommonUtils.analysis((SoapObject) obj, "getAppSearchInfo");
			Log.i("sObj", sObj.toString());
			Log.i("sObj", String.valueOf(sObj.getPropertyCount()));
			CommonUtils.SendMsg(handler, GETAPP_SUCCESS, "成功", 0);
		} catch (Exception e) {
			// TODO: handle exception
			CommonUtils.SendMsg(handler, GETAPP_FAILURE, "失败", 0);
		}

	}

	@SuppressLint("NewApi")
	@SuppressWarnings("deprecation")
	private View getView(AppInfo app, Bitmap bm) {
		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		final View item = inflater.inflate(R.layout.search_info, null);
		ImageView img_icon = (ImageView) item
				.findViewById(R.id.imgSinfoApp);
		

		TextView appName = (TextView) item.findViewById(R.id.tvSinfoAppname);
		TextView appKind = (TextView) item.findViewById(R.id.tvsInfoAppKind);

		TextView appDetail = (TextView) item.findViewById(R.id.tvSinfoAppDetail);
		TextView appDl = (TextView) item.findViewById(R.id.tv_sinfo_downloadcount);
		img_icon.setImageBitmap(app.getAppIcon());
		
		
		
		appName.setText(app.getAppName());
		appKind.setText(app.getKind());
		appDetail.setText(app.getDetailIntroduce());
		appDl.setText(app.getDownloadCount());
		item.setContentDescription(String.valueOf(app.getAppId()));
		item.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getApplicationContext(),
						AppDetailActivity.class);
				intent.putExtra("appid",
						Integer.parseInt(v.getContentDescription().toString()));
				intent.putExtra("resource", "AppSearchFra");
				startActivity(intent);
				//
				// myDialog = ProgressDialog.show(getApplicationContext(),
				// "请稍等...",
				// "正在载入应用详细信息...", true);
				myDialog = new ProgressDialog(SearchActivity.this,
						ProgressDialog.THEME_HOLO_LIGHT);
				myDialog.setTitle("加载");
				myDialog.setMessage("正在载入应用详细信息，请稍等...");

				myDialog.setCanceledOnTouchOutside(false);
				myDialog.setCancelable(true);
				myDialog.show();

				View v1 = myDialog.getWindow().getDecorView();
				CommonUtils.setDialogText(v1);

			}
		});

		return item;

	}

	private Bitmap getBitmap(int id) {
		Bitmap originalBitmap = BitmapFactory
				.decodeResource(getResources(), id);
		int originalWidth = originalBitmap.getWidth();
		int originalHeight = originalBitmap.getHeight();
		int newWidth = 48;
		int newHeight = 48;

		float scale = ((float) newHeight) / originalHeight;
		Matrix matrix = new Matrix();
		matrix.postScale(scale, scale);
		Bitmap changedBitmap = Bitmap.createBitmap(originalBitmap, 0, 0,
				originalWidth, originalHeight, matrix, true);

		return changedBitmap;

	}

	public static void closeProgressDialog() {

		myDialog.dismiss();

	}

}
