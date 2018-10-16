package com.clw.adapter;

import java.util.List;



import com.clw.clwappmarketnew.R;
import com.clw.domain.AppInfo;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ListRankingAppInfoAdapter extends BaseAdapter {
	private List<AppInfo> listRanking=null;
	LayoutInflater inflater=null;
	
	/**
	 * 
	 */
	public ListRankingAppInfoAdapter(Context context,List<AppInfo> lRanking
			) {
	
		inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		listRanking=lRanking;
		
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return listRanking.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return listRanking.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View view =null;
		ViewHolder holder=null;
	if (convertView==null||convertView.getTag()==null){
		view=inflater.inflate(R.layout.ranking_item, null);
		holder=new ViewHolder(view);
		view.setTag(holder);
		
		
	}else{
		view=convertView;
		holder=(ViewHolder)convertView.getTag();
		
	}
	
	AppInfo appInfo = (AppInfo) getItem(position);
	if (appInfo.getAppIcon() != null) {
		holder.appIcon.setImageBitmap(appInfo.getAppIcon());
	} else {
		holder.appIcon.setImageDrawable(appInfo.getDrawable());
	}
	holder.appKind.setText(appInfo.getKind());
	holder.appKind.setTextSize(10);
	holder.appName.setText(appInfo.getAppName());
	holder.appDetail.setText(appInfo.getDetailIntroduce());
	holder.appDetail.setTextSize(12);
	holder.tvNum.setText(String.valueOf(position+1));
	if (appInfo.getDownloadCount()==""||appInfo.getDownloadCount().equals("")){
		holder.tvCount.setText("下载量：0");	
	}
	else{
	holder.tvCount.setText("下载量："+appInfo.getDownloadCount());	
	}	
//	holder.appInstall.setOnClickListener(new OnClickListener() {
//		
//		@Override
//		public void onClick(View v) {
//			// TODO Auto-generated method stub
//			Log.i("imgclick", "sas");
//			
//			
//		}
//	});
	
	
		return view;
	}
	
	class ViewHolder {
		ImageView appIcon;
		TextView appName;
		TextView appKind;
		TextView appDetail;
//		ImageView appInstall;
		TextView tvNum;
		TextView tvCount;

		public ViewHolder(View view) {
			this.appIcon = (ImageView) view.findViewById(R.id.imgRankingApp);
			this.appName = (TextView) view.findViewById(R.id.tvRankAppname);
			this.appKind = (TextView) view.findViewById(R.id.tvRankAppKind);
			this.appDetail = (TextView) view.findViewById(R.id.tvRankAppDetail);
//			this.appInstall = (ImageView) view.findViewById(R.id.img_ranking_install);
		    this.tvCount=(TextView)view.findViewById(R.id.tv_rank_downloadcount);
			this.tvNum=(TextView)view.findViewById(R.id.tvRankNum);
			
		
		
		}
	}
	
	
	
	

}
