package zycz.practice.httpcomponents;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
/**
 * * 
 * @ClassName HttpClientUtil.java
 * <p>Description:HttpClient工具类 </p>
 * @author yu.zhang
 * @date 2017年11月16日 下午6:33:21
 */
public class HttpClientUtil {
	public static final String DEFAULT_CHARSET = "UTF-8";
	public static final String EMPTY_STRING = "";
	public static CloseableHttpClient httpClient = null;
	
	static {
		init();
	  }
	
	/*** 
	 * 方法名 : init
	 * <p>Description: 初始化http连接池
	 * 此处解释下MaxtTotal和DefaultMaxPerRoute的区别：
	 * 1、MaxtTotal是整个池子的大小；
	 * 2、DefaultMaxPerRoute是根据连接到的主机对MaxTotal的一个细分；比如：
	 *	 MaxtTotal=400 DefaultMaxPerRoute=200
	 *	   而我只连接到http://sishuok.com时，到这个主机的并发最多只有200；而不是400；
	 *	   而我连接到http://sishuok.com 和 http://qq.com时，到每个主机的并发最多只有200；
	 *	   即加起来是400（但不能超过400）；所以起作用的设置是DefaultMaxPerRoute。
	 * </p>
	 * @author yu.zhang
	 * @date 2017年11月16日 下午4:32:08
	 * @version 1.0  void
	 * <p>修改履历：</p>
	 */
	private static void init() {
		PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
		cm.setMaxTotal(200);//池子最大连接数
	    cm.setDefaultMaxPerRoute(50);
	    httpClient = HttpClients.custom().setConnectionManager(cm).build();
	}
	public static void main(String[] args) throws Exception{
		String url = "http://www.baidu.com";
		//模拟get请求
		System.out.println(get(url));
	}
	/*** 
	 * 方法名 : get
	 * <p>Description: get请求</p>
	 * @author yu.zhang
	 * @date 2017年11月16日 下午3:50:53
	 * @version 1.0 
	 * @param url
	 * @return String
	 * <p>修改履历：</p>
	 */
	public static String get(String url){
		HttpGet httpGet = new HttpGet(url);
		//发送request请求
		HttpResponse response = sendRequest(httpClient, httpGet);
		return parseResponse(response);
	}
	/*** 
	 * 方法名 : post
	 * <p>Description: post请求</p>
	 * @author yu.zhang
	 * @date 2017年11月16日 下午4:43:22
	 * @version 1.0 
	 * @param url
	 * @param params
	 * @return String
	 * <p>修改履历：</p>
	 */
	public static String post(String url,Map<String,String> params){
		HttpPost httpPost = postForm(url, params);
		HttpResponse response = sendRequest(httpClient, httpPost);
		return parseResponse(response);
	}
	/*** 
	 * 方法名 : postForm
	 * <p>Description: 组装post请求form表单</p>
	 * @author yu.zhang
	 * @date 2017年11月16日 下午5:08:48
	 * @version 1.0 
	 * @param url
	 * @return HttpPost
	 * <p>修改履历：</p>
	 */
	private static HttpPost postForm(String url,Map<String,String> params) {
		HttpPost httpPost;
		try {
			httpPost = new HttpPost(url);
			List<NameValuePair> nvps = new ArrayList<NameValuePair>();
			//组装参数
			Set<String> keys = params.keySet();
			for (String key : keys) {
				nvps.add(new BasicNameValuePair(key, params.get(key)));
			}
			httpPost.setEntity(new UrlEncodedFormEntity(nvps, DEFAULT_CHARSET));
			return httpPost;
		} catch (UnsupportedEncodingException e) {
			System.out.println("组装post请求form表单异常:" + e);
		}
		return null;
	}
	/*** 
	 * 方法名 : sendRequest
	 * <p>Description: 发送request请求</p>
	 * @author yu.zhang
	 * @date 2017年11月16日 下午5:28:32
	 * @version 1.0 
	 * @return HttpResponse
	 * <p>修改履历：</p>
	 */
	public static HttpResponse sendRequest(HttpClient httpClient,HttpUriRequest request) {
		try {
			return httpClient.execute(request);
		} catch (IOException e) {
			System.out.println("发送request请求异常:" + e);
		}
		return null;
	}
	/*** 
	 * 方法名 : parseResponse
	 * <p>Description: 解析response获取body</p>
	 * @author yu.zhang
	 * @date 2017年11月16日 下午5:19:10
	 * @version 1.0 
	 * @param response
	 * @return String
	 * <p>修改履历：</p>
	 */
	@SuppressWarnings("deprecation")
	private static String parseResponse(HttpResponse response){
		//打印状态码
		System.out.println("状态码：" + response.getStatusLine());
		String body = EMPTY_STRING; 
		try {
			//解析response拿到body
			HttpEntity entity = response.getEntity();
			body = EntityUtils.toString(entity, DEFAULT_CHARSET);
			return body;
		} catch (ParseException | IOException e) {
			System.out.println("解析response获取body异常:" + e);
		}finally{
			//关闭连接
			if(httpClient.getConnectionManager() != null) {
				httpClient.getConnectionManager().shutdown();
			}
		}
		return null;
	}
}
