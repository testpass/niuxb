package com.xxx;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.commons.lang.StringUtils;

import com.alibaba.fastjson.JSONObject;

public class WapPageUtil {
	public static void genWapListPage1(String type) {
		List<JSONObject> pageList = PageListUtil.getPageListByType(type);
		long totalNum = 0;
		if (StringUtils.equals(type, "100")) {
			pageList = PageListUtil.getPageListNew100();
			totalNum = 100;
		} else {
			pageList = PageListUtil.getPageListByType(type);
			totalNum = pageList.size()/10 + 1;
		}
		
		for (int i = 0; i < totalNum; i++) {
			StringBuilder sb = new StringBuilder();
			
			String wapListHtml = readFileByLines(Constants.WAP_LIST_HTML);
			List<JSONObject> curPageList;
			
			if (totalNum == 100) {
				curPageList = pageList;
			} else {
				if (i == pageList.size()/10) {
					curPageList = pageList.subList(i * 10, pageList.size());
				} else {
					curPageList = pageList.subList(i * 10, (i + 1) * 10);
				}
			}
			
//			if (StringUtils.equals(type, "100")) {
//				sb.append("");
//			} else {
//				sb.append(genWapPageinfo(curPageList, type, i + 1, totalNum));
//			}
			String imgListInfo = genWapListContent(curPageList, type);
			String wapPageinfo = genWapPageinfo(curPageList, i + 1, totalNum);
			wapListHtml = wapListHtml.replace("imgListInfo", imgListInfo);
			
			wapListHtml = wapListHtml.replace("wapPageinfo", wapPageinfo);
			genHtml(type, i + 1, wapListHtml);
		}
	}
	
