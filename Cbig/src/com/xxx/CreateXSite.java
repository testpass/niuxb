package com.xxx;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSONObject;


public class CreateXSite {
	public static void main(String[] args) throws Exception {
		
//		File file = new File("H:/batu"); 
//		File[] files = file.listFiles();
//		List<Long> filesizes = new ArrayList<Long>();
//		List<JSONObject> jList = new ArrayList<JSONObject>();
//		for (File fi : files) {
//			int fileTotal =  fi.list().length - 1;
//			int page = fileTotal / 3 + 1;
//			JSONObject listPageInfoj = null;
//			try {
//				listPageInfoj =  JSONObject.parseObject(PCPageUtil.readFileByLines(fi.getPath() + "/title.json"));
//			} catch (Exception e) {
//				continue;
//			}
//			if (listPageInfoj == null) {
//				continue;
//			}
//			JSONObject jListPageInfo = new JSONObject();
//			JSONObject j = new JSONObject();
//			jListPageInfo.put("preAlt", listPageInfoj.getString("title"));
//			jListPageInfo.put("preTitle", listPageInfoj.getString("title"));
//			jListPageInfo.put("bText", listPageInfoj.getString("title"));
//			jListPageInfo.put("content", listPageInfoj.getString("title"));
//			List<JSONObject> detailPageInfos = getDetailPageInfo(page, listPageInfoj.getString("title"), fileTotal, fi);
//			if (detailPageInfos == null) {
//				continue;
//			}
//			try {
//				j.put("prePic", ((List)detailPageInfos.get(0).get("detailImgList")).get(0));
//			} catch (Exception e) {
//				continue;
//			}
//			String dirName = getDirName(6, fi.getName());
//			j.put("dirName", dirName);
//			j.put("jListPageInfo", jListPageInfo.toJSONString());
//			j.put("detailPageInfo", detailPageInfos);
//		
//			savePicInfo(j);
//		}
		PCPageUtil.genListPage1("6");   
		PCPageUtil.genDetailPage1("6");
	}
	public static void savePicInfo(JSONObject j) throws Exception {
		String dir = "E:/nginx-1.12.1/html/pic" + "/" + j.getString("dirName");
		String[] pics = j.getString("prePic").split("/");
		
		String fileName =  "pre_" + pics[pics.length - 1];
		File fileDir = new File(dir); 
		if (!fileDir.exists()) {
			fileDir.mkdirs(); 
		} else {
			return;
		}
		List<JSONObject> detailPageInfo = (List<JSONObject>)j.get("detailPageInfo");
		for (int i = 0; i < detailPageInfo.size(); i++) {
			String detaildir = dir  + "/" + i;
			String  jDetailPageInfo = detailPageInfo.get(i).get("jDetailPageInfo").toString();
			String jDetailPageInfoFilename = detaildir + "/detailPageInfo.json";
			creatDetailFile(jDetailPageInfoFilename, jDetailPageInfo, detaildir);
			List<String> detailImgList = (List)detailPageInfo.get(i).get("detailImgList");
			for (String detailImgUrl : detailImgList) {
				String[] detailFileNmaes = detailImgUrl.split("/");
				String detailFileNmae = detailFileNmaes[detailFileNmaes.length - 1];
				try{
					download(detailImgUrl, detailFileNmae, detaildir);
				} catch (Exception e) {
					continue;
				}
				
			}
		}
		
		download(j.getString("prePic"), fileName, dir);
		String listPageInfo = j.get("jListPageInfo").toString();
		
		
		String listPageInfoFilename = dir + "/listPageInfo.json";
		
		creatFile(listPageInfoFilename, listPageInfo);
		
		System.out.println("savePicInfo dir = " + dir);
		
	}
	
	public static void creatDetailFile(String filename, String content, String dir) {
		try {
			File file = new File(filename);
			File fileDir = new File(dir);
			if (!fileDir.exists()) {
				fileDir.mkdir();
			}
			if (!file.exists()) {
				
					file.createNewFile();
				
			} else {
				file.delete();
				file.createNewFile();
			}
			PrintStream printStream = new PrintStream(filename);
			printStream.println(content);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void creatFile(String filename, String content) {
		try {
			File file = new File(filename);
			if (!file.exists()) {
				
					file.createNewFile();
				
			} else {
				file.delete();
				file.createNewFile();
			}
			PrintStream printStream = new PrintStream(filename);
			printStream.println(content);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static void download(String urlString, String filename,String savePath) throws Exception {
		File file = new File(urlString);  
	    if (file.isFile()) {  
	        OutputStream fos = new FileOutputStream(savePath + "/" + filename);  
	        InputStream fis = new FileInputStream(file);  
	        System.out.println(file.length());  
	        byte[] buf = new byte[4096];  
	        int len;  
	        while ((len = fis.read(buf)) != -1) {  
	            fos.write(buf, 0, len);  
	        }  
	        fis.close();  
	        fos.close();  
	    }  
    }
	public static String getDirName(int type, String id) {
		String preifx = type + "_";
		return preifx + id;
	}
	public static List<JSONObject> getDetailPageInfo(int page, String title, int fileTotal, File fi) throws IOException  {
		List<JSONObject> jList = new ArrayList<JSONObject>();
		for (int i = 0; i < page; i++) {
			JSONObject jj = new JSONObject();
			JSONObject jDetailPageInfo = new JSONObject();
			jDetailPageInfo.put("detailKeyWords", title + i);
			jDetailPageInfo.put("detailDescription", title + i);
			jDetailPageInfo.put("detailHeadTitle", title + i);
			jj.put("jDetailPageInfo", jDetailPageInfo);
//			Element divNav = doc.select("div.nry").get(0);
//			Elements imgs = divNav.getElementsByTag("img");
			List<String> detailImgList = new ArrayList<String>();
			for (int ji = 0; ji < 3; ji++) {
				if ((i * 3 + ji) >=  fileTotal) {
					continue;
				}
				detailImgList.add(fi.getPath() + "/" + fi.listFiles()[i * 3 + ji].getName());
			}
			
			jj.put("detailImgList", detailImgList);
			jList.add(jj);
		}
		return jList;
	}
}
