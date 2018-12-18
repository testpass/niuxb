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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.jsoup.Connection;
import org.jsoup.Connection.Method;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.alibaba.fastjson.JSONObject;

public class CrawXSiteUtil {
	private static Map<String, String> cookies = null;
	public static void main(String[] args) throws IOException {
		crawlSite1();
	}
	
	public static void crawlSite1() throws IOException {
		String url = "http://www.maya930.com/forumdisplay.php?fid=5";
		getLoginData();
		
		
        Document document = Jsoup.connect(url).cookies(cookies).post();
        List<Element> et = document.select("form");
        List<Element> tables = et.get(0).getElementsByTag("table");
        
        for (Element e : tables) {
        	List<Element> tr = e.getElementsByTag("tr");
        }
	}
	
	
	private static void getLoginData() throws IOException {
		String loginUrl = "http://www.maya930.com/logging.php?action=login";
		Connection con = Jsoup.connect(loginUrl);// 获取连接
		con.header("User-Agent",
	                "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:29.0) Gecko/20100101 Firefox/29.0");// 配置模拟浏览器
	    Response rs = con.execute();// 获取响应
	    Document d1 = Jsoup.parse(rs.body());
	    List<Element> et = d1.select("form");
	    String formhash = et.get(0).getElementsByTag("input").get(0).val();
	    String referer = "http%3A%2F%2Fwww.maya930.com%2Fforumdisplay.php%3Ffid%3D5";
	    String loginfield = "loginfield";
	    String questionid = "0";
	    String answer = "";
	    String cookietime = "2592000";
	    String styleid = "";
	    String loginmode = "";
	    String loginsubmit = "%CC%E1+%26%23160%3B+%BD%BB";
	    
	    Map<String,String> datas=new HashMap<String, String>();
		datas.put("username", "XXXXX");
		datas.put("password", "XXXX");
		datas.put("formhash", formhash);
		datas.put("referer", referer);
		datas.put("loginfield", loginfield);
		datas.put("questionid", questionid);
		datas.put("answer", answer);
		datas.put("cookietime", cookietime);
		datas.put("styleid", styleid);
		datas.put("loginmode", loginmode);
		datas.put("loginsubmit", loginsubmit);
		cookies = rs.cookies();
		Connection con2 = Jsoup.connect("http://www.maya930.com/logging.php?action=login&cookietime=2592000&loginfield=username&username=hiahia&password=198218&questionid=0&answer=&loginsubmit=%BB%E1%D4%B1%B5%C7%C2%BC");
		Response login = con2.ignoreContentType(true).cookies(cookies).method(Method.POST).execute();
		Document d3 = Jsoup.parse(login.body());
		List<Element> et1 = d3.select("form");
		cookies = login.cookies();
	}
//	public static void crawlSite1() {
		
//		// TODO Auto-generated method stub
//		String url = "http://1024xx3.org/pw/thread.php?fid=13";
//		List<String> columnList = new ArrayList<String>(); 
//		try {
//			Document doc = Jsoup.connect(url).get();
//			Elements divNav = doc.select("div.container-fluid.topnav.navbar");
//			Elements as = divNav.get(0).getElementsByClass("list-inline navul").get(0).getElementsByTag("a");
//			
//			for (Element e : as) {
//				String link = e.attr("href");
//				if (!StringUtils.equals(link, "http://www.tu11.com/")) {
////					if (StringUtils.contains(link, "cosplay") ) {
////						columnList.add(link);
////					}
////					if (StringUtils.contains(link, "meituisiwatupian") ) {
////						continue;
////					}
////					if (StringUtils.contains(link, "qingchunmeinvxiezhen") ) {
////						continue;
////					}
//					columnList.add(link);
//					
//				}
//			}
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		columnList.add("http://1024xx3.org/pw/thread.php?fid=14");
//		columnList.add("http://1024xx3.org/pw/thread.php?fid=15");
//		columnList.add("http://1024xx3.org/pw/thread.php?fid=16");
//		columnList.add("http://1024xx3.org/pw/thread.php?fid=49");
//		List<String> needCrawlPageUrlList  = new ArrayList<String>(); 
//		for (String s : columnList) {
//			needCrawlPageUrlList.add(s);
//			String nextUrl = getNextUrl(s);
//			if (!s.endsWith("/")) {
//				s = s + "/";
//			}
//			
//			needCrawlPageUrlList.add(s + nextUrl);
//			while (StringUtils.isNotEmpty(nextUrl)) {
//				
//				nextUrl = getNextUrl(s + nextUrl);
//				needCrawlPageUrlList.add(s + nextUrl);
//			}
//		}
//		for (int type = 0; type < 4; type++) {
//			
//			try {
//				savePicInfo(crawlListPic(type));
//			} catch (Exception e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
//	}
	
