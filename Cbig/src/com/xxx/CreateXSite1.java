package com.xxx;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.alibaba.fastjson.JSONObject;

public class CreateXSite1 {
	static Map<String, String> urlMap = new HashMap<String, String>();
	static{
		urlMap.put("0", "http://1024xx3.org/pw/thread.php?fid=14&page=");
		urlMap.put("1", "http://1024xx3.org/pw/thread.php?fid=15&page=");
		urlMap.put("2", "http://1024xx3.org/pw/thread.php?fid=16&page=");
		urlMap.put("3", "http://1024xx3.org/pw/thread.php?fid=49&page=");
	}
	
	static Map<String, Integer> pageListMap = new HashMap<String, Integer>();
	static{
		pageListMap.put("0", 528);
		pageListMap.put("1", 50);
		pageListMap.put("2", 442);
		pageListMap.put("3", 186);
	}
	
	public final static Map<String, String> titleTypeMap = new HashMap<String, String>();
	static{
		titleTypeMap.put("1", "wangyouzipai");
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
		
	}
	public static void main(String[] args) throws Exception {
		List<JSONObject> pageInfoList = getPageInfoList("1");
		createListPage(pageInfoList, "1");
//		PCPageUtil.genListPage1("6");   
//		PCPageUtil.genDetailPage1("6");
	}
	
	
	
	private static String genListContent(int pageIndex, List<JSONObject> pageInfoList, String type) {
		StringBuilder sb = new StringBuilder();
		sb.append("<ul>");
		int start = pageIndex * 20;
		int end = ((pageIndex + 1) * 20 < pageInfoList.size()) ?  (pageIndex + 1) * 20 : pageInfoList.size() -1;
		for(int i = start; i < end; i++) {
			JSONObject listPageInfoj = pageInfoList.get(i);
			JSONObject prelistPageInfoj = null;
			JSONObject nextlistPageInfoj = null;
			if (i != 0) {
				prelistPageInfoj = pageInfoList.get(i - 1);
			}
			if(i < pageInfoList.size() - 1) {
				nextlistPageInfoj = pageInfoList.get(i + 1);
			}
			JSONObject jListPageInfo = listPageInfoj.getJSONObject("jListPageInfo");
			String bText = (jListPageInfo == null) ? "XXX" : jListPageInfo.get("bText").toString();
			String preTitle = (jListPageInfo == null) ? "XXX" : jListPageInfo.get("preTitle").toString();
			String time ="";
			sb.append("<li class=\"col-xs-1-5\"><div class=\"shupic\"><a href=\"detail/");
			sb.append(listPageInfoj.get("picId") + "_0.html");
			sb.append("\" target=\"_blank\"><img class=\"img-responsive picheng\" src=\"");
			sb.append(listPageInfoj.get("prePic"));
			sb.append("\" alt=\"" + bText + "\"></a>");//待定
			sb.append("<p class=\"textbox2\"><a href=\"");
			sb.append(jListPageInfo.get("picId") + "_0.html");
			sb.append("\" class=\"biaoti center-block text-center\" title=\"");
			sb.append(preTitle);//待定
			sb.append("\" target=\"_blank\"><b>");
			sb.append(bText);//待定
			sb.append("</b></a>");
			sb.append("<a href=\"");
			sb.append(jListPageInfo.get("picId") + ".html");
			sb.append("\" class=\"leibie\"></a><a href=\"");
			sb.append("");
			sb.append("\">");
			sb.append(Constants.titleMap.get(type));
			sb.append("</a><span class=\"time\">");
			sb.append(time);//待定
			sb.append("</span></p></div></li>");
			genDetailPage1(type, listPageInfoj, prelistPageInfoj, nextlistPageInfoj, pageInfoList);
		}
		
		sb.append("</ul>");
		sb.append("</div></div>");
		String ret = sb.toString().replace("亿秀网", "我要撸啦啦");
		return ret;
	}
	
