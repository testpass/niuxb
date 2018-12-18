package com.xxx;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.alibaba.fastjson.JSONObject;

public class PageListUtil {
	static public List<JSONObject> getPageListNew100() {
		try {
			List<JSONObject> fileDList = readAllfile(Constants.PIC_ADDR_X);
			
			return fileDList.subList(0, 100);
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	static public List<JSONObject> getPageListByType(String type) {
		try {
			List<JSONObject> fileDList = readfile(Constants.PIC_ADDR_X, type);
			
			return fileDList;
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	public static List readAllfile(String filepath) throws FileNotFoundException, IOException {
		List<Map> fileDList = new ArrayList<Map>();
		File file = new File(filepath);
		File flist[] = file.listFiles();
		for (File f : flist) {
			if (f.isDirectory()) {
				Map m = new HashMap<>();
				System.out.println("f.getName() == " + f.getName());
				m.put(Long.parseLong(f.getName().split("_")[1]), f);
				fileDList.add(m);
			}
		}
		return reSortFileList(fileDList);
	}
	
	public static List readfile(String filepath, String type) throws FileNotFoundException, IOException {
		List<Map> fileDList = new ArrayList<Map>();
		File file = new File(filepath);
		File flist[] = file.listFiles();
		String fileNamePrefix = type + "_";
		for (File f : flist) {
			if (f.isDirectory()) {
				if (f.getName().startsWith(fileNamePrefix)) {
					Map m = new HashMap<>();
					try {
						m.put(Long.parseLong(f.getName().split("_")[1]), f);
					} catch (Exception e) {
						m.put(f.getName(), f);
					}
					
					fileDList.add(m);
				}

			}
		}
		if (fileDList.size() == 1) {
			return fileDList;
		}
		return reSortFileList(fileDList);
	}

	private static List reSortFileList(List<Map> fileDList) {
		if (fileDList == null || fileDList.isEmpty()) {
			return null;
		}
		
		
		List<Long> keyList = new ArrayList<>();
		for (Map m : fileDList) {
			Iterator iter = m.keySet().iterator();
			long key = 0;
			while (iter.hasNext()) {
				key= (long)iter.next();
			}
			keyList.add(key);
		}
		
		// Collections.reverse(keyList);
		 Collections.sort(keyList,Collections.reverseOrder());
		
		List<JSONObject> ret = new ArrayList<>();
		for (Long lo : keyList) {
			for (Map m : fileDList) {
				if (m.containsKey(lo)) {
					JSONObject j = new JSONObject();
					File fileD = ((File)m.get(lo));
					File flist[] = fileD.listFiles();
					List detailPicList = new ArrayList<>();
					
					String jsonFilePath = fileD.getPath() + "/listPageInfo.json";
					File jsonFile = new File(jsonFilePath);
					if (!jsonFile.exists()) {
						continue;
					}
					for (File f : flist) {
						if (f.getName().startsWith("pre_")) {
							j.put("prePic", Constants.PIC_ADDR_NET + "/" + fileD.getName() + "/" +f.getName());
						}
						List detailImgFileList = new ArrayList<>();
						if (f.isDirectory()) {
							File detailFlist[] = f.listFiles();
							JSONObject js = new JSONObject();
							for (int i = 0; i < detailFlist.length; i++) {
								if (!detailFlist[i].getName().endsWith(".json")) {
									detailImgFileList.add(detailFlist[i].getName());
								}
							}
							js.put("detailImgFileList", detailImgFileList);
							js.put("detailImgFileName", f.getName());
							detailPicList.add(js);
						}
						
					}
					
					j.put("picId", fileD.getName());
					j.put("detailPicList", detailPicList);
					ret.add(j);
				}
			}
		}
		return ret;
	}
	
}
