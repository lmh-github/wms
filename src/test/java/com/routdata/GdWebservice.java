/**
* @author jay_liang
* @date 2014-4-1 上午11:11:48
*/
package com.routdata;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;

import org.apache.log4j.Logger;

/** 
 * @author	YangZhigang
 * @date	Sep 16, 2011 10:39:00 AM
 * @version 1.0
 * @see		与广东自助服务接口类
 */
public class GdWebservice {
	//public final static String URL = "http://127.0.0.1:8080/szdbdzzfw/webservice/";
	public final static String URL = "http://10.194.129.187:8080/szdbdzzfw/webservice/";
	///public final static String URL = "http://219.134.187.38:7010/sdzzfwservice/webservice/";
	
	
	private String strurl = URL;
	private final Logger logger = Logger.getLogger(this.getClass());
	//private static String provider = "XT_TEST_ITCO";  // 服务商帐号
	//private static String proPwd = "9C3D630624D27F37A54F4D776E7BFC0C";		// 服务商帐号对应的口令
	//private static String sign = "TEST_ITCO";		// 签名
	
	/*
	private static String provider = "SGH";  // 服务商帐号
	private static String proPwd = "B1FEF80D962CF6DDE0E5BC269E56A447";		// 服务商帐号对应的口令
	private static String sign = "SGH";		// 签名
	*/
	
	private static String provider = "XT_TEST_ITCO";  // 服务商帐号
	private static String proPwd = "9C3D630624D27F37A54F4D776E7BFC0C";		// 服务商帐号对应的口令
	private static String sign = "TEST_ITCO";		// 签名
	
	private static String charset = "UTF-8"; // 字符编码
	
	/**
	 * 默认构造函数
	 */
	public GdWebservice() {		
	}
	
	/**
	 * @param url WebService address
	 */
	public GdWebservice(String name){
		this.strurl = URL+name;
	}
	
	/**
	 * @param url		WebService address
	 * @param provider	Provider account
	 * @param password	Password of provider account
	 */
	public GdWebservice(String url,String provider,String password,String sign, String charset){
		this.strurl = url;
		GdWebservice.provider = provider;
		GdWebservice.proPwd = password;
		GdWebservice.sign = sign;
		GdWebservice.charset = (charset==null?"UTF-8":charset);
	}
	
	public static void main(String[] args) throws Exception {
		
		GdWebservice service = new GdWebservice();
		//EE069450415GD,EE069450429GD
		//service.setStrurl("http://localhost:8080/sdzzfw/webservice/mailQueryService");
		//String str = i.queryMail("{\"vuserno\":\"test\",\"mailcode\":\"UI123456785UI,FS123456785JS,HH123456785HO\",\"password\":\"098F6BCD4621D373CADE4E832627B4F6\"}", "Yjk1YmRmMDdhMDI1MTZkMzRkM2NmYzMxMzZjNjg0YWI", "UTF-8")
		//System.out.println(str);
		/*Md5 md5 = new Md5();
		String content = "{\"vuserno\":\"vivi207\",\"mailcode\":\"AA166229777GG,ET405556272CS,NP123456785NA\",\"password\":\""+md5.getMD5ofStr("v6942320")+"\"}";
		String strRes = service.call("upQueryMail", content);
		System.out.println(strRes);*/
		/*
		service.setStrurl("http://219.134.187.38:7010/sdzzfwservice/webservice/mailQueryService");
		String content = "{\"vuserno\":\"vivi207\",\"mailcode\":\"UI123456785UI,FS123456785JS,HH123456785HO\",\"password\":\"098F6BCD4621D373CADE4E832627B4F6\"}";
		String strRes = service.call("queryMail", content);
		System.out.println(strRes);*/
		
		/*
大网统版：
1028204896604
EE645362828GD
 
大同城：
EV517555127CS
 
新本地：
YZ273172599TC
YZ273172585TC
		*/
		/*
		Md5 md5 = new Md5();
		service.setStrurl("http://www.szems.cn/sdzzfwservice/webservice/mailQueryService");
		//service.setStrurl("http://localhost:8070/sdzzfw/webservice/mailQueryService");
		String content = "{\"mailcode\":\"EE842856498GD\",\"vuserno\":\"SGH\",\"password\":\"B1FEF80D962CF6DDE0E5BC269E56A447\"}";
		String strRes = service.call("upQueryMail", content);
		System.out.println(strRes);*/
		
		//String content = "{\"mailcode\":\"EE535016455GD,EE535016455GD\",\"vuserno\":\"SGH\",\"password\":\"B1FEF80D962CF6DDE0E5BC269E56A447\"}";
		//String msign = SignEncrypter.encrypt(content,"SGH",charset);
		//System.out.println(msign);
		
		
		
//		Md5 md5 = new Md5();
		service.setStrurl("http://219.134.187.38:7010/sdzzfwservice/webservice/mailQueryService");
		//service.setStrurl("http://localhost:8070/sdzzfw/webservice/mailQueryService");
		String content = "{\"mailcode\":\"EC100851782CS\",\"vuserno\":\"SGH\",\"password\":\"B1FEF80D962CF6DDE0E5BC269E56A447\"}";
		String strRes = service.call("queryMail", content);
		System.out.println(strRes);
		
	}

