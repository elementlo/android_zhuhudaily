package com.qf.teach.project.zhihudaily.util;

import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class NetUtil {
private static final int TIMEOUT = 5000;
	
	/**
	 * 获取JSON字符串
	 * @param uri
	 * @return
	 */
	public static String getJson(String uri) {
		String json = null;
		try {
			HttpEntity httpEntity = requestGet(uri);
			if (httpEntity != null) {
				json = EntityUtils.toString(httpEntity, "UTF-8");
			}
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return json;
	}
	
	/**
	 * 获取Bitmap
	 * @param uri
	 * @return
	 */
	public static Bitmap getBitmap(String uri) {
		Bitmap bitmap = null;
		try {
			HttpEntity httpEntity = requestGet(uri);
			if (httpEntity != null) {
				InputStream is = httpEntity.getContent();
				bitmap = BitmapFactory.decodeStream(is);
			}
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return bitmap;
	}
	
	/**
	 * Get请求
	 * @param uri
	 * @return
	 */
	private static HttpEntity requestGet(String uri) {
		HttpEntity httpEntity = null;
		
		try {
			HttpClient httpClient = new DefaultHttpClient();
			HttpParams httpParams = httpClient.getParams();
			HttpConnectionParams.setConnectionTimeout(httpParams, TIMEOUT);
			HttpGet httpGet = new HttpGet(uri);
			HttpResponse httpResponse = httpClient.execute(httpGet);
			if (httpResponse != null && httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				httpEntity = httpResponse.getEntity();
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return httpEntity;
	}
}
