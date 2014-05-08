package com.ilyoan.mytrainresv.core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.os.AsyncTask;
import android.util.Log;

public class MyHttp {
	private static final String TAG = "MyTrainResv";
	private static String URL_LOGIN = "https://www.korail.com/servlets/hc.hc14100.sw_hc14111_i2Svt";
	private static String URL_SEARCH = "http://www.korail.com/servlets/pr.pr21100.sw_pr21111_i1Svt";

	public void login(String id, String pw) {
		Log.d(TAG, "MyHttp.login(" + id + ")");
		
		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("selInputFlg", "2"));
		nameValuePairs.add(new BasicNameValuePair("UserId", id));
		nameValuePairs.add(new BasicNameValuePair("UserPwd", pw));
		nameValuePairs.add(new BasicNameValuePair("hidMemberFlg", "1"));
		
		HttpTask httpTask = new HttpTask(Method.POST, URL_LOGIN, onLoginResponse);
		httpTask.setPostEntity(nameValuePairs);
		httpTask.execute();
	}
	
	private OnResponse onLoginResponse = new OnResponse() {
		@Override
		public void onResponse(int status, String content) {
			Log.d(TAG, "MyHttp.onLoginResponse - status: " + status);
			//Log.v(TAG, "MyHttp.onLoginResponse - content: " + content);
			if (content.contains("w_mem01106")) {				
				Log.i(TAG, "MyHttp.onLoginResponse - login succeeded");
				MyTrainResv.showToast("로그인 성공");				
			} else {
				Log.i(TAG, "MyHttp.onLoginResponse - login failed");
				MyTrainResv.showToast("로그인 실패");				
			}
		}
	};
	
	public void searchTrain(String stationFrom,
							String stationTo,
							String date,
							String timeFrom,
							boolean ktxOnly) {
		Log.d(TAG, "MyHttp.searchTrain() START");
		Log.v(TAG, "       stationFrom: " + stationFrom);
		Log.v(TAG, "         stationTo: " + stationTo);
		Log.v(TAG, "              date: " + date);
		Log.v(TAG, "          timeFrom: " + timeFrom);
		Log.v(TAG, "           ktxOnly: " + ktxOnly);
		
		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("txtGoStartCode", stationFrom));
		nameValuePairs.add(new BasicNameValuePair("txtGoEndCode", stationTo));		
		nameValuePairs.add(new BasicNameValuePair("txtGoAbrdDt", date));
		nameValuePairs.add(new BasicNameValuePair("txtGoHour", timeFrom));
		nameValuePairs.add(new BasicNameValuePair("selGoTrain", ktxOnly ? "00" : "05"));
		nameValuePairs.add(new BasicNameValuePair("txtPsgCnt1", "1"));
		nameValuePairs.add(new BasicNameValuePair("chkStnNm", "N"));
		nameValuePairs.add(new BasicNameValuePair("radJobId", "1"));
		
		HttpEntity param;
		try {
			param = new UrlEncodedFormEntity(nameValuePairs);
			String url = URL_SEARCH + "?" + getContentString(param);
			Log.d(TAG, "MyHttp.searchTrain - url: " + url);
			
			HttpTask httpTask = new HttpTask(Method.GET, URL_SEARCH, onSearchTrainResponse);
			httpTask.execute();
		} catch (UnsupportedEncodingException e) {
			Log.e(TAG, "HttpTask.searchTrain - exception: " + e.getMessage());
			e.printStackTrace();
		}
	}
	
	private OnResponse onSearchTrainResponse = new OnResponse() {
		@Override
		public void onResponse(int status, String content) {
			Log.d(TAG, "MyHttp.onSearchTrainResponse - status: " + status);
			Log.v(TAG, "MyHttp.onLoginResponse - content: " + content);			
		}
	};
		
	private static int getStatusCode(HttpResponse response) {
		return response.getStatusLine().getStatusCode();
	}
	
	private static String getContentString(HttpEntity entity) {
		BufferedReader br = null;
		StringBuilder sb = new StringBuilder();
		try {
			InputStream content = entity.getContent();
			br = new BufferedReader(new InputStreamReader(content));			
			String line;
			while ((line = br.readLine()) != null) {
				//Log.v(TAG, line);
				sb.append(line);
			}
		} catch (IllegalStateException e) {
			Log.e(TAG, "MyHttp.getContentString - exception: " + e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			Log.e(TAG, "MyHttp.getContentString - exception: " + e.getMessage());
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return sb.toString();
	}
		
	private interface OnResponse {
		public void onResponse(int status, String content);
	}
	
	public enum Method { GET, POST }
	
	private class HttpTask extends AsyncTask<String, Void, HttpResponse> {
		private Method method;
		private String url;
		private OnResponse onResponse = null;
		private HttpEntity postEntity = null;
		private int status;		
		private String contents;

		public HttpTask(Method method, String url, OnResponse onResponse) {
			this.method = method;
			this.url = url;
			this.onResponse = onResponse;
		}
		
		public void setPostEntity(List<NameValuePair> entities) {
			try {
				this.postEntity = new UrlEncodedFormEntity(entities);
			} catch (UnsupportedEncodingException e) {
				Log.e(TAG, "HttpTask.setPostEntity - exception: " + e.getMessage());
				e.printStackTrace();
			}
		}
		
		@Override
		protected HttpResponse doInBackground(String... params) {
			HttpClient httpClient = new DefaultHttpClient();
			HttpUriRequest request = null;
			HttpResponse response = null;	
			
			if (this.method == Method.GET) {
				request = new HttpGet(this.url);
			} else {
				HttpPost post = new HttpPost(this.url);
				if (this.postEntity != null) {
					post.setEntity(this.postEntity);	
				}				
				request = post;
			} 
			
			try {
				response = httpClient.execute(request);
				this.status = getStatusCode(response);
				this.contents = getContentString(response.getEntity());
			} catch (ClientProtocolException e) {
				Log.e(TAG, "HttpTask failed: " + e.getMessage());
				e.printStackTrace();
			} catch (IOException e) {
				Log.e(TAG, "HttpTask failed: " + e.getMessage());
				e.printStackTrace();
			}
			return response;
		}
		
		@Override
		protected void onPostExecute(HttpResponse response) {
			this.onResponse.onResponse(this.status, this.contents);
		}
	}
}
