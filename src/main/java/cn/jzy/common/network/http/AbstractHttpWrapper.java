/**
 * 
 */
package cn.jzy.common.network.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;

/**
 * 基于HTTP协议的互联网WEB请求的抽象实现<br/>
 * 支持两种请求方式：GET/POST
 * 
 * @author jiangzy
 * @see <a>HttpPostWrapper</a>
 * @see <a>HttpGetWrapper</a>
 */
public abstract class AbstractHttpWrapper {

	/**
	 * Web请求的编码类型，默认为UTF-8
	 */
	private String encode = "UTF-8";

	/**
	 * Web请求的目标地址
	 */
	private String url;

	/**
	 * Web请求的方法
	 */
	protected String method;

	/**
	 * Web请求的HTTP头信息
	 */
	protected Map<String, String> headers;

	/**
	 * Web请求的数据内容
	 */
	protected Map<String, String> params;
	/*
	 * protected boolean retryEnable; protected int retryMaxTimes;
	 */
	/**
	 * Web请求的HTTP底层实现
	 */
	protected HttpURLConnection connection;

	/**
	 * Web请求的响应代码
	 */
	protected int statusCode = -1;

	/**
	 * Web请求的返回数据内容，目前仅支持文本内容
	 */
	protected StringBuffer text;

	/**
	 * Web请求的错误消息
	 */
	protected StringBuffer error;

	/**
	 * 获取Web请求的响应代码
	 * 
	 * @return the statusCode
	 */
	public int getStatusCode() {
		return statusCode;
	}

	/**
	 * 获取 Web请求的返回数据内容，目前仅支持文本内容
	 * 
	 * @return the text
	 */
	public StringBuffer getText() {
		return text;
	}

	/**
	 * 获取Web请求的错误消息
	 * 
	 * @return the error
	 */
	public StringBuffer getError() {
		return error;
	}

	/**
	 * 获取Web请求的编码类型
	 * 
	 * @return the encode
	 */
	public String getEncode() {
		return encode;
	}

	/**
	 * 设置Web请求的编码类型
	 * 
	 * @param encode
	 *            the encode to set
	 */
	public void setEncode(String encode) {
		this.encode = encode;
	}

	/**
	 * 获取Web请求的目标地址
	 * 
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * 设置Web请求的目标地址
	 * 
	 * @param url
	 *            the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * 获取Web请求的HTTP头信息
	 * 
	 * @return the headers
	 */
	public Map<String, String> getHeaders() {
		return headers;
	}

	/**
	 * 设置Web请求的HTTP头信息
	 * 
	 * @param headers
	 *            the headers to set
	 */
	public void setHeaders(Map<String, String> headers) {
		this.headers = headers;
	}

	/**
	 * 获取Web请求的数据内容
	 * 
	 * @return the params
	 */
	public Map<String, String> getParams() {
		return params;
	}

	/**
	 * 设置Web请求的数据内容
	 * 
	 * @param params
	 *            the params to set
	 */
	public void setParams(Map<String, String> params) {
		this.params = params;
	}

	/**
	 * 默认构造函数
	 */
	public AbstractHttpWrapper() {
		this(null, "UTF-8");
	}

	/**
	 * 构造函数
	 * 
	 * @param url
	 *            ： WEB请求的地址
	 * @param encode
	 *            ： WEB请求的数据编码格式
	 */
	public AbstractHttpWrapper(String url, String encode) {
		this.setUrl(url);
		this.setEncode(encode);
	}

	/**
	 * 执行WEB请求的过程
	 * 
	 * @throws IOException
	 */
	public void forward() throws IOException {
		if (this.getUrl() == null || this.getUrl().length() == 0)
			return;

		try {
			this.initHttpConnection();
			this.appendHttpSetting();
			this.appendRequestHeaders();
			this.appendRequestParams();			
			this.handleHttpResponse();
		} catch(Exception ex){
			System.out.println(ex.getMessage());
		} finally {
			if (this.connection != null)
				this.connection.disconnect();
		}
	}
	
	/** 实例化HTTP协议的底层实现
	 * @see cn.jzy.common.network.http.AbstractHttpWrapper#initHttpConnection()
	 */
	protected void initHttpConnection() throws IOException {
		URL address = new URL(this.getUrl());		
		connection = (HttpURLConnection) address.openConnection();	
	}

	/**
	 * 设置HTTP-WEB请求的设置
	 * 
	 * @throws ProtocolException
	 */
	protected void appendHttpSetting() throws ProtocolException {
		if (this.connection == null) {
			// throw new Exception("connection is not created yet!");
			return;
		}

		this.connection.setRequestMethod(this.method);
		this.connection.setUseCaches(false);
	}

	/**
	 * 构建HTTP请求头内容
	 * 
	 * @throws UnsupportedEncodingException
	 */
	protected void appendRequestHeaders() throws UnsupportedEncodingException {
		if (this.connection == null || this.getHeaders() == null
				|| this.getHeaders().isEmpty())
			return;
		for (Map.Entry<String, String> entry : this.getHeaders().entrySet()) {
			System.out.println("[INFO]header[\"" + entry.getKey() + "\"]="
					+ entry.getValue());
			connection.addRequestProperty(entry.getKey(),
					URLEncoder.encode(entry.getValue(), this.getEncode()));
		}
	}

	/**
	 * 构建HTTP数据请求报文
	 * 
	 * @throws UnsupportedEncodingException
	 * @throws IOException
	 */
	protected void appendRequestParams() throws UnsupportedEncodingException,
			IOException {
		return;
	}

	/**
	 * 处理WEB请求的响应结果
	 * 
	 * @throws IOException
	 */
	protected void handleHttpResponse() throws IOException {
		// 发送请求
		connection.connect();
		int code = connection.getResponseCode();
		this.statusCode = code;
		System.out.println("[INFO]HTTP Message：" + connection.getResponseMessage());
		System.out.println("[INFO]HTTP Code：" + code);
		System.out.println("[INFO]content type：" + connection.getContentType());
		System.out.println("[INFO]content length：" + connection.getContentLength());
		System.out.println("[INFO]content encoding："
				+ connection.getContentEncoding());
		if (code == 200) {
			this.text = loadResponseData(connection.getInputStream(),
					this.getEncode());
		} else {
			this.error = loadResponseData(connection.getErrorStream(),
					this.getEncode());
		}
	}

	/**
	 * 从WEB服务器返回的数据流中加载内容内容
	 */
	private static StringBuffer loadResponseData(final InputStream stream,
			String encode) throws IOException {
		StringBuffer buffer = new StringBuffer();
		BufferedReader reader = new BufferedReader(new InputStreamReader(
				stream, encode));
		String line;
		while ((line = reader.readLine()) != null) {
			buffer.append(line);
		}
		reader.close();
		return buffer;
	}

}
