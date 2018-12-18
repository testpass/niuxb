package com.xxx;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.alibaba.fastjson.JSONObject;

public class CrawSiteUtil {

	public static void crawlSite1() {
		// TODO Auto-generated method stub
		String url = "http://www.tu11.com/";
		List<String> columnList = new ArrayList<String>(); 
		try {
			Document doc = Jsoup.connect(url).get();
			Elements divNav = doc.select("div.container-fluid.topnav.navbar");
			Elements as = divNav.get(0).getElementsByClass("list-inline navul").get(0).getElementsByTag("a");
			
			for (Element e : as) {
				String link = e.attr("href");
				if (!StringUtils.equals(link, "http://www.tu11.com/")) {
//					if (StringUtils.contains(link, "cosplay") ) {
//						columnList.add(link);
//					}
//					if (StringUtils.contains(link, "meituisiwatupian") ) {
//						continue;
//					}
//					if (StringUtils.contains(link, "qingchunmeinvxiezhen") ) {
//						continue;
//					}
					columnList.add(link);
					
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		List<String> needCrawlPageUrlList  = new ArrayList<String>(); 
		for (String s : columnList) {
			needCrawlPageUrlList.add(s);
			String nextUrl = getNextUrl(s);
			if (!s.endsWith("/")) {
				s = s + "/";
			}
			
			needCrawlPageUrlList.add(s + nextUrl);
			while (StringUtils.isNotEmpty(nextUrl)) {
				
				nextUrl = getNextUrl(s + nextUrl);
				needCrawlPageUrlList.add(s + nextUrl);
			}
		}
		for (String nextUrl : needCrawlPageUrlList) {
			
			try {
				savePicInfo(crawlListPic(nextUrl));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public static List<JSONObject> crawlListPic(String url) {
		List<JSONObject> saveInfoList = new ArrayList<JSONObject>();
		try {
			Document doc = Jsoup.connect(url).get();
			Elements divNav = doc.select("li.col-xs-1-5");
			if (divNav.size() == 0 ) {
				 divNav = doc.select("div.container.pic3list").select("li");
			}
			for (Element e : divNav) {
				Elements es = e.getElementsByTag("a");
				String hrefurl = es.get(0).attr("href");
				String[] hrefurls = hrefurl.split("/");
				System.out.println("hrefurl = " + hrefurl);
				String dirName = getDirName(hrefurl);
				String prePic = es.get(0).getElementsByTag("img").attr("src");
				String preAlt = es.get(0).getElementsByTag("img").attr("alt");
				String preTitle = es.get(1).attr("title");
				String bText = es.get(1).getElementsByTag("b").text();
				String content = es.get(3).text();
				String time = e.getElementsByTag("span").get(0).text();
				JSONObject j = new JSONObject();
				JSONObject jListPageInfo = new JSONObject();
				j.put("prePic", prePic);
				jListPageInfo.put("preAlt", preAlt);
				jListPageInfo.put("preTitle", preTitle);
				jListPageInfo.put("bText", bText);
				jListPageInfo.put("content", content);
				jListPageInfo.put("time", time);
				j.put("dirName", dirName);
				j.put("jListPageInfo", jListPageInfo.toJSONString());
				j.put("detailPageInfo", getDetailPageInfo(hrefurl));
				saveInfoList.add(j);
				
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return saveInfoList;
	}
	public static List<String> getDetailPageInfoList(String detailUrl)  {
		
		List<String> detailPageInfoUrls = new ArrayList<String>();
		detailPageInfoUrls.add(detailUrl);
		String nextUrl = getDetailNextUrl(detailUrl) ;
		String urlPrefix = "http://www.tu11.com/" + detailUrl.split("/")[3] + "/" + detailUrl.split("/")[4] + "/";
		if (StringUtils.isEmpty(nextUrl)) {
			return detailPageInfoUrls;
		}
		detailPageInfoUrls.add(urlPrefix + nextUrl);
		while (StringUtils.isNotEmpty(nextUrl)) {
			nextUrl = getDetailNextUrl(urlPrefix + nextUrl) ;
			if (StringUtils.isNotEmpty(nextUrl)) {
				detailPageInfoUrls.add(urlPrefix + nextUrl);
			}
			
		}
		return detailPageInfoUrls;
	}
	private static String getDetailNextUrl(String detailUrl) {
		Document doc;
		try {
			doc = Jsoup.connect(detailUrl).get();
			Elements divNav = doc.select("div.row.dede_pages");
			Elements as = divNav.get(0).getElementsByTag("a");
			for (Element e : as) {
				String text = e.text();
				if (StringUtils.equals(text, "下一�?")) {
					if (StringUtils.equals("#", e.attr("href"))) {
						return null;
					}
					return e.attr("href");
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
		
	}
	public static List<JSONObject> getDetailPageInfo(String detailUrl)  {
		List<String> detailPageInfoUrls = getDetailPageInfoList(detailUrl);
		List<JSONObject> jList = new ArrayList<JSONObject>();
		for (int i = 0; i < detailPageInfoUrls.size(); i++) {
			Document doc;
			try {
				JSONObject j = new JSONObject();
				JSONObject jDetailPageInfo = new JSONObject();
				doc = Jsoup.connect(detailPageInfoUrls.get(i)).get();
				String detailKeyWords = doc.select("meta[name=keywords]").get(0).attr("content");
				String detailDescription = doc.select("meta[name=description]").get(0).attr("content");
				String detailHeadTitle = doc.title();
				jDetailPageInfo.put("detailKeyWords", detailKeyWords);
				jDetailPageInfo.put("detailDescription", detailDescription);
				jDetailPageInfo.put("detailHeadTitle", detailHeadTitle);
				j.put("jDetailPageInfo", jDetailPageInfo);
				Element divNav = doc.select("div.nry").get(0);
				Elements imgs = divNav.getElementsByTag("img");
				List<String> detailImgList = new ArrayList<String>();
				for (Element e : imgs) {
					detailImgList.add(e.attr("src"));
				}
				j.put("detailImgList", detailImgList);
				jList.add(j);
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return jList;
	}
	public static void savePicInfo(List<JSONObject> saveInfoList) throws Exception {
		for (JSONObject j : saveInfoList) {
			String dir = Constants.PIC_ADDR + "/" + j.getString("dirName");
			String[] pics = j.getString("prePic").split("/");
			
			String fileName =  "pre_" + pics[pics.length - 1];
			File fileDir = new File(dir); 
			if (!fileDir.exists()) {
				fileDir.mkdirs(); 
			} else {
				continue;
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
					download(detailImgUrl, detailFileNmae, detaildir);
				}
			}
			
			download(j.getString("prePic"), fileName, dir);
			String listPageInfo = j.get("jListPageInfo").toString();
			
			
			String listPageInfoFilename = dir + "/listPageInfo.json";
			
			creatFile(listPageInfoFilename, listPageInfo);
			
			System.out.println("savePicInfo dir = " + dir);
		}
		
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
		if (StringUtils.contains(filename, "null")) {
			return;
		}
		if (StringUtils.contains(filename, "2012") || StringUtils.contains(filename, "2011") 
				|| StringUtils.contains(filename, "2013")) {
			return;
		}
        URL url = new URL(urlString);  
     
        URLConnection con = url.openConnection();  
      
        con.setConnectTimeout(5*1000);  

        InputStream is = con.getInputStream();  
      
 
        byte[] bs = new byte[1024];  
   
        int len;  

       File sf=new File(savePath);  
       if(!sf.exists()){  
           sf.mkdirs();  
       }  
       OutputStream os = new FileOutputStream(sf.getPath()+"/"+filename);  

        while ((len = is.read(bs)) != -1) {  
          os.write(bs, 0, len);  
        }  
  
        os.close();  
        is.close();  
    }
	
	public static String getDirName(String hrefurl) {
		String[] hrefurls = hrefurl.split("/");
		String preifx = Constants.titleLinkMap.get(hrefurls[3]) + "_";
		String end = hrefurls[5].replaceAll(".html", "");
		return preifx + hrefurls[4] + end;
	}
	
	public static String getNextUrl(String url) {
		try {
			Document doc = Jsoup.connect(url).get();
			Elements divNav = doc.select("div.pageinfo");
			Elements as = divNav.get(0).getElementsByTag("a");
			for (Element e : as) {
				String text = e.text();
				if (StringUtils.equals(text, "下一�?1111ß")) {
					return e.attr("href");
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
		
	}
	
	public static void crawlMSite() throws Exception {
		String url = "http://m.tu11.com/";
		List<JSONObject> mIndexImgList = new ArrayList<JSONObject>();
		try {
			Document doc = Jsoup.connect(url).get();
			Elements ulNav = doc.select("ul.listpic.clearfix");
			Elements as = ulNav.get(0).getElementsByTag("a");
			
			for (int i = 0; i < as.size(); i++) {
				Element e = as.get(i);
				JSONObject j = new JSONObject();
				String urll = e.attr("href");
				String link = e.getElementsByTag("img").attr("src");
				String alt = e.getElementsByTag("img").attr("alt");
				j.put("imgLink_" + i, link);
				j.put("imgalt_" + i, alt);
				mIndexImgList.add(j);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		String dir = Constants.PIC_ADDR + "/20_index";
		JSONObject j = new JSONObject();
		for (int i = 0; i < mIndexImgList.size(); i++) {
			JSONObject jo = mIndexImgList.get(i);
			String urlString = jo.getString("imgLink_" + i);
			String[] urlStrings =  urlString.split("/");
			String filename = i + "_" + urlStrings[urlStrings.length - 1];
			String alt = jo.getString("imgalt_" + i);
			download(urlString, filename, dir);
			j.put("imgalt_" + i, alt);
			
		}
		creatFile(dir + "/indexImgAlt.json", j.toJSONString());
		
	}
	public static void main(String[] args) throws Exception {
		crawlSite1();
		crawlMSite();
//		savePicInfo(crawlListPic("http://www.tu11.com/xingganmeinvxiezhen/list_1_2.html"));
	}

}
