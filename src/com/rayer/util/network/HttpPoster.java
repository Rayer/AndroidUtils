package com.rayer.util.network;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

public class HttpPoster {

	public interface HttpPosterListener {
		public void onClientProtocolException(ClientProtocolException e);
		public void onIOException(IOException e);
		public void onGetResult(String result);
	}
	
//	PostObject mPost;
//	HttpPosterListener mListener;
	
	public HttpPoster(){
//	HttpPoster(PostObject post, HttpPosterListener listener){
//		mPost = post;
//		mListener = listener;
	}
	
	
	public boolean post(PostObject post, HttpPosterListener listener) {
		if(post == null)
			return false;
		
		HttpGet get = new HttpGet(post.toString());
		ResponseHandler<String> handler = new BasicResponseHandler();
		HttpClient client = new DefaultHttpClient();
		
		try {
			String result = client.execute(get, handler);
			if(listener != null)
				listener.onGetResult(result);			
		} catch (ClientProtocolException e1) {
			if(listener != null)
				listener.onClientProtocolException(e1);
			e1.printStackTrace();
		} catch (IOException e1) {
			if(listener != null)
				listener.onIOException(e1);
			e1.printStackTrace();
		}
		
		return true;
	}

	
}
