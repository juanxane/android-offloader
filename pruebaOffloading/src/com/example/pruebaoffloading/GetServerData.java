package com.example.pruebaoffloading;


import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import android.os.AsyncTask;

public class GetServerData extends AsyncTask<String, Void, String> {
	
	private static final String SERVER_URL = "http://192.168.1.103:8083";
	
	@Override
	protected String doInBackground(String... execParams) {
		String res = "-2";
		try {
			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost(SERVER_URL);
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("n", execParams[0]));
			UrlEncodedFormEntity entity = new UrlEncodedFormEntity(nameValuePairs);
			httpPost.setEntity(entity);
			HttpResponse responseData = httpClient.execute(httpPost);
			
			InputStream instream = responseData.getEntity().getContent();
            res = Utils.convertStreamToString(instream);
		} catch (Exception e) {
			e.printStackTrace();
			
		}
		finally {
			return res;
		}
	}
}
