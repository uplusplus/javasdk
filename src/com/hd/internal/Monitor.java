package com.hd.internal;

import android.util.Log;

import com.hd.ls300.DeviceSetting.Setting.Connect;
import com.hd.ls300.Internal;

public class Monitor {
	private static final String TAG = "com.hd.internal.Monitor";
	Control control = null;
	Scan scan = null;

	public Monitor() {
	}
	/************************************************************************
	 * 配置扫描任务接口
	 ************************************************************************/
	
	//扫描步骤 1
	public boolean Connect_control() {
		Connect connect;
		int ret = 0;
		control = new Control();
		connect = Internal.setting.scansetting.control;
		switch (connect.type) {
			case CONNECT_PIPE:
				break;
			case CONNECT_SERIAL:
				ret = control.open(connect.dev, connect.bandrate);
				break;
			case CONNECT_TCP:
				ret = control.open_socket(connect.address, connect.port);
				break;
		}

		if (ret != 1) {
			Log.e(TAG, "打开控制板失败。");
			return false;
		}
		return true;
	}
	
	//扫描步骤 2
	public boolean control_check(){
		return control.check();
	}
	//扫描步骤 3
	public boolean Connect_scan() {
		Connect connect;
		int ret = 0;
		scan = new Scan();
		connect = Internal.setting.scansetting.sick;
		switch (connect.type) {
			case CONNECT_PIPE:
				break;
			case CONNECT_SERIAL:
				break;
			case CONNECT_TCP:
				ret = scan.create(control, connect.address, connect.port);
				break;
		}
		if (ret != 1) {
			Log.e(TAG, "创建扫描连接失败。");
			return false;
		}
		return true;
	}
	
	//扫描步骤 4
	public int set_data_dir(String ptDir, String grayDir) {
		return scan.set_data_dir(ptDir, grayDir);
	}

	//扫描步骤 5
	public boolean make_work(int speed_h_delay, double start_angle_h,
			double end_angle_h, int speed_v_hz, double resolution_v,
			double start_angle_v, double end_angle_v) {
		if(scan==null) return false;
		//TODO:主动调用
		scan.set_data_dir(Internal.point_cloud_path,Internal.image_path);
		return scan.config(speed_h_delay, start_angle_h, end_angle_h,
				speed_v_hz, resolution_v, start_angle_v, end_angle_v);
	}

	
	//扫描步骤 6
		public boolean start_work() {
			if(scan==null) return false;
			return scan.start();
		}
	
	//扫描步骤 7
	public boolean wait_work() {
		if(scan==null) return false;
		return scan.waitfor();
	}
	
	//扫描步骤 8
	public boolean stop_work() {
		if(scan==null) return false;
		return scan.stop();
	}

	/************************************************************************
	 * 额外接口
	 ************************************************************************/
	
	//开始工作
	public int turntable_start() {
		return control.turntable_start();
	}
	

	//停止工作
	public int turntable_stop() {
		return control.turntable_stop();
	}

	//是否停止工作
	public int turntable_is_stopped() {
		return control.turntable_is_stopped();
	}

	//搜索零点
	public int turntable_search_zero() {
		return control.search_zero();
	}

	//获取当前水平转台的角度
	public double get_turntable_angle() {
		return control.turntable_get_angle();
	}

	//转台工作
	public int turntable_config(int step_delay, double start, double end) {
		return control.turntable_config(step_delay, start, end);
	}

	//根据实际传过来的水平旋转角度，调整水平台，以较快速度转到实际水平台的起始角度/转台回到起始原点
	public int turntable_turn(double angle) {
		return control.turntable_turn(angle);
	}

	//相机拍照
	public int camera_take_photo() {
		return control.camera_take_photo();
	}

	//全景扫描
	public int camera_scan(double start_angle, double end_angle) {
		return control.camera_scan(start_angle, end_angle);
	}

	//获取温度
	public double get_temperature() {
		return control.get_temperature();
	}

	//获取倾斜度
	public int get_tilt(double[] angle) {
		return control.get_tilt(angle);
	}

	//获取状态
	public double get_battery() {
		return control.get_battery();
	}

	//亮红灯
	public int led_red() {
		return control.led_red();
	}

	//亮绿灯
	public int led_green() {
		return control.led_green();
	}

	//LED熄灭
	public int led_off() {
		return control.led_off();
	}

	//硬件信息
	public String get_info(int idx, int blen) {
		return control.get_info(idx, blen);
	}

}