	public static void genWapDetailPage1(String type) {
		List<JSONObject> pageList = PageListUtil.getPageListByType(type);
		if (StringUtils.equals(type, "100")) {
			pageList = PageListUtil.getPageListNew100();
		}
		for (int i = 0; i < pageList.size(); i++) {
			JSONObject j = pageList.get(i);
			List detailPicList = (List)j.get("detailPicList");
			
			String dir = Constants.PIC_ADDR + "/" + j.get("picId");
			File fileDir = new File(dir);
			
			fileDir.list();
			List<JSONObject> detailImgFileList = (List<JSONObject>)j.get("detailPicList");
			long pageSize = detailImgFileList.size();
			for (JSONObject jb : detailImgFileList) {
				String detailHtml = readFileByLines(Constants.WAP_DETAIL_HTML);
				List<String> list = (List<String>)jb.get("detailImgFileList");
				String detailImgFileName = jb.get("detailImgFileName").toString();
				JSONObject detailPageInfoj = null;
				
				try {
					String detailPageInfoFileName = Constants.PIC_ADDR + "/" +j.get("picId") + "/" + detailImgFileName + "/detailPageInfo.json";
					String detailPageInfoHtml = readFileByLines(detailPageInfoFileName);
					detailPageInfoj = JSONObject.parseObject(detailPageInfoHtml);
				} catch (Exception e) {
					
				}
				String detailKeyWords = (detailPageInfoj == null) ? "" : detailPageInfoj.getString("detailKeyWords");
				String detailHeadTitle = (detailPageInfoj == null) ? "" : detailPageInfoj.getString("detailHeadTitle");
				String detailDescription = (detailPageInfoj == null) ? "" : detailPageInfoj.getString("detailDescription");
				detailHtml = detailHtml.replace("wapDetailKeyWords", detailKeyWords);
				detailHtml = detailHtml.replace("detailDescription", detailDescription);
				detailHtml = detailHtml.replace("wapDetailHeadTitle", detailHeadTitle);
				detailHtml = detailHtml.replace("wapPageTitle", detailHeadTitle);
				detailHtml = detailHtml.replace("亿秀�?", "我爱丝足�?");
				StringBuilder contentImg = new StringBuilder();
				for (String pic : list) {
					String filePath = Constants.PIC_ADDR_NET + "/" + j.get("picId") + "/" + detailImgFileName + "/" + pic;
					contentImg.append("<p align=\"center\">");
					contentImg.append("<img src=\"").append(filePath).append("\" />");
					contentImg.append("</p><br/>");
				}

				StringBuilder sbPageInfo = new StringBuilder();
				sbPageInfo.append("<a>�?" + pageSize + "�?: </a>").append("</li><li>");
				long curPage = Long.parseLong(detailImgFileName);
				if (StringUtils.equals(detailImgFileName, "0")) {
					sbPageInfo.append("<li><a href='#'>上一�?</a></li>");
				} else {
					sbPageInfo.append("<li><a href='" + (String)j.get("picId") + "_" + (curPage - 1) + ".html" + "'>上一�?</a></li>");
				}
				for (int m = 0; m < pageSize; m++) {
					sbPageInfo.append("<li>");
					if (m == curPage) {
						sbPageInfo.append("<a href='#'>" + (curPage + 1)  + "</a>");
					} else {
						sbPageInfo.append("<a href='" + (String)j.get("picId") + "_" + (m) + ".html" +"'>" + (m + 1)  + "</a>");
					}
					sbPageInfo.append("</li>");
				}
				if (curPage == (pageSize -1)) {
					sbPageInfo.append("<li><a href='#'>下一�?</a></li>");
				} else {
					sbPageInfo.append("<li><a href='" + (String)j.get("picId") + "_" + (curPage + 1) + ".html" + "'>下一�?</a></li>");
				}
				String nextPath = "";
				String previousPath = "";
				
				if (i == 0) {
					nextPath = "<p>下一篇：没有�? </p>";
				} else {
					JSONObject nextj = pageList.get(i-1);
					JSONObject nextListPageInfoj = null;
					try {
						String listPageInfoFileName = Constants.PIC_ADDR + "/" + nextj.get("picId") + "/listPageInfo.json";
						String listPageInfoHtml = readFileByLines(listPageInfoFileName);
						nextListPageInfoj = JSONObject.parseObject(listPageInfoHtml);
					} catch (Exception e) {
						
					}
					nextPath = "<p>下一篇：<a href='"  + (String)nextj.get("picId") + "_0.html'>" + nextListPageInfoj.getString("preTitle") + "</a> </p>";
				}
				if(i == (pageList.size() - 1)) {
					previousPath = "<p>上一篇：没有�? </p>";
				} else {
					JSONObject previousj = pageList.get(i+1);
					JSONObject previousListPageInfoj = null;
					try {
						String listPageInfoFileName = Constants.PIC_ADDR + "/" + previousj.get("picId") + "/listPageInfo.json";
						String listPageInfoHtml = readFileByLines(listPageInfoFileName);
						previousListPageInfoj = JSONObject.parseObject(listPageInfoHtml);
					} catch (Exception e) {
						
					}
					previousPath = "<p>上一篇：<a href='"  + (String)previousj.get("picId") + "_0.html'>" + previousListPageInfoj.getString("preTitle") + "</a> </p>";
				}
				detailHtml = detailHtml.replace("wapContentImg", contentImg.toString());
				detailHtml = detailHtml.replace("wapNextPath", nextPath);
				detailHtml = detailHtml.replace("wapPreviousPath", previousPath);
				detailHtml = detailHtml.replace("wapPagelist", sbPageInfo.toString());
				detailHtml = detailHtml.replace("recommendationHtml", getRecommendationHtml(pageList.size(), pageList));
				genDeatilHtml(type, (String)j.get("picId") + "_" + detailImgFileName, detailHtml.toString());
			}
			System.out.println(j.get("picId"));
		}
		
	}
	private static String getSiteRecommendationHtml(int pageSize, List<JSONObject> pageList) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < 20; i++) {
			Random rand = new Random();
			int randNum = rand.nextInt(pageSize);
			JSONObject randj = pageList.get(randNum);
			JSONObject randListPageInfoj = null;
			try {
				String listPageInfoFileName = Constants.PIC_ADDR + "/" + randj.get("picId") + "/listPageInfo.json";
				String listPageInfoHtml = readFileByLines(listPageInfoFileName);
				randListPageInfoj = JSONObject.parseObject(listPageInfoHtml);
			} catch (Exception e) {
				
			}
			sb.append("<li class=\"col-xs-3\"><a href=\"").append((String)randj.get("picId") + "_0.html\"");
			sb.append("title=\"").append(randListPageInfoj.getString("preTitle")).append("\" target=\"_blank\">");
			sb.append(randListPageInfoj.getString("preTitle")).append("</a></li>");
		}
		return sb.toString();
	}
	private static String getRecommendationHtml(int pageSize, List<JSONObject> pageList) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < 6; i++) {
			Random rand = new Random();
			int randNum = rand.nextInt(pageSize);
			JSONObject randj = pageList.get(randNum);
			JSONObject randListPageInfoj = null;
			try {
				String listPageInfoFileName = Constants.PIC_ADDR + "/" + randj.get("picId") + "/listPageInfo.json";
				String listPageInfoHtml = readFileByLines(listPageInfoFileName);
				randListPageInfoj = JSONObject.parseObject(listPageInfoHtml);
			} catch (Exception e) {
				
			}
			sb.append("<li class=\"col-xs-2 shu6\">");
			sb.append("<div class=\"spb shupic1\">");
			sb.append("<a href=\"").append((String)randj.get("picId") + "_0.html\">");
			sb.append("<img class=\"img-responsive\" src=\"").append(randj.get("prePic"));
			sb.append("\" alt=\"" + randListPageInfoj.getString("preAlt")+ "\">");
			sb.append("<a href=\"").append((String)randj.get("picId")  + "_0.html\"");
			sb.append(" title=\"" + randListPageInfoj.getString("preTitle")+ "\">");
			sb.append("<p class=\"text1\">" + randListPageInfoj.getString("preTitle")+ "</p>");
			sb.append("</a></div></li>");
		}
		return sb.toString();
		
	}
	private static void genDeatilHtml(String type, String fileName, String html) {
		try {
			String filename = getWapDetailFileDir(type) + "/" + fileName + ".html";
			File file = new File(filename);
			System.out.println("filename = " + filename);
			File fileDir = new File(getWapDetailFileDir(type)); 
			
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
	
	private static void genHtml(String type, int curPageNum, String html) {
		try {
			String filename = getWapFileName(type, curPageNum);
			
			File file = new File(filename);
			File fileDir = new File(getWapFileDir(type)); 
			
			if (!fileDir.exists()) {
				fileDir.mkdirs(); 
			}
			if (!file.exists()) {
				file.createNewFile();
			} else {
				file.delete();
				file.createNewFile();
			}
			PrintStream printStream = new PrintStream(getWapFileName(type, curPageNum));
			printStream.println(html);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
	private static String getWapFileName(String type, int curPageNum) {
		return getWapFileDir(type) + "/" + curPageNum + ".html";
	}
	private static String getWapFileDir(String type) {
		return Constants.WAP_PAGE_ADDR + "/" + Constants.titleTypeMap.get(type) + "/";
	}
	
	private static String getWapDetailFileDir(String type) {
		return Constants.WAP_PAGE_ADDR + "/" + Constants.titleTypeMap.get(type) + "/detail";
	}
	private static String genWapListContent(List<JSONObject> curPageList, String type) {
		StringBuilder sb = new StringBuilder();
	
		for (JSONObject j : curPageList) {
			JSONObject listPageInfoj = null;
			try {
				String listPageInfoFileName = Constants.PIC_ADDR + "/" +j.get("picId") + "/listPageInfo.json";
				String listPageInfoHtml = readFileByLines(listPageInfoFileName);
				listPageInfoj = JSONObject.parseObject(listPageInfoHtml);
			} catch (Exception e) {
				
			}
			String bText = (listPageInfoj == null) ? "性感美女写真" : listPageInfoj.get("bText").toString();
			String preTitle = (listPageInfoj == null) ? "性感美女写真" : listPageInfoj.get("preTitle").toString();
			String time = (listPageInfoj == null) ? "" : listPageInfoj.get("time").toString();
			
			sb.append("<li><a href=\"detail/");
			sb.append(j.get("picId") + "_0.html");
			sb.append("\" ><img src=\"");
			sb.append(j.get("prePic"));
			sb.append("\" alt=\"" + bText + "\"></a></li>");
		}
		
		return sb.toString();
	}
	
	private static String genWapPageinfo(List<JSONObject> curPageList, int curPage, long totalNum) {
		StringBuilder sb = new StringBuilder();
		sb.append("<div class=\"jianju30\"></div><div class=\"container\"><div class=\"pageinfo\">");
		if (curPage == 1) {
			sb.append("<span class=\"disabled\">首页</span>");
		} else {
			sb.append("<a href=\"" + "1.html" + "\">首页</a>");
			sb.append("<a href=\"" + (curPage - 1) + ".html" + "\">上一�?</a>");
		}
		if (curPage <= 2) {
			long maxPage = (totalNum < 2) ? totalNum : 3;
			for (int i = 0; i < maxPage; i++) {
				if (curPage == (i + 1)) {
					sb.append("<span class=\"thisclass\">" + curPage + "</span>");
					
				} else {
					sb.append("<a href=\"" +  (i + 1)  + ".html\">" + (i + 1)  + "</a>");
				}
				
			}
		} else {
			long maxPage = (totalNum < (curPage + 1)) ? totalNum : (curPage + 1);
			for (int i = curPage - 1; i <= maxPage; i++) {
				if (curPage == i) {
					sb.append("<span class=\"thisclass\">" + curPage + "</span>");
					
				} else {
					sb.append("<a href=\"" +  i  + ".html\">" + i  + "</a>");
				}
				
			}
		}
		
		if (curPage == totalNum) {
			sb.append("<span class=\"thisclass\">末页</span>");
		} else {
			
			sb.append("<a href=\"" + (curPage + 1) + ".html" + "\">下一�?</a>").append("<a href=\"" +  totalNum  + ".html\">末页</a>");
		}
		
		sb.append("</div></div>");
		return sb.toString();
	}
	
	/**
     * 以行为单位读取文件，常用于读面向行的格式化文�?
     */
    public static String readFileByLines(String fileName) {
        File file = new File(fileName);
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
            StringBuilder sb = new StringBuilder();
            int line = 1;
            // �?次读入一行，直到读入null为文件结�?
            while ((tempString = reader.readLine()) != null) {
                // 显示行号
               
                sb.append(tempString);
                line++;
            }
            reader.close();
            return sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }
        return "";
    }
    
    
    
    private static String getIndexFlHtml(int pageSize, List<JSONObject> pageList) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < 5; i++) {
			Random rand = new Random();
			int randNum = rand.nextInt(pageSize);
			JSONObject randj = pageList.get(randNum);
			JSONObject randListPageInfoj = null;
			try {
				String listPageInfoFileName = Constants.PIC_ADDR + "/" + randj.get("picId") + "/listPageInfo.json";
				String listPageInfoHtml = readFileByLines(listPageInfoFileName);
				randListPageInfoj = JSONObject.parseObject(listPageInfoHtml);
			} catch (Exception e) {
				
			}
			sb.append("<li class=\"col-xs-1-5\">");
			sb.append("<div class=\"spb shupic\">");
			sb.append("<a href=\"").append("gaoxiaotupian/detail/" + (String)randj.get("picId") + "_0.html\" target=\"_blank\">");
			sb.append("<img class=\"img-responsive\" src=\"").append(randj.get("prePic"));
			sb.append("\" alt=\"" + randListPageInfoj.getString("preAlt")+ "\">");
			sb.append("<a href=\"").append("gaoxiaotupian/detail/" + (String)randj.get("picId")  + "_0.html\"");
			sb.append(" title=\"" + randListPageInfoj.getString("preTitle")+ "\">");
			sb.append("<p class=\"text1\">" + randListPageInfoj.getString("preTitle")+ "</p>");
			sb.append("</a></div></li>");
		}
		return sb.toString();
		
	}
    
    private static String getIndexXzHtml(int pageSize, List<JSONObject> pageList) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < 5; i++) {
			Random rand = new Random();
			int randNum = rand.nextInt(pageSize);
			JSONObject randj = pageList.get(randNum);
			JSONObject randListPageInfoj = null;
			try {
				String listPageInfoFileName = Constants.PIC_ADDR + "/" + randj.get("picId") + "/listPageInfo.json";
				String listPageInfoHtml = readFileByLines(listPageInfoFileName);
				randListPageInfoj = JSONObject.parseObject(listPageInfoHtml);
			} catch (Exception e) {
				
			}
			sb.append("<li class=\"col-xs-1-5\">");
			sb.append("<div class=\"spb shupic\">");
			sb.append("<a href=\"").append("cosplay/detail/" + (String)randj.get("picId") + "_0.html\" target=\"_blank\">");
			sb.append("<img class=\"img-responsive\" src=\"").append(randj.get("prePic"));
			sb.append("\" alt=\"" + randListPageInfoj.getString("preAlt")+ "\">");
			sb.append("<a href=\"").append("cosplay/detail/" + (String)randj.get("picId")  + "_0.html\"");
			sb.append(" title=\"" + randListPageInfoj.getString("preTitle")+ "\">");
			sb.append("<p class=\"text1\">" + randListPageInfoj.getString("preTitle")+ "</p>");
			sb.append("</a></div></li>");
		}
		return sb.toString();
		
	}
    
   
    private static void genIndexHtml(String html) {
		try {
			String filename = Constants.WAP_PAGE_ADDR + "/index.html";
			
			File file = new File(filename);
			
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
    
    private static String getIndexlatestPicHtml(int pageSize, List<JSONObject> pageList, String type) {
    	long totalNum = 0;
		totalNum = pageList.size()/10 + 1;
		List<JSONObject> curPageList = pageList.subList(0, 20);
		
		StringBuilder sb = new StringBuilder();
		
		for (JSONObject j : curPageList) {
			JSONObject listPageInfoj = null;
			try {
				String listPageInfoFileName = Constants.PIC_ADDR + "/" +j.get("picId") + "/listPageInfo.json";
				String listPageInfoHtml = readFileByLines(listPageInfoFileName);
				listPageInfoj = JSONObject.parseObject(listPageInfoHtml);
			} catch (Exception e) {
				
			}
			
			String bText = (listPageInfoj == null) ? "性感美女写真" : listPageInfoj.get("bText").toString();
			String preTitle = (listPageInfoj == null) ? "性感美女写真" : listPageInfoj.get("preTitle").toString();
			String time = (listPageInfoj == null) ? "" : listPageInfoj.get("time").toString();
			
			sb.append("<li><a href=\"" + Constants.titleTypeMap.get(type) + "/detail/");
			sb.append(j.get("picId") + "_0.html");
			sb.append("\" ><img src=\"");
			sb.append(j.get("prePic"));
			sb.append("\" alt=\"" + bText + "\"></a></li>");
		}
    	
	
		return sb.toString();
		
	}
    
    
    public static void genWapIndexPage() {
//		List<JSONObject> pageList = PageListUtil.getPageListByType(type);
		long totalNum = 0;
		String html = readFileByLines(Constants.WAP_INDEX_HTML);
		Random rand = new Random();
		int randNum = rand.nextInt(3);
		String type = "" + (randNum + 1);
		System.out.println("type = " + type);
		List<JSONObject> pageList = PageListUtil.getPageListByType(type);
		html = html.replace("latestPic", getIndexlatestPicHtml(pageList.size(), pageList, type));
		genIndexHtml(html);
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
//    	File file1 = new File("H:/batu/2078646/1.jpg"); 
//    	System.out.println("delete file " +  file1.length());
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
			if (f == null) {
				continue;
			}
			for (File jpgf : f) {
				String filename = jpgf.getName();
				if (StringUtils.contains(filename, "json")) {
					continue;
				}
				filesizes.add(jpgf.length());
				int[] aa = {98650,47567,51569,91923,97922,101331,88739,91289,89621,99297,88694,99673,89294,35934,26752,21725,41492,100037,49958,37628,356200,50026,44635,66961,56927,79311,75323,19828,54598,60580,88501,96720,60460,46770,81948,36936,39037,68545,268077,231387,171838,33454,60945,34574,19039,121120,84940,56200,38409,37150,54425,45435,45737,47926,44837,53148,61596,51471,44640,39054,43056,27902,41027,30167,37320,97106,68812,105498,46838,48329,36760,53292,43883,55307,50453,140748,54837,78175,42003,543517,34050,47306,59033,31431,18301,31295,31976,46342,37024,31176,45109,49475,34825,18040,86139,121638,111255,92243,193310,517640,27619,174340,66342,51982,51846,42843,37561,40888,45918,99071,71718,49138,157258,143158,164882,231538,182573,181897,137097,142049,147851,212775,140511,104383,49386,69045,84297,48952,52801,54869,53643,51358,52085,51449,63981,46481,35489,33359,28551};
				boolean equal = false;
				for (int a : aa) {
					if (jpgf.length() == a) {
						equal = true;
						break;
					}
				}
				if (equal) {
					jpgf.delete();
					System.out.println("delete file " + jpgf.getName());
				}
			}
//			System.out.println(fi.getName());
		}
	}
}
