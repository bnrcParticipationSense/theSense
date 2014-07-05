package com.bupt.bnrc.thesenser.utils;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.File;
import java.net.ContentHandler;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONException;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

public class Upload {
	
	static public JSONObject Uploading(Context app, String uploadUrl, JSONObject sendObj) {
		//HttpPost post = new HttpPost(uploadUrl);
		HttpPost post = new HttpPost("http://10.108.108.11/uploadjson.php");
		//HttpPost post = new HttpPost("http://10.108.107.92:8080/uploadFile/fileServlet");
		JSONObject receiveObj = null;
		
		HttpClient httpClient;
		HttpParams httpParameters;
		
		HttpResponse httpResponse;
		HttpEntity entity;
		
		httpParameters = new BasicHttpParams();// Set the timeout in milliseconds until a connection is established.  
	    HttpConnectionParams.setConnectionTimeout(httpParameters, 1000);// Set the default socket timeout (SO_TIMEOUT) // in milliseconds which is the timeout for waiting for data.  
	    HttpConnectionParams.setSoTimeout(httpParameters, 5000);  
		httpClient = new DefaultHttpClient(httpParameters);
		
		try
		{	
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("upload",sendObj.toString()));
			post.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));

			// �����ゆ�烽����ゆ��POST�����ゆ�烽����ゆ��
			httpResponse = httpClient.execute(post);
			entity = httpResponse.getEntity();
			
			int resCode = httpResponse.getStatusLine().getStatusCode();
			String resReason = httpResponse.getStatusLine().getReasonPhrase();
			
			if(resCode != 200)
			{
				receiveObj = new JSONObject();
				try {
					receiveObj.put("StatusCode", resCode);
					receiveObj.put("ReasonPhrase", resReason);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Toast.makeText(app, "Server response error!" + "\nreponse code: "+resCode, Toast.LENGTH_SHORT).show();
				Log.i("Upload", "error:"+resReason);
			}
			else
			{
				Toast.makeText(app, "SUCCESS", Toast.LENGTH_LONG).show();
				Log.i("Upload", "SUCCESS");
			}
			
			if (entity != null)
			{
					
				String rev = EntityUtils.toString(entity);
				try {
					receiveObj = new JSONObject(rev); 
				} catch(Exception e) {
					e.printStackTrace();
				}
				Log.i("UploadJSON", "return msg:"+receiveObj.toString());
				return receiveObj;
			}
			else if(receiveObj != null){
				return receiveObj;
			}
			else {
				return null;
			}

		}
		catch (ConnectTimeoutException e)
		{
			e.printStackTrace();
			Toast.makeText(app, "Time out", Toast.LENGTH_LONG).show();
			Log.i("UploadJSON", "time out");
			for(int i=0 ; i<3 ; i++) {
				try{
					httpResponse = httpClient.execute(post);
					entity = httpResponse.getEntity();
					
					int resCode = httpResponse.getStatusLine().getStatusCode();
					
					if(entity != null) {
						receiveObj = new JSONObject(EntityUtils.toString(entity));
						break;
					}
					
					if(resCode == 200) {
						break;
					}
				} catch(Exception ex) {
					ex.printStackTrace();
				}
			}
			return receiveObj;
		}
		catch (IOException e)
		{
			e.printStackTrace();
			return null;
		}
	}
	
	
	static public void Uploading(Context app, String uploadUrl, String fileName) {
		String end = "\r\n";
	    String twoHyphens = "--";
	    String boundary = UUID.randomUUID().toString();
	    
	    try
	    {
	    	
	    	//URL url = new URL(uploadUrl);
	    	// URL url = new URL("http://10.108.108.11/upload11.php");
	    	URL url = new URL("http://10.108.107.92:8080/uploadFile/fileServlet");

	    	Log.i("Upload", "file = "+fileName);
	    	HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
	    	// �����ゆ�烽����ゆ�锋�����杞胯揪��烽����ゆ�烽����ゆ�烽����ゆ�烽����������烽����ゆ�烽����ゆ�烽����ゆ�烽���������烽��琛���告�峰�������ゆ�烽��杞匡拷������瀛�涓������ゆ�烽����ゆ�烽��锟�
	    	// �����垮�ゆ�烽����ゆ�烽����ゆ�烽����ゆ�烽����ゆ�烽�����楗鸿�ф�风�ラ����ゆ�烽����ゆ�烽����风�规�烽����ゆ�锋�堕����ゆ�烽����ゆ�锋病��������ゆ�烽����ゆ�烽�����璇ф�烽����ゆ�烽����ゆ�烽��锟� HTTP �����ゆ�烽����ゆ�烽����ゆ�烽��渚ョ����烽����ゆ�烽����ゆ��
	    	httpURLConnection.setReadTimeout(5 * 1000);
	    	httpURLConnection.setConnectTimeout(5 * 1000);
	    	//httpURLConnection.setChunkedStreamingMode(128 * 1024);// 128K
	    	// �����ゆ�烽����ゆ�烽����ゆ�烽����ゆ�烽����ゆ�烽����ゆ�烽��锟�
	    	httpURLConnection.setDoInput(true);
	    	httpURLConnection.setDoOutput(true);
	    	httpURLConnection.setUseCaches(false);
	    	// 浣块����ゆ��POST�����ゆ�烽����ゆ��
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
	    	 * �����ゆ�烽��渚ョ》��烽����ゆ�疯�����杈�杈炬��
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
	    	// �����ゆ�峰�����渚ョ》���
	    	while ((count = fis.read(buffer)) != -1)
	    	{
	    		dos.write(buffer, 0, count);
	    	}
	    	fis.close();

	    	dos.writeBytes(end);
	    	dos.writeBytes(twoHyphens + boundary + twoHyphens + end);
	    	dos.flush();

	    	/*
	    	 * �����ゆ�峰�������ゆ�烽����ゆ�烽����ゆ�烽����ゆ��
	    	 */
	    	InputStream inputStream = httpURLConnection.getInputStream();
	    	InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
	    	BufferedReader reader = new BufferedReader(inputStreamReader);
	    	String inputLine = null;
	    	String result = "";
	    	while((inputLine = reader.readLine()) != null) {
	    		result += inputLine + "\n";
	    	}
	    	reader.close();
	    	/*
	    	 * �����ゆ�峰�������ゆ�峰�������ゆ��
	    	 */
	    	int res = httpURLConnection.getResponseCode();
	    	String s = httpURLConnection.getResponseMessage();
	    	
	    	if(res == 200)
	    	{
	    		Toast.makeText(app, "SUCCESS", Toast.LENGTH_LONG).show();
	    		Log.i("Upload", "SUCCESS");
	    	}
	    	if(s != null){
	    		Log.i("UploadJEPG",s);
	    	}
	    	
	    	Log.i("Upload", result);
	      	dos.close();
	      

	    } catch (Exception e)
	    {
	    	e.printStackTrace();
	    }
	}
	
	private JSONObject ReUpload(String uploadUrl, JSONObject sendObj) {
		JSONObject receiveObj = null;
		return receiveObj;
	}
	
	//***********************With Out Activity**************************
	static public JSONObject Uploading(String uploadUrl, JSONObject sendObj) {
		HttpPost post = new HttpPost(uploadUrl);
		JSONObject receiveObj = null;
		
		HttpClient httpClient;
		HttpParams httpParameters;
		
		HttpResponse httpResponse;
		HttpEntity entity;
		
		httpParameters = new BasicHttpParams();// Set the timeout in milliseconds until a connection is established.  
	    HttpConnectionParams.setConnectionTimeout(httpParameters, 20000);// Set the default socket timeout (SO_TIMEOUT) // in milliseconds which is the timeout for waiting for data.  
	    HttpConnectionParams.setSoTimeout(httpParameters, 25000);  
		httpClient = new DefaultHttpClient(httpParameters);
		
		try
		{	
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("upload",sendObj.toString()));
			post.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));

			// �����ゆ�烽����ゆ��POST�����ゆ�烽����ゆ��
			httpResponse = httpClient.execute(post);
			entity = httpResponse.getEntity();
			
			int resCode = httpResponse.getStatusLine().getStatusCode();
			String resReason = httpResponse.getStatusLine().getReasonPhrase();
			
			if(resCode != 200)
			{
				Log.i("Upload", "error");
				receiveObj = new JSONObject();
				try{
					receiveObj.put("StatusCode", resCode);
					receiveObj.put("ReasonPhrase", resReason);
				}catch(JSONException e) {
					e.printStackTrace();
				}
				return receiveObj;
			}
			else
			{
				Log.i("Upload", "SUCCESS");
			}
			
			if (entity != null)
			{
					
				String rev = EntityUtils.toString(entity);
				try {
					receiveObj = new JSONObject(rev); 
				} catch(Exception e) {
					e.printStackTrace();
				}
				Log.i("UploadJSON", "return msg:"+receiveObj.toString());
				return receiveObj;
			} else
				return null;

		}
		catch (ConnectTimeoutException e)
		{
			e.printStackTrace();
			Log.i("UploadJSON", "time out");
			for(int i=0 ; i<3 ; i++) {
				try{
					httpResponse = httpClient.execute(post);
					entity = httpResponse.getEntity();
					
					int resCode = httpResponse.getStatusLine().getStatusCode();
					
					if(entity != null) {
						receiveObj = new JSONObject(EntityUtils.toString(entity));
						break;
					}
					
					if(resCode == 200) {
						break;
					}
				} catch(Exception ex) {
					ex.printStackTrace();
				}
			}
			return receiveObj;
		}
		catch (IOException e)
		{
			e.printStackTrace();
			return null;
		}
	}
	
	
	static public JSONObject Uploading(String uploadUrl, String fileName) {
		String end = "\r\n";
	    String twoHyphens = "--";
	    String boundary = UUID.randomUUID().toString();
	    JSONObject receiveObj = null;
	    
	    try
	    {
	    	
	    	URL url = new URL(uploadUrl);
	    	//URL url = new URL("http://10.108.108.11/upload11.php");
	    	//URL url = new URL("http://10.108.105.190:8080/webInterface/fileServlet");
	    	Log.i("Upload", "file = "+fileName);
	    	HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
	    	// �����ゆ�烽����ゆ�锋�����杞胯揪��烽����ゆ�烽����ゆ�烽����ゆ�烽����������烽����ゆ�烽����ゆ�烽����ゆ�烽���������烽��琛���告�峰�������ゆ�烽��杞匡拷������瀛�涓������ゆ�烽����ゆ�烽��锟�
	    	// �����垮�ゆ�烽����ゆ�烽����ゆ�烽����ゆ�烽����ゆ�烽�����楗鸿�ф�风�ラ����ゆ�烽����ゆ�烽����风�规�烽����ゆ�锋�堕����ゆ�烽����ゆ�锋病��������ゆ�烽����ゆ�烽�����璇ф�烽����ゆ�烽����ゆ�烽��锟� HTTP �����ゆ�烽����ゆ�烽����ゆ�烽��渚ョ����烽����ゆ�烽����ゆ��
	    	httpURLConnection.setReadTimeout(5 * 1000);
	    	httpURLConnection.setConnectTimeout(5 * 1000);
	    	//httpURLConnection.setChunkedStreamingMode(128 * 1024);// 128K
	    	// �����ゆ�烽����ゆ�烽����ゆ�烽����ゆ�烽����ゆ�烽����ゆ�烽��锟�
	    	httpURLConnection.setDoInput(true);
	    	httpURLConnection.setDoOutput(true);
	    	httpURLConnection.setUseCaches(false);
	    	// 浣块����ゆ��POST�����ゆ�烽����ゆ��
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
	    	 * �����ゆ�烽��渚ョ》��烽����ゆ�疯�����杈�杈炬��
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
	    	// �����ゆ�峰�����渚ョ》���
	    	while ((count = fis.read(buffer)) != -1)
	    	{
	    		dos.write(buffer, 0, count);
	    	}
	    	fis.close();

	    	dos.writeBytes(end);
	    	dos.writeBytes(twoHyphens + boundary + twoHyphens + end);
	    	dos.flush();

	    	/*
	    	 * �����ゆ�峰�������ゆ�峰�������ゆ��
	    	 */
	    	int res = httpURLConnection.getResponseCode();
	    	if(res == 200)
	    	{
	    		//Toast.makeText(app, "SUCCESS", Toast.LENGTH_LONG).show();
	    		Log.i("Upload", "SUCCESS");
	    	}
	    	
	    	/*
	    	 * �����ゆ�峰�������ゆ�烽����ゆ�烽����ゆ�烽����ゆ��
	    	 */
	    	InputStream inputStream = httpURLConnection.getInputStream();
	    	InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
	    	BufferedReader reader = new BufferedReader(inputStreamReader);
	    	String inputLine = null;
	    	String result = "";
	    	while((inputLine = reader.readLine()) != null) {
	    		result += inputLine + "\n";
	    	}
	    	reader.close();
	    	
	    	Log.i("Upload", result);
	    	try{
	    		receiveObj = new JSONObject(result);
	    	}catch(JSONException e) {
	    		e.printStackTrace();
	    	}
	      	dos.close();
	      	
	      	return receiveObj;

	    } catch (Exception e)
	    {
	    	e.printStackTrace();
	    	return null;
	    }    
	}

}
