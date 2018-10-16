package com.clw.clwappmarketnew;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import com.clw.clwappmarketnew.MainActivity.MyClickListener;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.PagerTitleStrip;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity1 extends FragmentActivity {
	private Button btn_main;
	private Button btn_list;
	private Button btn_mine;
	private MyAppFra myApp;
	private MineMainFra mineFra;
	private ListRankingFragmet lRF;
	private int currentTabIndex;
	private int index;
	private Button[] arrayBtn;
	
	private ArrayList<Fragment> fragmentList;
	
	
	private ViewPager vp;
	
	private PagerTabStrip pagerTabStrip;
	private PagerTitleStrip pagerTitleStrip;
	
	private boolean exitOnce = false;
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.activity_main);
		
		vp=(ViewPager)findViewById(R.id.vp_main);
		
		btn_main = (Button) this.findViewById(R.id.btn_top_main);
		btn_list = (Button) this.findViewById(R.id.btn_top_list);
		btn_mine = (Button) this.findViewById(R.id.btn_top_mine);
		arrayBtn = new Button[] { btn_main, btn_list, btn_mine };
		
//		pagerTabStrip=(PagerTabStrip)findViewById()
		
		myApp = new MyAppFra();
		mineFra = new MineMainFra();
		lRF = new ListRankingFragmet();
		fragmentList=new ArrayList<Fragment>();
		fragmentList.add(myApp);
		
		fragmentList.add(lRF);
		fragmentList.add(mineFra);
		currentTabIndex=0;
		
		vp.setAdapter(new MyViewPagerAdapter(getSupportFragmentManager()));
		
		btn_main.setOnClickListener(new MyClickListener());
		btn_list.setOnClickListener(new MyClickListener());
		btn_mine.setOnClickListener(new MyClickListener());
		arrayBtn[currentTabIndex].setSelected(true);
		vp.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int arg0) {
				// TODO Auto-generated method stub
				if (currentTabIndex != arg0) {
					// 填充每页的activity
					
//					vp.setCurrentItem(index);
					
					arrayBtn[currentTabIndex].setSelected(false);
					
					arrayBtn[arg0].setSelected(true);
					currentTabIndex = arg0;

					
				}
				
				
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		
		
		
		
		
	
	}
	
	public class MyViewPagerAdapter extends FragmentPagerAdapter{

		public MyViewPagerAdapter(FragmentManager fm) {
			super(fm);
			// TODO Auto-generated constructor stub
		}

		@Override
		public Fragment getItem(int arg0) {
			// TODO Auto-generated method stub
		Log.i("arg0", String.valueOf(arg0));

			return fragmentList.get(arg0);
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return fragmentList.size();
		}
		
		
		
		
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (!exitOnce) {
				DisplayToast(getString(R.string.main_exit));
				exitOnce = true;
				new Timer().schedule(new TimerTask() {

					@Override
					public void run() {
						exitOnce = false;
					}
				}, 3000);
				
				return true;

			} else {
				finish();
			}

		}

		return super.onKeyDown(keyCode, event);

	}
	/* Toast控件显示提示信息 */
	public void DisplayToast(String str) {
		Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
	}
	class MyClickListener implements View.OnClickListener {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.btn_top_main:
				index = 0;
				// DisplayToast("button_main");
				break;
			case R.id.btn_top_list:
				index = 1;
				// DisplayToast("button_list");
				break;
			case R.id.btn_top_mine:
				index = 2;
				// DisplayToast("button_search");
				break;

			default:
				// DisplayToast("no button");
				break;
			}

			
			
			if (currentTabIndex != index) {
				// 填充每页的activity
				
				vp.setCurrentItem(index);
				
				arrayBtn[currentTabIndex].setSelected(false);
				
				arrayBtn[index].setSelected(true);
				currentTabIndex = index;

				
			}

		}

	};
}