	public static void genDetailPage1(String type, JSONObject listPageInfoj, JSONObject prelistPageInfoj, JSONObject nextlistPageInfoj, List<JSONObject> pageInfoList) {
		JSONObject detailPageInfo = listPageInfoj.getJSONObject("detailPageInfo");
		
		JSONObject jDetailPageInfo = detailPageInfo.getJSONObject("jDetailPageInfo");
		List detailImgList = (List)detailPageInfo.get("detailImgList");
		int pageSize = detailImgList.size()/3 + 1;
		for (int i = 0; i < pageSize; i++) {
			String detailKeyWords = (jDetailPageInfo == null) ? "" : jDetailPageInfo.getString("detailKeyWords");
			String detailHeadTitle = (jDetailPageInfo == null) ? "" : jDetailPageInfo.getString("detailHeadTitle");
			String detailDescription = (jDetailPageInfo == null) ? "" : jDetailPageInfo.getString("detailDescription");
			String detailHtml = PCPageUtil.readFileByLines(Constants.DETAIL_HTML);
			detailHtml = detailHtml.replace("detailKeyWords", detailKeyWords);
			detailHtml = detailHtml.replace("detailDescription", detailDescription);
			detailHtml = detailHtml.replace("detailHeadTitle", detailHeadTitle);
			detailHtml = detailHtml.replace("detailPageTitle", detailHeadTitle);
			detailHtml = detailHtml.replace("亿秀网", "51lulala");
			int start = (i * 3);
			int end = ((i + 1) * 3) < detailImgList.size() ? ((i + 1) * 3) : detailImgList.size() - 1;
			StringBuilder contentImg = new StringBuilder();
			for (int indexj = start; indexj < end; indexj++) {
				String detailImg = (String)detailImgList.get(indexj);
				contentImg.append("<p align=\"center\">");
				contentImg.append("<img src=\"").append(detailImg).append("\" />");
				contentImg.append("</p><br/>");
			}
			StringBuilder sbPageInfo = new StringBuilder();
			sbPageInfo.append("<a>共" + pageSize + "页: </a>").append("</li><li>");
			
			if (i == 0) {
				sbPageInfo.append("<li><a href='#'>上一页</a></li>");
			} else {
				sbPageInfo.append("<li><a href='" + listPageInfoj.get("picId") + "_" + (i - 1) + ".html" + "'>上一页</a></li>");
			}
			for (int m = 0; m < pageSize; m++) {
				sbPageInfo.append("<li>");
				if (m == i) {
					sbPageInfo.append("<a href='#'>" + (i + 1)  + "</a>");
				} else {
					sbPageInfo.append("<a href='" + listPageInfoj.get("picId") + "_" + (m) + ".html" +"'>" + (m + 1)  + "</a>");
				}
				sbPageInfo.append("</li>");
			}
			if (i == (pageSize -1)) {
				sbPageInfo.append("<li><a href='#'>下一页</a></li>");
			} else {
				sbPageInfo.append("<li><a href='" + listPageInfoj.get("picId") + "_" + (i + 1) + ".html" + "'>下一页</a></li>");
			}
			String nextPath = "";
			String previousPath = "";
			if(prelistPageInfoj == null) {
				previousPath = "<p>下一篇：没有了 </p>";
			} else {
				JSONObject preDetailPageInfo = prelistPageInfoj.getJSONObject("detailPageInfo");
				
				if (prelistPageInfoj != null && preDetailPageInfo != null) {
					previousPath = "<p>下一篇：<a href='"  + prelistPageInfoj.get("picId") + "_0.html'>" + preDetailPageInfo.getJSONObject("jDetailPageInfo").getString("detailKeyWords") + "</a> </p>";
				}
				
			}
			
			if(nextlistPageInfoj == null) {
				nextPath = "<p>上一篇：没有了 </p>";
			} else {
				
				JSONObject nextDetailPageInfo = nextlistPageInfoj.getJSONObject("detailPageInfo");
				
				if (nextlistPageInfoj != null && nextDetailPageInfo != null) {
					nextPath = "<p>上一篇：<a href='"  + nextlistPageInfoj.get("picId") + "_0.html'>" + nextDetailPageInfo.getJSONObject("jDetailPageInfo").getString("detailKeyWords") + "</a> </p>";
				}
				
				
			}
			detailHtml = detailHtml.replace("contentImg", contentImg.toString());
			detailHtml = detailHtml.replace("nextPath", nextPath);
			detailHtml = detailHtml.replace("previousPath", previousPath);
			detailHtml = detailHtml.replace("detailPageInfo", sbPageInfo.toString());
			detailHtml = detailHtml.replace("recommendationHtml1", getRecommendationHtml(pageInfoList.size(), pageInfoList));
			detailHtml = detailHtml.replace("recommendationHtml2",  getRecommendationHtml(pageInfoList.size(), pageInfoList));
			detailHtml = detailHtml.replace("siteRecommendationHtml", "");
			genDeatilHtml(type, listPageInfoj.get("picId") + "_" + i, detailHtml.toString());
			
		}
		
	}
	
