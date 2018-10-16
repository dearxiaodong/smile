package com.clw.clwappmarketnew;

import java.util.ArrayList;
import java.util.List;

import org.ksoap2.serialization.SoapObject;

import com.clw.adapter.ListRankingAppInfoAdapter;
import com.clw.domain.AppInfo;
import com.clw.utils.CommonUtils;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class ListRankingFragmet extends Fragment {
	private View mMainView;
	private ListRankingAppInfoAdapter infoAdapter = null;
	private ListView lvRanking;
	private List<AppInfo> aList;

	private List<String> lst;
	private Object obj = null;

	private static final int GET_RANKING_LIST_SUCC = 0;
	private GetRankingListThread listThread;

	public static ProgressDialog myDialog_list;

	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		LayoutInflater inflater=getActivity().getLayoutInflater();
		mMainView=inflater.inflate(R.layout.ranking_main, (ViewGroup)getActivity().findViewById(R.id.vp_main), false);
		
	}
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);

		lvRanking = (ListView) getView().findViewById(R.id.listView_rank);
		aList = new ArrayList<AppInfo>();

		lst = new ArrayList<String>();

		
		Button btn_to_search = (Button) getView().findViewById(
				R.id.btn_to_search);
		btn_to_search.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// 跳转到搜索页面
				Intent intent = new Intent(getActivity(), SearchActivity.class);
				startActivity(intent);

			}
		});

	}
@Override
public void onResume() {
	// TODO Auto-generated method stub
	if (CommonUtils.isNetWorkConnected(getView().getContext())) {
		if (obj==null){
		if (listThread != null) {

			listThread.interrupt();
			listThread = null;
		}
		listThread = new GetRankingListThread();
		listThread.start();
		}
	} else {

		CommonUtils.DisplayToast(getView().getContext(), "无网络，请检查网络状态！");
	}
	
	super.onResume();
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
		//return inflater.inflate(R.layout.ranking_main, container, false);
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

	private AppInfo getAppInfo(Bitmap bm, String appName, String appKind,
			String appDetail, String dlCount) {
		AppInfo appInfo = new AppInfo();
		appInfo.setAppIcon(bm);
		appInfo.setAppName(appName);
		appInfo.setKind(appKind);
		appInfo.setDetailIntroduce(appDetail);
		appInfo.setDownloadCount(dlCount);
		return appInfo;
	}

	public static void closeProgressDialog() {

		myDialog_list.dismiss();

	}

	private class GetRankingListThread extends Thread {
		@Override
		public void run() {
			// TODO Auto-generated method stub
			try {
				GetRankingList();
			} catch (Exception e) {
				// TODO: handle exception
			}
		}

	}

	public void GetRankingList() {

		lst.add("strsql");
		lst.add("2");

		obj = CommonUtils.GetWebserviceData("getRankList", lst);

		CommonUtils.SendMsg(handler, GET_RANKING_LIST_SUCC, "获取数据成功", 0);
	}

	Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub

			switch (msg.what) {
			case GET_RANKING_LIST_SUCC:
				aList.clear();
				SoapObject innerObj = (SoapObject) ((SoapObject) obj)
						.getProperty("getRankListResult");
				int sobjcount = innerObj.getPropertyCount();
				SoapObject childObj = (SoapObject) innerObj.getProperty(1);
				SoapObject childObj1 = (SoapObject) childObj
						.getProperty("NewDataSet");
				// Log.i("cb1", String.valueOf(childObj1.getPropertyCount()));
				for (int i = 0; i < childObj1.getPropertyCount(); i++) {
					AppInfo temInfo = new AppInfo();
					SoapObject temObj = (SoapObject) childObj1.getProperty(i);
					// Log.i("obj", temObj.toString());
					//Log.i("obj", String.valueOf(temObj.getPropertyCount()));
					try {
						temInfo.setAppId(Integer.parseInt(temObj.getProperty(
								"appid").toString()));
					} catch (Exception e) {
						// TODO: handle exception
					}
					try {
						temInfo.setAppName(temObj.getProperty("appname")
								.toString());
					} catch (Exception e) {
						// TODO: handle exception
						temInfo.setAppName("无");
					}

					try {
						temInfo.setKind(temObj.getProperty("kind").toString());
					} catch (Exception e) {
						// TODO: handle exception
						temInfo.setKind("无");
					}

					try {
						temInfo.setDetailIntroduce(temObj.getProperty(
								"detailIntroduce").toString());
					} catch (Exception e) {
						// TODO: handle exception
						temInfo.setDetailIntroduce("无");
					}

					try {
						temInfo.setDownloadCount(temObj.getProperty("dlcount")
								.toString());

					} catch (Exception e) {
						// TODO: handle exception
						temInfo.setDownloadCount("0");
					}

					String base64;
					try {
						base64 = temObj.getProperty("appicon").toString();
					} catch (Exception e) {
						// TODO: handle exception
						base64 = "";
					}

					if (!base64.equals("") || base64 != "") {
						temInfo.setAppIcon(CommonUtils.zoomImage(
								CommonUtils.stringtoBitmap(base64), 120, 120));
						// Log.i("base64", "base64 not null");
					} else {
						temInfo.setAppIcon(CommonUtils.zoomImage(
								getBitmap(R.drawable.noicon), 120, 120));

					}

					aList.add(temInfo);

				}

				infoAdapter = new ListRankingAppInfoAdapter(getActivity(),
						aList);
				lvRanking.setAdapter(infoAdapter);
				lvRanking.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							final int position, long id) {
						// TODO Auto-generated method stub

						Handler handler = new Handler();

						Runnable mTasks = new Runnable() {

							public void run() {

								Intent itt = new Intent(getActivity(),
										AppDetailActivity.class);

								itt.putExtra("appid", aList.get(position)
										.getAppId());
								itt.putExtra("resource", "ListRanking");
								startActivity(itt);

							}

						};

						// myDialog_list = ProgressDialog.show(getActivity(),
						// "请稍等...",
						// "正在载入应用详细信息...", true);
						myDialog_list = new ProgressDialog(getActivity(),
								ProgressDialog.THEME_HOLO_LIGHT);
						myDialog_list.setTitle("加载");
						myDialog_list.setMessage("正在载入应用详细信息，请稍等...");

						myDialog_list.setCanceledOnTouchOutside(false);
						myDialog_list.setCancelable(true);
						myDialog_list.show();

						// View v1 = myDialog_list.getWindow().getDecorView();
						// CommonUtils.setDialogText(v1);
						handler.post(mTasks);

					}

				});

				break;
			default:
				break;

			}

			super.handleMessage(msg);
		}
	};

}
