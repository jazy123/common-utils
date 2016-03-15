/**
 * 
 */
package cn.jzy.common.network.http;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

/**
 * 基于POST形式的HTTP-WEB请求实现<br/>
 * @author jiangzy 
 */
public class HttpPostWrapper extends AbstractHttpWrapper {

	/**
	 * 默认构造函数
	 */
	public HttpPostWrapper() {
		this(null, "UTF-8");
	}

	/**
	 * 构造函数
	 * @param url
	 * @param encode
	 */
	public HttpPostWrapper(String url, String encode) {
		super(url, encode);
		this.method = "POST";
	}	
	
	/**
	 * 构建HTTP数据请求报文
	 */
	@Override
	protected void appendRequestParams() throws IOException{
		if(!"POST".equalsIgnoreCase(this.method))
			return;
		
		String data = this.formatRequestData(this.getParams(), this.getEncode());		
		if(data == null || data.length() == 0){
			this.connection.setRequestProperty("Content-Length","0");
			return;
		}
		
		System.out.println("[INFO]Data：" + data);	
		this.connection.setDoOutput(true);		
		byte[] bytes = data.getBytes();		
		this.connection.setRequestProperty("Content-Length",String.valueOf(bytes.length));	
		System.out.println("[INFO]Content-Length：" + String.valueOf(bytes.length));
		OutputStream stream = connection.getOutputStream();		
		stream.write(bytes);		
		stream.flush();		
		stream.close();
	}

	/**
	 * 将需要POST到服务器的参数列表格式化输出为报文文本内容
	 * @param params
	 * @param encode
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	protected String formatRequestData(Map<String,String> params, String encode) throws UnsupportedEncodingException{
		if (params == null || params.isEmpty())
			return null;
		
		StringBuffer stringBuffer = new StringBuffer();		
		for (Map.Entry<String, String> entry : this.getParams().entrySet()) {
			String item = String.format("%s=%s&", entry.getKey(),
					URLEncoder.encode(entry.getValue(), encode));
			stringBuffer.append(item);
		}
		
		stringBuffer.deleteCharAt(stringBuffer.length() - 1);		
		return stringBuffer.toString();		
	}
}
