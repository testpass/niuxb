package com.xxx;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.alibaba.fastjson.JSONObject;

public class C {
	public class TThread extends Thread{
		@Override
		public void run() {
			// TODO Auto-generated method stub
			super.run();
		}
	}
	public static void main(String[] args) {
//    	genWapIndexPage();
//    	genWapListPage1("1");
//    	genWapListPage1("2");
//    	genWapListPage1("3");
//    	genWapListPage1("4");
//    	genWapListPage1("5");
//    	genWapListPage1("10");
//
//    	genWapDetailPage1("1");
//    	genWapDetailPage1("2");
//    	genWapDetailPage1("3");
//    	genWapDetailPage1("4");
//    	genWapDetailPage1("5");
//    	genWapDetailPage1("10");
//    	File file1 = new File("H:/batu/2060988/1.jpg"); 
//    	System.out.println("delete file " +  file1.length());
//    	File file = new File("H:/batu"); 
//		File[] files = file.listFiles();
//		List<Long> filesizes = new ArrayList<Long>();
//		for (File fi : files) {
//			File[] f = fi.listFiles();
//			if (f.length == 1) {
//				f[0].delete();
//				fi.delete();
//				System.out.println("delete file " + fi.getName());
//			}
//			
//			for (File jpgf : f) {
//				String filename = jpgf.getName();
//				if (StringUtils.contains(filename, "json")) {
//					continue;
//				}
//				filesizes.add(jpgf.length());
//				
//				if (jpgf.length() == 56156) {
//					jpgf.delete();
//					System.out.println("delete file " + jpgf.getName());
//				}
//			}
////			System.out.println(fi.getName());
//		}
//		deleteJpg();
		Thread t = new Thread();
	
	}
	
	
	public static void deleteJpg() {
		File file = new File("H:/batu"); 
		File[] files = file.listFiles();
		List<Long> filesizes = new ArrayList<Long>();
		for (File fi : files) {
			File[] f = fi.listFiles();
			if (f.length == 1) {
				f[0].delete();
				fi.delete();
				System.out.println("delete file " + fi.getName());
			}
			
			for (File jpgf : f) {
				String filename = jpgf.getName();
				if (StringUtils.contains(filename, "json")) {
					continue;
				}
				filesizes.add(jpgf.length());
				
				if (jpgf.length() == 191997) {
					jpgf.delete();
					System.out.println("delete file " + jpgf.getName());
				}
			}
//			System.out.println(fi.getName());
		}
//		List<JSONObject> list  = new ArrayList<JSONObject>();
//		System.out.println("filesizes.size() " + filesizes.size());
//		for (int i = 0; i < filesizes.size(); i++) {
//			JSONObject js = new JSONObject();
//			int red = 0;
//			long size = filesizes.get(i);
//			System.out.println("filesizes i = " + i);
//			for (int j = 0; j < filesizes.size(); j++) {
//				if (size == filesizes.get(j)) {
//					red++;
//				}
//			}
//			js.put("size", size);
//			js.put("red", red);
//			list.add(js);
//		}
//		List<JSONObject> newlist  = new ArrayList<JSONObject>();
////		System.out.println("list.size() " + list.size());
//		for (int i = 0; i < list.size(); i++) {
//			boolean exsit = false;
//			JSONObject js =  list.get(i);
////			System.out.println("list i " + i);
//			for (int j = 0; j < newlist.size(); j++) {
//				JSONObject js1 =  newlist.get(j);
//				if (StringUtils.equals(js.getString("size"), js1.getString("size"))) {
//					exsit = true;
//					break;
//				}
//			}
//			if (!exsit) {
//				newlist.add(js);
//			}
//			
//		}
//		System.out.println("newlist i = " + newlist.size());
//		List<Long> aaa = new ArrayList<Long>();
//		for (int i = 0; i < newlist.size(); i++) {
//			long red = Long.parseLong(newlist.get(i).getString("red"));
//			if(red > 5) {
//				System.out.println(newlist.get(i).getString("size") + " and " + newlist.get(i).getString("red"));
////				del(Long.parseLong(newlist.get(i).getString("red")));
//				aaa.add(Long.parseLong(newlist.get(i).getString("size")));
//				
//			}
//			System.out.println("newlist  " + newlist.get(i).toString());
//		}
//		System.out.println("aaa size = " + aaa.size());
//		del(aaa);
	}
	public static void del(List<Long> aaa) {
		File file = new File("H:/batu"); 
		File[] files = file.listFiles();
		List<Long> filesizes = new ArrayList<Long>();
		for (File fi : files) {
			File[] f = fi.listFiles();
			for (File jpgf : f) {
				String filename = jpgf.getName();
				if (StringUtils.contains(filename, "json")) {
					continue;
				}
//				System.out.println("filename = " + filename);
				filesizes.add(jpgf.length());
				for (long a : aaa) {
					
					if (jpgf.length() == a) {
						jpgf.delete();
						System.out.println("delete file " + jpgf.getName() + " and dir name = " + fi.getName());
						break;
					}
				}
				
			}
		}	
	}
}
