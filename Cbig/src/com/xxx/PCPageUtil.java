package com.xxx;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.List;
import java.util.Random;

import org.apache.commons.lang.StringUtils;

import com.alibaba.fastjson.JSONObject;

public class PCPageUtil {
	public static void genListPage1(String type) {
		List<JSONObject> pageList = PageListUtil.getPageListByType(type);
		long totalNum = 0;
		if (StringUtils.equals(type, "100")) {
			pageList = PageListUtil.getPageListNew100();
			totalNum = 100;
		} else {
			pageList = PageListUtil.getPageListByType(type);
			totalNum = pageList.size()/20 + 1;
		}
		
		for (int i = 0; i < totalNum; i++) {
			StringBuilder sb = new StringBuilder();
			
			String headHtml = readFileByLines(Constants.HEAD_HTML);
			String bottomHtml = readFileByLines(Constants.BOTTOM_HTML);
			List<JSONObject> curPageList;
			
			if (totalNum == 100) {
				curPageList = pageList;
			} else {
				if (i == pageList.size()/20) {
					curPageList = pageList.subList(i * 20, pageList.size());
				} else {
					curPageList = pageList.subList(i * 20, (i + 1) * 20);
				}
			}
			
			sb.append(headHtml);
			sb.append(genListContent(curPageList, type));
			if (StringUtils.equals(type, "100")) {
				sb.append("");
			} else {
				sb.append(genPageinfo(curPageList, type, i + 1, totalNum));
			}
			
			sb.append(bottomHtml);
			
			genHtml(type, i + 1, sb.toString());
		}
	}
	
