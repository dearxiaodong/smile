package com.clw.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;
import org.ksoap2.serialization.SoapObject;






import com.clw.clwappmarketnew.R;
import com.clw.service.ReadRemoteService;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Message;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class CommonUtils {

	private static int INSTALLED = 0; // ��ʾ�Ѿ���װ���Ҹ��������apk�ļ���һ���汾
	private static int UNINSTALLED = 1; // ��ʾδ��װ
	private static int INSTALLED_UPDATE = 2; // ��ʾ�Ѿ���װ���汾����������汾Ҫ�ͣ����Ե����ť����

	private static String sNameSpace = "http://122.226.253.93:8090/";
//	private static String sWebserviceURL = "http://10.0.2.2/dbweb/ClwAppDb.asmx";
	private static String sWebserviceURL = "http://122.226.253.93:8090/clwappdb/ClwAppDb.asmx";
	
	private static Boolean IsDotNetService = true;
	
	public static String sDownloadURL="http://122.226.253.93:8090/clwappdb/apkres";

	/**
	 * ��������Ƿ����
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isNetWorkConnected(Context context) {
		if (context != null) {
			ConnectivityManager mConnectivityManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mNetworkInfo = mConnectivityManager
					.getActiveNetworkInfo();
			if (mNetworkInfo != null) {
				return mNetworkInfo.isAvailable();
			}
		}

		return false;
	}

	/**
	 * ���Sdcard�Ƿ����
	 * 
	 * @return
	 */
	public static boolean isExitsSdcard() {
		if (android.os.Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED))
			return true;
		else
			return false;
	}

	public static String getTopActivity(Context context) {
		ActivityManager manager = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningTaskInfo> runningTaskInfos = manager.getRunningTasks(1);

		if (runningTaskInfos != null)
			return runningTaskInfos.get(0).topActivity.getClassName();
		else
			return "";
	}

	/**
	 * �ж϶�������������ÿһ�������Ƿ�Ϊ��: ����Ϊnull���ַ����г���Ϊ0�������ࡢMapΪempty
	 * 
	 * @param obj
	 * @return
	 */
	public static boolean isNullOrEmpty(Object obj) {

		if (obj != null) {

		} else {
			return true;
		}

		if (obj.toString() != null) {// �ж϶��󣨲�֪���Բ��ԣ�

		} else {
			return true;
		}

	
		if (obj instanceof CharSequence) {

			return ((CharSequence) obj).length() == 0;
		}
		if (obj instanceof Collection) {

			return ((Collection) obj).isEmpty();
		}

		if (obj instanceof Map) {

			return ((Map) obj).isEmpty();
		}

		if (obj instanceof Object[]) {

			Object[] object = (Object[]) obj;
			if (object.length == 0) {
				return true;
			}
			boolean empty = true;
			for (int i = 0; i < object.length; i++) {
				if (!isNullOrEmpty(object[i])) {
					empty = false;
					break;
				}
			}
			return empty;
		}

		if (obj instanceof JSONArray) {
			if (((JSONArray) obj).length() == 0) {
				return true;
			}
		}

		if (obj instanceof JSONObject) {
			if (((JSONObject) obj).length() == 0) {
				return true;
			}
		}

		return false;
	}

	/**
	 * �жϴ�app�Ƿ��Ѿ���װ
	 */

	public static boolean isInstalled(Context context, String pkgName) {
		if (pkgName == null || "".equals(pkgName)) {
			return false;

		}
		try {
			context.getPackageManager().getApplicationInfo(pkgName,
					PackageManager.GET_UNINSTALLED_PACKAGES);
			return true;
		} catch (NameNotFoundException e) {
			return false;
		}

	}

	// �������Ƿ�����
	public static boolean isStartService(Context ctx) {
		ActivityManager mActivityManager = (ActivityManager) ctx
				.getSystemService(Context.ACTIVITY_SERVICE);
		List<ActivityManager.RunningServiceInfo> currentService = mActivityManager
				.getRunningServices(100);
		final String igrsClassName = "com.iflytek.asr.AsrService"; // serviceName
		boolean b = igrsBaseServiceIsStart(currentService, igrsClassName);
		return b;
	}

	private static boolean igrsBaseServiceIsStart(
			List<ActivityManager.RunningServiceInfo> mServiceList,
			String className) {
		for (int i = 0; i < mServiceList.size(); i++) {
			if (className.equals(mServiceList.get(i).service.getClassName())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * �жϸ�Ӧ�����ֻ��еİ�װ���
	 * 
	 * @param context
	 *            Context
	 * @param packageName
	 *            Ҫ�ж�Ӧ�õİ���
	 * @param versionCode
	 *            Ҫ�ж�Ӧ�õİ汾��
	 */
	public static int doType(Context context, String packageName,
			int versionCode) {
		PackageManager pm = context.getPackageManager();

		List<PackageInfo> pakageinfos = pm
				.getInstalledPackages(PackageManager.GET_UNINSTALLED_PACKAGES);
		for (PackageInfo pi : pakageinfos) {
			String pi_packageName = pi.packageName;
			int pi_versionCode = pi.versionCode;
			// ������������ϵͳ�Ѿ���װ����Ӧ���д���
			if (packageName.endsWith(pi_packageName)) {
				// Log.i("test","��Ӧ�ð�װ����");
				if (versionCode == pi_versionCode) {
					Log.i("test", "�Ѿ���װ�����ø��£�����ж�ظ�Ӧ��");
					return INSTALLED;
				} else if (versionCode > pi_versionCode) {
					Log.i("test", "�Ѿ���װ���и���");
					return INSTALLED_UPDATE;
				}
			}
		}
		Log.i("test", "δ��װ��Ӧ�ã����԰�װ");
		return UNINSTALLED;
	}

	/* Toast�ؼ���ʾ��ʾ��Ϣ */
	public static void DisplayToast(Context context, String str) {
		Toast.makeText(context, str, Toast.LENGTH_SHORT).show();
	}

	public static Object GetWebserviceData(String sMethodName, List<String> lst) {
		
		try {
				ReadRemoteService readService = new ReadRemoteService(sNameSpace,
				sMethodName, sWebserviceURL, IsDotNetService, lst);
			Object Result = readService.Get();	
			
			return Result;
				
		} catch (Exception e) {
			// TODO: handle exception
			
			return null;
		}
	
		

		
	}

	

	public static Bitmap stringtoBitmap(String string) {
		// ��b64�ַ���ת����Bitmap����
		Bitmap bitmap = null;
		try {
			byte[] bitmapArray;
			bitmapArray = Base64.decode(string, Base64.DEFAULT);
			bitmap = BitmapFactory.decodeByteArray(bitmapArray, 0,
					bitmapArray.length);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return bitmap;
	}

	public static String bitmaptoString(Bitmap bitmap) {
		// ��Bitmapת�����ַ���
		String string = null;
		ByteArrayOutputStream bStream = new ByteArrayOutputStream();
		bitmap.compress(CompressFormat.PNG, 100, bStream);
		byte[] bytes = bStream.toByteArray();
		string = Base64.encodeToString(bytes, Base64.DEFAULT);
		return string;
	}

	/***
	 * ͼƬ�����ŷ���
	 * 
	 * @param bgimage
	 *            ��ԴͼƬ��Դ
	 * @param newWidth
	 *            �����ź���
	 * @param newHeight
	 *            �����ź�߶�
	 * @return
	 */
	public static Bitmap zoomImage(Bitmap bgimage, double newWidth,
			double newHeight) {
		// ��ȡ���ͼƬ�Ŀ�͸�
		float width = bgimage.getWidth();
		float height = bgimage.getHeight();
		// ��������ͼƬ�õ�matrix����
		Matrix matrix = new Matrix();
		// ������������
		float scaleWidth = ((float) newWidth) / width;
		float scaleHeight = ((float) newHeight) / height;
		// ����ͼƬ����
		matrix.postScale(scaleWidth, scaleHeight);
		Bitmap bitmap = Bitmap.createBitmap(bgimage, 0, 0, (int) width,
				(int) height, matrix, true);
		return bitmap;
	}

	public static SoapObject analysis(SoapObject sObj, String sMethodName) {
		SoapObject tmpObj = null;
		try {
			tmpObj = (SoapObject) ((SoapObject) ((SoapObject) ((SoapObject) sObj)
					.getProperty(sMethodName + "Result")).getProperty(1))
					.getProperty("NewDataSet");

		} catch (Exception e) {
			// TODO: handle exception
		}

		return tmpObj;

	}

	public static void tostx(Context tt, String xx) {
		Toast toast = Toast.makeText(tt, xx, Toast.LENGTH_LONG);
		toast.setGravity(Gravity.CENTER, 0, 0);
		LinearLayout toastView = (LinearLayout) toast.getView();
		setDialogText(toastView);

		ImageView imageCodeProject = new ImageView(tt);
		imageCodeProject.setImageResource(R.drawable.tips_icon);
		toastView.addView(imageCodeProject, 0);
		toast.show();
	}

	public static void setDialogText(View v) {

		if (v instanceof ViewGroup) {
			ViewGroup parent = (ViewGroup) v;
			int count = parent.getChildCount();
			for (int i = 0; i < count; i++) {
				View child = parent.getChildAt(i);
				setDialogText(child);
			}
		} else if (v instanceof TextView) {
			((TextView) v).setTextSize(20);
		}

	}
	
	public static void SendMsg(Handler handler,int what, String obj, int arg1) {
		Message message = new Message();
		message.what = what;
		message.obj = obj;
		message.arg1 = arg1;
		handler.sendMessage(message);
	}
	
	/**
	* encodeBase64File:(���ļ�ת��base64 �ַ���). <br/>
	* @author guhaizhou@126.com
	* @param path �ļ�·��
	* @return
	* @throws Exception
	* @since JDK 1.6
	*/
	public static String encodeBase64File(String path) throws Exception {
	File  file = new File(path);
	FileInputStream inputFile = new FileInputStream(file);
	byte[] buffer = new byte[(int)file.length()];
	inputFile.read(buffer);
	        inputFile.close();
	        return Base64.encodeToString(buffer,Base64.DEFAULT);
	}
	
	/**
	* decoderBase64File:(��base64�ַ����뱣���ļ�). <br/>
	* @author guhaizhou@126.com
	* @param base64Code �������ִ�
	* @param savePath  �ļ�����·��
	* @throws Exception
	* @since JDK 1.6
	*/
	public static void decoderBase64File(String base64Code,String savePath) throws Exception {
	//byte[] buffer = new BASE64Decoder().decodeBuffer(base64Code);
	byte[] buffer =Base64.decode(base64Code, Base64.DEFAULT);
	FileOutputStream out = new FileOutputStream(savePath);
	out.write(buffer);
	out.close();
	}
	public static String getSysDate(){
		SimpleDateFormat    sDateFormat    =   new    SimpleDateFormat("yyyy-MM-dd hh:mm:ss");       
		String    date    =    sDateFormat.format(new java.util.Date());    
		
		return date;
	}

}
