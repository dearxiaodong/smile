package com.clw.clwappmarketnew;

import java.util.Timer;
import java.util.TimerTask;

import com.clw.clwappmarketnew.R;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends FragmentActivity {
	private Button btn_main;
	private Button btn_list;
	private Button btn_mine;
	private Drawable btn_selected;
	private int currentTabIndex;
	private int index;
	private Fragment[] fragments;
	private MyAppFra myApp;
	private MineMainFra mineFra;
	private ListRankingFragmet lRF;
	private Button[] arrayBtn;
	private boolean exitOnce = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.lay_top_main);
		btn_main = (Button) this.findViewById(R.id.btn_top_main);
		btn_list = (Button) this.findViewById(R.id.btn_top_list);
		btn_mine = (Button) this.findViewById(R.id.btn_top_mine);
		arrayBtn = new Button[] { btn_main, btn_list, btn_mine };
		arrayBtn[0].setSelected(true);
//		Resources res = getResources();
//		btn_selected = res.getDrawable(R.drawable.point_ed);
		myApp = new MyAppFra();
		mineFra = new MineMainFra();
		lRF = new ListRankingFragmet();

		fragments = new Fragment[] { myApp, lRF, mineFra };
		btn_main.setOnClickListener(new MyClickListener());
		btn_list.setOnClickListener(new MyClickListener());
		btn_mine.setOnClickListener(new MyClickListener());

		getSupportFragmentManager().beginTransaction()
				.add(R.id.lay_main, myApp).show(myApp).commit();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			
			finish();
			return true;
		}
		
		
		return super.onOptionsItemSelected(item);
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
				FragmentTransaction trx = getSupportFragmentManager()
						.beginTransaction();
				trx.hide(fragments[currentTabIndex]);
				if (!fragments[index].isAdded()) {
					trx.add(R.id.lay_main, fragments[index]);
				}
				trx.show(fragments[index]).commit();

				arrayBtn[currentTabIndex].setSelected(false);
				arrayBtn[index].setSelected(true);
				currentTabIndex = index;

				//Log.i("index", String.valueOf(index));
			}

		}

	};

	/* Toast控件显示提示信息 */
	public void DisplayToast(String str) {
		Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
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

}
