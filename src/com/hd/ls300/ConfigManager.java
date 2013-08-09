package com.hd.ls300;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.util.Log;

import com.hd.base.Utils;
import com.hd.base.Utils.AreaF;
import com.hd.ls300.ConfigManager.Precisions.Precision;
import com.hd.ls300.ConfigManager.Precisions.precision;

public class ConfigManager {
	private static final String TAG = "ScanConfig";

	public static class Manager implements Interface {
		private List<ScanConfig> list = null;

		public Manager() {
			Load(Internal.config_path);
		}

		private static List<ScanConfig> MakeDefaultConfig(){
			List<ScanConfig> ret = new ArrayList<ScanConfig>();
			
			ret.add(new ScanConfig("Panoramic-Low",-45,90,0,360,Precision.PRECISION_LEVEL_LOW));
			ret.add(new ScanConfig("Panoramic-Normal",-45,90,0,360,Precision.PRECISION_LEVEL_NORMAL));
			ret.add(new ScanConfig("Panoramic-Middle",-45,90,0,360,Precision.PRECISION_LEVEL_MIDDLE));
			ret.add(new ScanConfig("Panoramic-High",-45,90,0,360,Precision.PRECISION_LEVEL_HIGH));
			
			return ret;
		}
		
		private static List<ScanConfig> EnumConfig(String path) {
			List<ScanConfig> ret = new ArrayList<ScanConfig>();
			List<String> fileList = Utils.getFiles(path,
					Internal.file_endwith.config);
			
			for (String file : fileList) {
				ScanConfig cfg = (ScanConfig) Utils.ReadSerializable(file);
				ret.add(cfg);
			}
			return ret;
		}

		@Override
		public void Load(String root) {
			String myDir = root==null?Internal.config_path:root;
			list = EnumConfig(myDir);
			if(list.size() <=0){
				list =  MakeDefaultConfig();
				Save(null);
			}
		}

		@Override
		public void Save(String root) {
			String myDir = root==null?Internal.config_path:root;
			String fpath;
			for (ScanConfig cfg : list) {
				fpath = Utils.joinPath(myDir, cfg.name);
				fpath += "." + Internal.file_endwith.config[0];
				Utils.WriteSerializable(fpath, cfg);
			}
		}

		@Override
		public ScanConfig Get(String name) {
			for (ScanConfig cfg : list)
				if (cfg.name.equalsIgnoreCase(name)) {
					return cfg;
				}

			return null;
		}

		@Override
		public boolean Contain(String name) {
			for (ScanConfig cfg : list)
				if (cfg.name.equalsIgnoreCase(name)) {
					return true;
				}

			return false;
		}

		@Override
		public boolean Put(ScanConfig config) {
			if (Contain(config.name))
				return false;
			return list.add(config);
		}

		@Override
		public boolean Remove(String name) {
			for (ScanConfig cfg : list)
				if (cfg.name.equalsIgnoreCase(name)) {
					return list.remove(cfg);
				}
			return false;
		}

		
		@Override
		public void Clear() {
			list.clear();
		}

		@Override
		public int Count() {
			return list.size();
		}

	}

	public static class Precisions {
		public static enum Precision {
			PRECISION_LEVEL_1, PRECISION_LEVEL_2, PRECISION_LEVEL_3, PRECISION_LEVEL_4,
			// /别名
			PRECISION_LEVEL_LOW, PRECISION_LEVEL_NORMAL, PRECISION_LEVEL_MIDDLE, PRECISION_LEVEL_HIGH,
		}

		public static class precision implements Serializable {
			// 前三个参数为决定性参数，后面的为计算出来的值。
			float frequency; // 垂直扫描频率（Hz） //2.5 5 10 15 Hz
			float resolution;// 垂直扫描精度（度） // 1.0 0.5 0.25 0.125 0.0625
			int plus_delay;// 水平步进的脉冲延时 // 50 100 200 400 ns
			// extra 全景扫描数据的，宽度，高度，需要的时间（S）。
			transient int max_width, max_height, max_time;
			transient float angle_resolution; // 水平角分辨率

