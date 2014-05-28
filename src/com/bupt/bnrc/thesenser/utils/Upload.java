package com.bupt.bnrc.thesenser.utils;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.OutputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.File;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.UUID;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONException;

import android.util.Log;
import android.widget.Toast;

public class Upload {
	
	static public JSONObject Uploading(String uploadUrl, JSONObject sendObj) {
		//HttpPost post = new HttpPost(uploadUrl);
		HttpPost post = new HttpPost("http://10.108.108.11/uploadjson.php");
		JSONObject receiveObj;
		
		HttpClient httpClient;
		HttpParams httpParameters;
		
		httpParameters = new BasicHttpParams();// Set the timeout in milliseconds until a connection is established.  
	    HttpConnectionParams.setConnectionTimeout(httpParameters, 20000);// Set the default socket timeout (SO_TIMEOUT) // in milliseconds which is the timeout for waiting for data.  
	    HttpConnectionParams.setSoTimeout(httpParameters, 25000);  
		httpClient = new DefaultHttpClient(httpParameters);
		
		try
		{
			//String encoderJson = URLEncoder.encode(sendObj.toString(), HTTP.UTF_8);

	        //DefaultHttpClient httpClient = new DefaultHttpClient();
	        post.addHeader(HTTP.CONTENT_TYPE, "application/json");
	        StringEntity se = new StringEntity(sendObj.toString());//(encoderJson);
	        se.setContentType("text/json");
	        se.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
	        post.setEntity(se);

			// 发送POST请求
			HttpResponse httpResponse = httpClient.execute(post);
			HttpEntity entity = httpResponse.getEntity();
			
			int resCode = httpResponse.getStatusLine().getStatusCode();
			
			if(resCode != 200)
			{
				//Toast.makeText(app, "Server response error!" + "\nreponse code: "+resCode, Toast.LENGTH_SHORT).show();
				Log.i("Upload", "SUCCESS");
			}
			
			if (entity != null)
			{
					
				        String rev = EntityUtils.toString(entity);          
				        receiveObj = new JSONObject(rev);  			      
				        return receiveObj;
			}
			else {
				return null;
			}

		}
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}
	
	static public void Uploading(String uploadUrl, String fileName) {
		String end = "\r\n";
	    String twoHyphens = "--";
	    String boundary = UUID.randomUUID().toString();
	    
	    try
	    {
	    	
	    	//URL url = new URL(uploadUrl);
	    	URL url = new URL("http://10.108.108.11/upload11.php");
	    	Log.i("Upload", "file = "+fileName);
	    	HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
	    	// 设置每次传输的流大小，可以有效防止手机因为内存不足崩溃
	    	// 此方法用于在预先不知道内容长度时启用没有进行内部缓冲的 HTTP 请求正文的流。
	    	httpURLConnection.setReadTimeout(5 * 1000);
	    	httpURLConnection.setConnectTimeout(5 * 1000);
	    	//httpURLConnection.setChunkedStreamingMode(128 * 1024);// 128K
	    	// 允许输入输出流
	    	httpURLConnection.setDoInput(true);
	    	httpURLConnection.setDoOutput(true);
	    	httpURLConnection.setUseCaches(true);
	    	// 使用POST方法
	    	httpURLConnection.setRequestMethod("POST");
	    	//httpURLConnection.setRequestProperty("Content-type","Application/x-www-form-urlencoded");
	    	//httpURLConnection.connect();
	    	/*
	      	DataOutputStream out = new DataOutputStream(httpURLConnection.getOutputStream());
	      	String content = "username="+URLEncoder.encode("forgetzzy", "utf-8");
	      	out.writeBytes(content);
	      	out.flush();
	      	out.close();
	    	 */
	      
	    	httpURLConnection.setRequestProperty("Charset", "utf-8");
	    	httpURLConnection.setRequestProperty("connection", "keep-alive");
	    	httpURLConnection.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
	      

	    	/*
	    	 * 把文件包装上传
	    	 */
	    	//OutputStream outputStream = new DataOutputStream(httpURLConnection.getOutputStream());
	      
	    	File file = new File(fileName);
	      
	    	DataOutputStream dos = new DataOutputStream(httpURLConnection.getOutputStream());
	    	dos.writeBytes(twoHyphens + boundary + end);
	    	dos.writeBytes("Content-Disposition: form-data; name=\"upfile\"; filename=\"" + file.getName() + "\"" + end);
	    	dos.writeBytes(end);

	    	FileInputStream fis = new FileInputStream(file);
	    	byte[] buffer = new byte[8192*1024]; // 8k
	    	int count = 0;
	    	// 读取文件
	    	while ((count = fis.read(buffer)) != -1)
	    	{
	    		dos.write(buffer, 0, count);
	    	}
	    	fis.close();

	    	dos.writeBytes(end);
	    	dos.writeBytes(twoHyphens + boundary + twoHyphens + end);
	    	dos.flush();

	    	/*
	    	 * 获取响应码
	    	 */
	    	int res = httpURLConnection.getResponseCode();
	    	if(res == 200)
	    	{
	    		//Toast.makeText(this, "SUCCESS", Toast.LENGTH_LONG).show();
	    		Log.i("Upload", "SUCCESS");
	    	}
	    	InputStream is = httpURLConnection.getInputStream();
	    	InputStreamReader isr = new InputStreamReader(is, "utf-8");
	    	BufferedReader br = new BufferedReader(isr);
	    	String result = br.readLine();

	    	//Toast.makeText(this, result, Toast.LENGTH_LONG).show();
	    	Log.i("Upload", result);
	      	dos.close();
	    	is.close();
	      

	    } catch (Exception e)
	    {
	    	e.printStackTrace();
	    }
	}

}
