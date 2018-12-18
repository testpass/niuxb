package com.xxx;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.alibaba.fastjson.JSONObject;

public class C2 {

	public static void main(String[] args) {File file = new File("H:/batu"); 
	File[] files = file.listFiles();
	List<Long> filesizes = new ArrayList<Long>();
	List<String> fname = new ArrayList<String>();
	fname.add("2087304");
	fname.add("2087305");
	fname.add("2087306");
	fname.add("2087307");
	fname.add("2078791");
	fname.add("2090899");
	fname.add("2086992");
	fname.add("2086993");
	fname.add("2086994");
	fname.add("2087082");
	fname.add("2087083");
	fname.add("2087084");
	fname.add("2087085");
	fname.add("2087086");
	fname.add("2082889");
	fname.add("2089190");
	fname.add("2087090");
	fname.add("2087091");
	fname.add("2087092");
	fname.add("2087093");
	fname.add("2071477");
	fname.add("2087195");
	fname.add("2087196");
	fname.add("2087197");
	fname.add("2082893");
	fname.add("2087198");
	fname.add("2087199");
	fname.add("2087200");
	fname.add("2087201");
	fname.add("2090896");
	
	for (File fi : files) {
		
		
		if (fname.contains(fi.getName())) {
			File[] f = fi.listFiles();
			for (File jpgf : f) {
				jpgf.delete();
			}
			System.out.println("delete file " + fi.getName());
			fi.delete();
		}
		
	}
	}
	
	
	public static void deleteJpg() {
		File file = new File("H:/batu"); 
		File[] files = file.listFiles();
		List<Long> filesizes = new ArrayList<Long>();
		for (File fi : files) {
			File[] f = fi.listFiles();
//			if (f.length == 1) {
//				f[0].delete();
//				fi.delete();
//				System.out.println("delete file " + fi.getName());
//			}
			
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
		List<JSONObject> list  = new ArrayList<JSONObject>();
		System.out.println("filesizes.size() " + filesizes.size());
		for (int i = 0; i < filesizes.size(); i++) {
			JSONObject js = new JSONObject();
			int red = 0;
			long size = filesizes.get(i);
			System.out.println("filesizes i = " + i);
			for (int j = 0; j < filesizes.size(); j++) {
				if (size == filesizes.get(j)) {
					red++;
				}
			}
			js.put("size", size);
			js.put("red", red);
			list.add(js);
		}
		List<JSONObject> newlist  = new ArrayList<JSONObject>();
		System.out.println("list.size() " + list.size());
		for (int i = 0; i < list.size(); i++) {
			boolean exsit = false;
			JSONObject js =  list.get(i);
			System.out.println("list i " + i);
			for (int j = 0; j < newlist.size(); j++) {
				JSONObject js1 =  newlist.get(j);
				if (StringUtils.equals(js.getString("size"), js1.getString("size"))) {
					exsit = true;
					break;
				}
			}
			if (!exsit) {
				newlist.add(js);
			}
			
		}
		System.out.println("newlist i = " + newlist.size());
		for (int i = 0; i < newlist.size(); i++) {
			if(Long.parseLong(newlist.get(i).getString("red")) > 5) {
				del(Long.parseLong(newlist.get(i).getString("red")));
			}
			System.out.println("newlist  " + newlist.get(i).toString());
		}
	}
	public static void del(long size) {
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
				filesizes.add(jpgf.length());
				if (jpgf.length() == size) {
					jpgf.delete();
					System.out.println("delete file " + jpgf.getName());
				}
			}
		}	
	}
}