			public precision(float fre, float res, int delay, int w, int h,
					int time) {
				frequency = fre;
				resolution = res;
				plus_delay = delay;
				max_width = w;
				max_height = h;
				max_time = time;
				angle_resolution = (360 / time) * (1 / fre);
			}

			public precision(float fre, float res, int delay) {
				frequency = fre;
				resolution = res;
				plus_delay = delay;
				int time = (int) (180 * 100 * 80 * delay / 1e6);
				int w = (int) (time * fre);
				int h = (int) (360 / res);
				angle_resolution = (360 / time) * (1 / fre);
				max_width = w;
				max_height = h;
				max_time = time;
			}

			@Override
			public String toString() {
				return "[" + frequency + "," + resolution + "," + plus_delay
						+ "]";
			}

			public static precision fromString(String s) {
				String v[] = s.replace('[', ' ').replace(']', ' ').split(",");
				return new precision(Float.parseFloat(v[0]),
						Float.parseFloat(v[1]), Integer.parseInt(v[2]));
			}
		}

		static precision[] configs = new precision[] {
				new precision(10f, 0.5f, 50, 720, 720, 72),
				new precision(7f, 0.375f, 100, 1008, 960, 144),
				new precision(10f, 0.25f, 100, 1440, 1440, 144),
				new precision(5f, 0.125f, 400, 2880, 2880, 576),
				new precision(2.5f, 0.0625f, 2500, 9000, 5760, 3600), };

		public static precision get(Precision p) {
			int idx = 0;
			switch (p) {
				case PRECISION_LEVEL_1:
				case PRECISION_LEVEL_LOW:
					idx = 0;
					break;
				case PRECISION_LEVEL_2:
				case PRECISION_LEVEL_NORMAL:
					idx = 1;
					break;
				case PRECISION_LEVEL_3:
				case PRECISION_LEVEL_MIDDLE:
					idx = 2;
					break;
				case PRECISION_LEVEL_4:
				case PRECISION_LEVEL_HIGH:
					idx = 3;
					break;
				default:
					Log.e(TAG, "Unrecognized precision.");
					return null;

			}
			return configs[idx];
		}
	}

	public static class ScanConfig implements Serializable {
		public String name;// 全景，粗扫，精扫
		public AreaF area;// {0,360, -45,90}
		public precision preci;

		// 直接创建
		public ScanConfig(String name, float start_angle_v, float end_angle_v,
				float start_angle_h, float end_angle_h, int speed_h,
				float speed_v, float resolution_v) {
			this.name = name;
			this.area = new AreaF(start_angle_h, end_angle_h, start_angle_v,
					end_angle_v);
			preci = new precision(speed_v, resolution_v, speed_h);
		}

		// 使用推荐精度配置创建
		public ScanConfig(String name, float start_angle_v, float end_angle_v,
				float start_angle_h, float end_angle_h, Precision prec) {
			this.name = name;
			this.area = new AreaF(start_angle_h, end_angle_h, start_angle_v,
					end_angle_v);
			this.preci = Precisions.get(prec);
		}

		public ScanConfig(String name) {
			this.name = name;
		}

		// 直接读文件创建
		public static ScanConfig fromString(String name, String content) {
			ScanConfig cfg = new ScanConfig(name);
			String v[] = content.split("\n");
			cfg.name = v[0];
			cfg.area = AreaF.fromString(v[1]);
			cfg.preci = precision.fromString(v[2]);
			return cfg;
		}

		@Override
		public String toString() {
			String ret;
			ret = String.format("%s\n%s\n%s", name, area.toString(),
					preci.toString());
			return ret;
		}

		// 获取完成需要时间（秒）
		public int getTime() {
			return (int) ((area.width1() / 2) * 100 * 80 * preci.plus_delay / 1e6);
		}

		// 获取水平转一度需要时间（秒）
		public int getTimePerDegree() {
			return (int) (100 * 80 * preci.plus_delay / 1e6);
		}

		// TODO:获取其它信息

	}

	public static interface Interface {
		public void Load(String root);

		public void Save(String root);

		public ScanConfig Get(String name);

		public boolean Contain(String name);

		public boolean Put(ScanConfig config);
		public boolean Remove(String name);

		public int Count();

		public void Clear();
	}
}
