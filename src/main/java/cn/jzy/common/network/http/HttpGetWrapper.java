package cn.jzy.common.network.http;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;

/**
 * 基于GET形式的HTTP-WEB请求实现<br/>
 * @author jiangzy
 */
public class HttpGetWrapper extends AbstractHttpWrapper {

	/**
	 * 默认构造函数
	 */
	public HttpGetWrapper(){
		this.method= "GET";
	}
	
	/** 实例化HTTP协议的底层实现
	 * @see cn.jzy.common.network.http.AbstractHttpWrapper#initHttpConnection()
	 */
	@Override
	protected void initHttpConnection() throws IOException {
		String urlAddress = appendRequestUrl();
		URL url = new URL(urlAddress);
		this.connection = (HttpURLConnection) url.openConnection();		
	}
	
	/**
	 * 构造GET方式的请求URL，合并请求参数到URL中
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	protected String appendRequestUrl() throws UnsupportedEncodingException {
		if (this.getParams() == null || this.getParams().isEmpty())
			return this.getUrl();

		StringBuffer buffer = new StringBuffer();

		for (Map.Entry<String, String> entry : params.entrySet()) {
			String item = String.format("&%s=%s", entry.getKey(),
					URLEncoder.encode(entry.getValue(), this.getEncode()));
			System.out.println(item);
			buffer.append(item);
		}
		
		String url = this.getUrl();

		if (url == null || url.length() == 0)
			return buffer.toString();

		return url.indexOf("?") == -1 ? url + "?" + buffer.substring(1) : url
				+ buffer.toString();
	}

}
