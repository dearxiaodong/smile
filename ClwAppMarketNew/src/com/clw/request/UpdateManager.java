package com.clw.request;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.DecimalFormat;










import com.clw.clwappmarketnew.R;
import com.clw.utils.CommonUtils;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.Intent;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnCancelListener;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Ӧ�ó�����¹��߰�
 * @author liux (http://my.oschina.net/liux)
 * @version 1.1
 * @created 2012-6-29
 */
public class UpdateManager {

	private static final int DOWN_NOSDCARD = 0;
    private static final int DOWN_UPDATE = 1;
    private static final int DOWN_OVER = 2;
	
    private static final int DIALOG_TYPE_LATEST = 0;
    private static final int DIALOG_TYPE_FAIL   = 1;
    
	private static UpdateManager updateManager;
	
	private Context mContext;
	//֪ͨ�Ի���
	private Dialog noticeDialog;
	//���ضԻ���
	private Dialog downloadDialog;
	//'�Ѿ�������' ���� '�޷���ȡ���°汾' �ĶԻ���
	private Dialog latestOrFailDialog;
    //������
    private ProgressBar mProgress;
    //��ʾ������ֵ
    private TextView mProgressText;
    //��ѯ����
    private ProgressDialog mProDialog;
    //����ֵ
    private int progress;
    //�����߳�
    private Thread downLoadThread;
    //��ֹ���
    private boolean interceptFlag;
	//��ʾ��
	private String updateMsg = "";
	//���صİ�װ��url
	private String apkUrl = "";
	//���ذ�����·��
    private String savePath = "";
	//apk��������·��
	private String apkFilePath = "";
	//��ʱ�����ļ�·��
	private String tmpFilePath = "";
	//�����ļ���С
	private String apkFileSize;
	//�������ļ���С
	private String tmpFileSize;
	
	private String curVersionName = "";
	
	private String downloadName;
    
    private Handler mHandler = new Handler(){
    	public void handleMessage(Message msg) {
    		switch (msg.what) {
			case DOWN_UPDATE:
				mProgress.setProgress(progress);
				mProgressText.setText(tmpFileSize + "/" + apkFileSize);
				break;
			case DOWN_OVER:
				downloadDialog.dismiss();
				installApk();
				break;
			case DOWN_NOSDCARD:
				downloadDialog.dismiss();
				Toast.makeText(mContext, "�޷����ذ�װ�ļ�������SD���Ƿ����", 3000).show();
				break;
			}
    	};
    };
    
	public static UpdateManager getUpdateManager() {
		if(updateManager == null){
			updateManager = new UpdateManager();
		}
		updateManager.interceptFlag = false;
		return updateManager;
	}
	
	

	//false ����ʾ�汾�޸��£� true �˺������Զ�ִ�к����������
	public boolean checkAppUpdate(Context context, int serverVersion,String dlName){
		
		//mVersionInfo = vInfo;
		this.mContext = context;
		this.downloadName=dlName;
		
		if (dlName.equals("ClwAppMarket")){
		if(serverVersion == 0){
			return false;
		}
		getCurrentVersion();
		if(isNewVersion(String.valueOf(serverVersion))){
			apkUrl = CommonUtils.sDownloadURL+"/ClwAppMarket.apk";
			updateMsg = "�°汾����";
			showNoticeDialog();
			return true;
		}
		
		}else{
			try {
				apkUrl = CommonUtils.sDownloadURL+"/"+URLEncoder.encode(dlName+".apk","UTF-8");
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			updateMsg = "�°汾����";
			//showNoticeDialog();
			showDownloadDialog();
			return true;
		}	
		
		return false;
	}	
	private boolean isNewVersion(String version) {
		if(version == null || version.length() == 0){
			return false;
		}
//		String newV[] = version.split("\\.");
//		String curV[] = curVersionName.split("\\.");
//		int minLen = newV.length < curV.length ? newV.length : curV.length;
//		for(int i = 0; i < minLen; ++i){
//			String test = newV[i];
//			String local = curV[i];
//			if (Integer.parseInt(test) > Integer.parseInt(local)) {
//				return true;
//			}
//			else if(Integer.parseInt(test) < Integer.parseInt(local)){
//				return false;
//			}
//		}
//		if(newV.length > curV.length){
//			return true;
//		}
		if (curVersionCode<Integer.parseInt(version)){
			return true;
		}
		
		return false;
	}
	/**
	 * ��ʾ'�Ѿ�������'����'�޷���ȡ�汾��Ϣ'�Ի���
	 */
	private void showLatestOrFailDialog(int dialogType) {
		if (latestOrFailDialog != null) {
			//�رղ��ͷ�֮ǰ�ĶԻ���
			latestOrFailDialog.dismiss();
			latestOrFailDialog = null;
		}
		AlertDialog.Builder builder = new Builder(mContext);
		builder.setTitle("ϵͳ��ʾ");
		if (dialogType == DIALOG_TYPE_LATEST) {
			builder.setMessage("����ǰ�Ѿ������°汾");
		} else if (dialogType == DIALOG_TYPE_FAIL) {
			builder.setMessage("�޷���ȡ�汾������Ϣ");
		}
		builder.setPositiveButton("ȷ��", null);
		latestOrFailDialog = builder.create();
		latestOrFailDialog.show();
	}

	private int curVersionCode;
	/**
	 * ��ȡ��ǰ�ͻ��˰汾��Ϣ
	 */
	private void getCurrentVersion(){
        try { 
        	PackageInfo info = mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0);
        	curVersionName = info.versionName;
        	curVersionCode = info.versionCode;
        } catch (NameNotFoundException e) {    
			e.printStackTrace(System.err);
		} 
	}
	
	/**
	 * ��ʾ�汾����֪ͨ�Ի���
	 */
	private void showNoticeDialog(){
		AlertDialog.Builder builder = new Builder(mContext);
		builder.setTitle("�����汾����");
		builder.setMessage(updateMsg);
		builder.setPositiveButton("��������", new OnClickListener() {			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				showDownloadDialog();			
			}
		});
		if(false){
			builder.setCancelable(false);
		}
		else {
			builder.setNegativeButton("�Ժ���˵", new OnClickListener() {			
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();				
				}
			});
		}
		noticeDialog = builder.create();
		noticeDialog.show();
	}
	
	
	/**
	 * ��ʾ���ضԻ���
	 */
	private void showDownloadDialog(){
		AlertDialog.Builder builder = new Builder(mContext);
		builder.setTitle("���������°汾");
		
		final LayoutInflater inflater = LayoutInflater.from(mContext);
		View v = inflater.inflate(R.layout.update_progress, null);
		mProgress = (ProgressBar)v.findViewById(R.id.update_progress);
		mProgressText = (TextView) v.findViewById(R.id.update_progress_text);
		
		builder.setView(v);
		builder.setNegativeButton("ȡ��", new OnClickListener() {	
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				interceptFlag = true;
			}
		});
		builder.setOnCancelListener(new OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialog) {
				dialog.dismiss();
				interceptFlag = true;
			}
		});
		downloadDialog = builder.create();
		downloadDialog.setCanceledOnTouchOutside(false);
		downloadDialog.show();
		
		downloadApk();
	}
	
	private Runnable mdownApkRunnable = new Runnable() {	
		@Override
		public void run() {
			try {
				String apkName = downloadName+".apk";
				String tmpApk = downloadName+".tmp";
				//�ж��Ƿ������SD��
				String storageState = Environment.getExternalStorageState();		
				if(storageState.equals(Environment.MEDIA_MOUNTED)){
					savePath = Environment.getExternalStorageDirectory().getAbsolutePath()+"/clwmarketdownload/";
					File file = new File(savePath);
					if(!file.exists()){
						file.mkdirs();
					}
					apkFilePath = savePath + apkName;
					tmpFilePath = savePath + tmpApk;
				}
				Log.i("apkFilePath", apkFilePath);
				//û�й���SD�����޷������ļ�
				if(apkFilePath == null || apkFilePath == ""){
					mHandler.sendEmptyMessage(DOWN_NOSDCARD);
					return;
				}
				
				File ApkFile = new File(apkFilePath);
				
				//�Ƿ������ظ����ļ�
				if(ApkFile.exists()){
//					downloadDialog.dismiss();
//					installApk();
//					return;
					ApkFile.delete();
				}
				
				//�����ʱ�����ļ�
				File tmpFile = new File(tmpFilePath);
				FileOutputStream fos = new FileOutputStream(tmpFile);
				
				URL url = new URL(apkUrl);
				HttpURLConnection conn = (HttpURLConnection)url.openConnection();
				conn.connect();
				int length = conn.getContentLength();
				InputStream is = conn.getInputStream();
				
				//��ʾ�ļ���С��ʽ��2��С������ʾ
		    	DecimalFormat df = new DecimalFormat("0.00");
		    	//������������ʾ�����ļ���С
		    	apkFileSize = df.format((float) length / 1024 / 1024) + "MB";
				
				int count = 0;
				byte buf[] = new byte[1024];
				
				do{   		   		
		    		int numread = is.read(buf);
		    		count += numread;
		    		//������������ʾ�ĵ�ǰ�����ļ���С
		    		tmpFileSize = df.format((float) count / 1024 / 1024) + "MB";
		    		//��ǰ����ֵ
		    	    progress =(int)(((float)count / length) * 100);
		    	    //���½���
		    	    mHandler.sendEmptyMessage(DOWN_UPDATE);
		    		if(numread <= 0){	
		    			//������� - ����ʱ�����ļ�ת��APK�ļ�
						if(tmpFile.renameTo(ApkFile)){
							//֪ͨ��װ
							mHandler.sendEmptyMessage(DOWN_OVER);
						}
		    			break;
		    		}
		    		fos.write(buf,0,numread);
		    	}while(!interceptFlag);//���ȡ����ֹͣ����
				
				fos.close();
				is.close();
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch(IOException e){
				e.printStackTrace();
			}
			
		}
	};
	
	/**
	* ����apk
	* @param url
	*/	
	private void downloadApk(){
		downLoadThread = new Thread(mdownApkRunnable);
		downLoadThread.start();
	}
	
	/**
    * ��װapk
    * @param url
    */
	private void installApk(){
		File apkfile = new File(apkFilePath);
        if (!apkfile.exists()) {
            return;
        }    
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.setDataAndType(Uri.parse("file://" + apkfile.toString()), "application/vnd.android.package-archive"); 
        mContext.startActivity(i);
	}
}