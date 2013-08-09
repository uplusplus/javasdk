package com.hd.ls300;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.util.Log;

import com.hd.base.Utils;
import com.hd.ls300.ConfigManager.ScanConfig;

public class DataManager {
	// 管理类
	public static class Manager implements Interface {
		private static final String TAG = "com.hd.ls300.DataManager.Manager";
		private List<Data> list = new ArrayList<Data>();

		public Manager() {
			Load(Internal.data_path);
		}

		private static List<Data> EnumData(String data_path) {
			List<Data> ret = new ArrayList<Data>();
			String ptDir = Internal.point_cloud_path;
			String imgDir = Internal.image_path;
			String metaDir = Internal.meta_path;
			List<String> ptFiles = Utils.getFiles(ptDir,
					Internal.file_endwith.point_cloud);
			for (String file : ptFiles) {
				String name = new File(file).getName();
				String image_file = Utils.getFiles(imgDir, name,
						Internal.file_endwith.image);
				String meta_file = Utils.getFiles(metaDir, name,
						Internal.file_endwith.meta);
				ScanConfig cfg = Internal.config_mgr.Get(name);
				MetaData meta = (MetaData) Utils.ReadSerializable(meta_file);

				Data data = new Data(name, ptDir, image_file, cfg, meta);
				ret.add(data);
			}
			return ret;
		}

		@Override
		public void Load(String root) {
			EnumData(root);
		}

		@Override
		public Data Get(String name) {
			for (Data data : list)
				if (data.name.equalsIgnoreCase(name)) {
					return data;
				}

			return null;
		}

		@Override
		public void Put(Data data) {
			// check data valid?
			list.add(data);
		}

		@Override
		public void Clear() {
			list.clear();
		}

		@Override
		public void Save(String root) {
			// TODO:保存数据，删除文件，新建文件？
			Log.i(TAG, "not implement yet.");
		}

		@Override
		public int Count() {
			// TODO Auto-generated method stub
			return list.size();
		}

	}

	// 采集数据描述
	public static class Data implements Serializable {
		public String name;
		public String point_cloud_path;
		public String image_path;
		public ScanConfig config;
		public MetaData meta;// 可选项

		public Data(String name, String point_cloud_path, String image_path,
				ScanConfig cfg, MetaData meta) {
			this.name = name;
			this.point_cloud_path = point_cloud_path;
			this.image_path = image_path;
			this.config = cfg;
			this.meta = meta;
		}
	}

	// 元数据描述
	public static class MetaData implements Serializable {
		public String date; // 采集时间
		public String stuff; // 采集人员
		public String location; // 地点
	}

	// 管理接口
	public static interface Interface {
		public void Load(String root);

		public void Save(String root);

		public Data Get(String name);

		public void Put(Data data);

		public int Count();

		public void Clear();
	}

}