	public static List<JSONObject> crawlListPic(int type) throws Exception {
		Map<Integer, String> urlMap = new HashMap<Integer, String>();
		urlMap.put(0, "http://1024xx3.org/pw/thread.php?fid=14&page=");
		urlMap.put(1, "http://1024xx3.org/pw/thread.php?fid=15&page=");
		urlMap.put(2, "http://1024xx3.org/pw/thread.php?fid=16&page=");
		urlMap.put(3, "http://1024xx3.org/pw/thread.php?fid=49&page=");
		
		Map<Integer, Integer> pageListMap = new HashMap<Integer, Integer>();
		pageListMap.put(0, 528);
		pageListMap.put(1, 596);
		pageListMap.put(2, 442);
		pageListMap.put(3, 186);
		List<JSONObject> saveInfoList = new ArrayList<JSONObject>();
		for (int i = pageListMap.get(type); i >0; i--) {
			
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
			List<JSONObject> singleLinkList = new ArrayList<JSONObject>();
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
				if(id < 278654) {
					continue;
				}
				String title = tr.getElementsByTag("a").get(1).text();
				String dirName = getDirName(type, id);
				j.put("title", tr.getElementsByTag("a").get(1).text());
				j.put("link", link);
				
				jListPageInfo.put("preAlt", title);
				jListPageInfo.put("preTitle", title);
				jListPageInfo.put("bText", title);
				jListPageInfo.put("content", title);
//				jListPageInfo.put("time", time);
				List<JSONObject> detailPageInfos = getDetailPageInfo(link, title);
				if (detailPageInfos == null) {
					continue;
				}
				try {
					j.put("prePic", ((List)detailPageInfos.get(0).get("detailImgList")).get(0));
				} catch (Exception e) {
					continue;
				}
				
				j.put("dirName", dirName);
				j.put("jListPageInfo", jListPageInfo.toJSONString());
				j.put("detailPageInfo", detailPageInfos);
				index++;
				System.out.println("index = " + index);
				saveInfoList.add(j);
			}
			savePicInfo(saveInfoList);
//			int listSize = singleLinkList.size() - 1;
//			for (int ji = listSize; ji >=0; ji--) {
//				
//				crawlSingle(singleLinkList.get(ji).get("link").toString(), singleLinkList.get(ji));
//			}
		}
		
		
		return saveInfoList;
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

	public static List<JSONObject> getDetailPageInfo(String detailUrl, String title) throws IOException  {
//		List<String> detailPageInfoUrls = getDetailPageInfoList(detailUrl);
		List<JSONObject> jList = new ArrayList<JSONObject>();
		Document detailDoc = null;
		try {
			detailDoc =  Jsoup.connect(detailUrl).get();
		} catch (Exception e) {
			return null;
		}
		
		StringBuilder sb = new StringBuilder();
		//subject_tpc
		Element subjectTpc = detailDoc.getElementById("subject_tpc");
		sb.append("[b]").append(subjectTpc.text()).append("[/b]\r");
		sb.append("=========================================== ");
		Element readTpc = detailDoc.getElementById("read_tpc");
		Elements es = readTpc.getElementsByTag("img");
		int page = es.size() / 3 + 1;
		for (int i = 0; i < page; i++) {
			Document doc;
			try {
				JSONObject j = new JSONObject();
				JSONObject jDetailPageInfo = new JSONObject();
//				doc = Jsoup.connect(detailPageInfoUrls.get(i)).get();
//				String detailKeyWords = doc.select("meta[name=keywords]").get(0).attr("content");
//				String detailDescription = doc.select("meta[name=description]").get(0).attr("content");
//				String detailHeadTitle = doc.title();
				jDetailPageInfo.put("detailKeyWords", title + i);
				jDetailPageInfo.put("detailDescription", title + i);
				jDetailPageInfo.put("detailHeadTitle", title + i);
				j.put("jDetailPageInfo", jDetailPageInfo);
//				Element divNav = doc.select("div.nry").get(0);
//				Elements imgs = divNav.getElementsByTag("img");
				List<String> detailImgList = new ArrayList<String>();
				for (int ji = 0; ji < 3; ji++) {
					if ((i * 3 + ji) >=  es.size()) {
						continue;
					}
					detailImgList.add(es.get(i * 3 + ji).attr("src"));
				}
				
				j.put("detailImgList", detailImgList);
				jList.add(j);
				
			} catch (Exception e) {
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
		try {
			if (StringUtils.contains(filename, "null")) {
				return;
			}
			if (StringUtils.contains(filename, "2012") || StringUtils.contains(filename, "2011") 
					|| StringUtils.contains(filename, "2013")) {
				return;
			}
	        URL url = new URL(urlString);  
	     
	        URLConnection con = url.openConnection();  
	        con.setRequestProperty("User-Agent",  "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.22 (KHTML, like Gecko) Chrome/25.0.1364.160 Safari/537.22"); 
	        con.setRequestProperty("Host",  url.getHost());  //������ ��ķ�������ַ������������***.**.***.***
	        con.setRequestProperty("Content-Type", " application/json");//�趨 �����ʽ json��Ҳ�����趨xml��ʽ��
	        con.setRequestProperty("Accept-Charset", "utf-8");  //���ñ�������
	        con.setRequestProperty("X-Auth-Token", "token");  //���������token
	        con.setRequestProperty("Connection", "keep-alive");  //�������ӵ�״̬
	        con.setRequestProperty("Transfer-Encoding", "chunked");//���ô������
//	        con.setRequestProperty("Content-Length", obj.toString().getBytes().length + "");  //�����ļ�����ĳ���         
	        con.setReadTimeout(10000);//���ö�ȡ��ʱʱ��          
	        con.setConnectTimeout(10000);//�������ӳ�ʱʱ��       
	      
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
		} catch(Exception e) {
			
		}
		
    }
	
	public static String getDirName(int type, int id) {
		String preifx = type + "_";
		return preifx + id;
	}
	
//	public static String getNextUrl(String url) {
//		try {
//			Document doc = Jsoup.connect(url).get();
//			Elements divNav = doc.select("div.pageinfo");
//			Elements as = divNav.get(0).getElementsByTag("a");
//			for (Element e : as) {
//				String text = e.text();
//				if (StringUtils.equals(text, "下一�?1111ß")) {
//					return e.attr("href");
//				}
//			}
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		return "";
//		
//	}
	
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


}
