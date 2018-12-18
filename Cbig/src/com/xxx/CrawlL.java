package com.xxx;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class CrawlL {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		File input = new File("C:/Users/test/Desktop/1.html");
		Document doc = Jsoup.parse(input, "UTF-8", "http://example.com/");
		Elements divNav = doc.select("div.dt-slider-cont");
		Elements imgs = divNav.get(0).getElementsByTag("img");
		for (int i = 0; i < imgs.size(); i++) {
			String mainPicAddr = "https:" + imgs.get(i).attr("src").replace("_.webp", "");
			download(mainPicAddr, "头图"+ i+ ".jpg", "C:/Users/test/Desktop/tb");
		}
		
		
//		Element skuDiv = doc.select("div.pop-main.hide").get(0);
		Elements divDetails = doc.select("div.desc_page_box.normal");
		for (int i = 0; i < divDetails.size(); i++) {
			Element divDetail = divDetails.get(i);
			String detailPicAddr = "https:" + divDetail.getElementsByTag("img").get(0).attr("src").replace("_.webp", "");
			download(detailPicAddr, i + ".jpg", "C:/Users/test/Desktop/tb");
		}
//		dowPic();
	}
	
	public static void dowPic() throws Exception {
		String picAdd = "http://img.alicdn.com/imgextra/i4/16762903/TB2rukyeCfD8KJjSszhXXbIJFXa_!!16762903.jpg_120x120q50s150.jpg;http://img.alicdn.com/imgextra/i3/16762903/TB2K3L0eBfH8KJjy1XbXXbLdXXa_!!16762903.jpg_120x120q50s150.jpg;http://img.alicdn.com/imgextra/i3/16762903/TB2bUgwewDD8KJjy0FdXXcjvXXa_!!16762903.jpg_120x120q50s150.jpg;http://img.alicdn.com/imgextra/i3/16762903/TB2SAgoexPI8KJjSspoXXX6MFXa_!!16762903.jpg_120x120q50s150.jpg;http://img.alicdn.com/imgextra/i3/16762903/TB2EUIrewvD8KJjy0FlXXagBFXa_!!16762903.jpg_120x120q50s150.jpg;http://img.alicdn.com/imgextra/i4/16762903/TB2MpMgaWzB9uJjSZFMXXXq4XXa_!!16762903.jpg_120x120q50s150.jpg;http://img.alicdn.com/imgextra/i3/16762903/TB2PH3lesbI8KJjy1zdXXbe1VXa_!!16762903.jpg_120x120q50s150.jpg;http://img.alicdn.com/imgextra/i3/16762903/TB2S.pWfDvI8KJjSspjXXcgjXXa_!!16762903.jpg_120x120q50s150.jpg;http://img.alicdn.com/imgextra/i4/16762903/TB2WiNEbkfb_uJjSsD4XXaqiFXa_!!16762903.jpg_120x120q50s150.jpg";
		String[] picAdds = picAdd.split(";");
		int i = 0;
		for (String s : picAdds) {
			s = s.replaceAll("120x120", "640x640");
			download(s, "主图" + i + ".jpg", "C:/Users/test/Desktop/tb");
			i++;
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
}
