package com.hd.internal;

class Scan {
	private long ptr;

	public Scan() {
		ptr = 0;
	}

	@Override
	protected void finalize() throws Throwable {
		destroy();
		super.finalize();
	}

	/************************************************************************/
	/* 与扫描仪建立连接，并初始化扫描参数信息                                                                    */
	/************************************************************************/
	public int create() {
		ptr = sj_create();
		return ptr == 0 ? 0 : 1;
	}

	public int create(Control control, String ip, int port) {
		ptr = sj_create(control.Ref(), ip, port);
		return ptr == 0 ? 0 : 1;
	}

	/************************************************************************/
	/* 与扫描仪断开连接                                                        */
	/************************************************************************/
	public int destroy() {
		int ret = 1;
		if (ptr != 0)
			sj_destroy(ptr);
		return ret;
	}

	/************************************************************************
	 * 开始扫描,此时开启读写两个线程
	 * 读线程负责读取扫描数据，并写入共享队列
	 * 写线程负责读取共享队列数据，并写入到文件
	 ************************************************************************/
	public boolean start() {
		return sj_start(ptr)==1;
	}

	/************************************************************************
	 * 停止扫描
	 ************************************************************************/
	public boolean stop() {
		return sj_stop(ptr)==1;
	}

	public boolean waitfor() {
		return sj_wait(ptr)==1;
	}

	/************************************************************************
	 *\brief 设置扫描速度范围参数
	 *\param scan_job 定义了扫描任务。
	 *\param speed_h 定义了水平速度。
	 *\param start_angle_h 定义了水平开始角度。
	 *\param end_angle_h 定义了水平结束角度。
	 *\param speed_v 定义了垂直速度。
	 *\param resolution_v 定义了垂直分辨率。
	 *\param active_sector_start_angles 定义了扫描区域开始角度。
	 *\param active_sector_stop_angles 定义了扫描区域结束角度。
	 *\retval E_OK 表示成功。
	 ************************************************************************/
	public boolean config(int speed_h_delay, double start_angle_h,
			double end_angle_h, int speed_v_hz, double resolution_v,
			double start_angle_v, double end_angle_v) {
		return sj_config(ptr, speed_h_delay, start_angle_h, end_angle_h,
				speed_v_hz, resolution_v, start_angle_v, end_angle_v)==1;
	}

	/************************************************************************
	 * 设置点云数据存储目录,灰度图存储目录
	 ************************************************************************/
	public int set_data_dir(String ptDir, String grayDir) {
		return sj_set_data_dir(ptr, ptDir, grayDir);
	}

	/************************************************************************/
	/* 与扫描仪建立连接，并初始化扫描参数信息                                                                    */
	/************************************************************************/
	private native static long sj_create();

	private native static long sj_create(long control, String ip, int port);

	/************************************************************************/
	/* 与扫描仪断开连接                                                        */
	/************************************************************************/
	private native static int sj_destroy(long ptr);

	/************************************************************************
	 * 开始扫描,此时开启读写两个线程
	 * 读线程负责读取扫描数据，并写入共享队列
	 * 写线程负责读取共享队列数据，并写入到文件
	 ************************************************************************/
	private native static int sj_start(long ptr);

	/************************************************************************
	 * 停止扫描
	 ************************************************************************/
	private native static int sj_stop(long ptr);

	private native static int sj_wait(long ptr);

	/************************************************************************
	 *\brief 设置扫描速度范围参数
	 *\param scan_job 定义了扫描任务。
	 *\param speed_h 定义了水平速度。
	 *\param start_angle_h 定义了水平开始角度。
	 *\param end_angle_h 定义了水平结束角度。
	 *\param speed_v 定义了垂直速度。
	 *\param resolution_v 定义了垂直分辨率。
	 *\param active_sector_start_angles 定义了扫描区域开始角度。
	 *\param active_sector_stop_angles 定义了扫描区域结束角度。
	 *\retval E_OK 表示成功。
	 ************************************************************************/
	private native static int sj_config(long ptr, int speed_h_delay,
			double start_angle_h, double end_angle_h, int speed_v_hz,
			double resolution_v, double start_angle_v, double end_angle_v);

	/************************************************************************
	 * 设置点云数据存储目录,灰度图存储目录
	 ************************************************************************/
	private native static int sj_set_data_dir(long ptr, String ptDir,
			String grayDir);
}
