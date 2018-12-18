package com.xxx;

import java.util.HashMap;
import java.util.Map;

public class Constants {
	public final static String PIC_ADDR = "I:/pic";
	public final static String PIC_ADDR_X = "E:/nginx-1.12.1/html/pic";
//	public final static String PIC_ADDR = "/usr/share/nginx/html/pic";
	public final static String PIC_ADDR_NET = "/pic";
//	public final static String PAGE_ADDR = "E:/nginx-1.12.1/html";
	public final static String PAGE_ADDR = "E:/nginx-1.12.1/html/";
//	public final static String WAP_PAGE_ADDR = "E:/nginx-1.12.1/html/wap";
	public final static String WAP_PAGE_ADDR = "E:/nginx-1.12.1/html/wap";
//	public final static String DETAIL_HTML = "E:/nginx-1.12.1/html/moudle/detail.html";
	public final static String DETAIL_HTML = "E:/nginx-1.12.1/html/moudle/detail.html";
	public final static String HEAD_HTML = "E:/nginx-1.12.1/html/moudle/common_header.html";
	public final static String INDEX_HTML = "E:/nginx-1.12.1/html/moudle/index.html";
	public final static String WAP_INDEX_HTML = "E:/nginx-1.12.1/html/moudle/wapindex.html";
	public final static String WAP_LIST_HTML = "E:/nginx-1.12.1/html/moudle/waplist.html";
	public final static String WAP_DETAIL_HTML = "E:/nginx-1.12.1/html/moudle/wapdetail.html";
	public final static String BOTTOM_HTML = "E:/nginx-1.12.1/html/moudle/common_bottom.html";
	public final static Map<String, String> titleLinkMap = new HashMap<String, String>();
	public final static Map<String, String> titleMap = new HashMap<String, String>();
	public final static Map<String, String> titleTypeMap = new HashMap<String, String>();
	static{
		titleLinkMap.put("xingganmeinvxiezhen", "1");
		titleLinkMap.put("meituisiwatupian", "2");
		titleLinkMap.put("BEAUTYLEGtuimo", "2");
		titleLinkMap.put("ligui", "2");
		titleLinkMap.put("gaoxiaotupian", "3");
		titleLinkMap.put("neihantupian", "3");
		titleLinkMap.put("qingchunmeinvxiezhen", "4");
		titleLinkMap.put("meinvtupianjingpin", "5");
		titleLinkMap.put("diannaobizhi", "6");
		titleLinkMap.put("meinvxiezhenbizhi", "7");
		titleLinkMap.put("shoujibizhi", "8");
		titleLinkMap.put("tupiantouxiang", "9");
		titleLinkMap.put("CosPlay", "10");
		titleLinkMap.put("new100", "100");
		
		titleTypeMap.put("1", "xingganmeinvxiezhen");
		titleTypeMap.put("2", "meituisiwatupian");
		titleTypeMap.put("3", "gaoxiaotupian");
		titleTypeMap.put("4", "qingchunmeinvxiezhen");
		titleTypeMap.put("5", "meinvtupianjingpin");
		titleTypeMap.put("6", "diannaobizhi");
		titleTypeMap.put("7", "meinvxiezhenbizhi");
		titleTypeMap.put("8", "shoujibizhi");
		titleTypeMap.put("9", "tupiantouxiang");
		titleTypeMap.put("10", "cosplay");
		titleTypeMap.put("100", "new100");
		
		titleMap.put("1", "性感美女写真");
		titleMap.put("2", "丝袜美腿");
		titleMap.put("3", "性感福利");
		titleMap.put("4", "人体写真");
		titleMap.put("5", "车展美女");
		titleMap.put("6", "电脑壁纸");
		titleMap.put("10", "动漫美女");
		titleMap.put("100", "�?近更�?");
	}
}
