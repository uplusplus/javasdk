package com.hd.ls300;

import java.io.Serializable;

import com.hd.base.Utils;

public class DeviceSetting {
	public static enum CONNECT_TYPE {
		CONNECT_TCP, CONNECT_SERIAL, CONNECT_PIPE
	}

	public static Setting get() {
		Setting ret;
		ret = (Setting) Utils.ReadSerializable(Internal.setting_file);
		if (ret == null) {
			ret = new Setting();
		}
		return ret;
	}

	public static class Setting implements Serializable {
		public void Save() {
			Utils.WriteSerializable(Internal.setting_file, this);
		}

		public static class Vendor implements Serializable {
			//不要在这里直接硬编码，在ＸＭＬ中写
			public String corp_name; //公司名称="海达数云"
			public String corp_address; //地址/联系方式="武汉市东湖高新技术开发区武大科技园慧业楼7F"
			public String phone_number; //电话
			public String introduction;//简介
			public Vendor(){
				corp_name = "海达数云";
				corp_address = "武汉市东湖高新技术开发区武大科技园慧业楼7F";
				phone_number="15327150110";
				introduction = "三维激光扫描仪";
			}
		}

		public static class Device implements Serializable {
			public String name;//设备名 ＬＳ３００
			public String software_version; //V2.0
			public Vendor vendor  = new Vendor();
			public Device(){
				name = "LS300";
				software_version = "2.0";
			}
		}

		public static class Connect implements Serializable {
			public CONNECT_TYPE type;
			//TCP
			public String address;
			public int port;
			//SERIAL
			public String dev;
			public int bandrate;
			//PIPE
			public String pipe;
		}

		public static class ScanSetting implements Serializable {
			public Connect control = new Connect(); //连接中位机
			public Connect sick = new Connect(); //连接扫描仪
			public String data_dir; //数据存取根目录
			public Connect remote = new Connect(); //远程监听连接
			public ScanSetting(){
				control.type = CONNECT_TYPE.CONNECT_SERIAL;
				control.dev = "/dev/ttyUSB0";
				control.bandrate = 38400;
				
				sick.type = CONNECT_TYPE.CONNECT_TCP;
				sick.address = "192.168.1.10";
				sick.port = 49152;
				data_dir = Internal.data_dir;
				
				remote.type = CONNECT_TYPE.CONNECT_PIPE;
				control.pipe = "/data/data/com.hd.ls300/cache/test.sprite";
			}
		}

		public ScanSetting scansetting = new ScanSetting();
		public Device device = new Device();
	}

}
