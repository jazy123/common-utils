package cn.jzy.common.utils.network.http;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.LinkedHashMap;
import java.util.Map;

import cn.jzy.common.network.http.AbstractHttpWrapper;
import cn.jzy.common.network.http.HttpGetWrapper;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class HttpGetWrapperTest 
    extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public HttpGetWrapperTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( HttpGetWrapperTest.class );
    }

    /**
     * Rigourous Test :-)
     * @throws IOException 
     */
    public void testLoginUnToken() throws IOException
    {
    	AbstractHttpWrapper wrapper = new HttpGetWrapper(); 
    	wrapper.setUrl("http://api.cnki.net/v20/api/login");
    	wrapper.setEncode("UTF-8");
    	Map<String,String> headers = this.createHeaders("");
    	wrapper.setHeaders(headers);    	
    	wrapper.forward(); 
    	int code = wrapper.getStatusCode();
        assertEquals(code, 400);
    }
    
    /**
     * Rigourous Test :-)
     * @throws IOException 
     */
    public void testLoginSuccess() throws IOException
    {
    	AbstractHttpWrapper wrapper = new HttpGetWrapper(); 
    	wrapper.setUrl("http://api.cnki.net/v20/api/login");
    	wrapper.setEncode("UTF-8");
    	Map<String,String> headers = this.createHeaders("101.5.100.100");
    	wrapper.setHeaders(headers);    	
    	wrapper.forward(); 
        assertNotNull(wrapper.getText());
    }
    
    protected Map<String,String> createHeaders(String ipAddr){
    	String appid = "brps";
    	String appkey = "XuM99xNoTMxyVfOB";    	
    	String timestamp = ((System.currentTimeMillis()) / 1000) + "";
		String token = "";
		try {
			String sign = String.format("timestamp=%s&appid=%s&appkey=%s",
					timestamp, appid, appkey);
			System.out.println("sign=>" + sign);
			// 加密指纹
			token = sha1Encode(sign).toLowerCase();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return null;
		}

		final Map<String, String> params = new LinkedHashMap<String, String>();
		params.put("action", "ip_login");
		params.put("appid", appid);
		params.put("timestamp", timestamp);
		params.put("appsign", token);
		params.put("ip", ipAddr);
		return params;
    }
    
    private static String sha1Encode(String s) throws NoSuchAlgorithmException {
		MessageDigest digest = MessageDigest.getInstance("SHA-1");
		digest.update(s.getBytes());
		byte messageDigest[] = digest.digest();
		return toHexString(messageDigest);
	}

	private static String toHexString(byte[] keyData) {
		if (keyData == null)
			return null;
		int expectedStringLen = keyData.length * 2;
		StringBuilder sb = new StringBuilder(expectedStringLen);
		for (int i = 0; i < keyData.length; i++) {
			String hexStr = Integer.toString(keyData[i] & 0x00FF, 16);
			if (hexStr.length() == 1) {
				hexStr = "0" + hexStr;
			}
			sb.append(hexStr);
		}
		return sb.toString();
	}
}
