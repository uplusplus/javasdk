package com.hd.ui;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;

import com.hd.internal.Monitor;
import com.hd.ls300.Internal;
import com.hd.ls300.R;

public class Demo extends Activity {
	private static final String TAG = "com.hd.ui.Demo";
	private EditText et;
	Monitor monitor;
	Thread work;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		String s = this.getApplicationInfo().dataDir;
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		this.setContentView(R.layout.activity_main);
		et = (EditText) this.findViewById(R.id.edit_box);
		et.setBackgroundDrawable(null);
		et.setTextColor(Color.WHITE);
		et.setGravity(Gravity.TOP);
		et.setText("LS300 V2.0\n");
		et.setEnabled(false);

		monitor = new Monitor();
	}

	@Override
	public void onResume() {
		super.onResume();
		Internal.Init();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		Log.v(TAG, "populate context menu");
		// add context menu item
		menu.add(0, 1, Menu.NONE, "连接");
		menu.add(0, 2, Menu.NONE, "控制板");
		menu.add(0, 3, Menu.NONE, "扫描");
		menu.add(0, 4, Menu.NONE, "退出");
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		boolean ret;
		switch (item.getItemId()) {
			case 1:
				print("连接测试:");
				ret = monitor.Connect_control();
				if (ret)
					print("连接控制板成功。");
				else {
					print("连接控制板失败。");
					break;
				}
				ret = monitor.Connect_scan();
				if (ret)
					print("连接扫描仪成功。");
				else
					print("连接扫描仪失败。");
				break;
			case 2:
				print("控制板测试:");
				ret = monitor.control_check();
				if (ret)
					print("控制板状态正常。");
				else
					print("控制板状态异常。");
				break;
			case 3:
				print("扫描测试:");
				ret = monitor.make_work(50, 0, 360, 5, 0.25, -45, 90);
				if (ret) {
					print("创建扫描任务成功。");
					work = new Thread() {
						@Override
						public void run() {
							boolean ret;
							ret = monitor.start_work();
							if (!ret)
								return;
							monitor.wait_work();
							monitor.stop_work();
							mHandler.obtainMessage(SCAN_DONE).sendToTarget();
						}

					};
					work.start();
				} else
					print("创建扫描任务失败。");
				break;
			case 4:
				print("终止任务!");
				if(work!=null) work.stop();
				work = null;
				monitor.stop_work();
				this.finish();
				break;
			default:
				return super.onContextItemSelected(item);
		}
		return true;
	}

	private static final int SCAN_DONE = 0;
	private static final int SCAN_RUNNING = 1;
	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {//此方法在ui线程运行  
			switch (msg.what) {
				case SCAN_DONE:
					print("扫描完成.");
					break;
				case SCAN_RUNNING:
					printf(".");
					break;
			}
		}
	};

	private void printf(String str) {
		et.append(str);
	}

	private void print(String str) {
		et.append(str);
		et.append("\n");
	}
}