	public String getStrurl() {
		return strurl;
	}

	public void setStrurl(String strurl) {
		this.strurl = strurl;
	}
	
	/**
	 * @author	YangZhigang
	 * @date	Sep 6, 2011 11:43:45 AM
	 * @see		呼叫远程Webservice
	 * @param service	接口名称
	 * @param content	参数内容：JSON格式
	 * @param sign		签名
	 * @param charset	字符集
	 * @return
	 */
	public String call(String service,String content){
		
		StringBuilder strbuf = new StringBuilder();		
		StringBuilder resbuf = new StringBuilder();
		String strRes = null;
		
		
		try {
			String msign = encrypt(content,sign,charset);
			
			strbuf.append("<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:ser=\"http://service.webservice.zzfw.routdata.com/\">");
			strbuf.append("<soapenv:Header><RequestSOAPHeader>");
			strbuf.append("<spid>"+GdWebservice.provider+"</spid>");
			strbuf.append("<sppwd>"+GdWebservice.proPwd+"</sppwd>");
			strbuf.append("</RequestSOAPHeader></soapenv:Header>");
			strbuf.append("<soapenv:Body>");
			strbuf.append("<ser:" + service + ">");

			strbuf.append("<arg0>");
			strbuf.append(content);
			strbuf.append("</arg0>");
			strbuf.append("<arg1>"+msign+"</arg1>");
			strbuf.append("<arg2>"+charset+"</arg2>");
			strbuf.append("</ser:"+service+">");
			strbuf.append("</soapenv:Body>");
			strbuf.append("</soapenv:Envelope>");

			URL url = new URL(strurl);
			HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();

			httpConn.setRequestMethod("POST");
			httpConn.setDoInput(true);
			httpConn.setDoOutput(true);
			httpConn.setAllowUserInteraction(true);
			httpConn.setUseCaches(false);
			httpConn.setRequestProperty("Content-Type", "text/xml;charset=UTF-8");

			httpConn.connect();

			OutputStream os = httpConn.getOutputStream();
			
			logger.info("发送内容："+strbuf);
			os.write(strbuf.toString().getBytes(charset));
			os.flush();
			
			int rescode = httpConn.getResponseCode();
			String resmsg = httpConn.getResponseMessage();
			logger.info("GDWebService response " + rescode + " - " + resmsg);
			
			if(httpConn.getErrorStream()!=null){
				StringBuffer resbuf1 = new StringBuffer();
				InputStream is = httpConn.getErrorStream();
				BufferedReader br = new BufferedReader(new InputStreamReader(is,"UTF-8"));
				String len = null;
				while ((len = br.readLine()) != null) {
					resbuf1.append(len);
				}
				is.close();
				System.out.println("//-------------------------");
				System.out.println(resbuf1);
			}
			
			if (rescode == 200) {

				InputStream is = httpConn.getInputStream();
				BufferedReader br = new BufferedReader(new InputStreamReader(is,"UTF-8"));

				String len = null;
				while ((len = br.readLine()) != null) {
					resbuf.append(len);
				}

				is.close();
				
				strRes = resbuf.substring(resbuf.indexOf("<return>") + 8, resbuf.indexOf("</return>"));
				strRes = strRes.replaceAll("&quot;", "\"");

			} else {
				resbuf.append("{\"status\":").append(rescode).append(",\"errorInfo\":\"").append(resmsg).append("\"}");
				strRes = resbuf.toString();
				
				
			}
			
			os.close();

			httpConn.disconnect();
		
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e);
		}
		
				
		return strRes;
	}

	public static void setProvider(String provider) {
		GdWebservice.provider = provider;
	}

	public static void setProPwd(String proPwd) {
		GdWebservice.proPwd = proPwd;
	}

	public static void setSign(String sign) {
		GdWebservice.sign = sign;
	}

	public static void setCharset(String charset) {
		GdWebservice.charset = charset;
	}
	

	// 签名程序代码片段
	public static String encrypt(String content, String keyValue, String charset)
			throws Exception {
		if (keyValue != null) {
			return base64(MD5(content + keyValue, charset), charset);
		}
		return base64(MD5(content, charset), charset);
	}

	/**
	 * 对传入的字符串进行MD5加密
	 * 
	 * @param plainText
	 * @return
	 */
	public static String MD5(String plainText, String charset) throws Exception {
		MessageDigest md = MessageDigest.getInstance("MD5");
		md.update(plainText.getBytes(charset));
		byte b[] = md.digest();
		int i;
		StringBuffer buf = new StringBuffer("");
		for (int offset = 0; offset < b.length; offset++) {
			i = b[offset];
			if (i < 0)
				i += 256;
			if (i < 16)
				buf.append("0");
			buf.append(Integer.toHexString(i));
		}
		return buf.toString();
	}

	/**
	 * base64编码
	 * 
	 * @param str
	 * @return
	 * @throws Exception
	 */
	public static String base64(String str, String charset) throws Exception {
		return (new sun.misc.BASE64Encoder()).encode(str.getBytes(charset));
	}
}
