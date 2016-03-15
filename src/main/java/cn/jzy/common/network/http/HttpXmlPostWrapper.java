/**
 * 
 */
package cn.jzy.common.network.http;

import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * 基于XML形式的HTTP网络请求
 * @author jiangzy
 * 
 */
public abstract class HttpXmlPostWrapper extends HttpPostWrapper {

	/**
	 * 初始化
	 */
	public HttpXmlPostWrapper() {
		super();
	}

	/**
	 * 初始化
	 * @param url
	 * @param encode
	 */
	public HttpXmlPostWrapper(String url, String encode) {
		super(url, encode);
	}
	
	/**
	 * 将需要POST到服务器的参数列表格式化输出为报文文本内容
	 * @param params
	 * @param encode
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	@Override
	protected String formatRequestData(Map<String, String> params, String encode)
			throws UnsupportedEncodingException {
		if (params == null || params.isEmpty())
			return null;
		
		this.connection.setRequestProperty("content-type", "application/xml");
		
		return formatRequestXml(params, encode);
	}
	
	/**
	 * 构建XML格式的POST请求报文
	 * @param params
	 * @param encode
	 * @return
	 */
	protected abstract String formatRequestXml(Map<String, String> params, String encode);	

}
