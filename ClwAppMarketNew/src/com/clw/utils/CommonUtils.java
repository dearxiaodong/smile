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

	private static int INSTALLED = 0; // 表示已经安装，且跟现在这个apk文件是一个版本
	private static int UNINSTALLED = 1; // 表示未安装
	private static int INSTALLED_UPDATE = 2; // 表示已经安装，版本比现在这个版本要低，可以点击按钮更新

	private static String sNameSpace = "http://122.226.253.93:8090/";
//	private static String sWebserviceURL = "http://10.0.2.2/dbweb/ClwAppDb.asmx";
	private static String sWebserviceURL = "http://122.226.253.93:8090/clwappdb/ClwAppDb.asmx";
	
	private static Boolean IsDotNetService = true;
	
	public static String sDownloadURL="http://122.226.253.93:8090/clwappdb/apkres";

	/**
	 * 检测网络是否可用
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
	 * 检测Sdcard是否存在
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
	 * 判断对象或对象数组中每一个对象是否为空: 对象为null，字符序列长度为0，集合类、Map为empty
	 * 
	 * @param obj
	 * @return
	 */
	public static boolean isNullOrEmpty(Object obj) {

		if (obj != null) {

		} else {
			return true;
		}

		if (obj.toString() != null) {// 判断对象（不知道对不对）

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
	 * 判断此app是否已经安装
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

	// 检查服务是否启动
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
	 * 判断该应用在手机中的安装情况
	 * 
	 * @param context
	 *            Context
	 * @param packageName
	 *            要判断应用的包名
	 * @param versionCode
	 *            要判断应用的版本号
	 */
	public static int doType(Context context, String packageName,
			int versionCode) {
		PackageManager pm = context.getPackageManager();

		List<PackageInfo> pakageinfos = pm
				.getInstalledPackages(PackageManager.GET_UNINSTALLED_PACKAGES);
		for (PackageInfo pi : pakageinfos) {
			String pi_packageName = pi.packageName;
			int pi_versionCode = pi.versionCode;
			// 如果这个包名在系统已经安装过的应用中存在
			if (packageName.endsWith(pi_packageName)) {
				// Log.i("test","此应用安装过了");
				if (versionCode == pi_versionCode) {
					Log.i("test", "已经安装，不用更新，可以卸载该应用");
					return INSTALLED;
				} else if (versionCode > pi_versionCode) {
					Log.i("test", "已经安装，有更新");
					return INSTALLED_UPDATE;
				}
			}
		}
		Log.i("test", "未安装该应用，可以安装");
		return UNINSTALLED;
	}

	/* Toast控件显示提示信息 */
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
		// 将b64字符串转换成Bitmap类型
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
		// 将Bitmap转换成字符串
		String string = null;
		ByteArrayOutputStream bStream = new ByteArrayOutputStream();
		bitmap.compress(CompressFormat.PNG, 100, bStream);
		byte[] bytes = bStream.toByteArray();
		string = Base64.encodeToString(bytes, Base64.DEFAULT);
		return string;
	}

	/***
	 * 图片的缩放方法
	 * 
	 * @param bgimage
	 *            ：源图片资源
	 * @param newWidth
	 *            ：缩放后宽度
	 * @param newHeight
	 *            ：缩放后高度
	 * @return
	 */
	public static Bitmap zoomImage(Bitmap bgimage, double newWidth,
			double newHeight) {
		// 获取这个图片的宽和高
		float width = bgimage.getWidth();
		float height = bgimage.getHeight();
		// 创建操作图片用的matrix对象
		Matrix matrix = new Matrix();
		// 计算宽高缩放率
		float scaleWidth = ((float) newWidth) / width;
		float scaleHeight = ((float) newHeight) / height;
		// 缩放图片动作
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
	* encodeBase64File:(将文件转成base64 字符串). <br/>
	* @author guhaizhou@126.com
	* @param path 文件路径
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
	* decoderBase64File:(将base64字符解码保存文件). <br/>
	* @author guhaizhou@126.com
	* @param base64Code 编码后的字串
	* @param savePath  文件保存路径
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