	private static String getRecommendationHtml(int pageSize, List<JSONObject> pageList) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < 6; i++) {
			Random rand = new Random();
			int randNum = rand.nextInt(pageSize);
			JSONObject randj = pageList.get(randNum);
			JSONObject randListPageInfoj = null;
			
			sb.append("<li class=\"col-xs-2 shu6\">");
			sb.append("<div class=\"spb shupic1\">");
			sb.append("<a href=\"").append(randj.get("picId") + "_0.html\">");
			sb.append("<img class=\"img-responsive\" src=\"").append(randj.get("prePic"));
			sb.append("\" alt=\"" + randj.getString("title")+ "\">");
			sb.append("<a href=\"").append(randj.get("picId")  + "_0.html\"");
			sb.append(" title=\"" + randj.getString("title")+ "\">");
			sb.append("<p class=\"text1\">" + randj.getString("title")+ "</p>");
			sb.append("</a></div></li>");
		}
		return sb.toString();
		
	}
	
	private static void genDeatilHtml(String type, String fileName, String html) {
		try {
			String filename = getDetailFileDir(type) + "/" + fileName + ".html";
			File file = new File(filename);
			System.out.println("filename = " + filename);
			File fileDir = new File(getDetailFileDir(type)); 
			
			if (!fileDir.exists()) {
				fileDir.mkdirs(); 
			}
			if (!file.exists()) {
				file.createNewFile();
			} else {
				file.delete();
				file.createNewFile();
			}
			PrintStream printStream = new PrintStream(filename);
			printStream.println(html);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
	
	private static String getDetailFileDir(String type) {
		return Constants.PAGE_ADDR + "/" + titleTypeMap.get(type) + "/detail";
	}
	public static void createListPage(List<JSONObject> pageInfoList, String type) throws Exception {
		long pageListSize = pageInfoList.size() / 20 + 1;
		String headHtml = PCPageUtil.readFileByLines(Constants.HEAD_HTML);
		String bottomHtml = PCPageUtil.readFileByLines(Constants.BOTTOM_HTML);
		for (int i = 0; i < pageListSize; i++) {
			int endLength = (i + 1) * 20 < pageInfoList.size() ?  (i + 1) * 20 : pageInfoList.size();
			List<JSONObject> curPageList = pageInfoList.subList(i * 20, endLength);
			StringBuilder sb = new StringBuilder();
			sb.append(headHtml);
			sb.append(genListContent(i, pageInfoList, type));
			sb.append(genPageinfo(curPageList, type, i + 1, pageListSize));
			sb.append(bottomHtml);
			genHtml(type, i + 1, sb.toString());
		}
	}
	
	private static void genHtml(String type, int curPageNum, String html) {
		try {
			String filename = getFileName(type, curPageNum);
			
			File file = new File(filename);
			File fileDir = new File(getFileDir(type)); 
			
			if (!fileDir.exists()) {
				fileDir.mkdirs(); 
			}
			if (!file.exists()) {
				file.createNewFile();
			} else {
				file.delete();
				file.createNewFile();
			}
			PrintStream printStream = new PrintStream(getFileName(type, curPageNum));
			printStream.println(html);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
	private static String getFileName(String type, int curPageNum) {
		return getFileDir(type) + "/" + curPageNum + ".html";
	}
	private static String getFileDir(String type) {
		return Constants.PAGE_ADDR + "/" + titleTypeMap.get(type);
	}
	private static String genPageinfo(List<JSONObject> curPageList, String type, int curPage, long totalNum) {
		StringBuilder sb = new StringBuilder();
		sb.append("<div class=\"jianju30\"></div><div class=\"container\"><div class=\"pageinfo\">");
		if (curPage == 1) {
			sb.append("<span class=\"disabled\">首页</span>");
		} else {
			sb.append("<a href=\"" + "1.html" + "\">首页</a>");
			sb.append("<a href=\"" + (curPage - 1) + ".html" + "\">上一页</a>");
		}
		if (curPage <= 10) {
			long maxPage = (totalNum < 10) ? totalNum : 11;
			for (int i = 0; i < maxPage; i++) {
				if (curPage == (i + 1)) {
					sb.append("<span class=\"thisclass\">" + curPage + "</span>");
					
				} else {
					sb.append("<a href=\"" +  (i + 1)  + ".html\">" + (i + 1)  + "</a>");
				}
				
			}
		} else {
			long maxPage = (totalNum < (curPage + 5)) ? totalNum : (curPage + 5);
			for (int i = curPage - 5; i < maxPage; i++) {
				if (curPage == (i + 1)) {
					sb.append("<span class=\"thisclass\">" + curPage + "</span>");
					
				} else {
					sb.append("<a href=\"" +  (i + 1)  + ".html\">" + (i + 1)  + "</a>");
				}
				
			}
		}
		
		if (curPage == totalNum) {
			sb.append("<span class=\"thisclass\">末页</span>");
		} else {
			
			sb.append("<a href=\"" + (curPage + 1) + ".html" + "\">下一页</a>").append("<a href=\"" +  totalNum  + ".html\">末页</a>");
		}
		
		sb.append("</div></div>");
		return sb.toString();
	}
	
	public static List<JSONObject> getPageInfoList(String type) throws IOException {
		List<JSONObject> saveInfoList = new ArrayList<JSONObject>();
		for (int i = pageListMap.get(type); i > 0; i--) {
			String url = urlMap.get(type) + i ;
			Document doc = null;
			try {
				doc =  Jsoup.connect(url).get();
			} catch (Exception e) {
				System.out.println("error url == " + url);
				continue;
			}
			
			Element div = doc.select("div.t.z").get(0);
			Element tbody = div.getElementsByTag("tbody").get(1);
			Elements trs = tbody.getElementsByTag("tr");
			String prefix = "http://1024xx3.org/pw/";
			int index = 0;
			System.out.println("tr size = " + trs.size());
			for (Element tr : trs) {
				JSONObject j = new JSONObject();
				JSONObject jListPageInfo = new JSONObject();
				String link = prefix + tr.getElementsByTag("a").attr("href");
				int id = 0;
				try {
					id = getId(link);
				} catch (Exception e) {
					id = 0;
				}
				if (id == 0) {
					continue;
				}
				
				String title = tr.getElementsByTag("a").get(1).text();
				System.out.println("title == " + title);
				if(StringUtils.contains(title, "在线AV影院")) {
					continue;
				}
				if(StringUtils.contains(title, "老司机翻墙推荐")) {
					continue;
				}
				j.put("title", tr.getElementsByTag("a").get(1).text());
				j.put("link", link);
				jListPageInfo.put("preAlt", title);
				jListPageInfo.put("preTitle", title);
				jListPageInfo.put("bText", title);
				jListPageInfo.put("content", title);
//				jListPageInfo.put("time", time);
				JSONObject detailPageInfo = getDetailPageInfo(link, title);
				if (detailPageInfo == null) {
					continue;
				}
				try {
					j.put("prePic", ((List)detailPageInfo.get("detailImgList")).get(0));
				} catch (Exception e) {
					continue;
				}
				
				j.put("jListPageInfo", jListPageInfo.toJSONString());
				j.put("detailPageInfo", detailPageInfo);
				j.put("picId", id);
				index++;
				saveInfoList.add(j);
			}
		}
		return saveInfoList;
	}
	
	public static JSONObject getDetailPageInfo(String detailUrl, String title) throws IOException  {
		List<JSONObject> jList = new ArrayList<JSONObject>();
		Document detailDoc = null;
		try {
			detailDoc =  Jsoup.connect(detailUrl).get();
		} catch (Exception e) {
			return null;
		}
		
		Element readTpc = detailDoc.getElementById("read_tpc");
		Elements es = readTpc.getElementsByTag("img");
		JSONObject jDetailPageInfo = new JSONObject();
		jDetailPageInfo.put("detailKeyWords", title);
		jDetailPageInfo.put("detailDescription", title);
		jDetailPageInfo.put("detailHeadTitle", title);
		List<String> detailImgList = new ArrayList<String>();
		JSONObject j = new JSONObject();
		for (int i = 0; i < es.size(); i++) {
			try {
				detailImgList.add(es.get(i).attr("src"));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		j.put("jDetailPageInfo", jDetailPageInfo);
		j.put("detailImgList", detailImgList);
		return j;
	}
	
	public static int getId(String link) {
		String[] links = link.split("/");
		int id = 0;
		try {
			id = Integer.parseInt(links[7].split(".html")[0]);
		} catch (Exception e) {
			String ids = links[4];
			int fi = ids.indexOf("read.php?tid=");
			ids.substring(fi + 13, fi + 19);
			id = Integer.parseInt(ids.substring(fi + 13, fi + 19));
		}
		
		return id;
	}
	
	public static String getDirName(int type, int id) {
		String preifx = type + "_";
		return preifx + id;
	}
}