	public static void genDetailPage1(String type) {
		
		List<JSONObject> pageList = PageListUtil.getPageListByType(type);
		if (StringUtils.equals(type, "100")) {
			pageList = PageListUtil.getPageListNew100();
		}
		for (int i = 0; i < pageList.size(); i++) {
			JSONObject j = pageList.get(i);
			List detailPicList = (List)j.get("detailPicList");
			
			String dir = Constants.PIC_ADDR_X + "/" + j.get("picId");
			File fileDir = new File(dir);
			
			fileDir.list();
			List<JSONObject> detailImgFileList = (List<JSONObject>)j.get("detailPicList");
			long pageSize = detailImgFileList.size();
			for (JSONObject jb : detailImgFileList) {
				String detailHtml = readFileByLines(Constants.DETAIL_HTML);
				List<String> list = (List<String>)jb.get("detailImgFileList");
				String detailImgFileName = jb.get("detailImgFileName").toString();
				JSONObject detailPageInfoj = null;
				
				try {
					String detailPageInfoFileName = Constants.PIC_ADDR_X + "/" +j.get("picId") + "/" + detailImgFileName + "/detailPageInfo.json";
					String detailPageInfoHtml = readFileByLines(detailPageInfoFileName);
					detailPageInfoj = JSONObject.parseObject(detailPageInfoHtml);
				} catch (Exception e) {
					
				}
				String detailKeyWords = (detailPageInfoj == null) ? "" : detailPageInfoj.getString("detailKeyWords");
				String detailHeadTitle = (detailPageInfoj == null) ? "" : detailPageInfoj.getString("detailHeadTitle");
				String detailDescription = (detailPageInfoj == null) ? "" : detailPageInfoj.getString("detailDescription");
				detailHtml = detailHtml.replace("detailKeyWords", detailKeyWords);
				detailHtml = detailHtml.replace("detailDescription", detailDescription);
				detailHtml = detailHtml.replace("detailHeadTitle", detailHeadTitle);
				detailHtml = detailHtml.replace("detailPageTitle", detailHeadTitle);
				detailHtml = detailHtml.replace("亿秀网", "51lulala");
				StringBuilder contentImg = new StringBuilder();
				for (String pic : list) {
					String filePath = Constants.PIC_ADDR_NET + "/" + j.get("picId") + "/" + detailImgFileName + "/" + pic;
					contentImg.append("<p align=\"center\">");
					contentImg.append("<img src=\"").append(filePath).append("\" />");
					contentImg.append("</p><br/>");
				}

				StringBuilder sbPageInfo = new StringBuilder();
				sbPageInfo.append("<a>共" + pageSize + "页: </a>").append("</li><li>");
				long curPage = Long.parseLong(detailImgFileName);
				if (StringUtils.equals(detailImgFileName, "0")) {
					sbPageInfo.append("<li><a href='#'>上一页</a></li>");
				} else {
					sbPageInfo.append("<li><a href='" + (String)j.get("picId") + "_" + (curPage - 1) + ".html" + "'>上一页</a></li>");
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
					nextPath = "<p>下一篇：没有了 </p>";
				} else {
					JSONObject nextj = pageList.get(i-1);
					JSONObject nextListPageInfoj = null;
					try {
						String listPageInfoFileName = Constants.PIC_ADDR_X + "/" + nextj.get("picId") + "/listPageInfo.json";
						String listPageInfoHtml = readFileByLines(listPageInfoFileName);
						nextListPageInfoj = JSONObject.parseObject(listPageInfoHtml);
					} catch (Exception e) {
						
					}
					if (nextj != null && nextListPageInfoj != null) {
						nextPath = "<p>下一篇：<a href='"  + (String)nextj.get("picId") + "_0.html'>" + nextListPageInfoj.getString("preTitle") + "</a> </p>";
					}
					
				}
				if(i == (pageList.size() - 1)) {
					previousPath = "<p>上一篇：没有了 </p>";
				} else {
					JSONObject previousj = pageList.get(i+1);
					JSONObject previousListPageInfoj = null;
					try {
						String listPageInfoFileName = Constants.PIC_ADDR_X + "/" + previousj.get("picId") + "/listPageInfo.json";
						String listPageInfoHtml = readFileByLines(listPageInfoFileName);
						previousListPageInfoj = JSONObject.parseObject(listPageInfoHtml);
					} catch (Exception e) {
						
					}
					if(previousj != null && previousj.get("picId") != null && previousListPageInfoj != null) {
						System.out.println("previousj = " + previousj);
						System.out.println("picId = " + previousj.get("picId"));
						System.out.println("previousListPageInfoj = " + previousListPageInfoj);
						System.out.println("previousListPageInfoj = " + previousListPageInfoj.getString("preTitle"));
						previousPath = "<p>上一篇：<a href='"  + (String)previousj.get("picId") + "_0.html'>" + previousListPageInfoj == null ? "上一篇" : previousListPageInfoj.getString("preTitle") + "</a> </p>";
					}
					
				}
				detailHtml = detailHtml.replace("contentImg", contentImg.toString());
				detailHtml = detailHtml.replace("nextPath", nextPath);
				detailHtml = detailHtml.replace("previousPath", previousPath);
				detailHtml = detailHtml.replace("detailPageInfo", sbPageInfo.toString());
				detailHtml = detailHtml.replace("recommendationHtml1", getRecommendationHtml(pageList.size(), pageList));
				detailHtml = detailHtml.replace("recommendationHtml2", getRecommendationHtml(pageList.size(), pageList));
				detailHtml = detailHtml.replace("siteRecommendationHtml", getSiteRecommendationHtml(pageList.size(), pageList));
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
				String listPageInfoFileName = Constants.PIC_ADDR_X + "/" + randj.get("picId") + "/listPageInfo.json";
				String listPageInfoHtml = readFileByLines(listPageInfoFileName);
				randListPageInfoj = JSONObject.parseObject(listPageInfoHtml);
			} catch (Exception e) {
				
			}
			sb.append("<li class=\"col-xs-3\"><a href=\"").append((String)randj.get("picId") + "_0.html\"");
			sb.append("title=\"").append(randListPageInfoj == null ? "" : randListPageInfoj.getString("preTitle")).append("\" target=\"_blank\">");
			sb.append(randListPageInfoj == null ? "" : randListPageInfoj.getString("preTitle")).append("</a></li>");
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
				String listPageInfoFileName = Constants.PIC_ADDR_X + "/" + randj.get("picId") + "/listPageInfo.json";
				String listPageInfoHtml = readFileByLines(listPageInfoFileName);
				randListPageInfoj = JSONObject.parseObject(listPageInfoHtml);
			} catch (Exception e) {
				
			}
			if (randListPageInfoj == null) {
				continue;
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
		return Constants.PAGE_ADDR + "/" + Constants.titleTypeMap.get(type);
	}
	
	private static String getDetailFileDir(String type) {
		return Constants.PAGE_ADDR + "/" + Constants.titleTypeMap.get(type) + "/detail";
	}
	private static String genListContent(List<JSONObject> curPageList, String type) {
		StringBuilder sb = new StringBuilder();
		sb.append("<ul>");
		for (JSONObject j : curPageList) {
			JSONObject listPageInfoj = null;
			try {
				String listPageInfoFileName = Constants.PIC_ADDR_X + "/" +j.get("picId") + "/listPageInfo.json";
				String listPageInfoHtml = readFileByLines(listPageInfoFileName);
				listPageInfoj = JSONObject.parseObject(listPageInfoHtml);
			} catch (Exception e) {
				
			}
			String bText = (listPageInfoj == null) ? "XXX" : listPageInfoj.get("bText").toString();
			String preTitle = (listPageInfoj == null) ? "XXX" : listPageInfoj.get("preTitle").toString();
			String time ="";
			sb.append("<li class=\"col-xs-1-5\"><div class=\"shupic\"><a href=\"detail/");
			sb.append(j.get("picId") + "_0.html");
			sb.append("\" target=\"_blank\"><img class=\"img-responsive picheng\" src=\"");
			sb.append(j.get("prePic"));
			sb.append("\" alt=\"" + bText + "\"></a>");//待定
			sb.append("<p class=\"textbox2\"><a href=\"");
			sb.append(j.get("picId") + "_0.html");
			sb.append("\" class=\"biaoti center-block text-center\" title=\"");
			sb.append(preTitle);//待定
			sb.append("\" target=\"_blank\"><b>");
			sb.append(bText);//待定
			sb.append("</b></a>");
			sb.append("<a href=\"");
			sb.append(j.get("picId") + ".html");
			sb.append("\" class=\"leibie\"></a><a href=\"");
			sb.append(Constants.titleTypeMap.get(type));
			sb.append("\">");
			sb.append(Constants.titleMap.get(type));
			sb.append("</a><span class=\"time\">");
			sb.append(time);//待定
			sb.append("</span></p></div></li>");
		}
		sb.append("</ul>");
		sb.append("</div></div>");
		String ret = sb.toString().replace("亿秀�?", "我爱丝足�?");
		return ret;
	}
	
	private static String genPageinfo(List<JSONObject> curPageList, String type, int curPage, long totalNum) {
		StringBuilder sb = new StringBuilder();
		sb.append("<div class=\"jianju30\"></div><div class=\"container\"><div class=\"pageinfo\">");
		if (curPage == 1) {
			sb.append("<span class=\"disabled\">首页</span>");
		} else {
			sb.append("<a href=\"" + "1.html" + "\">首页</a>");
			sb.append("<a href=\"" + (curPage - 1) + ".html" + "\">上一�?</a>");
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
//        	reader = new BufferedReader(new InputStreamReader(connectiton.getInputStream(),"GB2312"));
        	 InputStreamReader read = new InputStreamReader(
                     new FileInputStream(file), "UTF-8");//考虑到编码格式
                     BufferedReader bufferedReader = new BufferedReader(read);
            reader = new BufferedReader(read);
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
    
    /**
     * 以行为单位读取文件，常用于读面向行的格式化文�?
     */
    public static String readFileByLines1(String fileName) {
        File file = new File(fileName);
        BufferedReader reader = null;
        try {
//        	reader = new BufferedReader(new InputStreamReader(connectiton.getInputStream(),"GB2312"));
        	 InputStreamReader read = new InputStreamReader(
                     new FileInputStream(file), "GBK");//考虑到编码格式
                     BufferedReader bufferedReader = new BufferedReader(read);
            reader = new BufferedReader(read);
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
    
    public static void genIndexPage() {
//		List<JSONObject> pageList = PageListUtil.getPageListByType(type);
		long totalNum = 0;
//		String html = readFileByLines(Constants.INDEX_HTML);
//		List<JSONObject> pageList = PageListUtil.getPageListByType("1");
//		List<JSONObject> pageListXZ = PageListUtil.getPageListByType("10");
//		List<JSONObject> pageListFl = PageListUtil.getPageListByType("3");
//		html = html.replace("indexMvHtml", getIndexMvHtml(pageList.size(), pageList));
//		html = html.replace("indexXzHtml", getIndexXzHtml(pageListXZ.size(), pageListXZ));
//		html = html.replace("indexFlHtml", getIndexFlHtml(pageListFl.size(), pageListFl));
//		genIndexHtml(html);
	}
    
    private static String getIndexFlHtml(int pageSize, List<JSONObject> pageList) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < 5; i++) {
			Random rand = new Random();
			int randNum = rand.nextInt(pageSize);
			JSONObject randj = pageList.get(randNum);
			JSONObject randListPageInfoj = null;
			try {
				String listPageInfoFileName = Constants.PIC_ADDR_X + "/" + randj.get("picId") + "/listPageInfo.json";
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
				String listPageInfoFileName = Constants.PIC_ADDR_X + "/" + randj.get("picId") + "/listPageInfo.json";
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
    
    private static String getIndexMvHtml(int pageSize, List<JSONObject> pageList) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < 5; i++) {
			Random rand = new Random();
			int randNum = rand.nextInt(pageSize);
			JSONObject randj = pageList.get(randNum);
			JSONObject randListPageInfoj = null;
			try {
				String listPageInfoFileName = Constants.PIC_ADDR_X + "/" + randj.get("picId") + "/listPageInfo.json";
				String listPageInfoHtml = readFileByLines(listPageInfoFileName);
				randListPageInfoj = JSONObject.parseObject(listPageInfoHtml);
			} catch (Exception e) {
				
			}
			sb.append("<li class=\"col-xs-1-5\">");
			sb.append("<div class=\"spb shupic\">");
			sb.append("<a href=\"").append("xingganmeinvxiezhen/detail/" + (String)randj.get("picId") + "_0.html\" target=\"_blank\">");
			sb.append("<img class=\"img-responsive\" src=\"").append(randj.get("prePic"));
			sb.append("\" alt=\"" + randListPageInfoj.getString("preAlt")+ "\">");
			sb.append("<a href=\"").append("xingganmeinvxiezhen/detail/" + (String)randj.get("picId")  + "_0.html\"");
			sb.append(" title=\"" + randListPageInfoj.getString("preTitle")+ "\">");
			sb.append("<p class=\"text1\">" + randListPageInfoj.getString("preTitle")+ "</p>");
			sb.append("</a></div></li>");
		}
		return sb.toString();
		
	}
    private static void genIndexHtml(String html) {
		try {
			String filename = Constants.PAGE_ADDR + "/index.html";
			
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
    
    public static void main(String[] args) {
    	genIndexPage();
    	genListPage1("1");
    	genListPage1("2");
    	genListPage1("3");
    	genListPage1("4");
    	genListPage1("5");
    	genListPage1("10");
//    	genListPage1("100");
    	genDetailPage1("1");
    	genDetailPage1("2");
    	genDetailPage1("3");
    	genDetailPage1("4");
    	genDetailPage1("5");
    	genDetailPage1("10");
//    	genDetailPage1("100");
	}

	
}
