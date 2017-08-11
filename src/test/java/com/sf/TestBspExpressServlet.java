package com.sf;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class TestBspExpressServlet {


    public static void main(String[] args) throws Exception {
        //定义参数 begin
        // String uri = "http://10.0.78.9/bsp-oisp/sfexpressService";
        String uri = "http://bsp-ois.sit.sf-express.com:9080/bsp-ois/sfexpressService";
        // String xmlFile="H:\\git\\wms\\src\\test\\java\\com\\sf\\xml.txt";
        String xmlFile = "H:\\git\\wms\\src\\test\\java\\com\\sf\\xml.xml";
        System.out.println(xmlFile);
        String checkWord = "j8DzkIFgmlomPt0aLuwU";
        //定义参数 end
        URL url = new URL(uri);
        String xml = Util.loadFile(xmlFile);
        String verifyCode = Util.md5EncryptAndBase64(xml + checkWord);

        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setDoOutput(true);
        conn.setConnectTimeout(10000);
        conn.setReadTimeout(10000);

        OutputStream os = conn.getOutputStream();
        os.write(("xml=" + xml + "&verifyCode=" + verifyCode).getBytes("utf8"));
        os.close();

        InputStream in = conn.getInputStream();
        byte[] data = new byte[in.available()];
        in.read(data);
        System.out.println(new String(data, "utf8").replace("><", ">\n<"));
    }

}
